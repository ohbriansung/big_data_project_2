package cs677.monthcount;

import cs677.common.Constants;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class MonthMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
  private LongWritable out_value = new LongWritable(1);

  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    JSONObject obj = new JSONObject(value.toString());
    long seconds;
    try {
      String timeString = obj.getString(Constants.CREATED_UTC);
      seconds = Long.parseLong(timeString);
    } catch (JSONException e) {
      try {
        seconds = obj.getLong(Constants.CREATED_UTC);
      } catch (JSONException err) {
        System.out.println(e.getMessage());
        System.out.println(value.toString());
        return;
      }
    }
    LocalDateTime dateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);

    Text out_key = new Text(String.format("%04d-%02d", dateTime.getYear(), dateTime.getMonthValue()));

    context.write(out_key, out_value);
  }
}
