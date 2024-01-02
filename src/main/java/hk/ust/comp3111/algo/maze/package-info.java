/**
 * Contains several maze generation algorithms for use in the map editor.
 *
 * <p>
 * The main interface here is {@link hk.ust.comp3111.algo.maze.MazeStrategy}. It defines a common set of
 * operations which all maze generation algorithms must support - a subscriber
 * ({@link hk.ust.comp3111.algo.maze.MazeStrategy#onUpdate(java.util.function.Consumer)})
 * which runs the given {@link java.util.function.Consumer} on map updates, and
 * a {@link hk.ust.comp3111.algo.maze.MazeStrategy#generate(hk.ust.comp3111.api.MapData)}
 * method which initializes the map data instance, then generates a maze.
 * </p>
 *
 * <p>
 * The package supports three maze generation algorithms out of the box - Randomized Prim's algorithm, the
 * Recursive Backtracking method, and the Recursive Division method.
 * </p>
 *
 * <p>
 * The Randomized Prim's algorithm constructs a minimum spanning tree. Since all of the costs of adding a
 * vertex to the tree are the same for a maze on a grid, the algorithm randomly chooses an edge from the set
 * of all possible edges which does not connect to a previously explored vertex. By running the algorithm until
 * all the vertices are visited, there is a single path from any vertex to the other vertex.
 * </p>
 *
 * <p>
 * The Recursive Backtracking method operates similarly to the Randomized Prim's algorithm above, but instead
 * of randomly choosing an edge form the set of all possible edges, it randomly chooses an edge which does not
 * connect to an explored component from the current vertex. Then, it recursively continues from the new edge
 * until it has no more vertices which can be explored. The result is the same as the above - a tree which has a
 * single path from any vertex to the other vertex.
 * </p>
 *
 * <p>
 * The Recursive Division algorithm is not a tree-based algorithm as the algorithms above are - it recursively
 * divides the grid into partitions by adding either a single row or a single column with one hole in the middle.
 * To prevent a path from being blocked by additional divisions, either the column must be odd or the hole's coordinates
 * must be even. The recursion terminates when the width or the height of the partition is less than or equal to 2.
 * </p>
 *
 * <p>
 * The {@link hk.ust.comp3111.algo.maze.MazeHelper} class provides utility methods which can be used by all the maze
 * generators - a random vertex generator on a single column, and a method which has a high probability of generating an
 * additional path in order to give more choices to the player.
 * </p>
 * @since 0.1
 * @version 1.0
 */
package hk.ust.comp3111.algo.maze;