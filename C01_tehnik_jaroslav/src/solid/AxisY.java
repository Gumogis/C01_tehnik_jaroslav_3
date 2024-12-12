package solid;

import transforms.Col;
import transforms.Point3D;

public class AxisY extends Solid {
    public AxisY() {
        color = 0x00ff00;
        vb.add(new Point3D(0,0,0));
        vb.add(new Point3D(0,5,0));

        addIndices(0,1);

    }
}
