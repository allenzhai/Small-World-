import processing.core.PImage;

import java.util.List;

public class Quake extends AnimationEntity {
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake (Point position, List<PImage> images){
        super(position, images, QUAKE_ANIMATION_PERIOD, QUAKE_ACTION_PERIOD, false);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
}

