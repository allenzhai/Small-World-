import processing.core.PImage;

import java.util.List;

public abstract class ActionEntity extends Entity {
    private final int actionPeriod;
    private final boolean animation;

    public ActionEntity(Point position, List<PImage> images, int actionPeriod, boolean animation){
        super(position, images);
        this.actionPeriod  = actionPeriod;
        this.animation = animation;
    }
    protected int getActionPeriod() {
        return actionPeriod;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), this.getActionPeriod());
        if (animation){
            scheduler.scheduleEvent(this, ((AnimationEntity)this).createAnimationAction(0), ((AnimationEntity)this).getAnimationPeriod());
        }
    }
    public Action createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore);
    }

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}
