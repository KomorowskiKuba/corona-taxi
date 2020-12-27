public class Hospital {
    private final int id;
    private final String name;
    private final int x;
    private final int y;
    private final int totalBeds;
    private int emptyBeds;

    public Hospital(int id, String name, int x, int y, int totBeds) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalBeds() {
        return totalBeds;
    }
}
