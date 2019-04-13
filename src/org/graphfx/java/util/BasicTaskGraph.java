package util;

import java.util.*;

/**
 * Represents a Directed Acyclic GFXGraph where nodes are tasks and edges
 * represent dependencies.
 *
 * @author Abhinav Behal, Zhi Qiao
 */
public class BasicTaskGraph implements GFXGraph {
    private final Map<String, Node> nodes;
    private final Map<Node, List<Edge>> children;
    private final Map<Node, List<Node>> parents;
    private final Map<Node, Map<Node, Integer>> edgeCosts;

    /**
     * Creates a BasicTaskGraph with a given name, nodes, and associated edges.
     *
     * @param nodes the list of nodes in the BasicTaskGraph
     * @param edges the list of edges connecting nodes in the BasicTaskGraph
     */
    public BasicTaskGraph(Map<String, Node> nodes, List<Edge> edges) {
        children = new HashMap<>();
        parents = new HashMap<>();
        edgeCosts = new HashMap<>();
        this.nodes = nodes;

        build(edges);
    }

    @Override
    public Collection<Node> getAllNodes() {
        return nodes.values();
    }

    @Override
    public List<Edge> getEdges(Node node) {
        return children.get(node);
    }

    @Override
    public List<Node> getParents(Node node) {
        return parents.get(node);
    }

    @Override
    public List<Node> getEntryNodes() {
        List<Node> entries = new ArrayList<>();

        for (Node n : nodes.values()) {
            if (!parents.containsKey(n) || parents.get(n).isEmpty())
                entries.add(n);
        }
        return entries;
    }

    @Override
    public List<Node> getExitNodes() {
        List<Node> exits = new ArrayList<>();

        for (Node n : nodes.values()) {
            if (!children.containsKey(n) || children.get(n).isEmpty())
                exits.add(n);
        }

        return exits;
    }

    /**
     * Private helper method used to construct the edges connecting the graph.
     *
     * @param edges edges connecting nodes in the BasicTaskGraph
     */
    private void build(List<Edge> edges) {
        for (Node n : nodes.values()) {
            parents.put(n, new ArrayList<>());
            children.put(n, new ArrayList<>());
        }

        for (Edge e : edges) {
            Node parentNode = nodes.containsValue(e.getParent()) ? e.getParent() : null;
            Node childNode = nodes.containsValue(e.getChild()) ? e.getChild() : null;

            if (parentNode == null || childNode == null) return;

            children.get(parentNode).add(e);
            parents.get(childNode).add(parentNode);

            if (!edgeCosts.containsKey(parentNode))
                edgeCosts.put(parentNode, new HashMap<>());

            edgeCosts.get(parentNode).put(childNode, e.getCost());
        }
    }
}
