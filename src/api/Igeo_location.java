package api;

public class Igeo_location implements geo_location{
    private double x;
    private double y;
    private double z;
    public Igeo_location(double x,double y,double z ){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Igeo_location(geo_location loc){
        this.x = loc.x();
        this.y = loc.y();
        this.z = loc.z();
    }
    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

//   (  (x2-x1)^2 + (y2-y1)^2 + (z2-z1)^2  )^0.5
    @Override
    public double distance(geo_location g) {
        double disX = g.x() - this.x;
        double disY = g.y() - this.y;
        double disZ = g.z() - this.z;

        return Math.sqrt(Math.pow(disX,2) + Math.pow(disY,2) + Math.pow(disZ,2));
    }
}
