package cs677.misc;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CommentWritable implements WritableComparable<CommentWritable> {
  private Text comment;
  private IntWritable upvotes;

  public CommentWritable() {
    this.comment = new Text();
    this.upvotes = new IntWritable();
  }

  public CommentWritable(String comment, int upvotes) {
    this.comment = new Text(comment);
    this.upvotes = new IntWritable(upvotes);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CommentWritable that = (CommentWritable) o;

    if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
    return upvotes != null ? upvotes.equals(that.upvotes) : that.upvotes == null;
  }

  @Override
  public int hashCode() {
    int result = comment != null ? comment.hashCode() : 0;
    result = 31 * result + (upvotes != null ? upvotes.hashCode() : 0);
    return result;
  }

  @Override
  public int compareTo(CommentWritable that) {
    return that.upvotes.compareTo(this.upvotes);
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    this.comment.write(dataOutput);
    this.upvotes.write(dataOutput);
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    this.comment.readFields(dataInput);
    this.upvotes.readFields(dataInput);
  }

  @Override
  public String toString() {
    return String.format(
        "{\"comment\": \"%s\", \"upvotes\": %s}", comment, upvotes.toString());
  }
}
