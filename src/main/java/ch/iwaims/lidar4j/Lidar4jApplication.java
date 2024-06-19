package ch.iwaims.lidar4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Lidar4jApplication {

    private static final String SERIAL_PORT = "/dev/ttyUSB1";

    public static void main(String[] args) {
        SpringApplication.run(Lidar4jApplication.class, args);
    }

    private static void run(String... args) {
        new Thread(new SerialReader(SERIAL_PORT, new DataPacketFactory())).start();
    }

    @Bean
    CommandLineRunner init() {
        return Lidar4jApplication::run;
    }
}
