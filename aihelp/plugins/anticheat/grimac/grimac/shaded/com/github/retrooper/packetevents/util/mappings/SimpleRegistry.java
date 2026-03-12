package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SimpleRegistry<T extends MappedEntity> implements IRegistry<T> {
   private final ResourceLocation registryKey;
   private final Map<String, T> typeMap;
   private final Map<Integer, T> typeIdMap;
   private final Map<String, Integer> reverseTypeIdMap;

   public SimpleRegistry(String registryKey) {
      this(new ResourceLocation(registryKey));
   }

   public SimpleRegistry(ResourceLocation registryKey) {
      this.typeMap = new HashMap();
      this.typeIdMap = new HashMap();
      this.reverseTypeIdMap = new HashMap();
      this.registryKey = registryKey;
   }

   @ApiStatus.Internal
   public <Z extends T> Z define(String name, int id, Z instance) {
      return this.define(new ResourceLocation(name), id, instance);
   }

   @ApiStatus.Internal
   public <Z extends T> Z define(ResourceLocation name, int id, Z instance) {
      String nameStr = name.toString();
      this.typeMap.put(nameStr, instance);
      this.typeIdMap.put(id, instance);
      this.reverseTypeIdMap.put(nameStr, id);
      return instance;
   }

   @Nullable
   public T getByName(String name) {
      return (MappedEntity)this.typeMap.get(ResourceLocation.normString(name));
   }

   @Nullable
   public T getById(ClientVersion version, int id) {
      return (MappedEntity)this.typeIdMap.get(id);
   }

   public int getId(String entityName, ClientVersion version) {
      String normedName = ResourceLocation.normString(entityName);
      return (Integer)this.reverseTypeIdMap.getOrDefault(normedName, -1);
   }

   public int getId(MappedEntity entity, ClientVersion version) {
      return this.getId(entity.getName().toString(), version);
   }

   public Collection<T> getEntries() {
      return Collections.unmodifiableCollection(this.typeMap.values());
   }

   public int size() {
      return this.typeMap.size();
   }

   public ResourceLocation getRegistryKey() {
      return this.registryKey;
   }

   public String toString() {
      return "SimpleRegistry[" + this.registryKey + ']';
   }
}
