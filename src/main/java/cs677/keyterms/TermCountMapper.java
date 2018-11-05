package cs677.keyterms;

import cs677.Writables.TextCountWritable;
import cs677.common.Constants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.StringTokenizer;

/** Mapper: Reads line by line, split them into words. Emit <word, 1> pairs. */
public class TermCountMapper extends Mapper<LongWritable, Text, Text, TextCountWritable> {

  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    JSONObject obj = new JSONObject(value.toString());

    String subreddit = obj.getString(Constants.SUBREDDIT);

    StringTokenizer itr = new StringTokenizer(value.toString());
    while (itr.hasMoreTokens()) {
      context.write(new Text(itr.nextToken()), new TextCountWritable(subreddit, 1L));
    }
  }
}
