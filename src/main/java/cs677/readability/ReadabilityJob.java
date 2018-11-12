package cs677.readability;

import cs677.common.FileCreator;
import cs677.common.TimerStuff;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.time.Duration;
import java.time.Instant;
// yarn jar P2-1.0.jar cs677.readability.ReadabilityJob /data/2012/RC_2012*2*.bz2 /test/read

// yarn jar P2-1.0.jar cs677.readability.ReadabilityJob /data/20* /out/read
// yarn jar P2-1.0.jar cs677.readability.ReadabilityJob /data/20* /out/read skyrim
public class ReadabilityJob {
  static final String SUB_PARSING_KEY = "subParse";
  static final String ALL_SUBS_KEY = "allSubs";

  public static void main(String[] args) {
    if (args.length < 2 || args.length > 3) {
      System.out.println("required args: <input_path> <output_path> <optional-subreddit>");
      System.exit(-1);
    }
    String input = args[0];
    String output = args[1];
    boolean allSubs = false;
    String subParse = "";
    if (args.length == 2) {
      allSubs = true;
      output += "_all_subs";
    }
    if (args.length == 3) {
      subParse = args[2];
      output += "_" + subParse;
    }
    try {
      Instant t1 = Instant.now();

      Configuration conf = new Configuration();

      conf.setBoolean(ALL_SUBS_KEY, allSubs);
      conf.set(SUB_PARSING_KEY, subParse);

      /* Setup */
      Job job = Job.getInstance(conf, "readability " + subParse);
      job.setJarByClass(ReadabilityJob.class);

      /* Input path */
      FileInputFormat.addInputPath(job, new Path(input));
      System.out.println("Input path: " + input);

      /* Output path */
      Path outPath = FileCreator.findEmptyPath(conf, output);
      System.out.println("Output path: " + outPath.toString());
      FileOutputFormat.setOutputPath(job, outPath);

      /* Mapper */
      job.setMapperClass(ReadabilityMapper.class);
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(SenWorSylWritable.class);

      /* Reducer */
      if (args.length == 2) {
        job.setReducerClass(ReadabilityAvgReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FleschWritable.class);
      } else {
        job.setReducerClass(ReadabilityNullReducer.class);
        job.setOutputKeyClass(FleschWritable.class);
        job.setOutputKeyClass(NullWritable.class);
      }

      /* Wait job to complete */
      boolean completed = job.waitForCompletion(true);
      System.out.println(Instant.now());
      System.out.println("Input path: " + input);
      System.out.println("Output path: " + outPath.toString());

      Instant t2 = Instant.now();
      System.out.println("Time Taken: " + TimerStuff.formatDuration(Duration.between(t1, t2)));

      System.exit(completed ? 0 : 1);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
