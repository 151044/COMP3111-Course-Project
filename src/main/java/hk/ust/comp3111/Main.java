package hk.ust.comp3111;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import hk.ust.comp3111.engine.ImagePath;
import hk.ust.comp3111.ui.MainFrame;
import hk.ust.comp3111.ui.ThemeManager;
import hk.ust.comp3111.ui.UIHelper;

import javax.swing.*;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The main class.
 */
public class Main {
    private static MainFrame frame;

    /**
     * The main method.
     * @param args Command-line arguments; ignored
     */
    public static void main(String[] args) throws UnsupportedLookAndFeelException{
        Thread.setDefaultUncaughtExceptionHandler((thread, exception) ->
            UIHelper.showErrorDialog("An unhandled exception has occurred! "  + exception.getMessage() + "\n"
            + Arrays.stream(exception.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining("\n"))));

        ThemeManager.setLookAndFeel(new FlatMacDarkLaf());
        frame = new MainFrame();
        frame.setIconImage(UIHelper.readImage(ImagePath.TOM_RIGHT.getPath()));
        frame.setVisible(true);
    }

    /**
     * Gets the main frame of the application.
     * @return The main JFrame created
     */
    public static MainFrame getFrame() {
        return frame;
    }
}
