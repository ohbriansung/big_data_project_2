package cs677.keyterms;

import cs677.Writables.TextCountWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TermCountCombiner extends Reducer<Text, TextCountWritable, Text, TextCountWritable> {

  private TextCountWritable outVal = new TextCountWritable();

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

    for (Map.Entry<String, Long> entry : subs.entrySet()) {
      outVal.setCount(entry.getValue());
      outVal.setText(entry.getKey());
      context.write(key, outVal);
    }
  }
}
