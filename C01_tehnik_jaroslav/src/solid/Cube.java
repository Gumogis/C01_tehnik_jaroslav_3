package solid;

import transforms.Point3D;

public class Cube extends Solid {
    public Cube() {
        vb.add(new Point3D(1,1,1));
        vb.add(new Point3D(2,1,1));
        vb.add(new Point3D(2,1,2));
        vb.add(new Point3D(1,1,2));
        vb.add(new Point3D(1,2,1));
        vb.add(new Point3D(2,2,1));
        vb.add(new Point3D(1,2,2));
        vb.add(new Point3D(2,2,2));

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
