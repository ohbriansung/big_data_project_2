package cs677.common;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import java.nio.ByteBuffer;

public class DoubleComparator extends WritableComparator {

  public DoubleComparator() {
    super(DoubleWritable.class);
  }

  @Override
  public int compare(WritableComparable a, WritableComparable b) {
    return super.compare(b, a);
  }
}
