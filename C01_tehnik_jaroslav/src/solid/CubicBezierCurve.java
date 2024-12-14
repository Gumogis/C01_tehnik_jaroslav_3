package solid;

import transforms.Cubic;
import transforms.Point3D;

public class CubicBezierCurve extends Solid{

    public CubicBezierCurve() {
        // také upravit
        
        Point3D a = new Point3D(-2,-2,0);
        Point3D b = new Point3D(-3,-1,0.25);
        Point3D c = new Point3D(-4,-2,0.5);
        Point3D d = new Point3D(-2,-2,0.75);

        Cubic cubic = new Cubic(Cubic.BEZIER,a,b,c,d);

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
