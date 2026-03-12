package ac.grim.grimac.shaded.incendo.cloud.key;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.INTERNAL
)
public final class SimpleMutableCloudKeyContainer implements MutableCloudKeyContainer {
   private final Map<CloudKey<?>, Object> map;

   public SimpleMutableCloudKeyContainer(final Map<CloudKey<?>, Object> map) {
      this.map = map;
   }

   @NonNull
   public <V> Optional<V> optional(@NonNull final CloudKey<V> key) {
      return Optional.ofNullable(this.map.get(key));
   }

   @NonNull
   public <V> Optional<V> optional(@NonNull final String key) {
      return this.optional(CloudKey.of(key));
   }

   public boolean contains(@NonNull final CloudKey<?> key) {
      return this.map.containsKey(key);
   }

   @NonNull
   public Map<CloudKey<?>, ? extends Object> all() {
      return Collections.unmodifiableMap(this.map);
   }

   public <V> void store(@NonNull final CloudKey<V> key, @NonNull final V value) {
      this.map.put(key, value);
   }

   public <V> void store(@NonNull final String key, @NonNull final V value) {
      this.map.put(CloudKey.of(key), value);
   }

   public void remove(@NonNull final CloudKey<?> key) {
      this.map.remove(key);
   }

   public <V> V computeIfAbsent(@NonNull final CloudKey<V> key, @NonNull final Function<CloudKey<V>, V> defaultFunction) {
      return this.map.computeIfAbsent(key, ($) -> {
         return defaultFunction.apply(key);
      });
   }

   @Nullable
   public <V> V getOrNull(final CloudKey<V> key) {
      return this.map.get(key);
   }
}
