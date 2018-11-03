package cs677.common;

import cs677.Writables.CountTotalWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class CountTotalReducer extends Reducer<Text, CountTotalWritable, Text, CountTotalWritable> {

  @Override
  protected void reduce(Text key, Iterable<CountTotalWritable> values, Context context)
      throws IOException, InterruptedException {
    long count = 0;
    long total = 0;
    for (CountTotalWritable val : values) {
      count += val.getCount();
      total += val.getTotal();
    }
    context.write(key, new CountTotalWritable(count, total));
  }
}
