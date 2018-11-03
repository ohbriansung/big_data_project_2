package cs677.monthcount;

import cs677.misc.FileCreator;
import cs677.misc.YearMonthWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.time.Instant;

public class MonthJob {

  public static void main(String[] args) {
    try {
      Configuration conf = new Configuration();

      /* Job Name. You'll see this in the YARN webapp */
      Job job = Job.getInstance(conf, "month job");

      /* Current class */
      job.setJarByClass(MonthJob.class);

      /* Mapper */
      job.setMapperClass(MonthMapper.class);
      job.setMapOutputKeyClass(YearMonthWritable.class);
      job.setMapOutputValueClass(LongWritable.class);

      /* Combiner */
      job.setCombinerClass(MonthReducer.class);

      /* Reducer */
      job.setReducerClass(MonthReducer.class);
      job.setOutputKeyClass(YearMonthWritable.class);
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
      System.out.println(Instant.now());
      System.exit(completed ? 0 : 1);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
