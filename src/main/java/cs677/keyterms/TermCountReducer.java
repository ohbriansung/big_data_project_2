package cs677.keyterms;

import cs677.Writables.TextCountWritable;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TermCountReducer extends Reducer<Text, TextCountWritable, Text, ArrayWritable> {
  @Override
  protected void reduce(Text key, Iterable<TextCountWritable> values, Context context)
      throws InterruptedException, IOException {
    HashMap<String, Long> subs = new HashMap<>();
    for (TextCountWritable value : values) {
      long count = value.getCount();
      String text = value.getText();
      count += subs.getOrDefault(text, 0L);
      subs.put(text, count);
    }

    List<TextCountWritable> textCountWritableList = new LinkedList<>();

    for (Map.Entry<String, Long> entry : subs.entrySet()) {
      textCountWritableList.add(new TextCountWritable(entry.getKey(), entry.getValue()));
    }

    ArrayWritable outVal =
        new ArrayWritable(
            TextCountWritable.class, textCountWritableList.toArray(new TextCountWritable[0]));
    context.write(key, outVal);
  }
}
