package hk.ust.comp3111.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Utility class for layout-related static methods.
 */
public class UIHelper {
    private UIHelper(){
        throw new AssertionError("UIHelper cannot be instantiated!");
    }

    /**
     * Factory for allocating JFileChoosers which can only choose .csv files.
     * @return JFileChoosers starting at current directory
     */
    public static JFileChooser getCsvOpener() {
        JFileChooser chooser = new JFileChooser(Path.of("").toAbsolutePath().toFile());
        chooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        return chooser;
    }

    /**
     * Constructs a reasonable set of defaults for use with {@link GridBagLayout}.
     * Grid heights and widths are set to {@link GridBagConstraints#REMAINDER}.
     * GridX and GridY are set to 0.
     * WeightX and WeightY are both 1.
     * Fill is equal to {@link GridBagConstraints#BOTH}.
     * Anchor is set to {@link GridBagConstraints#CENTER}.
     * @return A {@link GridBagConstraints} object, with the specified fields as above.
     */
    public static GridBagConstraints defaultGridConstraints() {
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridheight = GridBagConstraints.REMAINDER;
        cons.gridwidth = GridBagConstraints.REMAINDER;
        cons.gridx = 0;
        cons.gridy = 0;
        cons.weightx = 1;
        cons.weighty = 1;
        cons.anchor = GridBagConstraints.CENTER;
        cons.fill = GridBagConstraints.BOTH;
        return cons;
    }

    /**
     * Shows an error dialog which does not block any other tasks.
     * @param message The error message to show
     */
    public static void showErrorDialog(String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = pane.createDialog(null, "Error!");
        dialog.setModalityType(Dialog.ModalityType.MODELESS);
        dialog.setVisible(true);
    }

    /**
     * Shows a dialog to prompt the user to open a file.
     * @return An optional containing a {@link File} if the user chooses a file; an empty optional otherwise
     */
    public static Optional<File> showFileOpener() {
        JFileChooser chooser = getCsvOpener();
        int ret = chooser.showOpenDialog(null);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            if (f.exists()) {
                return Optional.of(f);
            }
        }
        return Optional.empty();
    }

    /**
     * Attempts to load an image from the specified path.
     * If the image cannot be found on the path, load from the executable using {@link Class#getResourceAsStream(String)}.
     * Shows an error dialog if the image cannot be loaded.
     * @param path The path to load images from
     * @return An image if loading is successful; null otherwise
     */
    public static BufferedImage readImage(Path path) {
        Objects.requireNonNull(path);
        try {
            if (!path.toFile().exists()) {
                String resource = path.toFile().getName();
                InputStream stream = UIHelper.class.getResourceAsStream("/" + resource);
                if (stream == null) {
                    throw new IOException("Unable to read " + resource);
                }
                return ImageIO.read(stream);
            } else {
                return ImageIO.read(path.toFile());
            }
        } catch (IOException e) {
            showErrorDialog(e.getMessage());
            return null;
        }
    }

    /**
     * Shows a save file dialog, then runs the action provided on success.
     * @param cons The action to run on success
     */
    public static void saveFile(Consumer<File> cons) {
        JFileChooser chooser = getCsvOpener();
        int ret = chooser.showSaveDialog(null);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            if (f.exists()) {
                int result = JOptionPane.showConfirmDialog(null, "Overwrite existing file?",
                        "Overwrite file?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            cons.accept(f);
            JOptionPane.showMessageDialog(null,
                    "File has been saved.", "Success!",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
