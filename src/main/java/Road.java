import java.util.List;
import java.util.Objects;

public class Road {
    private final int id;
    private Hospital firstHospital;
    private Hospital secondHospital;
    private final double distance;

    public Road(int id, Hospital firstHospital, Hospital secondHospital, double distance) {
        this.id = id;
        this.firstHospital = firstHospital;
        this.secondHospital = secondHospital;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Road{" +
                "id=" + id +
                ", firstHospital=" + firstHospital.getId() +
                ", secondHospital=" + secondHospital.getId() +
                ", distance=" + distance +
                '}';
    }

    public Hospital getFirstHospital() { return firstHospital; }

    public Hospital getSecondHospital() {
        return secondHospital;
    }

    public int getId() {
        return id;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Road)) return false;
        Road road = (Road) o;
        return id == road.id && Double.compare(road.distance, distance) == 0 && firstHospital.equals(road.firstHospital) && secondHospital.equals(road.secondHospital);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstHospital, secondHospital, distance);
    }
}
