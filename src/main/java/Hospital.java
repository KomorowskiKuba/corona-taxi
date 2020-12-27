public class Hospital extends MapObject {
    private final int totalBeds;
    private int emptyBeds;

    public Hospital(int id, String name, int x, int y, int totBeds) {
        super(id, name, x, y);
        totalBeds = totBeds;
        emptyBeds = totBeds;
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
}
