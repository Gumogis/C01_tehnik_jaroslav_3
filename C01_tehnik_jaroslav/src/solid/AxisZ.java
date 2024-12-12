package solid;

import transforms.Col;
import transforms.Point3D;

public class AxisZ extends Solid {
    public AxisZ() {
        color = 0xff0000;
        vb.add(new Point3D(0,0,0));
        vb.add(new Point3D(0,0,10));

        addIndices(0,1);
    }
}
