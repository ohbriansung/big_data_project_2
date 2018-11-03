package cs677.monthcount;

import cs677.common.StringLongReducer;
import cs677.common.FileCreator;
import cs677.common.TimerStuff;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.time.Duration;
import java.time.Instant;

public class MonthJob {

  public static void main(String[] args) {
    System.out.println("{\"args\": [");
    for (String arg : args) {
      System.out.println("\"" + arg + "\", ");
    }
    System.out.println("]}");
    try {
      Instant t1 = Instant.now();
      Configuration conf = new Configuration();

      /* Job Name. You'll see this in the YARN webapp */
      Job job = Job.getInstance(conf, "month job");

      /* Current class */
      job.setJarByClass(MonthJob.class);

      /* Mapper */
      job.setMapperClass(MonthMapper.class);
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(LongWritable.class);

      /* Combiner */
      job.setCombinerClass(StringLongReducer.class);

      /* Reducer */
      job.setReducerClass(StringLongReducer.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(LongWritable.class);

      /* Input path */
      FileInputFormat.addInputPath(job, new Path(args[0]));
      System.out.println("Input path: " + args[0]);

      /* Output path */
      Path outPath = FileCreator.findEmptyPath(conf, args[1]);
      System.out.println("Output path: " + outPath.toString());
      FileOutputFormat.setOutputPath(job, outPath);

      /* Wait (block) for the job to complete... */
      boolean completed = job.waitForCompletion(true);
      Instant t2 = Instant.now();
      System.out.println(TimerStuff.formatDuration(Duration.between(t1, t2)));
      System.exit(completed ? 0 : 1);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
