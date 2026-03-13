package me.casperge.realisticseasons.particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.blockscanner.SimpleLocation;
import me.casperge.realisticseasons.particle.entity.FallingLeaf;
import me.casperge.realisticseasons.particle.entity.FireFlies;
import me.casperge.realisticseasons.particle.entity.FireworkBox;
import me.casperge.realisticseasons.particle.entity.Meteorite;
import me.casperge.realisticseasons.particle.entity.SeasonEntity;
import me.casperge.realisticseasons.particle.entity.SmallFallingLeaf;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParticleManager {
   private Random r = new Random();
   private RealisticSeasons main;
   public HashMap<UUID, List<SimpleLocation>> leaveLocations = new HashMap();
   private ParticleSpawner spawner;
   private Vector wind;
   public static List<UUID> disabledParticles = new ArrayList();
   int test = 0;
   private HashMap<SeasonEntity, List<Player>> activeEntities = new HashMap();
   int counter = 0;

   public ParticleManager(RealisticSeasons var1) {
      this.main = var1;
      this.wind = new Vector(JavaUtils.getRandom().nextDouble() / 2.0D, 0.0D, JavaUtils.getRandom().nextDouble() / 2.0D);
      this.spawner = new ParticleSpawner(var1, this);
      this.spawner.runTaskTimer(var1, 2L, 2L);
      Bukkit.getScheduler().runTaskTimer(var1, new Runnable() {
         public void run() {
            ArrayList var1 = new ArrayList();
            Iterator var2 = ParticleManager.this.activeEntities.keySet().iterator();

            SeasonEntity var3;
            while(var2.hasNext()) {
               var3 = (SeasonEntity)var2.next();
               if (var3.isDestroyed()) {
                  var1.add(var3);
               } else {
                  var3.tick((List)ParticleManager.this.activeEntities.get(var3));
               }
            }

            var2 = var1.iterator();

            while(var2.hasNext()) {
               var3 = (SeasonEntity)var2.next();
               ParticleManager.this.activeEntities.remove(var3);
            }

            ++ParticleManager.this.counter;
         }
      }, 2L, 2L);
   }

   public void runTest(Location var1, Player var2) {
      this.spawnFireFlies(var1);
   }

   public void spawnFireFlies(Location var1) {
      this.spawnEntity(ParticleManager.SeasonEntityType.FIREFLIES, (List)null, var1);
   }

   public void spawnSmallFallingLeaves(List<Player> var1, Location var2) {
      ArrayList var3 = new ArrayList();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         Player var5 = (Player)var4.next();
         if (disabledParticles.contains(var5.getUniqueId())) {
            var3.add(var5);
         }
      }

      var1.removeAll(var3);
      this.spawnEntity(ParticleManager.SeasonEntityType.SMALL_FALLING_LEAVES, var1, var2);
   }

   public SeasonEntity spawnEntity(ParticleManager.SeasonEntityType var1, List<Player> var2, Location var3) {
      switch(var1.ordinal()) {
      case 0:
         Meteorite var4 = new Meteorite(var3, var2);
         this.activeEntities.put(var4, var2);
         return var4;
      case 1:
         FallingLeaf var5 = new FallingLeaf(var3, var2, this.wind, this.main.getSettings().fallingLeafItem, this.main.getSettings().fallingLeafCMD);
         this.activeEntities.put(var5, var2);
         return var5;
      case 2:
         SmallFallingLeaf var6 = new SmallFallingLeaf(var3, var2);
         this.activeEntities.put(var6, var2);
         return var6;
      case 3:
         FireFlies var7 = new FireFlies(var3);
         this.activeEntities.put(var7, (Object)null);
         return var7;
      case 4:
         FireworkBox var8 = new FireworkBox(var3, var2);
         this.activeEntities.put(var8, var2);
         return var8;
      default:
         return null;
      }
   }

   public void playWhiteSparkles(Player var1) {
      if (!disabledParticles.contains(var1.getUniqueId())) {
         var1.spawnParticle(Particle.END_ROD, var1.getLocation().getX(), var1.getLocation().getY() + (double)this.main.getSettings().nightSparksHeight, var1.getLocation().getZ(), this.main.getSettings().nightSparksHeight, 50.0D, 16.0D, 50.0D, 0.0D);
      }
   }

   public void spawnMeteorite(List<Player> var1, Location var2) {
      ArrayList var3 = new ArrayList();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         Player var5 = (Player)var4.next();
         if (disabledParticles.contains(var5.getUniqueId())) {
            var3.add(var5);
         }
      }

      var1.removeAll(var3);
      this.spawnEntity(ParticleManager.SeasonEntityType.METEORITE, var1, var2);
   }

   public void spawnLeaf(List<Player> var1, Location var2) {
      ArrayList var3 = new ArrayList();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         Player var5 = (Player)var4.next();
         if (disabledParticles.contains(var5.getUniqueId())) {
            var3.add(var5);
         }
      }

      var1.removeAll(var3);
      this.spawnEntity(ParticleManager.SeasonEntityType.FALLING_LEAF, var1, var2);
   }

   public void spawnFireworkBox(List<Player> var1, Location var2) {
      this.spawnEntity(ParticleManager.SeasonEntityType.FIREWORKBOX, var1, var2);
   }

   public void spawnFireworkBox(Location var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.getWorld().getPlayers().iterator();

      while(var3.hasNext()) {
         Player var4 = (Player)var3.next();
         if (var4.getLocation().distance(var1) < 200.0D) {
            var2.add(var4);
         }
      }

      Location var5 = var1.getBlock().getLocation();
      var5.subtract(0.0D, 1.5D, 0.0D);
      var5.add(0.5D, 0.0D, 0.5D);
      this.spawnEntity(ParticleManager.SeasonEntityType.FIREWORKBOX, var2, var5);
   }

   public void playRandomSweatParticle(Player var1) {
      double var3 = JavaUtils.randomDouble(0.35D, 0.65D);
      double var7 = JavaUtils.randomDouble(0.35D, 0.65D);
      double var5;
      if (this.r.nextInt(2) == 1) {
         var5 = this.r.nextDouble();
      } else {
         var5 = this.r.nextDouble() * 0.6D + 1.0D;
      }

      Location var2 = new Location(var1.getWorld(), var1.getLocation().getX() + (var3 / 2.0D - var3) + 0.5D, var1.getLocation().getY() + var5, var1.getLocation().getZ() + (var7 / 2.0D - var7) + 0.5D);
      ArrayList var9 = new ArrayList();
      Iterator var10 = var1.getWorld().getPlayers().iterator();

      while(var10.hasNext()) {
         Player var11 = (Player)var10.next();
         if (var11.getLocation().distance(var2) < 18.0D && var11.canSee(var1) && !disabledParticles.contains(var11.getUniqueId())) {
            var9.add(var11);
         }
      }

      var1.spawnParticle(Particle.FALLING_WATER, var2, 1);
   }

   public void playRandomFireParticle(Entity var1) {
      Vector var2 = var1.getBoundingBox().getCenter();
      Location var3 = new Location(var1.getWorld(), var2.getX(), var2.getY(), var2.getZ());
      double var4 = var1.getBoundingBox().getWidthX() / 2.0D;
      double var6 = var1.getBoundingBox().getHeight() / 2.0D;
      double var8 = var1.getBoundingBox().getWidthZ() / 2.0D;
      var1.getWorld().spawnParticle(Particle.FLAME, var3, 1, var4, var6, var8, 0.0D, (Object)null);
   }

   public void playColdBreathEffect(final Player var1) {
      this.playBreathOnce(var1);
      Bukkit.getScheduler().runTaskLater(this.main, new Runnable() {
         public void run() {
            ParticleManager.this.playBreathOnce(var1);
            Bukkit.getScheduler().runTaskLater(ParticleManager.this.main, new Runnable() {
               public void run() {
                  ParticleManager.this.playBreathOnce(var1);
               }
            }, 3L);
         }
      }, 5L);
   }

   private void playBreathOnce(Player var1) {
      final Location var2 = var1.getEyeLocation().clone();
      final Vector var3 = var2.getDirection().clone().multiply(0.1D);
      var2.add(var3);
      var2.add(var3);
      DustOptions var4 = new DustOptions(Color.fromRGB(255, 255, 255), 1.0F);
      final DustOptions var5 = new DustOptions(Color.fromRGB(255, 255, 255), 1.4F);
      final DustOptions var6 = new DustOptions(Color.fromRGB(255, 255, 255), 1.8F);
      final ArrayList var7 = new ArrayList();
      Iterator var8 = var1.getWorld().getPlayers().iterator();

      while(true) {
         Player var9;
         do {
            do {
               do {
                  if (!var8.hasNext()) {
                     var2.add(var3);
                     Bukkit.getScheduler().runTaskLater(this.main, new Runnable() {
                        public void run() {
                           Iterator var1 = var7.iterator();

                           while(var1.hasNext()) {
                              Player var2x = (Player)var1.next();
                              var2x.spawnParticle(Particle.REDSTONE, var2.getX(), var2.getY() - 0.3D, var2.getZ(), 0, var3.getX(), var3.getY(), var3.getZ(), 0.1D, var5);
                           }

                           var2.add(var3);
                           Bukkit.getScheduler().runTaskLater(ParticleManager.this.main, new Runnable() {
                              public void run() {
                                 Iterator var1 = var7.iterator();

                                 while(var1.hasNext()) {
                                    Player var2x = (Player)var1.next();
                                    var2x.spawnParticle(Particle.REDSTONE, var2.getX(), var2.getY() - 0.3D, var2.getZ(), 0, var3.getX(), var3.getY(), var3.getZ(), 0.1D, var6);
                                 }

                              }
                           }, 2L);
                        }
                     }, 2L);
                     return;
                  }

                  var9 = (Player)var8.next();
               } while(!(var9.getLocation().distance(var1.getLocation()) < 17.0D));
            } while(!var9.canSee(var1));
         } while(this.main.getSettings().coldBreathHideOwn && var9 == var1);

         if (!disabledParticles.contains(var1.getUniqueId())) {
            var9.spawnParticle(Particle.REDSTONE, var2.getX(), var2.getY() - 0.3D, var2.getZ(), 0, var3.getX(), var3.getY(), var3.getZ(), 0.1D, var4);
            var7.add(var9);
         }
      }
   }

   public static enum SeasonEntityType {
      METEORITE,
      FALLING_LEAF,
      SMALL_FALLING_LEAVES,
      FIREFLIES,
      FIREWORKBOX;

      // $FF: synthetic method
      private static ParticleManager.SeasonEntityType[] $values() {
         return new ParticleManager.SeasonEntityType[]{METEORITE, FALLING_LEAF, SMALL_FALLING_LEAVES, FIREFLIES, FIREWORKBOX};
      }
   }
}
