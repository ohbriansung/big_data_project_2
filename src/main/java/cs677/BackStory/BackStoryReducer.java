package cs677.BackStory;

import cs677.Writables.BackGroundWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class BackStoryReducer extends Reducer<Text, BackGroundWritable, Text,Text> {
    private ArrayList<Subreddit> likedsubreddits = new ArrayList<>();

    @Override
    protected void reduce(Text key, Iterable<BackGroundWritable> values, Context context) throws IOException, InterruptedException {


        BackGroundWritable backGroundWritable = new BackGroundWritable(0,0,"none","none",0);
        for(BackGroundWritable value : values){
            String subreddit = value.getLikes().toString();
            Subreddit subclass = new Subreddit(1,subreddit);
            if(likedsubreddits.contains(subclass)){
                for(Subreddit sub : likedsubreddits){
                    if(sub.equals(subclass)){
                        sub.incrementcount(1);
                        break;
                    }
                }
            }else
                likedsubreddits.add(subclass);
            if(likedsubreddits.size() > 10){
                Collections.sort(likedsubreddits);
                likedsubreddits.remove(10);
            }

            backGroundWritable.append(value.getUpvotes().get(),value.getReadability_score().get(),value.getLocation().toString(),value.getCommentcount().get());

        }

        context.write(key, new Text( backGroundWritable.toString() + "\nLiked Subreddits " + likedsubreddits.toString()));

    }

    private class Subreddit implements Comparable<Subreddit>{
        private int count;
        private String subreddit;

        public Subreddit(int count, String subreddit)
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

        public void incrementcount(int count){
            this.count += count;
        }

        public String getSubreddit() {
            return subreddit;
        }
    }
}
