package cs677.sample;

import cs677.misc.FileCreator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Random;

public class SampleJob {
  public static void main(String[] args) {
    try {
      Configuration conf = new Configuration();

      /* Job Name. You'll see this in the YARN webapp */
      Job job = Job.getInstance(conf, "record count job");
      /* Current class */
      job.setJarByClass(SampleJob.class);

      /* Mapper class */
      job.setMapperClass(SampleMapper.class);

      /* Outputs from the Mapper. */
      job.setMapOutputKeyClass(NullWritable.class);
      job.setMapOutputValueClass(Text.class);

      /* Job input path in HDFS */
      FileInputFormat.addInputPath(job, new Path(args[0]));

      /* Job output path in HDFS. */
      Path outPath = FileCreator.findEmptyPath(conf, args[1]);
      FileOutputFormat.setOutputPath(job, outPath);

      /* Wait (block) for the job to complete... */
      boolean completed = job.waitForCompletion(true);
      System.out.println(Instant.now());
      System.exit(completed ? 0 : 1);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  private class SampleMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
    private NullWritable out = NullWritable.get();
    private Random rand = new Random();

    @Override
    protected void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
      float chance = rand.nextFloat();

      if (chance >= 0.05) {
        return;
      }

      JSONObject obj = new JSONObject(value.toString());
      String timeString = obj.getString("created_utc");
      long seconds = Long.parseLong(timeString);
      LocalDateTime dateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);

      if (dateTime.getMonth() != Month.FEBRUARY) return;
      if (dateTime.getDayOfMonth() != 22) return;

      context.write(value, out);
    }
  }
}
