package advancedplugins.pm2.cv.api.vehicle.projectile.impacts.impl;

import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.vehicle.projectile.ProjectileBuilderEditableField;
import advancedplugins.pm2.cv.api.vehicle.projectile.impacts.ImpactEffect;
import java.util.Collection;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

public class ExplosionImpact implements ImpactEffect {
   @ProjectileBuilderEditableField
   private double explosionStrength;

   public ExplosionImpact(double var1) {
      this.explosionStrength = var1;
   }

   public ExplosionImpact() {
      this(1.0D);
   }

   public void write(ConfigurationSection var1) {
      ConfigurationUtil.writeEnum(this.getType(), var1, "type");
      var1.set("explosion-strength", this.explosionStrength);
   }

   public void load(ConfigurationSection var1) {
      this.explosionStrength = var1.getDouble("explosion-strength");
   }

   public void apply(Block var1) {
      this.explode(var1.getLocation());
   }

   public void apply(Collection<Entity> var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Entity var3 = (Entity)var2.next();
         this.explode(var3.getLocation());
      }

   }

   public ImpactEffect.ImpactEffectType getType() {
      return ImpactEffect.ImpactEffectType.EXPLODE;
   }

   public void explode(Location var1) {
      var1.getWorld().createExplosion(var1, (float)this.explosionStrength);
   }
}
