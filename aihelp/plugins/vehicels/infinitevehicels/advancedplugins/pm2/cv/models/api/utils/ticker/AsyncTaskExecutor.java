package advancedplugins.pm2.cv.models.api.utils.ticker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncTaskExecutor {
   private final ExecutorService executor;
   private final List<CompletableFuture<Void>> futures = new ArrayList();

   public AsyncTaskExecutor() {
      this.executor = Executors.newWorkStealingPool();
   }

   public AsyncTaskExecutor(int var1) {
      this.executor = Executors.newWorkStealingPool(var1);
   }

   public CompletableFuture<Void> run(Runnable var1) {
      CompletableFuture var2 = CompletableFuture.runAsync(var1, this.executor);
      this.futures.add(var2);
      return var2;
   }

   public void join() {
      CompletableFuture.allOf((CompletableFuture[])this.futures.toArray((var0) -> {
         return new CompletableFuture[var0];
      })).join();
      this.futures.clear();
   }
}
