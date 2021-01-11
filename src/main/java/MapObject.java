public abstract class MapObject {
    private final double x;
    private final double y;
    private final int id;

    MapObject(int id, double x, double y) {
        this.id = id;this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return  "x=" + x +
                ", y=" + y +
                ", id=" + id +
                ", ";
    }
}
