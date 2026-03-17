package advancedplugins.pm2.cv.models.v1_20_R1.entity.hitbox;

import advancedplugins.pm2.cv.models.api.utils.math.OrientedBoundingBox;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.Vec3D;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class OBB extends AxisAlignedBB {
   private final Quaternionf rotation;
   private final float yaw;
   private final OrientedBoundingBox bukkitOBB;

   public OBB(Vec3D cornerA, Vec3D cornerB, Quaternionf rotation, float yaw) {
      this(var1.c, var1.d, var1.e, var2.c, var2.d, var2.e, var3, var4);
   }

   public OBB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Quaternionf rotation, float yaw) {
      super(var1, var3, var5, var7, var9, var11);
      this.rotation = var13;
      this.yaw = var14;
      this.bukkitOBB = new OrientedBoundingBox(this.f().j(), new Vector3f((float)this.b(), (float)this.c(), (float)this.d()), var13, var14);
   }

   public OBB makeOBBInstance(Vec3D position, Quaternionf rotation, float yaw) {
      return new OBB(var1.b(this.a, this.b, this.c), var1.b(this.d, this.e, this.f), var2, var3);
   }

   @NotNull
   public AxisAlignedBB c(double xInflate, double yInflate, double zInflate) {
      double var7 = this.a - var1;
      double var9 = this.b - var3;
      double var11 = this.c - var5;
      double var13 = this.d + var1;
      double var15 = this.e + var3;
      double var17 = this.f + var5;
      return new OBB(var7, var9, var11, var13, var15, var17, this.rotation, this.yaw);
   }

   public boolean c(@NotNull AxisAlignedBB aabb) {
      boolean var2;
      if (var1 instanceof OBB) {
         OBB var3 = (OBB)var1;
         var2 = this.intersects(var3);
      } else {
         var2 = super.c(var1);
      }

      return var2;
   }

   public boolean intersects(OBB obb) {
      return this.bukkitOBB.intersects(var1.bukkitOBB);
   }

   public boolean a(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
      OrientedBoundingBox var13 = new OrientedBoundingBox((float)var1, (float)var3, (float)var5, (float)var7, (float)var9, (float)var11);
      return this.bukkitOBB.intersects(var13);
   }

   public Optional<Vec3D> b(Vec3D from, Vec3D to) {
      Quaternionf var3 = this.rotation.conjugate(new Quaternionf());
      Vec3D var4 = this.f();
      float var5 = this.yaw * 0.017453292F;
      Vec3D var6 = (new Vec3D(var1.d(var4).j().rotateY(-var5).rotate(var3))).e(var4);
      Vec3D var7 = (new Vec3D(var2.d(var4).j().rotateY(-var5).rotate(var3))).e(var4);
      Optional var8 = super.b(var6, var7);
      if (var8.isEmpty()) {
         return var8;
      } else {
         Vec3D var9 = (new Vec3D(((Vec3D)var8.get()).d(var4).j().rotate(this.rotation).rotateY(var5))).e(var4);
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
