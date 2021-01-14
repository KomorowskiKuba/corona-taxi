public class Road {
    private final int id;
    private final Hospital firstHospital;
    private final Hospital secondHospital;
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

    public double getDistance() {
        return distance;
    }
}
