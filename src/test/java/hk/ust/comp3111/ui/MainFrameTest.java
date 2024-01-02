package hk.ust.comp3111.ui;

import hk.ust.comp3111.reflect.Reflect;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class MainFrameTest {
    @Test
    void setPanel() throws NoSuchFieldException, IllegalAccessException {
        assumeFalse(GraphicsEnvironment.isHeadless());
        MainFrame frame = new MainFrame();
        JPanel hacks = Reflect.getField("current", JPanel.class, frame);
        assertTrue(hacks instanceof StartPanel);
        // target function: MainFrame.setPanel(ConstrainedPanel)
        frame.setPanel(new MapCreatorPanel());
        hacks = Reflect.getField("current", JPanel.class, frame);
        assertTrue(hacks instanceof MapCreatorPanel);
    }

    @Test
    void newMainFrame() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        // target function: new MainFrame()
        MainFrame frame = new MainFrame();
        assertInstanceOf(BorderLayout.class, frame.getLayout());
        assertEquals(JFrame.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
        frame.dispose();
    }
}