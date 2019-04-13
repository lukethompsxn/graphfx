package util;

import java.util.Collection;
import java.util.List;

/**
 * Specifies the interface for all graphs representing a task schedule.
 * Classes implementing this interface should store the complete task schedule
 * and provide the implementation for the defined contracts.
 *
 * @author Zhi Qiao, Abhinav Behal
 */
public interface GFXGraph {

    /**
     * Gets the nodes in the graph.
     *
     * @return a collection of all the nodes in the graph
     */
    Collection<Node> getAllNodes();

    /**
     * Gets the outgoing edges from the given node.
     *
     * @param node the node to get the outgoing edges from
     * @return a list of the outgoing edges from the node
     */
    List<Edge> getEdges(Node node);

    /**
     * Gets the parent nodes of the given node.
     *
     * @param node the node for which to get the parents of
     * @return a list of the parent nodes
     */
    List<Node> getParents(Node node);

    /**
     * Gets the entry nodes of the graph
     *
     * @return a list of entry nodes into the graph
     */
    List<Node> getEntryNodes();

    /**
     * Gets the exit nodes of the graph
     *
     * @return a list of exit nodes out of the graph
     */
    List<Node> getExitNodes();
}
