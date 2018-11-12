package cs677.MusicRecommendation;

import com.google.gson.*;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class MusicRecommendationMapper extends Mapper<LongWritable, Text, Text, Text> {
    private final MusicGenre musicGenre = new MusicGenre();

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] split = value.toString().split("\t");
        String author = split[0];
        String textCountArray = split[1];

        JsonParser parser = new JsonParser();
        JsonArray array = (JsonArray) parser.parse(textCountArray);

        int score = -6;
        int count = 0;
        boolean check = false;

        for (JsonElement element : array) {
            JsonObject obj = ((JsonObject) element);
            String text = obj.get("text").getAsString();
            int val = obj.get("count").getAsInt();

            if (text.equals("SentimentScoreOfAuthorComment")) {
                if (count > 0) {
                    setTry(author, count, val, context);
                }
                else {
                    score = val;
                }
            } else if (text.equals("SentimentCountOfAuthorComment")) {
                if (score != -6) {
                    setTry(author, val, score, context);
                }
                else {
                    count = val;
                }
            } else if (!check) {
                String rec = this.musicGenre.getGenre(text);
                if (rec != null) {
                    context.write(new Text(author), new Text("recommended:" + rec));
                    check = true;
                }
            }
        }

    }

    private void setTry(String author, int count, int score, Context context) throws IOException, InterruptedException {
        int avg = score / count;
        String tryThis = this.musicGenre.getTry(avg);
        context.write(new Text(author), new Text("try:" + tryThis));
    }
}
