package cs677.readability;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class SenWorSylWritable implements Writable {
  private LongWritable sentences = new LongWritable();
  private LongWritable words = new LongWritable();
  private LongWritable syllables = new LongWritable();

  public SenWorSylWritable() {}

  public SenWorSylWritable(long sentences, long words, long syllables) {
    this.sentences.set(sentences);
    this.words.set(words);
    this.syllables.set(syllables);
  }

  public long getSentences() {
    return sentences.get();
  }

  public long getWords() {
    return words.get();
  }

  public long getSyllables() {
    return syllables.get();
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    this.sentences.write(dataOutput);
    this.words.write(dataOutput);
    this.syllables.write(dataOutput);
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    this.sentences.readFields(dataInput);
    this.words.readFields(dataInput);
    this.syllables.readFields(dataInput);
  }

  @Override
  public String toString() {
    return String.format(
        "{\"sentences\": %d, \"words\": %d, \"syllables\": %d}",
        sentences.get(), words.get(), syllables.get());
  }
}
