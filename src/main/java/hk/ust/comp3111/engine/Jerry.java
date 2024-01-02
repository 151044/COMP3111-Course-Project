package hk.ust.comp3111.engine;

import java.nio.file.Path;
import java.util.Map;

/**
 * Jerry, the pursued, in the game.
 */
public class Jerry extends Entity {
    private static final int CRYSTAL_TIME_DURATION = 25;

    private int crystal;

    /**
     * Constructs a new Jerry object.
     * @param imagePaths The map that stores Path to image sprites with keys of {@link ImagePath} type
     * @param state The default sprite state of Jerry
     */
    public Jerry(Map<ImagePath, Path> imagePaths, ImagePath state) {
        super(imagePaths, state);
        crystal = 0;
    }


    /**
     * Shows time left until crystal power for Jerry runs out.
     * @return The current crystal time count.
     */
    public int getCrystal() {return crystal;}

    /**
     * Activates Jerry's crystal power.
     */
    public void activateCrystal() {this.crystal = CRYSTAL_TIME_DURATION;}

    /**
     * Decreases the time counter for Jerry's crystal power.
     */
    public void decreaseCounter() {if (crystal > 0) --this.crystal;}
}
