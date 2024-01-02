package hk.ust.comp3111.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JerryTest {
    final Map<ImagePath, Path> imagePaths = ImagePath.IMAGES;

    final ImagePath state = ImagePath.JERRY_LEFT;

    Jerry j1;
    Jerry j2;

    @BeforeEach
    void setUp(){
        j1 = new Jerry(imagePaths, state);
        j2 = new Jerry(imagePaths, state);
    }

    @Test
    void testJerry(){
        // Target function: new Jerry(Map<ImagePath, Path>, ImagePath)
        assertEquals(state, j1.getImagePath());
    }

    @Test
    void testGetCrystal(){
        // Target function: Jerry.getCrystal()
        assertEquals(0, j2.getCrystal());
    }

    @Test
    void testActivateCrystal() throws NoSuchFieldException, IllegalAccessException {
        Field f = Jerry.class.getDeclaredField("CRYSTAL_TIME_DURATION");
        f.setAccessible(true);
        // Target function: Jerry.activateCrystal()
        j1.activateCrystal();
        assertEquals(f.get(j1), j1.getCrystal());
    }

    @Test
    void testDecreaseCounter() throws NoSuchFieldException, IllegalAccessException {
        Field f = Jerry.class.getDeclaredField("CRYSTAL_TIME_DURATION");
        f.setAccessible(true);
        j1.activateCrystal();
        // Target function: Jerry.decreaseCounter()
        j1.decreaseCounter();
        assertEquals((int) f.get(j1) - 1, j1.getCrystal());
        // Target function: Jerry.decreaseCounter()
        j2.decreaseCounter();
        assertEquals(0, j2.getCrystal());
    }

}