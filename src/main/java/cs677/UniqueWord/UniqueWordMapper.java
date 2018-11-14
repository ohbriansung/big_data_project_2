package cs677.UniqueWord;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cs677.common.Constants;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class UniqueWordMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        JsonParser p = new JsonParser();
        JsonObject o = (JsonObject) p.parse(value.toString());
        String author = o.get(Constants.AUTHOR).getAsString();

        if (author.equals("[deleted]")) {
            return;
        }

        String body = o.get(Constants.BODY).getAsString();
        String subreddit = o.get(Constants.SUBREDDIT).getAsString();
        StringTokenizer itr = new StringTokenizer(body);

        while (itr.hasMoreTokens()) {
            String token = itr.nextToken().toLowerCase().replaceAll("[\\d-_\\.]", " ");

            String[] terms = token.split("\\s+");
            for (String term : terms) {
                String temp = term.replaceAll("[^\\w]", "");
                if (temp.length() > 0) {
                    context.write(new Text(temp), new Text(subreddit));
                }
            }
        }
    }
}
