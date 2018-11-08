package cs677.life;

import cs677.common.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.json.JSONObject;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class LifeJob {
  private static final String AUTHOR_KEY = "user_key";

  public static void main(String[] args) {
    if (args.length != 3) {
      System.out.println("required args: <input_path> <output_path> <username>");
      System.exit(-1);
    }
    String input = args[0];
    String output = args[1];
    String user = args[2];

    try {
      Instant t1 = Instant.now();

      Configuration conf = new Configuration();

      conf.set(AUTHOR_KEY, user);

      /* Setup */
      Job job = Job.getInstance(conf, "Life " + user);
      job.setJarByClass(LifeJob.class);

      /* Input path */
      FileInputFormat.addInputPath(job, new Path(input));
      System.out.println("Input path: " + input);
      job.setInputFormatClass(KeyValueTextInputFormat.class);

      /* Output path */
      Path outPath = FileCreator.findEmptyPath(conf, output);
      System.out.println("Output path: " + outPath.toString());
      FileOutputFormat.setOutputPath(job, outPath);

      /* Mapper */
      job.setMapperClass(LifeMapper.class);
      job.setMapOutputKeyClass(LongWritable.class);
      job.setMapOutputValueClass(LifeWritable.class);

      /* Reducer */
      job.setReducerClass(LifeReducer.class);
      job.setOutputKeyClass(LongWritable.class);
      job.setOutputValueClass(LifeWritable.class);

      /* Wait job to complete */
      boolean completed = job.waitForCompletion(true);
      System.out.println(Instant.now());
      System.out.println("Input path: " + input);
      System.out.println("Output path: " + outPath.toString());

      Instant t2 = Instant.now();
      System.out.println("Time Taken: " + TimerStuff.formatDuration(Duration.between(t1, t2)));

      System.exit(completed ? 0 : 1);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  private static class LifeMapper extends Mapper<LongWritable, Text, LongWritable, LifeWritable> {
    public LifeMapper() {}

    @Override
    protected void map(LongWritable key, Text value, Context context)
        throws InterruptedException, IOException {
      JSONObject jsonObject = new JSONObject(value.toString());
      Configuration conf = context.getConfiguration();
      String authorFilter = conf.get(AUTHOR_KEY);

      String author = jsonObject.getString(Constants.AUTHOR);
      if (!author.equals(authorFilter)) return;
      String body = jsonObject.getString(Constants.BODY);
      String subreddit = jsonObject.getString(Constants.SUBREDDIT);
      int ups = jsonObject.getInt(Constants.UPS);

      long seconds;
      try {
        seconds = Util.getSeconds(jsonObject);
      } catch (Error e) {
        e.printStackTrace();
        return;
      }
      LongWritable outKey = new LongWritable(seconds);

      String firstSentence = getFirstSentence(body);

      SentimentScorer scorer = new SentimentScorer();
      double score = scorer.sentimentScore(body);
      LifeWritable lifeWritable = new LifeWritable(firstSentence, (long) ups, score, subreddit);

      context.write(outKey, lifeWritable);
    }

    private String getFirstSentence(String body) {
      int iP = body.indexOf('.');
      int iQ = body.indexOf('?');
      int iE = body.indexOf('!');
      int cutIndex = body.length() - 1;
      if (iP > 0 && iP < cutIndex) {
        cutIndex = iP;
      }
      if (iQ > 0 && iQ < cutIndex) {
        cutIndex = iQ;
      }
      if (iE > 0 && iE < cutIndex) {
        cutIndex = iE;
      }
      cutIndex += 1;

      return body.substring(0, cutIndex);
    }
  }

  private static class LifeReducer
      extends Reducer<LongWritable, LifeWritable, LifeWritable, NullWritable> {
    private static NullWritable nullWritable = NullWritable.get();

    public LifeReducer() {}

    @Override
    protected void reduce(LongWritable key, Iterable<LifeWritable> values, Context context)
        throws InterruptedException, IOException {

      for (LifeWritable value : values) {
        value.setTime(key.get());
        context.write(value, nullWritable);
      }
    }
  }

  private static class LifeWritable implements Writable {

    private final Text body = new Text();
    private final LongWritable upvotes = new LongWritable();
    private final DoubleWritable score = new DoubleWritable();
    private final Text subreddit = new Text();
    private final LongWritable time = new LongWritable();
    private LocalDateTime localDateTime;

    public LifeWritable() {}

    public LifeWritable(String body, long upvotes, double score, String subreddit) {
      this.body.set(body);
      this.upvotes.set(upvotes);
      this.score.set(score);
      this.subreddit.set(subreddit);
    }

    public void setTime(long time) {
      this.time.set(time);
      localDateTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC);
    }

    @Override
    public void write(DataOutput output) throws IOException {
      body.write(output);
      upvotes.write(output);
      score.write(output);
      subreddit.write(output);
      time.write(output);
    }

    @Override
    public void readFields(DataInput input) throws IOException {
      body.readFields(input);
      upvotes.readFields(input);
      score.readFields(input);
      subreddit.readFields(input);
      time.readFields(input);
      if (time.get() != 0L) {
        localDateTime = LocalDateTime.ofEpochSecond(time.get(), 0, ZoneOffset.UTC);
      }
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();

      sb.append("{");
      if (localDateTime != null) {
        sb.append("\"time\": \"");
        sb.append(localDateTime.format(DateTimeFormatter.ISO_INSTANT));
        sb.append("\", ");
      }
      sb.append("\"subreddit\": ");
      sb.append(subreddit);
      sb.append(", \"ups\": ");
      sb.append(upvotes);
      sb.append(", \"sentimentScore\": ");
      sb.append(score);
      sb.append(", \"body\": \"");
      sb.append(body);
      if (localDateTime != null) {
        sb.append("\", \"timeLong\": \"");
        sb.append(time);
      }
      sb.append("\"}");
      return sb.toString();
    }
  }
}
