# GraphFX

A simple and powerful extension to [GraphStream](http://graphstream-project.org/), specialised for JavaFX.

This project is designed to make integration of GraphStream into a JavaFX application much more simple, and to produce more visualisally pleasing results. This library handles the multithreading, updating, and creation of the graph, all you need to do is supply the graph data and implement a manager interface. 

In addition to the graph stream implementation, GraphFX automatically determines the levels of your nodes and correctly allocates them, thus allowing you to have defined levels of dynamic data. GraphFX also automatically distributes the nodes horizontally, ensuring that your graph is always centralised. It also automatically handles highlighting of nodes and edges when your algorithm has them currently scheduled, or has had them previously scheduled.

### Getting Started
Download the latest [release](https://github.com/lukethompsxn/graphfx/releases) and add it as a dependency to your project. 

**Basic Structure**
- `GFXManager`: This is the class you will interact with, it contains the method `createGraph()` for generating the graph and returning the `FxViewPanel`, the panel which you add to your JavaFX application.
- `GFXGraph`: This is an interface for the graph data class which is expected to be passed into the `GFXManager` constructor. You can use a pre-implemented concrete class `BasicTaskGraph` where you simply pass a list of `Nodes` and `Edges`.
- `GFXStateManager`: This is the interface which your algorithm state manager class must implement. It has two methods, `getCurrentNodes()` and `getOptimalNodes()` which are used to update the graph. 

**Using GraphFX**

To create a graph, simply create a new `GFXManager`, passing in your `GFXGraph` data, `GFXStateManager` concrete class and the path to the stylesheet. Then call `createGraph()` which will return the panel you need to include in your project. Thats all you need to do, GraphFX will handle the rest from there! 

```java
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
```java
// Creating the visual graph 

GFXManager manager = new GFXManager(graphData, stateManager, stylesPath);
FxViewPanel uiPanel = manager.createGraph();
anchorPane.getChildren().add(uiPanel);
```
**Styling**

You can define your own style sheet for styling the nodes, edges and the graph. Unfortunately, GraphStream does NOT support full css, please find the supported styling [here](http://graphstream-project.org/doc/Advanced-Concepts/GraphStream-CSS-Reference#the-css-reference). If you don't define your style sheet, then it will automatically use default styling. Please consult this [template](https://github.com/lukethompsxn/graphfx/blob/master/res/styles/default.css) for how to style your graph.


### Examples

![](https://lukethompson.co/resource/images/graphfx.jpg)

### Acknowledgements
- [Abhinav Behal](https://github.com/AbhinavBehal) and [Zhi Qiao](https://github.com/Z-Qi) for their help creating the `util` package.
- [GraphStream](http://graphstream-project.org/)

### License 
MIT License. Please refer to [LICENSE](https://github.com/lukethompsxn/graphfx/blob/master/LICENSE)
