package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAttributeModifiers;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEnchantments;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemLore;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemRarity;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;

public class StaticComponentMap implements IComponentMap {
   public static final StaticComponentMap EMPTY = new StaticComponentMap(Collections.emptyMap());
   @ApiStatus.Obsolete
   public static final StaticComponentMap SHARED_ITEM_COMPONENTS;
   private final boolean empty;
   private final Map<ComponentType<?>, ?> delegate;

   public StaticComponentMap(Map<ComponentType<?>, ?> delegate) {
      this.empty = delegate.isEmpty();
      this.delegate = this.empty ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap(delegate));
   }

   public static StaticComponentMap.Builder builder() {
      return new StaticComponentMap.Builder();
   }

   public boolean has(ComponentType<?> type) {
      return !this.empty && this.delegate.containsKey(type);
   }

   @Nullable
   public <T> T get(ComponentType<T> type) {
      return this.empty ? null : this.delegate.get(type);
   }

   public <T> void set(ComponentType<T> type, Optional<T> value) {
      throw new UnsupportedOperationException();
   }

   public StaticComponentMap merge(StaticComponentMap prioritizedMap) {
      return builder().setAll(this).setAll(prioritizedMap).build();
   }

   public Map<ComponentType<?>, ?> getDelegate() {
      return this.delegate;
   }

   public boolean isEmpty() {
      return this.empty;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof StaticComponentMap)) {
         return false;
      } else {
         StaticComponentMap that = (StaticComponentMap)obj;
         return this.delegate.equals(that.delegate);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.delegate);
   }

   public String toString() {
      return "Components" + this.delegate;
   }

   static {
      SHARED_ITEM_COMPONENTS = builder().set(ComponentTypes.MAX_STACK_SIZE, (int)64).set(ComponentTypes.LORE, (Object)ItemLore.EMPTY).set(ComponentTypes.ENCHANTMENTS, (Object)ItemEnchantments.EMPTY).set(ComponentTypes.REPAIR_COST, (int)0).set(ComponentTypes.ATTRIBUTE_MODIFIERS, (Object)ItemAttributeModifiers.EMPTY).set(ComponentTypes.RARITY, (Object)ItemRarity.COMMON).build();
   }

   public static class Builder {
      private final Map<ComponentType<?>, Object> map = new HashMap();

      public StaticComponentMap build() {
         return new StaticComponentMap(this.map);
      }

      public StaticComponentMap.Builder setAll(StaticComponentMap.Builder map) {
         return this.setAll(map.map);
      }

      public StaticComponentMap.Builder setAll(StaticComponentMap map) {
         return this.setAll(map.getDelegate());
      }

      public StaticComponentMap.Builder setAll(Map<ComponentType<?>, ?> map) {
         Iterator var2 = map.entrySet().iterator();

         while(var2.hasNext()) {
            Entry<ComponentType<?>, ?> entry = (Entry)var2.next();
            this.set((ComponentType)entry.getKey(), entry.getValue());
         }

         return this;
      }

      public <T> StaticComponentMap.Builder set(ComponentType<T> type, Optional<T> value) {
         return this.set(type, value.orElse((Object)null));
      }

      public <T> StaticComponentMap.Builder set(ComponentType<T> type, @Nullable T value) {
         if (value == null) {
            this.map.remove(type);
         } else {
            this.map.put(type, value);
         }

         return this;
      }
   }
}
