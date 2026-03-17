package implementation.v1_21_R6.damage;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.enums.EnumDamageType;
import advancedplugins.pm2.cv.api.util.Run;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import java.lang.reflect.Field;
import java.util.Objects;
import me.PM2.infinitevehicles.math.util.FastMath;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftSlime;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DamageHitbox extends Slime implements advancedplugins.pm2.cv.damage.DamageHitbox {
   @Nullable
   private advancedplugins.pm2.cv.damage.DamageHitbox.DamageListener listener;
   private boolean spawned;
   private double x;
   private double y;
   private double z;
   private boolean locationDirty;
   private float orientation;
   private boolean orientationDirty;
   private float width;
   private float height;
   private boolean dimensionsDirty;
   private final ServerLevel worldHandle;

   public DamageHitbox(@NotNull World var1) {
      super(EntityType.SLIME, ((CraftWorld)var1).getHandle());
      this.worldHandle = ((CraftWorld)var1).getHandle();
      this.aware = false;
      this.persist = false;
      this.setNoAi(true);
      this.setNoGravity(true);
      this.setSize(1, false);
      this.setPersistenceRequired(true);
      Run.sync(() -> {
         try {
            CraftSlime var1 = (CraftSlime)this.getBukkitEntity();
            var1.setInvisible(true);
         } catch (Exception var2) {
            this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, false, false, false));
         }

      });
      this.setSilent(true);
      ((AttributeInstance)Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH))).setBaseValue(Double.MAX_VALUE);
      super.setHealth(Float.MAX_VALUE);
      this.setWidth(0.15F);
      this.setHeight(0.15F);
   }

   public int getHandleId() {
      return this.getId();
   }

   public void setVehicle(@NotNull Vehicle var1) {
      this.getBukkitEntity().setMetadata("crafty-vehicles-damage-hitbox-handle", new FixedMetadataValue(InfiniteVehicles.getPlugin(), var1));
   }

   public void setHealth(float var1) {
   }

   protected boolean actuallyHurt(DamageSource var1, float var2) {
      return true;
   }

   public boolean hurtServer(ServerLevel var1, DamageSource var2, float var3) {
      if (this.listener != null) {
         try {
            EnumDamageType var4 = this.getType(var2);
            Entity var5 = var2.getEntity();
            Entity var6 = var2.getDirectEntity();
            this.listener.notify(var4, var3, var5 != null ? var5.getBukkitEntity() : null, var6 != null ? var6.getBukkitEntity() : null, this.getBukkitEntity());
         } catch (Throwable var7) {
         }
      }

      return true;
   }

   @Nullable
   private EnumDamageType getType(DamageSource var1) {
      Field[] var2 = DamageTypes.class.getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field var5 = var2[var4];
         if (var5.getType().isAssignableFrom(ResourceKey.class)) {
            try {
               ResourceKey var6 = (ResourceKey)var5.get((Object)null);
               if (var1.typeHolder().is(var6.location())) {
                  return EnumDamageType.match(var6.location().getPath());
               }
            } catch (IllegalArgumentException | IllegalAccessException var7) {
            }
         }
      }

      return null;
   }

   public void spawn() {
      if (!this.spawned) {
         this.spawned = true;
         Runnable var1 = () -> {
            this.setPosRaw(this.x, this.y, this.z);
            this.worldHandle.addFreshEntity(this);
         };
         if (Bukkit.isPrimaryThread()) {
            var1.run();
         } else {
            Run.sync(var1);
         }

      }
   }

   public void destroy() {
      Runnable var1 = this::discard;
      if (Bukkit.isPrimaryThread()) {
         var1.run();
      } else {
         Run.sync(var1);
      }

   }

   public Vector getLocation() {
      return new Vector(this.x, this.y, this.z);
   }

   public void setLocation(double var1, double var3, double var5) {
      if (Double.compare(var1, this.x) != 0 || Double.compare(var3, this.y) != 0 || Double.compare(var5, this.z) != 0) {
         this.x = var1;
         this.y = var3;
         this.z = var5;
         this.locationDirty = true;
      }
   }

   public void setOrientation(float var1) {
      if (Float.compare(this.orientation, var1) != 0) {
         this.orientation = var1;
         this.orientationDirty = true;
      }
   }

   public void tick() {
      if (this.dimensionsDirty) {
         this.dimensionsDirty = false;
         this.locationDirty = true;
         this.applySize();
         this.refreshDimensions();
         this.setBoundingBox(this.makeBoundingBox());
      }

      if (this.locationDirty) {
         this.locationDirty = false;
         this.setPos(this.x, this.y, this.z);
      }

      if (this.orientationDirty) {
         this.orientationDirty = false;
         this.setRot(this.orientation, 0.0F);
         this.yRotO = this.orientation;
         this.setYBodyRot(this.orientation);
         this.setYHeadRot(this.orientation);
      }

      super.tick();
   }

   private void applySize() {
      this.setSize((int)FastMath.floor((double)(this.width * 2.0F)), false);
      ((AttributeInstance)Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH))).setBaseValue(Double.MAX_VALUE);
      this.setHealth(Float.MAX_VALUE);
   }

   public void setWidth(float var1) {
      if (Float.compare(var1, this.width) != 0) {
         this.width = Math.min(Math.max(var1, 0.0F), 64.0F);
         this.dimensionsDirty = true;
      }

   }

   public void setHeight(float var1) {
      if (Float.compare(var1, this.height) != 0) {
         this.height = Math.min(Math.max(var1, 0.0F), 64.0F);
         this.dimensionsDirty = true;
      }

   }

   public void setListener(@Nullable advancedplugins.pm2.cv.damage.DamageHitbox.DamageListener var1) {
      this.listener = var1;
   }

   public float getWidth() {
      return this.width;
   }

   public float getHeight() {
      return this.height;
   }
}
