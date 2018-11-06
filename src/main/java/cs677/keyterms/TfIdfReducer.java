package cs677.keyterms;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class TfIdfReducer extends Reducer<DoubleWritable, Text, Text, DoubleWritable> {
  @Override
  protected void reduce(DoubleWritable key, Iterable<Text> values, Context context)
      throws InterruptedException, IOException {
    for (Text value : values) {
      context.write(value, key);
    }
  }
}
