package ch.iwaims.lidar4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SerialReader implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(SerialReader.class);
    private final String serialPort;
    private final DataPacketFactory dataPacketFactory;
    private final Consumer<DataPacket> consumer;

    public SerialReader(final String serialPort, DataPacketFactory dataPacketFactory, Consumer<DataPacket> consumer) {
        this.serialPort = serialPort;
        this.dataPacketFactory = dataPacketFactory;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        configureSerialPort(serialPort);

        File file = new File(serialPort);

        byte[] buffer = new byte[47];
        int i = 0;
        int previousByteValue = -1;

        try (final FileInputStream fis = new FileInputStream(file)) {
            while (true) {
                int b = fis.read();
                if (b == -1) {
                    logger.debug(String.format("%02X - End of file reached.", b & 0xFF));
                } else {
                    buffer[i] = (byte) b;

                    if (previousByteValue == 0x54 && buffer[i] == 0x2C) {
                        // start new package
                        buffer[0] = 0x54;
                        buffer[1] = 0x2C;
                        i = 1; // reset actual index
                    }

                    previousByteValue = buffer[i];
                    i++;

                    if (i == buffer.length) {
                        String hexString = toHexString(buffer);

                        if (buffer[0] == 0x54 && buffer[1] == 0x2C) {
                            // full package received
                            DataPacket dataPacket = dataPacketFactory.fromBytes(buffer);
                            if (logger.isTraceEnabled()) {
                                logger.trace(hexString);
                                logger.trace(dataPacket.toString());
                            }
                            consumer.accept(dataPacket);
                        } else {
                            // something is wrong...
                            logger.warn("Corrupt package: {}", hexString);
                        }
                        i = 0; // reset index
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String toHexString(byte[] buffer) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        return Stream.generate(byteBuffer::get)
                .limit(byteBuffer.capacity())
                .map(byteValue -> String.format("%02X", byteValue & 0xFF))
                .collect(Collectors.joining(" "));
    }

    private void configureSerialPort(final String serialPort) {
        String command = String.format("/usr/bin/stty -F %s %d cs%d %s %s %s",
                serialPort, // Port
                230400, // Baud rate 230400
                8, // Data length 8Bits
                "-cstopb", // Stop bit 1
                "-parenb", // Parity check bit N/A
                "-ixon" // Flow control N/A
        );
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
