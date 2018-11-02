package cs677.monthstudies;

import cs677.misc.YearMonthWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MonthReducer
    extends Reducer<YearMonthWritable, IntWritable, YearMonthWritable, LongWritable> {
  @Override
  protected void reduce(YearMonthWritable key, Iterable<IntWritable> values, Context context)
      throws IOException, InterruptedException {
    long count = 0;
    // calculate the total count
    for (IntWritable val : values) {
      count += val.get();
    }
    context.write(key, new LongWritable(count));
  }
}
