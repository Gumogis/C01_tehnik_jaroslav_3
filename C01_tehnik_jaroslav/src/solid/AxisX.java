package solid;

import transforms.Col;
import transforms.Point3D;

public class AxisX extends Solid {
    public AxisX() {
        color = 0x00ff00;
        vb.add(new Point3D(0,0,0));
        vb.add(new Point3D(10,0,0));

        addIndices(0,1);
    }
}
