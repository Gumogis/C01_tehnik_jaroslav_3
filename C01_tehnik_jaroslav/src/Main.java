import Controller.Controller3D;
import View.Window;

public class Main {
    public static void main(String[] args) {

        Window window = new Window(1000,800);
        new Controller3D(window.getPanel());
    }
}