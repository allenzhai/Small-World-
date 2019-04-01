public class Node
{
    private Point square;
    private int g;
    private int h;
    private int f;
    private Node priorSquare;
    private int order;

    public Node(Point square, Point end, int g, Node priorSquare, int order)
    {
        this.square = square;
        this.g = g;
        this.h = Math.abs(square.x - end.x) + Math.abs(square.y - end.y);
        this.f = g + h;
        this.priorSquare = priorSquare;
        this.order = order;
    }

    public boolean equals(Object o)
    {
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Node n = (Node)o;

        return n.square.equals(this.square);
    }

    public int getOrder() {return this.order;}

    public Point getSquare() { return this.square; }

    public int getDistanceFromStart() { return this.g; }

    public int getTotalDistance() { return this.f; }

    public Node getPriorSquare() { return this.priorSquare; }
}
