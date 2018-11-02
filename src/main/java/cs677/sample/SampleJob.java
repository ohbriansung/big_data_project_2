package cs677.sample;

import cs677.misc.FileCreator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.time.Instant;

public class SampleJob {
  public static void main(String[] args) {
    try {
      Configuration conf = new Configuration();

      /* Setup */
      Job job = Job.getInstance(conf, "sample job");
      job.setJarByClass(SampleJob.class);

      /* Mapper */
      job.setMapperClass(SampleMapper.class);
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(NullWritable.class);

      /* Reducer */
      job.setNumReduceTasks(0);

      /* Input path */
      FileInputFormat.addInputPath(job, new Path(args[0]));

      /* Output path */
      Path outPath = FileCreator.findEmptyPath(conf, args[1]);
      FileOutputFormat.setOutputPath(job, outPath);

      /* Wait job to complete */
      boolean completed = job.waitForCompletion(true);
      System.out.println(Instant.now());
      System.exit(completed ? 0 : 1);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
