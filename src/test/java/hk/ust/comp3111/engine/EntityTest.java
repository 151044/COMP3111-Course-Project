package hk.ust.comp3111.engine;

import hk.ust.comp3111.ui.UIHelper;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {
    final Map<ImagePath, Path> imagePaths = ImagePath.IMAGES;

    final ImagePath state1 = ImagePath.CRYSTAL;
    final ImagePath state2 = ImagePath.JERRY_RIGHT_CRYSTAL;

    final Entity e1 = new Entity(imagePaths, state1) {};
    final Entity e2 = new Entity(imagePaths, state2) {};
    final BufferedImage e1Image = UIHelper.readImage(state1.getPath());
    final BufferedImage e2Image = UIHelper.readImage(state2.getPath());
    byte[] e1Bytes;
    byte[] e2Bytes;

    void setUp() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(e1Image, "png", baos);
        e1Bytes = baos.toByteArray();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        ImageIO.write(e2Image, "png", baos2);
        e2Bytes = baos2.toByteArray();
    }

    @Test
    void testEntity() throws NoSuchFieldException {
        // Target function: new Entity(Map<ImagePath, Path>, ImagePath)
        Entity e3 = new Entity(imagePaths, state1) {};
        Field f = Entity.class.getDeclaredField("state");
        f.setAccessible(true);
        assertEquals(state1, e3.getImagePath());
    }



    @Test
    void testGetImage() throws IOException {
        setUp();
        // Target function: Entity.getImage()
        assertNotEquals(Optional.empty(), e1.getImage());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Target function: Entity.getImage()
        ImageIO.write(e1.getImage().orElseThrow(), "png", baos);
        byte[] e1Bytes2 = baos.toByteArray();

        assertArrayEquals(e1Bytes, e1Bytes2);

        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        // Target function: Entity.getImage()
        ImageIO.write(e2.getImage().orElseThrow(), "png", baos2);
        byte[] e2Bytes2 = baos2.toByteArray();

        assertArrayEquals(e2Bytes, e2Bytes2);
    }

    @Test
    void testGetImagePath(){
        // Target function: Entity.getImagePath()
        assertEquals(ImagePath.CRYSTAL, e1.getImagePath());
        // Target function: Entity.getImagePath()
        assertEquals(ImagePath.JERRY_RIGHT_CRYSTAL, e2.getImagePath());
    }

    @Test
    void testChangeImage(){
        // Target function: Entity.changeImage(ImagePath)
        e1.changeImage(ImagePath.CRYSTAL);
        assertEquals(ImagePath.CRYSTAL, e1.getImagePath());
        // Target function: Entity.changeImage(ImagePath)
        e1.changeImage(ImagePath.JERRY_LEFT);
        assertEquals(ImagePath.JERRY_LEFT, e1.getImagePath());
    }
}