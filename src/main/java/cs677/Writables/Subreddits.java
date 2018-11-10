package cs677.Writables;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.json.JSONObject;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Subreddits implements WritableComparable<Subreddits> {
  private LongWritable count = new LongWritable();
  private Text subreddit = new Text();

  public Subreddits() {}

  public Subreddits(long count, String subreddit) {
    this.count = new LongWritable(count);
    this.subreddit = new Text(subreddit);
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    count.write(dataOutput);
    subreddit.write(dataOutput);
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    count.readFields(dataInput);
    subreddit.readFields(dataInput);
  }

  @Override
  public int compareTo(Subreddits o) {
    return o.count.compareTo(this.count);
  }

  @Override
  public boolean equals(Object obj) {
    Subreddits sub = (Subreddits) obj;
    return sub.getSubreddit().equals(this.subreddit);
  }

  @Override
  public int hashCode() {
    return subreddit.hashCode();
  }

  public Text getSubreddit() {
    return subreddit;
  }

  public LongWritable getCount() {
    return count;
  }

  @Override
  public String toString() {
    return subreddit.toString();
  }

  JSONObject toJsonObject() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put(subreddit.toString(), count.get());
    return jsonObject;
  }
}
