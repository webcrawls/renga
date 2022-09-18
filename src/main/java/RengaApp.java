import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.template.JavalinPebble;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RengaApp {

    public static void main(String[] args) {
        new RengaApp(new ArrayList<>()).start();
    }


    private final List<Haiku> haikus;

    public RengaApp(final List<Haiku> haikus) {
        this.haikus = haikus;
    }

    public void start() {
        Javalin.create(cfg -> {
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
                    this.haikus.add(new Haiku(lines, new Author("anonymous"), Instant.now()));
                    final List<Haiku> haiku = new ArrayList<>(this.haikus);
                    Collections.reverse(haiku);
                    ctx.render("index.html", Map.of("haikus", haiku));
                })
                .start(7000);
    }

    public void save() {

    }

}
