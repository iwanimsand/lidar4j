package ch.iwaims.lidar4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Lidar4jApplication {

    private static final String SERIAL_PORT = "/dev/ttyUSB1";

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(Lidar4jApplication.class, args);
    }

    @Bean
    CommandLineRunner init(LidarGraph lidarGraph) {
        return args -> {
            LidarFrame frame = new LidarFrame(lidarGraph);
            frame.setVisible(true);

            SerialReader reader = new SerialReader(SERIAL_PORT, new DataPacketFactory(), dataPacket -> {
                lidarGraph.addDataPacket(dataPacket);
                lidarGraph.repaint();
            });

            new Thread(reader).start();
        };
    }
}
