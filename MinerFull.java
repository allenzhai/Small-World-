import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class MinerFull extends Miner{
    public MinerFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod , List<PImage> images){
        super(id, resourceLimit, 4, position, actionPeriod, animationPeriod, images);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(), BlackSmith.class);

        if (fullTarget.isPresent() && this.moveTo(world, fullTarget.get(), scheduler))
        {
            this.transform(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), this.getActionPeriod());
        }
    }

    public void transform(WorldModel world , EventScheduler scheduler, ImageStore imageStore) {

            MinerNotFull newMiner = new MinerNotFull(this.getId(), this.getResourceLimit(),
                    this.getPosition(), this.getActionPeriod(),
                    this.getAnimationPeriod(), this.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(newMiner);
            newMiner.scheduleActions(scheduler, world, imageStore);
        }
}
