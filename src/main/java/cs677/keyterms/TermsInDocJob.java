package cs677.keyterms;

import cs677.common.FileCreator;
import cs677.common.StringLongReducer;
import cs677.common.TimerStuff;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class TermsInDocJob {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("args required: <input> <output>");
      System.exit(-1);
    }
    String input = args[0];
    String output = args[1];
    try {
      Instant t1 = Instant.now();

      Configuration conf = new Configuration();
      Job job = Job.getInstance(conf, "terms in doc");
      job.setJarByClass(TermsInDocJob.class);

      /* Input path */
      FileInputFormat.addInputPath(job, new Path(input));
      System.out.println("Input path: " + input);

      /* Output path */
      Path outPath = FileCreator.findEmptyPath(conf, output);
      System.out.println("Output path: " + outPath.toString());
      FileOutputFormat.setOutputPath(job, outPath);

      /* Mapper */
      job.setMapperClass(TermsInDocMapper.class);
      job.setInputFormatClass(KeyValueTextInputFormat.class);
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(LongWritable.class);

      /* Reducer */
      job.setReducerClass(StringLongReducer.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(LongWritable.class);

      /* Wait job to complete */
      boolean completed = job.waitForCompletion(true);
      System.out.println(Instant.now());
      System.out.println("Input path: " + input);
      System.out.println("Output path: " + outPath.toString());

      Instant t2 = Instant.now();
      System.out.println("Time Taken: " + TimerStuff.formatDuration(Duration.between(t1, t2)));

      System.exit(completed ? 0 : 1);
    } catch (IOException | InterruptedException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private static class TermsInDocMapper extends Mapper<Text, Text, Text, LongWritable> {
    @Override
    protected void map(Text key, Text value, Context context)
        throws IOException, InterruptedException {
      JSONObject jsonObject = new JSONObject(value.toString());
      for (String document : jsonObject.keySet()) {
        context.write(new Text(document), new LongWritable(jsonObject.getLong(document)));
      }
    }
  }
}
