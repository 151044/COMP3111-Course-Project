package hk.ust.comp3111.engine;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TomTest {
    final Map<ImagePath, Path> imagePaths = ImagePath.IMAGES;

    final ImagePath state = ImagePath.TOM_LEFT;

    @Test
    void testTom(){
        Tom t1 = new Tom(imagePaths, state);
        // Target function: new Tom(Map<ImagePath, Path>, ImagePath)
        assertEquals(state, t1.getImagePath());
    }

}