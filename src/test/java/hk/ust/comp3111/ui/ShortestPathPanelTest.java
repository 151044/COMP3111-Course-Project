package hk.ust.comp3111.ui;

import hk.ust.comp3111.api.Coord;
import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.api.MapDataHelper;
import hk.ust.comp3111.reflect.Reflect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.when;

class ShortestPathPanelTest {
    private ShortestPathPanel panel;
    private final CyclicBarrier barrier = new CyclicBarrier(2);

    @BeforeEach
    void setup() {
        panel = new ShortestPathPanel(MapDataHelper.getMap());
    }
    @Test
    void setShortestPath() throws NoSuchFieldException, IllegalAccessException, InterruptedException, BrokenBarrierException {
        assumeFalse(GraphicsEnvironment.isHeadless());
        MapComponent comp = Reflect.getField("comp", MapComponent.class, panel);
        JTable table = Reflect.getField("table", JTable.class, panel);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        MapData data = comp.getData();
        SwingUtilities.invokeLater(() -> {
            // target function: ShortestPathPanel.setShortestPath(List<Vertex>)
            panel.setShortestPath(IntStream.range(2, 7).mapToObj(i -> data.at(new Coord(i, i)))
                    .flatMap(Optional::stream).collect(Collectors.toList()));
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        });
        barrier.await();
        barrier.reset();
        assertNotEquals(0, model.getRowCount());
        assertEquals(MapComponent.SHORTEST_PATH_COLOR, comp.at(new Coord(3, 3)).getBackground());
        SwingUtilities.invokeLater(() -> {
            // target function: ShortestPathPanel.setShortestPath(List<Vertex>)
            panel.setShortestPath(IntStream.range(4, 10).mapToObj(i -> data.at(new Coord(i, i)))
                    .flatMap(Optional::stream).collect(Collectors.toList()));
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        });
        barrier.await();
        assertNotEquals(0, model.getRowCount());
        assertNotEquals(MapComponent.SHORTEST_PATH_COLOR, comp.at(new Coord(2, 2)).getBackground());
    }

    @Test
    void getConstraint() {
        // target function: ShortestPathPanel.getConstraint()
        assertEquals(GridBagConstraints.BOTH, panel.getConstraint().fill);
        // target function: ShortestPathPanel.getConstraint()
        assertNotEquals(UIHelper.defaultGridConstraints(), panel.getConstraint());
    }

    @Test
    void newShortestPathPanel() {
        // target function: new ShortestPathPanel()
        ShortestPathPanel panel = new ShortestPathPanel(MapDataHelper.getMap());
        assertInstanceOf(BoxLayout.class, panel.getLayout());
    }

    @Test
    void writeVertices() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        JButton saveButton = Arrays.stream(panel.getComponents())
                        .filter(c -> c instanceof JPanel j
                            && Arrays.stream(j.getComponents())
                                .anyMatch(comp -> comp instanceof JButton))
                        .flatMap(c -> Arrays.stream(((JPanel) c).getComponents()))
                        .filter(c -> c instanceof JButton button && button.getText().equals("Save as CSV"))
                        .map(c -> (JButton) c)
                        .findFirst().orElseThrow();

        JFileChooser mockedChooser = Mockito.mock(JFileChooser.class);
        when(mockedChooser.showSaveDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        File file = new File("test-inputs" + File.separator + "maze-path.csv");
        when(mockedChooser.getSelectedFile()).thenReturn(file);

        try (MockedStatic<JOptionPane> optionPane = Mockito.mockStatic(JOptionPane.class);
             MockedStatic<UIHelper> uiHelper = Mockito.mockStatic(UIHelper.class)) {
            optionPane.when(() -> JOptionPane.showConfirmDialog(null, "Overwrite existing file?",
                            "Overwrite file?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE))
                    .thenReturn(JOptionPane.YES_OPTION);
            uiHelper.when(UIHelper::getCsvOpener).thenReturn(mockedChooser);

            uiHelper.when(() -> UIHelper.saveFile(any())).thenCallRealMethod();
            uiHelper.when(() -> UIHelper.showErrorDialog(any())).thenCallRealMethod();
            // target function: ShortestPathPanel.toCsvButton$actionPerformed(ActionEvent)
            saveButton.doClick();
        }
        assertTrue(file.exists());
    }

    @Test
    void writeVerticesFailure() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        JButton saveButton = Arrays.stream(panel.getComponents())
                .filter(c -> c instanceof JPanel j
                        && Arrays.stream(j.getComponents())
                        .anyMatch(comp -> comp instanceof JButton))
                .flatMap(c -> Arrays.stream(((JPanel) c).getComponents()))
                .filter(c -> c instanceof JButton button && button.getText().equals("Save as CSV"))
                .map(c -> (JButton) c)
                .findFirst().orElseThrow();

        JFileChooser mockedChooser = Mockito.mock(JFileChooser.class);
        when(mockedChooser.showSaveDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(mockedChooser.getSelectedFile()).thenReturn(new File("test-inputs" + File.separator + "maze-path.csv"));

        try (MockedStatic<JOptionPane> optionPane = Mockito.mockStatic(JOptionPane.class);
             MockedStatic<UIHelper> uiHelper = Mockito.mockStatic(UIHelper.class);
             MockedStatic<Files> files = Mockito.mockStatic(Files.class)) {
            optionPane.when(() -> JOptionPane.showConfirmDialog(null, "Overwrite existing file?",
                            "Overwrite file?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE))
                    .thenReturn(JOptionPane.YES_OPTION);
            uiHelper.when(UIHelper::getCsvOpener).thenReturn(mockedChooser);
            files.when(() -> Files.write(any(), anyCollection())).thenThrow(IOException.class);

            uiHelper.when(() -> UIHelper.saveFile(any())).thenCallRealMethod();
            uiHelper.when(() -> UIHelper.showErrorDialog(any())).thenCallRealMethod();
            // target function: ShortestPathPanel.toCsvButton$actionPerformed(ActionEvent)
            saveButton.doClick();
        }
        assertTrue(Arrays.stream(Window.getWindows())
                .anyMatch(w -> w instanceof JDialog));
        Arrays.stream(Window.getWindows()).forEach(Window::dispose);
    }
}