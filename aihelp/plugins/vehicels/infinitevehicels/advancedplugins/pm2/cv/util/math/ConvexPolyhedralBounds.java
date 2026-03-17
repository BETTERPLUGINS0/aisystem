package advancedplugins.pm2.cv.util.math;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.jetbrains.annotations.NotNull;

public class ConvexPolyhedralBounds implements Cloneable {
   private final Set<Vector3D> vertices = new LinkedHashSet();
   private final List<Triangle> triangles = new ArrayList();
   private final Set<Vector3D> vertexBacklog = new LinkedHashSet();
   private Vector3D minimumPoint;
   private Vector3D maximumPoint;
   private Vector3D centerAccum;
   private Triangle lastTriangle;

   public ConvexPolyhedralBounds() {
      this.centerAccum = Vector3D.ZERO;
   }

   public ConvexPolyhedralBounds(ConvexPolyhedralBounds copy) {
      this.centerAccum = Vector3D.ZERO;
      this.vertices.addAll(var1.vertices);
      this.triangles.addAll(var1.triangles);
      this.vertexBacklog.addAll(var1.vertexBacklog);
      this.minimumPoint = var1.minimumPoint;
      this.maximumPoint = var1.maximumPoint;
      this.centerAccum = var1.centerAccum;
      this.lastTriangle = var1.lastTriangle;
   }

   public void clear() {
      this.vertices.clear();
      this.triangles.clear();
      this.vertexBacklog.clear();
      this.minimumPoint = null;
      this.maximumPoint = null;
      this.centerAccum = Vector3D.ZERO;
      this.lastTriangle = null;
   }

   public boolean addVertex(@NotNull Vector3D vertex) {
      this.lastTriangle = null;
      if (this.vertices.contains(var1)) {
         return false;
      } else {
         if (this.vertices.size() == 3) {
            if (this.vertexBacklog.contains(var1)) {
               return false;
            }

            if (this.containsRaw(var1)) {
               return this.vertexBacklog.add(var1);
            }
         }

         this.vertices.add(var1);
         this.centerAccum = this.centerAccum.add(var1);
         if (this.minimumPoint == null) {
            this.minimumPoint = this.maximumPoint = var1;
         } else {
            this.minimumPoint = this.getMinimum(this.minimumPoint, var1);
            this.maximumPoint = this.getMaximum(this.maximumPoint, var1);
         }

         switch(this.vertices.size()) {
         case 0:
         case 1:
         case 2:
            return true;
         case 3:
            Vector3D[] var2 = (Vector3D[])this.vertices.toArray(new Vector3D[0]);
            this.triangles.add(new Triangle(var2[0], var2[1], var2[2]));
            this.triangles.add(new Triangle(var2[0], var2[2], var2[1]));
            return true;
         default:
            LinkedHashSet var7 = new LinkedHashSet();
            Iterator var3 = this.triangles.iterator();

            while(true) {
               Triangle var4;
               do {
                  if (!var3.hasNext()) {
                     var3 = var7.iterator();

                     while(var3.hasNext()) {
                        Edge var9 = (Edge)var3.next();
                        this.triangles.add(var9.createTriangle(var1));
                     }

                     if (!this.vertexBacklog.isEmpty()) {
                        this.vertices.remove(var1);
                        ArrayList var8 = new ArrayList(this.vertexBacklog);
                        this.vertexBacklog.clear();
                        Iterator var10 = var8.iterator();

                        while(var10.hasNext()) {
                           Vector3D var11 = (Vector3D)var10.next();
                           this.addVertex(var11);
                        }

                        this.vertices.add(var1);
                     }

                     return true;
                  }

                  var4 = (Triangle)var3.next();
               } while(!var4.above(var1));

               var3.remove();

               for(int var5 = 0; var5 < 3; ++var5) {
                  Edge var6 = var4.getEdge(var5);
                  if (!var7.remove(var6)) {
                     var7.add(var6);
                  }
               }
            }
         }
      }
   }

   private Vector3D getMinimum(Vector3D first, Vector3D second) {
      return new Vector3D(Math.min(var1.getX(), var2.getX()), Math.min(var1.getY(), var2.getY()), Math.min(var1.getZ(), var2.getZ()));
   }

   private Vector3D getMaximum(Vector3D first, Vector3D second) {
      return new Vector3D(Math.max(var1.getX(), var2.getX()), Math.max(var1.getY(), var2.getY()), Math.max(var1.getZ(), var2.getZ()));
   }

   public boolean isDefined() {
      return !this.triangles.isEmpty();
   }

   public Vector3D getMinimumPoint() {
      return this.minimumPoint;
   }

   public Vector3D getMaximumPoint() {
      return this.maximumPoint;
   }

   public Vector3D getCenter() {
      int var1 = this.vertices.size();
      return new Vector3D(this.centerAccum.getX() / (double)var1, this.centerAccum.getY() / (double)var1, this.centerAccum.getZ() / (double)var1);
   }

   public List<Edge> getEdges() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.triangles.iterator();

      while(var2.hasNext()) {
         Triangle var3 = (Triangle)var2.next();

         for(int var4 = 0; var4 < 3; ++var4) {
            Edge var5 = var3.getEdge(var4);
            if (!var1.contains(var5)) {
               var1.add(var5);
            }
         }
      }

      return var1;
   }

   public void shift(Vector3D change) {
      shiftCollection(this.vertices, var1);
      shiftCollection(this.vertexBacklog, var1);

      for(int var2 = 0; var2 < this.triangles.size(); ++var2) {
         Triangle var3 = (Triangle)this.triangles.get(var2);
         Vector3D var4 = var1.add(var3.getVertex(0));
         Vector3D var5 = var1.add(var3.getVertex(1));
         Vector3D var6 = var1.add(var3.getVertex(2));
         this.triangles.set(var2, new Triangle(var4, var5, var6));
      }

      this.minimumPoint = var1.add(this.minimumPoint);
      this.maximumPoint = var1.add(this.maximumPoint);
      this.centerAccum = var1.scalarMultiply((double)this.vertices.size()).add(this.centerAccum);
      this.lastTriangle = null;
   }

   private static void shiftCollection(Collection<Vector3D> collection, Vector3D change) {
      ArrayList var2 = new ArrayList(var0);
      var0.clear();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Vector3D var4 = (Vector3D)var3.next();
         var0.add(var1.add(var4));
      }

   }

   public boolean contains(Vector3D position) {
      if (!this.isDefined()) {
         return false;
      } else {
         Vector3D var2 = this.getMinimumPoint();
         Vector3D var3 = this.getMaximumPoint();
         return !this.containedWithin(var1, var2, var3) ? false : this.containsRaw(var1);
      }
   }

   private boolean containedWithin(Vector3D point, Vector3D min, Vector3D max) {
      return var1.getX() >= var2.getX() && var1.getX() <= var3.getX() && var1.getY() >= var2.getY() && var1.getY() <= var3.getY() && var1.getZ() >= var2.getZ() && var1.getZ() <= var3.getZ();
   }

   private boolean containsRaw(Vector3D pt) {
      if (this.lastTriangle != null && this.lastTriangle.above(var1)) {
         return false;
      } else {
         Iterator var2 = this.triangles.iterator();

         Triangle var3;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            var3 = (Triangle)var2.next();
         } while(this.lastTriangle == var3 || !var3.above(var1));

         this.lastTriangle = var3;
         return false;
      }
   }

   public Collection<Vector3D> getVertices() {
      if (this.vertexBacklog.isEmpty()) {
         return this.vertices;
      } else {
         ArrayList var1 = new ArrayList(this.vertices);
         var1.addAll(this.vertexBacklog);
         return var1;
      }
   }

   public Collection<Triangle> getTriangles() {
      return this.triangles;
   }

   public ConvexPolyhedralBounds clone() {
      try {
         return (ConvexPolyhedralBounds)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }
}
