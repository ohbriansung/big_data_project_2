package cs677.Writables;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Writable;
import org.json.JSONArray;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Subredditlist extends ArrayWritable {

  public Subredditlist() {
    super(Subreddits.class);
  }

  public Subredditlist(Writable[] values) {
    super(Subreddits.class, values);
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
    Subreddits[] subreddits = new Subreddits[writables.length];
    for (int i = 0; i < writables.length; i++) {
      subreddits[i] = (Subreddits) writables[i];
    }
    return subreddits;
  }

  JSONArray toJsonArray() {
    JSONArray jsonArray = new JSONArray();
    Writable[] writables = super.get();
    for (Writable writable : writables) {
      jsonArray.put(((Subreddits) writable).toJsonObject());
    }
    return jsonArray;
  }



  @Override
  public String toString() {
    Writable[] writables = super.get();
    Subreddits[] subreddits = new Subreddits[writables.length];
    for (int i = 0; i < writables.length; i++) {
      subreddits[i] = (Subreddits) writables[i];
    }
    StringBuilder sb = new StringBuilder();
    sb.append("Liked Subreddits: \n");
    for (Subreddits writable : subreddits) {
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
