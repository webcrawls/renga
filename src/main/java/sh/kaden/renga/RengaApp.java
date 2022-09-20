package sh.kaden.renga;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.template.JavalinPebble;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.jetbrains.annotations.Nullable;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import sh.kaden.renga.data.JsonRengaLoader;
import sh.kaden.renga.data.RengaLoader;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * {@code RengaApp} is the main entrypoint class for the Renga website.
 */
public class RengaApp {

    public static void main(String[] args) {
        final JsonRengaLoader loader = new JsonRengaLoader(new File("haikus.json"));
        final RengaApp app = new RengaApp(loader);
        app.start();
        Runtime.getRuntime().addShutdownHook(new Thread(app::save));
    }

    private final RengaLoader loader;
    private final List<Haiku> haikus;
    private Javalin javalin;

    /**
     * Constructs {@code RengaApp}.
     *
     * @param loader the renga loader
     */
    public RengaApp(final RengaLoader loader) {
        this.loader = loader;
        this.haikus = new ArrayList<>();
    }

    /**
     * Loads haikus from the provided {@link RengaLoader} and starts the web server.
     */
    public void start() {
        this.haikus.addAll(this.loader.loadHaikus());
        this.javalin = Javalin.create(cfg -> {
                    JavalinPebble.configure(new PebbleEngine.Builder()
                            .loader(new FileLoader())
                            .strictVariables(false)
                            .build());
                    final TemplateEngine engine = new TemplateEngine();
                    final FileTemplateResolver resolver = new FileTemplateResolver();
                    resolver.setCacheable(false);
                    engine.addTemplateResolver(resolver);
                    JavalinThymeleaf.configure(engine);
                    cfg.addStaticFiles("static", Location.EXTERNAL);
                })
                .get("/", ctx -> {
                    ctx.render("index.html", Map.of("haikus", this.haikus));
                })
                .post("/", ctx -> {
                    final String[] lines = ctx.formParam("lines").split(System.lineSeparator());
                    final boolean valid = this.checkLines(lines);

                    if (valid) {
                        final @Nullable String author = ctx.formParam("author");
                        this.haikus.add(0, new Haiku(lines, new Author(Objects.requireNonNullElse(author, "anonymous")), Instant.now()));
                    }

                    ctx.redirect(ctx.req.getRequestURI());
                })
                .start(7001);
    }

    /**
     * Saves haikus with {@link RengaLoader}.
     */
    public void save() {
        this.loader.saveHaikus(this.haikus);
    }

    private boolean checkLines(final String[] lines) {
        if (lines.length == 0) {
            return false;
        }

        boolean empty = true;
        for (final String line : lines) {
            if (!line.isEmpty()) {
                empty = false;
            }
        }

        return !empty;
    }

}
