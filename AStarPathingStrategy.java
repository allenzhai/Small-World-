import sun.awt.image.ImageWatched;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        Comparator<Node> bestFScore = Comparator.comparingInt(Node :: getF);
        PriorityQueue<Node> openList = new PriorityQueue<>(bestFScore);
        HashMap<Integer, Node> easyOpenList = new HashMap<>();
        HashMap<Integer, Node> closedList = new HashMap<>();

        //creating current node and adding it to openList
        List<Point> path = new LinkedList<>();
        Node currentNode = new Node(start, 0, 0, null, 0);
        openList.add(currentNode);
        easyOpenList.put(currentNode.getPoint().hashCode(), currentNode);

        while (!openList.isEmpty()) {

            //After going through all possible points for this current node remove it from openList and add it closed List
            closedList.put(currentNode.getPoint().hashCode(), currentNode);
            easyOpenList.remove(currentNode.getPoint().hashCode());
            currentNode = openList.remove();

            //Check if new node is the goal point
            if (withinReach.test(currentNode.getPoint(), end)) {
                return reconstructPath(currentNode, start);
            }

            //Filtering adjacent points that can be passed through and are not on the closedList
            List<Point> neighbors = potentialNeighbors.apply(currentNode.getPoint())
                    .filter(canPassThrough)
                    .filter(p -> !closedList.containsKey(p.hashCode()))
                    .collect(Collectors.toList());

            //if there are available neighbors then evaluate
            if (neighbors.size() > 0) {
                for (Point p : neighbors) {

                    int g = currentNode.getG() + 1;
                    int h = manhattanDistance(p, end);
                    int f = g + h;

                    //Case 1: Point is already on the openList
                    if (easyOpenList.containsKey(p.hashCode())){
                        Node update = easyOpenList.get(p.hashCode());

                        //only update values if the recalculated g value is better than the one already stored
                        if (update.getG() > g) {
                            update.setG(g);
                            update.setPrior(currentNode);
                        }
                    }
                    //Case 2: Point isn't already in the openList
                    else {
                        Node newNeighbor = new Node(p, g, h, currentNode, f);
                        //add the new point node to the openList
                            openList.add(newNeighbor);
                            easyOpenList.put(newNeighbor.getPoint().hashCode(), newNeighbor);
                        }

                    }
                }

        }
        return path;
    }

    public static LinkedList<Point> reconstructPath(Node node , Point start){
        LinkedList<Point> path = new LinkedList<>();

        while(node.getPrior() != null){
            path.add(0, node.getPoint());
            node = node.getPrior();
        }
        return path;
    }
    public static int manhattanDistance(Point p1, Point p2) {
        return Math.abs(p2.x - p1.x) + Math.abs(p2.y - p1.y);
    }
    public static boolean nodeChecker(Point p, PriorityQueue openList){
        for (Object s : openList){
            if (((Node)s).getPoint().equals(p))
                return true;
        }
        return false;
    }
    public static boolean nodeChecker2(Point p, HashSet closedList){
        for (Object s : closedList){
            if (((Node)s).getPoint().equals(p))
                return true;
        }
        return false;
    }
    public static Node getNode(Point p, PriorityQueue openList){
        for (Object s : openList) {
            if (((Node) s).getPoint().equals(p))
                return (Node) s;
        }
        return null;
    }
}