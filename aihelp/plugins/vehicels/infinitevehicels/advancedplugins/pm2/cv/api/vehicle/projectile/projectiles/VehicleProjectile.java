package advancedplugins.pm2.cv.api.vehicle.projectile.projectiles;

import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.projectile.impacts.ImpactEffect;
import advancedplugins.pm2.cv.api.vehicle.projectile.projectiles.impl.BlockProjectile;
import advancedplugins.pm2.cv.api.vehicle.projectile.projectiles.impl.EntityProjectile;
import advancedplugins.pm2.cv.api.vehicle.projectile.projectiles.impl.ParticleBeamProjectile;
import advancedplugins.pm2.cv.api.vehicle.projectile.projectiles.impl.ParticleGravityProjectile;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public interface VehicleProjectile {
   void tick(long var1);

   void finish();

   boolean isFinished();

   VehicleProjectile.VehicleProjectileType getType();

   ImpactEffect getImpact();

   void setLocation(Location var1);

   Location getLocation();

   /** @deprecated */
   default void spawn(Location spawnLocation, Vehicle shooter) {
      this.setLocation(spawnLocation);
      (new BukkitRunnable() {
         long ticks = 0L;

         public void run() {
            VehicleProjectile.this.tick((long)(this.ticks++));
            if (VehicleProjectile.this.isFinished()) {
               this.cancel();
            }

         }
      }).runTaskTimer(JavaPlugin.getProvidingPlugin(this.getClass()), 0L, 0L);
   }

   void write(ConfigurationSection var1);

   void load(ConfigurationSection var1);

   static VehicleProjectile read(ConfigurationSection section) {
      VehicleProjectile.VehicleProjectileType projectileType = (VehicleProjectile.VehicleProjectileType)ConfigurationUtil.loadEnum(VehicleProjectile.VehicleProjectileType.class, section, "type", true);

      VehicleProjectile instance;
      try {
         instance = (VehicleProjectile)projectileType.type.getConstructor().newInstance();
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException var4) {
         throw new RuntimeException(var4);
      }

      instance.load(section);
      instance.setImpactType(ImpactEffect.read(section.getConfigurationSection("impact-type")));
      return instance;
   }

   void setImpactType(ImpactEffect var1);

   VehicleProjectile copy();

   public static enum VehicleProjectileType {
      PARTICLE_BEAM("particle-beam", ParticleBeamProjectile.class),
      PARTICLE_GRAVITY("particle-gravity", ParticleGravityProjectile.class),
      BLOCK("block", BlockProjectile.class),
      ENTITY("entity", EntityProjectile.class);

      private final String key;
      private final Class<?> type;

      private VehicleProjectileType(String param3, Class<?> param4) {
         this.key = var3;
         this.type = var4;
      }

      public VehicleProjectile create() {
         try {
            VehicleProjectile var1 = (VehicleProjectile)this.type.getConstructor().newInstance();
            return var1;
         } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException var3) {
            throw new RuntimeException(var3);
         }
      }

      // $FF: synthetic method
      private static VehicleProjectile.VehicleProjectileType[] $values() {
         return new VehicleProjectile.VehicleProjectileType[]{PARTICLE_BEAM, PARTICLE_GRAVITY, BLOCK, ENTITY};
      }
   }
}
