package cs677.Writables;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CountTotalWritable implements Writable {

  LongWritable count = new LongWritable();
  LongWritable total = new LongWritable();

  public CountTotalWritable() {}

  public CountTotalWritable(long count, long total) {
    this.count.set(count);
    this.total.set(total);
  }

  public long getCount() {
    return count.get();
  }

  public long getTotal() {
    return total.get();
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    count.write(dataOutput);
    total.write(dataOutput);
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    count.readFields(dataInput);
    total.readFields(dataInput);
  }

  @Override
  public int hashCode() {
    int hash = count == null ? 0 : count.hashCode();
    hash *= 31;
    hash += total == null ? 0 : total.hashCode();
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\"count\": ");
    sb.append(count.get());
    sb.append(", \"total\": ");
    sb.append(total.get());
    sb.append("}");
    return sb.toString();
  }
}
