package cs677.keyterms;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;

public class IdfReducer extends Reducer<Text, Text, Text, ArrayWritable> {
  @Override
  protected void reduce(Text key, Iterable<Text> values, Context context)
      throws InterruptedException, IOException {
    HashSet<Text> subs = new HashSet<>();
    for (Text value : values) {
      subs.add(value);
    }
    ArrayWritable arrayWritable = new ArrayWritable(Text.class, subs.toArray(new Text[0]));
    context.write(key, arrayWritable);
  }
}
