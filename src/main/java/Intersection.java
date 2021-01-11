import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class Intersection {
    static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Point other = (Point) obj;
            if (this.x != other.x) {
                return false;
            } else if (this.y != other.y) {
                return false;
            }
            return true;
        }
    }

    ;


    static boolean onSegment(Point p, Point q, Point r) {

        if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) && q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y)){
            return true;
        }

        return false;
    }


    static int orientation(Point p, Point q, Point r) {

        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);

        if (val == 0) {
            return 0;
        }

        return (val > 0) ? 1 : 2;
    }


    static boolean doIntersect(Point p1, Point q1, Point p2, Point q2) {

        int o1 = orientation(p1, q1, p2);

        int o2 = orientation(p1, q1, q2);

        int o3 = orientation(p2, q2, p1);

        int o4 = orientation(p2, q2, q1);


        if (o1 != o2 && o3 != o4) {
            return true;
        }

        if (o1 == 0 && onSegment(p1, p2, q1)) {
            return true;
        }

        if (o2 == 0 && onSegment(p1, q2, q1)) {
            return true;
        }

        if (o3 == 0 && onSegment(p2, p1, q2)) {
            return true;
        }

        if (o4 == 0 && onSegment(p2, q1, q2)) {
            return true;
        }

        return false;
    }

    static double[] findCrossPoint(Point a, Point b, Point c, Point d) {
        double xa,ya,xb,yb,xc,yc,xd,yd,a1,b1,c1,a2,b2,c2,w,wx,wy,x,y;
        double[] crossPoint = new double[2];
        xa = a.x;
        ya = a.y;
        xb = b.x;
        yb = b.y;
        xc = c.x;
        yc = c.y;
        xd = d.x;
        yd = d.y;
        a1 = ya - yb;
        b1 = xb - xa;
        c1 = xa * yb - xb * ya;
        a2 = yc - yd;
        b2 = xd - xc;
        c2 = xc * yd - xd * yc;
        w = a1 * b2 - a2 * b1;
        wx = (-c1) * b2 + c2 * b1;
        wy = a1 * (-c2) + a2 * c1;
        x = wx / w;
        y = wy / w;
        crossPoint[0] = x;
        crossPoint[1] = y;
        return crossPoint;
    }


    public ArrayList<Double> findIntersections(List<Hospital> hospitalList, List<Road> roadList) {
        ArrayList<Double> intersectionsList = new ArrayList<>();
        ArrayList<Point> pointList = new ArrayList<>();
        for (int i = 0; i < roadList.size(); i++) {
            Point p = new Point(roadList.get(i).getFirstHospital().getX(), roadList.get(i).getFirstHospital().getY());
            Point q = new Point(roadList.get(i).getSecondHospital().getX(), roadList.get(i).getSecondHospital().getY());
            pointList.add(p);
            pointList.add(q);
        }

        for (int i = 0; i < roadList.size(); i++) {
            for (int j = i + 1; j < roadList.size(); j++) {
                if (pointList.get(2 * i).equals(pointList.get(2 * j)) || pointList.get(2 * i).equals(pointList.get((2 * j) + 1)) || pointList.get((2 * i) + 1).equals(pointList.get(2 * j)) || pointList.get((2 * i) + 1).equals(pointList.get((2 * j) + 1))) {
                    continue;
                }
                if (doIntersect(pointList.get(2 * i), pointList.get((2 * i) + 1), pointList.get(2 * j), pointList.get((2 * j) + 1))) {
                    double[] crossPoint = findCrossPoint(pointList.get(2 * i), pointList.get((2 * i) + 1), pointList.get(2 * j), pointList.get((2 * j) + 1));
                    intersectionsList.add(crossPoint[0]);
                    intersectionsList.add(crossPoint[1]);
                }
            }
        }
        return intersectionsList;
    }
}