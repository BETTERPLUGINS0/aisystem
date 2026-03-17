package advancedplugins.pm2.cv.api.vehicle.projectile.impacts;

import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.vehicle.projectile.impacts.impl.BreakBlockImpact;
import advancedplugins.pm2.cv.api.vehicle.projectile.impacts.impl.DamageEntityImpact;
import advancedplugins.pm2.cv.api.vehicle.projectile.impacts.impl.ExplosionImpact;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

public interface ImpactEffect {
   void write(ConfigurationSection var1);

   void load(ConfigurationSection var1);

   static ImpactEffect read(ConfigurationSection section) {
      ImpactEffect.ImpactEffectType effectType = (ImpactEffect.ImpactEffectType)ConfigurationUtil.loadEnum(ImpactEffect.ImpactEffectType.class, section, "type", true);

      ImpactEffect effect;
      try {
         effect = (ImpactEffect)effectType.type.getConstructor().newInstance();
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException var4) {
         throw new RuntimeException(var4);
      }

      effect.load(section);
      return effect;
   }

   void apply(Block var1);

   void apply(Collection<Entity> var1);

   ImpactEffect.ImpactEffectType getType();

   public static enum ImpactEffectType {
      EXPLODE("explosion", ExplosionImpact.class),
      DAMAGE_ENTITY("damage", DamageEntityImpact.class),
      DESTROY_BLOCK("destroy", BreakBlockImpact.class);

      final String key;
      final Class<?> type;

      private ImpactEffectType(String param3, Class<?> param4) {
         this.key = var3;
         this.type = var4;
      }

      public ImpactEffect create() {
         ImpactEffect var1 = null;

         try {
            var1 = (ImpactEffect)this.type.getConstructor().newInstance();
            return var1;
         } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException var3) {
            throw new RuntimeException(var3);
         }
      }

      public String getKey() {
         return this.key;
      }

      public Class<?> getType() {
         return this.type;
      }

      // $FF: synthetic method
      private static ImpactEffect.ImpactEffectType[] $values() {
         return new ImpactEffect.ImpactEffectType[]{EXPLODE, DAMAGE_ENTITY, DESTROY_BLOCK};
      }
   }
}
