package cs677.monthstudies;

import cs677.misc.FileCreator;
import cs677.misc.YearMonthWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class MonthJob {

  public static void main(String[] args) {
    try {
      Configuration conf = new Configuration();

      /* Job Name. You'll see this in the YARN webapp */
      Job job = Job.getInstance(conf, "month mapper job");

      /* Current class */
      job.setJarByClass(MonthJob.class);

      /* Mapper class */
      job.setMapperClass(MonthMapper.class);

      /* Reducer class */
      job.setReducerClass(MonthReducer.class);

      /* Outputs from the Mapper. */
      job.setMapOutputKeyClass(YearMonthWritable.class);
      job.setMapOutputValueClass(IntWritable.class);

      /* Outputs from the Reducer */
      job.setOutputKeyClass(YearMonthWritable.class);
      job.setOutputValueClass(LongWritable.class);

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

  private class MonthMapper extends Mapper<LongWritable, Text, YearMonthWritable, IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {

      JSONObject obj = new JSONObject(value.toString());
      String timeString = obj.getString("created_utc");
      long seconds = Long.parseLong(timeString);
      LocalDateTime dateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);

      YearMonthWritable out_key =
          new YearMonthWritable(dateTime.getYear(), dateTime.getMonthValue());

      context.write(out_key, new IntWritable(1));
    }
  }

  private class MonthReducer
      extends Reducer<YearMonthWritable, IntWritable, YearMonthWritable, LongWritable> {
    @Override
    protected void reduce(YearMonthWritable key, Iterable<IntWritable> values, Context context)
        throws IOException, InterruptedException {
      long count = 0;
      // calculate the total count
      for (IntWritable val : values) {
        count += val.get();
      }
      context.write(key, new LongWritable(count));
    }
  }
}
