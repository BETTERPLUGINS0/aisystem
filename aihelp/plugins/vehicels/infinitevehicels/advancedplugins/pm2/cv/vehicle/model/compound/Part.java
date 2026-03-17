package advancedplugins.pm2.cv.vehicle.model.compound;

import advancedplugins.pm2.cv.api.util.inventory.ItemStackUtil;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.BoneConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.CompoundModelConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.PartConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.RigConfiguration;
import advancedplugins.pm2.cv.enums.EnumDisplayEntity;
import advancedplugins.pm2.cv.enums.EnumDisplayProperty;
import advancedplugins.pm2.cv.enums.EnumItemDisplaySlot;
import advancedplugins.pm2.cv.fake.display.FakeDisplay;
import advancedplugins.pm2.cv.fake.display.FakeDisplayHandle;
import advancedplugins.pm2.cv.util.ConvertUtil;
import java.util.Iterator;
import java.util.UUID;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Part {
   protected final UUID uuid = UUID.randomUUID();
   protected final CompoundModel model;
   protected final PartConfiguration configuration;
   protected final FakeDisplay display;
   protected Matrix4f initialTransformation;

   public Part(CompoundModel model, PartConfiguration configuration, World world, double x, double y, double z) {
      this.model = var1;
      this.configuration = var2;
      this.display = FakeDisplay.createDisplay(var3, EnumDisplayEntity.fromMaterial(var2.getMaterial()));
      if (this.display.getKind() == EnumDisplayEntity.BLOCK) {
         Material var10 = var2.getMaterial();
         Object var11 = var2.getData();
         BlockData var12 = var10.createBlockData();
         if (var11 instanceof BlockData) {
            BlockData var13 = (BlockData)var11;
            if (var13.getMaterial() == var10) {
               var12 = var13;
            }
         }

         this.display.setProperty(EnumDisplayProperty.BLOCK_DATA, var12, false);
      } else {
         this.display.setProperty(EnumDisplayProperty.ITEM_STACK, var2.getItemStack(), false);
         EnumItemDisplaySlot var14 = EnumItemDisplaySlot.FIXED;
         if (ItemStackUtil.isBanner(var2.getMaterial())) {
            var14 = EnumItemDisplaySlot.HEAD;
         }

         this.display.setProperty(EnumDisplayProperty.ITEM_DISPLAY_SLOT, var14, false);
      }

      this.display.setLocation(var4, var6, var8);
      this.display.sendMetadata(false);
      this.calculateInitialTransformation();
      this.applyInitialTransformation();
   }

   protected void show() {
      this.display.register();
   }

   public void destroy() {
      this.display.unregister();
   }

   public void applyInitialTransformation() {
      ((FakeDisplayHandle)this.display.handle).applyProperty(EnumDisplayProperty.TRANSFORM_INTERPOLATION_DELAY, 5);
      ((FakeDisplayHandle)this.display.handle).applyProperty(EnumDisplayProperty.TRANSFORM_INTERPOLATION_DURATION, 3);
      this.display.setTransformation(this.initialTransformation, true, false);
   }

   public boolean equals(Object o) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         Part var2 = (Part)var1;
         return this.uuid.equals(var2.uuid);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.uuid.hashCode();
   }

   private void calculateInitialTransformation() {
      RigConfiguration var1 = ((CompoundModelConfiguration)this.model.getConfiguration()).getRig();
      BoneConfiguration var2 = var1 != null && var1.contains(this.configuration) ? var1.getParent(this.configuration) : null;
      Vector3f var3 = this.calculateTranslation(var2, var1);
      Quaternionf var4 = this.calculateRotation(var2, var1);
      this.initialTransformation = new Matrix4f();
      this.initialTransformation.translation(var3);
      this.initialTransformation.rotate(var4);
      if (this.configuration.getScale() != null) {
         this.initialTransformation.scale(ConvertUtil.toVector3f(this.configuration.getScale()));
      }

   }

   private Vector3f calculateTranslation(@Nullable BoneConfiguration bone, @Nullable RigConfiguration rig) {
      Vector3D var3 = this.configuration.getOffset();
      if (var1 != null && var2 != null) {
         Vector3D var4 = var1.getPivot();
         Matrix4f var5 = new Matrix4f();
         var5.translate(ConvertUtil.toVector3f(this.calculatePivot(var1, var2)));
         var5.rotate(this.calculateBoneRotation(var1, var2));
         return ConvertUtil.toVector3f(RigMath.mulProject((var3 != null ? var3 : Vector3D.ZERO).subtract(var4), var5));
      } else {
         return ConvertUtil.toVector3f(var3 != null ? var3 : Vector3D.ZERO);
      }
   }

   private Quaternionf calculateRotation(@Nullable BoneConfiguration bone, RigConfiguration rig) {
      Quaternionf var3 = var1 != null ? this.calculateBoneRotation(var1, var2) : new Quaternionf();
      Vector3D var4 = this.configuration.getRotation();
      if (var4 != null) {
         RigMath.rotateXYZ(var3, var4);
      }

      return var3;
   }

   private Vector3D calculatePivot(BoneConfiguration bone, RigConfiguration rig) {
      Vector3D var3 = var1.getPivot();
      BoneConfiguration var4 = var2.getParent(var1);
      if (var4 == null) {
         return var3;
      } else {
         Vector3D var5 = var4.getPivot();
         Matrix4f var6 = new Matrix4f();
         var6.translate(ConvertUtil.toVector3f(this.calculatePivot(var4, var2)));
         var6.rotate(this.calculateBoneRotation(var4, var2));
         return RigMath.mulProject(var3.subtract(var5), var6);
      }
   }

   private Quaternionf calculateBoneRotation(BoneConfiguration bone, RigConfiguration rig) {
      Quaternionf var3 = new Quaternionf();
      Iterator var4 = var2.getHierarchyUp(var1).iterator();

      while(var4.hasNext()) {
         BoneConfiguration var5 = (BoneConfiguration)var4.next();
         Vector3D var6 = var5.getRotation();
         if (var6 != null) {
            RigMath.rotateXYZ(var3, var6);
         }
      }

      return var3;
   }

   public UUID getUuid() {
      return this.uuid;
   }

   public CompoundModel getModel() {
      return this.model;
   }

   public PartConfiguration getConfiguration() {
      return this.configuration;
   }

   public Matrix4f getInitialTransformation() {
      return this.initialTransformation;
   }

   public FakeDisplay getDisplay() {
      return this.display;
   }
}
