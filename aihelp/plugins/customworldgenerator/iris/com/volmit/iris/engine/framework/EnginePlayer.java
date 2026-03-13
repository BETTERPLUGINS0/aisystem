package com.volmit.iris.engine.framework;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisEffect;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.scheduling.J;
import java.util.Iterator;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EnginePlayer {
   private final Engine engine;
   private final Player player;
   private IrisBiome biome;
   private IrisRegion region;
   private Location lastLocation;
   private long lastSample;

   public EnginePlayer(Engine engine, Player player) {
      this.engine = var1;
      this.player = var2;
      this.lastLocation = var2.getLocation().clone();
      this.lastSample = -1L;
      this.sample();
   }

   public void tick() {
      if (!this.sample() && IrisSettings.get().getWorld().isEffectSystem()) {
         J.a(() -> {
            Iterator var1;
            IrisEffect var2;
            if (this.region != null) {
               var1 = this.region.getEffects().iterator();

               while(var1.hasNext()) {
                  var2 = (IrisEffect)var1.next();

                  try {
                     var2.apply(this.player, this.getEngine());
                  } catch (Throwable var5) {
                     Iris.reportError(var5);
                  }
               }
            }

            if (this.biome != null) {
               var1 = this.biome.getEffects().iterator();

               while(var1.hasNext()) {
                  var2 = (IrisEffect)var1.next();

                  try {
                     var2.apply(this.player, this.getEngine());
                  } catch (Throwable var4) {
                     Iris.reportError(var4);
                  }
               }
            }

         });
      }
   }

   public long ticksSinceLastSample() {
      return M.ms() - this.lastSample;
   }

   public boolean sample() {
      Location var1 = this.player.getLocation().clone();
      if (var1.getWorld() != this.engine.getWorld().realWorld()) {
         return true;
      } else {
         try {
            if (this.ticksSinceLastSample() > 55L && var1.distanceSquared(this.lastLocation) > 81.0D) {
               this.lastLocation = var1;
               this.lastSample = M.ms();
               this.biome = this.engine.getBiome(var1);
               this.region = this.engine.getRegion(var1);
            }

            return false;
         } catch (Throwable var3) {
            Iris.reportError(var3);
            return true;
         }
      }
   }

   @Generated
   public Engine getEngine() {
      return this.engine;
   }

   @Generated
   public Player getPlayer() {
      return this.player;
   }

   @Generated
   public IrisBiome getBiome() {
      return this.biome;
   }

   @Generated
   public IrisRegion getRegion() {
      return this.region;
   }

   @Generated
   public Location getLastLocation() {
      return this.lastLocation;
   }

   @Generated
   public long getLastSample() {
      return this.lastSample;
   }

   @Generated
   public void setBiome(final IrisBiome biome) {
      this.biome = var1;
   }

   @Generated
   public void setRegion(final IrisRegion region) {
      this.region = var1;
   }

   @Generated
   public void setLastLocation(final Location lastLocation) {
      this.lastLocation = var1;
   }

   @Generated
   public void setLastSample(final long lastSample) {
      this.lastSample = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof EnginePlayer)) {
         return false;
      } else {
         EnginePlayer var2 = (EnginePlayer)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getLastSample() != var2.getLastSample()) {
            return false;
         } else {
            label73: {
               Engine var3 = this.getEngine();
               Engine var4 = var2.getEngine();
               if (var3 == null) {
                  if (var4 == null) {
                     break label73;
                  }
               } else if (var3.equals(var4)) {
                  break label73;
               }

               return false;
            }

            Player var5 = this.getPlayer();
            Player var6 = var2.getPlayer();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label59: {
               IrisBiome var7 = this.getBiome();
               IrisBiome var8 = var2.getBiome();
               if (var7 == null) {
                  if (var8 == null) {
                     break label59;
                  }
               } else if (var7.equals(var8)) {
                  break label59;
               }

               return false;
            }

            IrisRegion var9 = this.getRegion();
            IrisRegion var10 = var2.getRegion();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            Location var11 = this.getLastLocation();
            Location var12 = var2.getLastLocation();
            if (var11 == null) {
               if (var12 != null) {
                  return false;
               }
            } else if (!var11.equals(var12)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof EnginePlayer;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = this.getLastSample();
      int var10 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      Engine var5 = this.getEngine();
      var10 = var10 * 59 + (var5 == null ? 43 : var5.hashCode());
      Player var6 = this.getPlayer();
      var10 = var10 * 59 + (var6 == null ? 43 : var6.hashCode());
      IrisBiome var7 = this.getBiome();
      var10 = var10 * 59 + (var7 == null ? 43 : var7.hashCode());
      IrisRegion var8 = this.getRegion();
      var10 = var10 * 59 + (var8 == null ? 43 : var8.hashCode());
      Location var9 = this.getLastLocation();
      var10 = var10 * 59 + (var9 == null ? 43 : var9.hashCode());
      return var10;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getEngine());
      return "EnginePlayer(engine=" + var10000 + ", player=" + String.valueOf(this.getPlayer()) + ", biome=" + String.valueOf(this.getBiome()) + ", region=" + String.valueOf(this.getRegion()) + ", lastLocation=" + String.valueOf(this.getLastLocation()) + ", lastSample=" + this.getLastSample() + ")";
   }
}
