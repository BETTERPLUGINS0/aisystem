package advancedplugins.pm2.cv.api.vehicle.projectile.projectiles.impl;

import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.projectile.ProjectileBuilderEditableField;
import advancedplugins.pm2.cv.api.vehicle.projectile.impacts.DefaultImpactEffects;
import advancedplugins.pm2.cv.api.vehicle.projectile.impacts.ImpactEffect;
import advancedplugins.pm2.cv.api.vehicle.projectile.projectiles.VehicleProjectile;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityProjectile implements VehicleProjectile {
   private ImpactEffect impact;
   private Location spawnLocation;
   @ProjectileBuilderEditableField
   private Vector velocityMultiplier;
   @Nullable
   private Entity entity;
   @ProjectileBuilderEditableField
   private EntityType type;
   private boolean isFinished;

   public EntityProjectile(Location var1, @Nullable Vector var2, @NotNull EntityType var3, @Nullable ImpactEffect var4) {
      this.spawnLocation = var1;
      this.type = var3;
      this.velocityMultiplier = var2 == null ? new Vector(1, 1, 1) : var2;
      this.impact = var4 == null ? DefaultImpactEffects.damageEntity(5.0D) : var4;
   }

   public EntityProjectile() {
      this.type = EntityType.EGG;
      this.spawnLocation = new Location((World)Bukkit.getWorlds().get(0), 0.0D, 0.0D, 0.0D);
      this.velocityMultiplier = new Vector(0, 0, 0);
      this.impact = DefaultImpactEffects.damageEntity(0.0D);
   }

   public void spawn(Location var1, Vehicle var2) {
      this.spawnLocation = var1;
      this.entity = this.spawnLocation.getWorld().spawnEntity(this.spawnLocation, this.type);
      this.entity.setVelocity(this.spawnLocation.getDirection().multiply(this.velocityMultiplier));
      Entity var4 = this.entity;
      if (var4 instanceof Projectile) {
         Projectile var3 = (Projectile)var4;
         Stream var10000 = var2.getSeats().stream().filter((var0) -> {
            return var0.getPassenger() instanceof LivingEntity && var0.isMain();
         }).map((var0) -> {
            return (LivingEntity)var0.getPassenger();
         });
         Objects.requireNonNull(var3);
         var10000.forEach(var3::setShooter);
      }

      VehicleProjectile.super.spawn(var1, var2);
   }

   public void write(ConfigurationSection var1) {
      ConfigurationUtil.writeEnum(this.getType(), var1, "type");
      this.impact.write(var1.createSection("impact-type"));
      ConfigurationUtil.writeVector(this.velocityMultiplier, var1, "velocity-multiplier");
      ConfigurationUtil.writeEnum(this.type, var1, "entity");
   }

   public void load(ConfigurationSection var1) {
      this.impact = ImpactEffect.read(var1.getConfigurationSection("impact-type"));
      this.velocityMultiplier = ConfigurationUtil.loadVector(var1, "velocity-multiplier");
      this.type = (EntityType)ConfigurationUtil.loadEnum(EntityType.class, var1, "entity");
   }

   public void tick(long var1) {
      if (this.entity == null) {
         this.finish();
      }

      if (this.entity.isDead()) {
         this.handleDamage(25L);
         this.finish();
      } else {
         this.handleDamage(var1);
      }
   }

   private void handleDamage(long var1) {
      Collection var3 = this.entity.getWorld().getNearbyEntities(this.entity.getLocation(), 0.8D, 0.8D, 0.8D, (var0) -> {
         return var0 instanceof LivingEntity;
      });
      List var12 = var3.stream().filter((var0) -> {
         return !(var0 instanceof Projectile);
      }).toList();
      if (var1 > 3L) {
         if (!var12.isEmpty()) {
            this.impact.apply((Collection)var12);
            this.finish();
         }

         Block var4 = this.entity.getLocation().getBlock();
         if (var4.getType().isAir()) {
            BlockFace[] var5 = BlockFace.values();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               BlockFace var8 = var5[var7];
               if (var8.isCartesian()) {
                  Block var9 = var4.getRelative(var8);
                  if (!var9.getType().isAir()) {
                     var4 = var9;
                  }
               }
            }
         }

         if (!var4.getType().isAir()) {
            this.impact.apply(var4);
            this.finish();
         } else {
            for(double var13 = -0.5D; var13 < 1.5D; var13 += 0.5D) {
               for(double var14 = -0.49D; var14 < 0.0D; var14 += 0.5D) {
                  for(double var15 = -0.5D; var15 < 1.5D; var15 += 0.5D) {
                     Block var11 = this.entity.getLocation().clone().add(var13, 0.0D, var15).getBlock();
                     if (!var11.getType().isAir()) {
                        this.impact.apply(var11);
                        this.finish();
                        break;
                     }
                  }
               }
            }

         }
      }
   }

   public void finish() {
      this.isFinished = true;
      if (this.entity != null) {
         this.entity.remove();
      }

   }

   public boolean isFinished() {
      return this.isFinished;
   }

   public VehicleProjectile.VehicleProjectileType getType() {
      return VehicleProjectile.VehicleProjectileType.ENTITY;
   }

   public ImpactEffect getImpact() {
      return this.impact;
   }

   public void setLocation(Location var1) {
   }

   public Location getLocation() {
      return null;
   }

   public void setImpactType(ImpactEffect var1) {
      this.impact = var1;
   }

   public EntityProjectile copy() {
      EntityProjectile var1 = new EntityProjectile(this.spawnLocation, this.velocityMultiplier, this.type, this.impact);
      var1.entity = this.entity;
      return var1;
   }
}
