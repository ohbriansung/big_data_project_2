package cs677.commentcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;

public class UserCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
  private static LongWritable outVal = new LongWritable(1);

  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    JSONObject post = new JSONObject(value);
    String user = post.getString("user");
    context.write(new Text(user), outVal);
  }
}
