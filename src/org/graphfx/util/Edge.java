package org.graphfx.util;

import java.util.Objects;

/**
 * Represents a directed edge in a graph, with given parent and child nodes
 * as well as a cost.
 *
 * @author Abhinav Behal
 */
public class Edge {
    private final Node parent;
    private final Node child;
    private final int cost;

    /**
     * Creates a directed edge between a given parent and child node.
     *
     * @param parent the parent node
     * @param child  the child node
     * @param cost   the cost of the edge
     */
    public Edge(Node parent, Node child, int cost) {
        this.parent = parent;
        this.child = child;
        this.cost = cost;
    }

    /**
     * Returns the parent node in the edge.
     *
     * @return the parent node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Returns the child node in the edge.
     *
     * @return the child node
     */
    public Node getChild() {
        return child;
    }

    /**
     * Returns the cost of the edge.
     *
     * @return the cost of the edge
     */
    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return String.format("\t%s -> %s\t [Weight=%d];", parent, child, cost);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Edge)) return false;
        Edge e = (Edge) o;
        return e.parent.equals(parent) && e.child.equals(child);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, child);
    }
}
