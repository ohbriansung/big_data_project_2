package cs677.subredditscount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;

public class SubredditsCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    JSONObject obj = new JSONObject(value.toString());
    String sub = obj.getString("subreddit");
    context.write(new Text(sub), new IntWritable(1));
  }
}
