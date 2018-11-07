package cs677.BackStory;

import cs677.Writables.BackGroundKey;
import cs677.Writables.BackGroundWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class BackStoryReducer extends Reducer<BackGroundKey, BackGroundWritable, BackGroundKey,Text> {
    private ArrayList<Subreddit> likedsubreddits = new ArrayList<>();

    @Override
    protected void reduce(BackGroundKey key, Iterable<BackGroundWritable> values, Context context) throws IOException, InterruptedException {

        Subreddit subclass = new Subreddit(0,"0");
        BackGroundWritable backGroundWritable = new BackGroundWritable(0,0,"none","none",0);
        for(BackGroundWritable value : values){
            String subreddit = value.getLikes().toString();

            subclass = new Subreddit(1,subreddit);
            addtolist(subclass);

            key.append(value.getReadability_score().get(),value.getCommentcount().get());
            backGroundWritable.append(value.getUpvotes().get(),value.getReadability_score().get(),value.getLocation().toString(),value.getCommentcount().get());
        }

        context.write(key, new Text(buildtext(backGroundWritable)+ subclass.getSubreddit()) );
    }


    private String buildtext(BackGroundWritable backGroundWritable){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(backGroundWritable.toString());
        stringBuilder.append("\nNumber of reddits: " + Integer.toString(likedsubreddits.size()));
        stringBuilder.append("\nLikedSubreddits: ");

        for(int i = 0; i< likedsubreddits.size(); i++){
            stringBuilder.append("\n\t" + likedsubreddits.get(i).getSubreddit());
        }
        return stringBuilder.toString();
    }

    private void addtolist(Subreddit subreddit)
    {
        if(likedsubreddits.contains(subreddit)){
            for(Subreddit sub : likedsubreddits){
                if(sub.equals(subreddit)){
                    sub.incrementcount(1);
                    break;
                }
            }
        }else
            likedsubreddits.add(subreddit);

        if(likedsubreddits.size() > 10){
            Collections.sort(likedsubreddits);
            likedsubreddits.remove(10);
        }
    }

    private class Subreddit implements Comparable<Subreddit>{
        private int count;
        private String subreddit;

        private Subreddit(int count, String subreddit)
        {
            this.count = count;
            this.subreddit = subreddit;
        }

        @Override
        public int compareTo(Subreddit o) {
            if( o.count > this.count)
                return 1;
            else
                return -1;
        }

        @Override
        public boolean equals(Object obj) {
            Subreddit sub = (Subreddit) obj;
            return sub.getSubreddit().equals(this.subreddit);
        }

        @Override
        public int hashCode()
        {
            return subreddit.hashCode();
        }

        private void incrementcount(int count){
            this.count += count;
        }

        public String getSubreddit() {
            return subreddit;
        }

        @Override
        public String toString() {
            return subreddit;
        }
    }
}
