import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Santa extends Movable{

    public Santa(Point position,
                 List<PImage> images,
                 int actionPeriod, int animationPeriod)
    {
    super(position, images, actionPeriod, animationPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> santaTarget = findNearest(world, getPosition(),
                OreBlob.class);

        if (santaTarget.isPresent()) {
            if (moveTo(world, santaTarget.get(), scheduler)) {
                world.removeEntity(santaTarget.get());
            }
        }

        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), getActionPeriod());
    }
    protected Point nextPosition(WorldModel world, Point destPos)
    {
        PathingStrategy strategy = new AStarPathingStrategy();

        List<Point> newPos = strategy.computePath(getPosition(), destPos, point -> (!world.isOccupied(point) && world.withinBounds(point)),
                Movable::adjacent, PathingStrategy.CARDINAL_NEIGHBORS);

        if (newPos.size() == 0)
        {
            return getPosition();
        }

        return newPos.get(0);
    }

}
