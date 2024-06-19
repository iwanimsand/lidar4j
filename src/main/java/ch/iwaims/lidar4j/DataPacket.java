package ch.iwaims.lidar4j;

import java.util.List;

public class DataPacket {

    int packetType;
    int numberOfMeasurementPoints;
    int speed;
    float startAngle;
    float endAngle;
    int timestamp;
    List<MeasurementDataPoint> measurementDataPoints;
    byte crc;

    public DataPacket() {
    }

    public int getPacketType() {
        return packetType;
    }

    public void setPacketType(int packetType) {
        this.packetType = packetType;
    }

    public int getNumberOfMeasurementPoints() {
        return numberOfMeasurementPoints;
    }

    public void setNumberOfMeasurementPoints(int numberOfMeasurementPoints) {
        this.numberOfMeasurementPoints = numberOfMeasurementPoints;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public float getEndAngle() {
        return endAngle;
    }

    public void setEndAngle(float endAngle) {
        this.endAngle = endAngle;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public List<MeasurementDataPoint> getMeasurementDataPoints() {
        return measurementDataPoints;
    }

    public void setMeasurementDataPoints(List<MeasurementDataPoint> measurementDataPoints) {
        this.measurementDataPoints = measurementDataPoints;
    }

    public byte getCrc() {
        return crc;
    }

    public void setCrc(byte crc) {
        this.crc = crc;
    }

    @Override
    public String toString() {
        return "DataPacket{" +
                "packetType=" + packetType +
                ", numberOfMeasurementPoints=" + numberOfMeasurementPoints +
                ", speed=" + speed +
                ", startAngle=" + startAngle +
                ", endAngle=" + endAngle +
                ", timestamp=" + timestamp +
                ", measurementDataPoints=" + measurementDataPoints +
                ", crc=" + crc +
                '}';
    }

}
