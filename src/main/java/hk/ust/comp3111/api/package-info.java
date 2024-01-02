/**
 * Defines a set of shared classes for working on a map grid.
 *
 * <p>
 * The main abstractions introduced are the classes {@link hk.ust.comp3111.api.Vertex} and {@link hk.ust.comp3111.api.MapData}.
 * </p>
 * <p>
 * The {@link hk.ust.comp3111.api.Vertex} class includes all the relevant information of a single square on the game map.
 * Each vertex has a {@link hk.ust.comp3111.api.Vertex.TileType}, which governs the displayed colour of the vertex, as well as
 * if the vertex is passable.
 * </p>
 * <p>
 * The {@link hk.ust.comp3111.api.MapData} class performs operations on a group of {@link hk.ust.comp3111.api.Vertex} instances, which
 * when put together constitutes a map grid. MapData provides operations to modify a vertex, modify all vertices by applying
 * an operation, as well as holding {@link hk.ust.comp3111.engine.Entity} data. {@link hk.ust.comp3111.api.MapData} instances
 * can be created by either loading a file, or from a 2D list of vertices.
 * </p>
 * @since 0.1
 * @version 1.0
 */
package hk.ust.comp3111.api;