package com.volmit.iris.core.nms.v1X;

import com.volmit.iris.Iris;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.core.nms.INMSBinding;
import com.volmit.iris.core.nms.container.BiomeColor;
import com.volmit.iris.core.nms.container.BlockProperty;
import com.volmit.iris.core.nms.container.StructurePlacement;
import com.volmit.iris.core.nms.datapack.DataVersion;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.math.Vector3d;
import com.volmit.iris.util.nbt.mca.palette.MCABiomeContainer;
import com.volmit.iris.util.nbt.mca.palette.MCAPaletteAccess;
import com.volmit.iris.util.nbt.tag.CompoundTag;
import java.awt.Color;
import java.util.List;
import java.util.stream.StreamSupport;
import org.bukkit.Chunk;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.inventory.ItemStack;

public class NMSBinding1X implements INMSBinding {
   private static final boolean supportsCustomHeight = testCustomHeight();

   private static boolean testCustomHeight() {
      try {
         if (World.class.getDeclaredMethod("getMaxHeight") != null && World.class.getDeclaredMethod("getMinHeight") != null) {
         }

         return true;
      } catch (Throwable var1) {
         return false;
      }
   }

   public boolean hasTile(Material material) {
      return false;
   }

   public boolean hasTile(Location l) {
      return false;
   }

   public KMap<String, Object> serializeTile(Location location) {
      return null;
   }

   public void deserializeTile(KMap<String, Object> s, Location newPosition) {
   }

   public void injectBiomesFromMantle(Chunk e, Mantle mantle) {
   }

   public ItemStack applyCustomNbt(ItemStack itemStack, KMap<String, Object> customNbt) {
      return var1;
   }

   public void inject(long seed, Engine engine, World world) {
   }

   public Vector3d getBoundingbox() {
      return null;
   }

   public Entity spawnEntity(Location location, EntityType type, SpawnReason reason) {
      return var1.getWorld().spawnEntity(var1, var2);
   }

   public Color getBiomeColor(Location location, BiomeColor type) {
      return Color.GREEN;
   }

   public KList<String> getStructureKeys() {
      List var1 = StreamSupport.stream(Registry.STRUCTURE.spliterator(), false).map(Keyed::getKey).map(NamespacedKey::toString).toList();
      return new KList(var1);
   }

   public boolean missingDimensionTypes(String... keys) {
      return false;
   }

   public KMap<Material, List<BlockProperty>> getBlockProperties() {
      KMap var1 = new KMap();
      Material[] var2 = Material.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Material var5 = var2[var4];
         if (var5.isBlock()) {
            var1.put(var5, List.of());
         }
      }

      return var1;
   }

   public void placeStructures(Chunk chunk) {
   }

   public KMap<Identifier, StructurePlacement> collectStructures() {
      return new KMap();
   }

   public CompoundTag serializeEntity(Entity location) {
      return null;
   }

   public Entity deserializeEntity(CompoundTag s, Location newPosition) {
      return null;
   }

   public boolean supportsCustomHeight() {
      return supportsCustomHeight;
   }

   public Object getBiomeBaseFromId(int id) {
      return null;
   }

   public int getMinHeight(World world) {
      return supportsCustomHeight ? var1.getMinHeight() : 0;
   }

   public boolean supportsCustomBiomes() {
      return false;
   }

   public int getTrueBiomeBaseId(Object biomeBase) {
      return 0;
   }

   public Object getTrueBiomeBase(Location location) {
      return null;
   }

   public String getTrueBiomeBaseKey(Location location) {
      return null;
   }

   public Object getCustomBiomeBaseFor(String mckey) {
      return null;
   }

   public Object getCustomBiomeBaseHolderFor(String mckey) {
      return null;
   }

   public int getBiomeBaseIdForKey(String key) {
      return 0;
   }

   public String getKeyForBiomeBase(Object biomeBase) {
      return null;
   }

   public Object getBiomeBase(World world, Biome biome) {
      return null;
   }

   public Object getBiomeBase(Object registry, Biome biome) {
      return null;
   }

   public KList<Biome> getBiomes() {
      return (new KList(Biome.values())).qdel(Biome.CUSTOM);
   }

   public boolean isBukkit() {
      return true;
   }

   public DataVersion getDataVersion() {
      return DataVersion.UNSUPPORTED;
   }

   public int getBiomeId(Biome biome) {
      return var1.ordinal();
   }

   public MCABiomeContainer newBiomeContainer(int min, int max) {
      Iris.error("Cannot use the custom biome data! Iris is incapable of using MCA generation on this version of minecraft!");
      return null;
   }

   public MCABiomeContainer newBiomeContainer(int min, int max, int[] v) {
      Iris.error("Cannot use the custom biome data! Iris is incapable of using MCA generation on this version of minecraft!");
      return null;
   }

   public int countCustomBiomes() {
      return 0;
   }

   public void forceBiomeInto(int x, int y, int z, Object somethingVeryDirty, BiomeGrid chunk) {
   }

   public Vector3d getBoundingbox(EntityType entity) {
      return null;
   }

   public MCAPaletteAccess createPalette() {
      Iris.error("Cannot use the global data palette! Iris is incapable of using MCA generation on this version of minecraft!");
      return null;
   }
}
