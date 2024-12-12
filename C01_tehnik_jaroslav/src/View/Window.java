package View;

import javax.swing.*;

public class Window extends JFrame {
    private final Panel panel;
    public Window(int width, int height) {
        panel = new Panel(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Canvas");
        setResizable(false);
        setVisible(true);


        add(panel);
        pack();

        panel.setFocusable(true);
        panel.grabFocus();
    }

    public Panel getPanel() {
        return panel;
    }
}
