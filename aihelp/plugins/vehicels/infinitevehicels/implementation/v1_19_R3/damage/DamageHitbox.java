package implementation.v1_19_R3.damage;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.enums.EnumDamageType;
import advancedplugins.pm2.cv.api.util.Run;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import java.lang.reflect.Field;
import java.util.Objects;
import me.PM2.infinitevehicles.math.util.FastMath;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.attributes.AttributeModifiable;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.monster.EntitySlime;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftSlime;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DamageHitbox extends EntitySlime implements advancedplugins.pm2.cv.damage.DamageHitbox {
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

   public DamageHitbox(@NotNull World var1) {
      super(EntityTypes.aL, ((CraftWorld)var1).getHandle());
      this.aware = false;
      this.persist = false;
      this.t(true);
      this.e(true);
      this.a(1, false);
      this.setPersistenceRequired(true);
      Run.sync(() -> {
         try {
            CraftSlime var1 = (CraftSlime)this.getBukkitEntity();
            var1.setInvisible(true);
         } catch (Exception var2) {
            this.b(new MobEffect(MobEffects.n, Integer.MAX_VALUE, 0, false, false, false));
         }

      });
      this.d(true);
      ((AttributeModifiable)Objects.requireNonNull(this.a(GenericAttributes.a))).a(Double.MAX_VALUE);
      super.c(Float.MAX_VALUE);
      this.setWidth(0.15F);
      this.setHeight(0.15F);
   }

   public int getHandleId() {
      return this.af();
   }

   public void c(float var1) {
   }

   public void setVehicle(@NotNull Vehicle var1) {
      this.getBukkitEntity().setMetadata("crafty-vehicles-damage-hitbox-handle", new FixedMetadataValue(InfiniteVehicles.getPlugin(), var1));
   }

   public boolean a(DamageSource var1, float var2) {
      if (this.listener != null) {
         try {
            EnumDamageType var3 = this.getType(var1);
            Entity var4 = var1.d();
            Entity var5 = var1.c();
            this.listener.notify(var3, var2, var4 != null ? var4.getBukkitEntity() : null, var5 != null ? var5.getBukkitEntity() : null, this.getBukkitEntity());
         } catch (Throwable var6) {
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
               if (var1.k().a(var6.a())) {
                  return EnumDamageType.match(var6.a().a());
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
            this.p(this.x, this.y, this.z);
            this.Y().addFreshEntity(this, SpawnReason.CUSTOM);
         };
         if (Bukkit.isPrimaryThread()) {
            var1.run();
         } else {
            Run.sync(var1);
         }

      }
   }

   public void destroy() {
      Runnable var1 = this::ai;
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

   public void l() {
      if (this.dimensionsDirty) {
         this.dimensionsDirty = false;
         this.locationDirty = true;
         this.applySize();
         this.c_();
         this.a(this.am());
      }

      if (this.locationDirty) {
         this.locationDirty = false;
         this.e(this.x, this.y, this.z);
      }

      if (this.orientationDirty) {
         this.orientationDirty = false;
         this.a(this.orientation, 0.0F);
         this.L = this.orientation;
         this.s(this.orientation);
         this.r(this.orientation);
      }

      super.l();
   }

   private void applySize() {
      this.a((int)FastMath.floor((double)(this.width * 2.0F)), false);
      ((AttributeModifiable)Objects.requireNonNull(this.a(GenericAttributes.a))).a(Double.MAX_VALUE);
      this.c(Float.MAX_VALUE);
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
