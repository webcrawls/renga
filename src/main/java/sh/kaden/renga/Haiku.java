package sh.kaden.renga;

import java.time.Instant;

/**
 * {@code sh.kaden.renga.Haiku} represents an authored haiku.
 */
public class Haiku {

    private final String[] lines;
    private final Author author;
    private final Instant timestamp;

    /**
     * Constructs {@code sh.kaden.renga.Haiku}.
     *
     * @param lines   the lines of text that comprise the haiku
     * @param author  the author of the haiku
     * @param timestamp the creation timestamp
     */
    public Haiku(final String[] lines,
                 final Author author,
                 final Instant timestamp) {
        this.lines = lines;
        this.author = author;
        this.timestamp = timestamp;
    }

    public String[] lines() {
        return this.lines;
    }

    public Author author() {
        return this.author;
    }

    public Instant time() {
        return this.timestamp;
    }

}
