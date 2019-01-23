import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class MinerNotFull extends Miner {
    public MinerNotFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod , List<PImage> images){
        super(id, resourceLimit, 0, position, actionPeriod, animationPeriod, images);
    }

   //MinerNotFull Methods
   public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
       Optional<Entity> notFullTarget = world.findNearest(this.getPosition(), Ore.class);

       if (!notFullTarget.isPresent() ||
               !this.moveTo(world, notFullTarget.get(), scheduler) ||
               !this.transform(world, scheduler, imageStore))
       {
           scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), this.getActionPeriod());
       }
   }


    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getResourceCount() >= this.getResourceLimit())
        {
            MinerFull newMiner =  new MinerFull(this.getId(), this.getResourceLimit(),
                    this.getPosition(), this.getActionPeriod(),
                    this.getAnimationPeriod(), this.getImages());


            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(newMiner);
            newMiner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

}
