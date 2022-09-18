import java.time.Instant;
import java.util.List;

public class Haiku {

    private final String[] lines;
    private final Author author;
    private final Instant instant;

    public Haiku(final String[] lines,
                 final Author author,
                 final Instant instant) {
        this.lines = lines;
        this.author = author;
        this.instant = instant;
    }

    public String[] getLines() {
        return this.lines;
    }

    public Author getAuthor() {
        return this.author;
    }

    public String getTime() {
        return this.instant.toString();
    }

}
