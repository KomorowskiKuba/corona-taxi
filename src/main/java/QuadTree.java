class Point {
    private final float x;
    private final float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() { return x; }

    public float getY() { return y; }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }
}

class MyPair {
    private final int  id;
    private final float distance;

    public MyPair(int id, float distance) {
        this.id = id;
        this.distance = distance;
    }

    public float getDistance() { return distance; }

    public int getId() { return id; }
}

enum direction {NE, SE, SW, NW}

class Node {
    private final int id;
    private Point representative;
    private Area quadrant;
    private Node ne, se, sw, nw;

    public Node(Hospital hosp) {
        id = hosp.getId();
        representative = new Point(hosp.getX(), hosp.getY());
    }

    public int getId() {
        return id;
    }

    public Node getNe() { return ne; }

    public void setNe(Node ne) { this.ne = ne; }

    public Node getSe() { return se; }

    public void setSe(Node se) { this.se = se; }

    public Node getNw() { return nw; }

    public void setNw(Node nw) { this.nw = nw; }

    public Node getSw() { return sw; }

    public void setSw(Node sw) { this.sw = sw; }

    public Point getRepresentative() { return representative; }

    public Area getQuadrant() { return quadrant; }

    public void setQuadrant(Area q) { quadrant = q; }
}

class Area {
    private final float xLeft;
    private final float xRight;
    private final float yDown;
    private final float yUp;

    public Area(float xLeft, float yDown, float xRight, float yUp) {
        if (xLeft >= xRight) {
            throw new IllegalArgumentException("xLeft should be lesser than xRight");
        } else if (yDown >= yUp) {
            throw new IllegalArgumentException("yDown should be lesser than yUp");
        }
        this.xLeft = xLeft;
        this.xRight = xRight;
        this.yDown = yDown;
        this.yUp = yUp;
    }

    public Area getQuadrant(direction direction) {
        float quadrantWidth = (xRight - xLeft) / 2;
        float quadrantHeight = (yUp - yDown) / 2;

        switch (direction) {
            case NE:
                return new Area(xLeft + quadrantWidth, yDown + quadrantHeight, xRight, yUp);
            case SE:
                return new Area(xLeft + quadrantWidth, yDown, xRight, yDown + quadrantHeight);
            case SW:
                return new Area(xLeft, yDown, xLeft + quadrantWidth, yDown + quadrantHeight);
            case NW:
                return new Area(xLeft, yDown + quadrantHeight, xLeft + quadrantWidth, yUp);
            default:
                return null;
        }
    }

    public float getyUp() { return yUp; }

    public float getyDown() { return yDown; }

    public float getxRight() { return xRight; }

    public float getxLeft() { return xLeft; }
}

public class QuadTree {

    private Node root;

    public void fillTree(Hospital[] vector, Area quadrant) {
        for (Hospital x : vector) {
            Node child = new Node(x);
            root = put(root, child, quadrant);
        }
    }

    private Node put(Node node, Node child, Area quadrant) {
        if (node == null) {
            child.setQuadrant(quadrant);
            return child;
        }

        direction drc = checkDirection(quadrant, child.getRepresentative());
        put(node, child, quadrant, drc);
        return node;
    }

    //Sprawdza w, którym kwadracie (NE, SE, SW, NW) umieścić dany węzeł.
    private direction checkDirection(Area quadrant, Point child) {
        float chX = child.getX();
        float chY = child.getY();

        float xRight = quadrant.getxRight();
        float xLeft = quadrant.getxLeft();
        float yUp = quadrant.getyUp();
        float yDown = quadrant.getyDown();

        float centX = xLeft + (xRight - xLeft) / 2;
        float centY = yDown + (yUp - yDown) / 2;

        if (chX >= centX) {
            if (chY >= centY) {
                return direction.NE;
            } else {
                return direction.SE;
            }
        } else {
            if (chY > centY) {
                return direction.NW;
            } else {
                return direction.SW;
            }
        }
    }

    private void put(Node parent, Node child, Area quadrant, direction drc) {
        Node node;
        switch (drc) {
            case NE:
                node = put(parent.getNe(), child, quadrant.getQuadrant(direction.NE));
                parent.setNe(node);
                break;
            case SE:
                node = put(parent.getSe(), child, quadrant.getQuadrant(direction.SE));
                parent.setSe(node);
                break;
            case NW:
                node = put(parent.getNw(), child, quadrant.getQuadrant(direction.NW));
                parent.setNw(node);
                break;
            case SW:
                node = put(parent.getSw(), child, quadrant.getQuadrant(direction.SW));
                parent.setSw(node);
                break;
        }
    }

    //Szuka najbardziej skrajnych punktów i na ich podstawie tworzy prostokąt.
    public Area calcQuadrant(Hospital[] vector) {
        float xRight = vector[0].getX();
        float xLeft = vector[0].getX();
        float yLeft = vector[0].getY();
        float yRight = vector[0].getY();

        for (int i = 1; i < vector.length; i++) {
            float xCheck = vector[i].getX();
            float yCheck = vector[i].getY();

            if (xCheck > xRight) {
                xRight = xCheck;
            } else if (xCheck < xLeft) {
                xLeft = xCheck;
            }

            if (yCheck > yRight) {
                yRight = yCheck;
            } else if (yCheck < yLeft) {
                yLeft = yCheck;
            }
        }
        return new Area(xLeft, yLeft, xRight, yRight);
    }

    //Przeszukuje quadTree
    private MyPair searchNode(Node node, Point patient, MyPair best) {
        if (node.getNe() != null) {
            MyPair temp = checkNeNeighbours(node.getNe(), patient, best.getDistance());
            if (temp.getId() != -1) {
                best = temp;
                searchNode(node.getNe(), patient, best);
            }
        }
        if (node.getSe() != null) {
            MyPair temp = checkSeNeighbours(node.getSe(), patient, best.getDistance());
            if (temp.getId() != -1) {
                best = temp;
                searchNode(node.getSe(), patient, best);
            }
        }
        if (node.getSw() != null) {
            MyPair temp = checkSWNeighbours(node.getSw(), patient, best.getDistance());
            if (temp.getId() != -1) {
                best = temp;
                searchNode(node.getSw(), patient, best);
            }
        }
        if (node.getNw() != null) {
            MyPair temp = checkNwNeighbours(node.getNw(), patient, best.getDistance());
            if (temp.getId() != -1) {
                best = temp;
                searchNode(node.getNw(), patient, best);
            }
        }
        return best;
    }

    //Zwraca parę(id, odległość) określającą najbliższy szpital.
    public int findNearest(Point patient) {
        Node firstNode = findFirst(root, patient);
        float bestDistance = calcDistance(firstNode.getRepresentative(), patient);
        MyPair pair = new MyPair(firstNode.getId(), bestDistance);

        pair = searchNode(firstNode, patient, pair);

        return searchNode(root, patient, pair).getId();
    }

    private enum position {UP, DOWN, RIGHT, LEFT}

    private MyPair checkNeNeighbours(Node parent, Point patient, float bestDistance) {
        MyPair pair = checkBorderingNeighbour(parent.getSe(), patient, bestDistance, position.DOWN);
        pair = checkBorderingNeighbour(parent.getNw(), patient, pair.getDistance(), position.LEFT);
        pair = checkDiagonalNeighbour(parent.getSw(), patient, pair.getDistance(), direction.SW);
        return pair;
    }

    private MyPair checkSeNeighbours(Node parent, Point patient, float bestDistance) {
        MyPair pair = checkBorderingNeighbour(parent.getNe(), patient, bestDistance, position.UP);
        pair = checkBorderingNeighbour(parent.getSe(), patient, pair.getDistance(), position.LEFT);
        pair = checkDiagonalNeighbour(parent.getNw(), patient, pair.getDistance(), direction.NW);
        return pair;
    }

    private MyPair checkSWNeighbours(Node parent, Point patient, float bestDistance) {
        MyPair pair = checkBorderingNeighbour(parent.getNw(), patient, bestDistance, position.UP);
        pair = checkBorderingNeighbour(parent.getSe(), patient, pair.getDistance(), position.LEFT);
        pair = checkDiagonalNeighbour(parent.getNe(), patient, pair.getDistance(), direction.NW);
        return pair;
    }

    private MyPair checkNwNeighbours(Node parent, Point patient, float bestDistance) {
        MyPair pair = checkBorderingNeighbour(parent.getNe(), patient, bestDistance, position.UP);
        pair = checkBorderingNeighbour(parent.getSw(), patient, pair.getDistance(), position.LEFT);
        pair = checkDiagonalNeighbour(parent.getSe(), patient, pair.getDistance(), direction.NW);
        return pair;
    }

    private MyPair checkBorderingNeighbour(Node quadrant, Point patient, float bestDistance, position pos) {
        float checkedDistance;
        int id = -1;
        if (quadrant != null) {
            float yBorder = -1;
            float xBorder = -1;
            if (pos.equals(position.DOWN)) {
                yBorder = quadrant.getQuadrant().getyUp();
            } else if (pos.equals(position.UP)) {
                yBorder = quadrant.getQuadrant().getyDown();
            } else if (pos.equals(position.RIGHT)) {
                xBorder = quadrant.getQuadrant().getxLeft();
            } else if (pos.equals(position.LEFT)) {
                xBorder = quadrant.getQuadrant().getxRight();
            }

            switch (pos) {
                case UP:
                case DOWN:
                    if ((float) Math.pow(yBorder - patient.getY(), 2) < bestDistance) {
                        checkedDistance = calcDistance(quadrant.getRepresentative(), patient);
                        if (checkedDistance < bestDistance) {
                            bestDistance = checkedDistance;
                            id = quadrant.getId();
                            break;
                        }
                    }
                case RIGHT:
                case LEFT:
                    if ((float) Math.pow(xBorder - patient.getX(), 2) < bestDistance) {
                        checkedDistance = calcDistance(quadrant.getRepresentative(), patient);
                        if (checkedDistance < bestDistance) {
                            bestDistance = checkedDistance;
                            id = quadrant.getId();
                            break;
                        }
                    }
            }
        }
        return new MyPair(id, bestDistance);
    }

    private MyPair checkDiagonalNeighbour(Node quadrant, Point patient, float bestDistance, direction drc) {
        int id = -1;
        float checkedDistance;
        if (quadrant != null) {
            Point corner = null;
            switch (drc) {
                case NE:
                    corner = new Point(quadrant.getQuadrant().getxLeft(), quadrant.getQuadrant().getyDown());
                    break;
                case SE:
                    corner = new Point(quadrant.getQuadrant().getxLeft(), quadrant.getQuadrant().getyUp());
                    break;
                case SW:
                    corner = new Point(quadrant.getQuadrant().getxRight(), quadrant.getQuadrant().getyUp());
                    break;
                case NW:
                    corner = new Point(quadrant.getQuadrant().getxRight(), quadrant.getQuadrant().getyDown());
                    break;
            }

            if (calcDistance(corner, patient) < bestDistance) {
                checkedDistance = calcDistance(quadrant.getRepresentative(), patient);
                if (checkedDistance < bestDistance) {
                    bestDistance = checkedDistance;
                    id = quadrant.getId();
                    return new MyPair(id, bestDistance);
                }
            }
        }
        return new MyPair(id, bestDistance);
    }

    //Sprawdzamy, do którego kwadratu trafiłby pacjent i wybieramy pierwszy znaleziony szpital na tym samym poziomie.
    private Node findFirst(Node node, Point patient) {
        Node parent = node;
        while (node != null) {
            parent = node;
            switch (checkDirection(node.getQuadrant(), patient)) {
                case NE:
                    node = node.getNe();
                    break;
                case SE:
                    node = node.getSe();
                    break;
                case NW:
                    node = node.getNw();
                    break;
                case SW:
                    node = node.getSw();
                    break;
            }
        }
        return parent;
    }

    private float calcDistance(Point neighbour, Point patient) {
        float xn = neighbour.getX();
        float yn = neighbour.getY();
        float xp = patient.getX();
        float yp = patient.getY();

        return (float) (Math.pow(xn - xp, 2) + Math.pow(yn - yp, 2));
    }

    public static void main(String[] args) {
        QuadTree quadTree = new QuadTree();
        Hospital[] vector = new Hospital[9];

        //Wygląda to tragicznie, ale chciałem mieć dodawanie szpitali pod kontrolą,
        // a nie chciało mi się pisać specjalnej metody do dodawania szpitali :p,
        vector[0] = new Hospital(0, "0", -10, -10, 10);
        vector[1] = new Hospital(1, "1", -10, 10, 10);
        vector[2] = new Hospital(2, "2", 10, -10, 10);
        vector[3] = new Hospital(3, "3", 10, 10, 10);
        vector[4] = new Hospital(4, "4", 0, 0, 10);
        vector[5] = new Hospital(5, "5", 1, 3, 10);
        vector[6] = new Hospital(6, "6", 1, 1, 10);
        vector[7] = new Hospital(7, "7", 3, 3, 10);
        vector[8] = new Hospital(8, "8", 3, 1, 10);

        //Docelowo będzie przeszukiwać jedynie szpitale na obrzeżach zamiast wszystkich.
        Area quadrant = quadTree.calcQuadrant(vector);
        quadTree.fillTree(vector, quadrant);

        Point patient = new Point(1, 4);
        int nearestId = quadTree.findNearest(patient);
        System.out.println("Punkt " + patient.toString() + ". Id najbliższego szpitala: " + nearestId);
    }
}