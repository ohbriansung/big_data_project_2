package cs677.UserSimilarity;

import com.google.gson.JsonObject;
import cs677.Writables.AuthorWordsWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class UserSimilarityReducer extends Reducer<DoubleWritable, AuthorWordsWritable, Text, Text> {

    @Override
    protected void reduce(DoubleWritable key, Iterable<AuthorWordsWritable> values, Context context)
            throws IOException, InterruptedException {
        // sum up value
        for (AuthorWordsWritable val : values) {
            JsonObject obj = new JsonObject();
            obj.addProperty("similarity", key.get());
            obj.add("matched", val.getWordList());

            context.write(new Text(val.getAuthor()), new Text(obj.toString()));
        }
    }
}
