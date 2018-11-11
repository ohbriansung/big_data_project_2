package cs677.Writables;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class MatchMakerWriteable extends BackGroundWritable {

    public MatchMakerWriteable(){

    }

    private IntWritable compareScore = new IntWritable(0);



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


        if(abs(o.readability_score.get() - this.readability_score.get()) > 15){
            if(abs(o.getToxic_score().get() - this.getToxic_score().get()) > 10){
                return o.upvotes.compareTo(this.upvotes);
            }else
                return o.toxic_score.compareTo(this.toxic_score);


        }else
            return o.readability_score.compareTo(this.readability_score);
    }


}
