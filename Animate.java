import processing.core.PImage;

import java.util.List;

public abstract class Animate extends Active
{
    private int animationPeriod;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Animate(Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    protected int getAnimationPeriod() { return this.animationPeriod; }

    public void nextImage()
    {
        int index = (getImageIndex() + 1) % getImages().size();
        setImageIndex(index);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                getActionPeriod());
        int repeatCount;
        if (this instanceof Quake)
            repeatCount = QUAKE_ANIMATION_REPEAT_COUNT;
        else
            repeatCount = 0;
        scheduler.scheduleEvent( this,
                new Animation(this, repeatCount), getAnimationPeriod());
    }
}
