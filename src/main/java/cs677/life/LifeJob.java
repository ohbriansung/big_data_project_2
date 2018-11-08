package cs677.life;

import cs677.Writables.SubreddtTimeWritable;
import cs677.Writables.TextCountWritable;
import cs677.common.Constants;
import cs677.common.FileCreator;
import cs677.common.TimerStuff;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

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

      //      /* Mapper */
      job.setMapperClass(LifeMapper.class);
      job.setMapOutputKeyClass(SubreddtTimeWritable.class);
      job.setMapOutputValueClass(TextCountWritable.class);
      //
      //      /* Reducer */
      job.setReducerClass(LifeReducer.class);
      job.setOutputKeyClass(SubreddtTimeWritable.class);
      job.setOutputValueClass(TextCountWritable.class);

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

  private static class LifeMapper
      extends Mapper<LongWritable, Text, SubreddtTimeWritable, TextCountWritable> {
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
        String timeString = jsonObject.getString(Constants.CREATED_UTC);
        seconds = Long.parseLong(timeString);
      } catch (JSONException e) {
        try {
          seconds = jsonObject.getLong(Constants.CREATED_UTC);
        } catch (JSONException err) {
          System.out.println(e.getMessage());
          System.out.println(value.toString());
          return;
        }
      }
      SubreddtTimeWritable outKey = new SubreddtTimeWritable(subreddit, seconds);

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

      String firstSentence = body.substring(0, cutIndex);

      SentenceUpvotesWritable sentenceUpvotes =
          new SentenceUpvotesWritable(firstSentence, (long) ups);
      context.write(outKey, sentenceUpvotes);
    }
  }

  private static class LifeReducer
      extends Reducer<
          SubreddtTimeWritable,
          SentenceUpvotesWritable,
          SubreddtTimeWritable,
          SentenceUpvotesWritable> {
    public LifeReducer() {}

    @Override
    protected void reduce(
        SubreddtTimeWritable key, Iterable<SentenceUpvotesWritable> values, Context context)
        throws InterruptedException, IOException {

      for (SentenceUpvotesWritable value : values) {
        context.write(key, value);
      }
    }
  }

  private static class SentenceUpvotesWritable extends TextCountWritable {

    public SentenceUpvotesWritable() {
      super();
    }

    public SentenceUpvotesWritable(String text, long upvotes) {
      super(text, upvotes);
    }

    @Override
    public String toString() {
      return "{\"ups\": " + getCount() + ", \"body\": \"" + getCount() + "\"}";
    }
  }
}
