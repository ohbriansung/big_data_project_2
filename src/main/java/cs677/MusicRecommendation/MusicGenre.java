package cs677.MusicRecommendation;

import java.util.Arrays;
import java.util.HashSet;

/**
 * https://www.musicgenreslist.com
 */
public class MusicGenre {
    public static final HashSet<String> GENRE = new HashSet<>(
            Arrays.asList(
                    "classical",
                    "country",
                    "jazz",
                    "pop",
                    "hip-hop/rap",
                    "rock",
                    "electronic",
                    "blue",
                    "disney",
                    "comedy",
                    "r&b and soul",
                    "instrumental",
                    "inspirational"
            )
    );

    public static final HashSet<String> CLASSICAL = new HashSet<>(
            Arrays.asList(
                    "classical",
                    "avant-garde",
                    "baroque",
                    "chamber",
                    "chant",
                    "choral",
                    "impressionist",
                    "medieval",
                    "minimalism",
                    "opera",
                    "orchestral",
                    "renaissance",
                    "romantic",
                    "wedding"
            )
    );

    public static final HashSet<String> COUNTRY = new HashSet<>(
            Arrays.asList(
                    "country",
                    "howdy",
                    "americana",
                    "bluegrass",
                    "cowboy",
                    "quadrille",
                    "cotillion"
            )
    );

    public static final HashSet<String> JAZZ = new HashSet<>(
            Arrays.asList(
                    "jazz",
                    "acid",
                    "bop",
                    "cool",
                    "dixieland",
                    "fusion",
                    "ragtime",
                    "aaba",
                    "1920"
            )
    );

    public static final HashSet<String> POP = new HashSet<>(
            Arrays.asList(
                    "pop",
                    "britpop",
                    "contemporary",
                    "teen",
                    "coldplay",
                    "radiohead",
                    "britney",
                    "beatles",
                    "1920"
            )
    );

    public static final HashSet<String> HIP_HOP_RAP = new HashSet<>(
            Arrays.asList(
                    "hip-hop",
                    "rap",
                    "jay-z",
                    "t-pain",
                    "akon",
                    "eminem",
                    "dogg",
                    "beatles",
                    "1920"
            )
    );

    public static final HashSet<String> ROCK = new HashSet<>(
            Arrays.asList(
                    "rock",
                    "roll",
                    "psychedelic",
                    "metal",
                    "surf",
                    "tex-mex",
                    "keyboard",
                    "acdc",
                    "ac/dc",
                    "queen",
                    "maroon",
                    "lifehouse",
                    "linkin",
                    "radiohead",
                    "coldplay"
            )
    );

    public static final HashSet<String> ELECTRONIC = new HashSet<>(
            Arrays.asList(
                    "electronic",
                    "ambient",
                    "crunk",
                    "downtempo",
                    "electro",
                    "electronica",
                    "industrial",
                    "idm",
                    "8bit",
                    "chillwave",
                    "chiptune",
                    "bassline",
                    "hardstyle"
            )
    );

    public static final HashSet<String> BLUES = new HashSet<>(
            Arrays.asList(
                    "blue",
                    "blues",
                    "bar",
                    "rhythm",
                    "three-chord",
                    "30’s",
                    "civil",
                    "1910"
            )
    );

    public static final HashSet<String> DISNEY = new HashSet<>(
            Arrays.asList(
                    "disney",
                    "mickey",
                    "cartoon",
                    "mulan",
                    "lion",
                    "mermaid",
                    "aladdin",
                    "aristocats",
                    "stitch",
                    "princess",
                    "hunchback",
                    "cinderella",
                    "dumbo",
                    "wonderland",
                    "robinhood",
                    "bambi",
                    "tarzan",
                    "winnie",
                    "pixar"
            )
    );

    public static final HashSet<String> COMEDY = new HashSet<>(
            Arrays.asList(
                    "comedy",
                    "hilarious",
                    "funny",
                    "galifianakis",
                    "vaudeville"
            )
    );

    public static final HashSet<String> R_AND_B_AND_SOUL = new HashSet<>(
            Arrays.asList(
                    "r&b",
                    "soul",
                    "disco",
                    "funk",
                    "motown",
                    "quiet",
                    "glee"
            )
    );

    public static final HashSet<String> INSTRUMENTAL = new HashSet<>(
            Arrays.asList(
                    "instrumental",
                    "march",
                    "brisk",
                    "military",
                    "sonata"
            )
    );

    public static final HashSet<String> NISPIRATIONAL = new HashSet<>(
            Arrays.asList(
                    "inspirational",
                    "gospel",
                    "god",
                    "bless",
                    "blessing",
                    "christian",
                    "praise",
                    "worship",
                    "qawwali",
                    "ccm"
            )
    );
}
