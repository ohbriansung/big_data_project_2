package cs677.Writables;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Writable;
import org.json.JSONObject;

import java.util.Map;

public class TextCountMapWritable extends ArrayWritable {

  public TextCountMapWritable() {
    super(TextCountWritable.class);
  }

  public TextCountMapWritable(Map<String, Long> map) {
    super(TextCountWritable.class);
    set(map);
  }

  @Override
  public void set(Writable[] values) {
    throw new java.lang.UnsupportedOperationException(
        "set(Writable[] values) is unsupported. Please use set(Map<String, Long) function.");
  }

  public void set(Map<String, Long> map) {
    TextCountWritable[] writables = new TextCountWritable[map.size()];
    int i = 0;
    for (Map.Entry<String, Long> entry : map.entrySet()) {
      writables[i] = new TextCountWritable(entry.getKey(), entry.getValue());
    }
    super.set(writables);
  }

  @Override
  public TextCountWritable[] get() {
    Writable[] writables = super.get();
    TextCountWritable[] out = new TextCountWritable[writables.length];
    for (int i = 0; i < writables.length; i++) {
      out[i] = (TextCountWritable) writables[i];
    }
    return out;
  }

  @Override
  public String toString() {
    TextCountWritable[] values = get();
    JSONObject jsonObject = new JSONObject();
    for (TextCountWritable writable : values) {
      jsonObject.put(writable.getText(), writable.getCount());
    }
    return jsonObject.toString();
  }
}
