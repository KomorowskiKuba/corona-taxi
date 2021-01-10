import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Coordinates implements Comparable<Coordinates> {
    int x;
    int y;

    public int compareTo(Coordinates c) {
        if (this.x == c.x) {
            return this.y - c.y;
        } else {
            return this.x - c.x;
        }
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}

public class ConvexHull {

    public static long cross(Coordinates O, Coordinates A, Coordinates B) {
        return (A.x - O.x) * (long) (B.y - O.y) - (A.y - O.y) * (long) (B.x - O.x);
    }

    public ArrayList<Double> convex_hull(List<Hospital> hospitalList, List<Monument> monumentList) {
        Coordinates[] coordinatesArray = new Coordinates[hospitalList.size() + monumentList.size()];
        int counter = 0;

        for (int i = 0; i < hospitalList.size(); i++) {
            Coordinates point = new Coordinates();
            point.x = hospitalList.get(i).getX();
            point.y = hospitalList.get(i).getY();
            coordinatesArray[i] = point;
            counter++;
        }

        for (int i = 0; i < monumentList.size(); i++) {
            Coordinates point = new Coordinates();
            point.x = monumentList.get(i).getX();
            point.y = monumentList.get(i).getY();
            coordinatesArray[counter++] = point;
        }

        int n = coordinatesArray.length, k = 0;
        Coordinates[] H = new Coordinates[2 * n];

        Arrays.sort(coordinatesArray);

        for (int i = 0; i < n; ++i) {
            while (k >= 2 && cross(H[k - 2], H[k - 1], coordinatesArray[i]) <= 0)
                k--;
            H[k++] = coordinatesArray[i];
        }

        for (int i = n - 2, t = k + 1; i >= 0; i--) {
            while (k >= t && cross(H[k - 2], H[k - 1], coordinatesArray[i]) <= 0)
                k--;
            H[k++] = coordinatesArray[i];
        }
        if (k > 1) {
            H = Arrays.copyOfRange(H, 0, k - 1); // remove non-hull vertices after k; remove k - 1 which is a duplicate
        }
        ArrayList<Double> toReturn = new ArrayList<>();
        for (int i = 0; i < H.length; i++) {
            toReturn.add((double) H[i].x);
            toReturn.add((double) H[i].y);
        }
        return toReturn;
    }

    public boolean isInBorder(ArrayList<Double> borders, Point patient) {
        int pos = 0;
        int neg = 0;

        for (int i = 0; i < borders.size(); i += 2) {
            if (patient.getX() == borders.get(i) && patient.getY() == borders.get(i + 1)) {
                return true;
            }

            double x1 = borders.get(i);
            double y1 = borders.get(i + 1);

            int j = i < borders.size() - 1 ? i + 1 : 0;

            double x2 = borders.get(j);
            double y2 = borders.get(j + 1);

            double x = patient.getX();
            double y = patient.getY();

            double crossProduct = (x - x1) * (y2 - y1) - (x2 - x1) * (y - y1);

            if (crossProduct > 0) {
                pos++;
            }
            if (crossProduct < 0) {
                neg++;
            }

            if (pos > 0 && neg > 0) {
                return false;
            }
        }
        return true;
    }
}