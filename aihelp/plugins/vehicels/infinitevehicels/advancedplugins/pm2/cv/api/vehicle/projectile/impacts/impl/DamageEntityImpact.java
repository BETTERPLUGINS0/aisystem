package advancedplugins.pm2.cv.api.vehicle.projectile.impacts.impl;

import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.vehicle.projectile.ProjectileBuilderEditableField;
import advancedplugins.pm2.cv.api.vehicle.projectile.impacts.ImpactEffect;
import java.util.Collection;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class DamageEntityImpact implements ImpactEffect {
   @ProjectileBuilderEditableField
   private double amount;

   public DamageEntityImpact(double var1) {
      this.amount = var1;
   }

   public DamageEntityImpact() {
      this.amount = 0.0D;
   }

   public void write(ConfigurationSection var1) {
      ConfigurationUtil.writeEnum(this.getType(), var1, "type");
      var1.set("damage", this.amount);
   }

   public void load(ConfigurationSection var1) {
      this.amount = var1.getDouble("damage");
   }

   public void apply(Block var1) {
   }

   public void apply(Collection<Entity> var1) {
      var1.forEach((var1x) -> {
         if (var1x instanceof LivingEntity) {
            LivingEntity var2 = (LivingEntity)var1x;
            var2.damage(this.amount);
         }
      });
   }

   public ImpactEffect.ImpactEffectType getType() {
      return ImpactEffect.ImpactEffectType.DAMAGE_ENTITY;
   }
}
