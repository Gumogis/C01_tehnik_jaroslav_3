package solid;

import transforms.Cubic;
import transforms.Point3D;

public class CubicCoonCurve extends Solid {

    public CubicCoonCurve(){
        // také upravit

        Point3D a = new Point3D(-0.5,0,0);
        Point3D b = new Point3D(-0.25,0.5,0);
        Point3D c = new Point3D(0.25,0.5,0);
        Point3D d = new Point3D(0.5,0,0);

        Cubic cubic = new Cubic(Cubic.COONS,a,b,c,d);

        int n = 100;
        for(int i = 0; i <= n; i++){
            float t = (i/(float)n);
            Point3D p = cubic.compute(t);

            vb.add(p);
            if(i != n){
                ib.add(i);
                ib.add(i+1);
            }
        }
    }
}
