package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.plugin.VolmitSender;
import lombok.Generated;
import org.bukkit.World;

@Desc("Represents an entity spawn during initial chunk generation")
public class IrisSpawner extends IrisRegistrant {
   private transient IrisMarker referenceMarker;
   @ArrayType(
      min = 1,
      type = IrisEntitySpawn.class
   )
   @Desc("The entity spawns to add")
   private KList<IrisEntitySpawn> spawns = new KList();
   @ArrayType(
      min = 1,
      type = IrisEntitySpawn.class
   )
   @Desc("The entity spawns to add initially. EXECUTES PER CHUNK!")
   private KList<IrisEntitySpawn> initialSpawns = new KList();
   @Desc("The energy multiplier when calculating spawn energy usage")
   private double energyMultiplier = 1.0D;
   @Desc("This spawner will not spawn in a given chunk if that chunk has more than the defined amount of living entities.")
   private int maxEntitiesPerChunk = 1;
   @Desc("The block of 24 hour time to contain this spawn in.")
   private IrisTimeBlock timeBlock = new IrisTimeBlock();
   @Desc("The block of 24 hour time to contain this spawn in.")
   private IrisWeather weather;
   @Desc("The maximum rate this spawner can fire")
   private IrisRate maximumRate;
   @Desc("The maximum rate this spawner can fire on a specific chunk")
   private IrisRate maximumRatePerChunk;
   @Desc("The light levels this spawn is allowed to run in (0-15 inclusive)")
   private IrisRange allowedLightLevels;
   @Desc("Where should these spawns be placed")
   private IrisSpawnGroup group;

   public boolean isValid(IrisBiome biome) {
      boolean var10000;
      switch(this.group) {
      case NORMAL:
         switch(var1.getInferredType()) {
         case SHORE:
         case SEA:
         case CAVE:
            var10000 = false;
            return var10000;
         case LAND:
            var10000 = true;
            return var10000;
         default:
            throw new MatchException((String)null, (Throwable)null);
         }
      case CAVE:
         var10000 = true;
         return var10000;
      case UNDERWATER:
         switch(var1.getInferredType()) {
         case SHORE:
         case CAVE:
         case LAND:
            var10000 = false;
            return var10000;
         case SEA:
            var10000 = true;
            return var10000;
         default:
            throw new MatchException((String)null, (Throwable)null);
         }
      case BEACH:
         switch(var1.getInferredType()) {
         case SHORE:
            var10000 = true;
            return var10000;
         case SEA:
         case CAVE:
         case LAND:
            var10000 = false;
            return var10000;
         default:
            throw new MatchException((String)null, (Throwable)null);
         }
      default:
         throw new MatchException((String)null, (Throwable)null);
      }
   }

   public boolean isValid(World world) {
      return this.timeBlock.isWithin(var1) && this.weather.is(var1);
   }

   public boolean canSpawn(Engine engine) {
      if (!this.isValid(var1.getWorld().realWorld())) {
         return false;
      } else {
         IrisRate var2 = this.getMaximumRate();
         return var2.isInfinite() || var1.getEngineData().getCooldown(this).canSpawn(var2);
      }
   }

   public boolean canSpawn(Engine engine, int x, int z) {
      if (!this.canSpawn(var1)) {
         return false;
      } else {
         IrisRate var4 = this.getMaximumRatePerChunk();
         return var4.isInfinite() || var1.getEngineData().getChunk(var2, var3).getCooldown(this).canSpawn(var4);
      }
   }

   public void spawn(Engine engine) {
      if (!this.getMaximumRate().isInfinite()) {
         var1.getEngineData().getCooldown(this).spawn(var1);
      }
   }

   public void spawn(Engine engine, int x, int z) {
      this.spawn(var1);
      if (!this.getMaximumRatePerChunk().isInfinite()) {
         var1.getEngineData().getChunk(var2, var3).getCooldown(this).spawn(var1);
      }
   }

   public String getFolderName() {
      return "spawners";
   }

   public String getTypeName() {
      return "Spawner";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisSpawner)) {
         return false;
      } else {
         IrisSpawner var2 = (IrisSpawner)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (!super.equals(var1)) {
            return false;
         } else if (Double.compare(this.getEnergyMultiplier(), var2.getEnergyMultiplier()) != 0) {
            return false;
         } else if (this.getMaxEntitiesPerChunk() != var2.getMaxEntitiesPerChunk()) {
            return false;
         } else {
            KList var3 = this.getSpawns();
            KList var4 = var2.getSpawns();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            label107: {
               KList var5 = this.getInitialSpawns();
               KList var6 = var2.getInitialSpawns();
               if (var5 == null) {
                  if (var6 == null) {
                     break label107;
                  }
               } else if (var5.equals(var6)) {
                  break label107;
               }

               return false;
            }

            IrisTimeBlock var7 = this.getTimeBlock();
            IrisTimeBlock var8 = var2.getTimeBlock();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            IrisWeather var9 = this.getWeather();
            IrisWeather var10 = var2.getWeather();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            label86: {
               IrisRate var11 = this.getMaximumRate();
               IrisRate var12 = var2.getMaximumRate();
               if (var11 == null) {
                  if (var12 == null) {
                     break label86;
                  }
               } else if (var11.equals(var12)) {
                  break label86;
               }

               return false;
            }

            label79: {
               IrisRate var13 = this.getMaximumRatePerChunk();
               IrisRate var14 = var2.getMaximumRatePerChunk();
               if (var13 == null) {
                  if (var14 == null) {
                     break label79;
                  }
               } else if (var13.equals(var14)) {
                  break label79;
               }

               return false;
            }

            label72: {
               IrisRange var15 = this.getAllowedLightLevels();
               IrisRange var16 = var2.getAllowedLightLevels();
               if (var15 == null) {
                  if (var16 == null) {
                     break label72;
                  }
               } else if (var15.equals(var16)) {
                  break label72;
               }

               return false;
            }

            IrisSpawnGroup var17 = this.getGroup();
            IrisSpawnGroup var18 = var2.getGroup();
            if (var17 == null) {
               if (var18 != null) {
                  return false;
               }
            } else if (!var17.equals(var18)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisSpawner;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      int var2 = super.hashCode();
      long var3 = Double.doubleToLongBits(this.getEnergyMultiplier());
      var2 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      var2 = var2 * 59 + this.getMaxEntitiesPerChunk();
      KList var5 = this.getSpawns();
      var2 = var2 * 59 + (var5 == null ? 43 : var5.hashCode());
      KList var6 = this.getInitialSpawns();
      var2 = var2 * 59 + (var6 == null ? 43 : var6.hashCode());
      IrisTimeBlock var7 = this.getTimeBlock();
      var2 = var2 * 59 + (var7 == null ? 43 : var7.hashCode());
      IrisWeather var8 = this.getWeather();
      var2 = var2 * 59 + (var8 == null ? 43 : var8.hashCode());
      IrisRate var9 = this.getMaximumRate();
      var2 = var2 * 59 + (var9 == null ? 43 : var9.hashCode());
      IrisRate var10 = this.getMaximumRatePerChunk();
      var2 = var2 * 59 + (var10 == null ? 43 : var10.hashCode());
      IrisRange var11 = this.getAllowedLightLevels();
      var2 = var2 * 59 + (var11 == null ? 43 : var11.hashCode());
      IrisSpawnGroup var12 = this.getGroup();
      var2 = var2 * 59 + (var12 == null ? 43 : var12.hashCode());
      return var2;
   }

   @Generated
   public IrisSpawner() {
      this.weather = IrisWeather.ANY;
      this.maximumRate = new IrisRate();
      this.maximumRatePerChunk = new IrisRate();
      this.allowedLightLevels = new IrisRange(0.0D, 15.0D);
      this.group = IrisSpawnGroup.NORMAL;
   }

   @Generated
   public IrisSpawner(final IrisMarker referenceMarker, final KList<IrisEntitySpawn> spawns, final KList<IrisEntitySpawn> initialSpawns, final double energyMultiplier, final int maxEntitiesPerChunk, final IrisTimeBlock timeBlock, final IrisWeather weather, final IrisRate maximumRate, final IrisRate maximumRatePerChunk, final IrisRange allowedLightLevels, final IrisSpawnGroup group) {
      this.weather = IrisWeather.ANY;
      this.maximumRate = new IrisRate();
      this.maximumRatePerChunk = new IrisRate();
      this.allowedLightLevels = new IrisRange(0.0D, 15.0D);
      this.group = IrisSpawnGroup.NORMAL;
      this.referenceMarker = var1;
      this.spawns = var2;
      this.initialSpawns = var3;
      this.energyMultiplier = var4;
      this.maxEntitiesPerChunk = var6;
      this.timeBlock = var7;
      this.weather = var8;
      this.maximumRate = var9;
      this.maximumRatePerChunk = var10;
      this.allowedLightLevels = var11;
      this.group = var12;
   }

   @Generated
   public IrisMarker getReferenceMarker() {
      return this.referenceMarker;
   }

   @Generated
   public KList<IrisEntitySpawn> getSpawns() {
      return this.spawns;
   }

   @Generated
   public KList<IrisEntitySpawn> getInitialSpawns() {
      return this.initialSpawns;
   }

   @Generated
   public double getEnergyMultiplier() {
      return this.energyMultiplier;
   }

   @Generated
   public int getMaxEntitiesPerChunk() {
      return this.maxEntitiesPerChunk;
   }

   @Generated
   public IrisTimeBlock getTimeBlock() {
      return this.timeBlock;
   }

   @Generated
   public IrisWeather getWeather() {
      return this.weather;
   }

   @Generated
   public IrisRate getMaximumRate() {
      return this.maximumRate;
   }

   @Generated
   public IrisRate getMaximumRatePerChunk() {
      return this.maximumRatePerChunk;
   }

   @Generated
   public IrisRange getAllowedLightLevels() {
      return this.allowedLightLevels;
   }

   @Generated
   public IrisSpawnGroup getGroup() {
      return this.group;
   }

   @Generated
   public IrisSpawner setReferenceMarker(final IrisMarker referenceMarker) {
      this.referenceMarker = var1;
      return this;
   }

   @Generated
   public IrisSpawner setSpawns(final KList<IrisEntitySpawn> spawns) {
      this.spawns = var1;
      return this;
   }

   @Generated
   public IrisSpawner setInitialSpawns(final KList<IrisEntitySpawn> initialSpawns) {
      this.initialSpawns = var1;
      return this;
   }

   @Generated
   public IrisSpawner setEnergyMultiplier(final double energyMultiplier) {
      this.energyMultiplier = var1;
      return this;
   }

   @Generated
   public IrisSpawner setMaxEntitiesPerChunk(final int maxEntitiesPerChunk) {
      this.maxEntitiesPerChunk = var1;
      return this;
   }

   @Generated
   public IrisSpawner setTimeBlock(final IrisTimeBlock timeBlock) {
      this.timeBlock = var1;
      return this;
   }

   @Generated
   public IrisSpawner setWeather(final IrisWeather weather) {
      this.weather = var1;
      return this;
   }

   @Generated
   public IrisSpawner setMaximumRate(final IrisRate maximumRate) {
      this.maximumRate = var1;
      return this;
   }

   @Generated
   public IrisSpawner setMaximumRatePerChunk(final IrisRate maximumRatePerChunk) {
      this.maximumRatePerChunk = var1;
      return this;
   }

   @Generated
   public IrisSpawner setAllowedLightLevels(final IrisRange allowedLightLevels) {
      this.allowedLightLevels = var1;
      return this;
   }

   @Generated
   public IrisSpawner setGroup(final IrisSpawnGroup group) {
      this.group = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getReferenceMarker());
      return "IrisSpawner(referenceMarker=" + var10000 + ", spawns=" + String.valueOf(this.getSpawns()) + ", initialSpawns=" + String.valueOf(this.getInitialSpawns()) + ", energyMultiplier=" + this.getEnergyMultiplier() + ", maxEntitiesPerChunk=" + this.getMaxEntitiesPerChunk() + ", timeBlock=" + String.valueOf(this.getTimeBlock()) + ", weather=" + String.valueOf(this.getWeather()) + ", maximumRate=" + String.valueOf(this.getMaximumRate()) + ", maximumRatePerChunk=" + String.valueOf(this.getMaximumRatePerChunk()) + ", allowedLightLevels=" + String.valueOf(this.getAllowedLightLevels()) + ", group=" + String.valueOf(this.getGroup()) + ")";
   }
}
