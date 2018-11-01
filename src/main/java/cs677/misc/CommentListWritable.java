package cs677.misc;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class CommentListWritable implements Writable {
  private int count = 0;
  private final CommentWritable[] comments = new CommentWritable[3];

  public CommentListWritable() {}

  public void addComment(CommentWritable commentWritable) {
    count += 1;
    CommentWritable tmp;
    for (int i = 0; i < comments.length; i++) {
      if (comments[i] == null) {
        comments[i] = commentWritable;
        return;
      }
      if (comments[i].compareTo(commentWritable) > 0) {
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
    new IntWritable(count).write(dataOutput);
    new ArrayWritable(CommentWritable.class, comments).write(dataOutput);
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    IntWritable intWritable = new IntWritable();
    intWritable.readFields(dataInput);
    count = intWritable.get();
    ArrayWritable me = new ArrayWritable(CommentWritable.class);
    int counter = 0;
    for (Writable writable : me.get()) { // iterate
      CommentWritable comment = (CommentWritable) writable; // cast
      comments[counter] = comment;
      counter += 1;
      if (counter >= 3) {
        return;
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\"count\": ");
    sb.append(count);
    sb.append(", \"comments\": [");
    for (CommentWritable commentWritable : comments) {
      sb.append(commentWritable.toString());
      sb.append(", ");
    }
    sb.append("]}");
    return sb.toString();
  }
}
