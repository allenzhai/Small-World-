import processing.core.PImage;

import java.util.List;

public abstract class Entity
{
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(Point position, List<PImage> images)
    {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }
    public Point getPosition()
    {
        return this.position;
    }

    public void setPosition(Point p) {this.position = p; }

    public PImage getCurrentImage()
    {
        return (images.get(getImageIndex()));
    }

    protected List<PImage> getImages() { return this.images; }

    protected int getImageIndex() {return this.imageIndex; }

    protected void setImageIndex(int x) {this.imageIndex = x;}
}
