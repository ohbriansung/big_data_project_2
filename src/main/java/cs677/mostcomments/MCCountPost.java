package cs677.mostcomments;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.PriorityQueue;

public class MCCountPost {
    //todo: test
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
    PriorityQueue<JSONObject> jsonObjectPriorityQueue = new PriorityQueue<>(25, new JsonCompare());
    while (fileStatusListIterator.hasNext()) {
      LocatedFileStatus fileStatus = fileStatusListIterator.next();
      if (fileStatus.isFile()) {
        BufferedReader br =
            new BufferedReader(new InputStreamReader(fs.open(fileStatus.getPath())));
        String line = br.readLine();
        while (line != null) {
          JSONObject obj = new JSONObject(line.split("\\s+", 2)[1]);
          jsonObjectPriorityQueue.add(obj);
          if (jsonObjectPriorityQueue.size() > 20) {
              jsonObjectPriorityQueue.poll();
          }
          line = br.readLine();
        }
      }
    }
    System.out.println(String.format("Number of subreddits: %d", subreddit_count));
    System.out.println(String.format("Total number of entries: %d", total_count));
  }

  private static class JsonCompare implements Comparator<JSONObject> {
    @Override
    public int compare(JSONObject jsonObject, JSONObject t1) {
      return t1.getInt("count") - jsonObject.getInt("count");
    }
  }
}
