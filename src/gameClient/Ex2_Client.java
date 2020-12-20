/*
package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import gameClient.util.Point3D;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Ex2_Client implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;

	public static void main(String[] a) {
		Thread client = new Thread(new Ex2_Client());
		client.start();
	}
	
	@Override
	public void run() {
		int scenario_num = 0;//level
		game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
		//	int id = 999;
		//	game.login(id);
		String g = game.getGraph();
		String pks = game.getPokemons();
		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
		init(game);

///*
		game.startGame();
		_win.setTitle("Ex2 - OOP: (NONE trivial Solution) " + game.toString());

		int ind = 0;
		long dt = 100;
		boolean run = true;
//		_ar.getAgents();     // -> update in moveAgent()

		String move = moveAgent(game, gg);
*/
/*		_win.repaint();
		CL_Agent ag = _ar.getAgents().get(0);
		game.chooseNextEdge(ag.getID(),ag.getSrcNode()-1);
		geo_location loc = new Igeo_location(ag.getLocation());
		System.out.println(move);
		long start = System.currentTimeMillis() + 500;
		while (System.currentTimeMillis() < start) {}
		move = moveAgent(game, gg);
		System.out.println(move);
		_win.repaint();

		System.out.println();
		pain(loc, Arena.getAgents(move,gg).get(0).getLocation(), Arena.getAgents(move,gg).get(0));
*//*

*/
/*





	while(game.isRunning()) {
		String move = moveAgent(game, gg);
		_win.repaint();

		System.out.println(_ar.getAgents().get(0).getNextNode());

		long start = System.currentTimeMillis() + 500;
			while (System.currentTimeMillis() < start) {}
//		}
//		else 			System.out.println(_ar.getAgents().get(0).getNextNode());


		System.out.println("----------------------"+ind++);
	}


		if(false){
		while (game.isRunning()) {

			if (ind % 100000 == 0) {
				moveAgent(game, gg);
				_win.repaint();
				try {
						Thread.sleep(dt);
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			ind++;
		}
	}
*//*



		try{Thread.sleep(5000);}catch(Exception e){}
		String res = game.toString();

		System.out.println(res);
		System.exit(0);

	}
	*/
/**
	 * Moves each of the agents along the edge,
	 * in case the agent is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param
	 *//*



	private static String moveAgent(game_service game, directed_weighted_graph gg) { //origin
		String lg = game.move();
		List<CL_Agent> log = Arena.getAgents(lg, gg);

		_ar.setAgents(log);//updates the arena and visual agent
		String fs =  game.getPokemons();
		List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
		_ar.setPokemons(ffs);//updates the arena and visual pokemon
		for (CL_Agent ag: log) {
			int id = ag.getID();
			int dest = ag.getNextNode();
			int src = ag.getSrcNode();
			double v = ag.getValue();

			if(dest==-1) {
				dest = nextNode(gg, src);//algorithm
				dest = (src + 1) % 11;
				game.chooseNextEdge(ag.getID(), dest);//execute //update the next node?
//				ag.setNextNode(dest);//updates the dest node! i added
				System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
			}
		}
		return lg;
	}
	*/
/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 *//*

	private static int nextNode(directed_weighted_graph g, int src) { //how to choose pokemon's
		int ans = -1;
		CL_Pokemon p = _ar.getPokemons().iterator().next();
		_ar.updateEdge(p,g);

		edge_data edge = p.get_edge();
		System.out.println(edge.getSrc()+" "+edge.getDest());
		int srcD,dest;
		if(p.getType()<0){
			if(edge.getDest() - edge.getSrc()<0) {
				srcD = edge.getSrc();
				dest = edge.getDest();
			}else{
				dest = edge.getSrc();
				srcD = edge.getDest();
			}
		}else{
			if(edge.getDest() - edge.getSrc()<0) {
				dest = edge.getSrc();
				srcD = edge.getDest();
			}else{
				srcD = edge.getSrc();
				dest = edge.getDest();
			}
		}
		dw_graph_algorithms G = new DWGraph_Algo();
		G.init(g);
		List<node_data> path = G.shortestPath(src,edge.getSrc());
		if(path==null || path.isEmpty())return -1;
		else
			System.out.println(path.get(0).getKey());
			return path.get(0).getKey();

		*/
/*
			Collection<edge_data> ee = g.getE(src);
			Iterator<edge_data> itr = ee.iterator();
			int s = ee.size();
			int r = (int) (Math.random() * s);
			int i = 0;
			while (i < r) {
				itr.next();
				i++;
			}
			ans = itr.next().getDest();
			return ans;
		*//*

	}

	private void init(game_service game) {
		String g = game.getGraph();
		String fs = game.getPokemons();
		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
		//gg.init(g);
		_ar = new Arena();
		_ar.setGraph(gg);
		_ar.setPokemons(Arena.json2Pokemons(fs));
		_win = new MyFrame("test Ex2");
		_win.setSize(1000, 700);
		_win.update(_ar);

	
		_win.setVisible(true);
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("agents");
			System.out.println(info);
			System.out.println(game.getPokemons());
			int src_node = 0;  // arbitrary node, you should start at one of the pokemon
			ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
			for(int a = 0;a<cl_fs.size();a++) { Arena.updateEdge(cl_fs.get(a),gg);}
			for(int a = 0;a<rs;a++) {
				int ind = a%cl_fs.size();
				CL_Pokemon c = cl_fs.get(ind);
				int nn = c.get_edge().getDest();
				if(c.getType()<0 ) {nn = c.get_edge().getSrc();}
				
				game.addAgent(nn);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
	}
	void pain(geo_location loc1, geo_location loc2,CL_Agent ag){
		System.out.println(loc1.x()+" "+loc1.y());
		System.out.println(loc2);
		double x = (loc1.x()-loc2.x()/10);
		double y = (loc1.y()-loc2.y())/10;

		ag.setPosition(loc1);

		_win.repaint();
		System.out.println(x);

		while(ag.getLocation().distance(loc2)<0.00002){

			_win.repaint();
			ag.setPosition(new Igeo_location(ag.getLocation().x()-x ,ag.getLocation().y()-y ,0));

		}


	}
}
*/
