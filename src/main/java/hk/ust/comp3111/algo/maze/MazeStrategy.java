package hk.ust.comp3111.algo.maze;

import hk.ust.comp3111.api.MapData;

import javax.swing.*;
import java.util.function.Consumer;

/**
 * Encapsulates a maze generation strategy.
 * Implementors of this interface should modify the given map data object to a maze.
 */
public interface MazeStrategy {
    /**
     * Generates a maze.
     * All existing map data will be erased.
     * @param data The map data instance to generate a maze at.
     */
   void generate(MapData data);

    /**
     * When the map is updated, runs the given consumer with the current map data.
     * Note: Implementations may simply call the consumer.
     * It is not recommended for the consumer to block the caller for an extended period of time.
     * If the consumer performs UI updates, please use methods such as {@link SwingUtilities#invokeLater(Runnable)},
     * which runs on the event dispatch thread.
     * @param toRun The consumer to run
     */
    void onUpdate(Consumer<MapData> toRun);
}
