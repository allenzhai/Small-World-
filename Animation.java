
public class Animation extends Action {
    private final int repeatCount;

    public Animation(Entity entity, int repeatCount)
    {
        super(entity);
        this.repeatCount = repeatCount;
    }

    public int getRepeatCount() {
        return repeatCount;
    }
}
