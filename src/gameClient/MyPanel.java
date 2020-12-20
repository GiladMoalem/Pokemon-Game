/*
package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class MyPanel extends JPanel {

    private Arena _ar;
    private gameClient.util.Range2Range _w2f;

    */
/*public MyPanel(){

        this.setBackground(Color.pink);
    }
    public void paint(Graphics g) {
        super.printComponent(g);

        g.fillOval(200, 200, 50, 50);
//        drawGraph(g);
        *//*
*/
/*g.setColor(Color.CYAN);
        g.fillOval(100,100,45,60);
        g.drawOval(100,100,23,23);*//*
*/
/*

    }
    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while(iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.black);
            drawNode(n,5,g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while(itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.gray);
                drawEdge(e, g);
            }
        }
    }

    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);//paint nodes
        g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r); //paints the key above all nodes
    }
    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
        //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }

}
*/
