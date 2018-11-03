package cs677.subredditscount;

import cs677.common.StringIntReducer;
import cs677.common.FileCreator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/** This is the main class. Hadoop will invoke the main method of this class. */
public class SubredditsJob {
  public static void main(String[] args) {
    try {
      Configuration conf = new Configuration();

      /* Job Name. You'll see this in the YARN webapp */
      Job job = Job.getInstance(conf, "record count job");

      /* Current class */
      job.setJarByClass(SubredditsJob.class);

      /* Mapper class */
      job.setMapperClass(SubredditsCountMapper.class);

      /* Combiner class */
      job.setCombinerClass(StringIntReducer.class);

      /* Reducer class */
      job.setReducerClass(StringIntReducer.class);

      /* Outputs from the Mapper. */
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(IntWritable.class);

      /* Outputs from the Reducer */
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(IntWritable.class);

      /* Input path */
      FileInputFormat.addInputPath(job, new Path(args[0]));

      /* Output path */
      Path outPath = FileCreator.findEmptyPath(conf, args[1]);
      FileOutputFormat.setOutputPath(job, outPath);

      /* Wait (block) for the job to complete... */
      boolean completed = job.waitForCompletion(true);
      if (completed) {
        SubredditsCountPost.parseOutput(conf, outPath);
      }
      System.exit(completed ? 0 : 1);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
