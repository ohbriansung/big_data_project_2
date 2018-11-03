package cs677.screamers;

import cs677.Writables.CountTotalWritable;
import cs677.common.CountTotalReducer;
import cs677.common.DoubleComparator;
import cs677.common.FileCreator;
import cs677.common.TimerStuff;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class ScreamerJob {
  public static void main(String[] args) {
    System.out.println("Input Args: {\"args\": [");
    for (String arg : args) {
      System.out.println("\"" + arg + "\", ");
    }
    System.out.println("]}");
    if (args.length != 2) {
      System.out.println("required args: <input_path> <output_path>");
      System.exit(-1);
    }
    try {
      Instant t1 = Instant.now();

      JobControl jobControl = new JobControl("screamer controller");

      /* Paths Setup */

      Configuration conf = new Configuration();

      Path outPath = FileCreator.findEmptyPath(conf, args[1]);
      String outPathTmp = outPath.toString() + "/tmp";
      String outPathFinal = outPath.toString() + "/final";

      /* Job 1 */
      Configuration conf1 = new Configuration();
      Job countJob = screamCountJob(conf1, args[0], outPathTmp);

      ControlledJob cCountJob = new ControlledJob(conf1);
      cCountJob.setJob(countJob);
      jobControl.addJob(cCountJob);

      /* Job 2 */
      Configuration conf2 = new Configuration();

      Job flipJob = flipperJob(conf2, outPathTmp, outPathFinal);
      flipJob.setInputFormatClass(KeyValueTextInputFormat.class);

      ControlledJob cFlipJob = new ControlledJob(conf2);
      cFlipJob.setJob(flipJob);
      jobControl.addJob(cFlipJob);
      cFlipJob.addDependingJob(cFlipJob);

      /* Run */

      Thread jobControlThread = new Thread(jobControl);
      jobControlThread.start();

      while (!jobControl.allFinished()) {
        System.out.println("Jobs in waiting state: " + jobControl.getWaitingJobList().size());
        System.out.println("Jobs in ready state: " + jobControl.getReadyJobsList().size());
        System.out.println("Jobs in running state: " + jobControl.getRunningJobList().size());
        System.out.println("Jobs in success state: " + jobControl.getSuccessfulJobList().size());
        System.out.println("Jobs in failed state: " + jobControl.getFailedJobList().size());
        Thread.sleep(5000);
      }

      Instant t2 = Instant.now();
      System.out.println("Time Taken: " + TimerStuff.formatDuration(Duration.between(t1, t2)));
      System.exit(0);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  private static Job screamCountJob(Configuration conf, String input, String output)
      throws IOException {
    Job job = Job.getInstance(conf, "screamer screamCountJob");
    job.setJarByClass(ScreamerJob.class);

    /* Input path */
    FileInputFormat.addInputPath(job, new Path(input));
    System.out.println("Input path: " + input);

    /* Output path */
    FileOutputFormat.setOutputPath(job, new Path(output));
    System.out.println("Output path: " + output);

    /* Mapper */
    job.setMapperClass(ScreamerMapper.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(CountTotalWritable.class);

    /* Combiner */
    job.setCombinerClass(CountTotalReducer.class);

    /* Reducer */
    job.setReducerClass(ScreamerReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(DoubleWritable.class);

    return job;
  }

  private static Job flipperJob(Configuration conf, String input, String output)
      throws IOException {
    Job job = Job.getInstance(conf, "scream flipperJob");
    job.setJarByClass(ScreamerJob.class);

    /* Input path */
    FileInputFormat.addInputPath(job, new Path(input));
    System.out.println("Input path: " + input);

    /* Output path */
    FileOutputFormat.setOutputPath(job, new Path(output));
    System.out.println("Output path: " + output);

    /* Mapper */
    job.setMapperClass(TextDoubleFlipMapper.class);
    job.setMapOutputKeyClass(DoubleWritable.class);
    job.setMapOutputValueClass(Text.class);

    /* Reducer */
    job.setReducerClass(DoubleTextFlipReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(DoubleWritable.class);

    /* Sort */
    job.setSortComparatorClass(DoubleComparator.class);

    return job;
  }
}
