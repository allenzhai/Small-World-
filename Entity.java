import processing.core.PImage;
import java.util.List;

public abstract class Entity{
   private Point position;
   private final List<PImage> images;
   private int imageIndex = 0;

   public Entity(Point position, List<PImage> images){
      this.position = position;
      this.images = images;
   }
   protected Point getPosition(){
      return this.position;
   }
   protected void setPosition(Point p){ position = p;}
   protected int getImageIndex(){return imageIndex;}
   protected void setImageIndex(int o){this.imageIndex = o;}
   protected List<PImage> getImages(){
      return images;
   }
}