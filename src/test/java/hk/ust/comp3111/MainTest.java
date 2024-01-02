package hk.ust.comp3111;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class MainTest {
    @Test
    void mainTest() throws UnsupportedLookAndFeelException {
        assumeFalse(GraphicsEnvironment.isHeadless());
        // target function: Main.main(String[])
        Main.main(null);
        assertTrue(UIManager.getLookAndFeel() instanceof FlatMacDarkLaf);
        Thread.getDefaultUncaughtExceptionHandler()
                .uncaughtException(null, new RuntimeException());
        assertTrue(Arrays.stream(Window.getWindows())
                .anyMatch(w -> w instanceof JDialog));
        Arrays.stream(Window.getWindows()).forEach(Window::dispose);
    }

    @Test
    void getFrame() throws UnsupportedLookAndFeelException {
        assumeFalse(GraphicsEnvironment.isHeadless());
        Main.main(null);
        assertNotNull(Main.getFrame());
        // target function: Main.getFrame()
        Main.getFrame().dispose();
    }

    @Test
    void constructMain() {
        assertNotEquals(new Main(), new Main());
    }
}