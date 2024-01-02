package hk.ust.comp3111.ui;

import hk.ust.comp3111.Main;
import hk.ust.comp3111.api.MapDataHelper;
import hk.ust.comp3111.engine.Engine;
import hk.ust.comp3111.engine.GameStatus;
import hk.ust.comp3111.engine.ImagePath;
import hk.ust.comp3111.reflect.Reflect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class GamePanelTest {
    private Engine e;
    private GamePanel panel;
    void runTick(GamePanel panel) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method m = GamePanel.class.getDeclaredMethod("tick");
        m.setAccessible(true);
        m.invoke(panel);
    }
    @BeforeEach
    void setup() {
        e = new Engine(MapDataHelper.getMap(), ImagePath.IMAGES);
        panel = new GamePanel(e);
    }

    @Test
    void getConstraint() {
        assertEquals(GridBagConstraints.BOTH, panel.getConstraint().fill);
        assertNotEquals(UIHelper.defaultGridConstraints(), panel.getConstraint());
    }

    @Test
    void engineWinTest() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InterruptedException {
        assumeFalse(GraphicsEnvironment.isHeadless());
        Engine injectedEngine = new Engine(MapDataHelper.getMap(), ImagePath.IMAGES) {
            @Override
            public GameStatus tick() {
                return GameStatus.JERRY_WINS;
            }
        };
        GamePanel notRunning = new GamePanel(injectedEngine) {
            @Override
            public void start() {
                // do nothing
            }
        };
        try (MockedStatic<Main> mainMocked = Mockito.mockStatic(Main.class)) {
            mainMocked.when(Main::getFrame).thenReturn(new MainFrame());
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                assertTrue(Arrays.stream(Window.getWindows())
                        .anyMatch(w -> w instanceof JDialog));
                Arrays.stream(Window.getWindows()).forEach(Window::dispose);
            });
            t.start();
            // target function: GamePanel.tick() (indirect call)
            runTick(notRunning);
            t.join();
        }
    }

    @Test
    void engineLoseTest() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InterruptedException {
        assumeFalse(GraphicsEnvironment.isHeadless());
        Engine injectedEngine = new Engine(MapDataHelper.getMap(), ImagePath.IMAGES) {
            @Override
            public GameStatus tick() {
                return GameStatus.TOM_WINS;
            }
        };
        GamePanel notRunning = new GamePanel(injectedEngine) {
            @Override
            public void start() {
                // do nothing
            }
        };
        try (MockedStatic<Main> mainMocked = Mockito.mockStatic(Main.class)) {
            mainMocked.when(Main::getFrame).thenReturn(new MainFrame());
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                assertTrue(Arrays.stream(Window.getWindows())
                        .anyMatch(w -> w instanceof JDialog));
                Arrays.stream(Window.getWindows()).forEach(Window::dispose);
            });
            t.start();
            // target function: GamePanel.tick() (indirect call)
            runTick(notRunning);
            t.join();
        }
    }

    @Test
    void inputHandlingTest() throws NoSuchFieldException, IllegalAccessException {
        Engine injectedEngine = new Engine(MapDataHelper.getMap(), ImagePath.IMAGES) {
            @Override
            public GameStatus tick() {
                return GameStatus.TOM_WINS;
            }
        };
        GamePanel notRunning = new GamePanel(injectedEngine);
        AtomicBoolean inputBool = Reflect.getField("inputBool", AtomicBoolean.class, notRunning);

        JPanel panel = Reflect.getField("map", JPanel.class, notRunning);
        // target function: GamePanel.keyToAction.forEach$Consumer(String)
        panel.getActionMap().get("W").actionPerformed(null);
        assertFalse(inputBool.get());
    }

    @Test
    void startTest() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        Engine injectedEngine = new Engine(MapDataHelper.getMap(), ImagePath.IMAGES) {
            @Override
            public GameStatus tick() {
                return GameStatus.TOM_WINS;
            }
        };
        GamePanel notRunning = new GamePanel(injectedEngine);
        AtomicBoolean inputBool = Reflect.getField("inputBool", AtomicBoolean.class, notRunning);

        inputBool.set(false);
        // target function: GamePanel.start()
        notRunning.start();
        Thread.sleep(100);
        assertTrue(inputBool.get());
    }

    @Test
    void newGamePanel() {
        Engine injectedEngine = new Engine(MapDataHelper.getMap(), ImagePath.IMAGES) {
            @Override
            public GameStatus tick() {
                return GameStatus.TOM_WINS;
            }
        };
        // target function: new GamePanel()
        GamePanel panel = new GamePanel(injectedEngine);
        assertInstanceOf(BoxLayout.class, panel.getLayout());
    }
}