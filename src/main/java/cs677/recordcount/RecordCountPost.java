package cs677.recordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class RecordCountPost {

  public static void main(String[] args) {
    try {
      Configuration conf = new Configuration();
      parseOutput(conf, new Path(args[0]));
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  protected static void parseOutput(Configuration conf, Path path) throws IOException {
    FileSystem fs = path.getFileSystem(conf);
    RemoteIterator<LocatedFileStatus> fileStatusListIterator = fs.listFiles(path, false);
    BigInteger total_count = BigInteger.ZERO;
    int subreddit_count = 0;
    while (fileStatusListIterator.hasNext()) {
      LocatedFileStatus fileStatus = fileStatusListIterator.next();
      if (fileStatus.isFile()) {
        BufferedReader br =
            new BufferedReader(new InputStreamReader(fs.open(fileStatus.getPath())));
        String line = br.readLine();
        while (line != null) {
          //          JSONObject obj = new JSONObject(line);
          //          int post_count = Integer.parseInt(obj.getString("value"));
          BigInteger post_count = new BigInteger((line.split("\\s+", 2)[1]));
          total_count = total_count.add(post_count);
          subreddit_count += 1;
          line = br.readLine();
        }
      }
    }
    System.out.println(String.format("Number of subreddits: %d", subreddit_count));
    System.out.println(String.format("Total number of entries: %d", total_count));
  }
}
