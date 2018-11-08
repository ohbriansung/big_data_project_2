package cs677.Writables;

public class Subreddits implements Comparable<Subreddits>{
    private long count;
    private String subreddit;

    public Subreddits(long count, String subreddit)
    {
        this.count = count;
        this.subreddit = subreddit;
    }

    @Override
    public int compareTo(Subreddits o) {
        if( o.count > this.count)
            return 1;
        else
            return -1;
    }

    @Override
    public boolean equals(Object obj) {
        Subreddits sub = (Subreddits) obj;
        return sub.getSubreddit().equals(this.subreddit);
    }

    @Override
    public int hashCode()
    {
        return subreddit.hashCode();
    }


    public String getSubreddit() {
        return subreddit;
    }

    @Override
    public String toString() {
        return subreddit + ":" + Long.toString(count);
    }
}
