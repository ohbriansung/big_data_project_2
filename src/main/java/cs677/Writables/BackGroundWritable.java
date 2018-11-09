package cs677.Writables;

import org.apache.hadoop.io.*;

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
    Subredditlist subreddits = new Subredditlist();

    public BackGroundWritable(){};
    public BackGroundWritable(int upvotes, double rscore, String location, int commentcount,String like, ArrayList<Subreddits> subreddits){
        this.upvotes = new IntWritable(upvotes);
        this.readability_score = new DoubleWritable(rscore);
        this.location = new Text(location);
        this.likes = new Text(like);
        this.commentcount = new IntWritable(commentcount);
        this.subreddits = new Subredditlist(subreddits.toArray(new Subreddits[0]));
    }

    public void append(int upvotes,double rscore,String location, int commentcount){
        this.upvotes = new IntWritable(this.upvotes.get() + upvotes);

        this.readability_score = new DoubleWritable(rscore + this.readability_score.get());
        if(!location.equals("none"))
            this.location = new Text(location);
        this.commentcount = new IntWritable(this.commentcount.get() + commentcount);
    }

    public void updateList(ArrayList<Subreddits> subreddits){
        this.subreddits = new Subredditlist(subreddits.toArray(new Subreddits[0]));
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
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        upvotes.readFields(dataInput);
        readability_score.readFields(dataInput);
        location.readFields(dataInput);
        likes.readFields(dataInput);
        commentcount.readFields(dataInput);
        subreddits.readFields(dataInput);
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
                "Average education level: " +  Double.toString(readability_score.get()) + "\n" +
                "Comments Written: " + Double.toString(commentcount.get()) + "\n" +
                "Location: " + location + "\n" +
                "Subreddits: " + subreddits.toString() + "\n";
    }
}

