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

public class Controller3D {
    private final Panel panel;
    private final Renderer renderer;
    private Solid arrow;
    private LineRasterizer lineRasterizer;
    private Camera camera;
    private Mat4PerspRH proj;
    private AxisX axisX;
    private AxisY axisY;
    private AxisZ axisZ;
    private Curve curve;
    private CubicCurve cubicCurve;
    private final double CameraSpeed = 0.1;
    private final double sensitivity = 0.01;
    private boolean firstPerson = false;

    public Controller3D(Panel panel) {
        Vec2D tr = new Vec2D(-1.0,-1.0);
        tr = new Vec2D(tr.mul(new Vec2D(1,-1)).add(new Vec2D(1,1)).mul(new Vec2D(499,199)));
        System.out.println(tr);
        curve = new Curve();
        cubicCurve = new CubicCurve();

        this.panel = panel;
        lineRasterizer = new LineRasterizerTrivial(panel.getRaster());

        camera = new Camera()
                .withPosition(new Vec3D(0,1,-1))
                .withAzimuth(Math.toRadians(90))
                .withZenith(Math.toRadians(-35))
                .withFirstPerson(true);

        proj = new Mat4PerspRH(
                Math.toRadians(60),
                panel.getRaster().getHeight() / (float) panel.getRaster().getWidth(),
                0.01,
                200
                );

        // Init solids
        arrow = new Cube();

        renderer = new Renderer(lineRasterizer, panel.getHeight(), panel.getWidth());
        initObjects();
        initListeners();
        redraw();
        panel.repaint();

        }

        // Inicializace objektů separátně pro větší přehlednost
        private void initObjects(){

            axisX = new AxisX();
            axisY = new AxisY();
            axisZ = new AxisZ();

        }

        private void initListeners(){

            panel.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int key = e.getKeyCode();

                    if(key == KeyEvent.VK_W){
                        camera = camera.backward(CameraSpeed);
                    }

                    if(key == KeyEvent.VK_A){
                        camera = camera.right(CameraSpeed);
                    }

                    if(key == KeyEvent.VK_S){
                        camera = camera.forward(CameraSpeed);
                    }

                    if(key == KeyEvent.VK_D){
                        camera = camera.left(CameraSpeed);
                    }

                    if(key == KeyEvent.VK_KP_UP){
                    }

                    if(key == KeyEvent.VK_DOWN){
                    }

                    if(key == KeyEvent.VK_LEFT){
                        arrow.setModel(arrow.getModel().mul(new Mat4Transl(-0.5,0,0).mul(new Mat4RotZ(Math.toRadians(-10)).mul(new Mat4Transl(0.5,0,0)))));
                    }

                    if(key == KeyEvent.VK_RIGHT){
                        arrow.setModel(arrow.getModel().mul(new Mat4Transl(-0.5,0,0).mul(new Mat4RotZ(Math.toRadians(10)).mul(new Mat4Transl(0.5,0,0)))));
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

    // Funkce co nám kreslí už uložené objekty, prochází listy
    private void redraw(){
        panel.clear();

        renderer.setViewMatrix(camera.getViewMatrix());
        renderer.setProjectionMatrix(proj);

        renderer.renderSolid(arrow);

        renderer.renderSolid(axisX);
        renderer.renderSolid(axisY);
        renderer.renderSolid(axisZ);
        renderer.renderSolid(cubicCurve);

        panel.repaint();
    }
}
