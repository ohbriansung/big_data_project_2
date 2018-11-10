package cs677.MusicRecommendation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * https://www.musicgenreslist.com
 */
public class MusicGenre {
    private final HashMap<String, String> genre = new HashMap<>();
    private final HashMap<Integer, String> score = new HashMap<>();

    public MusicGenre() {
        try {
            makeMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getGenre(String keyword) {
        return this.genre.get(keyword);
    }

    public String getTry(int avg) {
        return this.score.get(avg);
    }

    private void makeMap() throws IOException {
        InputStream in = getClass().getResourceAsStream("/genre.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        for (String line; (line = reader.readLine()) != null;) {
            String[] parts = line.split("\\s+");
            if (parts.length >= 2) {
                this.genre.put(parts[0], parts[1]);
            }
        }

        in = getClass().getResourceAsStream("/music-score.txt");
        reader = new BufferedReader(new InputStreamReader(in));

        for (String line; (line = reader.readLine()) != null;) {
            String[] parts = line.split("\\s+");
            if (parts.length >= 2) {
                String cat = parts[1];
                if (parts.length == 3) {
                    cat += " " + parts[2];
                }
                this.score.put(Integer.parseInt(parts[0]), cat);
            }
        }
    }
}
