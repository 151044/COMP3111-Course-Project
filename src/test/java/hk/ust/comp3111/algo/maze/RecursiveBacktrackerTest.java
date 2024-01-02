package hk.ust.comp3111.algo.maze;

import hk.ust.comp3111.algo.shortestpath.BreadthFirstSearch;
import hk.ust.comp3111.api.Coord;
import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.api.Vertex;
import hk.ust.comp3111.reflect.Reflect;
import hk.ust.comp3111.api.MapDataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class RecursiveBacktrackerTest {

    private RecursiveBacktracker backtracker;
    private BreadthFirstSearch search;
    private Random random;

    @BeforeEach
    void hacks() throws NoSuchFieldException, IllegalAccessException {
        random = new Random(69420);
        backtracker = new RecursiveBacktracker();
        Reflect.setField("random", backtracker, random);
        search = new BreadthFirstSearch();
    }

    @Test
    void generate() {
        MapData data = MapDataHelper.getMap();
        AtomicBoolean bool = new AtomicBoolean();
        backtracker.onUpdate(d -> bool.set(true));
        // target function: RecursiveBacktracker.generate(MapData)
        backtracker.generate(data);
        assertFalse(data.getVertices().stream().flatMap(List::stream)
                .allMatch(v -> v.getTileType() == Vertex.TileType.BARRIER));
        assertTrue(data.findEntrance().isPresent());
        assertTrue(data.findEntrance().isPresent());
        assertTrue(search.bfsGeneral(data,
                data.findEntrance().orElseThrow(),
                data.findExit().orElseThrow(), true).size() > 0);
    }

    @Test
    void onUpdate() {
        MapData data = MapDataHelper.getMap();
        AtomicBoolean bool = new AtomicBoolean();
        // target function: RecursiveBacktracker.onUpdate(Consumer<MapData>)
        backtracker.onUpdate(d -> bool.set(true));
        backtracker.generate(data);
        assertTrue(bool.get());
    }

    @Test
    void carve() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method m = RecursiveBacktracker.class.getDeclaredMethod("carve", MapData.class, Vertex.class, List.class);
        m.setAccessible(true);
        MapData data = MapDataHelper.getMap();
        List<Vertex> vert = new ArrayList<>();
        vert.add(data.at(new Coord(random.nextInt(MapData.DEFAULT_WIDTH), random.nextInt(MapData.DEFAULT_HEIGHT))).orElseThrow());
        // target function: RecursiveBacktracker.carve(MapData, Vertex, List) (Indirect call)
        m.invoke(backtracker, data, data.findEntrance().orElseThrow(), new ArrayList<>());
        assertFalse(data.getVertices().stream().flatMap(List::stream)
                .allMatch(v -> v.getTileType() == Vertex.TileType.BARRIER));
    }
}