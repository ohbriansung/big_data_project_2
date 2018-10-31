package cs677.recordcount;

import cs677.misc.FileCreator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/** This is the main class. Hadoop will invoke the main method of this class. */
public class RecordCountJob {
  public static void main(String[] args) {
    try {
      Configuration conf = new Configuration();
      /* Job Name. You'll see this in the YARN webapp */
      Job job = Job.getInstance(conf, "record count job");

      /* Current class */
      job.setJarByClass(RecordCountJob.class);

      /* Mapper class */
      job.setMapperClass(RecordCountMapper.class);

      /* Combiner class */
      // job.setCombinerClass(WordCountReducer.class);

      /* Reducer class */
      job.setReducerClass(RecordCountReducer.class);

      /* Outputs from the Mapper. */
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(IntWritable.class);

      /* Outputs from the Reducer */
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(IntWritable.class);

      /* Job input path in HDFS */
      FileInputFormat.addInputPath(job, new Path(args[0]));

      /* Job output path in HDFS. NOTE: if the output path already exists
       * and you try to create it, the job will fail. You may want to
       * automate the creation of new output directories here */
      FileOutputFormat.setOutputPath(job, FileCreator.findEmptyPath(conf, args[1]));

      /* Wait (block) for the job to complete... */
      System.exit(job.waitForCompletion(true) ? 0 : 1);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
