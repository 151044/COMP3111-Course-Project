package hk.ust.comp3111.ui;

import hk.ust.comp3111.Main;
import hk.ust.comp3111.engine.Engine;
import hk.ust.comp3111.engine.GameStatus;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.awt.event.KeyEvent.*;

/**
 * The panel for playing the game.
 */
public class GamePanel extends ConstrainedPanel {
    private static final String TOM_WINS_MSG = "You lose! Try again next time!";
    private static final String JERRY_WINS_MSG = "You Win! Well done!";
    /**
     * The input delay per keystroke, in milliseconds.
     */
    private static final long INPUT_DELAY = 200L;
    /**
     * The time between each tick, in milliseconds.
     */
    private static final long TICK_DELAY = 200L;
    private final AtomicBoolean inputBool = new AtomicBoolean(true);
    private final AtomicBoolean isDone = new AtomicBoolean(false);
    private final MapComponent component;
    private final Engine e;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final JPanel map;

    /**
     * Constructs a new game panel with the specified engine instance for running the game.
     * @param e The engine to use
     */
    public GamePanel(Engine e) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.e = e;
        component = new MapComponent(e.getMapData());
        map = component.getMainPanel();
        add(map);
        setPreferredSize(new Dimension(1000, 1000)); // hacks
        start();
        Map<Integer, String> keyToAction = Map.of(
                VK_W, "W",
                VK_UP, "W",
                VK_A, "A",
                VK_LEFT, "A",
                VK_S, "S",
                VK_DOWN, "S",
                VK_D, "D",
                VK_RIGHT, "D"
        );
        keyToAction.forEach((i, s) -> {
            map.getInputMap().put(KeyStroke.getKeyStroke(i, 0), s);
            map.getActionMap().put(s, new KeyAction(s, (str) -> {
                if (inputBool.get()) {
                    e.changeDirection(Direction.toDirection(str));
                    inputBool.set(false);
                }
            }));
        });
    }

    @Override
    public GridBagConstraints getConstraint() {
        GridBagConstraints cons = super.getConstraint();
        cons.fill = GridBagConstraints.BOTH;
        return cons;
    }

    /**
     * Ticks and renders the changes to the game panel.
     * Do not call this method unless you are a test.
     */
    private void tick() {
        if (!isDone.get()) {
            GameStatus stat = e.tick();
            SwingUtilities.invokeLater(() -> {
                component.render();
                invalidate();
                revalidate();
                repaint();
            });
            switch (stat) {
                case TOM_WINS -> {
                    JOptionPane.showMessageDialog(null,
                            TOM_WINS_MSG,
                            "You lose!",
                            JOptionPane.INFORMATION_MESSAGE);
                    isDone.set(true);
                }
                case JERRY_WINS -> {
                    JOptionPane.showMessageDialog(null,
                            JERRY_WINS_MSG,
                            "You win!",
                            JOptionPane.INFORMATION_MESSAGE);
                    isDone.set(true);
                }
            }
        }
        if (isDone.get()) {
            Main.getFrame().setPanel(new StartPanel());
            scheduler.shutdown();
        }
    }

    /**
     * Starts the scheduler for running the game.
     * Do not call this method unless you are a test.
     * Provided only for test to override.
     */
    public void start() {
        scheduler.scheduleAtFixedRate(this::tick, TICK_DELAY, TICK_DELAY, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(() -> inputBool.set(true), 0, INPUT_DELAY, TimeUnit.MILLISECONDS);
    }
}
