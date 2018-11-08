package cs677.Toxic;

import cs677.common.SentimentScorer;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;

public class ToxicMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {

  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    JSONObject obj = new JSONObject(value.toString());
    String subreddit = obj.getString("subreddit");
    String body = obj.getString("body");

    SentimentScorer scorer = new SentimentScorer();
    double score = scorer.sentimentScore(body);

    context.write(new Text(subreddit), new DoubleWritable(score));
  }
}
