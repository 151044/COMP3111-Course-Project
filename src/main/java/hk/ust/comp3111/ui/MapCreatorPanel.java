package hk.ust.comp3111.ui;

import static hk.ust.comp3111.api.Vertex.TileType.*;

import hk.ust.comp3111.Main;
import hk.ust.comp3111.algo.maze.MazeStrategy;
import hk.ust.comp3111.algo.maze.RandomizedPrim;
import hk.ust.comp3111.algo.maze.RecursiveBacktracker;
import hk.ust.comp3111.algo.maze.RecursiveDivision;
import hk.ust.comp3111.algo.shortestpath.BreadthFirstSearch;
import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.api.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * The panel used for creating maps.
 */
public class MapCreatorPanel extends ConstrainedPanel {
    private MapData mapData;
    private MapComponent component;
    private JPanel mapPanel;
    private Mode mode = Mode.CLICK;
    private Vertex start;
    private final Map<Vertex, Vertex.TileType> toRevert = new HashMap<>();
    private final BreadthFirstSearch search = new BreadthFirstSearch();
    private final List<JButton> generatingButtons = new ArrayList<>();

    /**
     * Constructs a new map creator panel.
     */
    public MapCreatorPanel() {
        setLayout(new BorderLayout());
        // Actual Map panel
        MouseHandler handler = new MouseHandler();
        component = new MapComponent();
        mapData = component.getData();
        mapPanel = component.getMainPanel();
        component.getPanels().stream().flatMap(List::stream).forEach(p -> {
            p.addMouseListener(handler);
            p.addMouseMotionListener(handler);
        });

        // Editor Panel
        JPanel editorPanel = new JPanel();
        editorPanel.setLayout(new BoxLayout(editorPanel, BoxLayout.X_AXIS));
        List<String> orderedButtons = List.of("Toggle", "Clear",
                "Fill", "Line", "Entrance", "Exit");
        EditButtonHandler buttonHandler = new EditButtonHandler();
        orderedButtons.forEach(s -> {
            JButton button = new JButton(s);
            editorPanel.add(button);
            button.addActionListener(buttonHandler);
        });

        // File Stuff Panel
        JPanel backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.X_AXIS));
        JButton loadButton = new JButton("Load");
        JButton saveButton = new JButton("Save");
        JButton backButton = new JButton("Back");
        backPanel.add(loadButton);
        backPanel.add(saveButton);
        backPanel.add(backButton);

        // Generate Maze Panel
        JPanel generatePanel = new JPanel();
        generatePanel.setLayout(new BoxLayout(generatePanel, BoxLayout.X_AXIS));
        generatePanel.add(new JLabel("Maze Generating Algorithms:"));
        GenerateButtonHandler generateHandler = new GenerateButtonHandler();
        List<String> generatingMethods = List.of("Recursive Division", "Prim's Algorithm", "Recursive Backtracking");
        generatingMethods.forEach(s -> {
            JButton button = new JButton(s);
            generatePanel.add(button);
            generatingButtons.add(button);
            button.addActionListener(generateHandler);
        });

        // Event Handling
        loadButton.addActionListener(ignored -> replace());
        backButton.addActionListener(ignored -> Main.getFrame().setPanel(new StartPanel()));
        saveButton.addActionListener(ignored -> save());

        // Add the panels
        add(mapPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.add(editorPanel);
        buttonsPanel.add(generatePanel);
        buttonsPanel.add(backPanel);
        add(buttonsPanel, BorderLayout.PAGE_END);
        component.render();
    }

    /**
     * The mouse event handler for {@link MapCreatorPanel}.
     */
    private class MouseHandler extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            switch (mode) {
                case CLICK -> {
                    if (e.getComponent() instanceof JPanel panel && component.hasPanel(panel)) {
                        Vertex v = component.getVertex(panel);
                        v.setTileType(v.getTileType() == BARRIER ? CLEAR : BARRIER);
                    }
                }
                case LINE -> {
                    if (e.getComponent() instanceof JPanel panel && component.hasPanel(panel)) {
                        start = component.getVertex(panel);
                        start.setTileType(start.getTileType() == BARRIER ? CLEAR : BARRIER);
                    }
                }
                case ENTRANCE -> {
                    if (e.getComponent() instanceof JPanel panel && component.hasPanel(panel)) {
                        Vertex v = component.getVertex(panel);
                        if (v.x() == 0 || v.x() == (MapData.DEFAULT_WIDTH - 1) || v.y() == 0 || v.y() == (MapData.DEFAULT_HEIGHT - 1)) {
                            mapData.forAll(vert -> {
                                if (vert.getTileType() == ENTRANCE) {
                                    vert.setTileType(BARRIER);
                                }
                            });
                            v.setTileType(ENTRANCE);
                        }
                    }
                }
                case EXIT -> {
                    if (e.getComponent() instanceof JPanel panel && component.hasPanel(panel)) {
                        Vertex v = component.getVertex(panel);
                        if (v.x() == 0 || v.x() == (MapData.DEFAULT_WIDTH - 1) || v.y() == 0 || v.y() == (MapData.DEFAULT_HEIGHT - 1)) {
                            mapData.forAll(vert -> {
                                if (vert.getTileType() == EXIT) {
                                    vert.setTileType(BARRIER);
                                }
                            });
                            v.setTileType(EXIT);
                        }
                    }
                }
            }
            component.render();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (mode == Mode.LINE) {
                if (start != null && e.getComponent() instanceof JPanel panel
                    && component.hasPanel(panel)) {
                    Objects.requireNonNull(start);
                    Vertex at = component.getVertex(panel);
                    toRevert.forEach(Vertex::setTileType);
                    toRevert.clear();
                    List<Vertex> toColor = mapData.lerp(start, at);
                    toColor.forEach(v -> toRevert.put(v, v.getTileType()));
                    toColor.forEach(v -> v.setTileType(start.getTileType()));
                }
                component.render();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (mode == Mode.LINE) {
                start = null;
                toRevert.clear();
            }
            component.render();
        }
    }

    /**
     * Map edit button handlers for {@link MapCreatorPanel}.
     */
    private class EditButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            switch (actionEvent.getActionCommand()) {
                case "Clear" -> mode = Mode.CLEAR;
                case "Toggle" -> mode = Mode.CLICK;
                case "Fill" -> mode = Mode.FILL;
                case "Line" -> mode = Mode.LINE;
                case "Entrance" -> mode = Mode.ENTRANCE;
                case "Exit" -> mode = Mode.EXIT;
            }
            switch (mode) {
                case CLEAR ->
                        mapData.forAll(v -> v.setTileType(CLEAR));
                case FILL ->
                        mapData.forAll(v -> v.setTileType(BARRIER));
            }
            component.render();
        }
    }

    /**
     * Maze generator button handler for {@link MapCreatorPanel}.
     */
    private class GenerateButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            MazeStrategy strategy = null;
            switch (actionEvent.getActionCommand()) {
                case "Recursive Division" -> strategy = new RecursiveDivision();
                case "Prim's Algorithm" -> strategy = new RandomizedPrim();
                case "Recursive Backtracking" -> strategy = new RecursiveBacktracker();
            }
            if (strategy != null) {
                strategy.onUpdate(ignored -> SwingUtilities.invokeLater(component::render));
                MazeStrategy finalStrategy = strategy;
                new Thread(() -> {
                    SwingUtilities.invokeLater(() -> generatingButtons.forEach(b -> b.setEnabled(false)));
                    finalStrategy.generate(mapData);
                    SwingUtilities.invokeLater(() -> generatingButtons.forEach(b -> b.setEnabled(true)));
                }).start();
            }
        }
    }

    /**
     * Mode that the panel is operating in.
     * Public for test reasons only - do not use.
     */
    public enum Mode {
        /**
         * The click mode for toggling squares.
         */
        CLICK,
        /**
         * The clear mode for clearing the entire grid.
         */
        CLEAR,
        /**
         * The fill mode for filling the entire grid.
         */
        FILL,
        /**
         * The line mode for quickly drawing lines.
         */
        LINE,
        /**
         * The entrance mode for placing entrance tiles.
         */
        ENTRANCE,
        /**
         * The exit mode for placing exit tiles.
         */
        EXIT
    }
    @Override
    public GridBagConstraints getConstraint() {
        GridBagConstraints cons = super.getConstraint();
        cons.fill = GridBagConstraints.BOTH;
        return cons;
    }

    /**
     * Saves the current map.
     */
    public void save() {
        if (mapData.findExit().isEmpty() || mapData.findEntrance().isEmpty()) {
            UIHelper.showErrorDialog("Missing entrance or exit!");
            return;
        }
        if (search.bfsGeneral(mapData, mapData.findEntrance().orElseThrow(),
                mapData.findExit().orElseThrow(), true).isEmpty()) {
            UIHelper.showErrorDialog("No path between entrance or exit!");
            return;
        }
        UIHelper.saveFile((f) -> {
            try {
                mapData.write(f.toPath());
            } catch (IOException e) {
                UIHelper.showErrorDialog("Unable to write file.");
            }
        });
    }

    /**
     * Replaces the current map with a .csv chosen by the user.
     */
    public void replace() {
        Optional<File> optFile = UIHelper.showFileOpener();
        if (optFile.isPresent()) {
            MouseHandler handler = new MouseHandler();
            try {
                component = new MapComponent(new MapData(optFile.get().toPath()));
            } catch (IOException e) {
                UIHelper.showErrorDialog(e.getMessage());
                return;
            }
            remove(mapPanel);
            mapData = component.getData();
            mapPanel = component.getMainPanel();
            component.getPanels().stream().flatMap(List::stream).forEach(p -> {
                p.addMouseListener(handler);
                p.addMouseMotionListener(handler);
            });
            add(mapPanel, BorderLayout.CENTER);
            invalidate();
            revalidate();
            repaint();
            component.render();
        } else {
            UIHelper.showErrorDialog("No such file!");
        }
    }
}
