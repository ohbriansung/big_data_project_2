package cs677.Writables;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.json.JSONObject;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Readablity implements WritableComparable<Readablity> {

  private DoubleWritable fleschEase = new DoubleWritable();
  private DoubleWritable fleschGrade = new DoubleWritable();
  private DoubleWritable gunn = new DoubleWritable();
  private Text author = new Text();

  public Readablity() {}

  public Readablity(double fleschEase, double fleschGrade, double gunn, String author) {
    this.fleschEase.set(fleschEase);
    this.fleschGrade.set(fleschGrade);
    this.gunn.set(gunn);
    this.author.set(author);
  }

  public double getFleschEase() {
    return fleschEase.get();
  }

  public double getFleschGrade() {
    return fleschGrade.get();
  }

  public double getGunn() {
    return gunn.get();
  }

  @Override
  public int compareTo(Readablity o) {
    int compare = o.fleschEase.compareTo(this.fleschEase);
    if (compare == 0) compare = o.fleschGrade.compareTo(fleschGrade);
    if (compare == 0) compare = o.gunn.compareTo(gunn);
    return compare;
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    fleschEase.write(dataOutput);
    fleschGrade.write(dataOutput);
    gunn.write(dataOutput);
    author.write(dataOutput);
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    fleschEase.readFields(dataInput);
    fleschGrade.readFields(dataInput);
    gunn.readFields(dataInput);
    author.readFields(dataInput);
  }

  @Override
  public String toString() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("Key", author);
    jsonObject.put("FleschEase", fleschEase.get());
    jsonObject.put("FleschGrade", fleschGrade.get());
    jsonObject.put("Gunn", gunn.get());
    return jsonObject.toString();
  }
}
