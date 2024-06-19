package ch.iwaims.lidar4j;

import java.util.ArrayList;
import java.util.List;

public class DataPacketFactory {

    public DataPacket fromBytes(final byte[] data) {

        if (data.length != 47) {
            throw new IllegalArgumentException("Data length must be 47 bytes, actual length: " + data.length);
        }

        // ignore header
        int idx = 1;

        byte verLen = data[idx];
        int packetType = (verLen >> 5) & 0x07; // upper three bits
        int numberOfMeasurementPoints = verLen & 0x1F; // lower five bits

        // one byte read
        idx += 1;

        int speed = ((data[idx + 1] & 0xFF) << 8) | (data[idx] & 0xFF);

        // two bytes read
        idx += 2;

        float startAngle;
        {
            int tmp = ((data[idx + 1] & 0xFF) << 8) | (data[idx] & 0xFF);
            startAngle = tmp * 0.01f;
        }

        // two bytes read
        idx += 2;

        List<MeasurementDataPoint> measurementDataPoints = new ArrayList<>(numberOfMeasurementPoints);
        // data starts at position 6 (idx=5) and each measurement point has 3 bytes
        for (int i = 0; i < numberOfMeasurementPoints; i++) {
            int distance = ((data[idx + 1] & 0xFF) << 8) | (data[idx + 2] & 0xFF);
            int intensity = data[idx + 3] & 0xFF;

            MeasurementDataPoint measurementDataPoint = new MeasurementDataPoint();
            measurementDataPoint.setDistance(distance);
            measurementDataPoint.setIntensity(intensity);
            measurementDataPoints.add(measurementDataPoint);

            // three bytes read
            idx += 3;
        }

        float endAngle;
        {
            int tmp = ((data[idx + 1] & 0xFF) << 8) | (data[idx] & 0xFF);
            endAngle = tmp * 0.01f;
        }

        // two bytes read
        idx += 2;

        int timestamp = ((data[idx + 1] & 0xFF) << 8) | (data[idx] & 0xFF);

        // two bytes read
        idx += 2;

        byte crc = data[idx];

        // set angle in measurement data points
        // TODO optimization needed
        float step = ((endAngle - startAngle + 360) % 360) / (numberOfMeasurementPoints - 1);
        for (int i = 0; i < measurementDataPoints.size(); i++) {
            float angle = (startAngle + step * i) % 360;
            measurementDataPoints.get(i).setAngle(angle);
        }

        //
        DataPacket dataPacket = new DataPacket();
        dataPacket.setPacketType(packetType);
        dataPacket.setNumberOfMeasurementPoints(numberOfMeasurementPoints);
        dataPacket.setSpeed(speed);
        dataPacket.setStartAngle(startAngle);
        dataPacket.setMeasurementDataPoints(measurementDataPoints);
        dataPacket.setEndAngle(endAngle);
        dataPacket.setTimestamp(timestamp);
        dataPacket.setCrc(crc);

        return dataPacket;
    }

}
