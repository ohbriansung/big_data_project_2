package cs677.screamers;

import cs677.Writables.CountTotalWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ScreamerReducer extends Reducer<Text, CountTotalWritable, Text, DoubleWritable> {
  @Override
  protected void reduce(Text key, Iterable<CountTotalWritable> values, Context context)
      throws InterruptedException, IOException {
    long count = 0;
    long total = 0;
    for (CountTotalWritable val : values) {
      count += val.getCount();
      total += val.getTotal();
    }
    DoubleWritable outVal = new DoubleWritable(-1);
    if (total > 0) {
      outVal.set(((double) count) / total);
    }
    context.write(key, outVal);
  }
}
