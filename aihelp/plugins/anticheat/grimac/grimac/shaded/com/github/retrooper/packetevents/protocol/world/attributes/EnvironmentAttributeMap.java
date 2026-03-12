package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.modifiers.AttributeModifier;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Either;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class EnvironmentAttributeMap {
   public static final NbtCodec<EnvironmentAttributeMap> CODEC = (new NbtMapCodec<EnvironmentAttributeMap>() {
      public EnvironmentAttributeMap decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
         VersionedRegistry<EnvironmentAttribute<?>> registry = EnvironmentAttributes.getRegistry();
         ClientVersion version = wrapper.getServerVersion().toClientVersion();
         Map<EnvironmentAttribute<?>, EnvironmentAttributeMap.Entry<?, ?>> entries = new HashMap();
         Iterator var6 = compound.getTagNames().iterator();

         while(var6.hasNext()) {
            String tag = (String)var6.next();
            EnvironmentAttribute<?> attribute = (EnvironmentAttribute)registry.getByNameOrThrow(version, tag);
            if (attribute.isSynced()) {
               entries.put(attribute, (EnvironmentAttributeMap.Entry)compound.getOrThrow(tag, EnvironmentAttributeMap.Entry.codec(attribute), wrapper));
            }
         }

         return new EnvironmentAttributeMap(entries);
      }

      public void encode(NBTCompound compound, PacketWrapper<?> wrapper, EnvironmentAttributeMap value) throws NbtCodecException {
         Iterator var4 = value.entries.entrySet().iterator();

         while(var4.hasNext()) {
            java.util.Map.Entry<EnvironmentAttribute<?>, EnvironmentAttributeMap.Entry<?, ?>> entry = (java.util.Map.Entry)var4.next();
            if (((EnvironmentAttribute)entry.getKey()).isSynced()) {
               this.encode0(compound, (EnvironmentAttribute)entry.getKey(), (EnvironmentAttributeMap.Entry)entry.getValue(), wrapper);
            }
         }

      }

      private <T> void encode0(NBTCompound compound, EnvironmentAttribute<?> attribute, EnvironmentAttributeMap.Entry<T, ?> entry, PacketWrapper<?> wrapper) {
         compound.set(attribute.getName().toString(), entry, EnvironmentAttributeMap.Entry.codec(attribute), wrapper);
      }
   }).codec();
   public static final EnvironmentAttributeMap EMPTY = new EnvironmentAttributeMap(Collections.emptyMap());
   private final Map<EnvironmentAttribute<?>, EnvironmentAttributeMap.Entry<?, ?>> entries;

   private EnvironmentAttributeMap(Map<EnvironmentAttribute<?>, EnvironmentAttributeMap.Entry<?, ?>> entries) {
      this.entries = entries;
   }

   public static EnvironmentAttributeMap create() {
      return new EnvironmentAttributeMap(new HashMap());
   }

   public EnvironmentAttributeMap copyImmutable() {
      Map<EnvironmentAttribute<?>, EnvironmentAttributeMap.Entry<?, ?>> entries = new HashMap(this.entries);
      return new EnvironmentAttributeMap(Collections.unmodifiableMap(entries));
   }

   public EnvironmentAttributeMap copyMutable() {
      return new EnvironmentAttributeMap(new HashMap(this.entries));
   }

   public <T> EnvironmentAttributeMap set(EnvironmentAttribute<T> attribute, T value) {
      this.set(attribute, value, AttributeModifier.override());
      return this;
   }

   public <T, A> EnvironmentAttributeMap set(EnvironmentAttribute<T> attribute, A value, AttributeModifier<T, A> modifier) {
      this.entries.put(attribute, new EnvironmentAttributeMap.Entry(value, modifier));
      return this;
   }

   public void setAll(EnvironmentAttributeMap map) {
      this.entries.putAll(map.entries);
   }

   public <T> T getOrDefault(EnvironmentAttribute<T> attribute) {
      return this.apply(attribute, attribute.getDefaultValue());
   }

   @Nullable
   public <T> EnvironmentAttributeMap.Entry<T, ?> get(EnvironmentAttribute<T> attribute) {
      return (EnvironmentAttributeMap.Entry)this.entries.get(attribute);
   }

   public <T> T apply(EnvironmentAttribute<T> attribute, T base) {
      EnvironmentAttributeMap.Entry<T, ?> entry = this.get(attribute);
      return entry != null ? entry.getValue(base) : base;
   }

   public boolean contains(EnvironmentAttribute<?> attribute) {
      return this.entries.containsKey(attribute);
   }

   public Set<EnvironmentAttribute<?>> keySet() {
      return this.entries.keySet();
   }

   public int size() {
      return this.entries.size();
   }

   public boolean isEmpty() {
      return this.entries.isEmpty();
   }

   public boolean equals(Object obj) {
      return !(obj instanceof EnvironmentAttributeMap) ? false : this.entries.equals(((EnvironmentAttributeMap)obj).entries);
   }

   public int hashCode() {
      return Objects.hashCode(this.entries);
   }

   // $FF: synthetic method
   EnvironmentAttributeMap(Map x0, Object x1) {
      this(x0);
   }

   public static final class Entry<T, A> {
      private final A argument;
      private final AttributeModifier<T, A> modifier;

      public Entry(A argument, AttributeModifier<T, A> modifier) {
         this.argument = argument;
         this.modifier = modifier;
      }

      public static <T> EnvironmentAttributeMap.Entry<T, T> createOverride(T value) {
         return new EnvironmentAttributeMap.Entry(value, AttributeModifier.override());
      }

      public static <T> NbtCodec<EnvironmentAttributeMap.Entry<T, ?>> codec(EnvironmentAttribute<T> attribute) {
         NbtCodec<T> valueCodec = attribute.getType().getValueCodec();
         final NbtCodec<AttributeModifier<T, ?>> modifierCodec = attribute.getType().getModifierCodec();
         return NbtCodecs.either(valueCodec, (new NbtMapCodec<EnvironmentAttributeMap.Entry<T, ?>>() {
            public EnvironmentAttributeMap.Entry<T, ?> decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
               AttributeModifier<T, ?> modifier = (AttributeModifier)compound.getOrThrow("modifier", modifierCodec, wrapper);
               Object arg = compound.getOrThrow("argument", modifier.argumentCodec(attribute), wrapper);
               EnvironmentAttributeMap.Entry<T, Object> entry = new EnvironmentAttributeMap.Entry(arg, modifier);
               return entry;
            }

            public void encode(NBTCompound compound, PacketWrapper<?> wrapper, EnvironmentAttributeMap.Entry<T, ?> value) throws NbtCodecException {
               this.encode0(compound, wrapper, value);
            }

            private <A> void encode0(NBTCompound compound, PacketWrapper<?> wrapper, EnvironmentAttributeMap.Entry<T, A> value) throws NbtCodecException {
               compound.set("modifier", value.modifier, modifierCodec, wrapper);
               compound.set("argument", value.argument, value.modifier.argumentCodec(attribute), wrapper);
            }
         }).codec()).apply((e) -> {
            return (EnvironmentAttributeMap.Entry)e.map(EnvironmentAttributeMap.Entry::createOverride, Function.identity());
         }, (e) -> {
            if (e.isOverride()) {
               T arg = e.argument;
               return Either.createLeft(arg);
            } else {
               return Either.createRight(e);
            }
         });
      }

      public boolean isOverride() {
         return this.modifier == AttributeModifier.override();
      }

      public T getValue(T base) {
         return this.modifier.apply(base, this.argument);
      }

      public A getArgument() {
         return this.argument;
      }

      public AttributeModifier<T, A> getModifier() {
         return this.modifier;
      }

      public boolean equals(Object obj) {
         if (obj != null && this.getClass() == obj.getClass()) {
            EnvironmentAttributeMap.Entry<?, ?> entry = (EnvironmentAttributeMap.Entry)obj;
            return this.modifier != entry.modifier ? false : this.argument.equals(entry.argument);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.argument, this.modifier});
      }
   }
}
