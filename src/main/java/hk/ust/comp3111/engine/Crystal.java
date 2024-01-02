package hk.ust.comp3111.engine;

import java.nio.file.Path;
import java.util.Map;

/**
 * The crystal in the game.
 */
public class Crystal extends Entity{
    /**
     * Constructs a new Crystal object.
     * @param imagePaths The map that stores Path to image sprites with keys of {@link ImagePath} type
     * @param state The default sprite state of Crystal
     */
    public Crystal(Map<ImagePath, Path> imagePaths, ImagePath state) {
        super(imagePaths, state);
    }
}
