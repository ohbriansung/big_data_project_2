package cs677.subdist;

import cs677.Writables.TextCountArrayWritable;
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
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
// yarn jar P2-1.0.jar cs677.subdist.SubDistJob /samples/* /test/subdist
// yarn jar P2-1.0.jar cs677.subdist.SubDistJob /data/20* /out/subdist
public class SubDistJob {
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
      Job job = Job.getInstance(conf, "subdist");
      job.setJarByClass(SubDistJob.class);

      /* Input path */
      FileInputFormat.addInputPath(job, new Path(input));
      System.out.println("Input path: " + input);

      /* Output path */
      Path outPath = FileCreator.findEmptyPath(conf, output);
      System.out.println("Output path: " + outPath.toString());
      FileOutputFormat.setOutputPath(job, outPath);

      /* Mapper */
      job.setMapperClass(SubDistMapper.class);
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(TextCountWritable.class);

      /* Reducer */
      job.setReducerClass(SubDistReducer.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(TextCountArrayWritable.class);

      /* Wait job to complete */
      boolean completed = job.waitForCompletion(true);
      System.out.println(Instant.now());
      System.out.println("Input path: " + input);
      System.out.println("Output path: " + outPath.toString());

      Instant t2 = Instant.now();
      System.out.println("Time Taken: " + TimerStuff.formatDuration(Duration.between(t1, t2)));

      System.exit(completed ? 0 : 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static class SubDistMapper extends Mapper<LongWritable, Text, Text, TextCountWritable> {
    public SubDistMapper() {}

    @Override
    protected void map(LongWritable key, Text value, Context context)
        throws InterruptedException, IOException {
      JSONObject jsonObject = new JSONObject(value.toString());
      String user = jsonObject.getString(Constants.AUTHOR);
      if (user.equals(Constants.DELETED)) return;
      String sub = jsonObject.getString(Constants.SUBREDDIT);
      context.write(new Text(sub), new TextCountWritable(user, 1));
    }
  }

  private static class SubDistReducer
      extends Reducer<Text, TextCountWritable, Text, TextCountArrayWritable> {
    public SubDistReducer() {}

    @Override
    protected void reduce(Text key, Iterable<TextCountWritable> values, Context context)
        throws InterruptedException, IOException {
      HashMap<String, Long> countMap = new HashMap<>();

      for (TextCountWritable value : values) {
        long count = value.getCount();
        String user = value.getText();
        count += countMap.getOrDefault(user, 0L);
        countMap.put(user, count);
      }

      ArrayList<TextCountWritable> textCountList = new ArrayList<>();

      countMap.forEach((k, v) -> textCountList.add(new TextCountWritable(k, v)));

      TextCountArrayWritable outVal =
          new TextCountArrayWritable(textCountList.toArray(new TextCountWritable[0]));

      context.write(key, outVal);
    }
  }
}
