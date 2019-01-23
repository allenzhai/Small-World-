import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public abstract class Miner extends Movable{

    private final String id;
    private final int resourceLimit;
    private int resourceCount;
    private PathingStrategy strategy = new AStarPathingStrategy();

    Miner(String id, int resourceLimit, int resourceCount,  Point position, int actionPeriod, int animationPeriod , List<PImage> images){
        super(position, actionPeriod, animationPeriod, images);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.id = id;
    }

    protected String getId() {
        return id;
    }
    protected int getResourceLimit(){
        return resourceLimit;
    }
    protected int getResourceCount(){ return resourceCount; }

    public  boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (adjacent(this.getPosition(), target.getPosition()))
        {
            if (resourceCount < resourceLimit) {
                this.resourceCount += 1;
                world.removeEntity(target);
                scheduler.unscheduleAllEvents(target);
            }
            return true;
        }
        else
        {
            List<Point> points = strategy.computePath(this.getPosition(), target.getPosition(),
                        p -> world.withinBounds(p) && !world.isOccupied(p),
                        (p1, p2) -> adjacent(p1, p2),
                        PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS);

            if (points.size() == 0){
                return false;
            }
            Point nextPos = points.get(0);
//            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }
    public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.getPosition().x);
        Point newPos = new Point(this.getPosition().x + horiz,
                this.getPosition().y);

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.y - this.getPosition().y);
            newPos = new Point(this.getPosition().x,
                    this.getPosition().y + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = this.getPosition();
            }
        }

        return newPos;
    }

}
