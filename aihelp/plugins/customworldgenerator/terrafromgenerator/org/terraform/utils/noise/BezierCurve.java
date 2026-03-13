package org.terraform.utils.noise;

import org.jetbrains.annotations.NotNull;
import org.terraform.utils.Vector2f;

public class BezierCurve {
   final Vector2f control1;
   final Vector2f control2;
   Vector2f point1 = new Vector2f(0.0F, 0.0F);
   Vector2f point2 = new Vector2f(1.0F, 1.0F);

   public BezierCurve(Vector2f control1, Vector2f control2) {
      this.control1 = control1;
      this.control2 = control2;
   }

   public BezierCurve(Vector2f start, Vector2f control1, Vector2f control2, Vector2f end) {
      this.point1 = start;
      this.point2 = end;
      this.control1 = control1;
      this.control2 = control2;
   }

   @NotNull
   public static Vector2f cubic(float progress, @NotNull Vector2f point1, @NotNull Vector2f control1, @NotNull Vector2f control2, @NotNull Vector2f point2) {
      float progressBw = 1.0F - progress;
      double x = Math.pow((double)progressBw, 3.0D) * (double)point1.x + Math.pow((double)progressBw, 2.0D) * 3.0D * (double)progress * (double)control1.x + (double)(progressBw * 3.0F * progress * progress * control2.x) + (double)(progress * progress * progress * point2.x);
      double y = Math.pow((double)progressBw, 3.0D) * (double)point1.y + Math.pow((double)progressBw, 2.0D) * 3.0D * (double)progress * (double)control1.y + (double)(progressBw * 3.0F * progress * progress * control2.y) + (double)(progress * progress * progress * point2.y);
      return new Vector2f((float)x, (float)y);
   }

   @NotNull
   public Vector2f calculate(float progress) {
      return cubic(progress, this.point1, this.control1, this.control2, this.point2);
   }
}
