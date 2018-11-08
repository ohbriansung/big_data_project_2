package cs677.Writables;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class SubreddtTimeWritable implements WritableComparable<SubreddtTimeWritable> {

  Text subreddit = new Text();
  LongWritable time = new LongWritable();

  public SubreddtTimeWritable() {}

  public SubreddtTimeWritable(String subreddit, long time) {
    this.subreddit.set(subreddit);
    this.time.set(time);
  }

  public String getSubreddit() {
    return subreddit.toString();
  }

  public long getTime() {
    return time.get();
  }

  @Override
  public int hashCode() {
    int hash = subreddit.hashCode();
    hash *= 31;
    hash += time.hashCode();
    return hash;
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    subreddit.write(dataOutput);
    time.write(dataOutput);
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    subreddit.readFields(dataInput);
    time.readFields(dataInput);
  }

  @Override
  public int compareTo(SubreddtTimeWritable other) {
    int compared = this.time.compareTo(other.time);
    if (compared != 0) return compared;
    return this.subreddit.compareTo(other.subreddit);
  }
}
