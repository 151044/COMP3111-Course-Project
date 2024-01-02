package hk.ust.comp3111.algo.maze;

import hk.ust.comp3111.algo.shortestpath.BreadthFirstSearch;
import hk.ust.comp3111.api.Coord;
import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.api.Vertex;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static hk.ust.comp3111.api.Vertex.TileType.*;

/**
 * A set of utility classes for miscellaneous maze generation tasks.
 */
public class MazeHelper {
    private MazeHelper(){
        throw new AssertionError("MazeHelper cannot be instantiated!");
    }

    private static final Random random = new Random();
    private static final BreadthFirstSearch search = new BreadthFirstSearch();

    /**
     * Generates a random vertex on the given column which can be accessed (has at least one passable square).
     * @param data The map data to use
     * @param col The column at which to get a random vertex
     * @throws NoSuchElementException If the map column is out of range
     * @return A random vertex which can be accessed
     */
    public static Vertex generateAtCol(MapData data, int col) {
        Vertex ret;
        do {
            ret = data.at(new Coord(col, random.nextInt(MapData.DEFAULT_HEIGHT - 1)))
                    .orElseThrow();
        } while (data.around(ret, 1).stream().noneMatch(data::isPassable));
        return ret;
    }

    /**
     * Adds a path to the given maze which has an existing path.
     * @param data The maze to modify
     */
    public static void addPath(MapData data) {
        if (data.findEntrance().isEmpty() || data.findExit().isEmpty()) {
            throw new IllegalArgumentException("Map data has no entrance or exit.");
        }
        Vertex entrance = data.findEntrance().orElseThrow();
        Vertex exit = data.findExit().orElseThrow();
        // BFS: find reachable tiles along path
        List<Vertex> reachable = search.bfsGeneral(data, entrance, exit, false);

        // partitions the set of reachable vertices (KNN with k = 2)
        Map<Vertex, Vertex.TileType> partitioned = reachable.stream().collect(Collectors.toMap(
                Function.identity(), v -> search.bfsGeneral(data, entrance, v, true).size() >
                        search.bfsGeneral(data, v, exit, true).size() ? EXIT : ENTRANCE));

        // List of tiles which can be replaced:
        // those who are between the boundary of closer to entrance and closer to exit
        Map<Vertex, List<Vertex>> replacable = partitioned.entrySet().stream().map(entry -> Map.entry(entry.getKey(),
                data.around(entry.getKey(), 2).stream()
                .filter(v -> partitioned.get(v) != entry.getValue()).toList()))
                .filter(v -> !v.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // safety check: if we can't actually find any replacable vertices, bail out
        if (replacable.size() != 0) {
            Map.Entry<Vertex, List<Vertex>> chosen = new ArrayList<>(replacable.entrySet()).get(random.nextInt(replacable.size()));
            Vertex neighbour = chosen.getValue().get(random.nextInt(chosen.getValue().size()));
            Vertex vert = chosen.getKey();
            int dx = neighbour.x() - vert.x();
            int dy = neighbour.y() - vert.y();
            Vertex between = data.at(vert.getCoord()
                            .translate(new Coord((int) Math.signum(dx), (int) Math.signum(dy))))
                    .orElseThrow();
            between.setTileType(CLEAR);
        }
    }
}
