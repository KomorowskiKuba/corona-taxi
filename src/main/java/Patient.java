public class Patient extends MapObject {
    private boolean isAdditional;
    Patient(int id, int x, int y, boolean isAdditional) {
        super(id, x, y);
        this.isAdditional = isAdditional;
    }

    public boolean isAdditional() {
        return isAdditional;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
