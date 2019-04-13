package util;

/**
 * Represents a node in a graph, with a given label and cost.
 *
 * @author Abhinav Behal
 */
public class Node {
    private final String label;
    private final int cost;

    /**
     * Creates a node with a given label and cost.
     *
     * @param label the label of the node
     * @param cost  the cost of the node
     */
    public Node(String label, int cost) {
        this.label = label;
        this.cost = cost;
    }

    /**
     * Returns the label of the node.
     *
     * @return the label of the node
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the cost of the node.
     *
     * @return the cost of the node.
     */
    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Node)) return false;
        return label.equals(((Node) o).label);
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }
}
