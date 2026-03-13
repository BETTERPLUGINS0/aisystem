package org.terraform.v1_21_R7;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.vehicle.minecart.MinecartChest;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces.MonumentBuilding;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
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
   private final ChunkAccess ica;
   private final int chunkX;
   private final int chunkZ;
   private final ServerLevel ws;
   private final TerraformWorld tw;

   public PopulatorDataICA(PopulatorDataAbstract parent, TerraformWorld tw, ServerLevel ws, ChunkAccess ica, int chunkX, int chunkZ) {
      this.ica = ica;
      this.parent = parent;
      this.chunkX = chunkX;
      this.chunkZ = chunkZ;
      this.ws = ws;
      this.tw = tw;
   }

   @NotNull
   public Material getType(int x, int y, int z) {
      BlockState ibd = this.ica.getBlockState(new BlockPos(x, y, z));
      return CraftBlockData.fromData(ibd).getMaterial();
   }

   public BlockData getBlockData(int x, int y, int z) {
      BlockState ibd = this.ica.getBlockState(new BlockPos(x, y, z));
      return CraftBlockData.fromData(ibd);
   }

   public void setBiome(int rawX, int rawY, int rawZ, CustomBiomeType cbt, Biome fallback) {
      Registry<net.minecraft.world.level.biome.Biome> biomeRegistry = CustomBiomeHandler.getBiomeRegistry();
      Holder targetBiome;
      if (cbt == CustomBiomeType.NONE) {
         targetBiome = CraftBiome.bukkitToMinecraftHolder(fallback);
      } else {
         ResourceKey<net.minecraft.world.level.biome.Biome> rkey = (ResourceKey)CustomBiomeHandler.terraformGenBiomeRegistry.get(cbt);
         Optional<Reference<net.minecraft.world.level.biome.Biome>> optHolder = biomeRegistry.get(rkey);
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
      this.ica.setBlockState(new BlockPos(x, y, z), ((CraftBlockData)Bukkit.createBlockData(type)).getState(), 3);
   }

   public void setBlockData(int x, int y, int z, @NotNull BlockData data) {
      this.ica.setBlockState(new BlockPos(x, y, z), ((CraftBlockData)data).getState(), 3);
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
      BlockPos pos = new BlockPos(x, y, z);
      BlockEntity te = this.ica.getBlockEntity(pos);
      if (te instanceof RandomizableContainerBlockEntity) {
         RandomizableContainerBlockEntity rcb = (RandomizableContainerBlockEntity)te;
         rcb.setLootTable((ResourceKey)LootTableTranslator.translationMap.get(table));
      } else if (te instanceof BrushableBlockEntity) {
         BrushableBlockEntity bbe = (BrushableBlockEntity)te;
         bbe.setLootTable((ResourceKey)LootTableTranslator.translationMap.get(table), this.tw.getHashedRand((long)x, y, z).nextLong());
      }

   }

   public void registerNaturalSpawns(@NotNull NaturalSpawnType type, int x0, int y0, int z0, int x1, int y1, int z1) {
      ResourceKey var10000;
      switch(type) {
      case GUARDIAN:
         var10000 = BuiltinStructures.OCEAN_MONUMENT;
         break;
      case PILLAGER:
         var10000 = BuiltinStructures.PILLAGER_OUTPOST;
         break;
      case WITCH:
         var10000 = BuiltinStructures.SWAMP_HUT;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      ResourceKey<Structure> structureKey = var10000;
      Registry<Structure> featureRegistry = (Registry)MinecraftServer.getServer().registryAccess().lookup(Registries.STRUCTURE).orElseThrow();
      Structure structureFeature = (Structure)featureRegistry.getValue(structureKey);

      try {
         Class<MonumentBuilding> oceanMonumentPiecesHClass = MonumentBuilding.class;
         StructurePiece customBoundPiece = (StructurePiece)oceanMonumentPiecesHClass.getConstructor(RandomSource.class, Integer.TYPE, Integer.TYPE, Direction.class).newInstance(RandomSource.create(), x0, z0, Direction.DOWN);
         PiecesContainer container = new PiecesContainer(List.of(customBoundPiece));
         StructureStart start = new StructureStart(structureFeature, new ChunkPos(this.chunkX, this.chunkZ), 0, container);
         TerraformFieldHandler cachedBoundingBox = new TerraformFieldHandler(StructureStart.class, new String[]{"cachedBoundingBox", "h"});
         cachedBoundingBox.field.set(start, new BoundingBox(x0, y0, z0, x1, y1, z1));
         this.ica.setStartForStructure(structureFeature, start);
         this.ica.addReferenceForStructure(structureFeature, (new ChunkPos(this.chunkX, this.chunkZ)).toLong());
      } catch (InstantiationException | InvocationTargetException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException var16) {
         TerraformGeneratorPlugin.logger.stackTrace(var16);
      }

   }

   public void spawnMinecartWithChest(int x, int y, int z, TerraLootTable table, @NotNull Random random) {
      MinecartChest minecartChest = (MinecartChest)net.minecraft.world.entity.EntityType.CHEST_MINECART.create(this.ws.getMinecraftWorld(), EntitySpawnReason.CHUNK_GENERATION);
      if (minecartChest != null) {
         minecartChest.setPos((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F));
         minecartChest.setLootTable((ResourceKey)LootTableTranslator.translationMap.get(table), random.nextLong());
         this.ws.addFreshEntity(minecartChest);
      }

   }

   @NotNull
   public TerraformWorld getTerraformWorld() {
      return this.tw;
   }
}
