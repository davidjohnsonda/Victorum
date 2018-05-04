package io.github.victorum;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import javax.swing.*;
import java.awt.*;

public class FullscreenLauncher {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> launch());
    }

    private static void launch(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        AppSettings appSettings = new AppSettings(true);
        appSettings.setWidth(screenSize.width);
        appSettings.setHeight(screenSize.height);

        Victorum victorum = new Victorum();
        victorum.setSettings(appSettings);
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
