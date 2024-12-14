package solid;

import transforms.Point3D;

public class AxisZ extends Solid {
    public AxisZ() {
        color = 0x0000ff;
        vb.add(new Point3D(0,0,2));
        vb.add(new Point3D(0,0,-2));

        addIndices(0,1);
    }
}
