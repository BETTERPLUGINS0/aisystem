package advancedplugins.pm2.cv.fake.display;

import advancedplugins.pm2.cv.enums.EnumDisplayEntity;
import advancedplugins.pm2.cv.enums.EnumDisplayProperty;
import advancedplugins.pm2.cv.fake.FakeEntity;
import advancedplugins.pm2.cv.fake.FakeEntityLinker;
import advancedplugins.pm2.cv.nms.NmsImplementations;
import advancedplugins.pm2.cv.util.math.DisplayMathUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public abstract class FakeDisplay extends FakeEntity<EnumDisplayProperty, FakeDisplayHandle> {
   @NotNull
   protected Matrix4f transformation = new Matrix4f();

   public static FakeDisplay createDisplay(@NotNull World world, @NotNull EnumDisplayEntity kind) {
      Object var10000;
      switch(var1) {
      case ITEM:
         var10000 = new FakeDisplayItem(var0);
         break;
      case BLOCK:
         var10000 = new FakeDisplayBlock(var0);
         break;
      case TEXT:
         var10000 = new FakeDisplayText(var0);
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return (FakeDisplay)var10000;
   }

   public FakeDisplay(@NotNull World world) {
      super(EnumDisplayProperty.class, var1);
   }

   @NotNull
   public abstract EnumDisplayEntity getKind();

   protected FakeDisplayHandle createHandle(@NotNull World world) {
      Class var10000;
      switch((EnumDisplayEntity)Objects.requireNonNull(this.getKind(), "getKind() returned null")) {
      case ITEM:
         var10000 = NmsImplementations.getImplementation(FakeDisplayItemHandle.class);
         break;
      case BLOCK:
         var10000 = NmsImplementations.getImplementation(FakeDisplayBlockHandle.class);
         break;
      case TEXT:
         var10000 = NmsImplementations.getImplementation(FakeDisplayTextHandle.class);
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      Class var2 = var10000;

      try {
         Constructor var3 = var2.getConstructor(World.class);
         return (FakeDisplayHandle)var3.newInstance(var1);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException var4) {
         throw new IllegalStateException("couldn't create fake display handle", var4);
      }
   }

   public void setTransformation(@Nullable Matrix4f transformation, boolean send, boolean interpolate) {
      this.transformation = var1 != null ? new Matrix4f(var1) : new Matrix4f();
      ((FakeDisplayHandle)this.handle).applyTransformation(this.transformation);
      if (var2) {
         this.sendTransformation(var3);
      }

   }

   public void setTransformation(Transformation transformation) {
      ((FakeDisplayHandle)this.handle).applyProperty(EnumDisplayProperty.TRANSFORMATION, var1);
   }

   public void setTransformation(@Nullable Matrix4f transformation, boolean interpolate) {
      this.setTransformation(var1, true, var2);
   }

   public void setTransformation(@Nullable Vector3D translation, @Nullable Vector3D rotation, @Nullable Vector3D scale, boolean send, boolean interpolate) {
      this.transformation = new Matrix4f();
      if (var1 != null) {
         this.transformation.translation(DisplayMathUtil.migrate(var1));
      }

      if (var2 != null) {
         Quaternionf var6 = new Quaternionf();
         var6.rotationXYZ((float)var2.getX(), (float)var2.getY(), (float)var2.getZ());
         this.transformation.rotate(var6);
      }

      if (var3 != null) {
         this.transformation.scale(DisplayMathUtil.migrate(var3));
      }

      ((FakeDisplayHandle)this.handle).applyTransformation(this.transformation);
      if (var4) {
         this.sendTransformation(var5);
      }

   }

   public void setTransformation(@Nullable Vector3D translation, @Nullable Vector3D rotation, @Nullable Vector3D scale, boolean interpolate) {
      this.setTransformation(var1, var2, var3, true, var4);
   }

   public void sendTransformation(boolean interpolate) {
      if (var1) {
         this.setProperty(EnumDisplayProperty.TRANSFORM_INTERPOLATION_DELAY, this.getPropertyRaw(EnumDisplayProperty.TRANSFORM_INTERPOLATION_DELAY), true, false);
      }

      this.sendMetadata(false);
   }

   public void trickySetInvisibleTo(@NotNull Player player, boolean invisible, @Nullable FakeEntityLinker.Generic linker) {
      ((FakeDisplayHandle)this.handle).trickySetInvisibleTo(var1, var2, var3);
   }

   public void trickySetInvisibleTo(@NotNull Player player, boolean invisible) {
      this.trickySetInvisibleTo(var1, var2, (FakeEntityLinker.Generic)null);
   }

   @NotNull
   public Matrix4f getTransformation() {
      return this.transformation;
   }
}
