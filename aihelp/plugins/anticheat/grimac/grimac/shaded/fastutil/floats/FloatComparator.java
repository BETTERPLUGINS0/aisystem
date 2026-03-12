package ac.grim.grimac.shaded.fastutil.floats;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.Comparator;
import java.util.Objects;

@FunctionalInterface
public interface FloatComparator extends Comparator<Float> {
   int compare(float var1, float var2);

   default FloatComparator reversed() {
      return FloatComparators.oppositeComparator(this);
   }

   /** @deprecated */
   @Deprecated
   default int compare(Float ok1, Float ok2) {
      return this.compare(ok1, ok2);
   }

   default FloatComparator thenComparing(FloatComparator second) {
      return (FloatComparator)((Serializable)((k1, k2) -> {
         int comp = this.compare(k1, k2);
         return comp == 0 ? second.compare(k1, k2) : comp;
      }));
   }

   default Comparator<Float> thenComparing(Comparator<? super Float> second) {
      return (Comparator)(second instanceof FloatComparator ? this.thenComparing((FloatComparator)second) : super.thenComparing(second));
   }

   static <U extends Comparable<? super U>> FloatComparator comparing(Float2ObjectFunction<? extends U> keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (FloatComparator)((Serializable)((k1, k2) -> {
         return ((Comparable)keyExtractor.get(k1)).compareTo(keyExtractor.get(k2));
      }));
   }

   static <U extends Comparable<? super U>> FloatComparator comparing(Float2ObjectFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
      Objects.requireNonNull(keyExtractor);
      Objects.requireNonNull(keyComparator);
      return (FloatComparator)((Serializable)((k1, k2) -> {
         return keyComparator.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static FloatComparator comparingInt(Float2IntFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (FloatComparator)((Serializable)((k1, k2) -> {
         return Integer.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static FloatComparator comparingLong(Float2LongFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (FloatComparator)((Serializable)((k1, k2) -> {
         return Long.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static FloatComparator comparingDouble(Float2DoubleFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (FloatComparator)((Serializable)((k1, k2) -> {
         return Double.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   // $FF: synthetic method
   private static Object $deserializeLambda$(SerializedLambda lambda) {
      String var1 = lambda.getImplMethodName();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -1866857138:
         if (var1.equals("lambda$comparing$47bb00dc$1")) {
            var2 = 2;
         }
         break;
      case 33007787:
         if (var1.equals("lambda$thenComparing$99a1156d$1")) {
            var2 = 4;
         }
         break;
      case 71056266:
         if (var1.equals("lambda$comparingLong$69064d56$1")) {
            var2 = 3;
         }
         break;
      case 251744898:
         if (var1.equals("lambda$comparingInt$3765281c$1")) {
            var2 = 0;
         }
         break;
      case 1730609352:
         if (var1.equals("lambda$comparing$b2521a43$1")) {
            var2 = 5;
         }
         break;
      case 1882694324:
         if (var1.equals("lambda$comparingDouble$889bf4ac$1")) {
            var2 = 1;
         }
      }

      switch(var2) {
      case 0:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/floats/FloatComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(FF)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/floats/FloatComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/floats/Float2IntFunction;FF)I")) {
            return (k1, k2) -> {
               return Integer.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 1:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/floats/FloatComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(FF)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/floats/FloatComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/floats/Float2DoubleFunction;FF)I")) {
            return (k1, k2) -> {
               return Double.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 2:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/floats/FloatComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(FF)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/floats/FloatComparator") && lambda.getImplMethodSignature().equals("(Ljava/util/Comparator;Lac/grim/grimac/shaded/fastutil/floats/Float2ObjectFunction;FF)I")) {
            return (k1, k2) -> {
               return keyComparator.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 3:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/floats/FloatComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(FF)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/floats/FloatComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/floats/Float2LongFunction;FF)I")) {
            return (k1, k2) -> {
               return Long.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 4:
         if (lambda.getImplMethodKind() == 7 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/floats/FloatComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(FF)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/floats/FloatComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/floats/FloatComparator;FF)I")) {
            FloatComparator var10000 = (FloatComparator)lambda.getCapturedArg(0);
            return (k1, k2) -> {
               int comp = this.compare(k1, k2);
               return comp == 0 ? second.compare(k1, k2) : comp;
            };
         }
         break;
      case 5:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/floats/FloatComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(FF)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/floats/FloatComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/floats/Float2ObjectFunction;FF)I")) {
            return (k1, k2) -> {
               return ((Comparable)keyExtractor.get(k1)).compareTo(keyExtractor.get(k2));
            };
         }
      }

      throw new IllegalArgumentException("Invalid lambda deserialization");
   }
}
