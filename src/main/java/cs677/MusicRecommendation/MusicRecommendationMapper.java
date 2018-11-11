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
        String line = value.toString();
        int firstSpaceIndex = line.indexOf(" ");
        String author = line.substring(0, firstSpaceIndex);
        String textCountArray = line.substring(firstSpaceIndex + 1);

        JsonParser parser = new JsonParser();
        JsonArray array = (JsonArray) parser.parse(textCountArray);

        int score = -6;
        int count = 0;
        boolean check = false;
        boolean check2 = false;

        for (JsonElement element : array) {
            if (check && check2) {
                break;
            }

            JsonObject obj = ((JsonObject) element);
            if (obj.get("SentimentScoreOfAuthorComment") != null) {
                score = obj.get("SentimentScoreOfAuthorComment").getAsInt();
                if (count > 0 && !check2) {
                    setTry(author, count, score, context);
                    check2 = true;
                }
            } else if (obj.get("SentimentCountOfAuthorComment") != null) {
                count = obj.get("SentimentCountOfAuthorComment").getAsInt();
                if (score != -6 && !check2) {
                    setTry(author, count, score, context);
                    check2 = true;
                }
            } else if (!check) {
                String str = obj.toString();
                int i = str.indexOf("\"");
                str = str.substring(i + 1);
                i = str.indexOf("\"");
                str = str.substring(0, i);

                String rec = this.musicGenre.getGenre(str);
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
