import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Tree extends Active
{
    public Tree(Point position, List<PImage> images,
                int actionPeriod)
    {
        super(position, images, actionPeriod);
    }

    public void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(getPosition());

        if (openPt.isPresent())
        {
            KillerElf elf = new KillerElf(openPt.get(), imageStore.getImageList("elf"),1100, 0);
            world.addEntity(elf);
            elf.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                getActionPeriod());
    }
}
