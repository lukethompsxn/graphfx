package org.graphfx;

import org.graphfx.util.Node;

import java.util.Set;

/**
 * @author Luke Thompson
 */
public interface GFXStateManager {

    /**
     * This method is designed to return the current scheduled nodes on
     * the task graph.
     *
     * @return set of currently scheduled nodes
     */
    Set<Node> getCurrentNodes();


    /**
     * This method is designed to return the optimal scheduled nodes once they
     * have been determined
     *
     * @return set of optimally scheduled nodes
     */
    Set<Node> getOptimalNodes();

    /**
     * This method is designed to determine when the algorithm has completed its
     * scheduling. This this be implemented by your state manager to return
     * true when scheduling is complete, and false otherwise.
     *
     * @return true when scheduling complete; false otherwise
     */
    boolean hasCompleted();
}
