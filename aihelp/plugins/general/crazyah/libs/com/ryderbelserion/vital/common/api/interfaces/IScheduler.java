package libs.com.ryderbelserion.vital.common.api.interfaces;

import java.util.function.Consumer;

public interface IScheduler {
   void runDelayedTask(Consumer<IScheduler> var1, long var2);

   void runRepeatingTask(Consumer<IScheduler> var1, long var2, long var4);

   void runRepeatingTask(Consumer<IScheduler> var1, long var2);

   void runRepeatingAsyncTask(Consumer<IScheduler> var1, long var2, long var4);

   void runDelayedAsyncTask(Consumer<IScheduler> var1, long var2);
}
