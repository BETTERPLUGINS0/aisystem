package me.casperge.realisticseasons.particle.entity;

import java.util.List;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class SmallFallingLeaf implements SeasonEntity {
   private Location currentLocation;
   private int size;
   private int count = 0;
   private boolean isDestroyed = false;
   boolean doTick = true;

   public SmallFallingLeaf(Location var1, List<Player> var2) {
      this.currentLocation = var1;
      this.size = 10 + JavaUtils.getRandom().nextInt(20);
   }

   public void tick(List<Player> var1) {
      this.doTick = !this.doTick;
      if (this.doTick) {
         ++this.count;
         if (!this.isDestroyed) {
            this.playParticle();
            if (this.count > this.size) {
               this.isDestroyed = true;
            }
         }

      }
   }

   private void playParticle() {
      this.currentLocation.getWorld().spawnParticle(Particle.ASH, this.currentLocation, 2, 0.2D, 0.5D, 0.2D, 0.0D, (Object)null, false);
   }

   public boolean isDestroyed() {
      return this.isDestroyed;
   }

   public Location getLocation() {
      return this.currentLocation;
   }
}
