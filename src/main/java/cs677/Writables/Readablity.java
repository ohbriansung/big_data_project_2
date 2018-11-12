package cs677.Writables;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.json.JSONObject;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;



public class Readablity implements WritableComparable<Readablity> {

    private DoubleWritable fletch = new DoubleWritable();
    private DoubleWritable gunn = new DoubleWritable();
    private Text author = new Text();

    public Readablity(){

    }

    public Readablity(double fletch, double gunn, String author){
        this.fletch = new DoubleWritable(fletch);
        this.gunn = new DoubleWritable(gunn);
        this.author = new Text( author);
    }

    public DoubleWritable getFletch() {
        return fletch;
    }

    public DoubleWritable getGunn() {
        return gunn;
    }

    @Override
    public int compareTo(Readablity o) {
        return o.getFletch().compareTo(this.getFletch());
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        fletch.write(dataOutput);
        gunn.write(dataOutput);
        author.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        fletch.readFields(dataInput);
        gunn.readFields(dataInput);
        author.readFields(dataInput);
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Key", author);
        jsonObject.put("Readablity", getGunn().get());
        jsonObject.put("Grade", getFletch().get());
        return jsonObject.toString();
    }
}
