package advancedplugins.pm2.cv.models.v1_21_R10.entity.hitbox;

import advancedplugins.pm2.cv.models.api.utils.math.OrientedBoundingBox;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class OrientedBoundingVolume extends AABB {
   private final Quaternionf orientation;
   private final float rotationAngle;
   private final OrientedBoundingBox wrappedBounds;

   public OrientedBoundingVolume(Vec3 var1, Vec3 var2, Quaternionf var3, float var4) {
      this(var1.x, var1.y, var1.z, var2.x, var2.y, var2.z, var3, var4);
   }

   public OrientedBoundingVolume(double var1, double var3, double var5, double var7, double var9, double var11, Quaternionf var13, float var14) {
      super(var1, var3, var5, var7, var9, var11);
      this.orientation = var13;
      this.rotationAngle = var14;
      this.wrappedBounds = this.createWrappedBounds();
   }

   private OrientedBoundingBox createWrappedBounds() {
      Vector3f var1 = this.getCenter().toVector3f();
      Vector3f var2 = new Vector3f((float)this.getXsize(), (float)this.getYsize(), (float)this.getZsize());
      return new OrientedBoundingBox(var1, var2, this.orientation, this.rotationAngle);
   }

   public OrientedBoundingVolume createTransformedInstance(Vec3 var1, Quaternionf var2, float var3) {
      Vec3 var4 = var1.add(this.minX, this.minY, this.minZ);
      Vec3 var5 = var1.add(this.maxX, this.maxY, this.maxZ);
      return new OrientedBoundingVolume(var4, var5, var2, var3);
   }

   @NotNull
   public AABB inflate(double var1, double var3, double var5) {
      double var7 = this.minX - var1;
      double var9 = this.minY - var3;
      double var11 = this.minZ - var5;
      double var13 = this.maxX + var1;
      double var15 = this.maxY + var3;
      double var17 = this.maxZ + var5;
      return new OrientedBoundingVolume(var7, var9, var11, var13, var15, var17, this.orientation, this.rotationAngle);
   }

   public boolean intersects(@NotNull AABB var1) {
      return var1 instanceof OrientedBoundingVolume ? this.checkOrientedIntersection((OrientedBoundingVolume)var1) : super.intersects(var1);
   }

   public boolean checkOrientedIntersection(OrientedBoundingVolume var1) {
      return this.wrappedBounds.intersects(var1.wrappedBounds);
   }

   public boolean intersects(double var1, double var3, double var5, double var7, double var9, double var11) {
      OrientedBoundingBox var13 = new OrientedBoundingBox((float)var1, (float)var3, (float)var5, (float)var7, (float)var9, (float)var11);
      return this.wrappedBounds.intersects(var13);
   }

   public Optional<Vec3> clip(Vec3 var1, Vec3 var2) {
      Quaternionf var3 = this.orientation.conjugate(new Quaternionf());
      Vec3 var4 = this.getCenter();
      float var5 = this.rotationAngle * 0.017453292F;
      Vec3 var6 = this.transformPointToLocalSpace(var1, var4, var5, var3);
      Vec3 var7 = this.transformPointToLocalSpace(var2, var4, var5, var3);
      Optional var8 = super.clip(var6, var7);
      if (var8.isEmpty()) {
         return var8;
      } else {
         Vec3 var9 = this.transformPointToWorldSpace((Vec3)var8.get(), var4, var5);
         return Optional.of(var9);
      }
   }

   private Vec3 transformPointToLocalSpace(Vec3 var1, Vec3 var2, float var3, Quaternionf var4) {
      Vector3f var5 = var1.subtract(var2).toVector3f();
      Vector3f var6 = var5.rotateY(-var3).rotate(var4);
      return (new Vec3(var6)).add(var2);
   }

   private Vec3 transformPointToWorldSpace(Vec3 var1, Vec3 var2, float var3) {
      Vector3f var4 = var1.subtract(var2).toVector3f();
      Vector3f var5 = var4.rotate(this.orientation).rotateY(var3);
      return (new Vec3(var5)).add(var2);
   }

   public OrientedBoundingBox getBukkitOBB() {
      return this.wrappedBounds;
   }

   public OrientedBoundingVolume makeOBBInstance(Vec3 var1, Quaternionf var2, float var3) {
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
