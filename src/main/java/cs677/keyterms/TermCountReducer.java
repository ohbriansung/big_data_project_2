package cs677.keyterms;

import cs677.Writables.TextCountWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TermCountReducer extends Reducer<Text, TextCountWritable, Text, Text> {
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

    StringBuilder sb = new StringBuilder();
    sb.append("{");
    for (Map.Entry<String, Long> entry : subs.entrySet()) {
      sb.append("\"");
      sb.append(entry.getKey());
      sb.append("\": ");
      sb.append(entry.getValue());
      sb.append(", ");
    }
    sb.append("}");

    Text outVal = new Text(sb.toString());
    context.write(key, outVal);
  }
}
