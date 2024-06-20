package ch.iwaims.lidar4j;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class LidarGraph extends JPanel {

    private final ConcurrentLinkedQueue<DataPacket> dataQueue = new ConcurrentLinkedQueue<>();
    private final int maxDataSize = 200;

    public LidarGraph() {
        Timer timer = new Timer(250, e -> repaint());
        timer.start();
    }

    public void addDataPacket(DataPacket packet) {
        if (dataQueue.size() >= maxDataSize) {
            dataQueue.poll();
        }
        dataQueue.add(packet);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        List<MeasurementDataPoint> measurementDataPoints = dataQueue.stream()
                .flatMap(p -> p.getMeasurementDataPoints().stream())
                .toList();

        for (MeasurementDataPoint measurementDataPoint : measurementDataPoints) {

            float angle = measurementDataPoint.getAngle();
            double angleRad = Math.toRadians(angle);
            int distance = measurementDataPoint.getDistance();

            int x = (int) (centerX + distance * Math.cos(angleRad) / 4);
            int y = (int) (centerY - distance * Math.sin(angleRad) / 4);

            g2d.setColor(new Color(measurementDataPoint.getIntensity(), 0, 0));
            g2d.fillOval(x, y, 2, 2);

            if (distance < 1000) {
                int barWidth = width / 360;
                int barHeight = distance / 2;
                int barX = barWidth * (int) angle;

                g2d.setColor(Color.lightGray);
                g2d.fillRect(barX, 0, barWidth, barHeight);
            }

        }

    }

}
