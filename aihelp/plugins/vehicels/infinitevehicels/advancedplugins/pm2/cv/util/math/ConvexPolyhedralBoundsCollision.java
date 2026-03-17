package advancedplugins.pm2.cv.util.math;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.Collector.Characteristics;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;

public class ConvexPolyhedralBoundsCollision {
   private static ConvexPolyhedralBoundsCollision.Projection project(ConvexPolyhedralBounds bounds, Vector3D axis) {
      Stream var10000 = var0.getVertices().stream();
      Objects.requireNonNull(var1);
      return (ConvexPolyhedralBoundsCollision.Projection)var10000.collect(new ConvexPolyhedralBoundsCollision.ProjectionCollector(var1::dotProduct));
   }

   private static boolean areProjectionsOverlapping(ConvexPolyhedralBoundsCollision.Projection a, ConvexPolyhedralBoundsCollision.Projection b) {
      return var0.min <= var1.max && var0.max >= var1.min;
   }

   public static boolean areColliding(ConvexPolyhedralBounds a, ConvexPolyhedralBounds b, List<Vector3D> collisionPoints) {
      LinkedHashSet var3 = new LinkedHashSet();
      Iterator var4 = var0.getEdges().iterator();

      Iterator var6;
      while(var4.hasNext()) {
         Edge var5 = (Edge)var4.next();
         var6 = var1.getEdges().iterator();

         while(var6.hasNext()) {
            Edge var7 = (Edge)var6.next();
            Vector3D var8 = NormalizationUtil.normalize(var5.getStart().subtract(var5.getEnd()).crossProduct(var7.getStart().subtract(var7.getEnd())));
            if (!var8.equals(Vector3D.ZERO)) {
               var3.add(var8);
               if (!areProjectionsOverlapping(project(var0, var8), project(var1, var8))) {
                  return false;
               }
            }
         }
      }

      var4 = var0.getTriangles().iterator();

      Triangle var9;
      Vector3D var10;
      while(var4.hasNext()) {
         var9 = (Triangle)var4.next();
         var10 = NormalizationUtil.normalize(var9.getNormal());
         if (!var10.equals(Vector3D.ZERO) && var3.add(var10) && !areProjectionsOverlapping(project(var0, var10), project(var1, var10))) {
            return false;
         }
      }

      var4 = var1.getTriangles().iterator();

      while(var4.hasNext()) {
         var9 = (Triangle)var4.next();
         var10 = NormalizationUtil.normalize(var9.getNormal());
         if (!var10.equals(Vector3D.ZERO) && var3.add(var10) && !areProjectionsOverlapping(project(var0, var10), project(var1, var10))) {
            return false;
         }
      }

      if (var2 != null) {
         var4 = var0.getTriangles().iterator();

         while(var4.hasNext()) {
            var9 = (Triangle)var4.next();
            var6 = var1.getTriangles().iterator();

            while(var6.hasNext()) {
               Triangle var11 = (Triangle)var6.next();
               Stream var10000 = Arrays.stream(var9.getVertices());
               Objects.requireNonNull(var11);
               var10000 = var10000.filter(var11::containsRaw);
               Objects.requireNonNull(var2);
               var10000.forEach(var2::add);
               var10000 = Arrays.stream(var11.getVertices());
               Objects.requireNonNull(var9);
               var10000 = var10000.filter(var9::containsRaw);
               Objects.requireNonNull(var2);
               var10000.forEach(var2::add);
            }
         }
      }

      return true;
   }

   public static boolean areColliding(ConvexPolyhedralBounds a, ConvexPolyhedralBounds b) {
      return areColliding(var0, var1, (List)null);
   }

   private static class ProjectionCollector implements Collector<Vector3D, ConvexPolyhedralBoundsCollision.ProjectionCollector.State, ConvexPolyhedralBoundsCollision.Projection> {
      private final Function<Vector3D, Double> projectionFunction;

      ProjectionCollector(Function<Vector3D, Double> projectionFunction) {
         this.projectionFunction = var1;
      }

      public Supplier<ConvexPolyhedralBoundsCollision.ProjectionCollector.State> supplier() {
         return ConvexPolyhedralBoundsCollision.ProjectionCollector.State::new;
      }

      public BiConsumer<ConvexPolyhedralBoundsCollision.ProjectionCollector.State, Vector3D> accumulator() {
         return (var1, var2) -> {
            var1.accumulate((Double)this.projectionFunction.apply(var2));
         };
      }

      public BinaryOperator<ConvexPolyhedralBoundsCollision.ProjectionCollector.State> combiner() {
         return (var0, var1) -> {
            var0.combine(var1);
            return var0;
         };
      }

      public Function<ConvexPolyhedralBoundsCollision.ProjectionCollector.State, ConvexPolyhedralBoundsCollision.Projection> finisher() {
         return (var0) -> {
            return new ConvexPolyhedralBoundsCollision.Projection(var0.min, var0.max);
         };
      }

      public Set<Characteristics> characteristics() {
         return EnumSet.of(Characteristics.UNORDERED);
      }

      static class State {
         double min = Double.POSITIVE_INFINITY;
         double max = Double.NEGATIVE_INFINITY;

         void accumulate(double value) {
            this.min = Math.min(this.min, var1);
            this.max = Math.max(this.max, var1);
         }

         void combine(ConvexPolyhedralBoundsCollision.ProjectionCollector.State another) {
            this.min = Math.min(this.min, var1.min);
            this.max = Math.max(this.max, var1.max);
         }
      }
   }

   private static class Projection {
      double min;
      double max;

      Projection(double min, double max) {
         this.min = var1;
         this.max = var3;
      }
   }
}
