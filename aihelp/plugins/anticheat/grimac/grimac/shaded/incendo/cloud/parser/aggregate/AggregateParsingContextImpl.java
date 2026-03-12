package ac.grim.grimac.shaded.incendo.cloud.parser.aggregate;

import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.key.CloudKey;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.checkerframework.checker.nullness.qual.NonNull;

final class AggregateParsingContextImpl<C> implements AggregateParsingContext<C> {
   private final Map<CloudKey<?>, Object> storage = new HashMap();
   private final Collection<String> validKeys;

   AggregateParsingContextImpl(@NonNull final AggregateParser<C, ?> parser) {
      this.validKeys = (Collection)parser.components().stream().map(CommandComponent::name).collect(Collectors.toList());
   }

   public <V> void store(@NonNull final CloudKey<V> key, @NonNull final V value) {
      this.storage.put(key, value);
   }

   public <V> void store(@NonNull final String key, @NonNull final V value) {
      this.storage.put(CloudKey.of(key), value);
   }

   public void remove(@NonNull final CloudKey<?> key) {
      this.storage.remove(key);
   }

   public <V> V computeIfAbsent(@NonNull final CloudKey<V> key, @NonNull final Function<CloudKey<V>, V> defaultFunction) {
      return this.storage.computeIfAbsent(key, (k) -> {
         return defaultFunction.apply(k);
      });
   }

   @NonNull
   public <V> Optional<V> optional(@NonNull final CloudKey<V> key) {
      Object value = this.storage.get(key);
      return value != null ? Optional.of(value) : Optional.empty();
   }

   @NonNull
   public <V> Optional<V> optional(@NonNull final String key) {
      Object value = this.storage.get(CloudKey.of(key));
      return value != null ? Optional.of(value) : Optional.empty();
   }

   @NonNull
   public <V> V get(@NonNull final CloudKey<V> key) {
      if (!this.validKeys.contains(key.name())) {
         throw new NullPointerException("No value with the given key has been stored in the context");
      } else {
         Object value = Objects.requireNonNull(this.storage.get(key));
         return value;
      }
   }

   @NonNull
   public <V> V get(@NonNull final String key) {
      if (!this.validKeys.contains(key)) {
         throw new NullPointerException("No value with the given key has been stored in the context");
      } else {
         Object value = Objects.requireNonNull(this.storage.get(CloudKey.of(key)));
         return value;
      }
   }

   public boolean contains(@NonNull final CloudKey<?> key) {
      return this.storage.containsKey(key);
   }

   public boolean contains(@NonNull final String key) {
      return this.storage.containsKey(CloudKey.of(key));
   }

   @NonNull
   public Map<CloudKey<?>, ? extends Object> all() {
      return this.storage;
   }
}
