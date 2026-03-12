package ac.grim.grimac.shaded.incendo.cloud.meta;

import ac.grim.grimac.shaded.geantyref.GenericTypeReflector;
import ac.grim.grimac.shaded.incendo.cloud.key.CloudKey;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public class SimpleCommandMeta extends CommandMeta {
   private final Map<CloudKey<?>, Object> metaMap;

   protected SimpleCommandMeta(@NonNull final Map<CloudKey<?>, Object> metaMap) {
      this.metaMap = Collections.unmodifiableMap(new HashMap(metaMap));
   }

   @NonNull
   public final <V> Optional<V> optional(@NonNull final CloudKey<V> key) {
      Object value = this.metaMap.get(key);
      if (value == null) {
         return Optional.empty();
      } else if (!GenericTypeReflector.isSuperType(key.type().getType(), value.getClass())) {
         throw new IllegalArgumentException("Conflicting argument types between key type of " + key.type().getType().getTypeName() + " and value type of " + value.getClass());
      } else {
         return Optional.of(value);
      }
   }

   @NonNull
   public <V> Optional<V> optional(@NonNull final String key) {
      Object value = this.metaMap.get(CloudKey.of(key));
      return value == null ? Optional.empty() : Optional.of(value);
   }

   public boolean contains(@NonNull final CloudKey<?> key) {
      return this.metaMap.containsKey(key);
   }

   @NonNull
   public final Map<CloudKey<?>, ? extends Object> all() {
      return new HashMap(this.metaMap);
   }

   public final boolean equals(final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         SimpleCommandMeta that = (SimpleCommandMeta)other;
         return Objects.equals(this.metaMap, that.metaMap);
      } else {
         return false;
      }
   }

   public final int hashCode() {
      return Objects.hashCode(this.metaMap);
   }
}
