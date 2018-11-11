package cs677.Writables;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Writable;
import org.json.JSONObject;

public class TextCountArrayWritable extends ArrayWritable {

  public TextCountArrayWritable() {
    super(TextCountWritable.class);
  }

  public TextCountArrayWritable(TextCountWritable[] values) {
    super(TextCountWritable.class, values);
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
