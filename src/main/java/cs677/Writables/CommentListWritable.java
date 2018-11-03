package cs677.Writables;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.TreeSet;

public class CommentListWritable implements WritableComparable<CommentListWritable> {
  private IntWritable count = new IntWritable(0);
  private TreeSet<CommentWritable> comments = new TreeSet<>();

  public CommentListWritable() {}

  public void addComment(CommentWritable commentWritable) {
    if (commentWritable == null) return;
    count = new IntWritable(count.get() + 1);
    if (comments.size() >= 3) {
      if (commentWritable.compareTo(comments.first()) > 0) {
        comments.pollFirst();
        comments.add(commentWritable);
      }
      return;
    }
    comments.add(commentWritable);
  }

  public void addComment(String comment, int upvotes) {
    this.addComment(new CommentWritable(comment, upvotes));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CommentListWritable that = (CommentListWritable) o;

    return this.comments.equals(that.comments);
  }

  @Override
  public int hashCode() {
    return comments.hashCode();
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    count.write(dataOutput);
    CommentWritable[] commentArray = new CommentWritable[comments.size()];
    new ArrayWritable(CommentWritable.class, comments.descendingSet().toArray(commentArray))
        .write(dataOutput);
  }

  @Override
  public int compareTo(CommentListWritable other) {
    if (other == null) return 1;
    if (this.count == null && other.count == null) return 0;
    if (this.count == null) return -1;
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
