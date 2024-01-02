package hk.ust.comp3111.algo.shortestpath;

import hk.ust.comp3111.api.Coord;
import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.api.Vertex;
import hk.ust.comp3111.api.MapDataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BreadthFirstSearchTest {
    // The second map
    private static final String input2 = """
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
2, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1""";


    private final BreadthFirstSearch bfs = new BreadthFirstSearch();
    private final MapData testMap = MapDataHelper.getMap();
    private final MapData testMap2 = MapDataHelper.getMap(input2);
    private Vertex v1, v2, v3, v4, v5;

    Object newNode(Vertex vertex, MapData data) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Arrays.stream(BreadthFirstSearch.class.getDeclaredClasses())
                .filter(c -> c.getName().equals("hk.ust.comp3111.algo.shortestpath.BreadthFirstSearch$GraphNode"))
                .findFirst()
                .orElseThrow();
        Constructor<?> cons = clazz.getDeclaredConstructor(Vertex.class, MapData.class);
        cons.setAccessible(true);
        return cons.newInstance(vertex, data);
    }

    Object getField(String fieldName, Object o) throws NoSuchFieldException, IllegalAccessException {
        Field f = o.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        return f.get(o);
    }

    @BeforeEach
    void setup() {
        v1 = testMap.at(new Coord(18, 29)).orElseThrow(); // entrance of TestMap
        v2 = testMap.at(new Coord(3, 0)).orElseThrow(); // exit of TestMap
        v3 = testMap.at(new Coord(3, 28)).orElseThrow(); // random barrier vertex of TestMap

        v4 = testMap2.at(new Coord(0, 1)).orElseThrow(); // entrance of TestMap2
        v5 = testMap2.at(new Coord(21, 28)).orElseThrow(); // exit of TestMap2
    }

    @Test
    void testGraphNode() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        // target function: new BreadthFirstSearch$GraphNode(Vertex, MapData)
        Object testNode = newNode(v2, testMap);
        assertEquals("(3, 0), EXIT", getField("current", testNode).toString());
        assertNull(getField("parent", testNode));
        assertEquals(false, getField("visited", testNode));
        assertEquals("[(3, 1), CLEAR]", getField("adjacents", testNode).toString());
    }

    @Test
    void testBFS() {
        // target function: BreadthFirstSearch.bfsGeneral(MapData, Vertex, Vertex, boolean)
        assertEquals("[]", bfs.bfsGeneral(testMap, v3, v2, true).toString());

        // target function: BreadthFirstSearch.bfsGeneral(MapData, Vertex, Vertex, boolean)
        assertEquals("[(18, 29), ENTRANCE, (18, 28), CLEAR, (18, 27), CLEAR, (17, 27), CLEAR, (17, 26), CLEAR, (16, 26), CLEAR, (16, 25), CLEAR, (16, 24), CLEAR, (15, 24), CLEAR, (15, 23), CLEAR, (14, 23), CLEAR, (14, 22), CLEAR, (14, 21), CLEAR, (13, 21), CLEAR, (13, 20), CLEAR, (13, 19), CLEAR, (12, 19), CLEAR, (12, 18), CLEAR, (12, 17), CLEAR, (11, 17), CLEAR, (11, 16), CLEAR, (11, 15), CLEAR, (10, 15), CLEAR, (10, 14), CLEAR, (9, 14), CLEAR, (9, 13), CLEAR, (8, 13), CLEAR, (8, 12), CLEAR, (8, 11), CLEAR, (8, 10), CLEAR, (7, 10), CLEAR, (7, 9), CLEAR, (7, 8), CLEAR, (6, 8), CLEAR, (6, 7), CLEAR, (5, 7), CLEAR, (5, 6), CLEAR, (5, 5), CLEAR, (4, 5), CLEAR, (4, 4), CLEAR, (4, 3), CLEAR, (3, 3), CLEAR, (3, 2), CLEAR, (3, 1), CLEAR, (3, 0), EXIT]",
                bfs.bfsGeneral(testMap, v1, v2, true).toString());

        // target function: BreadthFirstSearch.bfsGeneral(MapData, Vertex, Vertex, boolean)
        assertEquals("[(18, 28), CLEAR, (18, 27), CLEAR, (18, 29), ENTRANCE, (17, 27), CLEAR, (19, 27), CLEAR, (18, 26), CLEAR, (17, 26), CLEAR, (20, 27), CLEAR, (19, 26), CLEAR, (16, 26), CLEAR, (21, 27), CLEAR, (15, 26), CLEAR, (16, 25), CLEAR, (22, 27), CLEAR, (14, 26), CLEAR, (16, 24), CLEAR, (23, 27), CLEAR, (13, 26), CLEAR, (15, 24), CLEAR, (24, 27), CLEAR, (12, 26), CLEAR, (15, 23), CLEAR, (25, 27), CLEAR, (11, 26), CLEAR, (14, 23), CLEAR, (26, 27), CLEAR, (10, 26), CLEAR, (14, 22), CLEAR, (26, 26), CLEAR, (9, 26), CLEAR, (14, 21), CLEAR, (26, 25), CLEAR, (8, 26), CLEAR, (13, 21), CLEAR, (26, 24), CLEAR, (7, 26), CLEAR, (13, 20), CLEAR, (26, 23), CLEAR, (7, 25), CLEAR, (13, 19), CLEAR, (26, 22), CLEAR, (6, 25), CLEAR, (12, 19), CLEAR, (26, 21), CLEAR, (5, 25), CLEAR, (12, 18), CLEAR, (26, 20), CLEAR, (4, 25), CLEAR, (12, 17), CLEAR, (26, 19), CLEAR, (3, 25), CLEAR, (11, 17), CLEAR, (26, 18), CLEAR, (2, 25), CLEAR, (11, 16), CLEAR, (26, 17), CLEAR, (2, 24), CLEAR, (11, 15), CLEAR, (26, 16), CLEAR, (2, 23), CLEAR, (10, 15), CLEAR, (26, 15), CLEAR, (2, 22), CLEAR, (10, 14), CLEAR, (26, 14), CLEAR, (2, 21), CLEAR, (9, 14), CLEAR, (10, 13), CLEAR, (26, 13), CLEAR, (2, 20), CLEAR, (9, 13), CLEAR, (11, 13), CLEAR, (25, 13), CLEAR, (26, 12), CLEAR, (2, 19), CLEAR, (8, 13), CLEAR, (9, 12), CLEAR, (12, 13), CLEAR, (24, 13), CLEAR, (26, 11), CLEAR, (2, 18), CLEAR, (7, 13), CLEAR, (8, 12), CLEAR, (13, 13), CLEAR, (23, 13), CLEAR, (26, 10), CLEAR, (2, 17), CLEAR, (6, 13), CLEAR, (8, 11), CLEAR, (14, 13), CLEAR, (22, 13), CLEAR, (26, 9), CLEAR, (2, 16), CLEAR, (5, 13), CLEAR, (8, 10), CLEAR, (15, 13), CLEAR, (21, 13), CLEAR, (26, 8), CLEAR, (2, 15), CLEAR, (4, 13), CLEAR, (7, 10), CLEAR, (16, 13), CLEAR, (20, 13), CLEAR, (26, 7), CLEAR, (2, 14), CLEAR, (3, 13), CLEAR, (7, 9), CLEAR, (17, 13), CLEAR, (19, 13), CLEAR, (26, 6), CLEAR, (2, 13), CLEAR, (7, 8), CLEAR, (18, 13), CLEAR, (26, 5), CLEAR, (2, 12), CLEAR, (6, 8), CLEAR, (26, 4), CLEAR, (2, 11), CLEAR, (6, 7), CLEAR, (26, 3), CLEAR, (2, 10), CLEAR, (5, 7), CLEAR, (6, 6), CLEAR, (26, 2), CLEAR, (2, 9), CLEAR, (5, 6), CLEAR, (26, 1), CLEAR, (2, 8), CLEAR, (5, 5), CLEAR, (25, 1), CLEAR, (2, 7), CLEAR, (4, 5), CLEAR, (24, 1), CLEAR, (2, 6), CLEAR, (4, 4), CLEAR, (23, 1), CLEAR, (2, 5), CLEAR, (4, 3), CLEAR, (22, 1), CLEAR, (2, 4), CLEAR, (3, 3), CLEAR, (21, 1), CLEAR, (2, 3), CLEAR, (3, 2), CLEAR, (20, 1), CLEAR, (2, 2), CLEAR, (3, 1), CLEAR, (19, 1), CLEAR, (2, 1), CLEAR, (4, 1), CLEAR, (3, 0), EXIT, (18, 1), CLEAR, (5, 1), CLEAR]",
                bfs.bfsGeneral(testMap, v1, v2, false).toString());

        // target function: BreadthFirstSearch.bfsGeneral(MapData, Vertex, Vertex, boolean)
        assertEquals("[]", bfs.bfsGeneral(testMap2, v4, v5, true).toString());
    }

    @Test
    void testSPV() {
        // target function: BreadthFirstSearch.nextShortestPathVertex(MapData, Vertex, Vertex)
        assertEquals("(3, 28), BARRIER", bfs.nextShortestPathVertex(testMap, v3, v2).toString());

        // target function: BreadthFirstSearch.nextShortestPathVertex(MapData, Vertex, Vertex)
        assertEquals("(18, 28), CLEAR", bfs.nextShortestPathVertex(testMap, v1, v2).toString());
    }

    @Test
    void testRRV() throws NoSuchFieldException, IllegalAccessException {
        Field f = BreadthFirstSearch.class.getDeclaredField("rand");
        f.setAccessible(true);
        f.set(bfs, new Random(69420));

        // target function: BreadthFirstSearch.randomReachableVertex(MapData, Vertex, Vertex)
        assertEquals("(3, 28), BARRIER", bfs.randomReachableVertex(testMap, v3, v2).toString());

        // target function: BreadthFirstSearch.randomReachableVertex(MapData, Vertex, Vertex)
        assertEquals("(23, 13), CLEAR", bfs.randomReachableVertex(testMap, v1, v2).toString());
    }

}