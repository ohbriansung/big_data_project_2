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
import java.util.PriorityQueue;

public class MCCountPost {
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
    PriorityQueue<CommentKeeper> priorityQueue = new PriorityQueue<>(25);
    CommentKeeper nextComment;
    while (fileStatusListIterator.hasNext()) {
      LocatedFileStatus fileStatus = fileStatusListIterator.next();
      if (fileStatus.isFile()) {
        BufferedReader br =
            new BufferedReader(new InputStreamReader(fs.open(fileStatus.getPath())));
        String line = br.readLine();
        while (line != null) {
          String[] splitLine = line.split("\\s+", 2);
          JSONObject obj = new JSONObject(splitLine[1]);
          nextComment =
              new CommentKeeper(
                  splitLine[0], obj.getInt("count"), obj.getJSONArray("comments").toString());
          if (priorityQueue.size() >= 25 && nextComment.compareTo(priorityQueue.peek()) > 0) {
            priorityQueue.poll();
            priorityQueue.add(nextComment);
          }
          if (priorityQueue.size() < 25) {
            priorityQueue.add(nextComment);
          }
          line = br.readLine();
        }
      }
    }
    while (priorityQueue.size() > 0) {
      System.out.println(priorityQueue.poll().toString());
    }
  }

  private static class CommentKeeper implements Comparable<CommentKeeper> {
    int count;
    String comments;
    String user;

    private CommentKeeper(String user, int count, String comments) {
      this.user = user;
      this.count = count;
      this.comments = comments;
    }

    @Override
    public int compareTo(CommentKeeper other) {
      return this.count - other.count;
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{\"user\": ");
      sb.append(user);
      sb.append(", \"count\": ");
      sb.append(count);
      sb.append(", \"comments\": ");
      sb.append(comments);
      sb.append("}");
      return sb.toString();
    }
  }
}
