package org.terraform.v1_20_R3;

import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.entity.TileEntityBeehive;
import net.minecraft.world.level.block.entity.TileEntityLootable;
import net.minecraft.world.level.block.entity.TileEntityMobSpawner;
import net.minecraft.world.level.chunk.IChunkAccess;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R3.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_20_R3.generator.CraftLimitedRegion;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_20_R3.util.RandomSourceWrapper;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
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
         Entity e = clr.createEntity(new Location(this.gen.getTerraformWorld().getWorld(), (double)rawX, (double)rawY, (double)rawZ), type.getEntityClass(), true);
         if (e instanceof EntityInsentient) {
            ((EntityInsentient)e).fF();
         }

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

   public void lootTableChest(int x, int y, int z, TerraLootTable table) {
      BlockPosition pos = new BlockPosition(x, y, z);
      TileEntity te = this.rlwa.c_(pos);
      if (te instanceof TileEntityLootable) {
         ((TileEntityLootable)te).a((MinecraftKey)LootTableTranslator.translationMap.get(table));
      } else if (te instanceof BrushableBlockEntity) {
         ((BrushableBlockEntity)te).a((MinecraftKey)LootTableTranslator.translationMap.get(table), this.gen.getTerraformWorld().getHashedRand((long)x, y, z).nextLong());
      }

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
