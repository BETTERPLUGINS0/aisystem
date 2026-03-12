package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class PatchableComponentMap implements IComponentMap {
   public static final PatchableComponentMap EMPTY = new PatchableComponentMap(Collections.emptyMap(), Collections.emptyMap());
   private final Map<ComponentType<?>, ?> base;
   private final Map<ComponentType<?>, Optional<?>> patches;

   public PatchableComponentMap(StaticComponentMap base) {
      this((Map)base.getDelegate(), new HashMap());
   }

   public PatchableComponentMap(Map<ComponentType<?>, ?> base) {
      this((Map)base, new HashMap());
   }

   public PatchableComponentMap(StaticComponentMap base, Map<ComponentType<?>, Optional<?>> patches) {
      this(base.getDelegate(), patches);
   }

   public PatchableComponentMap(Map<ComponentType<?>, ?> base, Map<ComponentType<?>, Optional<?>> patches) {
      this.base = Collections.unmodifiableMap(new HashMap(base));
      this.patches = patches;
   }

   @Nullable
   public <T> T get(ComponentType<T> type) {
      Optional<?> patched = (Optional)this.patches.get(type);
      return patched != null ? patched.orElse((Object)null) : this.base.get(type);
   }

   public <T> void set(ComponentType<T> type, Optional<T> value) {
      Object baseVal = this.base.get(type);
      T newVal = value.orElse((Object)null);
      if (Objects.equals(baseVal, newVal)) {
         this.patches.remove(type);
      } else {
         this.patches.put(type, value);
      }

   }

   public boolean has(ComponentType<?> type) {
      Optional<?> patched = (Optional)this.patches.get(type);
      return patched != null ? patched.isPresent() : this.base.containsKey(type);
   }

   public PatchableComponentMap copy() {
      return new PatchableComponentMap(this.base, new HashMap(this.patches));
   }

   public Map<ComponentType<?>, ?> getBase() {
      return this.base;
   }

   public Map<ComponentType<?>, Optional<?>> getPatches() {
      return this.patches;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof PatchableComponentMap)) {
         return false;
      } else {
         PatchableComponentMap that = (PatchableComponentMap)obj;
         return !this.base.equals(that.base) ? false : this.patches.equals(that.patches);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.base, this.patches});
   }

   public String toString() {
      return "PatchableComponentMap{base=" + this.base + ", patches=" + this.patches + '}';
   }
}
