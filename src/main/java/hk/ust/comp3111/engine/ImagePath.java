package hk.ust.comp3111.engine;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Possible states of image sprites.
 */
public enum ImagePath {
    /**
     * The crystal.
     */
    CRYSTAL("sprCrystal.png"),
    /**
     * When Jerry faces left.
     */
    JERRY_LEFT("sprJerryLeft.png"),
    /**
     * When Jerry faces right.
     */
    JERRY_RIGHT("sprJerryRight.png"),
    /**
     * When Jerry has crystal power and faces left.
     */
    JERRY_LEFT_CRYSTAL("sprJerryCrystalLeft.png"),
    /**
     * When Jerry has crystal power and faces right.
     */
    JERRY_RIGHT_CRYSTAL("sprJerryCrystalRight.png"),
    /**
     * When Tom faces left.
     */
    TOM_LEFT("sprTomLeft.png"),
    /**
     * When Tom faces left.
     */
    TOM_RIGHT("sprTomRight.png"),
    /**
     * When Jerry faces left and wins.
     */
    JERRY_WIN_LEFT("sprJerryWinLeft.png"),
    /**
     * When Jerry faces right and wins.
     */
    JERRY_WIN_RIGHT("sprJerryWinRight.png"),
    /**
     * When Tom faces left and wins.
     */
    TOM_WIN_LEFT("sprTomWinLeft.png"),
    /**
     * When Tom faces right and wins.
     */
    TOM_WIN_RIGHT("sprTomWinRight.png");

    private final Path p;

    /**
     * Constructs a new ImagePath with the specified file name.
     * Prepends "image" + {@link java.io.File#separator} to the path received.
     * @param path The path to add
     */
    ImagePath(String path) {
        this.p = Path.of("images" + File.separator + path);
    }

    /**
     * Gets the path associated with this ImagePath.
     * @return The associated path
     */
    public Path getPath() {
        return p;
    }

    /**
     * The set of image paths.
     */
    public static final Map<ImagePath, Path> IMAGES = Arrays.stream(ImagePath.values())
            .collect(Collectors.toMap(Function.identity(), ImagePath::getPath));
}
