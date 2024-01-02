package hk.ust.comp3111.ui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class ThemeManagerTest {

    @Test
    void setLookAndFeelHeadless() throws UnsupportedLookAndFeelException {
        FlatIntelliJLaf laf = new FlatIntelliJLaf();
        // target function: ThemeManager.setLookAndFeel(LookAndFeel)
        ThemeManager.setLookAndFeel(laf);
        assertEquals(UIManager.getLookAndFeel(), laf);

        // target function: ThemeManager.setLookAndFeel(LookAndFeel)
        assertThrows(UnsupportedLookAndFeelException.class,
                () -> ThemeManager.setLookAndFeel(new LookAndFeel() {
            @Override
            public String getName() {
                return "No You";
            }

            @Override
            public String getID() {
                return "NoU";
            }

            @Override
            public String getDescription() {
                return "The No You Look and Feel, designed for maximum incompatibility!";
            }

            @Override
            public boolean isNativeLookAndFeel() {
                return false;
            }

            @Override
            public boolean isSupportedLookAndFeel() {
                return false;
            }
        }));
    }

    @Test
    void setLookAndFeelFull() throws UnsupportedLookAndFeelException {
        assumeFalse(GraphicsEnvironment.isHeadless());
        JFrame f = new JFrame();
        f.setVisible(true);
        FlatIntelliJLaf laf = new FlatIntelliJLaf();
        // target function: ThemeManager.setLookAndFeel(LookAndFeel)
        ThemeManager.setLookAndFeel(laf);
        assertEquals(UIManager.getLookAndFeel(), laf);
        f.dispose();
    }

    @Test
    void privateConstructor() {
        Constructor<ThemeManager> cons;
        try {
            cons = ThemeManager.class.getDeclaredConstructor();
            cons.setAccessible(true);
            // target function: new ThemeManager()
            assertThrows(InvocationTargetException.class, cons::newInstance);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}