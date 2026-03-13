package com.dfsek.terra.registry;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.lib.google.common.collect.ListMultimap;
import com.dfsek.terra.lib.google.common.collect.Multimaps;
import java.lang.reflect.AnnotatedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class OpenRegistryImpl<T> implements OpenRegistry<T> {
   private static final OpenRegistryImpl.Entry<?> NULL = new OpenRegistryImpl.Entry((Object)null);
   private final Map<RegistryKey, OpenRegistryImpl.Entry<T>> objects;
   private final ListMultimap<String, Pair<RegistryKey, OpenRegistryImpl.Entry<T>>> objectIDs;
   private final TypeKey<T> typeKey;

   public OpenRegistryImpl(TypeKey<T> typeKey) {
      this(new HashMap(), typeKey);
   }

   protected OpenRegistryImpl(Map<RegistryKey, OpenRegistryImpl.Entry<T>> init, TypeKey<T> typeKey) {
      this.objectIDs = Multimaps.newListMultimap(new HashMap(), ArrayList::new);
      this.objects = init;
      this.typeKey = typeKey;
   }

   public T load(@NotNull AnnotatedType type, @NotNull Object o, @NotNull ConfigLoader configLoader, DepthTracker depthTracker) throws LoadException {
      return this.getByID((String)o).orElseThrow(() -> {
         String var10002 = type.getType().getTypeName();
         return new LoadException("No such " + var10002 + " matching \"" + String.valueOf(o) + "\" was found in this registry. Registry contains items: " + this.getItemsFormatted(), depthTracker);
      });
   }

   private String getItemsFormatted() {
      return this.objects.isEmpty() ? "[ ]" : (String)this.objects.keySet().stream().map(RegistryKey::toString).sorted().reduce("", (a, b) -> {
         return a + "\n - " + b;
      });
   }

   public boolean register(@NotNull RegistryKey identifier, @NotNull T value) {
      return this.register(identifier, new OpenRegistryImpl.Entry(value));
   }

   public void registerChecked(@NotNull RegistryKey identifier, @NotNull T value) throws DuplicateEntryException {
      if (this.objects.containsKey(identifier)) {
         throw new DuplicateEntryException("Value with identifier \"" + String.valueOf(identifier) + "\" is already defined in registry.");
      } else {
         this.register(identifier, value);
      }
   }

   public void clear() {
      this.objects.clear();
      this.objectIDs.clear();
   }

   private boolean register(RegistryKey identifier, OpenRegistryImpl.Entry<T> value) {
      boolean exists = this.objects.containsKey(identifier);
      this.objects.put(identifier, value);
      this.objectIDs.put(identifier.getID(), Pair.of(identifier, value));
      return exists;
   }

   public Optional<T> get(@NotNull RegistryKey key) {
      return Optional.ofNullable(((OpenRegistryImpl.Entry)this.objects.getOrDefault(key, NULL)).getValue());
   }

   public boolean contains(@NotNull RegistryKey key) {
      return this.objects.containsKey(key);
   }

   public void forEach(@NotNull Consumer<T> consumer) {
      this.objects.forEach((id, obj) -> {
         consumer.accept(obj.getRaw());
      });
   }

   public void forEach(@NotNull BiConsumer<RegistryKey, T> consumer) {
      this.objects.forEach((id, entry) -> {
         consumer.accept(id, entry.getRaw());
      });
   }

   @NotNull
   public Collection<T> entries() {
      return (Collection)this.objects.values().stream().map(OpenRegistryImpl.Entry::getRaw).collect(Collectors.toList());
   }

   @NotNull
   public Set<RegistryKey> keys() {
      return this.objects.keySet();
   }

   public TypeKey<T> getType() {
      return this.typeKey;
   }

   public Map<RegistryKey, T> getMatches(String id) {
      return (Map)this.objectIDs.get(id).stream().collect(HashMap::new, (map, pair) -> {
         map.put((RegistryKey)pair.getLeft(), ((OpenRegistryImpl.Entry)pair.getRight()).getValue());
      }, Map::putAll);
   }

   public Map<RegistryKey, T> getDeadEntries() {
      Map<RegistryKey, T> dead = new HashMap();
      this.objects.forEach((id, entry) -> {
         if (entry.dead()) {
            dead.put(id, entry.value);
         }

      });
      return dead;
   }

   private static final class Entry<T> {
      private final T value;
      private final AtomicInteger access = new AtomicInteger(0);

      public Entry(T value) {
         this.value = value;
      }

      public boolean dead() {
         return this.access.get() == 0;
      }

      public T getValue() {
         this.access.incrementAndGet();
         return this.value;
      }

      private T getRaw() {
         return this.value;
      }
   }
}
