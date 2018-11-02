package cs677.sample;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Random;

public class SampleMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
  private NullWritable out = NullWritable.get();
  private Random rand = new Random();

  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    float chance = rand.nextFloat();

    if (chance >= 0.05) {
      return;
    }

    JSONObject obj = new JSONObject(value.toString());
    String timeString = obj.getString("created_utc");
    long seconds = Long.parseLong(timeString);
    LocalDateTime dateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);

    if (dateTime.getMonth() != Month.FEBRUARY) return;
    if (dateTime.getDayOfMonth() != 22) return;

    context.write(value, out);
  }
}
