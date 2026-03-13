package org.terraform.v1_19_R3;

import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.entity.TileEntityBeehive;
import net.minecraft.world.level.block.entity.TileEntityLootable;
import net.minecraft.world.level.block.entity.TileEntityMobSpawner;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.storage.loot.LootTables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_19_R3.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_19_R3.generator.CraftLimitedRegion;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_19_R3.util.RandomSourceWrapper;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.bukkit.NativeGeneratorPatcherPopulator;
import org.terraform.coregen.populatordata.IPopulatorDataBaseHeightAccess;
import org.terraform.coregen.populatordata.IPopulatorDataBeehiveEditor;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;

public class PopulatorData extends PopulatorDataAbstract implements IPopulatorDataBaseHeightAccess, IPopulatorDataBeehiveEditor {
   private static final HashMap<EntityType, EntityTypes<?>> entityTypesDict = new HashMap();
   final GeneratorAccessSeed rlwa;
   final IChunkAccess ica;
   private final int chunkX;
   private final int chunkZ;
   private final NMSChunkGenerator gen;
   private int radius = 1;

   public PopulatorData(GeneratorAccessSeed rlwa, IChunkAccess ica, NMSChunkGenerator gen, int chunkX, int chunkZ) {
      this.rlwa = rlwa;
      this.chunkX = chunkX;
      this.chunkZ = chunkZ;
      this.gen = gen;
      this.ica = ica;
      if (entityTypesDict.isEmpty()) {
         EntityType[] var6 = EntityType.values();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            EntityType type = var6[var8];
            if (type != EntityType.ENDER_SIGNAL && type != EntityType.UNKNOWN) {
               try {
                  String var10000 = type.toString();
                  Optional<EntityTypes<?>> et = EntityTypes.a("minecraft:" + var10000.toLowerCase(Locale.ENGLISH));
                  et.ifPresent((entityTypes) -> {
                     entityTypesDict.put(type, entityTypes);
                  });
               } catch (IllegalArgumentException var11) {
                  TerraformGeneratorPlugin.logger.stackTrace(var11);
               }
            }
         }
      }

   }

   public void setRadius(int radius) {
      this.radius = radius;
   }

   @NotNull
   public Material getType(int x, int y, int z) {
      try {
         return CraftMagicNumbers.getMaterial(this.rlwa.a_(new BlockPosition(x, y, z)).b());
      } catch (Exception var5) {
         Bukkit.getLogger().info("Error chunk: " + this.chunkX + "," + this.chunkZ + "--- Block Coords: " + 16 * this.chunkX + "," + 16 * this.chunkZ + " for coords " + x + "," + y + "," + z);
         TerraformGeneratorPlugin.logger.stackTrace(var5);
         return null;
      }
   }

   public BlockData getBlockData(int x, int y, int z) {
      return CraftBlockData.fromData(this.rlwa.a_(new BlockPosition(x, y, z)));
   }

   public void setType(int x, int y, int z, @NotNull Material type) {
      if (Math.abs((x >> 4) - this.chunkX) <= this.radius && Math.abs((z >> 4) - this.chunkZ) <= this.radius) {
         this.rlwa.a(new BlockPosition(x, y, z), ((CraftBlockData)Bukkit.createBlockData(type)).getState(), 0);
      } else if (this.radius > 0) {
         NativeGeneratorPatcherPopulator.pushChange(this.rlwa.getMinecraftWorld().getWorld().getName(), x, y, z, Bukkit.createBlockData(type));
      } else {
         TerraformGeneratorPlugin.logger.stackTrace(new Exception("Tried to call adjacent chunk with populator radius 0: (" + x + "," + y + "," + z + ") for chunk (" + this.chunkX + "," + this.chunkZ + ")"));
      }

   }

   public void setBlockData(int x, int y, int z, @NotNull BlockData data) {
      if (Math.abs((x >> 4) - this.chunkX) <= this.radius && Math.abs((z >> 4) - this.chunkZ) <= this.radius) {
         this.rlwa.a(new BlockPosition(x, y, z), ((CraftBlockData)data).getState(), 0);
      } else if (this.radius > 0) {
         NativeGeneratorPatcherPopulator.pushChange(this.rlwa.getMinecraftWorld().getWorld().getName(), x, y, z, data);
      } else {
         TerraformGeneratorPlugin.logger.stackTrace(new Exception("Tried to call adjacent chunk with populator radius 0: (" + x + "," + y + "," + z + ") for chunk (" + this.chunkX + "," + this.chunkZ + ")"));
      }

   }

   public Biome getBiome(int rawX, int rawZ) {
      TerraformWorld tw = this.gen.getTerraformWorld();
      return tw.getBiomeBank(rawX, rawZ).getHandler().getBiome();
   }

   public int getChunkX() {
      return this.chunkX;
   }

   public int getChunkZ() {
      return this.chunkZ;
   }

   public void addEntity(int rawX, int rawY, int rawZ, @NotNull EntityType type) {
      if (Math.abs((rawX >> 4) - this.chunkX) <= 1 && Math.abs((rawZ >> 4) - this.chunkZ) <= 1) {
         CraftLimitedRegion clr = new CraftLimitedRegion(this.rlwa, this.ica.f());
         Entity e = clr.createEntity(new Location(this.gen.getTerraformWorld().getWorld(), (double)rawX, (double)rawY, (double)rawZ), type.getEntityClass());
         this.rlwa.b(e);
      } else {
         TerraformGeneratorPlugin.logger.info("Failed to spawn " + String.valueOf(type) + " as it was out of bounds.");
      }
   }

   public void setSpawner(int rawX, int rawY, int rawZ, EntityType type) {
      if (TConfig.c.FEATURE_SPAWNERS_ENABLED) {
         BlockPosition pos = new BlockPosition(rawX, rawY, rawZ);
         this.setType(rawX, rawY, rawZ, Material.SPAWNER);
         TileEntity tileentity = this.rlwa.c_(pos);
         if (tileentity instanceof TileEntityMobSpawner) {
            try {
               EntityTypes<?> nmsEntity = (EntityTypes)entityTypesDict.get(type);
               if (nmsEntity == null) {
                  TerraformGeneratorPlugin.logger.error(String.valueOf(type) + " was not present in the entityTypesDict.");
               }

               ((TileEntityMobSpawner)tileentity).a(nmsEntity, new RandomSourceWrapper(new Random()));
            } catch (SecurityException | IllegalArgumentException var8) {
               TerraformGeneratorPlugin.logger.stackTrace(var8);
            }
         } else {
            TerraformGeneratorPlugin.logger.error("Failed to fetch mob spawner entity at (," + rawX + "," + rawY + "," + rawZ + ")");
         }

      }
   }

   public void lootTableChest(int x, int y, int z, @NotNull TerraLootTable table) {
      BlockPosition pos = new BlockPosition(x, y, z);
      TileEntityLootable.a(this.rlwa, RandomSource.a(this.gen.getTerraformWorld().getHashedRand((long)x, y, z).nextLong()), pos, this.getLootTable(table));
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
      case ANCIENT_CITY:
         var10000 = LootTables.P;
         break;
      case ANCIENT_CITY_ICE_BOX:
         var10000 = LootTables.Q;
         break;
      case RUINED_PORTAL:
         var10000 = LootTables.R;
         break;
      default:
         var10000 = null;
      }

      return var10000;
   }

   @NotNull
   public TerraformWorld getTerraformWorld() {
      return this.gen.getTerraformWorld();
   }

   public int getBaseHeight(int rawX, int rawZ) {
      return 100;
   }

   public void setBeehiveWithBee(int rawX, int rawY, int rawZ) {
      BlockPosition pos = new BlockPosition(rawX, rawY, rawZ);
      this.setType(rawX, rawY, rawZ, Material.BEE_NEST);

      try {
         TileEntityBeehive tileentity = (TileEntityBeehive)this.rlwa.c_(pos);
         if (tileentity == null) {
            this.setType(rawX, rawY, rawZ, Material.BEE_NEST);
            tileentity = (TileEntityBeehive)this.rlwa.c_(pos);
         }

         NBTTagCompound nbttagcompound = new NBTTagCompound();
         nbttagcompound.a("id", "minecraft:bee");
         tileentity.a(nbttagcompound, 0, false);
      } catch (IllegalArgumentException | SecurityException | NullPointerException var7) {
         TerraformGeneratorPlugin.logger.stackTrace(var7);
      }

   }
}
