package org.terraform.spigot.v1_21_R7;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.Holder.c;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.vehicle.minecart.EntityMinecartChest;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.entity.TileEntityLootable;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces.h;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_21_R7.block.CraftBiome;
import org.bukkit.craftbukkit.v1_21_R7.block.data.CraftBlockData;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.coregen.NaturalSpawnType;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.coregen.populatordata.PopulatorDataICABiomeWriterAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.version.TerraformFieldHandler;

public class PopulatorDataICA extends PopulatorDataICABiomeWriterAbstract {
   private final PopulatorDataAbstract parent;
   private final IChunkAccess ica;
   private final int chunkX;
   private final int chunkZ;
   private final WorldServer ws;
   private final TerraformWorld tw;

   public PopulatorDataICA(PopulatorDataAbstract parent, TerraformWorld tw, WorldServer ws, IChunkAccess ica, int chunkX, int chunkZ) {
      this.ica = ica;
      this.parent = parent;
      this.chunkX = chunkX;
      this.chunkZ = chunkZ;
      this.ws = ws;
      this.tw = tw;
   }

   @NotNull
   public Material getType(int x, int y, int z) {
      IBlockData ibd = this.ica.a_(new BlockPosition(x, y, z));
      return CraftBlockData.fromData(ibd).getMaterial();
   }

   public BlockData getBlockData(int x, int y, int z) {
      IBlockData ibd = this.ica.a_(new BlockPosition(x, y, z));
      return CraftBlockData.fromData(ibd);
   }

   public void setBiome(int rawX, int rawY, int rawZ, CustomBiomeType cbt, Biome fallback) {
      IRegistry<BiomeBase> biomeRegistry = CustomBiomeHandler.getBiomeRegistry();
      Holder targetBiome;
      if (cbt == CustomBiomeType.NONE) {
         targetBiome = CraftBiome.bukkitToMinecraftHolder(fallback);
      } else {
         ResourceKey<BiomeBase> rkey = (ResourceKey)CustomBiomeHandler.terraformGenBiomeRegistry.get(cbt);
         Optional<c<BiomeBase>> optHolder = biomeRegistry.a(rkey);
         if (optHolder.isEmpty()) {
            TerraformGeneratorPlugin.logger.error("Custom biome was not found in the vanilla registry!");
            targetBiome = CraftBiome.bukkitToMinecraftHolder(fallback);
         } else {
            targetBiome = (Holder)optHolder.get();
         }
      }

      this.ica.setBiome(rawX >> 2, rawY >> 2, rawZ >> 2, targetBiome);
   }

   public void setBiome(int rawX, int rawY, int rawZ, Biome biome) {
      this.ica.setBiome(rawX >> 2, rawY >> 2, rawZ >> 2, CraftBiome.bukkitToMinecraftHolder(biome));
   }

   public void setType(int x, int y, int z, @NotNull Material type) {
      this.ica.a(new BlockPosition(x, y, z), ((CraftBlockData)Bukkit.createBlockData(type)).getState(), 3);
   }

   public void setBlockData(int x, int y, int z, @NotNull BlockData data) {
      this.ica.a(new BlockPosition(x, y, z), ((CraftBlockData)data).getState(), 3);
   }

   public Biome getBiome(int rawX, int rawZ) {
      return this.parent.getBiome(rawX, rawZ);
   }

   public int getChunkX() {
      return this.chunkX;
   }

   public int getChunkZ() {
      return this.chunkZ;
   }

   public void addEntity(int rawX, int rawY, int rawZ, EntityType type) {
      this.parent.addEntity(rawX, rawY, rawZ, type);
   }

   public void setSpawner(int rawX, int rawY, int rawZ, EntityType type) {
      this.parent.setSpawner(rawX, rawY, rawZ, type);
   }

   public void lootTableChest(int x, int y, int z, TerraLootTable table) {
      BlockPosition pos = new BlockPosition(x, y, z);
      TileEntity te = this.ica.c_(pos);
      if (te instanceof TileEntityLootable) {
         TileEntityLootable rcb = (TileEntityLootable)te;
         rcb.a((ResourceKey)LootTableTranslator.translationMap.get(table));
      } else if (te instanceof BrushableBlockEntity) {
         BrushableBlockEntity bbe = (BrushableBlockEntity)te;
         bbe.a((ResourceKey)LootTableTranslator.translationMap.get(table), this.tw.getHashedRand((long)x, y, z).nextLong());
      }

   }

   public void registerNaturalSpawns(@NotNull NaturalSpawnType type, int x0, int y0, int z0, int x1, int y1, int z1) {
      ResourceKey var10000;
      switch(type) {
      case GUARDIAN:
         var10000 = BuiltinStructures.l;
         break;
      case PILLAGER:
         var10000 = BuiltinStructures.a;
         break;
      case WITCH:
         var10000 = BuiltinStructures.j;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      ResourceKey<Structure> structureKey = var10000;
      IRegistry<Structure> featureRegistry = (IRegistry)MinecraftServer.getServer().bc().a(Registries.bs).orElseThrow();
      Structure structureFeature = (Structure)featureRegistry.c(structureKey);

      try {
         Class<h> oceanMonumentPiecesHClass = h.class;
         StructurePiece customBoundPiece = (StructurePiece)oceanMonumentPiecesHClass.getConstructor(RandomSource.class, Integer.TYPE, Integer.TYPE, EnumDirection.class).newInstance(RandomSource.a(), x0, z0, EnumDirection.a);
         PiecesContainer container = new PiecesContainer(List.of(customBoundPiece));
         StructureStart start = new StructureStart(structureFeature, new ChunkCoordIntPair(this.chunkX, this.chunkZ), 0, container);
         TerraformFieldHandler cachedBoundingBox = new TerraformFieldHandler(StructureStart.class, new String[]{"cachedBoundingBox", "h"});
         cachedBoundingBox.field.set(start, new StructureBoundingBox(x0, y0, z0, x1, y1, z1));
         this.ica.a(structureFeature, start);
         this.ica.a(structureFeature, (new ChunkCoordIntPair(this.chunkX, this.chunkZ)).b());
      } catch (InstantiationException | InvocationTargetException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException var16) {
         TerraformGeneratorPlugin.logger.stackTrace(var16);
      }

   }

   public void spawnMinecartWithChest(int x, int y, int z, TerraLootTable table, @NotNull Random random) {
      EntityMinecartChest minecartChest = (EntityMinecartChest)EntityTypes.B.a(this.ws.getMinecraftWorld(), EntitySpawnReason.b);
      if (minecartChest != null) {
         minecartChest.a_((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F));
         minecartChest.a((ResourceKey)LootTableTranslator.translationMap.get(table), random.nextLong());
         this.ws.b(minecartChest);
      }

   }

   @NotNull
   public TerraformWorld getTerraformWorld() {
      return this.tw;
   }
}
