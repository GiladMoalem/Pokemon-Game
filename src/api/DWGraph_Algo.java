package api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

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
                informationE.put(obj);//add -> put
            }
        }

        for (node_data n : this.getGraph().getV()) {
            JSONObject obj = new JSONObject();
            String str = n.getLocation().x() + "," + n.getLocation().y() + "," + n.getLocation().z();
            obj.put("pos", str);
            obj.put("id", n.getKey());
            informationN.put(obj);//add -> put
        }
        all.put("Edges", informationE);
        all.put("Nodes", informationN);

        FileWriter f = null;
        try {

            // Constructs a FileWriter given a file name, using the platform's default charset
            f = new FileWriter("C:\\Users\\Gilad\\Desktop\\file.txt");//to change to file!!!!!
            f.write(all.toString());//all.toJSONString() -> all.toString()


        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {
                f.flush();
                f.close();
                System.out.println(all);//Checking for us
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;

    }



    @Override
    public boolean load(String file) {
    /*DWGraph_DS*/ g = new DWGraph_DS();//change 1: remove the "DWGraph_DS" and work on this.g

        try {
            FileReader f = new FileReader("C:\\Users\\Gilad\\Desktop\\file.txt");
            org.json.simple.parser.JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(f);
            JSONArray a = (JSONArray) jsonObject.get("Edges");
            JSONArray b = (JSONArray) jsonObject.get("Nodes");
//            int i = 0;
            for (int i=0; i < b.length(); i++){//change 2: while -> for, size() -> length()
//            while (i < b.size()) {//can use for loop/ to fix and work with STRINGS
                JSONObject n=(JSONObject)b.get(i);
                String s=(String)n.get("pos");
        int first=0,second = 0;//change 3: edit STRING s
        for(int ch=0; ch<s.length(); ch++){
            if(s.charAt(ch) == ','){
                if(first == 0)
                    first = ch;
                else second = ch;
            }
        }
        //change 4: casting to double
                double x = Double.parseDouble ( s.substring(0,first) );
                double y = Double.parseDouble ( s.substring(first+1, second) );
                double z = Double.parseDouble ( s.substring(second+1) );
                Igeo_location l= new Igeo_location(x,y,z);
//        node_data node= new Inode_data();

                int key = (int)n.get("id");//change 5: reading the key node
                node_data n2 = new Inode_data(key, l); //change 6 -> create node with specific location and key
                g.addNode(n2);
//                i++;
            }
//            int p=0;
//            while(p<a.size()){
            for (int p=0; p < a.length(); p++){//change 6: while -> for, size() -> length()
                JSONObject e=(JSONObject)a.get(p);
                g.connect((int) e.get("src"), (int) e.get("dest"), Double.parseDouble(e.get("w").toString()));//change 7: casting to int and no double
//                p++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
