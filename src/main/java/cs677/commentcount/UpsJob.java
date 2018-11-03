package cs677.commentcount;

import cs677.common.Constants;
import cs677.misc.FileCreator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

// nohup yarn jar ~/workplace/cs677/p2/P2-1.0.jar cs677.commentcount.UpsJob /data/2012/RC_2012-07.bz2 /out/ups qkme_transcriber > ups01 &
public class UpsJob {

  public static void main(String[] args) {

    if (args.length != 3) {
      System.out.println("Args: <input> <output> <user>");
      System.out.println(-1);
    }

    try {
      Configuration conf = new Configuration();

      conf.setStrings(Constants.AUTHOR, args[2]);
      System.out.println("Author: " + args[2]);

      Job job = Job.getInstance(conf, "ups job");
      job.setJarByClass(UpsJob.class);

      /* Mapper */
      job.setMapperClass(UpsCountMapper.class);

      /* Reducer */
      job.setReducerClass(UpsCountReducer.class);

      /* Inputs and Outputs */
      job.setMapOutputKeyClass(IntWritable.class);
      job.setMapOutputValueClass(Text.class);
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
      boolean completed = job.waitForCompletion(true);
      System.exit(completed ? 0 : 1);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
