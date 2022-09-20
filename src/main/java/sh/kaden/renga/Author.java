package sh.kaden.renga;

import java.util.Optional;

public class Author {

    private final String name;

    public Author(final String name) {
        this.name = name;
    }

    public String name() {
        return this.name;
    }

}
