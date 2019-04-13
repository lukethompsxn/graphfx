package org.graphfx.util;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;

import java.util.*;

/**
 * This class represents the graph tree hierarchy visualisation. It handles the
 * interactions with GraphStream library and carries out the creation of the
 * visualised graph and highlighting.
 *
 * @author Luke Thompson
 */
public class GraphWindow {
    private GFXGraph graphData;
    private Graph visualisedGraph;
    private Map<Node, Integer> nodeLevels;
    private Map<Integer, Integer> levels;
    private Set<String> previousNodes;
    private Set<String> previousEdges;
    private Set<String> currentNodes = new HashSet<>();
    private Set<String> currentEdges = new HashSet<>();

    private static final String UI_LABEL = "ui.label";
    private static final String UI_CLASS = "ui.class";
    private static final String MARKED = "marked";
    private static final String SEEN = "seen";
    private static final String COORDINATE_SYSTEM = "xyz";
    private static final String SEPARATOR = "-";
    private static final String DEFAULT_STYLE = "res/styles/default.css";

    public GraphWindow(GFXGraph graphData, String stylePath) {
        this.graphData = graphData;
        nodeLevels = new HashMap<>();
        levels = new HashMap<>();

        if (stylePath == null || stylePath.isEmpty()) {
            stylePath = DEFAULT_STYLE;
        }

        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        visualisedGraph = new MultiGraph("GFXGraph");
        visualisedGraph.setAttribute("ui.stylesheet", "url('" + stylePath + "')");
    }

    /**
     * This method is used to draw the initial tree of nodes and edges which
     * make up the graph.
     */
    public void drawTree() {
        int z = 0;

        determineLevels();

        for (Node node : graphData.getAllNodes()) {
            if (visualisedGraph.getNode(node.getLabel()) == null) {
                visualisedGraph.addNode(node.getLabel()).setAttribute(UI_LABEL, node.getLabel());
                visualisedGraph.getNode(node.getLabel()).setAttribute(COORDINATE_SYSTEM, getXCoordinate(node), nodeLevels.get(node), z);
            }

            for (Edge edge : graphData.getEdges(node)) {
                if (visualisedGraph.getNode(edge.getChild().getLabel()) == null) {
                    visualisedGraph.addNode(edge.getChild().getLabel()).setAttribute(UI_LABEL, edge.getChild().getLabel());
                    visualisedGraph.getNode(edge.getChild().getLabel()).setAttribute(COORDINATE_SYSTEM, getXCoordinate(edge.getChild()), nodeLevels.get(edge.getChild()), z);
                }
                String id = edge.getParent().getLabel() + SEPARATOR + edge.getChild().getLabel();
                if (visualisedGraph.getEdge(id) == null) {
                    visualisedGraph.addEdge(id, edge.getParent().getLabel(), edge.getChild().getLabel(), true);
                }
            }
        }
    }

    /**
     * This method is called whenever the current schedule is updated to handle
     * the redrawing of the highlighted visualisation.
     */
    public void drawHighlighting(Set<Node> scheduledNodes) {
        previousNodes = new HashSet<>(currentNodes);
        previousEdges = new HashSet<>(currentEdges);
        currentNodes = new HashSet<>();
        currentEdges = new HashSet<>();

        for (Node parent : scheduledNodes) {
            visualisedGraph.getNode(parent.getLabel()).setAttribute(UI_CLASS, MARKED);
            currentNodes.add(parent.getLabel());
            for (Edge e : graphData.getEdges(parent)) {
                if (scheduledNodes.contains(e.getChild())) {
                    String label = parent.getLabel() + SEPARATOR + e.getChild().getLabel();
                    visualisedGraph.getEdge(label).setAttribute(UI_CLASS, MARKED);
                    currentEdges.add(label);
                }
            }
        }

        for (String s : previousNodes) {
            if (!currentNodes.contains(s)) {
                visualisedGraph.getNode(s).setAttribute(UI_CLASS, SEEN);
            }
        }

        for (String s : previousEdges) {
            if (!currentEdges.contains(s)) {
                visualisedGraph.getEdge(s).setAttribute(UI_CLASS, SEEN);
            }
        }
    }

    /**
     * This method is used to return the view panel which contains the graph
     * object.
     *
     * @return fxViewPanel containing graph
     */
    public FxViewPanel getViewPanel() {
        FxViewer fxViewer = new FxViewer(visualisedGraph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        return (FxViewPanel) fxViewer.addDefaultView(false);
    }

    /**
     * This method is used to determine the level which is the node is to be
     * placed on in the visualisation.
     * This is done by placing the node one level below its lowest parent.
     */
    private void determineLevels() {
        List<Node> exitNodes = graphData.getExitNodes();
        int maxLevel = 0;

        Queue<Node> queue = new ArrayDeque<>();

        for (Node node : exitNodes) {
            nodeLevels.put(node, 0);
        }

        queue.addAll(exitNodes);

        while (!queue.isEmpty()) {
            Node node = queue.poll();

            for (Node parent : graphData.getParents(node)) {

                if (nodeLevels.containsKey(parent)) {
                    if (nodeLevels.get(parent) < nodeLevels.get(node) + 2) {
                        nodeLevels.put(parent, nodeLevels.get(node) + 2);
                        queue.add(parent);

                        if (nodeLevels.get(parent) > maxLevel) {
                            maxLevel = nodeLevels.get(parent);
                        }
                    }
                } else {
                    nodeLevels.put(parent, nodeLevels.get(node) + 2);
                    queue.add(parent);

                    if (nodeLevels.get(parent) > maxLevel) {
                        maxLevel = nodeLevels.get(parent);
                    }
                }

            }
        }

        for (Node node : graphData.getEntryNodes()) {
            nodeLevels.put(node, maxLevel);
        }

        for (Integer i : nodeLevels.values()) {
            if (levels.containsKey(i)) {
                levels.put(i, levels.get(i) + 1);
            } else {
                levels.put(i, 1);
            }
        }
    }

    /**
     * This method is used to retrieve the X coordinate for the given node.
     * This is done by using the number of nodes at that level in the display
     * hierarchy to create an even distribution at each level.
     *
     * @param node the node to retrieve the x coordinate for
     * @return the x coordinate for the given node
     */
    private double getXCoordinate(Node node) {
        int level = nodeLevels.get(node);
        int num = levels.get(level);

        if (num % 2 == 0) {
            if (num - 2 == 0) {
                levels.put(level, num - 4);
            } else {
                levels.put(level, num - 2);
            }
        } else {
            levels.put(level, num - 2);
            num = num - 1;
        }
        return (0 - num);
    }
}
