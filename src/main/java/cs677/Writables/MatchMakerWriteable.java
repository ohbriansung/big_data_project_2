package cs677.Writables;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class MatchMakerWriteable extends BackGroundWritable {

    public MatchMakerWriteable(){

    }





    public MatchMakerWriteable(
            String user,
            double tox,
            int upvotes,
            double rscore,
            String location,
            int commentcount,
            String like,
            ArrayList<SubredditWritable> subreddits) {
        super.upvotes = new IntWritable(upvotes);
        super.readability_score = new DoubleWritable(rscore);
        super.location = new Text(location);
        super.likes = new Text(like);
        super.commentcount = new IntWritable(commentcount);
        super.toxic_score = new DoubleWritable(tox);
        super.subreddits = new SubredditList(subreddits.toArray(new SubredditWritable[0]));
        super.user = new Text(user);
    }

    @Override
    public void append(double tox, int upvotes, double rscore, String location, int commentcount) {
        super.append(tox, upvotes, rscore, location, commentcount);
    }


    @Override
    public int compareTo(BackGroundWritable o) {
        if(abs(o.getReadAvg() - this.getReadAvg()) > 15){
            if(o.getReadAvg() > this.getReadAvg())
                return -1;
            else
                return 1;
        }else
            if(abs(o.getToxAvg() - this.getToxAvg()) > 1)
                return o.getUpvotes().compareTo(this.getUpvotes());
            else
                if(o.getToxAvg() > this.getToxAvg())
                    return -1;
                else
                    return 0;

    }


}
