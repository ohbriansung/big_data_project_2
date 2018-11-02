package cs677.monthcount;

import cs677.misc.YearMonthWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class MonthMapper extends Mapper<LongWritable, Text, YearMonthWritable, LongWritable> {
  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    JSONObject obj = new JSONObject(value.toString());
    long seconds;
    try {
      String timeString = obj.getString("created_utc");
      seconds = Long.parseLong(timeString);
    } catch (JSONException e) {
      try {
        seconds = obj.getLong("created_utc");
      } catch (JSONException err) {
        System.out.println(e.getMessage());
        System.out.println(value.toString());
        return;
      }
    }
    LocalDateTime dateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);

    YearMonthWritable out_key = new YearMonthWritable(dateTime.getYear(), dateTime.getMonthValue());

    context.write(out_key, new LongWritable(1));
  }
}
