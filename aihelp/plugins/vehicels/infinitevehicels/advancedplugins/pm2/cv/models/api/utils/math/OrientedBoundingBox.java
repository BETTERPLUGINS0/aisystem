package advancedplugins.pm2.cv.models.api.utils.math;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class OrientedBoundingBox {
   private static final Vector3f GLOBAL_RIGHT = new Vector3f(1.0F, 0.0F, 0.0F);
   private static final Vector3f GLOBAL_UP = new Vector3f(0.0F, 1.0F, 0.0F);
   private static final Vector3f GLOBAL_FORWARD = new Vector3f(0.0F, 0.0F, 1.0F);
   private final Vector3f origin;
   private final Quaternionf rotation;
   private final Vector3f right;
   private final Vector3f up;
   private final Vector3f forward;
   private final float halfX;
   private final float halfY;
   private final float halfZ;

   public OrientedBoundingBox(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.origin = new Vector3f((var4 + var1) * 0.5F, (var5 + var2) * 0.5F, (var6 + var3) * 0.5F);
      this.rotation = new Quaternionf();
      this.right = new Vector3f(GLOBAL_RIGHT);
      this.up = new Vector3f(GLOBAL_UP);
      this.forward = new Vector3f(GLOBAL_FORWARD);
      this.halfX = (var4 - var1) * 0.5F;
      this.halfY = (var5 - var2) * 0.5F;
      this.halfZ = (var6 - var3) * 0.5F;
   }

   public OrientedBoundingBox(Vector3f var1, Vector3f var2, Vector3f var3, float var4) {
      this(var1, var2, (new Quaternionf()).rotationXYZ(var3.x, var3.y, var3.z), var4);
   }

   public OrientedBoundingBox(Vector3f var1, Vector3f var2, Quaternionf var3, float var4) {
      this.origin = new Vector3f(var1);
      this.rotation = var3.rotateLocalY(var4 * 0.017453292F, new Quaternionf());
      this.right = GLOBAL_RIGHT.rotate(this.rotation, new Vector3f());
      this.up = GLOBAL_UP.rotate(this.rotation, new Vector3f());
      this.forward = GLOBAL_FORWARD.rotate(this.rotation, new Vector3f());
      this.halfX = var2.x * 0.5F;
      this.halfY = var2.y * 0.5F;
      this.halfZ = var2.z * 0.5F;
   }

   public boolean intersects(BoundingBox var1) {
      return this.intersects(new OrientedBoundingBox((float)var1.getMinX(), (float)var1.getMinY(), (float)var1.getMinZ(), (float)var1.getMaxX(), (float)var1.getMaxY(), (float)var1.getMaxZ()));
   }

   public boolean intersects(OrientedBoundingBox var1) {
      Vector3f var2 = new Vector3f(var1.origin.x - this.origin.x, var1.origin.y - this.origin.y, var1.origin.z - this.origin.z);

      for(int var3 = 0; var3 < 15; ++var3) {
         Vector3f var4 = this.getL(var3, var1);
         double var5 = this.projectionOnAxis(var2, var4);
         double var7 = this.projectionOnAxis((new Vector3f(this.right)).mul(this.halfX), var4) + this.projectionOnAxis((new Vector3f(this.up)).mul(this.halfY), var4) + this.projectionOnAxis((new Vector3f(this.forward)).mul(this.halfZ), var4) + this.projectionOnAxis((new Vector3f(var1.right)).mul(var1.halfX), var4) + this.projectionOnAxis((new Vector3f(var1.up)).mul(var1.halfY), var4) + this.projectionOnAxis((new Vector3f(var1.forward)).mul(var1.halfZ), var4);
         if (var5 > var7) {
            return false;
         }
      }

      return true;
   }

   public RayTraceResult rayTrace(@NotNull Vector3f var1, @NotNull Vector3f var2, double var3, Consumer<BoundingBox> var5) {
      if (!this.origin.isFinite()) {
         return new RayTraceResult(new Vector((double)var1.x + (double)var2.x * var3, (double)var1.y + (double)var2.y * var3, (double)var1.z + (double)var2.z * var3));
      } else {
         Quaternionf var6 = this.rotation.conjugate(new Quaternionf());
         Vector3f var7 = var1.sub(this.origin, new Vector3f()).rotate(var6).add(this.origin);
         Vector3f var8 = (new Vector3f(var2)).rotate(var6);
         BoundingBox var9 = BoundingBox.of(new Vector(this.origin.x, this.origin.y, this.origin.z), (double)this.halfX, (double)this.halfY, (double)this.halfZ);
         if (var5 != null) {
            var5.accept(var9);
         }

         RayTraceResult var10 = var9.rayTrace(new Vector(var7.x, var7.y, var7.z), new Vector(var8.x, var8.y, var8.z), var3);
         if (var10 == null) {
            return null;
         } else {
            Vector3f var11 = var10.getHitPosition().toVector3f().sub(this.origin).rotate(this.rotation).add(this.origin);
            return new RayTraceResult(new Vector(var11.x, var11.y, var11.z), var10.getHitBlockFace());
         }
      }
   }

   public double distanceSquared(@NotNull Vector3f var1) {
      if (!this.origin.isFinite()) {
         return Double.NaN;
      } else {
         Quaternionf var2 = this.rotation.conjugate(new Quaternionf());
         Vector3f var3 = var1.sub(this.origin, new Vector3f()).rotate(var2).add(this.origin);
         BoundingBox var4 = BoundingBox.of(new Vector(this.origin.x, this.origin.y, this.origin.z), (double)this.halfX, (double)this.halfY, (double)this.halfZ);
         return MathUtils.distanceSquaredToBoundingBox(Vector.fromJOML(var3), var4);
      }
   }

   public boolean contains(@NotNull Vector3f var1) {
      Quaternionf var2 = this.rotation.conjugate(new Quaternionf());
      Vector3f var3 = var1.sub(this.origin, new Vector3f()).rotate(var2).add(this.origin);
      BoundingBox var4 = BoundingBox.of(new Vector(this.origin.x, this.origin.y, this.origin.z), (double)this.halfX, (double)this.halfY, (double)this.halfZ);
      return var4.contains(Vector.fromJOML(var3));
   }

   private double projectionOnAxis(Vector3f var1, Vector3f var2) {
      return (double)Math.abs(var1.dot(var2));
   }

   private Vector3f getL(int var1, OrientedBoundingBox var2) {
      Vector3f var3;
      switch(var1) {
      case 0:
         var3 = this.right;
         break;
      case 1:
         var3 = this.up;
         break;
      case 2:
         var3 = this.forward;
         break;
      case 3:
         var3 = var2.right;
         break;
      case 4:
         var3 = var2.up;
         break;
      case 5:
         var3 = var2.forward;
         break;
      case 6:
         var3 = this.right.cross(var2.right, new Vector3f());
         break;
      case 7:
         var3 = this.right.cross(var2.up, new Vector3f());
         break;
      case 8:
         var3 = this.right.cross(var2.forward, new Vector3f());
         break;
      case 9:
         var3 = this.up.cross(var2.right, new Vector3f());
         break;
      case 10:
         var3 = this.up.cross(var2.up, new Vector3f());
         break;
      case 11:
         var3 = this.up.cross(var2.forward, new Vector3f());
         break;
      case 12:
         var3 = this.forward.cross(var2.right, new Vector3f());
         break;
      case 13:
         var3 = this.forward.cross(var2.up, new Vector3f());
         break;
      case 14:
         var3 = this.forward.cross(var2.forward, new Vector3f());
         break;
      default:
         throw new IllegalStateException("Unexpected value: " + var1);
      }

      return var3;
   }

   public ItemDisplay visualize(World var1, ItemStack var2) {
      Location var3 = new Location(var1, (double)this.origin.x, (double)this.origin.y, (double)this.origin.z);
      return (ItemDisplay)var1.spawn(var3, ItemDisplay.class, (var2x) -> {
         var2x.setItemStack(var2);
         var2x.setTransformation(new Transformation(new Vector3f(), this.rotation, new Vector3f(2.0F * this.halfX, 2.0F * this.halfY, 2.0F * this.halfZ), new Quaternionf()));
      });
   }

   public void visualize(World var1) {
      Vector3f var2 = (new Vector3f(this.right)).mul(this.halfX);
      Vector3f var3 = (new Vector3f(this.up)).mul(this.halfY);
      Vector3f var4 = (new Vector3f(this.forward)).mul(this.halfZ);
      Vector3f var5 = (new Vector3f(var2)).mul(-1.0F);
      Vector3f var6 = (new Vector3f(var3)).mul(-1.0F);
      Vector3f var7 = (new Vector3f(var4)).mul(-1.0F);
      this.drawLine(var2, var3, var4, var1, Color.ORANGE, (double)(this.halfZ * 2.0F));
      this.drawLine(var2, var6, var4, var1, Color.ORANGE, (double)(this.halfZ * 2.0F));
      this.drawLine(var5, var3, var4, var1, Color.ORANGE, (double)(this.halfZ * 2.0F));
      this.drawLine(var5, var6, var4, var1, Color.ORANGE, (double)(this.halfZ * 2.0F));
      this.drawLine(var3, var4, var2, var1, Color.GREEN, (double)(this.halfX * 2.0F));
      this.drawLine(var3, var7, var2, var1, Color.GREEN, (double)(this.halfX * 2.0F));
      this.drawLine(var6, var4, var2, var1, Color.GREEN, (double)(this.halfX * 2.0F));
      this.drawLine(var6, var7, var2, var1, Color.GREEN, (double)(this.halfX * 2.0F));
      this.drawLine(var4, var2, var3, var1, Color.AQUA, (double)(this.halfY * 2.0F));
      this.drawLine(var4, var5, var3, var1, Color.AQUA, (double)(this.halfY * 2.0F));
      this.drawLine(var7, var2, var3, var1, Color.AQUA, (double)(this.halfY * 2.0F));
      this.drawLine(var7, var5, var3, var1, Color.AQUA, (double)(this.halfY * 2.0F));
   }

   private void drawLine(Vector3f var1, Vector3f var2, Vector3f var3, World var4, Color var5, double var6) {
      Vector3f var8 = (new Vector3f(var1)).add(var2);
      Vector3f var9 = (new Vector3f(var8)).add(var3);
      Vector3f var10 = (new Vector3f(var8)).sub(var3);
      double var11 = 1.0D / var6;

      for(double var13 = 0.0D; var13 < var6; var13 += 0.1D) {
         Vector3f var15 = MathUtils.lerp(var9, var10, var13 * var11);
         var4.spawnParticle(Particle.REDSTONE, (double)(this.origin.x + var15.x), (double)(this.origin.y + var15.y), (double)(this.origin.z + var15.z), 1, new DustOptions(var5, 0.2F));
      }

   }

   public String toString() {
      String var1 = String.valueOf(this.origin);
      return "OrientedBoundingBox(origin=" + var1 + ", rotation=" + String.valueOf(this.rotation) + ", right=" + String.valueOf(this.right) + ", up=" + String.valueOf(this.up) + ", forward=" + String.valueOf(this.forward) + ", halfX=" + this.halfX + ", halfY=" + this.halfY + ", halfZ=" + this.halfZ + ")";
   }
}
