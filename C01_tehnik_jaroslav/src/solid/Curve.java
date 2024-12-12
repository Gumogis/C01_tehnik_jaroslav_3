package solid;

import transforms.Point3D;

public class Curve extends Solid {
public Curve() {

    //upravit, nesmí být stejné
    int n = 100;
    for(int i = 0; i <= n; i++){
        float x = i/(float)n;
        float z = (float) Math.sin(5*Math.PI*x);

        vb.add(new Point3D(x,0,z));
        if(i != n){
            ib.add(i);
            ib.add(i+1);
        }
    }
}
}
