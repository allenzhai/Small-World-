import processing.core.PImage;

import java.util.List;

public class Quake extends Animate
{
    public Quake(Point position,
                     List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod, animationPeriod);
    }

    public void executeActivity(WorldModel world,
                                     ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
}
