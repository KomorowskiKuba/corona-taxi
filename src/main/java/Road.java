public class Road {
    private final int id;
    private final int firstHospitalId;
    private final int secondHospitalId;
    private final int distance;

    public Road(int id, int firstHospitalId, int secondHospitalId, int distance) {
        this.id = id;
        this.firstHospitalId = firstHospitalId;
        this.secondHospitalId = secondHospitalId;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Road{" +
                "id=" + id +
                ", firstHospitalId=" + firstHospitalId +
                ", secondHospitalId=" + secondHospitalId +
                ", distance=" + distance +
                '}';
    }
}
