package solid;

import transforms.Col;
import transforms.Point3D;

public class AxisX extends Solid {
    public AxisX() {
        color = 0xff0000;
        vb.add(new Point3D(-2,0,0));
        vb.add(new Point3D(2,0,0));

        addIndices(0,1);
    }
}
