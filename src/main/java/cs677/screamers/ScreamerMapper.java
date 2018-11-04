package cs677.screamers;

import cs677.Writables.CountTotalWritable;
import cs677.common.Constants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

public class ScreamerMapper extends Mapper<LongWritable, Text, Text, CountTotalWritable> {

  private Random random = new Random();

  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    if (random.nextFloat() > 0.01) return;

    JSONObject obj = new JSONObject(value.toString());

    Configuration conf = context.getConfiguration();
    String jsonKey = conf.get(ScreamerJob.JSON_KEY);
    String filterKey = obj.getString(jsonKey);

    String body = obj.getString(Constants.BODY);
    long count = 0;
    long total = 0;
    // https://www.programiz.com/java-programming/examples/alphabet
    for (char c : body.toCharArray()) {
      if (c >= 'a' && c <= 'z') {
        // lowercase
        total += 1;
      }
      if (c >= 'A' && c <= 'Z') {
        // uppercase
        total += 1;
        count += 1;
      }
    }
    if (total > 0) context.write(new Text(filterKey), new CountTotalWritable(count, total));
  }
}
