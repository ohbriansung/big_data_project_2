package cs677.UserSimilarity;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubredditAuthorWordListReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        List<String> array = new ArrayList<>();

        // sum up value
        for (Text val : values) {
            array.add(val.toString());
        }

        context.write(key, new Text(array.toString()));
    }
}
