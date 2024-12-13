package Controller;

import Rasterize.LineRasterizer;
import Rasterize.LineRasterizerTrivial;
import render.Renderer;
import solid.*;
import View.Panel;
import transforms.*;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class Controller3D {
    private final Panel panel;
    private Renderer renderer;
    private Solid cube;
    private Solid cuboid;
    private LineRasterizer lineRasterizer;
    private Camera camera;
    private Mat4PerspRH proj;
    private AxisX axisX;
    private AxisY axisY;
    private AxisZ axisZ;
    private Curve curve;
    private ArrayList<Solid> solids;
    private ArrayList<Solid> axis;
    private int selectedSolid;
    private CubicCurve cubicCurve;
    private final double CameraSpeed = 0.1;
    private final double sensitivity = 0.01;

    public Controller3D(Panel panel) {

        this.panel = panel;

        initObjects();
        initListeners();
        redraw();
        panel.repaint();

    }

        // Inicializace objektů separátně pro větší přehlednost

    private void initObjects(){

        camera = new Camera()
                .withPosition(new Vec3D(0,0,-1))
                .withAzimuth(Math.toRadians(90))
                .withZenith(Math.toRadians(-25))
                .withFirstPerson(true);

        proj = new Mat4PerspRH(
                Math.toRadians(60),
                panel.getRaster().getHeight() / (float) panel.getRaster().getWidth(),
                0.01,
                200
            );

        lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        renderer = new Renderer(lineRasterizer, panel.getHeight(), panel.getWidth());

        axis = new ArrayList<>();
        solids = new ArrayList<>();
        curve = new Curve();
        cubicCurve = new CubicCurve();

        cube = new Cube();
        cuboid = new Cuboid();
        axisX = new AxisX();
        axisY = new AxisY();
        axisZ = new AxisZ();


        axis.add(axisX);
        axis.add(axisY);
        axis.add(axisZ);
        solids.add(cube);
        solids.add(cuboid);
        solids.add(cubicCurve);

        selectedSolid = 0;
    }

    private void initListeners(){

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if(key == KeyEvent.VK_W){
                    camera = camera.forward(CameraSpeed);
                }

                if(key == KeyEvent.VK_A){
                    camera = camera.left(CameraSpeed);
                }

                if(key == KeyEvent.VK_S){
                    camera = camera.backward(CameraSpeed);
                }

                if(key == KeyEvent.VK_D){
                    camera = camera.right(CameraSpeed);
                }

                if(key == KeyEvent.VK_H){
                    selectedSolid = (selectedSolid+1)%(solids.size());
                }

                if(key == KeyEvent.VK_P){
                    rotateObject(0.5,0,0.5,10,2);
                }

                if(key == KeyEvent.VK_L){
                    rotateObject(0.5,0,0.5,-10,2);
                }

                if(key == KeyEvent.VK_UP){
                    rotateObject(0,0.5,0.5,-10,1);
                }

                if(key == KeyEvent.VK_DOWN){
                    rotateObject(0,0.5,0.5,10,1);
                }

                if(key == KeyEvent.VK_LEFT){
                    rotateObject(0.5,0.5,0,-10,0);
                }

                if(key == KeyEvent.VK_RIGHT){
                    rotateObject(0.5,0.5,0, 10,0);
                }

                redraw();
            }
        });

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

    private void rotateObject(double x, double y, double z, int r, int axis){
        if(axis == 0)
            solids.get(selectedSolid).setModel(solids.get(selectedSolid).getModel().mul(new Mat4Transl(-x,-y,z).mul(new Mat4RotZ(Math.toRadians(r)).mul(new Mat4Transl(x,y,z)))));
        else if(axis == 1)
            solids.get(selectedSolid).setModel(solids.get(selectedSolid).getModel().mul(new Mat4Transl(x,-y,-z).mul(new Mat4RotX(Math.toRadians(r)).mul(new Mat4Transl(x,y,z)))));
        else if(axis == 2)
            solids.get(selectedSolid).setModel(solids.get(selectedSolid).getModel().mul(new Mat4Transl(-x,y,-z).mul(new Mat4RotY(Math.toRadians(r)).mul(new Mat4Transl(x,y,z)))));


    }

    // Funkce co nám kreslí už uložené objekty, prochází listy
    private void redraw(){
        panel.clear();

        renderer.setViewMatrix(camera.getViewMatrix());
        renderer.setProjectionMatrix(proj);

        renderer.renderSolids(solids);
        renderer.renderSolids(axis);

        panel.repaint();
    }
}
