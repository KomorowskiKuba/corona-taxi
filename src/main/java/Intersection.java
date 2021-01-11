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

        if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) && q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y)) {
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
        double xa, ya, xb, yb, xc, yc, xd, yd, a1, b1, c1, a2, b2, c2, w, wx, wy, x, y;
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


    public void findIntersections(List<Hospital> hospitalList, List<Hospital> hospitalAndIntersectionList, List<Road> roadList) {
        ArrayList<Double> intersectionsList = new ArrayList<>();
        ArrayList<Point> pointList = new ArrayList<>();
        int roadIndex = roadList.size() + 1;
        int hospitalIndex = hospitalList.size() + 1;
        int intersectionIndex = 1;
        int n;
        for (int i = 0; i < roadList.size(); i++) {
            Point p = new Point((int) roadList.get(i).getFirstHospital().getX(), (int) roadList.get(i).getFirstHospital().getY());
            Point q = new Point((int) roadList.get(i).getSecondHospital().getX(), (int) roadList.get(i).getSecondHospital().getY());
            pointList.add(p);
            pointList.add(q);
        }
        n = roadList.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (pointList.get(2 * i).equals(pointList.get(2 * j)) || pointList.get(2 * i).equals(pointList.get((2 * j) + 1)) || pointList.get((2 * i) + 1).equals(pointList.get(2 * j)) || pointList.get((2 * i) + 1).equals(pointList.get((2 * j) + 1))) {
                    continue;
                }
                if (doIntersect(pointList.get(2 * i), pointList.get((2 * i) + 1), pointList.get(2 * j), pointList.get((2 * j) + 1))) {
                    double[] crossPoint = findCrossPoint(pointList.get(2 * i), pointList.get((2 * i) + 1), pointList.get(2 * j), pointList.get((2 * j) + 1));
                    Hospital intersection = new Hospital(hospitalIndex, "Intersection " + intersectionIndex,crossPoint[0], crossPoint[1], 0,0);
                    hospitalAndIntersectionList.add(intersection);
                    intersectionIndex++;
                    roadIndex = addRoads(pointList.get(2 * i), pointList.get((2 * i) + 1), pointList.get(2 * j), pointList.get((2 * j) + 1), crossPoint[0], crossPoint[1], roadIndex, intersection, hospitalList, roadList);
                }
            }
        }
    }

    private int addRoads(Point p1, Point q1, Point p2, Point q2, double x, double y, int roadIndex, Hospital intersection, List<Hospital> hospitalList, List<Road> roadList) {
        InputFileReader ifr = new InputFileReader();
        double distanceBtwnHsptlAndIntr;
        double distanceBtwnHospitals;
        double realDistance;

        Hospital firstHospital = Hospital.findHospitalByCoordinates(hospitalList, (double)p1.x, (double)p1.y);
        Hospital secondHospital = Hospital.findHospitalByCoordinates(hospitalList, (double)q1.x, (double)q1.y);
        distanceBtwnHsptlAndIntr = Math.sqrt(Math.pow(x - (double) p1.x, 2) + Math.pow(y - (double)p1.y, 2));
        distanceBtwnHospitals = Math.sqrt(Math.pow((double)q1.x - (double) p1.x, 2) + Math.pow((double)q1.y - (double)p1.y, 2));
        realDistance = (distanceBtwnHsptlAndIntr/distanceBtwnHospitals) * getRoadByHospitals(roadList, firstHospital, secondHospital).getDistance();
        roadList.add(new Road(roadIndex, firstHospital, intersection, realDistance));

        distanceBtwnHsptlAndIntr = Math.sqrt(Math.pow(x - (double) q1.x, 2) + Math.pow(y - (double)q1.y, 2));
        realDistance = (distanceBtwnHsptlAndIntr/distanceBtwnHospitals) * getRoadByHospitals(roadList, firstHospital, secondHospital).getDistance();
        roadList.add(new Road(roadIndex + 1, secondHospital, intersection, realDistance));

        roadList.remove(getRoadByHospitals(roadList, firstHospital, secondHospital));

        firstHospital = Hospital.findHospitalByCoordinates(hospitalList, (double)p2.x, (double)p2.y);
        secondHospital = Hospital.findHospitalByCoordinates(hospitalList, (double)q2.x, (double)q2.y);
        distanceBtwnHsptlAndIntr = Math.sqrt(Math.pow(x - (double) p2.x, 2) + Math.pow(y - (double)p2.y, 2));
        distanceBtwnHospitals = Math.sqrt(Math.pow((double)q2.x - (double) p2.x, 2) + Math.pow((double)q2.y - (double)p2.y, 2));
        realDistance = (distanceBtwnHsptlAndIntr/distanceBtwnHospitals) * getRoadByHospitals(roadList, firstHospital, secondHospital).getDistance();
        roadList.add(new Road(roadIndex + 2, firstHospital, intersection, realDistance));

        distanceBtwnHsptlAndIntr = Math.sqrt(Math.pow(x - (double) q2.x, 2) + Math.pow(y - (double)q2.y, 2));
        realDistance = (distanceBtwnHsptlAndIntr/distanceBtwnHospitals) * getRoadByHospitals(roadList, firstHospital, secondHospital).getDistance();
        roadList.add(new Road(roadIndex + 3, secondHospital, intersection, realDistance));

        roadList.remove(getRoadByHospitals(roadList, firstHospital, secondHospital));

        return ++roadIndex;
    }
    public Road getRoadByHospitals(List<Road> roadList, Hospital firstHospital, Hospital secondHospital){
        for( int i = 0; i< roadList.size(); i++){
            if(roadList.get(i).getFirstHospital() == firstHospital || roadList.get(i).getFirstHospital() == secondHospital){
                if(roadList.get(i).getFirstHospital() == firstHospital){
                    if(roadList.get(i).getSecondHospital() == secondHospital){
                        return roadList.get(i);
                    }
                }
                if(roadList.get(i).getFirstHospital() == secondHospital){
                    if(roadList.get(i).getSecondHospital() == firstHospital){
                        return roadList.get(i);
                    }
                }
            }
        }
        return null;
    }

}