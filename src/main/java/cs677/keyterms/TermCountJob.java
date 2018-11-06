package cs677.keyterms;

import cs677.Writables.TextCountWritable;
import cs677.common.FileCreator;
import cs677.common.TimerStuff;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.time.Duration;
import java.time.Instant;
// yarn jar P2-1.0.jar cs677.keyterms.TermCountJob /data/20* /out/termcount_subs_5per
public class TermCountJob {
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
      Job job = Job.getInstance(conf, "term count job");
      job.setJarByClass(TermCountJob.class);

      /* Input path */
      FileInputFormat.addInputPath(job, new Path(input));
      System.out.println("Input path: " + input);

      /* Output path */
      Path outPath = FileCreator.findEmptyPath(conf, output);
      System.out.println("Output path: " + outPath.toString());
      FileOutputFormat.setOutputPath(job, outPath);

      /* Mapper */
      job.setMapperClass(TermCountMapper.class);
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(TextCountWritable.class);

      /* Combiner */
      job.setCombinerClass(TermCountCombiner.class);

      /* Reducer */
      job.setReducerClass(TermCountReducer.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(Text.class);

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
}
