package solid;

import transforms.Point3D;

public class Cube extends Solid {
    public Cube() {
        vb.add(new Point3D(0,0,0));
        vb.add(new Point3D(1,0,0));
        vb.add(new Point3D(1,0,1));
        vb.add(new Point3D(0,0,1));
        vb.add(new Point3D(0,1,0));
        vb.add(new Point3D(1,1,0));
        vb.add(new Point3D(0,1,1));
        vb.add(new Point3D(1,1,1));

        addIndices(
                0,1,
                1,2,
                2,3,
                3,0,
                0,4,
                4,5,
                5,1,
                5,7,
                7,2,
                7,6,
                6,3,
                6,4
        );
    }
}
