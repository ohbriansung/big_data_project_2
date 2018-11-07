package cs677.Writables;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class BackGroundKey implements WritableComparable<BackGroundKey> {


    DoubleWritable readability_score = new DoubleWritable(0);
    Text author = new Text("");
    DoubleWritable commentcount = new DoubleWritable(0);


    public BackGroundKey(){};
    public BackGroundKey(double rscore,double commentcount, String author){
        this.readability_score = new DoubleWritable(rscore);
        this.author = new Text(author);
        this.commentcount = new DoubleWritable(commentcount);
    }

    public void append(double rscore,double commentcount){
        this.readability_score = new DoubleWritable(rscore + this.readability_score.get());

        this.commentcount = new DoubleWritable(this.commentcount.get() + commentcount);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        author.write(dataOutput);
        readability_score.write(dataOutput);
        commentcount.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        author.readFields(dataInput);
        readability_score.readFields(dataInput);
        commentcount.readFields(dataInput);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BackGroundKey that = (BackGroundKey) o;
        return that.author.equals(this.author.toString());
    }

    @Override
    public int hashCode() {
        return author.hashCode();
    }

    @Override
    public int compareTo(BackGroundKey o) {
        return o.getReadability_score().compareTo(this.getReadability_score());
    }



    public DoubleWritable getReadability_score() {
        return new DoubleWritable(readability_score.get()/commentcount.get());
    }


    @Override
    public String toString() {
        return author.toString();
    }
}
