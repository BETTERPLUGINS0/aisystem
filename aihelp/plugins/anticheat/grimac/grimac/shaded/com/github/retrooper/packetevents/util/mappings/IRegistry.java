package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.function.BiFunction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface IRegistry<T extends MappedEntity> extends BiFunction<ClientVersion, Integer, T> {
   default T getByNameOrThrow(ClientVersion version, ResourceLocation name) {
      T value = this.getByName(version, name);
      if (value == null) {
         throw new IllegalArgumentException("Can't resolve '" + name + "' in '" + this.getRegistryKey() + "' for " + version);
      } else {
         return value;
      }
   }

   @Nullable
   default T getByName(ClientVersion version, ResourceLocation name) {
      return this.getByName(version, name.toString());
   }

   default T getByNameOrThrow(ClientVersion version, String name) {
      T value = this.getByName(version, name);
      if (value == null) {
         String normedName = ResourceLocation.normString(name);
         throw new IllegalArgumentException("Can't resolve '" + normedName + "' in '" + this.getRegistryKey() + "' for " + version);
      } else {
         return value;
      }
   }

   @Nullable
   default T getByName(ClientVersion version, String name) {
      return this.getByName(name);
   }

   default T getByNameOrThrow(ResourceLocation name) {
      T value = this.getByName(name);
      if (value == null) {
         throw new IllegalArgumentException("Can't resolve '" + name + "' in '" + this.getRegistryKey() + "'");
      } else {
         return value;
      }
   }

   @Nullable
   default T getByName(ResourceLocation name) {
      return this.getByName(name.toString());
   }

   default T getByNameOrThrow(String name) {
      T value = this.getByName(name);
      if (value == null) {
         String normedName = ResourceLocation.normString(name);
         throw new IllegalArgumentException("Can't resolve '" + normedName + "' in '" + this.getRegistryKey() + "'");
      } else {
         return value;
      }
   }

   @Nullable
   T getByName(String name);

   default T getByIdOrThrow(ClientVersion version, int id) {
      T value = this.getById(version, id);
      if (value == null) {
         throw new IllegalArgumentException("Can't resolve #" + id + " (" + version + ") in '" + this.getRegistryKey() + "'");
      } else {
         return value;
      }
   }

   @Nullable
   T getById(ClientVersion version, int id);

   default int getId(String entityName, ClientVersion version) {
      return this.getId(this.getByNameOrThrow(version, entityName), version);
   }

   int getId(MappedEntity entity, ClientVersion version);

   Collection<T> getEntries();

   int size();

   ResourceLocation getRegistryKey();

   default T apply(ClientVersion version, Integer id) {
      return this.getByIdOrThrow(version, id);
   }
}
