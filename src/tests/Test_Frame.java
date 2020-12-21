package tests;

import Server.Game_Server_Ex2;
import api.*;
import gameClient.Arena;
import gameClient.CL_Pokemon;
import gameClient.MyFrame;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Test_Frame implements Runnable{

    directed_weighted_graph g;
    private Arena _ar;
    private MyFrame _win;

    public static void main(String[] args){
        Thread t = new Thread(new Test_Frame());
        t.start();
        /*MyFrame2 frm = new MyFrame2();
        frm.setVisible(true);
        List<Point> p = new LinkedList<Point>();
        p.add(new Point(212,441));
        p.add(new Point(15,40));
        p.add(new Point(838,203));
        frm.update(p);*/


    }
    @Override
    public void run(){
        game_service game = Game_Server_Ex2.getServer(3);
        init(game);

        game.startGame();

    }
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

        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");//add (count=)rs agents
//            System.out.println(info);
//            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<CL_Pokemon> cl_fs = (ArrayList<CL_Pokemon>) _ar.getPokemons();
            for(int a = 0;a<cl_fs.size();a++) { Arena.updateEdge(cl_fs.get(a),gg);}//update the pokemon's edge

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
}
