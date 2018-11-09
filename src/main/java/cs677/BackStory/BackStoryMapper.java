package cs677.BackStory;


import com.kennycason.fleschkincaid.FleschKincaid;
import cs677.Writables.BackGroundKey;
import cs677.Writables.BackGroundWritable;
import cs677.Writables.Subreddits;
import cs677.Writables.TextCountWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BackStoryMapper extends Mapper<LongWritable, Text, Text, BackGroundWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        // tokenize into words.
        JSONObject obj = new JSONObject(value.toString());

        String sub = obj.getString("subreddit");

        int ups = obj.getInt("ups");
        FleschKincaid fleschKincaid = new FleschKincaid();
        double readscore = fleschKincaid.calculate(obj.getString("body"));
        String location = getLocation(obj.getString("body"));
        int commentcount = 1;
        ArrayList<Subreddits> subreddits = new ArrayList<>();
        subreddits.add(new Subreddits(1,sub));
        BackGroundWritable background = new BackGroundWritable(ups,readscore,location,commentcount,sub,subreddits);

        String user = obj.getString("author");
        context.write(new Text(user),background);
    }

    private String getLocation(String body){
        String regex = "((i|we)('m|\\s*am|\\s*are|'re)\\s*from\\s*([\\w\\d\\s]+))";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(body);

        if (m.find()) {
            return m.group(4);
        }

        return "no location";
    }
}
