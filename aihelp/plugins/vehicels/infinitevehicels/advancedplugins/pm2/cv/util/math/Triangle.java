package advancedplugins.pm2.cv.util.math;

import com.google.common.base.Preconditions;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;

public class Triangle {
   private String tag = "Triangle";
   private final Vector3D[] vertices;
   private final Vector3D normal;
   private final double maxDotProduct;
   public final Vector3D v1;
   public final Vector3D v2;
   public final Vector3D v3;

   public Triangle(Vector3D v1, Vector3D v2, Vector3D v3) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      Preconditions.checkNotNull(var3);
      this.vertices = new Vector3D[]{var1, var2, var3};
      this.v1 = var1;
      this.v2 = var2;
      this.v3 = var3;
      this.normal = NormalizationUtil.normalize(var2.subtract(var1).crossProduct(var3.subtract(var1)));
      this.maxDotProduct = Math.max(Math.max(this.normal.dotProduct(var1), this.normal.dotProduct(var2)), this.normal.dotProduct(var3));
   }

   public Vector3D[] getVertices() {
      return this.vertices;
   }

   public Edge[] getEdges() {
      Edge[] var1 = new Edge[3];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = this.getEdge(var2);
      }

      return var1;
   }

   public Vector3D getNormal() {
      return this.normal;
   }

   public Vector3D getVertex(int index) {
      return this.vertices[var1];
   }

   public Edge getEdge(int index) {
      return var1 == this.vertices.length - 1 ? new Edge(this.vertices[var1], this.vertices[0]) : new Edge(this.vertices[var1], this.vertices[var1 + 1]);
   }

   public boolean below(Vector3D pt) {
      Preconditions.checkNotNull(var1);
      return this.normal.dotProduct(var1) < this.maxDotProduct;
   }

   public boolean above(Vector3D pt) {
      Preconditions.checkNotNull(var1);
      return this.normal.dotProduct(var1) > this.maxDotProduct;
   }

   public boolean containsRaw(Vector3D point) {
      Preconditions.checkNotNull(var1);
      Vector3D var2 = var1.subtract(this.v1);
      Vector3D var3 = this.v2.subtract(this.v1);
      Vector3D var4 = this.v3.subtract(this.v1);
      double var5 = var3.dotProduct(var3);
      double var7 = var3.dotProduct(var4);
      double var9 = var3.dotProduct(var2);
      double var11 = var4.dotProduct(var4);
      double var13 = var4.dotProduct(var2);
      double var15 = 1.0D / (var5 * var11 - var7 * var7);
      double var17 = (var11 * var9 - var7 * var13) * var15;
      double var19 = (var5 * var13 - var7 * var9) * var15;
      return var17 >= 0.0D && var19 >= 0.0D && var17 + var19 <= 1.0D;
   }

   public Triangle tag(String tag) {
      Preconditions.checkNotNull(var1);
      this.tag = var1;
      return this;
   }

   public String toString() {
      String var10000 = this.tag;
      return var10000 + "(" + String.valueOf(this.vertices[0]) + "," + String.valueOf(this.vertices[1]) + "," + String.valueOf(this.vertices[2]) + ")";
   }
}
