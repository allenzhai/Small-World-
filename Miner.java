import processing.core.PImage;

import java.util.List;

public abstract class Miner extends Movable
{
    private String id;
    private int resourceLimit;

    public Miner(String id, Point position, List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod) {
        super(position, images, actionPeriod, animationPeriod);
        this.id = id;
        this.resourceLimit = resourceLimit;
    }

    protected String getID() { return this.id; }

    protected int getResourceLimit() { return this.resourceLimit; }

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

    protected boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (this instanceof MinerNotFull)
        {
            if (((MinerNotFull)this).getResourceCount() >= getResourceLimit())
            {
                MinerFull miner = new MinerFull(getID(), getPosition(), getImages(), getResourceLimit(),
                        getActionPeriod(), getAnimationPeriod());

                world.removeEntity(this);
                scheduler.unscheduleAllEvents(this);

                world.addEntity(miner);
                miner.scheduleActions(scheduler, world, imageStore);

                return true;
            }

            return false;
        }
        else
        {
            MinerNotFull miner = new MinerNotFull(getID(), getPosition(), getImages(), getResourceLimit(), 0,
                    getActionPeriod(), getAnimationPeriod());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);
            return true;
        }
    }
}
