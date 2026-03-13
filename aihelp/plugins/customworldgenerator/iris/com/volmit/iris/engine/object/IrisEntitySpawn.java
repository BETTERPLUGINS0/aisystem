package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.math.Vector3d;
import com.volmit.iris.util.matter.MatterMarker;
import com.volmit.iris.util.matter.slices.MarkerMatter;
import lombok.Generated;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;

@Snippet("entity-spawn")
@Desc("Represents an entity spawn during initial chunk generation")
public class IrisEntitySpawn implements IRare {
   private final transient AtomicCache<RNG> rng = new AtomicCache();
   private final transient AtomicCache<IrisEntity> ent = new AtomicCache();
   @RegistryListResource(IrisEntity.class)
   @Required
   @Desc("The entity")
   private String entity = "";
   @Desc("The energy multiplier when calculating spawn energy usage")
   private double energyMultiplier = 1.0D;
   @MinNumber(1.0D)
   @Desc("The 1 in RARITY chance for this entity to spawn")
   private int rarity = 1;
   @MinNumber(1.0D)
   @Desc("The minumum of this entity to spawn")
   private int minSpawns = 1;
   @MinNumber(1.0D)
   @Desc("The max of this entity to spawn")
   private int maxSpawns = 1;
   private transient IrisSpawner referenceSpawner;
   private transient IrisMarker referenceMarker;

   public int spawn(Engine gen, Chunk c, RNG rng) {
      int var4 = this.minSpawns == this.maxSpawns ? this.minSpawns : var3.i(Math.min(this.minSpawns, this.maxSpawns), Math.max(this.minSpawns, this.maxSpawns));
      int var5 = 0;
      if (var4 > 0) {
         for(int var6 = 0; var6 < var4; ++var6) {
            int var7 = var2.getX() * 16 + var3.i(15);
            int var8 = var2.getZ() * 16 + var3.i(15);
            int var9 = var1.getHeight(var7, var8, true) + (var1.getWorld().tryGetRealWorld() ? var1.getWorld().realWorld().getMinHeight() : -64);
            int var10 = var1.getHeight(var7, var8, false) + (var1.getWorld().tryGetRealWorld() ? var1.getWorld().realWorld().getMinHeight() : -64);
            Location var10000;
            switch(this.getReferenceSpawner().getGroup()) {
            case NORMAL:
               var10000 = new Location(var2.getWorld(), (double)var7, (double)(var10 + 1), (double)var8);
               break;
            case CAVE:
               var10000 = (Location)var1.getMantle().findMarkers(var2.getX(), var2.getZ(), MarkerMatter.CAVE_FLOOR).convert((var1x) -> {
                  return var1x.toLocation(var2.getWorld()).add(0.0D, 1.0D, 0.0D);
               }).getRandom(var3);
               break;
            case UNDERWATER:
            case BEACH:
               var10000 = new Location(var2.getWorld(), (double)var7, (double)var3.i(var9 + 1, var10), (double)var8);
               break;
            default:
               throw new MatchException((String)null, (Throwable)null);
            }

            Location var11 = var10000;
            if (var11 != null) {
               if (!(this.referenceSpawner.getAllowedLightLevels().getMin() > 0.0D) && !(this.referenceSpawner.getAllowedLightLevels().getMax() < 15.0D)) {
                  if (this.spawn100(var1, var11) != null) {
                     ++var5;
                  }
               } else if (this.referenceSpawner.getAllowedLightLevels().contains(var11.getBlock().getLightLevel()) && this.spawn100(var1, var11) != null) {
                  ++var5;
               }
            }
         }
      }

      return var5;
   }

   public int spawn(Engine gen, IrisPosition c, RNG rng) {
      int var4 = this.minSpawns == this.maxSpawns ? this.minSpawns : var3.i(Math.min(this.minSpawns, this.maxSpawns), Math.max(this.minSpawns, this.maxSpawns));
      int var5 = 0;
      if (!var1.getWorld().tryGetRealWorld()) {
         return 0;
      } else {
         World var6 = var1.getWorld().realWorld();
         if (var4 > 0) {
            if (this.referenceMarker != null && this.referenceMarker.shouldExhaust()) {
               var1.getMantle().getMantle().remove(var2.getX(), var2.getY() - var1.getWorld().minHeight(), var2.getZ(), MatterMarker.class);
            }

            for(int var7 = 0; var7 < var4; ++var7) {
               Location var8 = var2.toLocation(var6).add(0.0D, 1.0D, 0.0D);
               if (!(this.referenceSpawner.getAllowedLightLevels().getMin() > 0.0D) && !(this.referenceSpawner.getAllowedLightLevels().getMax() < 15.0D)) {
                  if (this.spawn100(var1, var8, true) != null) {
                     ++var5;
                  }
               } else if (this.referenceSpawner.getAllowedLightLevels().contains(var8.getBlock().getLightLevel()) && this.spawn100(var1, var8, true) != null) {
                  ++var5;
               }
            }
         }

         return var5;
      }
   }

   public IrisEntity getRealEntity(Engine g) {
      return (IrisEntity)this.ent.aquire(() -> {
         return (IrisEntity)var1.getData().getEntityLoader().load(this.getEntity());
      });
   }

   public Entity spawn(Engine g, Location at) {
      if (this.getRealEntity(var1) == null) {
         return null;
      } else {
         return ((RNG)this.rng.aquire(() -> {
            return new RNG(var1.getSeedManager().getEntity());
         })).i(1, this.getRarity()) == 1 ? this.spawn100(var1, var2) : null;
      }
   }

   private Entity spawn100(Engine g, Location at) {
      return this.spawn100(var1, var2, false);
   }

   private Entity spawn100(Engine g, Location at, boolean ignoreSurfaces) {
      try {
         IrisEntity var4 = this.getRealEntity(var1);
         if (var4 == null) {
            Iris.debug("      You are trying to spawn an entity that does not exist!");
            return null;
         } else if (!var3 && !var4.getSurface().matches(var2.clone().subtract(0.0D, 1.0D, 0.0D).getBlock())) {
            return null;
         } else {
            Vector3d var5 = INMS.get().getBoundingbox(var4.getType());
            if (!var3 && var5 != null) {
               boolean var6 = this.isAreaClearForSpawn(var2, var5);
               if (!var6) {
                  return null;
               }
            }

            Entity var8 = var4.spawn(var1, var2.add(0.5D, 0.5D, 0.5D), (RNG)this.rng.aquire(() -> {
               return new RNG(var1.getSeedManager().getEntity());
            }));
            if (var8 != null) {
               String var10000 = String.valueOf(C.DARK_AQUA);
               Iris.debug("Spawned " + var10000 + "Entity<" + this.getEntity() + "> " + String.valueOf(C.GREEN) + String.valueOf(var8.getType()) + String.valueOf(C.LIGHT_PURPLE) + " @ " + String.valueOf(C.GRAY) + var8.getLocation().getX() + ", " + var8.getLocation().getY() + ", " + var8.getLocation().getZ());
            }

            return var8;
         }
      } catch (Throwable var7) {
         Iris.reportError(var7);
         var7.printStackTrace();
         Iris.error("      Failed to retrieve real entity @ " + String.valueOf(var2) + " (entity: " + this.getEntity() + ")");
         return null;
      }
   }

   private boolean isAreaClearForSpawn(Location center, Vector3d boundingBox) {
      World var3 = var1.getWorld();
      int var4 = var1.getBlockX() - (int)(var2.x / 2.0D);
      int var5 = var1.getBlockX() + (int)(var2.x / 2.0D);
      int var6 = var1.getBlockY();
      int var7 = var1.getBlockY() + (int)var2.y;
      int var8 = var1.getBlockZ() - (int)(var2.z / 2.0D);
      int var9 = var1.getBlockZ() + (int)(var2.z / 2.0D);

      for(int var10 = var4; var10 <= var5; ++var10) {
         for(int var11 = var6; var11 <= var7; ++var11) {
            for(int var12 = var8; var12 <= var9; ++var12) {
               if (var3.getBlockAt(var10, var11, var12).getType() != Material.AIR) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   @Generated
   public IrisEntitySpawn() {
   }

   @Generated
   public IrisEntitySpawn(final String entity, final double energyMultiplier, final int rarity, final int minSpawns, final int maxSpawns, final IrisSpawner referenceSpawner, final IrisMarker referenceMarker) {
      this.entity = var1;
      this.energyMultiplier = var2;
      this.rarity = var4;
      this.minSpawns = var5;
      this.maxSpawns = var6;
      this.referenceSpawner = var7;
      this.referenceMarker = var8;
   }

   @Generated
   public AtomicCache<RNG> getRng() {
      return this.rng;
   }

   @Generated
   public AtomicCache<IrisEntity> getEnt() {
      return this.ent;
   }

   @Generated
   public String getEntity() {
      return this.entity;
   }

   @Generated
   public double getEnergyMultiplier() {
      return this.energyMultiplier;
   }

   @Generated
   public int getRarity() {
      return this.rarity;
   }

   @Generated
   public int getMinSpawns() {
      return this.minSpawns;
   }

   @Generated
   public int getMaxSpawns() {
      return this.maxSpawns;
   }

   @Generated
   public IrisSpawner getReferenceSpawner() {
      return this.referenceSpawner;
   }

   @Generated
   public IrisMarker getReferenceMarker() {
      return this.referenceMarker;
   }

   @Generated
   public IrisEntitySpawn setEntity(final String entity) {
      this.entity = var1;
      return this;
   }

   @Generated
   public IrisEntitySpawn setEnergyMultiplier(final double energyMultiplier) {
      this.energyMultiplier = var1;
      return this;
   }

   @Generated
   public IrisEntitySpawn setRarity(final int rarity) {
      this.rarity = var1;
      return this;
   }

   @Generated
   public IrisEntitySpawn setMinSpawns(final int minSpawns) {
      this.minSpawns = var1;
      return this;
   }

   @Generated
   public IrisEntitySpawn setMaxSpawns(final int maxSpawns) {
      this.maxSpawns = var1;
      return this;
   }

   @Generated
   public IrisEntitySpawn setReferenceSpawner(final IrisSpawner referenceSpawner) {
      this.referenceSpawner = var1;
      return this;
   }

   @Generated
   public IrisEntitySpawn setReferenceMarker(final IrisMarker referenceMarker) {
      this.referenceMarker = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisEntitySpawn)) {
         return false;
      } else {
         IrisEntitySpawn var2 = (IrisEntitySpawn)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getEnergyMultiplier(), var2.getEnergyMultiplier()) != 0) {
            return false;
         } else if (this.getRarity() != var2.getRarity()) {
            return false;
         } else if (this.getMinSpawns() != var2.getMinSpawns()) {
            return false;
         } else if (this.getMaxSpawns() != var2.getMaxSpawns()) {
            return false;
         } else {
            String var3 = this.getEntity();
            String var4 = var2.getEntity();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisEntitySpawn;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getEnergyMultiplier());
      int var6 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      var6 = var6 * 59 + this.getRarity();
      var6 = var6 * 59 + this.getMinSpawns();
      var6 = var6 * 59 + this.getMaxSpawns();
      String var5 = this.getEntity();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getRng());
      return "IrisEntitySpawn(rng=" + var10000 + ", ent=" + String.valueOf(this.getEnt()) + ", entity=" + this.getEntity() + ", energyMultiplier=" + this.getEnergyMultiplier() + ", rarity=" + this.getRarity() + ", minSpawns=" + this.getMinSpawns() + ", maxSpawns=" + this.getMaxSpawns() + ", referenceSpawner=" + String.valueOf(this.getReferenceSpawner()) + ", referenceMarker=" + String.valueOf(this.getReferenceMarker()) + ")";
   }
}
