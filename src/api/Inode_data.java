package api;

public class Inode_data implements node_data,Comparable {

    private int key;
    private int tag;
    private double weight;
    private String info;
    private geo_location location;
    private static int taz = 0;
//    HashMap<Integer, edge_data> edges;

    public Inode_data() {
        this.key = taz++;
        this.info = "";
//        this.location = new Igeo_location();
//        edges = new HashMap<Integer, edge_data>();
    }

    //copy constructor without neighbors
    public Inode_data(node_data n) {
        this.key = n.getKey();
        this.tag = n.getTag();
        this.weight = n.getWeight();
        this.info = n.getInfo();
//        this.edges = new HashMap<>();
//        this.location = new Igeo_location(n.getLocation());
    }

//    v-v-v-v
//    copy constructor for the load
    public Inode_data(Igeo_location g) {
        this.key = taz++;
        this.info = "";
        this.location = g;
    }
//    copy constructor for the load
    public Inode_data( int n, Igeo_location g){
        this.key = n;
        this.tag = 0;
        this.weight = 0;
        this.info = "";
        this.location = g;
    }
//    ^-^-^-^
    @Override
    public int getKey() {
        return key;
    }

    @Override
    public geo_location getLocation() {
        return location;
    }
//hope its correct
    @Override
    public void setLocation(geo_location p) {
        this.location = new Igeo_location(p);
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s+"";
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }
//for the PriorityQueue in Graph_Algo(shortestPath)
    @Override
    public int compareTo(Object o) {
        if(this.weight < ((Inode_data)o).weight ) return -1;
        else return 1;
    }

}