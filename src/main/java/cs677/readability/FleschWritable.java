package cs677.readability;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FleschWritable implements WritableComparable<FleschWritable> {
  private DoubleWritable ease = new DoubleWritable();
  private DoubleWritable grade = new DoubleWritable();

  public FleschWritable() {}

  public FleschWritable(double ease, double grade) {
    this.ease.set(ease);
    this.grade.set(grade);
  }

  public FleschWritable(long sentences, long words, long syllables) {
    sentences = Math.max(sentences, 1);
    words = Math.max(words, 1);
    syllables = Math.max(syllables, 1);
    this.ease.set(easeScore(sentences, words, syllables));
    this.grade.set(gradeScore(sentences, words, syllables));
  }

  public static double easeScore(long sentences, long words, long syllables) {
    return 206.835 - (1.015 * ((double) words / sentences)) - (84.6 * ((double) syllables / words));
  }

  public static double gradeScore(long sentences, long words, long syllables) {
    return 0.39 * ((double) words / sentences) + (11.8 * ((double) syllables / words)) - 15.59;
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    ease.write(dataOutput);
    grade.write(dataOutput);
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    ease.readFields(dataInput);
    grade.readFields(dataInput);
  }

  @Override
  public String toString() {
    return String.format("{\"ease\": %f, \"grade\": %f}", ease.get(), grade.get());
  }

  @Override
  public int compareTo(FleschWritable other) {
    if (other == null) return 1;
    int comp = this.ease.compareTo(other.ease);
    if (comp == 0) {
      comp = this.grade.compareTo(other.grade);
    }
    return comp;
  }
}
