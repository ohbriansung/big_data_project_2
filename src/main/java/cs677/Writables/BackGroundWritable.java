package cs677.Writables;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.json.JSONObject;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

public class BackGroundWritable implements WritableComparable<BackGroundWritable> {

  IntWritable upvotes = new IntWritable(0);
  DoubleWritable readability_score = new DoubleWritable(0);
  Text location = new Text("");
  Text likes = new Text("");
  IntWritable commentcount = new IntWritable(0);
  SubredditList subreddits = new SubredditList();
  DoubleWritable toxic_score = new DoubleWritable(0);
  Text user = new Text("");

  public BackGroundWritable() {};

  public BackGroundWritable(
      String user,
      double tox,
      int upvotes,
      double rscore,
      String location,
      int commentcount,
      String like,
      ArrayList<SubredditWritable> subreddits) {
    this.upvotes = new IntWritable(upvotes);
    this.readability_score = new DoubleWritable(rscore);
    this.location = new Text(location);
    this.likes = new Text(like);
    this.commentcount = new IntWritable(commentcount);
    this.toxic_score = new DoubleWritable(tox);
    this.subreddits = new SubredditList(subreddits.toArray(new SubredditWritable[0]));
    this.user = new Text(user);
  }

  public void append(double tox, int upvotes, double rscore, String location, int commentcount) {
    this.upvotes = new IntWritable(this.upvotes.get() + upvotes);
    this.toxic_score = new DoubleWritable(this.toxic_score.get() + tox);
    this.readability_score = new DoubleWritable(rscore + this.readability_score.get());
    if (!location.equals("none")) this.location = new Text(location);
    this.commentcount = new IntWritable(this.commentcount.get() + commentcount);
  }

  public void updateList(ArrayList<SubredditWritable> subreddits) {
    this.subreddits = new SubredditList(subreddits.toArray(new SubredditWritable[0]));
  }

  @Override
  public int compareTo(BackGroundWritable o) {
    return o.commentcount.compareTo(this.commentcount);
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    upvotes.write(dataOutput);
    readability_score.write(dataOutput);
    location.write(dataOutput);
    likes.write(dataOutput);
    commentcount.write(dataOutput);
    subreddits.write(dataOutput);
    toxic_score.write(dataOutput);
    user.write(dataOutput);
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    upvotes.readFields(dataInput);
    readability_score.readFields(dataInput);
    location.readFields(dataInput);
    likes.readFields(dataInput);
    commentcount.readFields(dataInput);
    subreddits.readFields(dataInput);
    toxic_score.readFields(dataInput);
    user.readFields(dataInput);
  }

  public Text getLikes() {
    return likes;
  }

  public DoubleWritable getReadability_score() {
    return readability_score;
  }

  public IntWritable getCommentcount() {
    return commentcount;
  }

  public IntWritable getUpvotes() {
    return upvotes;
  }

  public DoubleWritable getToxic_score() {
    return toxic_score;
  }

  public Text getLocation() {
    return location;
  }

  public double getReadAvg(){
    return readability_score.get()/commentcount.get();
  }

  public  double getToxAvg(){
    return toxic_score.get()/commentcount.get();
  }

  public String toJsonString() {
    JSONObject jsonObject = new JSONObject();
    double rscore = readability_score.get() / (double) commentcount.get();
    double tscore = toxic_score.get() / (double) commentcount.get();
    if (Double.isNaN(rscore)) rscore = 0;
    if (Double.isNaN(tscore)) tscore = 0;

    jsonObject.put("User", user);
    jsonObject.put("Upvotes", (double) upvotes.get() / (double) commentcount.get());
    jsonObject.put("Education", rscore);
    jsonObject.put("Toxic", tscore);
    jsonObject.put("CommentCount", commentcount.get());
    jsonObject.put("Location", location);
    jsonObject.put("SubredditWritable", subreddits.toJsonArray());
    return jsonObject.toString();
  }

  public String readable() {
    return "____________________________________________\n"
        + "User: "
        + user
        + "Average upvotes: "
        + Double.toString((double) upvotes.get() / (double) commentcount.get())
        + "\n"
        + "Education level: "
        + Double.toString(readability_score.get() / (double) commentcount.get())
        + "\n"
        + "Toxic Level: "
        + Double.toString(toxic_score.get() / (double) commentcount.get())
        + "\n"
        + "Comments Written: "
        + Double.toString(commentcount.get())
        + "\n"
        + "Location: "
        + location
        + "\n"
        + subreddits.toString()
        + "\n"
        + "____________________________________________\n";
  }

  @Override
  public String toString() {
    return toJsonString();
  }
}
