package ac.grim.grimac.platform.api.scheduler;

public interface TaskHandle {
   boolean isSync();

   boolean isCancelled();

   void cancel();
}
