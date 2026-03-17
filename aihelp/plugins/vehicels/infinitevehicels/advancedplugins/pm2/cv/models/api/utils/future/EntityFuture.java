package advancedplugins.pm2.cv.models.api.utils.future;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import org.bukkit.entity.Entity;

public class EntityFuture<T> extends AbstractFoliaFuture<T> {
   private final Entity entity;

   private EntityFuture(Entity var1) {
      this.entity = var1;
   }

   private EntityFuture(Entity var1, T var2) {
      super(var2);
      this.entity = var1;
   }

   static <T> EntityFuture<T> empty(Entity var0) {
      return new EntityFuture(var0);
   }

   static <T> EntityFuture<T> completed(Entity var0, T var1) {
      return new EntityFuture(var0, var1);
   }

   protected <U> AbstractFuture<U> createEmpty() {
      return empty(this.entity);
   }

   protected void executeSync(Runnable var1) {
      this.entity.getScheduler().execute(ModelAPI.PLUGIN, var1, (Runnable)null, 0L);
   }

   protected void executeSync(Runnable var1, int var2) {
      this.entity.getScheduler().execute(ModelAPI.PLUGIN, var1, (Runnable)null, (long)var2);
   }
}
