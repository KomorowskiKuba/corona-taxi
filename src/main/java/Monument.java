public class Monument extends MapObject {
    private final String name;

    Monument(int id, String name, int x, int y) {
        super(id, x, y);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Monument{" + super.toString() +
                "name='" + name + '\'' +
                '}';
    }
}
