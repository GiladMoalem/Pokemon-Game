package tests;

import api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Random;


public class Tester_DWGraph_Algo {
    private Random rand = new Random();

    @Test
    void copy(){
        directed_weighted_graph g = new DWGraph_DS();
        createGraph(g,10,20);
        dw_graph_algorithms G1 = new DWGraph_Algo();
        G1.init(g);
        dw_graph_algorithms G2 = new DWGraph_Algo();
        G2.init (G1.copy());

        directed_weighted_graph g1 = G1.getGraph();
        directed_weighted_graph g2 = G2.getGraph();

        LinkedList<node_data> verG = new LinkedList<>(g1.getV());
        LinkedList<node_data> verG1 = new LinkedList<>(g2.getV());
        while(!verG.isEmpty() && !verG1.isEmpty()){
            node_data node1 = verG.pollFirst();
            node_data node2 = verG1.pollFirst();
            Assertions.assertEquals(node1.getKey(), node2.getKey());
            LinkedList<edge_data> nei1 =new LinkedList<>(g.getE(node1.getKey()));
            LinkedList<edge_data> nei2 =new LinkedList<>(g1.getE(node2.getKey()));
            for(edge_data node: nei1){
                boolean plag =false;
                for (edge_data node3: nei2)
                    if(node.getDest()==node3.getDest()){
                        plag=true;
                        Assertions.assertEquals(g.getEdge(node1.getKey(),node.getDest()) , g1.getEdge(node2.getKey(),node3.getDest()));
                    }
                Assertions.assertTrue(plag);
            }
            for(edge_data node: nei2){
                boolean plag =false;
                for (edge_data node3: nei1)
                    if(node.getDest()==node3.getDest())
                        plag=true;
                Assertions.assertTrue(plag);
            }
        }
        Assertions.assertEquals(verG.isEmpty(),verG1.isEmpty());
        Assertions.assertEquals(G1.isConnected(),G2.isConnected());
        int v1 = rand.nextInt(g.nodeSize());
        int v2 = rand.nextInt(g.nodeSize());

        Assertions.assertEquals(G1.shortestPathDist(v1,v2),G2.shortestPathDist(v1,v2));
    }
    @Test
    void shortestPath(){
        dw_graph_algorithms G1 = new DWGraph_Algo();//null graph
        Assertions.assertEquals(true,G1.isConnected());
        Assertions.assertEquals(null,G1.shortestPath(0,1));
        Assertions.assertEquals(-1,G1.shortestPathDist(0,1));

        G1.init(new DWGraph_DS());//one vertex graph
        G1.getGraph().addNode(new Inode_data(0));
        Assertions.assertEquals(true,G1.isConnected());
        Assertions.assertEquals(null,G1.shortestPath(0,1));
        Assertions.assertEquals(0,G1.shortestPath(0,0).get(0).getKey());
        Assertions.assertEquals(1,G1.shortestPath(0,0).size());
        Assertions.assertEquals(0,G1.shortestPathDist(0,0));
        Assertions.assertEquals(-1,G1.shortestPathDist(1,1));
        long start = System.currentTimeMillis();
        directed_weighted_graph g = new DWGraph_DS();//connected graph
        createConnectedGraph(g,2000);
        long after = System.currentTimeMillis();
        System.out.println("tack for to mack the graph: " + (after-start)/1000.0+"s'");
        G1.init(g);
        Assertions.assertEquals(true, G1.isConnected());
        System.out.println("tack for check connection: "+ (System.currentTimeMillis()-after)/1000.0+"s'");
    }
    void createConnectedGraph(directed_weighted_graph g, int v){
        int [] vertex = new int[v];
        for (int i = 0; i < v; i++ ) {
            node_data n = new Inode_data();
            g.addNode(n);
            vertex[i] = n.getKey();
        }
        for(int i=0;i<v;i++){
            for(int j =0;j<v;j++)
                if(i!=j)
                    g.connect(i,j,rand.nextDouble());
        }
        for(int i=0;i<v;i++){
            for(int j =0;j<v;j++)
                if(i!=j)
                    g.connect(j,i,rand.nextDouble());
        }
    }

    void createGraph(directed_weighted_graph g, Integer v, int e){
        for (int i = 0; i < v; i++ )
            g.addNode(new Inode_data());

        node_data[] vertex = new node_data[g.nodeSize()];
        LinkedList<node_data> ll = new LinkedList<node_data>(g.getV());

        for (int i = 0; i < vertex.length; i++) {
            vertex[i] = ll.pollFirst();
        }
        int num1, num2;
        for (int i=0; i < e; i++){
            num1 = rand.nextInt(vertex.length);
            num2 = rand.nextInt(vertex.length);
            while(num1==num2)
                num2 = rand.nextInt(vertex.length);
            if(g.getEdge(num1,num2)==null)
                g.connect(num1,num2, rand.nextDouble());
            else
                i--;
        }
    }

}