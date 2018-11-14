package cs677.MusicRecommendation;

import cs677.Writables.TextCountWritable;
import cs677.common.Constants;
import cs677.common.SentimentScorer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

public class AuthorMapper extends Mapper<LongWritable, Text, Text, TextCountWritable> {
    private SentimentScorer scorer = new SentimentScorer();

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        JsonParser p = new JsonParser();
        JsonObject o = (JsonObject) p.parse(value.toString());
        String author = o.get(Constants.AUTHOR).getAsString();

        if (author.equals("[deleted]") || skip()) {
            return;
        }

        // tokenize body words
        String body = o.get(Constants.BODY).getAsString();
        StringTokenizer itr = new StringTokenizer(body);

        // calculate sentiment score and put it in map
        int score = (int) scorer.sentimentScore(body);
        TextCountWritable scoreWritable =  new TextCountWritable("SentimentScoreOfAuthorComment", score);
        TextCountWritable countWritable =  new TextCountWritable("SentimentCountOfAuthorComment", 1);
        context.write(new Text(author), scoreWritable);
        context.write(new Text(author), countWritable);

        // emit word, count pairs
        HashMap<String, Integer> map = new HashMap<>();
        while (itr.hasMoreTokens()) {
            String token = itr.nextToken().replaceAll("[^\\w\\d\\s-_@.]", "");
            int count = map.getOrDefault(token, 0);
            count++;
            map.put(token, count);
        }

        for (String k : map.keySet()) {
            TextCountWritable w =  new TextCountWritable(k, map.get(k));
            context.write(new Text(author), w);
        }
    }

    private boolean skip() {
        Random r = new Random();
        int result = r.nextInt(4);

        if (result == 0) {
            return true;  // skip 25% of the data
        }
        else {
            return false;
        }
    }
}
