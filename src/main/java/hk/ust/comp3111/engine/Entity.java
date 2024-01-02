package hk.ust.comp3111.engine;

import hk.ust.comp3111.ui.UIHelper;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.*;

/**
 * An entity in the game.
 */
public abstract class Entity {
    private BufferedImage image;
    private final Map<ImagePath, Path> imagePaths;
    private ImagePath state;

    /**
     * Constructs a new Entity object.
     * @param imagePaths The map that stores Path to image sprites with keys of {@link ImagePath} type
     * @param state The default sprite state of the Entity
     */
    public Entity(Map<ImagePath, Path> imagePaths, ImagePath state) {
        this.imagePaths = imagePaths;
        this.state = state;
        image = UIHelper.readImage(this.imagePaths.get(state));
    }

    /**
     * Returns the image of the Entity.
     * @return The image of the Entity if it exists, or an empty Optional otherwise.
     */
    public Optional<BufferedImage> getImage() {
        return image == null ? Optional.empty() : Optional.of(image);
    }

    public ImagePath getImagePath() {
        return state;
    }

    /**
     * Changes the image of the Entity
     */
    public void changeImage(ImagePath state){
        if (this.state == state) return;
        image = UIHelper.readImage(this.imagePaths.get(state));
        this.state = state;
    }
}
