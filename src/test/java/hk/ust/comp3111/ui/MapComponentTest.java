package hk.ust.comp3111.ui;

import hk.ust.comp3111.api.Coord;
import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.api.MapDataHelper;
import hk.ust.comp3111.api.Vertex;
import hk.ust.comp3111.engine.Engine;
import hk.ust.comp3111.engine.ImagePath;
import hk.ust.comp3111.reflect.Reflect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class MapComponentTest {
    private MapComponent m1, m2;
    private MapData data;

    @BeforeEach
    void setUp() {
        m1 = new MapComponent();
        data = MapDataHelper.getMap();
        // The constructor mutates MapData, so this is necessary
        Engine e = new Engine(data, ImagePath.IMAGES);
        m2 = new MapComponent(data);
    }

    @Test
    void getMainPanel() {
        // target function: MapComponent.getMainPanel()
        assertInstanceOf(GridLayout.class, m1.getMainPanel().getLayout());
        // target function: MapComponent.getMainPanel()
        assertInstanceOf(GridLayout.class, m2.getMainPanel().getLayout());
    }

    @Test
    void at() {
        // target function: MapComponent.at(Coord)
        assertEquals(Vertex.TileType.BARRIER.getColor(), m1.at(new Coord(3, 7)).getBackground());
        // target function: MapComponent.at(Coord)
        assertEquals(Vertex.TileType.EXIT.getColor(), m2.at(new Coord(3, 0)).getBackground());
        // target function: MapComponent.at(Coord)
        assertEquals(Vertex.TileType.CLEAR.getColor(), m2.at(new Coord(2, 1)).getBackground());
    }

    @Test
    void hasPanel() {
        // target function: MapComponent.hasPanel(JPanel)
        assertFalse(m1.hasPanel(new JPanel()));
        // target function: MapComponent.hasPanel(JPanel)
        assertTrue(m1.hasPanel(m1.at(new Coord(0, 0))));
        // target function: MapComponent.hasPanel(JPanel)
        assertTrue(m2.hasPanel(m2.at(new Coord(3, 0))));
    }

    @Test
    void getVertex() {
        // target function: MapComponent.getVertex(JPanel)
        assertNull(m1.getVertex(new JPanel()));
        // target function: MapComponent.getVertex(JPanel)
        assertEquals(new Coord(3, 0), m1.getVertex(m1.at(new Coord(3, 0))).getCoord());
        // target function: MapComponent.getVertex(JPanel)
        assertEquals(new Coord(3, 7), m2.getVertex(m2.at(new Coord(3, 7))).getCoord());
    }

    @Test
    void getData() {
        // target function: MapComponent.getData()
        assertNotEquals(data, m1.getData());
        // target function: MapComponent.getData()
        assertEquals(data, m2.getData());
    }

    @Test
    void getPanels() {
        // target function: MapComponent.getPanels()
        assertTrue(m1.getPanels().stream().flatMap(List::stream).anyMatch(j -> j == m1.at(new Coord(3, 7))));
        // target function: MapComponent.getPanels()
        assertTrue(m2.getPanels().stream().flatMap(List::stream).anyMatch(j -> j == m2.at(new Coord(7, 3))));
        // target function: MapComponent.getPanels()
        assertFalse(m1.getPanels().stream().flatMap(List::stream).anyMatch(j -> j == m2.at(new Coord(3, 7))));
    }

    @Test
    void highlightPath() {
        // target function: MapComponent.highlightPath(List<Vertex>)
        m2.highlightPath(IntStream.range(0, 5).mapToObj(i -> data.at(new Coord(i, i)))
                .flatMap(Optional::stream).collect(Collectors.toList()));
        assertEquals(MapComponent.SHORTEST_PATH_COLOR, m2.at(new Coord(2, 2)).getBackground());
        assertNotEquals(MapComponent.SHORTEST_PATH_COLOR, m1.at(new Coord(2, 2)).getBackground());
    }

    @Test
    void render() {
        // target function: MapComponent.render()
        m1.render();
        // target function: MapComponent.render()
        m2.render();
        // exactly the same thing; render() shouldn't change anything
        assertEquals(Vertex.TileType.BARRIER.getColor(), m1.at(new Coord(3, 7)).getBackground());
        assertTrue(Arrays.stream(m2.at(data.findEntrance().orElseThrow().getCoord()).getComponents())
                .anyMatch(c -> c instanceof JLabel));
        m1.getData().at(new Coord(3, 7)).orElseThrow().setTileType(Vertex.TileType.CLEAR);
        // target function: MapComponent.render()
        m1.render();
        assertEquals(Vertex.TileType.CLEAR.getColor(), m1.at(new Coord(3, 7)).getBackground());
        assertEquals(Vertex.TileType.EXIT.getColor(), m2.at(new Coord(3, 0)).getBackground());
        assertEquals(Vertex.TileType.CLEAR.getColor(), m2.at(new Coord(2, 1)).getBackground());
        m2.highlightPath(IntStream.range(0, 5).mapToObj(i -> data.at(new Coord(i, i)))
                .flatMap(Optional::stream).collect(Collectors.toList()));
        // target function: MapComponent.render()
        m2.render();
        assertEquals(MapComponent.SHORTEST_PATH_COLOR, m2.at(new Coord(4, 4)).getBackground());
    }

    /*
    Note to grading TAs: This is NOT a unit test.
    This is a test to measure ongoing performance impacts.
     */
    @Test
    @Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
    void renderPerformance() {
        for (int i = 0; i < 10000; i++) {
            m2.render();
        }
    }

    @Test
    void blankNewMapComponent() {
        // target function: new MapComponent()
        MapComponent comp = new MapComponent();
        assertTrue(comp.getPanels().stream().flatMap(List::stream)
                .allMatch(j -> j.getBackground().equals(Vertex.TileType.BARRIER.getColor())));
    }

    @Test
    void existingNewMapComponent() {
        // target function: new MapComponent(MapData)
        MapComponent comp = new MapComponent(MapDataHelper.getMap());
        assertFalse(comp.getPanels().stream().flatMap(List::stream)
                .allMatch(j -> j.getBackground().equals(Vertex.TileType.BARRIER.getColor())));
        assertEquals(MapDataHelper.getMap(), comp.getData());
    }

    @Test
    @SuppressWarnings("unchecked")
    void initTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Method m = MapComponent.class.getDeclaredMethod("init", Function.class);
        m.setAccessible(true);
        MapComponent comp = new MapComponent();
        Reflect.getField("panels", List.class, comp).clear();
        Reflect.getField("mapPanel", JPanel.class, comp).removeAll();
        Reflect.getField("lookupVertex", Map.class, comp).clear();
        // target function: MapComponent.init()
        Reflect.setField("mapData", comp,
                new MapData((List<List<Vertex>>) m.invoke(comp,
                        ((Function<Coord, Vertex>) (c) -> new Vertex(c, Vertex.TileType.CLEAR)))));
        comp.render();
        assertTrue(comp.getData().getVertices().stream().flatMap(List::stream)
                .allMatch(v -> v.getTileType() == Vertex.TileType.CLEAR));
    }
}