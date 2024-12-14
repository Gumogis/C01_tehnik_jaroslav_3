package solid;

import transforms.Point3D;

public class Curve extends Solid {
public Curve() {

    //psychotická křivka pohybuje se po všech osách
    int n = 1000;
    for(int i = 0; i <= n; i++){
        float x = i/(float)n;
        float y = (float) Math.sin(2*Math.PI*x);
        float z = (float) Math.cos(5*Math.PI*x)* (float) Math.sin(-3*Math.PI*x);

        vb.add(new Point3D(x,y,z));
        if(i != n){
            ib.add(i);
            ib.add(i+1);
        }
    }
}
}
