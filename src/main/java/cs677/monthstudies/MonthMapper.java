package cs677.monthstudies;

import cs677.misc.YearMonthWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class MonthMapper extends Mapper<LongWritable, Text, YearMonthWritable, IntWritable> {
  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    JSONObject obj = new JSONObject(value.toString());
    String timeString = obj.getString("created_utc");
    long seconds = Long.parseLong(timeString);
    LocalDateTime dateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);

    YearMonthWritable out_key = new YearMonthWritable(dateTime.getYear(), dateTime.getMonthValue());

    context.write(out_key, new IntWritable(1));
  }
}
