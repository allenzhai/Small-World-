final class Event
{
   private Action action;
   private long time;
   private Active entity;

   public Event(Action action, long time, Active entity)
   {
      this.action = action;
      this.time = time;
      this.entity = entity;
   }

   public Action getAction()
   {
      return this.action;
   }

   public long getTime()
   {
      return this.time;
   }

   public Active getEntity()
   {
      return this.entity;
   }
}
