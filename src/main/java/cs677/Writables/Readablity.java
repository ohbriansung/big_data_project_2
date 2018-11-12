package cs677.Writables;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import static java.lang.StrictMath.abs;

public class Readablity implements WritableComparable<Readablity> {

    private DoubleWritable fletch = new DoubleWritable();
    private DoubleWritable gunn = new DoubleWritable();

    public Readablity(){

    }

    public Readablity(double fletch, double gunn){
        this.fletch = new DoubleWritable(fletch);
        this.gunn = new DoubleWritable(gunn);
    }

    public DoubleWritable getFletch() {
        return fletch;
    }

    public DoubleWritable getGunn() {
        return gunn;
    }

    @Override
    public int compareTo(Readablity o) {
        if(abs(o.getFletch().get() - this.getFletch().get()) > 1){
            return o.getGunn().compareTo(this.getGunn());
        }else
            return o.getFletch().compareTo(this.getFletch());


    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        fletch.write(dataOutput);
        gunn.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        fletch.readFields(dataInput);
        gunn.readFields(dataInput);
    }

    @Override
    public String toString() {
        return "Readablity: " + Double.toString(getGunn().get()) + "\nGrade: " + Double.toString(getFletch().get());
    }
}
