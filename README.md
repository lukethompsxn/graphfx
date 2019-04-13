# GraphFX

A simple and powerful extension to [GraphStream](http://graphstream-project.org/), specialised for JavaFX.

This project is designed to make integration of GraphStream into a JavaFX application much more simple, and to produce more visualisally pleasing results. This library handles the multithreading, updating, and creation of the graph, all you need to do is supply the graph data and implement a manager interface. 

### Getting Started
Download the latest [release](https://github.com/lukethompsxn/graphfx/releases) and add it as a dependency to your project. 

**Basic Structure**
- `GFXManager`: This is the class you will interact with, it contains the method `createGraph()` for generating the graph and returning the `FxViewPanel`, the panel which you add to your JavaFX application.
- `GFXGraph`: This is an interface for the graph data class which is expected to be passed into the `GFXManager` constructor. You can use a pre-implemented concrete class `BasicTaskGraph` where you simply pass a list of `Nodes` and `Edges`.
- `GFXStateManager`: This is the interface which your algorithm state manager class must implemented. It has two methods, `getCurrentNodes()` and `getOptimalNodes()` which are used to update the graph. 

**Using GraphFX**

To create a graph, simply create a new `GFXManager`, passing in your `GFXGraph` data, `GFXStateManager` concrete class and the path to the stylesheet. Then call `createGraph()` which will return the panel you need to include in your project. Thats all you need to do, GraphFX will handle the rest from there! 

```
// Creating your graphData object
Map<String, Node> nodes = new HashMap<>();
Node nodeA = new Node("A", 2);
Node nodeB = new Node("B", 1);
nodes.put(nodeA.getId(), nodeA);
nodes.put(nodeB.getId(), nodeB);

List<Edge> edges = new ArrayList<>();
Edge edge = new Edge(nodeA.getId(), nodeB.getId(), 4);
edges.add(edge);

GFXGraph graphData = new BasicTaskGraph(nodes, edges);
```
```
// Creating the visual graph 
GFXManager manager = new GFXManager(graphData, stateManager, stylesPath);
FxViewPanel uiPanel = manager.createGraph();
anchorPane.getChildren().add(uiPanel);
```

**Example of a Project Using GraphFX*

![](https://raw.githubusercontent.com/lukethompsxn/TaskScheduler/master/res/wiki/GraphView.png)

### Acknowledgements
- [Abhinav Behal](https://github.com/AbhinavBehal) and [Zhi Qiao](https://github.com/Z-Qi) for their help creating the `util` package.
- [GraphStream](http://graphstream-project.org/)
