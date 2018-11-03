package cs677.screamers;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class DoubleTextFlipReducer extends Reducer<DoubleWritable, Text, Text, DoubleWritable> {
  @Override
  public void reduce(DoubleWritable key, Iterable<Text> value, Context context)
      throws IOException, InterruptedException {
    for (Text val : value) {
      context.write(val, key);
    }
  }
}
