package solid;

import transforms.Point3D;

public class Cuboid extends Solid {
    public Cuboid(){
        vb.add(new Point3D(-1,-1,-1));
        vb.add(new Point3D(0,-1,-1));
        vb.add(new Point3D(0,-1,0));
        vb.add(new Point3D(-1,-1,0));
        vb.add(new Point3D(-0.5,-0.5,-0.5));
        color = 0x0078ff;
        addIndices(
                0,1,
                1,2,
                2,3,
                3,0,
                3,4,
                4,2,
                4,1,
                4,0
        );
    }
}
