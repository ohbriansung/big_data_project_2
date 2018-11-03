package cs677.common;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class StringLongReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

  @Override
  protected void reduce(Text key, Iterable<LongWritable> values, Context context)
      throws IOException, InterruptedException {
    long count = 0;
    // calculate the total count
    for (LongWritable val : values) {
      count += val.get();
    }
    context.write(key, new LongWritable(count));
  }
}
