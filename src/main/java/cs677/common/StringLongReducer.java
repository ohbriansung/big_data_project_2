package cs677.common;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class StringLongReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
  @Override
  public void reduce(Text key, Iterable<LongWritable> values, Context context)
      throws IOException, InterruptedException {
    long count = 0;
    for (LongWritable value : values) {
      count += value.get();
    }
    context.write(key, new LongWritable(count));
  }
}
