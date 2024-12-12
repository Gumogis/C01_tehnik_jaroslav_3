package solid;

import transforms.Col;
import transforms.Point3D;

public class AxisZ extends Solid {
    public AxisZ() {
        color = 0x0000ff;
        vb.add(new Point3D(0,0,0));
        vb.add(new Point3D(0,0,5));

        addIndices(0,1);
    }
}
