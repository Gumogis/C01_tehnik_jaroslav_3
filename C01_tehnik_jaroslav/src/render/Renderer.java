package render;

import Models.Line;
import Rasterize.LineRasterizer;
import solid.Solid;
import transforms.Camera;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private int width;
    private int height;
    private LineRasterizer lineRasterizer;
    private Mat4 view;
    private Mat4 projection;

    public Renderer(LineRasterizer lineRasterizer, int height, int width) {
        this.lineRasterizer = lineRasterizer;
        this.width = width;
        this.height = height;
    }

    public void renderSolid(Solid solid){
        // for cyklus co pronásobí všechny body modelovací maticí, povinné!
        ArrayList<Point3D> transdpoints = new ArrayList<>();
        //mvp matice
        Mat4 mvp = new Mat4(solid.getModel().mul(view).mul(projection));


        for(int i = 0; i < solid.getIb().size(); i++){
            int indexA = solid.getIb().get(i);

            Point3D pointA = new Point3D(solid.getVb().get(indexA));

            //modelovací matice MVP, model view projection
            pointA = pointA.mul(mvp);

            transdpoints.add(pointA);
        }

        for( int i = 0; i < transdpoints.size(); i+=2){

            Point3D pointA = new Point3D(transdpoints.get(i));
            Point3D pointB = new Point3D(transdpoints.get(i+1));

            if (isInView(pointA, pointB)) {
            if(pointA.getW() == 0 || pointB.getW() == 0){

            }
            else {
                pointA = pointA.mul(1/ pointA.getW());
                pointB = pointB.mul(1/ pointB.getW());
            }

            // transformace do okna obrazovky
            Vec3D pointAInScreen = transformToScreen(new Vec3D(pointA));
            Vec3D pointBInScreen = transformToScreen(new Vec3D(pointB));

            Line line = new Line(
                    (int) Math.round(pointAInScreen.getX()),
                    (int) Math.round(pointAInScreen.getY()),
                    (int) Math.round(pointBInScreen.getX()),
                    (int) Math.round(pointBInScreen.getY())
            );

            lineRasterizer.drawLine(line, solid.getColor());
            }
        }
    }

    private boolean isInView(Point3D pointA, Point3D pointB) {
        boolean boolA =
                pointA.getX() > -pointA.getW()
                        && pointA.getX() < pointA.getW()
                        && pointA.getY() > -pointA.getW()
                        && pointA.getY() < pointA.getW()
                        && pointA.getZ() > -pointA.getW()
                        && pointA.getZ() < pointA.getW();

        boolean boolB =
                pointB.getX() > -pointB.getW()
                        && pointB.getX() < pointB.getW()
                        && pointB.getY() > -pointB.getW()
                        && pointB.getY() < pointB.getW()
                        && pointB.getZ() > -pointB.getW()
                        && pointB.getZ() < pointB.getW();

        return boolA && boolB;
    }

    private Vec3D transformToScreen(Vec3D point){
        return point = point.mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1,1,0))
                .mul(new Vec3D((width-1)/2., (height-1)/2.,1 ));
    }

    public void renderSolids(List<Solid> solids){
        for(Solid solid : solids){
            renderSolid(solid);
        }
    }

    public void setViewMatrix(Mat4 view){
        this.view = view;
    }

    public void setProjectionMatrix(Mat4 projection){
        this.projection = projection;
    }

    public void setLineRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }
}
