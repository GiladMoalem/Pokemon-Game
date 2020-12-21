package api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.*;
import com.google.gson.JsonArray;

import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph g;

    public DWGraph_Algo(){
        g = new DWGraph_DS();
    }
    @Override
    public void init(directed_weighted_graph g) {
        this.g = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return g;
    }
// to check the copy function
    @Override
    public directed_weighted_graph copy() {
        return new DWGraph_DS(this.g);
    }
// checks if can get from specific node to all the other nodes and if can to get specific node from all the other nodes.
//    check node to all neighbors and opposite = O(n+n^2)
//    check node, upside down the all graph and check same node again = O(2n+v) ...v-vertex
    @Override
    public boolean isConnected() {
        if(g==null || g.nodeSize()<2) return true;
        HashMap<Integer, node_data> spare = new HashMap<>();
        LinkedList<node_data> waiting = new LinkedList<>();

        node_data nd, src= g.getV().iterator().next();
        waiting.add(src);
        spare.put(src.getKey(),src);

        while(!waiting.isEmpty() && spare.size() < g.nodeSize()) {
            nd = waiting.poll();
            for (edge_data edge : g.getE(nd.getKey())) {
                if (!spare.containsKey(edge.getDest())) {//first time pass trough this dest node
                    spare.put(edge.getDest(), g.getNode(edge.getDest()));
                    waiting.add(g.getNode(edge.getDest()));
                }
            }
        }
        if(spare.size() < g.nodeSize()) return false; //The connection |src ->all nodes| doesn't exists
//        spare contain all graph nodes

        directed_weighted_graph g2 = new DWGraph_DS(g,0);//help function - opposites graph. n->d \ d->n
        waiting = new LinkedList<>();

        src = g2.getNode(src.getKey());//important line. for to work on g2 and not on g
        waiting.add(src);
        spare.remove(src.getKey());

        while(!waiting.isEmpty() && !spare.isEmpty()){
            nd = waiting.poll();
            for (edge_data edge: g2.getE(nd.getKey())){
                if(spare.containsKey(edge.getDest())){
                    spare.remove(edge.getDest());
                    waiting.add(g2.getNode(edge.getDest()));
                }
            }
        }
        return spare.isEmpty();
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        LinkedList<node_data> result = (LinkedList<node_data>) shortestPath(src, dest);
        if(result == null) return -1;
        if(result.size() < 2) return 0;//unnecessary line
        double path = 0;
        node_data node1 = result.pollFirst();
        edge_data edge;

        while(!result.isEmpty()){
            edge = g.getEdge(node1.getKey(), result.getFirst().getKey());
            path += edge.getWeight();
            node1 = result.pollFirst();
        }
        return path;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if(g == null || g.getNode(src)==null || g.getNode(dest)==null) return null;
        if(src==dest){
            LinkedList<node_data> result = new LinkedList<>();
            result.add(g.getNode(src));
            return result;
        }

        HashMap<Integer,Integer> father = new HashMap<>(); // [key|key's father]
        PriorityQueue<node_data> waitingList = new PriorityQueue<node_data>(); //waiting list node needed checking -> insert O(log(n))
//        LinkedList<node_data> waitingList = new LinkedList<node_data>(); //waiting list for node needed checking

//put src and starting
        father.put(src,-1);
        g.getNode(src).setWeight(0);//weight - the minimum distance from src
        waitingList.add(g.getNode(src));

        while(!waitingList.isEmpty()){
            node_data nd = waitingList.poll();

            if (!father.containsKey(dest) || g.getNode(dest).getWeight() > nd.getWeight()) {//breaking conditions
                for (edge_data edge : g.getE(nd.getKey())) {
//                if (!father.containsKey(dest) || g.getNode(dest).getWeight() > nd.getWeight() + edge.getWeight()) {
                    if (!father.containsKey(edge.getDest())) {//first time pass trough this node
                        g.getNode(edge.getDest()).setWeight(nd.getWeight() + edge.getWeight());//write this before that adding the waiting list!
                        father.put(edge.getDest(), edge.getSrc());
                        waitingList.add(g.getNode(edge.getDest()));
                    } else if (g.getNode(edge.getDest()).getWeight() > nd.getWeight() + edge.getWeight()) { //if node newWeight<node oldWeight
                        g.getNode(edge.getDest()).setWeight(nd.getWeight() + edge.getWeight());
                        father.put(edge.getDest(), edge.getSrc());
                    }
                }
            }
            else break;
        }
        for(Integer key: father.keySet()){
            g.getNode(key).setWeight(0);
        }
        if(father.containsKey(dest)){
            LinkedList<node_data> result = new LinkedList<>();
            while(dest != -1){
                result.addFirst(g.getNode(dest));
                dest = father.get(dest);
            }
            return result;
        }//no path from src to dest
        return null;
    }

    @Override
    public boolean save(String file) throws JSONException {
        JSONArray informationE = new JSONArray();
        JSONArray informationN = new JSONArray();
        JSONObject all = new JSONObject();
        Iterator<node_data> nodes = g.getV().iterator();
        while (nodes.hasNext()) {
            for (edge_data n : this.getGraph().getE(nodes.next().getKey())) {
                JSONObject obj = new JSONObject();
                obj.put("src", n.getSrc());
                obj.put("w", n.getWeight());
                obj.put("dest", n.getDest());
                informationE.put(obj);
            }
        }

        for (node_data n : this.getGraph().getV()) {
            JSONObject obj = new JSONObject();
            String str = n.getLocation().x() + "," + n.getLocation().y() + "," + n.getLocation().z();
            obj.put("pos", str);
            obj.put("id", n.getKey());
            informationN.put(obj);
        }
        all.put("Edges", informationE);
        all.put("Nodes", informationN);

        FileWriter f = null;
        try {

            f = new FileWriter("out\\g0.txt");
            f.write(all.toString());


        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {
                f.flush();
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;

    }



    @Override
    public boolean load(String file) {
    g = new DWGraph_DS();
    Gson gson = new Gson();

        try {
            FileReader f = new FileReader(file);
            JsonObject graph = gson.fromJson(f, JsonObject.class);
            f.close();
            JsonArray a = graph.get("Edges").getAsJsonArray();
            JsonArray b = graph.get("Nodes").getAsJsonArray();
            for (int i=0; i < b.size(); i++){
                JsonObject n=b.get(i).getAsJsonObject();
                String s=n.get("pos").getAsString();
                int first=0,second = 0;
                for(int ch=0; ch<s.length(); ch++){
                    if(s.charAt(ch) == ','){
                        if(first == 0)
                            first = ch;
                        else second = ch;
                    }
                }

                double x = Double.parseDouble ( s.substring(0,first) );
                double y = Double.parseDouble ( s.substring(first+1, second) );
                double z = Double.parseDouble ( s.substring(second+1) );
                Igeo_location l= new Igeo_location(x,y,z);
                int key = n.get("id").getAsInt();
                node_data n2 = new Inode_data(key, l);
                g.addNode(n2);
            }
            for (int p=0; p < a.size(); p++){
                JsonObject e=a.get(p).getAsJsonObject();
                g.connect(e.get("src").getAsInt(), e.get("dest").getAsInt(), e.get("w").getAsDouble());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
