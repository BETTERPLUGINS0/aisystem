package ac.grim.grimac.api;

import java.util.concurrent.CompletableFuture;

public final class GrimAPIProvider {
   private static GrimAbstractAPI instance;
   private static final CompletableFuture<GrimAbstractAPI> futureInstance = new CompletableFuture();

   private GrimAPIProvider() {
   }

   public static void init(GrimAbstractAPI api) {
      if (instance == null && !futureInstance.isDone()) {
         instance = api;
         futureInstance.complete(api);
      } else {
         throw new IllegalStateException("GrimAPI is already initialized");
      }
   }

   public static GrimAbstractAPI get() {
      if (instance == null) {
         throw new IllegalStateException("GrimAPI is not loaded. Ensure the Grim mod is installed and initialized.");
      } else {
         return instance;
      }
   }

   public static CompletableFuture<GrimAbstractAPI> getAsync() {
      return instance != null ? CompletableFuture.completedFuture(instance) : futureInstance;
   }
}
