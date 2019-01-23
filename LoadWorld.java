import java.util.Scanner;

public class LoadWorld {
    private static final int PROPERTY_KEY = 0;
    private static final String BGND_KEY = "background";

    private static final String MINER_KEY = "miner";
    private static final String OBSTACLE_KEY = "obstacle";
    private static final String ORE_KEY = "ore";
    private static final String SMITH_KEY = "blacksmith";
    private static final String VEIN_KEY = "vein";

    private final WorldModel world;

    public LoadWorld(WorldModel world){
        this.world = world;
    }

    public WorldModel getWorld() {
        return world;
    }

    public void execute(Scanner in, ImageStore imageStore) {
        int lineNumber = 0;
        while (in.hasNextLine())
        {
            try
            {
                if (!processLine(in.nextLine(), this.world, imageStore))
                {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e)
            {
                System.err.println(String.format("invalid entry on line %d",
                        lineNumber));
            }
            catch (IllegalArgumentException e)
            {
                System.err.println(String.format("issue on line %d: %s",
                        lineNumber, e.getMessage()));
            }
            lineNumber++;
        }
    }
    public static boolean processLine(String line, WorldModel world, ImageStore imageStore) {
        String[] properties = line.split("\\s");
        if (properties.length > 0)
        {
            switch (properties[PROPERTY_KEY])
            {
                case BGND_KEY:
                    return Parse.parseBackground(properties, world, imageStore);
                case MINER_KEY:
                    return Parse.parseMiner(properties, world, imageStore);
                case OBSTACLE_KEY:
                    return Parse.parseObstacle(properties, world, imageStore);
                case ORE_KEY:
                    return Parse.parseOre(properties, world, imageStore);
                case SMITH_KEY:
                    return Parse.parseSmith(properties, world, imageStore);
                case VEIN_KEY:
                    return Parse.parseVein(properties, world, imageStore);
            }
        }
        return false;
    }
}
