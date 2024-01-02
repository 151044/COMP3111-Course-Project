package hk.ust.comp3111.algo.maze;

import hk.ust.comp3111.api.Coord;
import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.api.Vertex;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import static hk.ust.comp3111.api.Vertex.TileType.*;

/**
 * Implements a randomized version of Prim's algorithm for generating mazes.
 */
public class RandomizedPrim implements MazeStrategy {
    private Consumer<MapData> cons = null;
    private final Random random = new Random();
    @Override
    public void generate(MapData data) {
        data.forAll(v -> v.setTileType(BARRIER));
        Vertex root = data.at(new Coord(random.nextInt(MapData.DEFAULT_WIDTH), random.nextInt(MapData.DEFAULT_HEIGHT)))
                .orElse(data.at(new Coord(0, 0)).orElseThrow());
        root.setTileType(CLEAR);
        List<Vertex> walls = data.around(root, 2);
        while (walls.size() > 0) {
            Vertex vert = walls.get(random.nextInt(walls.size()));
            List<Vertex> neighbours = data.around(vert, 2).stream()
                            .filter(v -> v.getTileType() == CLEAR).toList();
            Vertex neighbour = neighbours.get(random.nextInt(neighbours.size()));
            int dx = neighbour.x() - vert.x();
            int dy = neighbour.y() - vert.y();
            Vertex between = data.at(vert.getCoord()
                    .translate(new Coord((int) Math.signum(dx), (int) Math.signum(dy))))
                    .orElseThrow();
            between.setTileType(CLEAR);
            vert.setTileType(CLEAR);
            walls.addAll(data.around(vert, 2).stream()
                    .filter(v -> v.getTileType() == BARRIER)
                    .filter(v -> !walls.contains(v)).toList());
            walls.remove(vert);
        }
        Vertex entrance = MazeHelper.generateAtCol(data, 0);
        Vertex exit = MazeHelper.generateAtCol(data, MapData.DEFAULT_WIDTH - 1);
        entrance.setTileType(ENTRANCE);
        exit.setTileType(EXIT);

        MazeHelper.addPath(data);
        if (cons != null) {
            cons.accept(data);
        }
    }

    @Override
    public void onUpdate(Consumer<MapData> toRun) {
        cons = toRun;
    }
}
