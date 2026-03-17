package advancedplugins.pm2.cv.api.vehicle.projectile.projectiles.impl;

import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.projectile.ProjectileBuilderEditableField;
import advancedplugins.pm2.cv.api.vehicle.projectile.impacts.DefaultImpactEffects;
import advancedplugins.pm2.cv.api.vehicle.projectile.impacts.ImpactEffect;
import advancedplugins.pm2.cv.api.vehicle.projectile.projectiles.VehicleProjectile;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockProjectile implements VehicleProjectile {
   public static NamespacedKey FALLING_BLOCK_KEY = new NamespacedKey(JavaPlugin.getProvidingPlugin(BlockProjectile.class), "disable-block-spawn");
   private ImpactEffect impact;
   @ProjectileBuilderEditableField
   private BlockData blockData;
   private Location spawnLocation;
   @ProjectileBuilderEditableField
   private Vector velocityMultiplier;
   @ProjectileBuilderEditableField
   private FallingBlock display;
   private boolean isFinished = false;

   public BlockProjectile(Location var1, @Nullable Vector var2, @NotNull BlockData var3, @Nullable ImpactEffect var4) {
      this.spawnLocation = var1;
      this.blockData = var3;
      this.velocityMultiplier = var2 == null ? new Vector(1, 1, 1) : var2;
      this.impact = var4 == null ? DefaultImpactEffects.damageEntity(5.0D) : var4;
   }

   public BlockProjectile() {
      this.blockData = Material.SAND.createBlockData();
      this.spawnLocation = new Location((World)Bukkit.getWorlds().get(0), 0.0D, 0.0D, 0.0D);
      this.velocityMultiplier = new Vector(0, 0, 0);
      this.impact = DefaultImpactEffects.damageEntity(0.0D);
   }

   public void spawn(Location var1, Vehicle var2) {
      this.spawnLocation = var1;
      this.display = this.spawnLocation.getWorld().spawnFallingBlock(this.spawnLocation, this.blockData);
      this.display.setGravity(true);
      this.display.setDropItem(false);
      this.display.setVelocity(this.spawnLocation.getDirection().multiply(this.velocityMultiplier));
      this.display.getPersistentDataContainer().set(FALLING_BLOCK_KEY, PersistentDataType.BOOLEAN, true);
      VehicleProjectile.super.spawn(var1, var2);
   }

   public void write(ConfigurationSection var1) {
      ConfigurationUtil.writeEnum(this.getType(), var1, "type");
      this.impact.write(var1.createSection("impact-type"));
      ConfigurationUtil.writeVector(this.velocityMultiplier, var1, "velocity-multiplier");
      var1.set("block-data", this.blockData.getAsString(true));
   }

   public void load(ConfigurationSection var1) {
      this.impact = ImpactEffect.read(var1.getConfigurationSection("impact-type"));
      this.velocityMultiplier = ConfigurationUtil.loadVector(var1, "velocity-multiplier");
      ConfigurationUtil.loadEnum(EntityType.class, var1, "entity");
      this.blockData = Bukkit.getServer().createBlockData(var1.getString("block-data"));
   }

   public void tick(long var1) {
      if (this.display == null) {
         this.finish();
      }

      Collection var3 = this.display.getWorld().getNearbyEntities(this.display.getLocation(), 1.0D, 1.0D, 1.0D, (var0) -> {
         return var0 instanceof LivingEntity;
      });
      if (!var3.isEmpty()) {
         this.impact.apply(var3);
         this.finish();
      }

      Block var4 = this.display.getLocation().clone().add(0.0D, -1.0D, 0.0D).getBlock();
      if (!var4.getType().isAir()) {
         this.impact.apply(var4);
         this.finish();
      } else {
         for(double var5 = -0.5D; var5 < 1.5D; var5 += 0.5D) {
            for(double var7 = -0.49D; var7 < 1.0D; var7 += 0.5D) {
               for(double var9 = -0.5D; var9 < 1.5D; var9 += 0.5D) {
                  Block var11 = this.display.getLocation().clone().add(var5, var7, var9).getBlock();
                  if (!var11.getType().isAir()) {
                     this.impact.apply(var11);
                     this.finish();
                  }
               }
            }
         }

      }
   }

   public void setLocation(Location var1) {
      this.spawnLocation = var1;
   }

   public Location getLocation() {
      return this.spawnLocation;
   }

   public void finish() {
      this.isFinished = true;
      if (this.display != null) {
         this.display.getWorld().playEffect(this.display.getLocation(), Effect.STEP_SOUND, this.display.getBlockData().getMaterial());
         this.display.remove();
      }

   }

   public boolean isFinished() {
      return this.isFinished;
   }

   public VehicleProjectile.VehicleProjectileType getType() {
      return VehicleProjectile.VehicleProjectileType.BLOCK;
   }

   public ImpactEffect getImpact() {
      return this.impact;
   }

   public void setImpactType(ImpactEffect var1) {
      this.impact = var1;
   }

   public BlockProjectile copy() {
      BlockProjectile var1 = new BlockProjectile(this.spawnLocation, this.velocityMultiplier, this.blockData, this.impact);
      var1.display = this.display;
      return var1;
   }
}
