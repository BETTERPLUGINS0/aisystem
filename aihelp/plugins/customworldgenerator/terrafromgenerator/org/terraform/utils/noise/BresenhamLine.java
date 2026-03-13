package org.terraform.utils.noise;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.terraform.utils.Vector2f;

public class BresenhamLine {
   final Vector2f point1;
   final Vector2f point2;

   public BresenhamLine(Vector2f point1, Vector2f point2) {
      this.point1 = point1;
      this.point2 = point2;
   }

   @NotNull
   public List<Vector2f> getPoints() {
      return this.genLine(Math.round(this.point1.x), Math.round(this.point1.y), Math.round(this.point2.x), Math.round(this.point2.y));
   }

   @NotNull
   public List<Vector2f> genLine(int x0, int y0, int x1, int y1) {
      List<Vector2f> line = new ArrayList(16);
      int dx = Math.abs(x1 - x0);
      int dy = Math.abs(y1 - y0);
      int sx = x0 < x1 ? 1 : -1;
      int sy = y0 < y1 ? 1 : -1;
      int err = dx - dy;

      while(true) {
         line.add(new Vector2f((float)x0, (float)y0));
         if (x0 == x1 && y0 == y1) {
            return line;
         }

         int e2 = 2 * err;
         if (e2 > -dy) {
            err -= dy;
            x0 += sx;
         }

         if (e2 < dx) {
            err += dx;
            y0 += sy;
         }
      }
   }
}
