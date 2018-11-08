package cs677.MusicRecommendation;

import cs677.common.Constants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.StringTokenizer;

public class AuthorMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    String author;

    public AuthorMapper() {}

    @Override
    protected void setup(Context context) {
        Configuration conf = context.getConfiguration();
        author = conf.get(Constants.AUTHOR, "");
    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        JsonParser p = new JsonParser();
        JsonObject o = (JsonObject) p.parse(value.toString());

        if (!o.get(Constants.AUTHOR).getAsString().equals(author)) {
            return;
        }

        // tokenize body words
        String body = o.get(Constants.BODY).getAsString();
        StringTokenizer itr = new StringTokenizer(body);

        // emit word, count pairs
        while (itr.hasMoreTokens()) {
            context.write(new Text(itr.nextToken().toLowerCase()), new IntWritable(1));
        }
    }
}
