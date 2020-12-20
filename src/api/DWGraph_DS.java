package api;

import java.util.Collection;
import java.util.HashMap;

//This is process

public class DWGraph_DS implements directed_weighted_graph {
    private int edgeSize;
    private int modeCounter;
    private HashMap<Integer,node_data> nodes;
    private HashMap<Integer, HashMap<Integer, edge_data>> edges;   // getting key's node and giving the all neighbors and edges of this node -> [ key's node | [ key's neighbor | edge ] ]

    public DWGraph_DS(){
        nodes = new HashMap<Integer, node_data>();
        edges = new HashMap<Integer, HashMap<Integer, edge_data>>();
    }
//    copy deep constructor run in o(v) v=vertexes
    public DWGraph_DS(directed_weighted_graph g){
        this.edgeSize = g.edgeSize();
        this.modeCounter = g.getMC();

        this.nodes = new HashMap<>();//deep copy for all the nodes
        this.edges = new HashMap<Integer, HashMap<Integer, edge_data>>();

        for (node_data n: g.getV()) {
            this.nodes.put(n.getKey(), new Inode_data(n) );
            edges.put(n.getKey(), new HashMap<Integer, edge_data>());
            for(edge_data edge : g.getE(n.getKey()) ){
                edges.get(n.getKey()).put( edge.getDest(), new Iedge_data(edge) );
            }
        }
    }
//    copy deep opposite constructor run in o(v) v=vertex
    public DWGraph_DS(directed_weighted_graph g, int opposite){
        this.edgeSize = g.edgeSize();
        this.modeCounter = g.getMC();

        this.nodes = new HashMap<>();//deep copy for all the nodes
        this.edges = new HashMap<Integer, HashMap<Integer, edge_data>>();

        for (node_data n: g.getV()) {
            this.nodes.put(n.getKey(), new Inode_data(n) );
            if(!edges.containsKey(n.getKey())) {
                edges.put(n.getKey(), new HashMap<Integer, edge_data>());
            }
            for(edge_data edge : g.getE(n.getKey()) ){
                if(!edges.containsKey(edge.getDest())) {
                    edges.put(edge.getDest(), new HashMap<Integer, edge_data>());
                }
                edges.get(edge.getDest()).put( edge.getSrc(), new Iedge_data(edge,0) );
            }
        }
    }

    @Override
    public node_data getNode(int key) {
        return nodes.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        if(!nodes.containsKey(src) || !nodes.containsKey(dest) || !edges.get(src).containsKey(dest)) return null;
        return edges.get(src).get(dest);
    }

    @Override
    public void addNode(node_data n) {
        if(nodes.containsKey(n.getKey())) return;
        nodes.put(n.getKey(), n);
        edges.put(n.getKey(), new HashMap<Integer, edge_data>());
        modeCounter++;
    }

    @Override
    public void connect(int src, int dest, double w) {
        if(!nodes.containsKey(src) || !nodes.containsKey(dest) || src == dest || w<0 ) return;

        if(  !edges.get(src).containsKey(dest) ){//new connection
            edges.get(src).put(dest, new Iedge_data(src, dest, w));
            edgeSize++;
            modeCounter++;
        }else if(edges.get(src).get(dest).getWeight() != w)//update connection
            ((Iedge_data)edges.get(src).get(dest)).setWeight(w);
            modeCounter++;
    }

    @Override
    public Collection<node_data> getV() {
        return nodes.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        if(!nodes.containsKey(node_id)) return null;
        return edges.get(node_id).values();
    }

    @Override
    public node_data removeNode(int key) {
        if(!nodes.containsKey(key)) return null;
        edgeSize -= edges.get(key).size();//remove all edges start with key
        edges.remove(key);

        for (node_data n : nodes.values()) {//remove all edges end with key
            if( n.getKey()!=key && edges.get(n.getKey()).containsKey(key) ){
                edges.get(n.getKey()).remove(key);
                edgeSize--;
            }
        }

        modeCounter++;
        return nodes.remove(key);
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        if(!nodes.containsKey(src) || !nodes.containsKey(dest))return null;
        if(edges.get(src).containsKey(dest)) {
            edgeSize--;
            modeCounter++;
        }
        return edges.get(src).remove(dest);
    }

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    @Override
    public int edgeSize() {
        return edgeSize;
    }

    @Override
    public int getMC() {
        return modeCounter;
    }

}
