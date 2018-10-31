package cs677.misc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class FileCreator {

  public static Path findEmptyPath(Configuration conf, String location) throws IOException {
    FileSystem fs = FileSystem.get(conf);
    int number = 0;
    String numbered_location = String.format("%s_%04d", location, number);
    Path path = new Path(numbered_location);
    while (fs.exists(path)) {
      number += 1;
      numbered_location = String.format("%s_%04d", location, number);
      path = new Path(numbered_location);
    }
    return path;
  }
}
