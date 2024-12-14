package solid;

import transforms.Cubic;
import transforms.Point3D;

public class CubicFergusonCurve extends Solid {

    public CubicFergusonCurve() {
        // také upravit

        Point3D a = new Point3D(-1,1,0);
        Point3D b = new Point3D(2,2,0);
        Point3D c = new Point3D(2,1,0);
        Point3D d = new Point3D(1,2,0);

        Cubic cubic = new Cubic(Cubic.FERGUSON,a,b,c,d);

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
