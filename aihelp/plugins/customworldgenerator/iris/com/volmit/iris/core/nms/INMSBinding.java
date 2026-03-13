package com.volmit.iris.core.nms;

import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.core.nms.container.BiomeColor;
import com.volmit.iris.core.nms.container.BlockProperty;
import com.volmit.iris.core.nms.container.StructurePlacement;
import com.volmit.iris.core.nms.datapack.DataVersion;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.platform.PlatformChunkGenerator;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.math.Vector3d;
import com.volmit.iris.util.nbt.mca.palette.MCABiomeContainer;
import com.volmit.iris.util.nbt.mca.palette.MCAPaletteAccess;
import com.volmit.iris.util.nbt.tag.CompoundTag;
import java.awt.Color;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.inventory.ItemStack;

public interface INMSBinding {
   boolean hasTile(Material material);

   boolean hasTile(Location l);

   KMap<String, Object> serializeTile(Location location);

   void deserializeTile(KMap<String, Object> s, Location newPosition);

   CompoundTag serializeEntity(Entity location);

   Entity deserializeEntity(CompoundTag s, Location newPosition);

   boolean supportsCustomHeight();

   Object getBiomeBaseFromId(int id);

   int getMinHeight(World world);

   boolean supportsCustomBiomes();

   int getTrueBiomeBaseId(Object biomeBase);

   Object getTrueBiomeBase(Location location);

   String getTrueBiomeBaseKey(Location location);

   Object getCustomBiomeBaseFor(String mckey);

   Object getCustomBiomeBaseHolderFor(String mckey);

   int getBiomeBaseIdForKey(String key);

   String getKeyForBiomeBase(Object biomeBase);

   Object getBiomeBase(World world, Biome biome);

   Object getBiomeBase(Object registry, Biome biome);

   KList<Biome> getBiomes();

   boolean isBukkit();

   int getBiomeId(Biome biome);

   MCABiomeContainer newBiomeContainer(int min, int max, int[] data);

   MCABiomeContainer newBiomeContainer(int min, int max);

   default World createWorld(WorldCreator c) {
      ChunkGenerator var3 = c.generator();
      if (var3 instanceof PlatformChunkGenerator) {
         PlatformChunkGenerator gen = (PlatformChunkGenerator)var3;
         if (this.missingDimensionTypes(gen.getTarget().getDimension().getDimensionTypeKey())) {
            throw new IllegalStateException("Missing dimension types to create world");
         }
      }

      return c.createWorld();
   }

   int countCustomBiomes();

   void forceBiomeInto(int x, int y, int z, Object somethingVeryDirty, BiomeGrid chunk);

   default boolean supportsDataPacks() {
      return false;
   }

   MCAPaletteAccess createPalette();

   void injectBiomesFromMantle(Chunk e, Mantle mantle);

   ItemStack applyCustomNbt(ItemStack itemStack, KMap<String, Object> customNbt) throws IllegalArgumentException;

   void inject(long seed, Engine engine, World world) throws NoSuchFieldException, IllegalAccessException;

   Vector3d getBoundingbox(EntityType entity);

   Entity spawnEntity(Location location, EntityType type, SpawnReason reason);

   Color getBiomeColor(Location location, BiomeColor type);

   default DataVersion getDataVersion() {
      return DataVersion.V1_19_2;
   }

   default int getSpawnChunkCount(World world) {
      return 441;
   }

   KList<String> getStructureKeys();

   boolean missingDimensionTypes(String... keys);

   default boolean injectBukkit() {
      return true;
   }

   KMap<Material, List<BlockProperty>> getBlockProperties();

   void placeStructures(Chunk chunk);

   KMap<Identifier, StructurePlacement> collectStructures();
}
