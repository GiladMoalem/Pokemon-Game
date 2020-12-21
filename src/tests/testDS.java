package tests;

import api.DWGraph_DS;
import api.Inode_data;
import api.node_data;
import api.directed_weighted_graph;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class testDS {
    private final Random rand = new Random();
    @Test
    public void node(){
        directed_weighted_graph G = new DWGraph_DS();
        createGraph(G, 10, 20);
    }


    @Test
    public void nodeSize() {
        directed_weighted_graph graph = new DWGraph_DS();
        Assertions.assertEquals(0, graph.nodeSize());
        graph.addNode(new Inode_data(2));
        graph.addNode(new Inode_data(1));
        graph.addNode(new Inode_data(1));
        Assertions.assertEquals(2,graph.nodeSize());
        graph.removeNode(3);
        graph.removeNode(1);
        graph.removeNode(1);
        int s = graph.nodeSize();
        Assertions.assertEquals(1,s);
    }

    @Test
    public void edgeSize() {
        directed_weighted_graph graph = new DWGraph_DS();
        graph.addNode(new Inode_data(0));
        graph.addNode(new Inode_data(1));
        graph.addNode(new Inode_data(2));
        graph.addNode(new Inode_data(3));
        graph.connect(0,1,1);
        graph.connect(0,2,2);
        graph.connect(0,3,3);
        graph.connect(1,0,1.5);
        graph.connect(0,0,4);
        graph.connect(0,6,2);
        int e_size =  graph.edgeSize();
        Assertions.assertEquals(4, e_size);

    }
    @Test
    public void getV() {
        directed_weighted_graph g = new DWGraph_DS();
        createGraph(g,10,45);
        Collection<node_data> v = g.getV();
        Iterator<node_data> iter = v.iterator();
        while (iter.hasNext()) {
            node_data n = iter.next();
            Assertions.assertNotNull(n);
            Assertions.assertEquals(g.getNode(n.getKey()),n);
        }
    }
    @Test
    public void copyGraph(){
        directed_weighted_graph g = new DWGraph_DS();
        createGraph(g,10, 20);
        directed_weighted_graph g1 = new DWGraph_DS(g);
        LinkedList<node_data> verG = new LinkedList<>(g.getV());
        LinkedList<node_data> verG1 = new LinkedList<>(g1.getV());
        while(!verG.isEmpty() && !verG1.isEmpty()){
            node_data node1 = verG.pollFirst();
            node_data node2 = verG1.pollFirst();
            Assertions.assertEquals(node1.getKey(), node2.getKey());
            LinkedList<node_data> nei1 =new LinkedList<>(g.getV());//neighbors of node1
            LinkedList<node_data> nei2 =new LinkedList<>(g1.getV());//neighbors of node2
            for(node_data node: nei1){
                boolean plag =false;
                for (node_data node3: nei2)
                    if(node.getKey()==node3.getKey()){
                        plag=true;
                        Assertions.assertEquals(g.getEdge(node1.getKey(),node.getKey()) , g1.getEdge(node2.getKey(),node3.getKey()));// the edge's weight the same
                    }
                Assertions.assertTrue(plag);
            }
            for(node_data node: nei2){
                boolean plag =false;
                for (node_data node3: nei1)
                    if(node.getKey()==node3.getKey())
                        plag=true;
                Assertions.assertTrue(plag);
            }
        }
        Assertions.assertEquals(verG.isEmpty(),verG1.isEmpty());
    }
    @Test
    public void remove(){
        directed_weighted_graph graph = new DWGraph_DS();
        graph.addNode(new Inode_data(0));
        graph.addNode(new Inode_data(1));
        graph.addNode(new Inode_data(2));
        graph.addNode(new Inode_data(3));
        graph.addNode(new Inode_data(4));
        graph.addNode(new Inode_data(5));
        graph.addNode(new Inode_data(6));
        graph.addNode(new Inode_data(7));
        graph.connect(0,1,1.1);
        graph.connect(0,2,-1);
        graph.connect(0,3,1);
        graph.connect(0,2,8);
        graph.connect(1,0,1.1);
        graph.connect(3,4,12);
        graph.connect(7,1,9);
        Assertions.assertEquals(8,graph.nodeSize());
        Assertions.assertEquals(6,graph.edgeSize());
        Assertions.assertEquals(14,graph.getMC());
        graph.removeNode(0);
        Assertions.assertEquals(7,graph.nodeSize());
        Assertions.assertEquals(2,graph.edgeSize());
        Assertions.assertEquals(15,graph.getMC());

    }

    void createGraph(directed_weighted_graph g,Integer v, int e){
        for (int i = 0; i < v; i++ )
            g.addNode(new Inode_data());

        node_data[] vertex = new node_data[g.nodeSize()];
        LinkedList<node_data> ll = new LinkedList<>(g.getV());

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
                g.connect(num1,num2, rand.nextDouble()*10);
            else
                i--;
        }
    }

}
