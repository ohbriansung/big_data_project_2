package cs677.Writables;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Container for year-month pairs, output in the format YYYY-MM. This class demonstrates how to make
 * a WritableComparable suitable for use as a *key* or value.
 *
 * <p>IMPORTANT: You need to implement hashCode() for Hadoop to partition keys correctly!
 *
 * @author malensek
 */
public class YearMonthWritable implements WritableComparable<YearMonthWritable> {

  private IntWritable year = new IntWritable(0);
  private IntWritable month = new IntWritable(0);

  public YearMonthWritable() {}

  public YearMonthWritable(int year, int month) {
    this.year.set(year);
    this.month.set(month);
  }

  public YearMonthWritable(LocalDate date) {
    this(date.getYear(), date.getMonthValue());
  }

  public YearMonthWritable(LocalDateTime dateTime) {
    this(dateTime.getYear(), dateTime.getMonthValue());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    YearMonthWritable that = (YearMonthWritable) o;

    if (year == null) return that.year == null;
    if (!year.equals(that.year)) return false;
    if (month == null) return that.month == null;
    return month.equals(that.month);
  }

  @Override
  public int hashCode() {
    int result = year != null ? year.get() : 0;
    result = 13 * result + (month != null ? month.get() : 0);
    return result;
  }

  @Override
  public int compareTo(YearMonthWritable that) {
    int yearCompare = this.year.compareTo(that.year);
    if (yearCompare != 0) {
      return yearCompare;
    }

    return this.month.compareTo(that.month);
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    this.year.write(dataOutput);
    this.month.write(dataOutput);
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    this.year.readFields(dataInput);
    this.month.readFields(dataInput);
  }

  @Override
  public String toString() {
    return year.toString() + "-" + String.format("%02d", month.get());
  }
}
