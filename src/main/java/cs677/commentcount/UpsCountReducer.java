package cs677.commentcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class UpsCountReducer extends Reducer<IntWritable, Text, Text, IntWritable> {
  public UpsCountReducer() {}

  @Override
  protected void reduce(IntWritable key, Iterable<Text> values, Context context)
      throws IOException, InterruptedException {
    for (Text val : values) {
      context.write(val, key);
    }
  }
}
