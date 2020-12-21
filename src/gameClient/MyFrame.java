package gameClient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class MyFrame extends JFrame{
	private Arena _ar;
	private gameClient.util.Range2Range _w2f;
	private MyPanel panel;
	private int moveCounter = 0;



	public MyFrame(String a) {
		super(a);//gives the title
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ends the game when EXIT

		panel = new MyPanel();////
		this.add(panel);
	}
	public void initTimeF(int timetoend){
		panel.initTime(timetoend);
	}
	public void update(Arena ar) {
		this._ar = ar;
		updateFrame();
	}

	private void updateFrame() {
		Range rx = new Range(20,this.getWidth()-37);
		Range ry = new Range(this.getHeight()-120,40);
		Range2D frame = new Range2D(rx,ry);
		directed_weighted_graph g = _ar.getGraph();
		_w2f = Arena.w2f(g,frame);

	}
	public void addMove(){
		moveCounter++;
	}
	@Override
	public void paint(Graphics g) {
		super.paintComponents(g);

	}

	private void drawInfo(Graphics g) {
		List<String> str = _ar.get_info();
		String dt = "none";
		for(int i=0;i<str.size();i++) {
			g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
		}

	}
	private void drawPokemons(Graphics g) {
		List<CL_Pokemon> fs = _ar.getPokemons();
		if(fs!=null) {
		Iterator<CL_Pokemon> itr = fs.iterator();
		
		while(itr.hasNext()) {
			
			CL_Pokemon f = itr.next();
			Point3D c = f.getLocation();
			int r=10;
			g.setColor(Color.green);
			if(f.getType()<0) {g.setColor(Color.orange);}
			if(c!=null) {

				geo_location fp = this._w2f.world2frame(c);
				g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
			//	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
				
			}
		}
		}
	}
	private void drawAgants(Graphics g) {
		g.setColor(Color.red);
		if(_ar.getAgents()!=null)
		for(CL_Agent ag: _ar.getAgents()){
			geo_location c = ag.getLocation();
			int r=8;
			if(c!=null) {
				geo_location fp = this._w2f.world2frame(c);
				g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
			}
		}
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
		geo_location fp = _w2f.world2frame(pos);
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
//		g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
	}




	//////////////////////////////////
	public class MyPanel extends JPanel {
		private long time;
		private LocalDateTime startTime;

		public MyPanel(){
			this.setBackground(Color.pink);


		}
		public void initTime(int timeToEnd){
			this.time = timeToEnd;
			startTime = LocalDateTime.now();
		}

		public void paint(Graphics g) {
			super.printComponent(g);

			drawGraph(g);
			drawPokemons(g);
			drawAgants(g);
			drawInfo(g);
			drawTime(g);
			drawAgentsGrad(g);
			drawMoves(g);

			updateFrame();



		}
		private void drawTime(Graphics g){
			g.setColor(Color.BLACK);
			Font f = new FontUIResource(g.getFont().getName(),20,20);
			g.setFont(f);
			LocalDateTime nowTime = LocalDateTime.now();
			int pas = ((nowTime.getSecond()-startTime.getSecond()+60 )%60);
			String s = "Time: "+(time - pas)+"";
			g.drawString(s,this.getWidth()/2,this.getHeight()-10);
		}
		private void drawAgentsGrad(Graphics g){
			for(int i=0; i<_ar.getAgents().size(); i++){
				CL_Agent ag = _ar.getAgents().get(i);
				String s = "AGENT "+ag.getID()+": "+ag.getValue();
				g.drawString(s,30,this.getHeight()-(20*i)-10);
			}

		}
		private void drawMoves(Graphics g){
			String s = "Moves: "+moveCounter;
			g.drawString(s,this.getWidth()-140,this.getHeight()-10);

		}

	}

}
