package advancedplugins.pm2.cv.models.v1_21_R5.entity.hitbox;

import advancedplugins.pm2.cv.models.api.utils.math.OrientedBoundingBox;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class OBB extends AABB {
   private final Quaternionf rotation;
   private final float yaw;
   private final OrientedBoundingBox bukkitOBB;

   public OBB(Vec3 var1, Vec3 var2, Quaternionf var3, float var4) {
      this(var1.x, var1.y, var1.z, var2.x, var2.y, var2.z, var3, var4);
   }

   public OBB(double var1, double var3, double var5, double var7, double var9, double var11, Quaternionf var13, float var14) {
      super(var1, var3, var5, var7, var9, var11);
      this.rotation = var13;
      this.yaw = var14;
      this.bukkitOBB = new OrientedBoundingBox(this.getCenter().toVector3f(), new Vector3f((float)this.getXsize(), (float)this.getYsize(), (float)this.getZsize()), var13, var14);
   }

   public OBB makeOBBInstance(Vec3 var1, Quaternionf var2, float var3) {
      return new OBB(var1.add(this.minX, this.minY, this.minZ), var1.add(this.maxX, this.maxY, this.maxZ), var2, var3);
   }

   @NotNull
   public AABB inflate(double var1, double var3, double var5) {
      double var7 = this.minX - var1;
      double var9 = this.minY - var3;
      double var11 = this.minZ - var5;
      double var13 = this.maxX + var1;
      double var15 = this.maxY + var3;
      double var17 = this.maxZ + var5;
      return new OBB(var7, var9, var11, var13, var15, var17, this.rotation, this.yaw);
   }

   public boolean intersects(@NotNull AABB var1) {
      boolean var2;
      if (var1 instanceof OBB) {
         OBB var3 = (OBB)var1;
         var2 = this.intersects(var3);
      } else {
         var2 = super.intersects(var1);
      }

      return var2;
   }

   public boolean intersects(OBB var1) {
      return this.bukkitOBB.intersects(var1.bukkitOBB);
   }

   public boolean intersects(double var1, double var3, double var5, double var7, double var9, double var11) {
      OrientedBoundingBox var13 = new OrientedBoundingBox((float)var1, (float)var3, (float)var5, (float)var7, (float)var9, (float)var11);
      return this.bukkitOBB.intersects(var13);
   }

   public Optional<Vec3> clip(Vec3 var1, Vec3 var2) {
      Quaternionf var3 = this.rotation.conjugate(new Quaternionf());
      Vec3 var4 = this.getCenter();
      float var5 = this.yaw * 0.017453292F;
      Vec3 var6 = (new Vec3(var1.subtract(var4).toVector3f().rotateY(-var5).rotate(var3))).add(var4);
      Vec3 var7 = (new Vec3(var2.subtract(var4).toVector3f().rotateY(-var5).rotate(var3))).add(var4);
      Optional var8 = super.clip(var6, var7);
      if (var8.isEmpty()) {
         return var8;
      } else {
         Vec3 var9 = (new Vec3(((Vec3)var8.get()).subtract(var4).toVector3f().rotate(this.rotation).rotateY(var5))).add(var4);
         return Optional.of(var9);
      }
   }

   @Generated
   public Quaternionf getRotation() {
      return this.rotation;
   }

   @Generated
   public float getYaw() {
      return this.yaw;
   }

   @Generated
   public OrientedBoundingBox getBukkitOBB() {
      return this.bukkitOBB;
   }
}
