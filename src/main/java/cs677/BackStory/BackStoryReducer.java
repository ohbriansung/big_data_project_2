package cs677.BackStory;

import cs677.Writables.BackGroundKey;
import cs677.Writables.BackGroundWritable;

import cs677.Writables.Subreddits;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;


public class BackStoryReducer extends Reducer<Text, BackGroundWritable, NullWritable,BackGroundWritable> {

    @Override
    protected void reduce(Text key, Iterable<BackGroundWritable> values, Context context) throws IOException, InterruptedException {
        HashMap<String,Long> likedsubreddits = new HashMap<>();
        String subreddit;
        ArrayList<Subreddits> top10  = new ArrayList<>();
        BackGroundWritable backGroundWritable = new BackGroundWritable(key.toString(),0,0,0,"none",0,"none",top10);
        for(BackGroundWritable value : values){
            subreddit = value.getLikes().toString();
            long count = 1;
            count += likedsubreddits.getOrDefault(subreddit, 0L);
            likedsubreddits.put(subreddit,count);

            backGroundWritable.append(value.getToxic_score().get(),value.getUpvotes().get(),value.getReadability_score().get(),value.getLocation().toString(),value.getCommentcount().get());
        }

        top10 = tolist(likedsubreddits);
        backGroundWritable.updateList(top10);
        context.write(NullWritable.get(), backGroundWritable);
    }





    private ArrayList<Subreddits> tolist(HashMap<String,Long> subreddits)
    {
        ArrayList<Subreddits> top10 = new ArrayList<>();
        for(Map.Entry<String,Long> entry : subreddits.entrySet()){
            top10.add(new Subreddits(entry.getValue(),entry.getKey()));
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
