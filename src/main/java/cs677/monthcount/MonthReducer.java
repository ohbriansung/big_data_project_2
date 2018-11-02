package cs677.monthcount;

import cs677.misc.YearMonthWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MonthReducer
    extends Reducer<YearMonthWritable, LongWritable, YearMonthWritable, LongWritable> {
  @Override
  protected void reduce(YearMonthWritable key, Iterable<LongWritable> values, Context context)
      throws IOException, InterruptedException {
    long count = 0;
    // calculate the total count
    for (LongWritable val : values) {
      count += val.get();
    }
    context.write(key, new LongWritable(count));
  }
}
