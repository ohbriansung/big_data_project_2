package cs677.Writables;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class UpperCount implements WritableComparable<UpperCount> {


    private DoubleWritable count = new DoubleWritable();
    private DoubleWritable uppercount = new DoubleWritable();


    public UpperCount(){

    }


    @Override
    public int compareTo(UpperCount o) {
        if(o.average() > this.average())
            return 1;
        else
            return -1;
    }

    public UpperCount(double count, double uppercount){
        this.count = new DoubleWritable(count);
        this.uppercount = new DoubleWritable(uppercount);
    }

    public void addcounts(double count, double uppercount){
        this.count = new DoubleWritable(this.count.get() + count);
        this.uppercount = new DoubleWritable(this.uppercount.get() + uppercount);
    }

    public double average(){
        return this.uppercount.get()/this.count.get();
    }

    public DoubleWritable getCount() {
        return count;
    }

    public DoubleWritable getUppercount() {
        return uppercount;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        count.write(dataOutput);
        uppercount.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        count.readFields(dataInput);
        uppercount.readFields(dataInput);
    }

    @Override
    public String toString() {
        double average = uppercount.get()/count.get();
        return (Double.toString(average));
    }
}
