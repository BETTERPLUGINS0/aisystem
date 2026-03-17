package advancedplugins.pm2.cv.models.core.citizens;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import lombok.Generated;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

@TraitName("bug")
public class BugTrait extends Trait {
   private int tick;

   public BugTrait() {
      super("bug");
   }

   public void run() {
      Entity var1 = this.npc.getEntity();
      if (var1 != null) {
         EntityHandler var2 = ModelAPI.getEntityHandler();
         Location var3 = var1.getLocation();
         World var4 = var3.getWorld();
         Vector var5 = (new Vector(0.0D, 0.0D, 0.5D)).rotateAroundY((double)(-var2.getYRot(var1) * 0.017453292F));
         Vector var6 = (new Vector(0, 0, 1)).rotateAroundY((double)(-var2.getYBodyRot(var1) * 0.017453292F));
         Vector var7 = (new Vector(0.0D, 0.0D, 1.5D)).rotateAroundY((double)(-var2.getYHeadRot(var1) * 0.017453292F));
         var4.spawnParticle(Particle.REDSTONE, var3.clone().add(var5), 1, new DustOptions(Color.RED, 0.25F));
         var4.spawnParticle(Particle.REDSTONE, var3.clone().add(var6), 1, new DustOptions(Color.GREEN, 0.25F));
         var4.spawnParticle(Particle.REDSTONE, var3.clone().add(var7), 1, new DustOptions(Color.BLUE, 0.25F));
      }

   }

   @Generated
   public int getTick() {
      return this.tick;
   }

   @Generated
   public void setTick(int var1) {
      this.tick = var1;
   }
}
