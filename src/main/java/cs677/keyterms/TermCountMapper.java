package cs677.keyterms;

import cs677.Writables.TextCountWritable;
import cs677.common.Constants;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;

public class TermCountMapper extends Mapper<LongWritable, Text, Text, TextCountWritable> {

  private Random random = new Random();

  private TextCountWritable outVal = new TextCountWritable("", 1L);
  private Text outKey = new Text();

  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    if (random.nextFloat() > 0.001) return;

    JSONObject obj = new JSONObject(value.toString());

    String subreddit = obj.getString(Constants.SUBREDDIT);

    StringTokenizer itr = new StringTokenizer(obj.getString(Constants.BODY));
    String token;
    outVal.setText(subreddit);
    while (itr.hasMoreTokens()) {
      token = itr.nextToken().toLowerCase().replaceAll(Constants.REGEX_ALL_PUNCTIATION, "");
      outKey.set(token);
      context.write(outKey, outVal);
    }
  }
}
