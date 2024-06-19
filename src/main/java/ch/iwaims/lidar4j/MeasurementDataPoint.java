package ch.iwaims.lidar4j;

public class MeasurementDataPoint {

    private int distance;
    private int intensity;
    private float angle;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "MeasurementDataPoint{" +
                "distance=" + distance +
                ", intensity=" + intensity +
                ", angle=" + angle +
                '}';
    }
}
