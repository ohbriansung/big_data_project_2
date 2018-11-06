package cs677.Writables;

import org.apache.hadoop.io.ArrayWritable;

public class TextCountArrayWritable extends ArrayWritable {

  public TextCountArrayWritable() {
    super(TextCountWritable.class);
  }

  public TextCountArrayWritable(TextCountWritable[] values) {
    super(TextCountWritable.class, values);
  }

  @Override
  public TextCountWritable[] get() {
    return (TextCountWritable[]) super.get();
  }

  @Override
  public String toString() {
    TextCountWritable[] values = get();
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    for (TextCountWritable writable : values) {
      sb.append("\"");
      sb.append(writable.getText());
      sb.append("\": ");
      sb.append(writable.getCount());
      sb.append(", ");
    }
    sb.append("}");
    return sb.toString();
  }
}
