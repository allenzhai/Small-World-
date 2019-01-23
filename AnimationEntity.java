import processing.core.PImage;

import java.util.List;

public abstract class AnimationEntity extends ActionEntity{

    private final int animationPeriod;

    public AnimationEntity(Point position, List<PImage> images, int animationPeriod, int actionPeriod, boolean animation ){
        super(position, images, actionPeriod, animation);
        this.animationPeriod = animationPeriod;
    }
    protected int getAnimationPeriod() { return animationPeriod; }

    public Action createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }
    public void nextImage() {
        this.setImageIndex((this.getImageIndex() + 1) % this.getImages().size());
    }
}
