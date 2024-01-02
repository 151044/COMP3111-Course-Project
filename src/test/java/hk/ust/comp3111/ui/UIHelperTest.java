package hk.ust.comp3111.ui;

import hk.ust.comp3111.engine.ImagePath;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UIHelperTest {

    @Test
    void defaultGridConstraints() {
        // target function: UIHelper.defaultGridConstraints()
        GridBagConstraints cons = UIHelper.defaultGridConstraints();
        assertEquals(GridBagConstraints.BOTH, cons.fill);
        assertEquals(GridBagConstraints.REMAINDER, cons.gridheight);
        assertNotEquals(0, cons.weightx);
    }

    @Test
    void showErrorDialog() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        // target function: UIHelper.showErrorDialog(String)
        UIHelper.showErrorDialog("Bad error!");
        assertTrue(Arrays.stream(Window.getWindows())
                .anyMatch(w -> w instanceof JDialog));
        Arrays.stream(Window.getWindows()).forEach(Window::dispose);
    }

    @Test
    void loadImage() {
        // target function: UIHelper.readImage(Path)
        assertNotNull(UIHelper.readImage(ImagePath.TOM_LEFT.getPath()));
        // target function: UIHelper.readImage(Path)
        assertNotNull(UIHelper.readImage(Path.of("/images/sprTomRight.png")));
    }

    @Test
    void loadImageFailure() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        // target function: UIHelper.readImage(Path)
        assertNull(UIHelper.readImage(Path.of("void_haha.png")));
        assertTrue(Arrays.stream(Window.getWindows())
                .anyMatch(w -> w instanceof JDialog));
        Arrays.stream(Window.getWindows()).forEach(Window::dispose);
    }

    @Test
    void showFileOpener() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        try (MockedStatic<UIHelper> uiHelper = Mockito.mockStatic(UIHelper.class)) {
            JFileChooser mocked = Mockito.mock(JFileChooser.class);
            when(mocked.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
            when(mocked.getSelectedFile()).thenReturn(new File("test-inputs" + File.separator + "maze-one.csv"));
            uiHelper.when(UIHelper::getCsvOpener).thenReturn(mocked);
            uiHelper.when(UIHelper::showFileOpener).thenCallRealMethod();
            // target function: UIHelper.showFileOpener()
            Optional<File> f = UIHelper.showFileOpener();
            assertEquals("maze-one.csv", f.orElseThrow().getName());

            when(mocked.showOpenDialog(null)).thenReturn(JFileChooser.CANCEL_OPTION);
            assertTrue(UIHelper.showFileOpener().isEmpty());
        }
    }

    @Test
    void privateConstructor() {
        Constructor<UIHelper> cons;
        try {
            cons = UIHelper.class.getDeclaredConstructor();
            cons.setAccessible(true);
            // target function: new UIHelper()
            assertThrows(InvocationTargetException.class, cons::newInstance);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getCsvChooser() {
        // target function: UIHelper.getCsvOpener()
        JFileChooser chooser = UIHelper.getCsvOpener();
        assertFalse(chooser.accept(new File("b")));
        assertTrue(chooser.accept(new File("test-inputs" + File.separator + "maze-one.csv")));
    }

    @Test
    void saveFile() {
        try (MockedStatic<UIHelper> uiHelper = Mockito.mockStatic(UIHelper.class);
                MockedStatic<JOptionPane> optionPane = Mockito.mockStatic(JOptionPane.class)) {
            optionPane.when(() -> JOptionPane.showConfirmDialog(null, "Overwrite existing file?",
                            "Overwrite file?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE))
                            .thenReturn(JOptionPane.YES_OPTION);
            uiHelper.when(() -> UIHelper.saveFile(any()))
                    .thenCallRealMethod();

            JFileChooser mockedChooser = Mockito.mock(JFileChooser.class);
            when(mockedChooser.showSaveDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
            when(mockedChooser.getSelectedFile())
                    .thenReturn(new File("test-inputs" + File.separator + "maze-three.csv"));
            uiHelper.when(UIHelper::getCsvOpener).thenReturn(mockedChooser);
            AtomicBoolean atomic = new AtomicBoolean(false);
            // target function: UIHelper.saveFile(Consumer<File>)
            UIHelper.saveFile((ignored) -> atomic.set(true));
            assertTrue(atomic.get());
        }
    }
}

