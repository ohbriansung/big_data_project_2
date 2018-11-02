package cs677.commentcount;

import cs677.common.StringLongReducer;
import cs677.misc.FileCreator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class CommentCountJob {
  public static void main(String[] args) {
    try {
      JobControl jobControl = new JobControl("User-Comments job chain");
      Configuration countConf = new Configuration();
      Job countJob = Job.getInstance(countConf, "comment counting job");

      String input = args[0];
      String outputBase = FileCreator.findEmptyPath(countConf, args[1]).toString();
      String outPass1 = outputBase + "/pass1";
      String outPass2 = outputBase + "/pass2";
      FileInputFormat.addInputPath(countJob, new Path(input));
      FileOutputFormat.setOutputPath(countJob, new Path(outPass1));

      countJob.setMapperClass(UserCountMapper.class);
      countJob.setReducerClass(StringLongReducer.class);
      countJob.setCombinerClass(StringLongReducer.class);

      countJob.setOutputKeyClass(Text.class);
      countJob.setOutputValueClass(LongWritable.class);

      ControlledJob controlledCountJob = new ControlledJob(countConf);
      controlledCountJob.setJob(countJob);

//      https://coe4bd.github.io/HadoopHowTo/multipleJobsSingle/multipleJobsSingle.html

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
