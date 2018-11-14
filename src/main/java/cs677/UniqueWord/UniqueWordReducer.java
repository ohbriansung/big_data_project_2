package cs677.UniqueWord;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class UniqueWordReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

        String term = key.toString();
        if (term.contains("http") || term.contains("www") || term.contains("0") || term.contains("yes")) {
            return;
        }

        String oneSubreddit = null;
        for (Text val : values) {
            String subreddit = val.toString();

            if (oneSubreddit == null || oneSubreddit.equals(subreddit)) {
                oneSubreddit = subreddit;
            }
            else {
                return;
            }
        }

        context.write(key, new Text(oneSubreddit));
    }
}
