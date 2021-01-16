import java.util.Objects;

public class Monument extends MapObject {
    private final String name;

    Monument(int id, String name, double x, double y) {
        super(id, x, y);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Monument{" + super.toString() +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Monument)) return false;
        Monument monument = (Monument) o;
        return name.equals(monument.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
