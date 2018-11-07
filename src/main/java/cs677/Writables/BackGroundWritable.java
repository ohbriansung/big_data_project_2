package cs677.Writables;

import org.apache.hadoop.io.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class BackGroundWritable implements WritableComparable<BackGroundWritable> {

    IntWritable upvotes = new IntWritable(0);
    DoubleWritable readability_score = new DoubleWritable(0);
    Text location = new Text("");
    Text likes = new Text("");
    IntWritable commentcount = new IntWritable(0);

    public BackGroundWritable(){};
    public BackGroundWritable(int upvotes, double rscore, String location,String like, int commentcount){
        this.upvotes = new IntWritable(upvotes);
        this.readability_score = new DoubleWritable(rscore);
        this.location = new Text(location);
        this.likes = new Text(likes);
        this.commentcount = new IntWritable(commentcount);
    }

    public void append(int upvotes,double rscore,String location, int commentcount){
        this.upvotes = new IntWritable(this.upvotes.get() + upvotes);
        this.readability_score = new DoubleWritable(rscore + this.readability_score.get());
        if(!location.equals("none"))
            this.location = new Text(location);
        this.commentcount = new IntWritable(this.commentcount.get() + commentcount);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        upvotes.write(dataOutput);
        readability_score.write(dataOutput);
        location.write(dataOutput);
        likes.write(dataOutput);
        commentcount.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        upvotes.readFields(dataInput);
        readability_score.readFields(dataInput);
        location.readFields(dataInput);
        likes.readFields(dataInput);
        commentcount.readFields(dataInput);
    }



    @Override
    public int compareTo(BackGroundWritable o) {
        if (o.readability_score.compareTo(this.readability_score) > 0)
            return o.upvotes.compareTo(this.upvotes);
        else
            return this.upvotes.compareTo(o.upvotes);
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



    public Text getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Average upvotes: " + Double.toString((double) upvotes.get()/ (double)commentcount.get()) + "\n" +
                "Average education level: " +  Double.toString(readability_score.get()/(double) commentcount.get()) + "\n" +
                "Comments Written: " + Double.toString(commentcount.get()) + "\n" +
                "Location: " + location + "\n";
    }
}

