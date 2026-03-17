package advancedplugins.pm2.cv.models.api.utils.callback;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class ExecutionCallback<T> {
   private final Function<Collection<T>, T> invoker;
   private final Map<UUID, T> callbacks = Maps.newConcurrentMap();

   public ExecutionCallback(Function<Collection<T>, T> var1) {
      this.invoker = var1;
   }

   public UUID subscribe(T var1) {
      return this.subscribe(UUID.randomUUID(), var1);
   }

   public UUID subscribe(UUID var1, T var2) {
      this.callbacks.put(var1, var2);
      return var1;
   }

   public void unsubscribe(UUID var1) {
      this.callbacks.remove(var1);
   }

   public T invoker() {
      return this.invoker.apply(this.callbacks.values());
   }
}
