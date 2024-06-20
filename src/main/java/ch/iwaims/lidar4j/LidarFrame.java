package ch.iwaims.lidar4j;

import javax.swing.*;

public class LidarFrame extends JFrame {
    public LidarFrame(LidarGraph lidarGraph) {
        setTitle("Lidar Graph");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(lidarGraph);
        setSize(800, 600);
    }
}

