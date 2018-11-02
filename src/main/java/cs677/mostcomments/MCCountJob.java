package cs677.mostcomments;

import cs677.misc.CommentListWritable;
import cs677.misc.CommentWritable;
import cs677.misc.FileCreator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.time.Instant;

public class MCCountJob {
  public static void main(String[] args) {
    try {
      Configuration conf = new Configuration();

      /* Job Name. You'll see this in the YARN webapp */
      Job job = Job.getInstance(conf, "mc count job");
      /* Current class */
      job.setJarByClass(MCCountJob.class);

      /* Mapper class */
      job.setMapperClass(MCCountMapper.class);

      /* Reducer class */
      job.setReducerClass(MCCountReducer.class);

      /* Outputs from the Mapper. */
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(CommentWritable.class);

      /* Outputs from the Reducer */
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(CommentListWritable.class);

      /* Job input path in HDFS */
      FileInputFormat.addInputPath(job, new Path(args[0]));

      /* Job output path in HDFS. */
      Path outPath = FileCreator.findEmptyPath(conf, args[1]);
      FileOutputFormat.setOutputPath(job, outPath);

      /* Wait (block) for the job to complete... */
      boolean completed = job.waitForCompletion(true);
      if (completed) {
        MCCountPost.parseOutput(conf, outPath);
      }
      System.out.println(Instant.now());
      System.exit(completed ? 0 : 1);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
