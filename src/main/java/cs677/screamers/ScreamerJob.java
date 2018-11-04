package cs677.screamers;

import cs677.Writables.CountTotalWritable;
import cs677.common.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// yarn jar P2-1.0.jar cs677.screamers.ScreamerJob /data/2* /out/screamer_a author
// yarn jar P2-1.0.jar cs677.screamers.ScreamerJob /data/2* /out/screamer_s subreddit
public class ScreamerJob extends Configured implements Tool {

  static final String JSON_KEY = "jsonKey";

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");

  @Override
  public int run(String[] args) throws Exception {
    System.out.println("Input Args: {\"args\": [");
    for (String arg : args) {
      System.out.println("\"" + arg + "\", ");
    }
    System.out.println("]}");
    if (args.length != 3) {
      System.out.println("required args: <input_path> <output_path> <json_key>");
      System.exit(-1);
    }
    String jsonKey = args[2];
    if (!isValidJsonKey(jsonKey)) {
      System.out.println("Only valid json keys: " + Constants.AUTHOR + ", " + Constants.SUBREDDIT);
      System.exit(-1);
    }

    Instant t1 = Instant.now();

    /* Paths Setup */
    Configuration countConf = getConf();

    Path outPath = FileCreator.findEmptyPath(countConf, args[1]);
    String outPathTmp = outPath.toString() + "/tmp";
    String outPathFinal = outPath.toString() + "/final";

    JobControl jobControl = new JobControl("JobController");

    /* Job 1 */
    countConf.set(JSON_KEY, jsonKey);

    Job countJob = screamCountJob(countConf, args[0], outPathTmp);

    ControlledJob controlledCountJob = new ControlledJob(countConf);
    controlledCountJob.setJob(countJob);

    jobControl.addJob(controlledCountJob);

    /* Job 2 */
    Configuration flipConf = getConf();
    Job flipJob = flipperJob(flipConf, outPathTmp, outPathFinal);
    flipJob.setInputFormatClass(KeyValueTextInputFormat.class);

    ControlledJob controlledFlipJob = new ControlledJob(flipConf);
    controlledFlipJob.setJob(flipJob);
    controlledFlipJob.addDependingJob(controlledCountJob);
    jobControl.addJob(controlledFlipJob);

    /* Run */
    Thread jobRunnerThread = new Thread(jobControl);
    jobRunnerThread.start();

    while (!jobControl.allFinished()) {
      System.out.println(
          String.format(
              "%s: {\"waiting\": %d, \"ready\": %d, \"running\": %d, \"success\": %d, \"failed\": %d}",
              LocalDateTime.now().format(DATE_TIME_FORMATTER),
              jobControl.getWaitingJobList().size(),
              jobControl.getReadyJobsList().size(),
              jobControl.getRunningJobList().size(),
              jobControl.getSuccessfulJobList().size(),
              jobControl.getFailedJobList().size()));
      try {
        Thread.sleep(30000);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    Instant t2 = Instant.now();
    System.out.println("Time Taken: " + TimerStuff.formatDuration(Duration.between(t1, t2)));
    jobControl.stop();
    System.exit(0);
    return (countJob.waitForCompletion(true) ? 0 : 1);
  }

  public static void main(String[] args) {
    if (args.length != 3) {
      System.out.println("required args: <input_path> <output_path> <json_key>");
      System.exit(-1);
    }
    try {
      int exitStatus = ToolRunner.run(new Configuration(), new ScreamerJob(), args);
      System.exit(exitStatus);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  private static boolean isValidJsonKey(String key) {
    return key.equals(Constants.AUTHOR) || key.equals(Constants.SUBREDDIT);
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
