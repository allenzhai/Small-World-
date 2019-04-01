import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import processing.core.*;

public final class VirtualWorld
   extends PApplet
{
   private static final int TIMER_ACTION_PERIOD = 100;

   private static final int VIEW_WIDTH = 640;
   private static final int VIEW_HEIGHT = 480;
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   private static final int WORLD_WIDTH_SCALE = 2;
   private static final int WORLD_HEIGHT_SCALE = 2;

   private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   private static final String IMAGE_LIST_FILE_NAME = "imagelist";
   private static final String DEFAULT_IMAGE_NAME = "background_default";
   private static final int DEFAULT_IMAGE_COLOR = 0x808080;

   private static final String LOAD_FILE_NAME = "gaia.sav";

   private static final String FAST_FLAG = "-fast";
   private static final String FASTER_FLAG = "-faster";
   private static final String FASTEST_FLAG = "-fastest";
   private static final double FAST_SCALE = 0.5;
   private static final double FASTER_SCALE = 0.25;
   private static final double FASTEST_SCALE = 0.10;

   private static double timeScale = 1.0;

   private ImageStore imageStore;
   private WorldModel world;
   private WorldView view;
   private EventScheduler scheduler;

   private long next_time;

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         this.scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      view.drawViewport();
   }

   public void mousePressed(){
   worldEvent();
   }


   private Point mouseToPoint(int x, int y)
   {
      return new Point((x + (view.getViewport().getCol() * 32))/TILE_WIDTH ,
              (y + (view.getViewport().getRow() * 32))/TILE_HEIGHT);
   }

   private void worldEvent(){
      Point pressed = mouseToPoint(mouseX, mouseY);
      Point other = mouseToPoint(mouseX + 20, mouseY + 20);
      Point other2 = mouseToPoint(mouseX - 20, mouseY - 20);
      Point other3 = mouseToPoint(mouseX - 45, mouseY - 45);
      Point other4 = mouseToPoint(mouseX - 45, mouseY + 50);
      Point other5 = mouseToPoint(mouseX + 45, mouseY - 65);
      Point other6 = mouseToPoint(mouseX - 78, mouseY - 30);
      Point other7 = mouseToPoint(mouseX + 80, mouseY + 45);

      Tree t1 = new Tree(pressed, imageStore.getImageList("tree"), 30000);
      Tree t2 = new Tree(other, imageStore.getImageList("tree"), 30000);
      Tree t3 = new Tree(other2, imageStore.getImageList("tree"), 30000);
      Tree t4 = new Tree(other3, imageStore.getImageList("tree"), 40000);
      Tree t5 = new Tree(other4, imageStore.getImageList("tree"), 30000);
      Tree t6 = new Tree(other5, imageStore.getImageList("tree"), 30000);
      Tree t7 = new Tree(other6, imageStore.getImageList("tree"), 40000);
      Tree t8 = new Tree(other7, imageStore.getImageList("tree"), 30000);

      if (!world.isOccupied(pressed))
      {
         world.addEntity(t1);
         if (!world.isOccupied(other))
            world.addEntity(t2);
         if (!world.isOccupied(other2))
            world.addEntity(t3);
         if (!world.isOccupied(other3))
            world.addEntity(t4);
         if (!world.isOccupied(other4))
            world.addEntity(t5);
         if (!world.isOccupied(other5))
            world.addEntity(t6);
         if (!world.isOccupied(other6))
            world.addEntity(t7);
         if (!world.isOccupied(other7))
            world.addEntity(t8);

         Optional<Entity> withinBoundsBlackSmith = Movable.findNearest(world, pressed, Blacksmith.class);
         BiPredicate<Entity, Point> local = (b, p) -> (b.getPosition().x - p.x) + (b.getPosition().y - p.y) < 100;
         if (withinBoundsBlackSmith.isPresent()){
            Entity newSanta = withinBoundsBlackSmith.get();
            if(local.test(newSanta, pressed)){
               Santa santa = new Santa(newSanta.getPosition(), imageStore.getImageList("santa"), 2, 3);
               world.removeEntity(newSanta);

               world.addEntity(santa);
               scheduler.scheduleEvent(santa, new Activity(santa, world, imageStore), 2);
               santa.scheduleActions(scheduler, world, imageStore);
            }
         }
      }

      t1.scheduleActions(scheduler, world, imageStore);
      t4.scheduleActions(scheduler, world, imageStore);
      t7.scheduleActions(scheduler, world, imageStore);

      redraw();
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
         }
         view.shiftView(dx, dy);
      }
   }

   public static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
              imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   public static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         imageStore.loadImages(in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }


   public static void loadWorld(WorldModel world, String filename,
      ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         world.load(in, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.getEntities())
      {
         if (entity instanceof Active)
            ((Active)entity).scheduleActions(scheduler, world, imageStore);
      }
   }

   public static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
