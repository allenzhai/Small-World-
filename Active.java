import processing.core.PImage;

import java.util.List;

public abstract class Active extends Entity
{
    private int actionPeriod;

    public Active(Point position, List<PImage> images, int actionPeriod)
    {
        super(position, images);
        this.actionPeriod = actionPeriod;
    }

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                getActionPeriod());
    }

    protected int getActionPeriod() {return this.actionPeriod;}
}
