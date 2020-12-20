package api;

//we added setWeight function that change the weight of the edge
public class Iedge_data implements edge_data{

    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;

    public Iedge_data(int src, int dest, double weight) {
        this.src = src;
        this.dest=dest;
        this.weight=weight;
        this.info = "";
        this.tag = 0;
    }

    public Iedge_data(edge_data edge) {
        this.src = edge.getSrc();
        this.dest= edge.getDest();
        this.weight = edge.getWeight() ;
        this.info = edge.getInfo()+"";
        this.tag = edge.getTag();
    }
//    opposite copy constructor
    public Iedge_data(edge_data edge, int opposite) {
        this.src = edge.getDest();
        this.dest= edge.getSrc();
        this.weight = edge.getWeight() ;
        this.info = edge.getInfo()+"";
        this.tag = edge.getTag();
    }
    @Override
    public int getSrc() {
        return src;
    }

    @Override
    public int getDest() {
        return dest;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    public void setWeight(double w) {
        this.weight = w;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }
}


