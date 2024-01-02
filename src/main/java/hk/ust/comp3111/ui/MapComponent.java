package hk.ust.comp3111.ui;

import hk.ust.comp3111.api.Coord;
import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.api.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.function.Function;

import static hk.ust.comp3111.api.Vertex.TileType.BARRIER;

/**
 * A component containing the game map.
 * Holds map data and the display panel.
 */
public class MapComponent {
    /**
     * Defines the color for the shortest path.
     */
    public static final Color SHORTEST_PATH_COLOR = Color.RED;
    private final MapData mapData;
    private final JPanel mapPanel = new JPanel();
    private final Map<JPanel, Vertex> lookupVertex = new HashMap<>();
    private final List<List<JPanel>> panels = new ArrayList<>();
    private final List<Vertex> shortestPath = new ArrayList<>();
    /**
     * The pixels per panel.
     */
    public static final int PX = 30;

    /**
     * Constructs a new MapComponent with all vertices being barriers.
     */
    public MapComponent() {
        mapPanel.setLayout(new GridLayout(MapData.DEFAULT_HEIGHT + 1, MapData.DEFAULT_WIDTH + 1));
        List<List<Vertex>> vertices = init(c -> new Vertex(c, BARRIER));
        mapData = new MapData(vertices);
        render();
    }

    /**
     * Constructs a new MapComponent with the specified MapData.
     * @param data The map data to use
     */
    public MapComponent(MapData data) {
        Objects.requireNonNull(data);
        mapData = data;
        init(c -> data.at(c).orElseThrow());
        render();
    }

    private List<List<Vertex>> init(Function<Coord, Vertex> func) {
        mapPanel.setLayout(new GridLayout(MapData.DEFAULT_HEIGHT + 1, MapData.DEFAULT_WIDTH + 1));
        List<List<Vertex>> vertices = new ArrayList<>();
        // Top numbers
        for (int i = 0; i <= MapData.DEFAULT_WIDTH; i++) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setPreferredSize(new Dimension(PX, PX));
            if (i != 0) {
                JLabel label = new JLabel(Integer.toString(i));
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(label);
            }
            mapPanel.add(panel);
        }
        for (int i = 0; i < MapData.DEFAULT_HEIGHT; i++) {
            List<JPanel> panel = new ArrayList<>();
            List<Vertex> vertex = new ArrayList<>();
            for (int j = 0; j < MapData.DEFAULT_WIDTH; j++) {
                Vertex vert = func.apply(new Coord(j, i));
                vertex.add(vert);
                JPanel jPanel = new JPanel(new BorderLayout());
                jPanel.setSize(PX, PX);
                jPanel.setPreferredSize(new Dimension(PX, PX));
                panel.add(jPanel);
                lookupVertex.put(jPanel, vert);
            }
            vertices.add(vertex);
            panels.add(panel);
        }

        for (int i = 0; i < MapData.DEFAULT_HEIGHT; i++) {
            // Side numbers
            JPanel labelPanel = new JPanel();
            JLabel label = new JLabel(Integer.toString(i + 1));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            labelPanel.add(label);
            mapPanel.add(labelPanel);
            for (int j = 0; j < MapData.DEFAULT_WIDTH; j++) {
                mapPanel.add(panels.get(i).get(j));
            }
        }
        return vertices;
    }

    /**
     * Gets the map panel to be added to the UI.
     * @return The map panel of this MapComponent
     */
    public JPanel getMainPanel() {
        return mapPanel;
    }

    /**
     * Gets the {@link JPanel} at the specified coordinate.
     * The coordinates are in (column, row) = (x, y).
     * @param c The coordinates of the JPanel, relative to the MapData
     * @return The retrieved {@link JPanel}
     */
    public JPanel at(Coord c) {
        return panels.get(c.y()).get(c.x());
    }

    /**
     * Checks if the {@link JPanel} exists in this MapComponent.
     * @param panel The panel to check
     * @return True if the panel belongs to this component, false otherwise
     */
    public boolean hasPanel(JPanel panel) {
        return lookupVertex.containsKey(panel);
    }

    /**
     * Gets the vertex associated with this {@link JPanel}.
     * No checks on whether this JPanel is associated with anything are performed.
     * Use {@link #hasPanel(JPanel)} to make this check first.
     * @param panel The panel to get the associated vertex for
     * @return The vertex corresponding to this panel
     */
    public Vertex getVertex(JPanel panel) {
        return lookupVertex.get(panel);
    }

    /**
     * Gets the underlying map data of this component.
     * @return The map data instance
     */
    public MapData getData() {
        return mapData;
    }

    /**
     * Gets all the {@link JPanel}s associated with a maze square.
     * @return The list of all {@link JPanel}s in the maze
     */
    public List<List<JPanel>> getPanels() {
        return panels;
    }

    /**
     * Updates all the vertices of the MapComponent to reflect changes in the underlying map data.
     * Should be called from the event dispatch thread;
     * use methods such as {@link SwingUtilities#invokeLater(Runnable)} if your code executes in a different thread.
     */
    public void render() {
        for (int i = 0; i < MapData.DEFAULT_HEIGHT; i++) {
            for (int j = 0; j < MapData.DEFAULT_WIDTH; j++) {
                JPanel panel = at(new Coord(j, i));
                mapData.at(new Coord(j, i)).ifPresent(v -> {
                    panel.removeAll();
                    if (shortestPath.contains(v)) {
                        panel.setBackground(SHORTEST_PATH_COLOR);
                    } else {
                        panel.setBackground(v.getTileType().getColor());
                    }
                });
            }
        }
        mapData.getEntities().forEach(e -> {
            if (e.getImage().isPresent()) {
                Vertex v = mapData.entityLocation(e);
                Optional<BufferedImage> image = e.getImage();
                ImageIcon icon = new ImageIcon(image.orElseThrow());
                at(v.getCoord()).add(new JLabel(icon));
            }
        });
    }

    /**
     * Highlights the given path, and de-highlights the previous path.
     * @param vertices The path to highlight
     */
    public void highlightPath(List<Vertex> vertices) {
        mapData.findEntrance().ifPresent(vertices::remove);
        mapData.findExit().ifPresent(vertices::remove);
        shortestPath.clear();
        shortestPath.addAll(vertices);
        render();
    }
}
