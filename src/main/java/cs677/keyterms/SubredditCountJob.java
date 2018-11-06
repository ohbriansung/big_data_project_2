package cs677.keyterms;

import cs677.common.FileCreator;
import cs677.common.TimerStuff;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
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
import java.util.HashSet;
// yarn jar P2-1.0.jar cs677.keyterms.SubredditCountJob /test/termcount_01 /test/termsubcount
public class SubredditCountJob {

  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("required args: <input_path> <output_path>");
      System.exit(-1);
    }
    String input = args[0];
    String output = args[1];
    try {
      Instant t1 = Instant.now();

      Configuration conf = new Configuration();

      /* Setup */
      Job job = Job.getInstance(conf, "term subcount job");
      job.setJarByClass(SubredditCountJob.class);

      /* Input path */
      FileInputFormat.addInputPath(job, new Path(input));
      System.out.println("Input path: " + input);

      /* Output path */
      Path outPath = FileCreator.findEmptyPath(conf, output);
      System.out.println("Output path: " + outPath.toString());
      FileOutputFormat.setOutputPath(job, outPath);

      /* Mapper */
      job.setMapperClass(SubredditMapper.class);
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(Text.class);
      job.setInputFormatClass(KeyValueTextInputFormat.class);

      /* Reducer */
      job.setReducerClass(SubredditReducer.class);
      job.setOutputKeyClass(IntWritable.class);
      job.setOutputValueClass(NullWritable.class);

      /* Wait job to complete */
      boolean completed = job.waitForCompletion(true);
      System.out.println(Instant.now());
      System.out.println("Input path: " + input);
      System.out.println("Output path: " + outPath.toString());

      Instant t2 = Instant.now();
      System.out.println("Time Taken: " + TimerStuff.formatDuration(Duration.between(t1, t2)));

      /* Post parse */

      System.exit(completed ? 0 : 1);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  private static class SubredditMapper extends Mapper<Text, Text, Text, Text> {
    private static Text outKey = new Text("");

    public SubredditMapper() {}

    @Override
    protected void map(Text key, Text value, Context context)
        throws IOException, InterruptedException {
      try {
        JSONObject jsonObject = new JSONObject(value.toString());
        HashSet<String> subreddits = new HashSet<>(jsonObject.keySet());
        for (String sub : subreddits) {
          context.write(outKey, new Text(sub));
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  private static class SubredditReducer extends Reducer<Text, Text, IntWritable, NullWritable> {
    public SubredditReducer() {}

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
        throws IOException, InterruptedException {
      HashSet<Text> subs = new HashSet<>();
      for (Text value : values) {
        subs.add(value);
      }
      context.write(new IntWritable(subs.size()), NullWritable.get());
    }
  }
}
