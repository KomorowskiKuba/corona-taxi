import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Hospital extends MapObject {
    private final int totalBeds;
    private int emptyBeds;
    private final String name;
    private double distance = Double.MAX_VALUE;
    private List<Hospital> shortestPath = new LinkedList<>();
    private final Map<Hospital, Double> adjacentHospitalsMap = new HashMap<>();

    public Hospital(int id, String name, double x, double y, int totBeds, int freeBeds) {
        super(id, x, y);
        this.name = name;
        this.totalBeds = totBeds;
        this.emptyBeds = freeBeds;
    }

    public static Hospital findHospitalById(List<Hospital> hospitalList, int id) {
        for (Hospital h : hospitalList) {
            if (h.getId() == id) {
                return h;
            }
        }
        return null;
    }

    public static Hospital findHospitalByCoordinates(List<Hospital> hospitalList, double x, double y) {
        for (Hospital h : hospitalList) {
            if (h.getX() == x) {
                if (h.getY() == y){
                    return h;
                }
            }
        }
        return null;
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

    public int getEmptyBeds() {
        return emptyBeds;
    }

    public double getDistance() {
        return distance;
    }

    public void addDestination(Hospital destination, double distance) {
        adjacentHospitalsMap.put(destination, distance);
    }

    public Map<Hospital, Double> getAdjacentNodeMap() {
        return adjacentHospitalsMap;
    }

    public List<Hospital> getShortestPath() {
        return shortestPath;
    }


    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setShortestPath(List<Hospital> shortestPath) {
        this.shortestPath = shortestPath;
    }

    @Override
    public String toString() {
        return "Hospital{" + super.toString() +
                "totalBeds=" + totalBeds +
                ", emptyBeds=" + emptyBeds +
                ", name='" + name + '\'' +
                ", distance=" + distance +
                '}';
    }
}