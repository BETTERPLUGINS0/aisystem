package org.terraform.v1_18_R2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.vehicle.EntityMinecartChest;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.block.entity.TileEntityLootable;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.IStructureAccess;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.WorldGenMonumentPieces.WorldGenMonumentPiece1;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.storage.loot.LootTables;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.coregen.NaturalSpawnType;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.coregen.populatordata.PopulatorDataICABiomeWriterAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;

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
      DedicatedServer dedicatedserver = ((CraftServer)Bukkit.getServer()).getServer();
      IRegistry<BiomeBase> biomeRegistry = dedicatedserver.aU().b(IRegistry.aP);
      Holder targetBiome;
      if (cbt == CustomBiomeType.NONE) {
         targetBiome = CraftBlock.biomeToBiomeBase(this.ica.biomeRegistry, fallback);
      } else {
         ResourceKey<BiomeBase> rkey = (ResourceKey)CustomBiomeHandler.terraformGenBiomeRegistry.get(cbt);
         targetBiome = biomeRegistry.g(rkey);
         if (targetBiome == null) {
            TerraformGeneratorPlugin.logger.error("Custom biome was not found in the vanilla registry!");
            targetBiome = CraftBlock.biomeToBiomeBase(this.ica.biomeRegistry, fallback);
         }
      }

      this.ica.setBiome(rawX >> 2, rawY >> 2, rawZ >> 2, targetBiome);
   }

   public void setBiome(int rawX, int rawY, int rawZ, Biome biome) {
      this.ica.setBiome(rawX >> 2, rawY >> 2, rawZ >> 2, CraftBlock.biomeToBiomeBase(this.ica.biomeRegistry, biome));
   }

   public void setType(int x, int y, int z, @NotNull Material type) {
      this.ica.a(new BlockPosition(x, y, z), ((CraftBlockData)Bukkit.createBlockData(type)).getState(), false);
   }

   public void setBlockData(int x, int y, int z, @NotNull BlockData data) {
      this.ica.a(new BlockPosition(x, y, z), ((CraftBlockData)data).getState(), false);
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

   public void lootTableChest(int x, int y, int z, @NotNull TerraLootTable table) {
      BlockPosition pos = new BlockPosition(x, y, z);
      TileEntityLootable.a(this.ica, this.tw.getHashedRand((long)x, y, z), pos, this.getLootTable(table));
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
         throw new IncompatibleClassChangeError();
      }

      ResourceKey<StructureFeature<?, ?>> structureKey = var10000;
      CraftServer craftserver = (CraftServer)Bukkit.getServer();
      DedicatedServer dedicatedserver = craftserver.getServer();
      IRegistry<StructureFeature<?, ?>> featureRegistry = dedicatedserver.aU().b(IRegistry.aL);
      StructureFeature<?, ?> structureFeature = (StructureFeature)featureRegistry.a(structureKey);
      final StructurePiece customBoundPiece = new WorldGenMonumentPiece1(new Random(), x0, z0, EnumDirection.a);
      PiecesContainer container = new PiecesContainer(new ArrayList<StructurePiece>() {
         {
            this.add(customBoundPiece);
         }
      });
      StructureStart start = new StructureStart(structureFeature, new ChunkCoordIntPair(this.chunkX, this.chunkZ), 0, container);

      try {
         Field i = StructureStart.class.getDeclaredField("g");
         i.setAccessible(true);
         i.set(start, new StructureBoundingBox(x0, y0, z0, x1, y1, z1));
      } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException var17) {
         TerraformGeneratorPlugin.logger.stackTrace(var17);
      }

      IStructureAccess sa = this.ica;
      sa.a(structureFeature, start);
      sa.a(structureFeature, (new ChunkCoordIntPair(this.chunkX, this.chunkZ)).a());
   }

   public void spawnMinecartWithChest(int x, int y, int z, @NotNull TerraLootTable table, @NotNull Random random) {
      EntityMinecartChest entityminecartchest = new EntityMinecartChest(this.ws.getMinecraftWorld(), (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F));
      entityminecartchest.a(this.getLootTable(table), random.nextLong());
      this.ws.addFreshEntity(entityminecartchest, SpawnReason.CHUNK_GEN);
   }

   @Nullable
   private MinecraftKey getLootTable(@NotNull TerraLootTable table) {
      MinecraftKey var10000;
      switch(table) {
      case EMPTY:
         var10000 = LootTables.a;
         break;
      case SPAWN_BONUS_CHEST:
         var10000 = LootTables.b;
         break;
      case END_CITY_TREASURE:
         var10000 = LootTables.c;
         break;
      case SIMPLE_DUNGEON:
         var10000 = LootTables.d;
         break;
      case VILLAGE_WEAPONSMITH:
         var10000 = LootTables.e;
         break;
      case VILLAGE_TOOLSMITH:
         var10000 = LootTables.f;
         break;
      case VILLAGE_ARMORER:
         var10000 = LootTables.g;
         break;
      case VILLAGE_CARTOGRAPHER:
         var10000 = LootTables.h;
         break;
      case VILLAGE_MASON:
         var10000 = LootTables.i;
         break;
      case VILLAGE_SHEPHERD:
         var10000 = LootTables.j;
         break;
      case VILLAGE_BUTCHER:
         var10000 = LootTables.k;
         break;
      case VILLAGE_FLETCHER:
         var10000 = LootTables.l;
         break;
      case VILLAGE_FISHER:
         var10000 = LootTables.m;
         break;
      case VILLAGE_TANNERY:
         var10000 = LootTables.n;
         break;
      case VILLAGE_TEMPLE:
         var10000 = LootTables.o;
         break;
      case VILLAGE_DESERT_HOUSE:
         var10000 = LootTables.p;
         break;
      case VILLAGE_PLAINS_HOUSE:
         var10000 = LootTables.q;
         break;
      case VILLAGE_TAIGA_HOUSE:
         var10000 = LootTables.r;
         break;
      case VILLAGE_SNOWY_HOUSE:
         var10000 = LootTables.s;
         break;
      case VILLAGE_SAVANNA_HOUSE:
         var10000 = LootTables.t;
         break;
      case ABANDONED_MINESHAFT:
         var10000 = LootTables.u;
         break;
      case NETHER_BRIDGE:
         var10000 = LootTables.v;
         break;
      case STRONGHOLD_LIBRARY:
         var10000 = LootTables.w;
         break;
      case STRONGHOLD_CROSSING:
         var10000 = LootTables.x;
         break;
      case STRONGHOLD_CORRIDOR:
         var10000 = LootTables.y;
         break;
      case DESERT_PYRAMID:
         var10000 = LootTables.z;
         break;
      case JUNGLE_TEMPLE:
         var10000 = LootTables.A;
         break;
      case JUNGLE_TEMPLE_DISPENSER:
         var10000 = LootTables.B;
         break;
      case IGLOO_CHEST:
         var10000 = LootTables.C;
         break;
      case WOODLAND_MANSION:
         var10000 = LootTables.D;
         break;
      case UNDERWATER_RUIN_SMALL:
         var10000 = LootTables.E;
         break;
      case UNDERWATER_RUIN_BIG:
         var10000 = LootTables.F;
         break;
      case BURIED_TREASURE:
         var10000 = LootTables.G;
         break;
      case SHIPWRECK_MAP:
         var10000 = LootTables.H;
         break;
      case SHIPWRECK_SUPPLY:
         var10000 = LootTables.I;
         break;
      case SHIPWRECK_TREASURE:
         var10000 = LootTables.J;
         break;
      case PILLAGER_OUTPOST:
         var10000 = LootTables.K;
         break;
      case RUINED_PORTAL:
         var10000 = LootTables.P;
         break;
      default:
         var10000 = null;
      }

      return var10000;
   }

   @NotNull
   public TerraformWorld getTerraformWorld() {
      return this.tw;
   }
}
