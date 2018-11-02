package cs677.misc;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class CommentListWritable implements WritableComparable<CommentListWritable> {
  private IntWritable count = new IntWritable(0);
  private final CommentWritable[] comments = new CommentWritable[3];

  public CommentListWritable() {}

  public void addComment(CommentWritable commentWritable) {
    count = new IntWritable(count.get() + 1);
    CommentWritable tmp;
    for (int i = 0; i < comments.length; i++) {
      if (comments[i] == null) {
        comments[i] = commentWritable;
        return;
      }
      if (commentWritable.compareTo(comments[i]) > 0) {
        tmp = comments[i];
        comments[i] = commentWritable;
        commentWritable = tmp;
      }
    }
  }

  public void addComment(String comment, int upvotes) {
    this.addComment(new CommentWritable(comment, upvotes));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CommentListWritable that = (CommentListWritable) o;

    return Arrays.equals(this.comments, that.comments);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(comments);
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    count.write(dataOutput);
    new ArrayWritable(CommentWritable.class, comments).write(dataOutput);
  }

  @Override
  public int compareTo(CommentListWritable other) {
    if (other == null) return 1;
    return this.count.compareTo(other.count);
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    count.readFields(dataInput);
    ArrayWritable arr = new ArrayWritable(CommentWritable.class);
    for (Writable writable : arr.get()) { // iterate
      CommentWritable comment = (CommentWritable) writable;
      addComment(comment);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\"count\": ");
    sb.append(count);
    sb.append(", \"comments\": [");
    for (CommentWritable commentWritable : comments) {
      if (commentWritable != null) {
        sb.append(commentWritable.toString());
        sb.append(", ");
      }
    }
    sb.append("]}");
    return sb.toString();
  }
}
