package io.github.victorum;

import com.jme3.system.JmeCanvasContext;

import javax.swing.*;
import java.awt.*;

public class FullscreenLauncher {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> launch());
    }

    private static void launch(){
        Victorum victorum = new Victorum();
        victorum.createCanvas();

        JmeCanvasContext jmeCanvasContext = (JmeCanvasContext) victorum.getContext();
        jmeCanvasContext.setSystemListener(victorum);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(jmeCanvasContext.getCanvas(), BorderLayout.CENTER);

        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
    }

}
