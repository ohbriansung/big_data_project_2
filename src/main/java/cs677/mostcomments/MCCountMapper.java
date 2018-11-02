package cs677.mostcomments;

import cs677.misc.CommentWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;

public class MCCountMapper extends Mapper<LongWritable, Text, Text, CommentWritable> {
  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    JSONObject obj = new JSONObject(value.toString());
    String author = obj.getString("author");
    int ups = obj.getInt("ups");
    String body = obj.getString("body");

    context.write(
        new Text(author),
        new CommentWritable(body.substring(0, Math.min(100, body.length())), ups));
  }
}
