package cs677.Writables;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CommentWritable implements WritableComparable<CommentWritable> {
  private Text comment = new Text();
  private IntWritable upvotes = new IntWritable();

  public CommentWritable() {
  }

  public CommentWritable(String comment, int upvotes) {
    this.comment.set(comment);
    this.upvotes.set(upvotes);
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
    if (that == null) return 1;
    if (this.upvotes == null && that.upvotes == null) return 0;
    if (this.upvotes == null) return -1;
    return this.upvotes.compareTo(that.upvotes);
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
        "{\"comment\": \"%s\", \"upvotes\": %s}",
        StringEscapeUtils.escapeJava(comment.toString()), upvotes.toString());
  }
}
