package Controller;

import Rasterize.LineRasterizer;
import Rasterize.LineRasterizerTrivial;
import render.Renderer;
import solid.*;
import View.Panel;
import transforms.*;


import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class Controller3D {

    //rasterizer čar a panely
    private final Panel panel;
    private Renderer renderer;
    private LineRasterizer lineRasterizer;

    //perspektivní modely
    private Camera camera;
    private Mat4PerspRH proj;
    private Mat4OrthoRH orthProj;

    // solidové objekty
    private Solid cube;
    private Solid cuboid;
    private AxisX axisX;
    private AxisY axisY;
    private AxisZ axisZ;
    private Curve curve;
    private CubicBezierCurve cubicBezierCurve;
    private CubicFergusonCurve cubicFergusonCurve;
    private CubicCoonCurve cubicCoonCurve;

    //listy pro solidy se kterýma budeme pracovat a statické osy xyz
    private ArrayList<Solid> solids;
    private ArrayList<Solid> axis;

    //proměnné na změnění rychlosti, barev, booleany
    private int selectedSolid = 0;
    private Mat4ViewRH viewProj;
    private final double CameraSpeed = 0.1;
    private final double sensitivity = 0.01;
    private int selectedColor = 0xFF69B4;
    private int unselectedColor = 0xFF0000;
    private boolean rotate = false;
    private boolean move = false;
    private boolean zoom = false;
    private boolean isAnimating = false;
    private int cameraSwitch = 0;
    private Timer animationTimer;

    public Controller3D(Panel panel) {

        this.panel = panel;

        initObjects();
        initListeners();
        redraw();
        panel.repaint();

    }

        // Inicializace objektů separátně pro větší přehlednost

    private void initObjects(){

        //objekt pro kameru
        camera = new Camera()
                .withPosition(new Vec3D(0,-6,5))
                .withAzimuth(Math.toRadians(90))
                .withZenith(Math.toRadians(-25))
                .withFirstPerson(true);

        //Matice pro pohledové mody
        proj = new Mat4PerspRH(
                Math.toRadians(60),
                panel.getRaster().getHeight() / (float) panel.getRaster().getWidth(),
                0.01,
                200
            );

        orthProj = new Mat4OrthoRH(
                10,
                10,
                0.01,
                200
        );

        viewProj = new Mat4ViewRH(
                new Vec3D(0,1,0),
                new Vec3D(0,1,0),
                new Vec3D(0,1,0)
        );

        lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        renderer = new Renderer(lineRasterizer, panel.getHeight(), panel.getWidth());

        // vytváření listů
        axis = new ArrayList<>();
        solids = new ArrayList<>();

        // vytváření objektů
        curve = new Curve();
        cubicBezierCurve = new CubicBezierCurve();
        cubicFergusonCurve = new CubicFergusonCurve();
        cubicCoonCurve = new CubicCoonCurve();
        cube = new Cube();
        cuboid = new Pyramid();
        axisX = new AxisX();
        axisY = new AxisY();
        axisZ = new AxisZ();

        //přidáváme vytvořená tělesa do listu ze kterého potom budeme vybírat kterým budeme manipulovat
        axis.add(axisX);
        axis.add(axisY);
        axis.add(axisZ);
        solids.add(cube);
        solids.add(cuboid);
        solids.add(cubicBezierCurve);
        solids.add(cubicFergusonCurve);
        solids.add(cubicCoonCurve);
        solids.add(curve);

        solids.get(selectedSolid).setColor(selectedColor);
    }

    private void initListeners(){

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if(key == KeyEvent.VK_W)
                    camera = camera.forward(CameraSpeed);
                if(key == KeyEvent.VK_A)
                    camera = camera.left(CameraSpeed);
                if(key == KeyEvent.VK_S)
                    camera = camera.backward(CameraSpeed);
                if(key == KeyEvent.VK_D)
                    camera = camera.right(CameraSpeed);

                if(key == KeyEvent.VK_H) {
                    solids.get(selectedSolid).setColor(unselectedColor);
                    selectedSolid = (selectedSolid + 1) % (solids.size());
                    solids.get(selectedSolid).setColor(selectedColor);
                }

                if(key == KeyEvent.VK_P) {
                    if(rotate)
                        rotateObject(10, 2);
                    if(move)
                        moveObject(1,2);
                    if(zoom)
                        zoomObject(2,2);
                }
                if(key == KeyEvent.VK_L){
                    if(rotate)
                        rotateObject(-10,2);
                    if(move)
                        moveObject(-1,2);
                    if(zoom)
                        zoomObject(0.5,2);
                }
                if(key == KeyEvent.VK_UP) {
                    if(rotate)
                        rotateObject(-10, 1);
                    if(move)
                        moveObject(1,1);
                    if(zoom)
                        zoomObject(2,1);
                }
                if(key == KeyEvent.VK_DOWN) {
                    if(rotate)
                        rotateObject(10, 1);
                    if(move)
                        moveObject(-1,1);
                    if(zoom)
                        zoomObject(0.5,1);
                }
                if(key == KeyEvent.VK_LEFT) {
                    if(rotate)
                        rotateObject(-10, 0);
                    if(move)
                        moveObject(-1,0);
                    if(zoom)
                        zoomObject(0.5,0);
                }
                if(key == KeyEvent.VK_RIGHT) {
                    if(rotate)
                        rotateObject(10, 0);
                    if(move)
                        moveObject(1,0);
                    if(zoom)
                        zoomObject(2,0);
                }

                if(key == KeyEvent.VK_R) {
                    rotate = !rotate;
                    if(rotate)
                        System.out.println("Rotating selected object");
                    else
                        System.out.println("Stopping rotation of a selected object");
                }

                if(key == KeyEvent.VK_T){
                    move = !move;
                    if(move)
                        System.out.println("Moving selected object");
                    else
                        System.out.println("Stopping the movement");
                }

                if(key == KeyEvent.VK_Z){
                    zoom = !zoom;
                    if(zoom)
                        System.out.println("Scaling/Zooming the selected object");
                    else
                        System.out.println("Stopping the zooming/scaling");
                }

                if(key == KeyEvent.VK_C){
                    camera = new Camera()
                            .withPosition(new Vec3D(0,0,0))
                            .withAzimuth(Math.toRadians(90))
                            .withZenith(Math.toRadians(-25))
                            .withFirstPerson(true);

                    cameraSwitch = (cameraSwitch+1)%3;
                    System.out.println("switching perspective");
                }

                if(key == KeyEvent.VK_J){
                    if (isAnimating)
                        stopAnimation();
                    else
                        startAnimation();
                }

                redraw();
            }
        });

        //listener pro myš, když se stiskne tlačítko myši (pravé i levé) tak začne pohybovat kamerou
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            private int lastX = -1, lastY = -1;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (lastX != -1 && lastY != -1) {
                    int deltaX = e.getX() - lastX;
                    int deltaY = e.getY() - lastY;
                    camera = camera.addAzimuth(-deltaX * sensitivity)
                            .addZenith(-deltaY * sensitivity);
                }
                lastX = e.getX();
                lastY = e.getY();

                redraw();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }
        });
    }

    //funkce pro pohyb, rotaci a zoom vybraných objektů
    private void rotateObject(double r, int axis){
        Point3D xyz = solids.get(selectedSolid).getCenter();
        xyz = xyz.mul(solids.get(selectedSolid).getModel());
        double centerX = xyz.getX();
        double centerY = xyz.getY();
        double centerZ = xyz.getZ();

        if(axis == 0)
            solids.get(selectedSolid).setModel(solids.get(selectedSolid).getModel().mul(new Mat4Transl(-centerX,-centerY,0).mul(new Mat4RotZ(Math.toRadians(r)).mul(new Mat4Transl(centerX, centerY, 0)))));
        else if(axis == 1)
            solids.get(selectedSolid).setModel(solids.get(selectedSolid).getModel().mul(new Mat4Transl(0,-centerY, -centerZ).mul(new Mat4RotX(Math.toRadians(r)).mul(new Mat4Transl(0, centerY, centerZ)))));
        else if(axis == 2)
            solids.get(selectedSolid).setModel(solids.get(selectedSolid).getModel().mul(new Mat4Transl(-centerX,0,-centerZ).mul(new Mat4RotY(Math.toRadians(r)).mul(new Mat4Transl(centerX,0, centerZ)))));
    }

    private void moveObject(double r, int axis){

        if(axis == 0)
            solids.get(selectedSolid).setModel(solids.get(selectedSolid).getModel().mul(new Mat4Transl(r,0,0)));
        else if(axis == 1)
            solids.get(selectedSolid).setModel(solids.get(selectedSolid).getModel().mul(new Mat4Transl(0,0,r)));
        else if(axis == 2)
            solids.get(selectedSolid).setModel(solids.get(selectedSolid).getModel().mul(new Mat4Transl(0,r,0)));
    }

    private void zoomObject(double r, int axis){
        if(axis == 0)
            solids.get(selectedSolid).setModel(solids.get(selectedSolid).getModel().mul(new Mat4Scale(r,1,1)));
        else if(axis == 1)
            solids.get(selectedSolid).setModel(solids.get(selectedSolid).getModel().mul(new Mat4Scale(1,1,r)));
        else if(axis == 2)
            solids.get(selectedSolid).setModel(solids.get(selectedSolid).getModel().mul(new Mat4Scale(1,r,1)));

    }

    //Funkce které zapnou a vypnou animace, animace točí tělesem okolo všech os
    private void startAnimation() {
        if (isAnimating) return;

        isAnimating = true;

        animationTimer = new Timer(32, e -> {
                rotateObject(5,0);
                rotateObject(-5,1);
                rotateObject(5,2);
                redraw();
        });

        animationTimer.start();
    }

    private void stopAnimation() {
        if (!isAnimating) return;

        isAnimating = false;
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    // Funkce co nám kreslí už uložené objekty, prochází listy
    private void redraw(){
        panel.clear();

        renderer.setViewMatrix(camera.getViewMatrix());
        if(cameraSwitch == 0){
            renderer.setProjectionMatrix(proj);
        }else if(cameraSwitch == 1){
            renderer.setProjectionMatrix(orthProj);
        } else if(cameraSwitch == 2) {
            renderer.setProjectionMatrix(viewProj);
        }

        renderer.renderSolids(axis);
        renderer.renderSolids(solids);

        panel.repaint();
    }
}
