package hk.ust.comp3111.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class for setting the UI theme.
 */
public class ThemeManager {
    private ThemeManager(){
        throw new AssertionError("ThemeManager cannot be instantiated!");
    }

    /**
     * Sets the theme of the UI components.
     * @param laf The Look and Feel (theme) to set
     * @throws UnsupportedLookAndFeelException If the look and feel given is not compatible with this platform
     */
    public static void setLookAndFeel(LookAndFeel laf) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(laf);

        // Updates all the windows to use the new look and feel
        for (Window w : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(w);
            w.pack();
        }
    }
}
