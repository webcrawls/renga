package sh.kaden.renga.data;

import org.jetbrains.annotations.NonNls;
import sh.kaden.renga.Haiku;

import java.util.List;

/**
 * {@code RengaLoader} loads and saves lists of Haikus.
 */
public interface RengaLoader {

    List<Haiku> loadHaikus();

    void saveHaikus(final List<Haiku> haikus);

}
