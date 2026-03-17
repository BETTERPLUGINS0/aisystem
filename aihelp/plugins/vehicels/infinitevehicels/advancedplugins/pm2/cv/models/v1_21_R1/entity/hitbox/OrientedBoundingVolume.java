package advancedplugins.pm2.cv.models.v1_21_R1.entity.hitbox;

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

   public OrientedBoundingVolume(Vec3D firstCorner, Vec3D secondCorner, Quaternionf orientation, float rotationAngle) {
      this(var1.c, var1.d, var1.e, var2.c, var2.d, var2.e, var3, var4);
   }

   public OrientedBoundingVolume(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Quaternionf orientation, float rotationAngle) {
      super(var1, var3, var5, var7, var9, var11);
      this.orientation = var13;
      this.rotationAngle = var14;
      this.wrappedBounds = this.createWrappedBounds();
   }

   private OrientedBoundingBox createWrappedBounds() {
      Vector3f var1 = this.f().j();
      Vector3f var2 = new Vector3f((float)this.b(), (float)this.c(), (float)this.d());
      return new OrientedBoundingBox(var1, var2, this.orientation, this.rotationAngle);
   }

   public OrientedBoundingVolume createTransformedInstance(Vec3D position, Quaternionf newOrientation, float newRotation) {
      Vec3D var4 = var1.b(this.a, this.b, this.c);
      Vec3D var5 = var1.b(this.d, this.e, this.f);
      return new OrientedBoundingVolume(var4, var5, var2, var3);
   }

   @NotNull
   public AxisAlignedBB c(double xExpansion, double yExpansion, double zExpansion) {
      double var7 = this.a - var1;
      double var9 = this.b - var3;
      double var11 = this.c - var5;
      double var13 = this.d + var1;
      double var15 = this.e + var3;
      double var17 = this.f + var5;
      return new OrientedBoundingVolume(var7, var9, var11, var13, var15, var17, this.orientation, this.rotationAngle);
   }

   public boolean c(@NotNull AxisAlignedBB other) {
      return var1 instanceof OrientedBoundingVolume ? this.checkOrientedIntersection((OrientedBoundingVolume)var1) : super.c(var1);
   }

   public boolean checkOrientedIntersection(OrientedBoundingVolume other) {
      return this.wrappedBounds.intersects(var1.wrappedBounds);
   }

   public boolean a(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
      OrientedBoundingBox var13 = new OrientedBoundingBox((float)var1, (float)var3, (float)var5, (float)var7, (float)var9, (float)var11);
      return this.wrappedBounds.intersects(var13);
   }

   public Optional<Vec3D> b(Vec3D startPoint, Vec3D endPoint) {
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

   private Vec3D transformPointToLocalSpace(Vec3D point, Vec3D center, float radianAngle, Quaternionf inverseOrientation) {
      Vector3f var5 = var1.d(var2).j();
      Vector3f var6 = var5.rotateY(-var3).rotate(var4);
      return (new Vec3D(var6)).e(var2);
   }

   private Vec3D transformPointToWorldSpace(Vec3D localPoint, Vec3D center, float radianAngle) {
      Vector3f var4 = var1.d(var2).j();
      Vector3f var5 = var4.rotate(this.orientation).rotateY(var3);
      return (new Vec3D(var5)).e(var2);
   }

   public OrientedBoundingBox getBukkitOBB() {
      return this.wrappedBounds;
   }

   public OrientedBoundingVolume makeOBBInstance(Vec3D position, Quaternionf rotation, float yaw) {
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
