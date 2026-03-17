package advancedplugins.pm2.cv.models.v1_21_R6.entity.hitbox;

import advancedplugins.pm2.cv.models.api.utils.math.OrientedBoundingBox;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.Vec3D;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class OrientedBoundingVolume extends AxisAlignedBB {
   private final Quaternionf orientation;
   private final float rotationAngle;
   private final OrientedBoundingBox wrappedBounds;

   public OrientedBoundingVolume(Vec3D var1, Vec3D var2, Quaternionf var3, float var4) {
      this(var1.d, var1.e, var1.f, var2.d, var2.e, var2.f, var3, var4);
   }

   public OrientedBoundingVolume(double var1, double var3, double var5, double var7, double var9, double var11, Quaternionf var13, float var14) {
      super(var1, var3, var5, var7, var9, var11);
      this.orientation = var13;
      this.rotationAngle = var14;
      this.wrappedBounds = this.createWrappedBounds();
   }

   private OrientedBoundingBox createWrappedBounds() {
      Vector3f var1 = this.f().l();
      Vector3f var2 = new Vector3f((float)this.b(), (float)this.c(), (float)this.d());
      return new OrientedBoundingBox(var1, var2, this.orientation, this.rotationAngle);
   }

   public OrientedBoundingVolume createTransformedInstance(Vec3D var1, Quaternionf var2, float var3) {
      Vec3D var4 = var1.b(this.a, this.b, this.c);
      Vec3D var5 = var1.b(this.d, this.e, this.f);
      return new OrientedBoundingVolume(var4, var5, var2, var3);
   }

   @NotNull
   public AxisAlignedBB c(double var1, double var3, double var5) {
      double var7 = this.a - var1;
      double var9 = this.b - var3;
      double var11 = this.c - var5;
      double var13 = this.d + var1;
      double var15 = this.e + var3;
      double var17 = this.f + var5;
      return new OrientedBoundingVolume(var7, var9, var11, var13, var15, var17, this.orientation, this.rotationAngle);
   }

   public boolean c(@NotNull AxisAlignedBB var1) {
      return var1 instanceof OrientedBoundingVolume ? this.checkOrientedIntersection((OrientedBoundingVolume)var1) : super.c(var1);
   }

   public boolean checkOrientedIntersection(OrientedBoundingVolume var1) {
      return this.wrappedBounds.intersects(var1.wrappedBounds);
   }

   public boolean a(double var1, double var3, double var5, double var7, double var9, double var11) {
      OrientedBoundingBox var13 = new OrientedBoundingBox((float)var1, (float)var3, (float)var5, (float)var7, (float)var9, (float)var11);
      return this.wrappedBounds.intersects(var13);
   }

   public Optional<Vec3D> b(Vec3D var1, Vec3D var2) {
      Quaternionf var3 = this.orientation.conjugate(new Quaternionf());
      Vec3D var4 = this.f();
      float var5 = this.rotationAngle * 0.017453292F;
      Vec3D var6 = this.transformPointToLocalSpace(var1, var4, var5, var3);
      Vec3D var7 = this.transformPointToLocalSpace(var2, var4, var5, var3);
      Optional var8 = super.b(var6, var7);
      if (var8.isEmpty()) {
         return var8;
      } else {
         Vec3D var9 = this.transformPointToWorldSpace((Vec3D)var8.get(), var4, var5);
         return Optional.of(var9);
      }
   }

   private Vec3D transformPointToLocalSpace(Vec3D var1, Vec3D var2, float var3, Quaternionf var4) {
      Vector3f var5 = var1.d(var2).l();
      Vector3f var6 = var5.rotateY(-var3).rotate(var4);
      return (new Vec3D(var6)).e(var2);
   }

   private Vec3D transformPointToWorldSpace(Vec3D var1, Vec3D var2, float var3) {
      Vector3f var4 = var1.d(var2).l();
      Vector3f var5 = var4.rotate(this.orientation).rotateY(var3);
      return (new Vec3D(var5)).e(var2);
   }

   public OrientedBoundingBox getBukkitOBB() {
      return this.wrappedBounds;
   }

   public OrientedBoundingVolume makeOBBInstance(Vec3D var1, Quaternionf var2, float var3) {
      return this.createTransformedInstance(var1, var2, var3);
   }

   @Generated
   public Quaternionf getOrientation() {
      return this.orientation;
   }

   @Generated
   public float getRotationAngle() {
      return this.rotationAngle;
   }

   @Generated
   public OrientedBoundingBox getWrappedBounds() {
      return this.wrappedBounds;
   }
}
