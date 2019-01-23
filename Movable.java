import processing.core.PImage;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class Movable extends AnimationEntity{

    private PathingStrategy strategy = new AStarPathingStrategy();

    public Movable(Point position, int actionPeriod, int animationPeriod, List<PImage> images){
        super(position, images, animationPeriod, actionPeriod, true);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (adjacent(this.getPosition(), target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            List<Point> points = strategy.computePath(this.getPosition(), target.getPosition(),
                                                        p -> world.withinBounds(p) && !world.isOccupied(p),
                                                        (p1, p2) -> adjacent(p1, p2),
                                                        PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS);
//            Point nextPos = this.nextPosition(world, target.getPosition());
            if (points.size() == 0){
                return false;
            }
            Point nextPos = points.get(0);

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }


//    public Point nextPosition(WorldModel world, Point destPos) {
//        int horiz = Integer.signum(destPos.x - this.getPosition().x);
//        Point newPos = new Point(this.getPosition().x + horiz,
//                this.getPosition().y);
//
//        Optional<Entity> occupant = world.getOccupant(newPos);
//
//        if (horiz == 0 ||
//                (occupant.isPresent() && !(Objects.equals(occupant.getClass().getName(), "Ore"))))
//        {
//            int vert = Integer.signum(destPos.y - this.getPosition().y);
//            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
//            occupant = world.getOccupant(newPos);
//
//            if (vert == 0 ||
//                    (occupant.isPresent() && !(Objects.equals(occupant.getClass().getName(), "Ore"))))
//            {
//                newPos = this.getPosition();
//            }
//        }
//
//        return newPos;
//    }

    public static boolean adjacent(Point p1, Point p2) {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
                (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
    }
}
