package cs677.mostcomments;

import cs677.misc.CommentListWritable;
import cs677.misc.CommentWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MCCountReducer extends Reducer<Text, CommentWritable, Text, CommentListWritable> {

  @Override
  protected void reduce(Text key, Iterable<CommentWritable> values, Context context)
      throws IOException, InterruptedException {
    CommentListWritable cmWritable = new CommentListWritable();

    // calculate the total count
    for (CommentWritable val : values) {
      cmWritable.addComment(val);
    }

    context.write(key, cmWritable);
  }
}
