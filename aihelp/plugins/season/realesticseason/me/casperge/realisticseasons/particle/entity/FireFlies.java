package me.casperge.realisticseasons.particle.entity;

import java.util.List;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class FireFlies implements SeasonEntity {
   private Location loc;
   private int count = 0;
   private boolean isDestroyed = false;

   public FireFlies(Location var1) {
      this.loc = var1;
   }

   public void tick(List<Player> var1) {
      ++this.count;
      if (!this.isDestroyed) {
         if (this.loc.getWorld().getFullTime() % 24000L <= 13670L || this.loc.getWorld().getFullTime() % 24000L >= 22812L) {
            this.isDestroyed = true;
            return;
         }

         this.playParticle();
         if (this.count > 2000) {
            this.isDestroyed = true;
         }
      }

   }

   private void playParticle() {
      if (JavaUtils.getRandom().nextInt(3) != 0) {
         this.loc.getWorld().spawnParticle(Particle.TOTEM, this.loc.getX(), this.loc.getY(), this.loc.getZ(), 1, 10.0D, 3.0D, 10.0D, 1.0D, (Object)null, true);
      }
   }

   public boolean isDestroyed() {
      return this.isDestroyed;
   }

   public Location getLocation() {
      return this.loc;
   }
}
