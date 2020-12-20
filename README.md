# Pokemon Game
# About the project
In this assignment (-'2') we will improve assignment 1, by generalizing the data structure we have developed so that it can support directed graphs. Including the ability to save and restore the graph from a file in Json format, calculate a the shortest directed route, check directed connectivity and more. 

in the src package we have two main packages- api and gameClient (which contains also util package). The main methods we have in api packages are:

Class DWGraph_DS This class represents an undirectional weighted graph based on a HashMap.

public DWGraph_DS() default constructor, create new empty graph.

public DWGraph_DS(graph g) Copy constructor. Copy's a graph with all its data(EdgeCount, ModeCount) and all its neighbor by value.

public node_data getNode(int key) returns a node from the graph represented with this key.

public void addNode(node_data n) adds a node to a graph.

public void connect(int node1, int node2, double w) connects two nodes with an edge (that have a weight- w).

public Collection<node_data> getV() returns a pointer (shallow copy)  for the collection representing all the nodes in the graph.

public node_data removeNode(int key) removes specific node from the graph and all the edges ends or begins in it.

public void removeEdge(int node1, int node2) removes a edge between two nodes in the graph.

public int nodeSize() returns a count of all the nodes in the graph.

Class DWGraph_Algo This class represents a graph algorithms that support the following functions:

Public DWGraph_Algo() Default constructor. Create new DWGraph_Algo with empty graph.

public void init(directed_weighted_graph g) Init the graph on which this set of algorithms operates on.

public directed_weighted_graph copy() Compute a deep copy of this graph.

public boolean isConnected() Check if the graph is connected (there is a valid path from every node to each other node).

public double shortestPathDist(int src, int dest) Receive two keys of nodes – source and dest – and returns the shortest path weight between this two nodes. If there is no path between these two returns -1.

public List<node_data> shortestPath(int src, int dest)- receive two nodes represented by keys called src, dest and returns list's nodes of the shortest path from src to dest if there is path between this two node returns empty list.

Public Boolean save(String file)- saves this weight directed graph to the given file name- in JSON format.

Public Boolean load(String file)- this method load a graph to this graph algorithm. If the file was successfully loaded – the underlying graph of this class will be changed to the loaded one. In case the graph was not loaded, the original graph should remain as is.
