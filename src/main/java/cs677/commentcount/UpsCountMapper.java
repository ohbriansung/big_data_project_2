package cs677.commentcount;

import cs677.common.Constants;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;

public class UpsCountMapper extends Mapper<LongWritable, Text, IntWritable, Text> {
  String author;

  public UpsCountMapper() {}

  @Override
  protected void setup(Context context) {
    Configuration conf = context.getConfiguration();
    author = conf.get(Constants.AUTHOR, "");
  }

  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    JSONObject post = new JSONObject(value.toString());

    if (!post.getString(Constants.AUTHOR).equals(author)) return;

    int ups = post.getInt(Constants.UPS);
    String body = StringEscapeUtils.escapeJson(post.getString(Constants.BODY));
    context.write(new IntWritable(ups), new Text(body));
  }
}
