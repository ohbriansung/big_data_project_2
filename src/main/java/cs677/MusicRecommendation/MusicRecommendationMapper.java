package cs677.MusicRecommendation;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Mapper: Reads line by line, split them into words. Emit <word, 1> pairs.
 */
public class MusicRecommendationMapper
extends Mapper<LongWritable, Text, Text, IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {

        // parse line into JsonObject
        JsonObject obj;
        try {
            JsonParser parser = new JsonParser();
            obj = (JsonObject) parser.parse(value.toString());
        } catch (JsonSyntaxException ignore) {
            // invalid json string format
            return;
        }

        // Get subreddit
        String sub = obj.get("subreddit").getAsString();

        // emit word, count pairs.
        context.write(new Text(sub), new IntWritable(1));
    }
}
