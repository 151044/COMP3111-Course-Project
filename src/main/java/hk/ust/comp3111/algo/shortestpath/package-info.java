/**
 * Contains the shortest path algorithm for use in game loop and shortest path visualizer.
 *
 * <p>
 *      The {@link hk.ust.comp3111.algo.shortestpath.BreadthFirstSearch} class contains the Breadth First Search algorithm.
 * </p>
 *
 * <p>
 *      The class provides two methods - general Breadth First Search ({@link hk.ust.comp3111.algo.shortestpath.BreadthFirstSearch#bfsGeneral(hk.ust.comp3111.api.MapData, hk.ust.comp3111.api.Vertex, hk.ust.comp3111.api.Vertex, boolean)}) which searches for the shortest path or every reachable Vertex between two Vertices,
 *      and ({@link hk.ust.comp3111.algo.shortestpath.BreadthFirstSearch#randomReachableVertex(hk.ust.comp3111.api.MapData, hk.ust.comp3111.api.Vertex, hk.ust.comp3111.api.Vertex)}) which returns a random reachable Vertex between two Vertices.
 * </p>
 *
 * @since 0.1
 * @version 1.0
 */

package hk.ust.comp3111.algo.shortestpath;