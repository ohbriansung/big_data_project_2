package cs677.Toxic;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class ToxicMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {

  private HashMap<String, Integer> sentimentMap = new HashMap<>();

  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    JSONObject obj = new JSONObject(value.toString());
    String subreddit = obj.getString("subreddit");
    String body = obj.getString("body");

    double sentScore = 0;
    double count = 0;
    StringTokenizer itr = new StringTokenizer(body);
    makeMap();
    // emit word, count pairs.
    while (itr.hasMoreTokens()) {
      count += 1;
      sentScore += sentimentMap.getOrDefault(itr.nextToken(), 0);
    }

    context.write(new Text(subreddit), new DoubleWritable(sentScore / count));
  }

  private void makeMap() throws IOException {
    String line;
    InputStream in = getClass().getResourceAsStream("/AFINN-111.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    while ((line = reader.readLine()) != null) {
      String[] parts = line.split("\t");
      if (parts.length >= 2) {
        String key = parts[0];
        int value = Integer.parseInt(parts[1]);
        sentimentMap.put(key, value);
      } else {
        System.out.println("ignoring line: " + line);
      }
    }
  }
}
