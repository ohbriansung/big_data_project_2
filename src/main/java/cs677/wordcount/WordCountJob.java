package cs677.wordcount;

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
public class WordCountJob {
  public static void main(String[] args) {
    try {
      Configuration conf = new Configuration();

      /* Job Name. You'll see this in the YARN webapp */
      Job job = Job.getInstance(conf, "word count job");

      /* Current class */
      job.setJarByClass(WordCountJob.class);

      /* Mapper class */
      job.setMapperClass(WordCountMapper.class);

      /* Combiner */
      job.setCombinerClass(StringIntReducer.class);

      /* Reducer */
      job.setReducerClass(StringIntReducer.class);

      /* Outputs from the Mapper. */
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(IntWritable.class);

      /* Outputs from the Reducer */
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(IntWritable.class);

      /* Input path */
      FileInputFormat.addInputPath(job, new Path(args[0]));
      System.out.println("Input path: " + args[0]);

      /* Output path */
      Path outPath = FileCreator.findEmptyPath(conf, args[1]);
      System.out.println("Output path: " + outPath.toString());
      FileOutputFormat.setOutputPath(job, outPath);

      /* Wait (block) for the job to complete... */
      System.exit(job.waitForCompletion(true) ? 0 : 1);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
