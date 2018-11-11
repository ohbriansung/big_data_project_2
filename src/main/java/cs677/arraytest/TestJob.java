package cs677.arraytest;

import cs677.Writables.TextCountMapWritable;
import cs677.Writables.TextCountWritable;
import cs677.common.Constants;
import cs677.common.FileCreator;
import cs677.common.TimerStuff;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
// yarn jar P2-1.0.jar cs677.arraytest.TestJob /samples/* /test/arraytest
public class TestJob {

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
      Job job = Job.getInstance(conf, "array test");
      job.setJarByClass(TestJob.class);

      /* Input path */
      FileInputFormat.addInputPath(job, new Path(input));
      System.out.println("Input path: " + input);

      /* Output path */
      Path outPath = FileCreator.findEmptyPath(conf, output);
      System.out.println("Output path: " + outPath.toString());
      FileOutputFormat.setOutputPath(job, outPath);

      /* Mapper */
      job.setMapperClass(ArrayTestMapper.class);
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(TextCountMapWritable.class);

      /* Reducer */
      job.setReducerClass(ArrayTestReducer.class);
      job.setOutputKeyClass(TextCountMapWritable.class);
      job.setOutputValueClass(NullWritable.class);

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

  private static class ArrayTestMapper
      extends Mapper<LongWritable, Text, Text, TextCountMapWritable> {
    private static final Text outKey = new Text("");

    private HashMap<String, Long> counts = new HashMap<>();

    public ArrayTestMapper() {}

    @Override
    protected void map(LongWritable key, Text value, Context context)
        throws InterruptedException, IOException {
      JSONObject jsonObject = new JSONObject(value.toString());

      String body = jsonObject.getString(Constants.BODY);
      StringTokenizer itr = new StringTokenizer(body);
      String token;
      long count;
      while (itr.hasMoreTokens()) {
        token = itr.nextToken().toLowerCase();
        count = counts.getOrDefault(token, 0L) + 1;
        counts.put(token, count);
      }

      TextCountMapWritable outVal = new TextCountMapWritable(counts);

      context.write(outKey, outVal);
    }
  }

  private static class ArrayTestReducer
      extends Reducer<Text, TextCountMapWritable, TextCountMapWritable, NullWritable> {
    private static NullWritable nullWritable = NullWritable.get();

    public ArrayTestReducer() {}

    @Override
    protected void reduce(Text key, Iterable<TextCountMapWritable> values, Context context)
        throws InterruptedException, IOException {

      for (TextCountMapWritable value : values) {
        context.write(value, nullWritable);
      }
    }
  }
}
