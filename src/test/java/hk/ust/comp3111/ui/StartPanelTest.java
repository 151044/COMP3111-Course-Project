package hk.ust.comp3111.ui;

import hk.ust.comp3111.Main;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class StartPanelTest {
    @BeforeAll
    static void updateMain() throws UnsupportedLookAndFeelException {
        assumeFalse(GraphicsEnvironment.isHeadless());
        Main.main(null);
    }
    JButton getButton(StartPanel panel, String str) {
        return Arrays.stream(panel.getComponents())
                .filter(c -> c instanceof JButton)
                .map(c -> (JButton) c)
                .filter(c -> c.getText().equals(str))
                .findFirst()
                .orElseThrow();
    }
    @Test
    void shortestPathTest() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        StartPanel panel = new StartPanel();
        JButton button = getButton(panel, "Shortest Path");
        try (MockedStatic<UIHelper> uiHelper = Mockito.mockStatic(UIHelper.class)) {
            uiHelper.when(UIHelper::defaultGridConstraints)
                            .thenCallRealMethod();
            uiHelper.when(UIHelper::showFileOpener)
                    .thenReturn(Optional.of(new File("test-inputs" + File.separator + "maze-one.csv")));
            // target function: StartPanel.shortestButton$actionPerformed(ActionEvent)
            button.doClick();
        }
    }

    @Test
    void shortestPathTestFailure() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        StartPanel panel = new StartPanel();
        JButton button = getButton(panel, "Shortest Path");
        try (MockedStatic<UIHelper> uiHelper = Mockito.mockStatic(UIHelper.class)) {
            uiHelper.when(UIHelper::showFileOpener)
                    .thenReturn(Optional.of(new File("test-inputs" + File.separator + "not-a-csv.csv")));
            // target function: StartPanel.shortestButton$actionPerformed(ActionEvent)
            button.doClick();
        }
    }

    @Test
    void loadNonExistentFailure() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        StartPanel panel = new StartPanel();
        JButton button = getButton(panel, "Start Game");
        try (MockedStatic<UIHelper> uiHelper = Mockito.mockStatic(UIHelper.class)) {
            uiHelper.when(UIHelper::showFileOpener)
                    .thenReturn(Optional.empty());
            uiHelper.when(() -> UIHelper.showErrorDialog(Mockito.anyString()))
                    .thenCallRealMethod();
            // target function: StartPanel.playButton$actionPerformed(ActionEvent)
            button.doClick();
        }
        assertTrue(Arrays.stream(Window.getWindows())
                .anyMatch(w -> w instanceof JDialog));
        Arrays.stream(Window.getWindows()).forEach(Window::dispose);
    }

    @Test
    void loadReadFailure() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        StartPanel panel = new StartPanel();
        JButton button = getButton(panel, "Start Game");
        try (MockedStatic<UIHelper> uiHelper = Mockito.mockStatic(UIHelper.class)) {
            uiHelper.when(UIHelper::showFileOpener)
                    .thenReturn(Optional.of(new File("test-inputs" + File.separator + "not-a-csv.csv")));
            uiHelper.when(() -> UIHelper.showErrorDialog(Mockito.anyString()))
                    .thenCallRealMethod();
            // target function: StartPanel.playButton$actionPerformed(ActionEvent)
            button.doClick();
        }
        assertTrue(Arrays.stream(Window.getWindows())
                .anyMatch(w -> w instanceof JDialog));
        Arrays.stream(Window.getWindows()).forEach(Window::dispose);
    }

    @Test
    void loadInvalidMapFailure() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        StartPanel panel = new StartPanel();
        JButton button = getButton(panel, "Start Game");
        try (MockedStatic<UIHelper> uiHelper = Mockito.mockStatic(UIHelper.class)) {
            uiHelper.when(UIHelper::defaultGridConstraints)
                    .thenCallRealMethod();
            uiHelper.when(() -> UIHelper.showErrorDialog(Mockito.anyString()))
                            .thenCallRealMethod();
            uiHelper.when(UIHelper::showFileOpener)
                    .thenReturn(Optional.of(new File("test-inputs" + File.separator + "maze-two.csv")));
            // target function: StartPanel.playButton$actionPerformed(ActionEvent)
            button.doClick();
        }
        assertTrue(Arrays.stream(Window.getWindows())
                .anyMatch(w -> w instanceof JDialog));
        Arrays.stream(Window.getWindows()).forEach(Window::dispose);
    }

    @Test
    void loadSuccess() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        StartPanel panel = new StartPanel();
        JButton button = getButton(panel, "Start Game");
        try (MockedStatic<UIHelper> uiHelper = Mockito.mockStatic(UIHelper.class)) {
            uiHelper.when(UIHelper::defaultGridConstraints)
                            .thenCallRealMethod();
            uiHelper.when(UIHelper::showFileOpener)
                    .thenReturn(Optional.of(new File("test-inputs" + File.separator + "maze-one.csv")));
            // target function: StartPanel.playButton$actionPerformed(ActionEvent)
            button.doClick();
        }
    }

    @Test
    void newStartPanel() {
        // target function: new StartPanel()
        StartPanel panel = new StartPanel();
        assertInstanceOf(GridBagLayout.class, panel.getLayout());
    }

    @Test
    void addAndIncrement() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method m = StartPanel.class.getDeclaredMethod("addAndIncrement", JComponent.class, GridBagConstraints.class);
        m.setAccessible(true);
        StartPanel panel = new StartPanel();
        GridBagConstraints cons = UIHelper.defaultGridConstraints();
        JLabel randomLabel = new JLabel("Random Label Text");
        // target function: StartPanel.addAndIncrement(JComponent, GridBagConstraints)
        m.invoke(panel, randomLabel, cons);
        assertTrue(Arrays.asList(panel.getComponents()).contains(randomLabel));
        assertEquals(1, cons.gridy);
    }
}