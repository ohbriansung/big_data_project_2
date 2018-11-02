package cs677.commentcount;

import cs677.misc.FileCreator;
import cs677.recordcount.RecordCountJob;
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

public class UserJob {

  public static void main(String[] args) {
    if (args.length != 3) {
      System.out.println("Args: <input> <output> <user>");
      System.out.println(-1);
    }

    try {
      Configuration conf = new Configuration();

      conf.setStrings("author", args[3]);

      Job job = Job.getInstance(conf, "user upvotes job");
      job.setJarByClass(RecordCountJob.class);

      /* Mapper */
      job.setMapperClass(UpsCountMapper.class);

      /* Reducer */
      job.setReducerClass(UpsCountReducer.class);

      /* Inputs and Outputs */
      job.setMapOutputKeyClass(IntWritable.class);
      job.setMapOutputValueClass(Text.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(IntWritable.class);

      /* Job input path in HDFS */
      FileInputFormat.addInputPath(job, new Path(args[0]));

      /* Job output path in HDFS. */
      Path outPath = FileCreator.findEmptyPath(conf, args[1]);
      FileOutputFormat.setOutputPath(job, outPath);

      /* Wait (block) for the job to complete... */
      boolean completed = job.waitForCompletion(true);
      System.exit(completed ? 0 : 1);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  private class UpsCountMapper extends Mapper<LongWritable, Text, IntWritable, Text> {
    String author;

    @Override
    protected void setup(Context context) {
      Configuration conf = context.getConfiguration();
      author = conf.get("author", "");
    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {

      JSONObject post = new JSONObject(value.toString());
      int ups = post.getInt("ups");
      String body = post.getString("body");
      context.write(new IntWritable(ups), new Text(body));
    }
  }

  private class UpsCountReducer extends Reducer<IntWritable, Text, Text, IntWritable> {
    @Override
    protected void reduce(IntWritable key, Iterable<Text> values, Context context)
        throws IOException, InterruptedException {
      for (Text val : values) {
        context.write(val, key);
      }
    }
  }
}
