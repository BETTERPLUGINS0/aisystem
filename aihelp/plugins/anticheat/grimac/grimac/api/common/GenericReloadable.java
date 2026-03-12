package ac.grim.grimac.api.common;

import java.util.concurrent.CompletableFuture;

public interface GenericReloadable<T> {
   void reload(T var1);

   default CompletableFuture<Boolean> reloadAsync(T config) {
      try {
         this.reload(config);
         return CompletableFuture.completedFuture(true);
      } catch (Exception var4) {
         CompletableFuture<Boolean> future = new CompletableFuture();
         future.completeExceptionally(var4);
         return future;
      }
   }
}
