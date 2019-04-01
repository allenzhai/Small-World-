import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OreBlob extends Movable
{
    private static final String QUAKE_KEY = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    public OreBlob(Point position,
                     List<PImage> images,
                     int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod, animationPeriod);
    }

    public void executeActivity(WorldModel world,
                                       ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> blobTarget = findNearest(world,
                getPosition(), Vein.class);
        long nextPeriod = getActionPeriod();

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveTo(world, blobTarget.get(), scheduler))
            {
                Quake quake = new Quake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY), QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);

                world.addEntity(quake);
                nextPeriod += getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                nextPeriod);
    }

    protected Point nextPosition(WorldModel world,
                                     Point destPos)
    {
        PathingStrategy strategy = new AStarPathingStrategy();

        List<Point> newPos = strategy.computePath(getPosition(), destPos, point -> ((!world.isOccupied(point) ||
                        (world.getOccupant(point).isPresent() && world.getOccupant(point).get() instanceof Ore))&&
                        world.withinBounds(point)), Movable::adjacent, PathingStrategy.CARDINAL_NEIGHBORS);

        if (newPos.size() == 0)
        {
            return getPosition();
        }

        return newPos.get(0);
    }
}
