package sh.kaden.renga.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import sh.kaden.renga.Author;
import sh.kaden.renga.Haiku;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code JsonRengaLoader} loads Renga data from a {@link File} containing JSON.
 */
public class JsonRengaLoader implements RengaLoader {

    private final File file;

    public JsonRengaLoader(final File file) {
        this.file = file;
    }

    @Override
    public List<Haiku> loadHaikus() {
        String jsonString;
        try {
            if (!this.file.exists()) {
                Files.writeString(this.file.toPath(), "[]");
            }
            jsonString = Files.readString(this.file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final JsonElement json = JsonParser.parseString(jsonString);
        if (!json.isJsonArray()) {
            throw new RuntimeException("not arr");
        }
        final List<Haiku> haikus = new ArrayList<>();
        for (final JsonElement haikuElement : json.getAsJsonArray()) {
            if (!haikuElement.isJsonObject()) {
                throw new RuntimeException();
            }

            final JsonObject haikuJson = haikuElement.getAsJsonObject();
            final Author author = new Author(haikuJson.get("author").getAsString());
            final Instant time = Instant.ofEpochMilli(haikuJson.get("timestamp").getAsLong());
            final JsonArray lines = haikuJson.get("lines").getAsJsonArray();
            final String[] array = new String[lines.size()];

            for (int i = 0; i < lines.size(); i++) {
                final JsonElement lineElement = lines.get(i);
                if (!lineElement.isJsonPrimitive()) {
                    throw new RuntimeException();
                }

                final String line = lineElement.getAsString();
                array[i] = line;
            }

            haikus.add(new Haiku(array, author, time));
        }

        return haikus;
    }

    @Override
    public void saveHaikus(final List<Haiku> haikus) {
        final JsonArray array = new JsonArray();
        for (final Haiku haiku : haikus) {
            final JsonObject haikuJson = new JsonObject();
            final JsonArray linesJson = new JsonArray();
            for (final String line : haiku.lines()) {
                linesJson.add(line);
            }
            haikuJson.add("lines", linesJson);
            haikuJson.addProperty("author", haiku.author().name());
            haikuJson.addProperty("timestamp", haiku.time().toEpochMilli());
            array.add(haikuJson);
        }

        try {
            Files.writeString(this.file.toPath(), array.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
