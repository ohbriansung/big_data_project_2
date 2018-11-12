package cs677.BackStory;

import cs677.Writables.BackGroundWritable;
import cs677.Writables.MatchMakerWriteable;
import cs677.Writables.SubredditWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MatchReducer extends Reducer<Text,BackGroundWritable,NullWritable,MatchMakerWriteable> {

    @Override
    protected void reduce(Text key, Iterable<BackGroundWritable> values, Context context) throws IOException, InterruptedException {
        HashMap<String,Long> likedsubreddits = new HashMap<>();
        String subreddit;
        ArrayList<SubredditWritable> top10 = new ArrayList<>();
        MatchMakerWriteable matcher = new MatchMakerWriteable(key.toString(),0,0,0,"none",0,"none",top10);
        for(BackGroundWritable value : values){
            subreddit = value.getLikes().toString();
            long count = 1;
            count += likedsubreddits.getOrDefault(subreddit, 0L);
            likedsubreddits.put(subreddit,count);

            matcher.append(value.getToxic_score().get(),value.getUpvotes().get(),value.getReadability_score().get(),value.getLocation().toString(),value.getCommentcount().get());
        }

        top10 = tolist(likedsubreddits);
        matcher.updateList(top10);
        if(matcher.getReadAvg() < 100 && matcher.getReadAvg() > 30 && matcher.getCommentcount().get() > 30)
            context.write(NullWritable.get(), matcher);
    }

    protected ArrayList<SubredditWritable> tolist(HashMap<String,Long> subreddits)
    {
        ArrayList<SubredditWritable> top10 = new ArrayList<>();
        for(Map.Entry<String,Long> entry : subreddits.entrySet()){
            top10.add(new SubredditWritable(entry.getValue(),entry.getKey()));
            if(top10.size() > 10)
            {
                Collections.sort(top10);
                top10.remove(10);
            }
        }
        Collections.sort(top10);
        return top10;

    }

}
