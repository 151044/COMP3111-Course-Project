package hk.ust.comp3111.algo.maze;

import hk.ust.comp3111.api.Coord;
import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.api.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static hk.ust.comp3111.api.Vertex.TileType.*;

/**
 * Implements the recursive backtracker strategy for generating mazes.
 */
public class RecursiveBacktracker implements MazeStrategy {
    private Consumer<MapData> cons;
    private final Random random = new Random();
    @Override
    public void generate(MapData data) {
        data.forAll(v -> v.setTileType(Vertex.TileType.BARRIER));
        List<Vertex> visited = new ArrayList<>();
        Vertex root = data.at(new Coord(random.nextInt(MapData.DEFAULT_WIDTH), random.nextInt(MapData.DEFAULT_HEIGHT)))
                .orElse(data.at(new Coord(0, 0)).orElseThrow());
        visited.add(root);
        carve(data, root, visited);
        Vertex entrance = MazeHelper.generateAtCol(data, 0);
        Vertex exit = MazeHelper.generateAtCol(data, MapData.DEFAULT_WIDTH - 1);
        entrance.setTileType(ENTRANCE);
        exit.setTileType(EXIT);

        MazeHelper.addPath(data);
        if (cons != null) {
            cons.accept(data);
        }
    }

    /**
     * Carves a random path recursively.
     * @param data The map data to use
     * @param target The target vertex to slice from
     * @param visited The list of visited vertices
     */
    private void carve(MapData data, Vertex target, List<Vertex> visited) {
        List<Vertex> around = data.around(target, 2)
                .stream().filter(v -> !visited.contains(v))
                .filter(v -> v.getTileType() != CLEAR)
                .collect(Collectors.toList());
        if (around.size() > 0) {
            Collections.shuffle(around);
            for (Vertex v : around) {
                if (v.getTileType() != CLEAR && !visited.contains(v)) {
                    int dx = v.x() - target.x();
                    int dy = v.y() - target.y();
                    Vertex between = data.at(target.getCoord()
                                    .translate(new Coord((int) Math.signum(dx), (int) Math.signum(dy))))
                            .orElseThrow();
                    between.setTileType(CLEAR);
                    v.setTileType(CLEAR);
                    visited.add(between);
                    visited.add(v);
                    carve(data, v, visited);
                }
            }
        }
    }

    @Override
    public void onUpdate(Consumer<MapData> toRun) {
        cons = toRun;
    }
}
