package solid;

import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solid {
    protected List<Point3D> vb = new ArrayList<>();
    protected List<Integer> ib = new ArrayList<>();
    protected Mat4 model = new Mat4Identity();
    protected int color = 0xFF0000;

    protected  void addIndices(Integer... indices){
        ib.addAll(Arrays.asList(indices));
    }

    public List<Point3D> getVb() {
        return vb;
    }
    public List<Integer> getIb() {
        return ib;
    }

    public Mat4 getModel() {
        return model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    //zprůměrujeme všechny body ve vertex bufferu a vyjde nám středový bod podle kterého můžeme transformovat
    public Point3D getCenter(){
        int vbSize = vb.size();
        double x = 0;
        double y = 0;
        double z = 0;
        for(Point3D point : vb){
            x+=point.getX();
            y+=point.getY();
            z+=point.getZ();
        }

        return new Point3D(x/vbSize,y/vbSize,z/vbSize);
    }
}
