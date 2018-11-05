package cs677.Writables;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TextCountWritable implements Writable {
  LongWritable count = new LongWritable();
  Text text = new Text();

  public TextCountWritable() {}

  public TextCountWritable(String text, Long count) {
    this.text.set(text);
    this.count.set(count);
  }

  public String getText() {
    return text.toString();
  }

  public long getCount() {
    return count.get();
  }

  public void setCount(Long count) {
    this.count.set(count);
  }

  public void setText(String text) {
    this.text.set(text);
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    count.write(dataOutput);
    text.write(dataOutput);
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    count.readFields(dataInput);
    text.readFields(dataInput);
  }

  @Override
  public int hashCode() {
    int hashCode = count != null ? count.hashCode() : 0;
    hashCode = 31 * hashCode + (text != null ? text.hashCode() : 0);
    return hashCode;
  }

  @Override
  public String toString() {
    return "{\"" + text + "\": " + count + "}";
  }
}
