package advancedplugins.pm2.cv.models.api.model.rpc.entity;

import advancedplugins.pm2.cv.models.api.utils.Utils;
import advancedplugins.pm2.cv.models.api.utils.math.Box;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Hitbox implements Box {
   private final double width;
   private final double height;
   private final double depth;
   private final double eyeHeight;

   public Hitbox(double var1, double var3, double var5, double var7) {
      this.width = var1;
      this.height = var3;
      this.depth = var5;
      this.eyeHeight = var7;
   }

   public Hitbox clone() {
      return new Hitbox(this.width, this.height, this.depth, this.eyeHeight);
   }

   public double getMaxWidth() {
      return Math.max(this.width, this.depth);
   }

   @NotNull
   public BoundingBox createBoundingBox(Vector var1) {
      return new BoundingBox(var1.getX() - this.width * 0.5D, var1.getY(), var1.getZ() - this.depth * 0.5D, var1.getX() + this.width * 0.5D, var1.getY() + this.height, var1.getZ() + this.depth * 0.5D);
   }

   public String toSimpleString() {
      String var1 = Utils.FORMATTER.format(this.width);
      return var1 + " x " + Utils.FORMATTER.format(this.height) + " x " + Utils.FORMATTER.format(this.width);
   }

   public String toEyeHeightString() {
      return Utils.FORMATTER.format(this.eyeHeight);
   }

   public double getWidth() {
      return this.width;
   }

   public double getHeight() {
      return this.height;
   }

   public double getDepth() {
      return this.depth;
   }

   public double getEyeHeight() {
      return this.eyeHeight;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Hitbox)) {
         return false;
      } else {
         Hitbox var2 = (Hitbox)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getWidth(), var2.getWidth()) != 0) {
            return false;
         } else if (Double.compare(this.getHeight(), var2.getHeight()) != 0) {
            return false;
         } else if (Double.compare(this.getDepth(), var2.getDepth()) != 0) {
            return false;
         } else {
            return Double.compare(this.getEyeHeight(), var2.getEyeHeight()) == 0;
         }
      }
   }

   protected boolean canEqual(Object var1) {
      return var1 instanceof Hitbox;
   }

   public String toString() {
      double var1 = this.getWidth();
      return "Hitbox(width=" + var1 + ", height=" + this.getHeight() + ", depth=" + this.getDepth() + ", eyeHeight=" + this.getEyeHeight() + ")";
   }
}
