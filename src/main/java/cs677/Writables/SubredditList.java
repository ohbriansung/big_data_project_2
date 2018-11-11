package cs677.Writables;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Writable;
import org.json.JSONArray;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class SubredditList extends ArrayWritable {

  public SubredditList() {
    super(SubredditWritable.class);
  }

  public SubredditList(Writable[] values) {
    super(SubredditWritable.class, values);
  }

  @Override
  public void write(DataOutput out) throws IOException {
    super.write(out);
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    super.readFields(in);
  }

  @Override
  public Writable[] get() {
    return (super.get());
  }

  @Override
  public Object toArray() {
    Writable[] writables = super.get();
    SubredditWritable[] subreddits = new SubredditWritable[writables.length];
    for (int i = 0; i < writables.length; i++) {
      subreddits[i] = (SubredditWritable) writables[i];
    }
    return subreddits;
  }

  JSONArray toJsonArray() {
    JSONArray jsonArray = new JSONArray();
    Writable[] writables = super.get();
    for (Writable writable : writables) {
      jsonArray.put(((SubredditWritable) writable).toJsonObject());
    }
    return jsonArray;
  }


  @Override
  public String toString() {
    Writable[] writables = super.get();
    SubredditWritable[] subreddits = new SubredditWritable[writables.length];
    for (int i = 0; i < writables.length; i++) {
      subreddits[i] = (SubredditWritable) writables[i];
    }
    StringBuilder sb = new StringBuilder();
    sb.append("Liked SubredditWritable: \n");
    for (SubredditWritable writable : subreddits) {
      sb.append("\t");
      sb.append(writable.getSubreddit());
      sb.append(":");
      sb.append(writable.getCount());
      sb.append("\n");
    }
    sb.append("}");
    return sb.toString();
  }
}
