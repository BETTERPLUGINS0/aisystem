package advancedplugins.pm2.cv.fake.armorstand;

import advancedplugins.pm2.cv.api.enums.EnumRotableLimb;
import advancedplugins.pm2.cv.api.enums.EnumStandSlot;
import advancedplugins.pm2.cv.enums.EnumStandProperty;
import advancedplugins.pm2.cv.fake.FakeEntity;
import advancedplugins.pm2.cv.nms.NmsImplementations;
import advancedplugins.pm2.cv.util.math.TrigonometryUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.bukkit.World;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakeArmorStand extends FakeEntity<EnumStandProperty, FakeArmorStandHandle> {
   protected final ItemStack[] equipment = new ItemStack[EquipmentSlot.values().length];
   @NotNull
   protected final Vector3D[] limbRotations = new Vector3D[EnumRotableLimb.values().length];

   public FakeArmorStand(@NotNull World world) {
      super(EnumStandProperty.class, var1);

      for(int var2 = 0; var2 < this.properties.length; ++var2) {
         this.properties[var2] = EnumStandProperty.values()[var2].getDefaultValueRaw();
      }

      EnumRotableLimb[] var6 = EnumRotableLimb.values();
      int var3 = var6.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumRotableLimb var5 = var6[var4];
         this.limbRotations[var5.ordinal()] = var5.getDefaultRotation();
      }

   }

   protected FakeArmorStandHandle createHandle(@NotNull World world) {
      Class var2 = NmsImplementations.getImplementation(FakeArmorStandHandle.class);

      try {
         Constructor var3 = var2.getConstructor(World.class);
         return (FakeArmorStandHandle)var3.newInstance(var1);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException var4) {
         throw new IllegalStateException("couldn't create fake armor stand handle", var4);
      }
   }

   @Nullable
   public ItemStack getEquipment(@NotNull EnumStandSlot slot) {
      return this.equipment[var1.ordinal()];
   }

   public void setEquipment(@NotNull EnumStandSlot slot, @Nullable ItemStack value, boolean send) {
      if (!Objects.equals(this.equipment[var1.ordinal()], var2)) {
         this.equipment[var1.ordinal()] = var2;
         ((FakeArmorStandHandle)this.handle).applyEquipment(var1, var2);
         if (var3) {
            ((FakeArmorStandHandle)this.handle).sendEquipment();
         }
      }

   }

   public void setEquipment(@NotNull EnumStandSlot slot, @Nullable ItemStack value) {
      this.setEquipment(var1, var2, true);
   }

   public void setAllEquipment(@Nullable Map<EnumStandSlot, ItemStack> all, boolean send) {
      EnumStandSlot[] var3 = EnumStandSlot.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumStandSlot var6 = var3[var5];
         this.setEquipment(var6, var1 != null ? (ItemStack)var1.getOrDefault(var6, (Object)null) : null, false);
      }

      if (var2) {
         ((FakeArmorStandHandle)this.handle).sendEquipment();
      }

   }

   public void sendEquipment() {
      ((FakeArmorStandHandle)this.handle).sendEquipment();
   }

   @NotNull
   public Vector3D getLimbRotation(@NotNull EnumRotableLimb limb) {
      return this.limbRotations[var1.ordinal()];
   }

   public void setLimbRotation(@NotNull EnumRotableLimb limb, @NotNull Vector3D rotation, boolean send) {
      Vector3D var4 = this.fixLimbRotation(var2);
      if (!Objects.equals(var4, this.getLimbRotation(var1))) {
         this.limbRotations[var1.ordinal()] = var2;
         ((FakeArmorStandHandle)this.handle).applyLimbRotation(var1, (float)var4.getX(), (float)var4.getY(), (float)var4.getZ());
         if (var3) {
            this.sendMetadata(false);
         }
      }

   }

   public void setLimbRotation(@NotNull EnumRotableLimb limb, @NotNull Vector3D rotation) {
      this.setLimbRotation(var1, var2, true);
   }

   public void setLimbRotation(@NotNull EnumRotableLimb limb, float x, float y, float z, boolean send) {
      this.setLimbRotation(var1, new Vector3D((double)var2, (double)var3, (double)var4), var5);
   }

   public void setLimbRotation(@NotNull EnumRotableLimb limb, float x, float y, float z) {
      this.setLimbRotation(var1, var2, var3, var4, true);
   }

   protected Vector3D fixLimbRotation(Vector3D rotation) {
      return new Vector3D((double)this.fixLimbRotationComponent((float)var1.getX()), (double)this.fixLimbRotationComponent((float)var1.getY()), (double)this.fixLimbRotationComponent((float)var1.getZ()));
   }

   protected float fixLimbRotationComponent(float component) {
      return !Float.isInfinite(var1) && !Float.isNaN(var1) ? TrigonometryUtil.normalize(var1) : 0.0F;
   }
}
