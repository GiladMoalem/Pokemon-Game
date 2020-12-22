package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class Ex2 implements Runnable{
    private static MyFrame _win;
    private static Arena _ar;
    private long id;
    private int level;
    private JTextField idText;
    private JTextField levelText;

    public Ex2(){}
    public Ex2(Long id, int level){
        this.id = id;
        this.level = level;
    }

    public void Login() {
        JFrame frame = new JFrame();
        JLabel id = new JLabel("Enter id:");
        JLabel level = new JLabel("Enter level: ");
        idText = new JTextField();
        levelText = new JTextField();
        JButton submit = new JButton("SUBMIT");
        submit.addActionListener(this::ActionPerformed);


        JPanel pnl = new JPanel(new GridLayout(0, 1));
        pnl.add(id);
        pnl.add(idText);
        pnl.add(level);
        pnl.add(levelText);
        pnl.add(submit);

        frame.setLayout(new GridLayout(0, 1));
        frame.setSize(1000, 700);
        frame.add(pnl, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void ActionPerformed(ActionEvent action) {
        this.id = Long.parseLong(idText.getText());
        level = Integer.parseInt(levelText.getText());
        Thread runGame = new Thread(new Ex2(id,level));
        runGame.start();
    }

    @Override
    public void run() {
        game_service game = Game_Server_Ex2.getServer(level); // you have [0,23] games
       	game.login(id);
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        init(game);
        game.startGame();
        _win.initTimeF((int)game.timeToEnd()/1000);

        while(game.isRunning()) {
            moveAgent(game, gg);
            _win.addMove();
            _win.repaint();
//            try{Thread.sleep(90);}catch(Exception e){}
            long start = System.currentTimeMillis() + 100;
            while (System.currentTimeMillis() < start) {}

        }

        String res = game.toString();

        System.out.println(res);
        System.exit(0);

    }

    /**
     * this method initial the game. creates new arena with graph and pokemons from the game.
     * creates new frame.
     * create agents and added it in the game.
     * @param game
     */
    private void init(game_service game) {
        String fs = game.getPokemons();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();

        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
        _win = new MyFrame("test Ex2");
        _win.setSize(1000, 700);
        _win.update(_ar);
        _win.setVisible(true);
        /**
        run on the pokemons list and creates agents. each agent is located next to the greatest pokemon's value
        and added the agents list to the game
        //the agents list not exists in the arena(this._ar)
         */
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());// create list of pokemons without edge
            PriorityQueue<CL_Pokemon> pri = new PriorityQueue<>(cl_fs);
            Object[] p =  pri.toArray();

            CL_Pokemon c =null;
            for(CL_Pokemon a: pri) { Arena.updateEdge(a,gg);}//update edges of pokemons

            for(int a = 0;a<rs;a++) {
                int ind = a%cl_fs.size();
                c = (CL_Pokemon) p[ind];

                int nn = c.get_edge().getSrc();
                if( a>= cl_fs.size()) nn = a%gg.getV().size();
                game.addAgent(nn);
            }

            System.out.println(_ar.getPokemons());

        }
        catch (JSONException e) {e.printStackTrace();}
    }

    private static String moveAgent(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.updateAgents(log);//updates the arena and visual agent

        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);//updates the arena and visual pokemon

        dw_graph_algorithms G = new DWGraph_Algo();//for the shortest algorithms
        G.init(gg);
        directed_weighted_graph g2 = G.copy();
        G.init(g2);

        HashMap<edge_data, CL_Agent> arrange = new HashMap<>();
        for(CL_Agent ag: log)
            goTo(ag, arrange, _ar.getPokemons(), G, gg,game);

        for (CL_Agent ag : log) {

            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();

            if (dest == -1) {
                }
            }
        for(edge_data a: arrange.keySet()){
            CL_Agent ag = arrange.get(a);
            if(ag.getNextNode()==-1) {
                int dest = newNextNode(G, ag.getSrcNode(), a);
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: " + ag.getID() + ", val: " + ag.getValue() + "   turned to node: " + dest);

            }
        }
        return lg;
        }

    private static int newNextNode (dw_graph_algorithms G,int src,edge_data edge){
        if(src==edge.getSrc()) return edge.getDest();
        return G.shortestPath(src, edge.getSrc()).get(1).getKey();
    }

    /**
         * this method gets src node and pokemon's list and returns the closest pokemon's edge
         * @param G
         * @param src
         * @param allPokemons
         * @return the edge of the closest pokemon
         */
    private static edge_data nextEdge (dw_graph_algorithms G, int src, List<CL_Pokemon > allPokemons){ //how to choose pokemon's
            PriorityQueue<CL_Pokemon> p = new PriorityQueue<>(allPokemons);

            double min = -2;
            edge_data edgePokemon = null;
            for (CL_Pokemon pok : p) {
                edge_data edge = pok.get_edge();
                double x = G.shortestPathDist(src, edge.getDest()) + edge.getWeight();
                if ((min == -2) || (x < min)) {
                    min = x;
                    edgePokemon = edge;
                }
            }
            return edgePokemon;
        }

    /**
     * this method gets graph algorithms, dest node and two src node and returns true if the first src node closer to the dest
     * @param G
     * @param src1
     * @param src2
     * @param dest
     * @return
     */
    private static boolean isCloser(dw_graph_algorithms G, int src1, int src2, int dest){
            double dist1 = G.shortestPathDist(src1,dest);
            double dist2 = G.shortestPathDist(src2,dest);
        return dist1!=-1 && dist1<=dist2;
        }

    /**
     * recursive method gets agent, HashMap of agents and pokemons,  and update the agents in the HashMap to hold unique edge.
     * @param ag
     * @param allAgents
     * @param pokemons
     * @param G
     * @param g
     */
    private static void goTo(CL_Agent ag, HashMap<edge_data,CL_Agent> allAgents, List<CL_Pokemon> pokemons, dw_graph_algorithms G,directed_weighted_graph g,game_service game){
        edge_data edge = nextEdge(G, ag.getSrcNode(), pokemons);
        if(edge == null) {
            //random function nextNode
            int dest = nextNode(G.getGraph(),ag.getSrcNode());
            game.chooseNextEdge(ag.getID(), dest);
            return;
        }
        if(!allAgents.containsKey(edge)){
            allAgents.put(edge, ag);
        }else{
            List<CL_Pokemon> newPokemon = new LinkedList<>();
            for(CL_Pokemon p: pokemons)
                if(!Arena.isOnEdge(p.getLocation(), edge,p.getType(), g))
                    newPokemon.add(p);
            if (isCloser(G,allAgents.get(edge).getSrcNode(),ag.getSrcNode(),edge.getSrc())){//oldAgent closer then the new agent
                goTo(ag, allAgents, newPokemon, G, g,game);
            }else{
                CL_Agent other = allAgents.get(edge);
                allAgents.put(edge, ag);
                goTo( other, allAgents, newPokemon, G, g,game);
            }
        }
    }

    private static int nextNode(directed_weighted_graph g, int src) {
        int ans = -1;
        Collection<edge_data> ee = g.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size();
        int r = (int)(Math.random()*s);
        int i=0;
        while(i<r) {itr.next();i++;}
        ans = itr.next().getDest();
        return ans;
    }


}
