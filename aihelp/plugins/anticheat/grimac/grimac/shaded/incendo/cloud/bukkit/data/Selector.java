package ac.grim.grimac.shaded.incendo.cloud.bukkit.data;

import java.util.Collection;
import java.util.Collections;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE,
   since = "2.0.0"
)
public interface Selector<V> {
   @NonNull
   String inputString();

   @NonNull
   Collection<V> values();

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   public interface Single<V> extends Selector<V> {
      @NonNull
      default Collection<V> values() {
         return Collections.singletonList(this.single());
      }

      @NonNull
      V single();
   }
}
