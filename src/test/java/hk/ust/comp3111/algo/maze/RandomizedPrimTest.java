package hk.ust.comp3111.algo.maze;

import hk.ust.comp3111.algo.shortestpath.BreadthFirstSearch;
import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.api.Vertex;
import hk.ust.comp3111.reflect.Reflect;
import hk.ust.comp3111.api.MapDataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class RandomizedPrimTest {
    private RandomizedPrim prim;
    private BreadthFirstSearch search;

    @BeforeEach
    void hacks() throws NoSuchFieldException, IllegalAccessException {
        Random random = new Random(69420);
        prim = new RandomizedPrim();
        Reflect.setField("random", prim, random);
        search = new BreadthFirstSearch();
    }

    @Test
    void generate() {
        MapData data = MapDataHelper.getMap();
        AtomicBoolean bool = new AtomicBoolean();
        prim.onUpdate(d -> bool.set(true));
        // target function: RandomizedPrim.generate(MapData)
        prim.generate(data);
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
        // target function: RandomizedPrim.onUpdate(Consumer<MapData>)
        prim.onUpdate(d -> bool.set(true));
        prim.generate(data);
        assertTrue(bool.get());
    }
}