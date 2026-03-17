package advancedplugins.pm2.cv.api.vehicle.projectile.impacts.impl;

import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.vehicle.projectile.impacts.ImpactEffect;
import java.util.Collection;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

public class BreakBlockImpact implements ImpactEffect {
   public void write(ConfigurationSection var1) {
      ConfigurationUtil.writeEnum(this.getType(), var1, "type");
   }

   public void load(ConfigurationSection var1) {
   }

   public void apply(Block var1) {
      if (var1.getType() != Material.BEDROCK) {
         var1.getWorld().playEffect(var1.getLocation(), Effect.STEP_SOUND, var1.getType());
         var1.setType(Material.AIR);
      }
   }

   public void apply(Collection<Entity> var1) {
   }

   public ImpactEffect.ImpactEffectType getType() {
      return ImpactEffect.ImpactEffectType.DESTROY_BLOCK;
   }
}
