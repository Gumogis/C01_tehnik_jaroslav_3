package solid;

import transforms.Col;
import transforms.Point3D;

public class AxisY extends Solid {
    public AxisY() {
        color = 0x00ff00;
        vb.add(new Point3D(0,2,0));
        vb.add(new Point3D(0,-2,0));

        addIndices(0,1);

    }
}
