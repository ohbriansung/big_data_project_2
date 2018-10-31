package cs677.misc;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Source: https://acadgild.com/blog/implementing-custom-output-format-hadoop Modified from source
 * to output a json file
 *
 * @author mcdomingo
 */
public class JsonOutputFormat extends FileOutputFormat<Writable, Writable> {

  protected static class JsonRecordWriter extends RecordWriter<Writable, Writable> {

    private DataOutputStream out;

    public JsonRecordWriter(DataOutputStream out) {
      this.out = out;
    }

    @Override
    public synchronized void write(Writable key, Writable value) throws IOException {
      String json_pair = String.format("{\"key: \"%s\", value: \"%s\"}%n", key.toString(), value.toString());
      out.writeBytes(json_pair);
    }

    @Override
    public synchronized void close(TaskAttemptContext job) throws IOException {
      out.close();
    }
  }

  public RecordWriter<Writable, Writable> getRecordWriter(TaskAttemptContext job)
      throws IOException {
    String file_extension = ".data";
    Path file = getDefaultWorkFile(job, file_extension);
    FileSystem fs = file.getFileSystem(job.getConfiguration());
    FSDataOutputStream fileOut = fs.create(file, false);
    return new JsonRecordWriter(fileOut);
  }
}
