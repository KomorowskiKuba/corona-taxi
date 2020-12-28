public abstract class MapObject {
    private final int x;
    private final int y;
    private final int id;

    MapObject(int id, int x, int y) {
        this.id = id;this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
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
