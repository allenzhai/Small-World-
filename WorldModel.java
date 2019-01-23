
import java.util.*;

final class WorldModel
{
   private static final int ORE_REACH = 1;

   private final int numRows;
   private final int numCols;
   private final Background background[][];
   private final Entity occupancy[][];
   private final Set<Entity> entities;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public int getNumRows() {
      return numRows;
   }
   public int getNumCols() {
      return numCols;
   }
   public Set<Entity> getEntities(){
      return entities;
   }

   public  Optional<Point> findOpenAround(Point pos) {
      for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++)
      {
         for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++)
         {
            Point newPt = new Point(pos.x + dx, pos.y + dy);
            if (this.withinBounds(newPt) &&
                    !this.isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   public boolean withinBounds(Point pos) {
      return pos.y >= 0 && pos.y < this.numRows &&
              pos.x >= 0 && pos.x < this.numCols;
   }

   public Optional<Entity> findNearest(Point pos, Class type) {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : this.entities)
      {
         if (entity.getClass() == type)
         {
            ofType.add(entity);
         }
      }

      return nearestEntity(ofType, pos);
   }
   private static Optional<Entity> nearestEntity(List<Entity> entities, Point pos) {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         Entity nearest = entities.get(0);
         int nearestDistance = distanceSquared(nearest.getPosition(), pos);

         for (Entity other : entities)
         {
            int otherDistance = distanceSquared(other.getPosition(), pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }
   public static int distanceSquared(Point p1, Point p2) {
      int deltaX = p1.x - p2.x;
      int deltaY = p1.y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }

   public void addEntity(Entity entity) {
      if (this.withinBounds(entity.getPosition()))
      {
         this.setOccupancyCell(entity.getPosition(), entity);
        this.entities.add(entity);
      }
   }
   public void removeEntity(Entity entity)
   {
      removeEntityAt(this, entity.getPosition());
   }
   public void moveEntity(Entity entity, Point pos) {
      Point oldPos = entity.getPosition();
      if (this.withinBounds(pos) && !pos.equals(oldPos))
      {
         this.setOccupancyCell(oldPos, null);
         this.removeEntityAt(this, pos);
         this.setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }
   public static void removeEntityAt(WorldModel world, Point pos) {
      if (world.withinBounds(pos)
              && world.getOccupancyCell(pos) != null)
      {
         Entity entity = world.getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1)) ;
         world.getEntities().remove(entity);
         world.setOccupancyCell(pos, null);
      }
   }
   public void tryAddEntity(Entity entity) {
      if (this.isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      this.addEntity(entity);
   }

   public  Background getBackgroundCell(Point pos)
   {
      return this.background[pos.y][pos.x];
   }
   public void setBackground(Point pos, Background background) {
      if (this.withinBounds(pos))
      {
         this.setBackgroundCell(pos, background);
      }
   }
   public void setBackgroundCell(Point pos, Background background)
   {
      this.background[pos.y][pos.x] = background;
   }

   public Optional<Entity> getOccupant(Point pos) {
      if (this.isOccupied(pos))
      {
         return Optional.of(this.getOccupancyCell(pos));
      }
      else
      {
         return Optional.empty();
      }
   }
   public boolean isOccupied(Point pos) {
      return this.withinBounds(pos) &&
              this.getOccupancyCell(pos) != null;
   }
   public  Entity getOccupancyCell(Point pos)
   {
      return this.occupancy[pos.y][pos.x];
   }
   public  void setOccupancyCell(Point pos, Entity entity)
   {
      this.occupancy[pos.y][pos.x] = entity;
   }



}
