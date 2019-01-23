public class Node {
    private Point point;
    private int g;
    private int h;
    private int f;
    private Node prior;

    public Node(Point location, int distanceFromStart, int heuristicDistance, Node previousNode, int fScore){
        point = location;
        g = distanceFromStart;
        h = heuristicDistance;
        prior = previousNode;
        f = fScore;
    }

    public Point getPoint() {
        return point;
    }
    public int getG() {
        return g;
    }
    public int getH() {
        return h;
    }
    public Node getPrior() {
        return prior;
    }
    public int getF(){return f;}

    public void setG(int g) {
        this.g = g;
    }
    public void setH(int h) {
        this.h = h;
    }
    public void setF(int f) {
        this.f = f;
    }
    public void setPrior(Node prior) {
        this.prior = prior;
    }
}
