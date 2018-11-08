package cs677.life;

import cs677.common.FileCreator;
import cs677.common.TimerStuff;
import cs677.keyterms.TfIdfMapper;
import cs677.keyterms.TfIdfReducer;
import cs677.readability.ReadabilityJob;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.time.Duration;
import java.time.Instant;

public class LifeJob {
  static final String USER_KEY = "user_key";

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

      conf.set(USER_KEY, user);

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
//      job.setMapperClass(TfIdfMapper.class);
//      job.setMapOutputKeyClass(DoubleWritable.class);
//      job.setMapOutputValueClass(Text.class);
//
//      /* Reducer */
//      job.setReducerClass(TfIdfReducer.class);
//      job.setOutputKeyClass(Text.class);
//      job.setOutputValueClass(DoubleWritable.class);

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
}
