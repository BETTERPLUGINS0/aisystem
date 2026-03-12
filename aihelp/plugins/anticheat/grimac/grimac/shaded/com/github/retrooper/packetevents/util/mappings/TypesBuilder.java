package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer.SequentialNBTReader;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.VersionMapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.VersionRange;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.VisibleForTesting;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ApiStatus.Internal
public class TypesBuilder {
   private final String mapPath;
   private Map<ClientVersion, Map<String, Integer>> entries;
   private VersionMapper versionMapper;
   @Nullable
   VersionedRegistry<?> registry;

   public TypesBuilder(String mapPath, boolean lazy) {
      this.entries = new HashMap();
      this.mapPath = mapPath;
      if (!lazy) {
         this.load();
      }

   }

   public TypesBuilder(String mapPath) {
      this(mapPath, false);
   }

   public void load() {
      if (this.entries == null) {
         this.entries = new HashMap();
      }

      try {
         SequentialNBTReader.Compound rootCompound = MappingHelper.decompress("mappings/" + this.mapPath);

         try {
            rootCompound.skipOne();
            SequentialNBTReader.Compound compound = (SequentialNBTReader.Compound)rootCompound.next().getValue();
            int length = ((NBTNumber)compound.next().getValue()).getAsInt();
            SequentialNBTReader.Compound entries = (SequentialNBTReader.Compound)compound.next().getValue();
            ClientVersion[] versions = new ClientVersion[length];
            Entry<String, NBT> first = entries.next();
            if (((NBT)first.getValue()).getType() == NBTType.LIST) {
               this.loadAsArray(first, entries, versions);
            } else {
               this.loadAsMap(first, entries, versions);
            }

            this.versionMapper = new VersionMapper(versions);
         } catch (Throwable var8) {
            if (rootCompound != null) {
               try {
                  rootCompound.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (rootCompound != null) {
            rootCompound.close();
         }

      } catch (IOException var9) {
         throw new RuntimeException("Unable to load mapping files.", var9);
      }
   }

   private void loadAsArray(final Entry<String, NBT> first, final SequentialNBTReader.Compound entries, final ClientVersion[] versions) {
      ClientVersion start = ClientVersion.valueOf((String)first.getKey());
      versions[0] = start;
      List<String> lastEntries = new ArrayList();
      Iterator var6 = ((SequentialNBTReader.List)first.getValue()).iterator();

      while(var6.hasNext()) {
         NBT entry = (NBT)var6.next();
         lastEntries.add(((NBTString)entry).getValue());
      }

      Consumer<ClientVersion> mapLoader = (versionx) -> {
         Map<String, Integer> map = new HashMap();
         int size = lastEntries.size();

         for(int i = 0; i < size; ++i) {
            map.put((String)lastEntries.get(i), i);
         }

         this.entries.put(versionx, map);
      };
      mapLoader.accept(start);
      int i = 1;
      Iterator var8 = entries.iterator();

      while(var8.hasNext()) {
         Entry<String, NBT> entry = (Entry)var8.next();
         ClientVersion version = ClientVersion.valueOf((String)entry.getKey());
         versions[i++] = version;
         List<ListDiff<String>> diff = MappingHelper.createListDiff((SequentialNBTReader.Compound)entry.getValue());

         for(int j = diff.size() - 1; j >= 0; --j) {
            ((ListDiff)diff.get(j)).applyTo((List)lastEntries);
         }

         mapLoader.accept(version);
      }

   }

   private void loadAsMap(final Entry<String, NBT> first, final SequentialNBTReader.Compound entries, final ClientVersion[] versions) {
      ClientVersion start = ClientVersion.valueOf((String)first.getKey());
      versions[0] = start;
      Map<String, Integer> lastEntries = (Map)StreamSupport.stream(((SequentialNBTReader.Compound)first.getValue()).spliterator(), false).collect(Collectors.toMap(Entry::getKey, (entryx) -> {
         return ((NBTNumber)entryx.getValue()).getAsInt();
      }));
      Consumer<ClientVersion> mapLoader = (versionx) -> {
         Map<String, Integer> map = new HashMap(lastEntries);
         this.entries.put(versionx, map);
      };
      mapLoader.accept(start);
      int i = 1;
      Iterator var8 = entries.iterator();

      while(var8.hasNext()) {
         Entry<String, NBT> entry = (Entry)var8.next();
         ClientVersion version = ClientVersion.valueOf((String)entry.getKey());
         versions[i++] = version;
         List<MapDiff<String, Integer>> diff = MappingHelper.createDiff((SequentialNBTReader.Compound)entry.getValue());
         Iterator var12 = diff.iterator();

         while(var12.hasNext()) {
            MapDiff<String, Integer> d = (MapDiff)var12.next();
            d.applyTo(lastEntries);
         }

         mapLoader.accept(version);
      }

   }

   @Nullable
   public VersionedRegistry<?> getRegistry() {
      return this.registry;
   }

   public ClientVersion[] getVersions() {
      return this.versionMapper.getVersions();
   }

   public ClientVersion[] getReversedVersions() {
      return this.versionMapper.getReversedVersions();
   }

   public int getDataIndex(ClientVersion rawVersion) {
      return this.versionMapper.getIndex(rawVersion);
   }

   public VersionMapper getVersionMapper() {
      return this.versionMapper;
   }

   public void addExtraVersionStep(ClientVersion version) {
      VersionMapper newMapper = this.versionMapper.withExtra(version);
      if (this.versionMapper != newMapper) {
         int baseIndex = this.versionMapper.getIndex(version);
         ClientVersion baseVersion = this.versionMapper.getVersions()[baseIndex];
         this.entries.put(version, (Map)this.entries.get(baseVersion));
         this.versionMapper = newMapper;
      }

   }

   @VisibleForTesting
   public boolean isMappingDataLoaded() {
      return this.entries != null;
   }

   public void unloadFileMappings() {
      this.entries.clear();
      this.entries = null;
   }

   public TypesBuilderData define(String key, VersionRange range) {
      ResourceLocation name = new ResourceLocation(key);
      int[] ids = new int[this.getVersions().length];
      int index = 0;
      ClientVersion[] var6 = this.getVersions();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         ClientVersion v = var6[var8];
         Map<String, Integer> map = (Map)this.entries.get(v);
         if (map.containsKey(key)) {
            int id = (Integer)map.get(key);
            ids[index] = id;
         } else {
            ids[index] = -1;
         }

         ++index;
      }

      return new TypesBuilderData(name, ids, this, range);
   }

   @Nullable
   public Map<ClientVersion, Map<String, Integer>> getEntries() {
      return this.entries;
   }
}
