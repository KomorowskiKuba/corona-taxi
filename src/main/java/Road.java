public class Road {
    private final int id;
    private Hospital firstHospital;
    private Hospital secondHospital;
    private final int distance;

    public Road(int id, Hospital firstHospital, Hospital secondHospital, int distance) {
        this.id = id;
        this.firstHospital = firstHospital;
        this.secondHospital = secondHospital;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Road{" +
                "id=" + id +
                ", firstHospital=" + firstHospital +
                ", secondHospital=" + secondHospital +
                ", distance=" + distance +
                '}';
    }

    public Hospital getFirstHospital() { return firstHospital; }

    public Hospital getSecondHospital() {
        return secondHospital;
    }
}
