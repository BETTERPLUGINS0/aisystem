package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Iterator;
import java.util.Optional;
import java.util.Map.Entry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface IComponentMap {
   static StaticComponentMap decode(NBT nbt, PacketWrapper<?> wrapper, IRegistry<? extends ComponentType<?>> registry) {
      return decode(nbt, wrapper.getServerVersion().toClientVersion(), registry);
   }

   /** @deprecated */
   @Deprecated
   static StaticComponentMap decode(NBT nbt, ClientVersion version, IRegistry<? extends ComponentType<?>> registry) {
      NBTCompound compound = (NBTCompound)nbt;
      StaticComponentMap.Builder components = StaticComponentMap.builder();
      Iterator var5 = compound.getTags().entrySet().iterator();

      while(var5.hasNext()) {
         Entry<String, NBT> entry = (Entry)var5.next();
         ComponentType<?> type = (ComponentType)registry.getByName((String)entry.getKey());
         if (type == null) {
            throw new IllegalStateException("Unknown component type named " + (String)entry.getKey() + " encountered");
         }

         Object value = type.decode((NBT)entry.getValue(), version);
         components.set(type, value);
      }

      return components.build();
   }

   static NBT encode(PacketWrapper<?> wrapper, StaticComponentMap components) {
      return encode(components, wrapper.getServerVersion().toClientVersion());
   }

   /** @deprecated */
   @Deprecated
   static NBT encode(StaticComponentMap components, ClientVersion version) {
      NBTCompound compound = new NBTCompound();
      Iterator var3 = components.getDelegate().entrySet().iterator();

      while(var3.hasNext()) {
         Entry<ComponentType<?>, ?> entry = (Entry)var3.next();
         String key = ((ComponentType)entry.getKey()).getName().toString();
         NBT value = ((ComponentType)entry.getKey()).encode(entry.getValue(), version);
         compound.setTag(key, value);
      }

      return compound;
   }

   default <T> Optional<T> getOptional(ComponentType<T> type) {
      return Optional.ofNullable(this.get(type));
   }

   boolean has(ComponentType<?> type);

   @Contract("_, !null -> !null")
   @Nullable
   default <T> T getOr(ComponentType<T> type, @Nullable T otherValue) {
      T value = this.get(type);
      return value != null ? value : otherValue;
   }

   @Nullable
   <T> T get(ComponentType<T> type);

   default <T> void set(ComponentValue<T> component) {
      this.set(component.getType(), component.getValue());
   }

   default <T> void set(ComponentType<T> type, @Nullable T value) {
      this.set(type, Optional.ofNullable(value));
   }

   default <T> void unset(ComponentType<T> type) {
      this.set(type, Optional.empty());
   }

   <T> void set(ComponentType<T> type, Optional<T> value);
}
