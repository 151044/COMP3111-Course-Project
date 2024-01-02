package hk.ust.comp3111.engine;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CrystalTest {
    final Map<ImagePath, Path> imagePaths = ImagePath.IMAGES;

    final ImagePath state = ImagePath.CRYSTAL;

    @Test
    void testCrystal(){
        // Target function: new Crystal(Map<ImagePath, Path>, ImagePath)
        Crystal c2 = new Crystal(imagePaths, state);
        assertEquals(state, c2.getImagePath());
    }

}