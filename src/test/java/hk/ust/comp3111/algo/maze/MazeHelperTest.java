package hk.ust.comp3111.algo.maze;

import hk.ust.comp3111.algo.shortestpath.BreadthFirstSearch;
import hk.ust.comp3111.api.MapDataHelper;
import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.api.Vertex;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class MazeHelperTest {
    @Test
    void privateConstructor() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<MazeHelper> crash = MazeHelper.class.getDeclaredConstructor();
        crash.setAccessible(true);
        // target function: new MazeHelper()
        assertThrows(InvocationTargetException.class, crash::newInstance);
    }
    @Test
    void generateAtCol() {
        MapData data = MapDataHelper.getMap();
        // target function: MazeHelper.generateAtCol(MapData, int)
        assertEquals(3, MazeHelper.generateAtCol(data, 3).x());
        // target function: MazeHelper.generateAtCol(MapData, int)
        assertNotEquals(0, MazeHelper.generateAtCol(data, 2).x());
    }

    @Test
    void addPathInvalid() {
        MapData data = MapDataHelper.getMap();
        data.findExit().orElseThrow().setTileType(Vertex.TileType.BARRIER);
        // target function: MazeHelper.addPath(MapData)
        assertThrows(IllegalArgumentException.class, () -> MazeHelper.addPath(data));
    }

    @Test
    void addPathValid() {
        MapData data = MapDataHelper.getMap();
        // target function: MazeHelper.addPath(MapData)
        MazeHelper.addPath(data);
        assertFalse(new BreadthFirstSearch().bfsGeneral(data, data.findEntrance().orElseThrow(),
                data.findExit().orElseThrow(), true).isEmpty());
    }
}