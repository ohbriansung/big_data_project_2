package cs677.BackStory;

import cs677.Writables.BackGroundKey;
import cs677.Writables.BackGroundWritable;

import cs677.Writables.Subreddits;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;


public class BackStoryReducer extends Reducer<BackGroundKey, BackGroundWritable, BackGroundKey,Text> {

    @Override
    protected void reduce(BackGroundKey key, Iterable<BackGroundWritable> values, Context context) throws IOException, InterruptedException {
        HashMap<String,Long> likedsubreddits = new HashMap<>();
        String subreddit;
        BackGroundWritable backGroundWritable = new BackGroundWritable(0,0,"none","none",0);
        for(BackGroundWritable value : values){
            subreddit = value.getLikes().toString();
            long count = 1;
            count += likedsubreddits.getOrDefault(subreddit, 0L);
            likedsubreddits.put(subreddit,count);

            key.append(value.getReadability_score().get(),1);
            backGroundWritable.append(value.getUpvotes().get(),value.getReadability_score().get(),value.getLocation().toString(),value.getCommentcount().get());
        }

        ArrayList<Subreddits> top10 = tolist(likedsubreddits);
        context.write(key, new Text(buildtext(backGroundWritable,top10)));
    }


    private String buildtext(BackGroundWritable backGroundWritable, ArrayList<Subreddits> top10){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        stringBuilder.append(backGroundWritable.toString());
        if(top10.size() > 0) {
            stringBuilder.append("\nLikedSubreddits: ");
            for (int i = 0; i < top10.size(); i++) {
                stringBuilder.append("\n\t" + top10.get(i).toString());
            }
        }else{
            stringBuilder.append("Something is very wrong");
        }
        stringBuilder.append("\n\n");
        return stringBuilder.toString();
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
