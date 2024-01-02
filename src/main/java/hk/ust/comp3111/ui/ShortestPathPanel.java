package hk.ust.comp3111.ui;

import hk.ust.comp3111.Main;
import hk.ust.comp3111.algo.shortestpath.BreadthFirstSearch;
import hk.ust.comp3111.api.Coord;
import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.api.Vertex;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * The panel for displaying the shortest path.
 */
public class ShortestPathPanel extends ConstrainedPanel {
    private final MapComponent comp;
    private final JTable table;
    private final BreadthFirstSearch bfs = new BreadthFirstSearch();
    private List<Vertex> shortestPath = new ArrayList<>();

    /**
     * Constructs a new shortest path panel.
     * @param data The map data to visualize
     */
    public ShortestPathPanel(MapData data) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.X_AXIS));
        this.comp = new MapComponent(data);
        displayPanel.add(comp.getMainPanel());

        // Panel for shortest paths
        JPanel pathDisplay = new JPanel();
        pathDisplay.setLayout(new BorderLayout());
        table = new JTable(new Vector<>(), new Vector<>(List.of("Column", "Row")));
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        pathDisplay.add(scrollPane);
        displayPanel.add(pathDisplay);

        JPanel exitPanel = new JPanel();
        exitPanel.setLayout(new BoxLayout(exitPanel, BoxLayout.X_AXIS));
        JButton exitButton = new JButton("Exit");
        JButton toCsvButton = new JButton("Save as CSV");
        exitPanel.add(exitButton);
        exitPanel.add(toCsvButton);

        add(displayPanel);
        add(exitPanel);

        exitButton.addActionListener(ignored -> Main.getFrame().setPanel(new StartPanel()));
        toCsvButton.addActionListener(ignored -> UIHelper.saveFile((f) -> {
            try {
                Files.write(f.toPath(), List.of(shortestPath.stream()
                        .map(v -> v.getCoord().translate(new Coord(1, 1)).toString()
                                .replace("(", "")
                                .replace(")", ""))
                        .collect(Collectors.joining(",\n"))));
            } catch (IOException e) {
                UIHelper.showErrorDialog("Unable to save file.");
            }
        }));
        new Thread(() -> {
            List<Vertex> vert = bfs.bfsGeneral(data, data.findEntrance().orElseThrow(), data.findExit().orElseThrow(), true);
            if (vert.size() > 1) {
                SwingUtilities.invokeLater(() -> setShortestPath(vert));
                comp.render();
            }
        }).start();
    }

    /**
     * Sets the shortest path displayed by this panel.
     * @param vert The list of vertices to display
     */
    public void setShortestPath(List<Vertex> vert) {
        comp.highlightPath(vert);
        shortestPath = vert;
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
            model.fireTableDataChanged();
        }
        vert.forEach(v -> model.addRow(new Vector<>(List.of(v.y() + 1, v.x() + 1))));
        model.fireTableDataChanged();
    }

    @Override
    public GridBagConstraints getConstraint() {
        GridBagConstraints cons = super.getConstraint();
        cons.fill = GridBagConstraints.BOTH;
        return cons;
    }
}
