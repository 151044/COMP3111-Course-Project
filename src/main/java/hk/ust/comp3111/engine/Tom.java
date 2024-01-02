package hk.ust.comp3111.engine;


import java.nio.file.Path;
import java.util.Map;

/**
 * Tom, the pursuer, in the game.
 */
public class Tom extends Entity {
    /**
     * Constructs a new Tom object.
     * @param imagePaths The map that stores Path to image sprites with keys of {@link ImagePath} type
     * @param state The default sprite state of Tom
     */
    public Tom(Map<ImagePath, Path> imagePaths, ImagePath state) {
        super(imagePaths, state);
    }
}
