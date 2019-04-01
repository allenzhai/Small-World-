import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<>();

        // create comparator for priority queue (total dist then order which they came in)
        Comparator<Node> totalDistanceComp = Comparator.comparing(Node::getTotalDistance).thenComparing(Node::getOrder);
        int order = 0;

        // create open list and closed list
        PriorityQueue<Node> openList = new PriorityQueue<>(totalDistanceComp);
        HashSet<Point> closedList = new HashSet<>();


        // set starting point
        Node startNode = new Node(start, end, 0,null, 0);
        openList.add(startNode);

        // go through open list
        while (openList.size() > 0)
        {
            // get current node
            Node currentNode = openList.poll();

            // get valid neighbors
            List<Point> adjPoints = potentialNeighbors.apply(currentNode.getSquare())
                    .filter(canPassThrough).filter(p -> !closedList.contains(p)).collect(Collectors.toList());

            // put valid neighbors into open list if not already in open list
            for (Point p: adjPoints)
            {
                order++;
                Node adj = new Node(p, end, currentNode.getDistanceFromStart() +1, currentNode, order);
                if (!openList.contains(adj))
                {
                    openList.add(adj);
                }
            }


            // if at the end, create the path
            if (withinReach.test(currentNode.getSquare(), end))
            {
                Node node = currentNode;
                while(node != null)
                {
                    path.add(0, node.getSquare());
                    node = node.getPriorSquare();
                }

                ((LinkedList<Point>) path).removeFirst();
                return path;
            }

            // place current node into closed list
            closedList.add(currentNode.getSquare());
        }

        return path;
    }
}
