import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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

    public static int orientation(Coordinates p, Coordinates q, Coordinates r) {
        int val = (q.y - p.y) * (r.x - q.x) -
                (q.x - p.x) * (r.y - q.y);

        if (val == 0) return 0;
        return (val > 0) ? 1 : 2;
    }

    public static ArrayList<Double> convex_hull(List<Hospital> hospitalList, List<Monument> monumentList) {
        ArrayList<Double> toReturn = new ArrayList<>();
        int n = hospitalList.size() + monumentList.size();
        Coordinates[] points = new Coordinates[n];
        int counter = 0;

        for (int i = 0; i < hospitalList.size(); i++) {
            Coordinates point = new Coordinates();
            point.x = hospitalList.get(i).getX();
            point.y = hospitalList.get(i).getY();
            points[i] = point;
            counter++;
        }

        for (int i = 0; i < monumentList.size(); i++) {
            Coordinates point = new Coordinates();
            point.x = monumentList.get(i).getX();
            point.y = monumentList.get(i).getY();
            points[counter++] = point;
        }

        Vector<Coordinates> hull = new Vector<Coordinates>();

        int l = 0;
        for (int i = 1; i < n; i++)
            if (points[i].x < points[l].x)
                l = i;

        int p = l, q;
        do {
            hull.add(points[p]);
            q = (p + 1) % n;

            for (int i = 0; i < n; i++) {
                if (orientation(points[p], points[i], points[q])
                        == 2)
                    q = i;
            }
            p = q;

        } while (p != l);

        for (Coordinates tmp : hull) {
            toReturn.add((double) tmp.x);
            toReturn.add((double) tmp.y);
        }
        return toReturn;
    }

    public static boolean isInBorder(ArrayList<Double> borders, Point patient) {
        int pos = 0;
        int neg = 0;

        for (int i = 0; i < borders.size(); i += 2) {
            if (patient.getX() == borders.get(i) && patient.getY() == borders.get(i + 1)) {
                return true;
            }

            double x = patient.getX();
            double y = patient.getY();

            double x1 = borders.get(i);
            double y1 = borders.get(i + 1);

            int j = i < borders.size() - 2 ? i + 2 : 0;

            double x2 = borders.get(j);
            double y2 = borders.get(j + 1);

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