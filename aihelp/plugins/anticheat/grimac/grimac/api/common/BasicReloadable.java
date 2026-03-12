package ac.grim.grimac.api.common;

import java.util.concurrent.CompletableFuture;

public interface BasicReloadable {
   void reload();

   default boolean isLoadedAsync() {
      return false;
   }

   default CompletableFuture<Boolean> reloadAsync() {
      try {
         this.reload();
         return CompletableFuture.completedFuture(true);
      } catch (Exception var3) {
         CompletableFuture<Boolean> future = new CompletableFuture();
         future.completeExceptionally(var3);
         return future;
      }
   }
}
