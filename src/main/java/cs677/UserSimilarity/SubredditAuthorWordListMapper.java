package cs677.UserSimilarity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cs677.common.Constants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

public class SubredditAuthorWordListMapper extends Mapper<LongWritable, Text, Text, Text> {
    String subreddit;

    public SubredditAuthorWordListMapper() {}

    @Override
    protected void setup(Context context) {
        Configuration conf = context.getConfiguration();
        this.subreddit = conf.get(Constants.SUBREDDIT, "");
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        JsonParser p = new JsonParser();
        JsonObject o = (JsonObject) p.parse(value.toString());
        String subreddit = o.get(Constants.SUBREDDIT).getAsString();

        // only gather data in same subreddit
        if (!subreddit.equals(this.subreddit)) {
            return;
        }

        String author = o.get(Constants.AUTHOR).getAsString();
        String body = o.get(Constants.BODY).getAsString();

        // tokenize body into words
        StringTokenizer itr = new StringTokenizer(body);

        // emit word, count pairs
        HashSet<String> set = new HashSet<>();
        while (itr.hasMoreTokens()) {
            String token = itr.nextToken().toLowerCase().replaceAll("[-_\\.]", " ");
            token = token.replaceAll("[^\\w\\d]", "");

            String[] strings = token.split(" ");
            for (String str : strings) {
                if (str.length() > 0) {
                    set.add(str);
                }
            }
        }

        // map author's word and count
        for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
            String k = it.next();
            context.write(new Text(author), new Text(k));
        }
    }
}
