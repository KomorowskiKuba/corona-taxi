public class Hospital extends MapObject {
    private final int totalBeds;
    private int emptyBeds;
    private final String name;

    public Hospital(int id, String name, int x, int y, int totBeds, int freeBeds) {
        super(id, x, y);
        this.name = name;
        this.totalBeds = totBeds;
        this.emptyBeds = freeBeds;
    }

    public int bringPatient() {
        emptyBeds--;
        if (emptyBeds == 0) {
            return 0;
        }
        return emptyBeds;
    }

    public int getTotalBeds() {
        return totalBeds;
    }

    @Override
    public String toString() {
        return "Hospital{" + super.toString() +
                "totalBeds=" + totalBeds +
                ", emptyBeds=" + emptyBeds +
                ", name='" + name + '\'' +
                '}';
    }
}
