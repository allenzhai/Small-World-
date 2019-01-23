abstract class Action {

   private final Entity entity;

   public Action(Entity entity){
      this.entity = entity;
   }
   public void executeAction(EventScheduler scheduler){
      if (this instanceof Animation){
         ((AnimationEntity)this.entity).nextImage();

         if (((Animation)this).getRepeatCount() != 1)
         {
            scheduler.scheduleEvent(this.entity,
                    ((AnimationEntity)this.entity).createAnimationAction(Math.max(((Animation)this).getRepeatCount() - 1, 0)),
                    ((AnimationEntity)this.entity).getAnimationPeriod());
         }
      }
      else{
         ((ActionEntity)this.entity).executeActivity(((Activity)this).getWorld(), ((Activity)this).getImageStore(), scheduler);
      }
   }
}