import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull extends Miner
{
    private int resourceCount;

    public MinerNotFull(String id, Point position,
                     List<PImage> images, int resourceLimit, int resourceCount,
                     int actionPeriod, int animationPeriod)
    {
        super(id, position, images, resourceLimit, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = findNearest(world, getPosition(),
                Ore.class);

        if (!notFullTarget.isPresent() ||
                !moveTo(world, notFullTarget.get(), scheduler) ||
                !transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    new Activity(this, world, imageStore),
                    getActionPeriod());
        }
    }

    public int getResourceCount() {return resourceCount; }

    public void setResourceCount(int x) {resourceCount = resourceCount + x; }
}
