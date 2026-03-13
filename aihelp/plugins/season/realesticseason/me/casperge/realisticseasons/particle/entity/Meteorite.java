package me.casperge.realisticseasons.particle.entity;

import java.util.List;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Meteorite implements SeasonEntity {
   private Vector direction;
   private Location currentLocation;
   private int size;
   private int count = 0;
   private boolean isDestroyed = false;

   public Meteorite(Location var1, List<Player> var2) {
      double[] var3 = JavaUtils.toCartesianAsVector((double)JavaUtils.getRandom().nextInt(360));
      this.direction = (new Vector(var3[0], 0.0D, var3[1])).normalize().multiply(5);
      this.size = 10 + JavaUtils.getRandom().nextInt(60);
      this.currentLocation = var1.clone().subtract(this.direction.clone().multiply(this.size / 2));
   }

   public void tick(List<Player> var1) {
      ++this.count;
      if (!this.isDestroyed) {
         this.playParticle();
         this.currentLocation.add(this.direction);
         if (this.count > this.size) {
            this.isDestroyed = true;
         }
      }

   }

   private void playParticle() {
      double var1 = 1.25D - (double)JavaUtils.getRandom().nextInt(50) / 100.0D;
      this.currentLocation.getWorld().spawnParticle(Particle.FLAME, this.currentLocation.getX(), this.currentLocation.getY(), this.currentLocation.getZ(), 35, 0.25D * var1, 0.25D * var1, 0.25D * var1, 0.0D, (Object)null, true);
      this.currentLocation.getWorld().spawnParticle(Particle.FLAME, this.currentLocation.clone().subtract(this.direction.clone().multiply(0.3D)), 35, 0.25D * var1, 0.25D * var1, 0.25D * var1, 0.0D, (Object)null, true);
      this.currentLocation.getWorld().spawnParticle(Particle.FLAME, this.currentLocation.clone().subtract(this.direction.clone().multiply(0.6D)), 35, 0.25D * var1, 0.25D * var1, 0.25D * var1, 0.0D, (Object)null, true);
      this.currentLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, this.currentLocation.clone().subtract(this.direction), 25, 0.5D * var1, 0.5D * var1, 0.5D * var1, 0.0D, (Object)null, true);
   }

   public boolean isDestroyed() {
      return this.isDestroyed;
   }

   public Location getLocation() {
      return this.currentLocation;
   }
}
