package cs677.ComplexWord;

import cs677.Writables.Readablity;
import cs677.common.TimerStuff;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.map.InverseMapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import java.time.Duration;
import java.time.Instant;

// yarn jar P2-1.0.jar cs677.ComplexWord.ComplexSpeakerJob rarepuppers /data/2012/*-01*
// /test/rare_pups
public class ComplexSpeakerJob {

  public static void main(String[] args) {
    try {
      Configuration conf = new Configuration();
      Instant t1 = Instant.now();
      conf.set("subreddit", args[0]);
      Job job = Job.getInstance(conf, "Readability");
      job.setJarByClass(ComplexSpeakerJob.class);

      job.setMapperClass(ComplexSpeakerMapper.class);
      job.setReducerClass(ComplexSpeakerReducer.class);

      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(Readablity.class);
      job.setOutputKeyClass(NullWritable.class);
      job.setOutputValueClass(Readablity.class);
      job.setOutputFormatClass(SequenceFileOutputFormat.class);

      FileInputFormat.addInputPath(job, new Path(args[1]));
      FileOutputFormat.setOutputPath(job, new Path(args[2] + "/temp"));

      job.waitForCompletion(true);
      Instant t2 = Instant.now();
      System.out.println("Time Taken: " + TimerStuff.formatDuration(Duration.between(t1, t2)));

      Configuration conf2 = new Configuration();
      Job job2 = Job.getInstance(conf2, "Reverse Map");

      job2.setInputFormatClass(SequenceFileInputFormat.class);
      job2.setJarByClass(ComplexSpeakerJob.class);

      job2.setMapperClass(InverseMapper.class);

      job2.setMapOutputKeyClass(Readablity.class);
      job2.setMapOutputValueClass(NullWritable.class);

      FileInputFormat.addInputPath(job2, new Path(args[2] + "/temp"));
      FileOutputFormat.setOutputPath(job2, new Path(args[2] + "/final"));

      job2.waitForCompletion(true);
      t2 = Instant.now();
      System.out.println(
          "Total Time Taken: " + TimerStuff.formatDuration(Duration.between(t1, t2)));
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
