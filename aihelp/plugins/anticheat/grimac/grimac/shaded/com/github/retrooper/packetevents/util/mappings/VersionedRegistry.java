package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MapUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.VersionRange;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.VisibleForTesting;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class VersionedRegistry<T extends MappedEntity> implements IRegistry<T> {
   private static final String REGISTRY_MAPPINGS_PREFIX = "registries/";
   private final ResourceLocation registryKey;
   private final TypesBuilder typesBuilder;
   private final ClientVersion[] extraSteps;
   private final Map<String, T>[] typeNames;
   private final Map<Integer, T>[] typeIds;
   private final Set<T> entries;

   public VersionedRegistry(String registry) {
      this(registry);
   }

   public VersionedRegistry(String registry, ClientVersion... extraSteps) {
      this(registry, "registries/" + registry, extraSteps);
   }

   public VersionedRegistry(String registry, String mappingsPath) {
      this(registry, mappingsPath);
   }

   public VersionedRegistry(String registry, String mappingsPath, ClientVersion... extraSteps) {
      this(new ResourceLocation(registry), mappingsPath, extraSteps);
   }

   public VersionedRegistry(ResourceLocation registryKey, String mappingsPath) {
      this(registryKey, mappingsPath);
   }

   public VersionedRegistry(ResourceLocation registryKey, String mappingsPath, ClientVersion... extraSteps) {
      this.entries = new HashSet();
      this.registryKey = registryKey;
      this.typesBuilder = new TypesBuilder(mappingsPath);
      this.extraSteps = extraSteps;
      this.postLoadMappings();
      int versions = this.typesBuilder.getVersionMapper().size();
      this.typeNames = new Map[versions];
      this.typeIds = new Map[versions];
   }

   @ApiStatus.Internal
   public <Z extends T> Z define(String name, Function<TypesBuilderData, Z> builder) {
      return this.define(name, VersionRange.ALL_VERSIONS, builder);
   }

   @ApiStatus.Internal
   public <Z extends T> Z define(String name, VersionRange range, Function<TypesBuilderData, Z> builder) {
      TypesBuilderData typeData = this.typesBuilder.define(name, range);
      Z instance = (MappedEntity)builder.apply(typeData);
      MappingHelper.registerMapping(this.typesBuilder, this.typeNames, this.typeIds, typeData, instance);
      return instance;
   }

   @VisibleForTesting
   @ApiStatus.Internal
   public TypesBuilder getTypesBuilder() {
      return this.typesBuilder;
   }

   @VisibleForTesting
   @ApiStatus.Internal
   public void postLoadMappings() {
      this.typesBuilder.registry = this;
      ClientVersion[] var1 = this.extraSteps;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ClientVersion extraStep = var1[var3];
         this.typesBuilder.addExtraVersionStep(extraStep);
      }

   }

   @ApiStatus.Internal
   public void unloadMappings() {
      this.typesBuilder.unloadFileMappings();
      Map<String, T> lastNameMap = this.typeNames[0];

      for(int i = 1; i < this.typeNames.length; ++i) {
         Map<String, T> nameMap = this.typeNames[i];
         if (MapUtil.isDeepEqual(lastNameMap, nameMap)) {
            this.typeNames[i] = lastNameMap;
         } else {
            lastNameMap = nameMap;
         }
      }

      Set<String> entryNames = new HashSet();

      for(int i = this.typeNames.length - 1; i >= 0; --i) {
         Iterator var4 = this.typeNames[i].entrySet().iterator();

         while(var4.hasNext()) {
            Entry<String, T> entry = (Entry)var4.next();
            if (entryNames.add((String)entry.getKey())) {
               this.entries.add((MappedEntity)entry.getValue());
            }
         }
      }

   }

   @Nullable
   public T getByName(ClientVersion version, ResourceLocation name) {
      int index = this.typesBuilder.getDataIndex(version);
      return (MappedEntity)this.typeNames[index].get(name.toString());
   }

   @Nullable
   public T getByName(ClientVersion version, String name) {
      int index = this.typesBuilder.getDataIndex(version);
      return (MappedEntity)this.typeNames[index].get(ResourceLocation.normString(name));
   }

   @Nullable
   public T getByName(ResourceLocation name) {
      ClientVersion version = PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
      return this.getByName(version, name);
   }

   @Nullable
   public T getByName(String name) {
      ClientVersion version = PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
      return this.getByName(version, name);
   }

   @Nullable
   public T getById(ClientVersion version, int id) {
      int index = this.typesBuilder.getDataIndex(version);
      return (MappedEntity)this.typeIds[index].get(id);
   }

   public int getId(MappedEntity entity, ClientVersion version) {
      return entity.getId(version);
   }

   public Collection<T> getEntries() {
      return Collections.unmodifiableCollection(this.entries);
   }

   public int size() {
      return this.entries.size();
   }

   public ResourceLocation getRegistryKey() {
      return this.registryKey;
   }

   public String toString() {
      return "VersionedRegistry[" + this.registryKey + ']';
   }
}
