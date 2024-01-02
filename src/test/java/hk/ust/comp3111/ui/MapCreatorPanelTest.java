package hk.ust.comp3111.ui;

import hk.ust.comp3111.api.Coord;
import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.api.MapDataHelper;
import hk.ust.comp3111.api.Vertex;
import hk.ust.comp3111.reflect.Reflect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.List;
import java.util.Optional;

import static java.awt.event.MouseEvent.MOUSE_RELEASED;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.mockito.Mockito.*;

class MapCreatorPanelTest {
    private MapCreatorPanel panel;
    private JFileChooser mockedChooser;
    @BeforeEach
    void init() {
        panel = new MapCreatorPanel();
        mockedChooser = Mockito.mock(JFileChooser.class);
    }
    void setMode(MapCreatorPanel.Mode m) throws NoSuchFieldException, IllegalAccessException {
        Reflect.setField("mode", panel, m);
    }

    MapCreatorPanel.Mode getMode() throws NoSuchFieldException, IllegalAccessException {
        return Reflect.getField("mode", MapCreatorPanel.Mode.class, panel);
    }

    ActionEvent newActionEvent(String command) {
        return new ActionEvent(new JButton(), -1, command);
    }

    MouseEvent newMouseEvent(Component comp, int id) {
        return new MouseEvent(comp, id, 1, 0, 0, 0, 1, false);
    }

    MapComponent getComponent() throws NoSuchFieldException, IllegalAccessException {
        return Reflect.getField("component", MapComponent.class, panel);
    }

    @Test
    void editHandlerTest() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless());
        ActionListener listener = Reflect.getInnerInstance("EditButtonHandler", ActionListener.class, panel);
        Map<String, MapCreatorPanel.Mode> map = Map.of(
                "Clear", MapCreatorPanel.Mode.CLEAR,
                "Toggle", MapCreatorPanel.Mode.CLICK,
                "Fill", MapCreatorPanel.Mode.FILL,
                "Line", MapCreatorPanel.Mode.LINE,
                "Entrance", MapCreatorPanel.Mode.ENTRANCE,
                "Exit", MapCreatorPanel.Mode.EXIT
        );
        map.forEach((s, m) -> {
            try {
                setMode(m);
                // target function: EditButtonHandler.actionPerformed(ActionEvent)
                listener.actionPerformed(newActionEvent(s));
                assertEquals(m, getMode());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void entranceExitEdits() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        assumeFalse(GraphicsEnvironment.isHeadless());
        ActionListener listener = Reflect.getInnerInstance("EditButtonHandler", ActionListener.class, panel);
        Map<String, MapCreatorPanel.Mode> map = Map.of(
                "Entrance", MapCreatorPanel.Mode.ENTRANCE,
                "Exit", MapCreatorPanel.Mode.EXIT
        );
        map.forEach((s, m) -> {
            try {
                setMode(m);
                // target function: EditButtonHandler.actionPerformed(ActionEvent)
                listener.actionPerformed(newActionEvent(s));
                assertEquals(m, getMode());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        // target function: EditButtonHandler.actionPerformed(ActionEvent)
        listener.actionPerformed(newActionEvent("Fill"));
        getComponent().render();
        assertTrue(getComponent().getPanels().stream().flatMap(List::stream)
                .allMatch(j -> j.getBackground().equals(Vertex.TileType.BARRIER.getColor())));
    }

    @Test
    void generateHandlerTest() throws Exception {
        ActionListener listener = Reflect.getInnerInstance("GenerateButtonHandler", ActionListener.class, panel);
        MapComponent comp = getComponent();
        // target function: GenerateButtonHandler.actionPerformed(ActionEvent)
        listener.actionPerformed(newActionEvent("Prim's Algorithm"));
        Thread.sleep(500);
        comp.render();
        assertFalse(comp.getPanels().stream().flatMap(List::stream)
                .allMatch(j -> j.getBackground().equals(Vertex.TileType.BARRIER.getColor())));
        comp.getData().forAll(c -> c.setTileType(Vertex.TileType.BARRIER));
        // target function: GenerateButtonHandler.actionPerformed(ActionEvent)
        listener.actionPerformed(newActionEvent("Recursive Division"));
        Thread.sleep(300);
        comp.render();
        assertFalse(comp.getPanels().stream().flatMap(List::stream)
                .allMatch(j -> j.getBackground().equals(Vertex.TileType.BARRIER.getColor())));
        comp.getData().forAll(c -> c.setTileType(Vertex.TileType.BARRIER));
        // target function: GenerateButtonHandler.actionPerformed(ActionEvent)
        listener.actionPerformed(newActionEvent("Recursive Backtracking"));
        Thread.sleep(300);
        comp.render();
        assertFalse(comp.getPanels().stream().flatMap(List::stream)
                .allMatch(j -> j.getBackground().equals(Vertex.TileType.BARRIER.getColor())));
    }

    @Test
    void mhToggle() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        MouseListener clickListener = Reflect.getInnerInstance("MouseHandler", MouseListener.class, panel);
        MapComponent comp = getComponent();
        setMode(MapCreatorPanel.Mode.CLICK);
        // target function: MouseHandler.mousePressed(MouseEvent)
        clickListener.mousePressed(newMouseEvent(comp.at(new Coord(0, 0)), MouseEvent.MOUSE_PRESSED));
        assertEquals(Vertex.TileType.CLEAR, comp.getVertex(comp.at(new Coord(0, 0))).getTileType());
        setMode(MapCreatorPanel.Mode.ENTRANCE);
        // target function: MouseHandler.mousePressed(MouseEvent)
        clickListener.mousePressed(newMouseEvent(comp.at(new Coord(0, 0)), MouseEvent.MOUSE_PRESSED));
        // target function: MouseHandler.mousePressed(MouseEvent)
        clickListener.mousePressed(newMouseEvent(comp.at(new Coord(17, 17)), MouseEvent.MOUSE_PRESSED));
        // target function: MouseHandler.mousePressed(MouseEvent)
        clickListener.mousePressed(newMouseEvent(comp.at(new Coord(0, 28)), MouseEvent.MOUSE_PRESSED));
        assertEquals(Vertex.TileType.BARRIER, comp.getVertex(comp.at(new Coord(0, 0))).getTileType());
        assertEquals(Vertex.TileType.BARRIER, comp.getVertex(comp.at(new Coord(17, 17))).getTileType());
        assertEquals(Vertex.TileType.ENTRANCE, comp.getVertex(comp.at(new Coord(0, 28))).getTileType());
    }

    @Test
    void mhClick() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        MouseListener clickListener = Reflect.getInnerInstance("MouseHandler", MouseListener.class, panel);
        MapComponent comp = getComponent();
        setMode(MapCreatorPanel.Mode.EXIT);
        // target function: MouseHandler.mousePressed(MouseEvent)
        clickListener.mousePressed(newMouseEvent(comp.at(new Coord(0, 0)), MouseEvent.MOUSE_PRESSED));
        // target function: MouseHandler.mousePressed(MouseEvent)
        clickListener.mousePressed(newMouseEvent(comp.at(new Coord(17, 17)), MouseEvent.MOUSE_PRESSED));
        // target function: MouseHandler.mousePressed(MouseEvent)
        clickListener.mousePressed(newMouseEvent(comp.at(new Coord(0, 28)), MouseEvent.MOUSE_PRESSED));
        assertEquals(Vertex.TileType.BARRIER, comp.getVertex(comp.at(new Coord(0, 0))).getTileType());
        assertEquals(Vertex.TileType.BARRIER, comp.getVertex(comp.at(new Coord(17, 17))).getTileType());
        assertEquals(Vertex.TileType.EXIT, comp.getVertex(comp.at(new Coord(0, 28))).getTileType());
    }

    @Test
    void mhLinePressed() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        MouseListener clickListener = Reflect.getInnerInstance("MouseHandler", MouseListener.class, panel);
        MapComponent comp = getComponent();
        setMode(MapCreatorPanel.Mode.LINE);
        // target function: MouseHandler.mousePressed(MouseEvent)
        clickListener.mousePressed(newMouseEvent(comp.at(new Coord(0, 0)), MouseEvent.MOUSE_PRESSED));
        assertEquals(Vertex.TileType.CLEAR, comp.getVertex(comp.at(new Coord(0, 0))).getTileType());
    }
    @Test
    void mhLineEntered() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        MouseListener clickListener = Reflect.getInnerInstance("MouseHandler", MouseListener.class, panel);
        MapComponent comp = getComponent();
        setMode(MapCreatorPanel.Mode.LINE);
        clickListener.mousePressed(newMouseEvent(comp.at(new Coord(0, 0)), MouseEvent.MOUSE_PRESSED));
        // target function: MouseHandler.mouseEntered(MouseMotionEvent)
        clickListener.mouseEntered(newMouseEvent(comp.at(new Coord(4, 0)), MouseEvent.MOUSE_ENTERED));
        assertEquals(Vertex.TileType.CLEAR, comp.getVertex(comp.at(new Coord(1, 0))).getTileType());
        // target function: MouseHandler.mouseEntered(MouseMotionEvent)
        clickListener.mouseEntered(newMouseEvent(comp.at(new Coord(0, 28)), MouseEvent.MOUSE_ENTERED));
        assertEquals(Vertex.TileType.BARRIER, comp.getVertex(comp.at(new Coord(1, 0))).getTileType());
    }
    @Test
    void mhLineReleased() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        MouseListener clickListener = Reflect.getInnerInstance("MouseHandler", MouseListener.class, panel);
        MapComponent comp = getComponent();
        setMode(MapCreatorPanel.Mode.LINE);
        clickListener.mousePressed(newMouseEvent(comp.at(new Coord(0, 0)), MouseEvent.MOUSE_PRESSED));
        clickListener.mouseEntered(newMouseEvent(comp.at(new Coord(4, 0)), MouseEvent.MOUSE_ENTERED));
        clickListener.mouseEntered(newMouseEvent(comp.at(new Coord(0, 28)), MouseEvent.MOUSE_ENTERED));
        // target function: MouseHandler.mouseReleased(MouseEvent)
        clickListener.mouseReleased(newMouseEvent(comp.at(new Coord(0, 0)), MOUSE_RELEASED));
        assertNull(Reflect.getField("start", JPanel.class, panel));
    }
    @Test
    void getConstraint() {
        // target function: MapCreatorPanel.getConstraint()
        assertEquals(GridBagConstraints.BOTH, panel.getConstraint().fill);
        // target function: MapCreatorPanel.getConstraint()
        assertNotEquals(UIHelper.defaultGridConstraints(), panel.getConstraint());
    }

    @Test
    void replaceNoSuchFileFailure() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        try (MockedStatic<UIHelper> uiHelper = Mockito.mockStatic(UIHelper.class)) {
            uiHelper.when(UIHelper::showFileOpener).thenReturn(Optional.empty());
            uiHelper.when(()-> UIHelper.showErrorDialog(any())).thenCallRealMethod();
            // target function: MapCreatorPanel.replace()
            panel.replace();
            assertTrue(Arrays.stream(Window.getWindows())
                    .anyMatch(w -> w instanceof JDialog));
            Arrays.stream(Window.getWindows()).forEach(Window::dispose);
        }
    }

    @Test
    void replaceFileFailure() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        try (MockedStatic<UIHelper> uiHelper = Mockito.mockStatic(UIHelper.class)) {
            uiHelper.when(UIHelper::showFileOpener).thenReturn(
                    Optional.of(new File("test-inputs" + File.separator + "not-a-csv.csv"))
            );
            uiHelper.when(()-> UIHelper.showErrorDialog(any())).thenCallRealMethod();
            // target function: MapCreatorPanel.replace()
            panel.replace();
            assertTrue(Arrays.stream(Window.getWindows())
                    .anyMatch(w -> w instanceof JDialog));
            Arrays.stream(Window.getWindows()).forEach(Window::dispose);
        }
    }

    @Test
    void replaceSuccess() throws NoSuchFieldException, IllegalAccessException {
        assumeFalse(GraphicsEnvironment.isHeadless());
        try (MockedStatic<UIHelper> uiHelper = Mockito.mockStatic(UIHelper.class)) {
            uiHelper.when(UIHelper::showFileOpener)
                            .thenReturn(Optional.of(Path.of("test-inputs" + File.separator + "maze-one.csv").toFile()));
            // target function: MapCreatorPanel.replace()
            panel.replace();
        }
        MapComponent comp = getComponent();
        assertFalse(comp.getPanels().stream().flatMap(List::stream)
                .allMatch(j -> j.getBackground().equals(Vertex.TileType.BARRIER.getColor())));
    }

    @Test
    void saveNoExitFailure() throws InterruptedException {
        assumeFalse(GraphicsEnvironment.isHeadless());
        Vertex v = MapDataHelper.getMap().findExit().orElseThrow();
        v.setTileType(Vertex.TileType.BARRIER);
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            assertTrue(Arrays.stream(Window.getWindows())
                    .anyMatch(w -> w instanceof JDialog));
            Arrays.stream(Window.getWindows()).forEach(Window::dispose);
        });
        t.start();
        // target function: MapCreatorPanel.save()
        panel.save();
        t.join();
    }
    @Test
    void saveIOExceptionFailure() throws NoSuchFieldException, IllegalAccessException, IOException {
        assumeFalse(GraphicsEnvironment.isHeadless());
        MapComponent comp = getComponent();
        comp.getData().at(new Coord(0, 0)).orElseThrow().setTileType(Vertex.TileType.ENTRANCE);
        comp.getData().at(new Coord(29, 29)).orElseThrow().setTileType(Vertex.TileType.EXIT);
        File f = new File("test-inputs" + File.separator + "failure.csv");
        when(mockedChooser.showSaveDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(mockedChooser.getSelectedFile()).thenReturn(f);

        MapData data = new MapData(Path.of("test-inputs" + File.separator + "maze-one.csv"));
        data = Mockito.spy(data);
        Reflect.setField("mapData", panel, data);
        doThrow(new IOException()).when(data).write(f.toPath());

        try (MockedStatic<JOptionPane> optionPane = Mockito.mockStatic(JOptionPane.class);
                MockedStatic<UIHelper> uiHelper = Mockito.mockStatic(UIHelper.class)) {
            optionPane.when(() -> JOptionPane.showConfirmDialog(null, "Overwrite existing file?",
                            "Overwrite file?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE))
                    .thenReturn(JOptionPane.YES_OPTION);
            uiHelper.when(UIHelper::getCsvOpener).thenReturn(mockedChooser);

            uiHelper.when(() -> UIHelper.saveFile(any())).thenCallRealMethod();
            uiHelper.when(() -> UIHelper.showErrorDialog(any())).thenCallRealMethod();
            // target function: MapCreatorPanel.save()
            panel.save();
        }
        assertTrue(Arrays.stream(Window.getWindows())
                .anyMatch(w -> w instanceof JDialog));
        Arrays.stream(Window.getWindows()).forEach(Window::dispose);
    }

    @Test
    void saveNoPathFailure() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        assumeFalse(GraphicsEnvironment.isHeadless());
        MapComponent comp = getComponent();
        comp.getData().at(new Coord(0, 0)).orElseThrow().setTileType(Vertex.TileType.ENTRANCE);
        comp.getData().at(new Coord(29, 29)).orElseThrow().setTileType(Vertex.TileType.EXIT);
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            assertTrue(Arrays.stream(Window.getWindows())
                    .anyMatch(w -> w instanceof JDialog));
            Arrays.stream(Window.getWindows()).forEach(Window::dispose);
        });
        t.start();
        // target function: MapCreatorPanel.save()
        panel.save();
        t.join();
    }

    @Test
    void saveSuccess() throws NoSuchFieldException, IllegalAccessException {
        assumeFalse(GraphicsEnvironment.isHeadless());
        MapComponent comp = getComponent();
        comp.getData().forAll(v -> v.setTileType(Vertex.TileType.CLEAR));
        comp.getData().at(new Coord(0, 0)).orElseThrow().setTileType(Vertex.TileType.ENTRANCE);
        comp.getData().at(new Coord(29, 29)).orElseThrow().setTileType(Vertex.TileType.EXIT);

        when(mockedChooser.showSaveDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(mockedChooser.getSelectedFile()).thenReturn(new File("test-inputs" + File.separator + "maze-three.csv"));

        try (MockedStatic<JOptionPane> optionPane = Mockito.mockStatic(JOptionPane.class);
                MockedStatic<UIHelper> uiHelper = Mockito.mockStatic(UIHelper.class)) {
            optionPane.when(() -> JOptionPane.showConfirmDialog(null, "Overwrite existing file?",
                    "Overwrite file?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE))
                            .thenReturn(JOptionPane.YES_OPTION);
            uiHelper.when(UIHelper::getCsvOpener).thenReturn(mockedChooser);

            uiHelper.when(() -> UIHelper.saveFile(any())).thenCallRealMethod();
            uiHelper.when(() -> UIHelper.showErrorDialog(any())).thenCallRealMethod();
            // target function: MapCreatorPanel.save()
            panel.save();
        }
    }

    @Test
    void newMapCreatorPanel() {
        // target function: new MapCreatorPanel()
        MapCreatorPanel panel = new MapCreatorPanel();
        assertInstanceOf(BorderLayout.class, panel.getLayout());
    }
}