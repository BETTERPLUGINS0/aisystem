package ac.grim.grimac.shaded.fastutil.doubles;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.Comparator;
import java.util.Objects;

@FunctionalInterface
public interface DoubleComparator extends Comparator<Double> {
   int compare(double var1, double var3);

   default DoubleComparator reversed() {
      return DoubleComparators.oppositeComparator(this);
   }

   /** @deprecated */
   @Deprecated
   default int compare(Double ok1, Double ok2) {
      return this.compare(ok1, ok2);
   }

   default DoubleComparator thenComparing(DoubleComparator second) {
      return (DoubleComparator)((Serializable)((k1, k2) -> {
         int comp = this.compare(k1, k2);
         return comp == 0 ? second.compare(k1, k2) : comp;
      }));
   }

   default Comparator<Double> thenComparing(Comparator<? super Double> second) {
      return (Comparator)(second instanceof DoubleComparator ? this.thenComparing((DoubleComparator)second) : super.thenComparing(second));
   }

   static <U extends Comparable<? super U>> DoubleComparator comparing(Double2ObjectFunction<? extends U> keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (DoubleComparator)((Serializable)((k1, k2) -> {
         return ((Comparable)keyExtractor.get(k1)).compareTo(keyExtractor.get(k2));
      }));
   }

   static <U extends Comparable<? super U>> DoubleComparator comparing(Double2ObjectFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
      Objects.requireNonNull(keyExtractor);
      Objects.requireNonNull(keyComparator);
      return (DoubleComparator)((Serializable)((k1, k2) -> {
         return keyComparator.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static DoubleComparator comparingInt(Double2IntFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (DoubleComparator)((Serializable)((k1, k2) -> {
         return Integer.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static DoubleComparator comparingLong(Double2LongFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (DoubleComparator)((Serializable)((k1, k2) -> {
         return Long.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static DoubleComparator comparingDouble(Double2DoubleFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (DoubleComparator)((Serializable)((k1, k2) -> {
         return Double.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   // $FF: synthetic method
   private static Object $deserializeLambda$(SerializedLambda lambda) {
      String var1 = lambda.getImplMethodName();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -2022242124:
         if (var1.equals("lambda$comparingDouble$863b5f72$1")) {
            var2 = 2;
         }
         break;
      case -1318285536:
         if (var1.equals("lambda$thenComparing$f8e9881b$1")) {
            var2 = 5;
         }
         break;
      case -1162931474:
         if (var1.equals("lambda$comparingLong$ad87f45c$1")) {
            var2 = 0;
         }
         break;
      case -698826528:
         if (var1.equals("lambda$comparingInt$b5e08b0a$1")) {
            var2 = 4;
         }
         break;
      case -129694361:
         if (var1.equals("lambda$comparing$c73e10c9$1")) {
            var2 = 1;
         }
         break;
      case 1866929843:
         if (var1.equals("lambda$comparing$c957134a$1")) {
            var2 = 3;
         }
      }

      switch(var2) {
      case 0:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/doubles/DoubleComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(DD)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/doubles/DoubleComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/doubles/Double2LongFunction;DD)I")) {
            return (k1, k2) -> {
               return Long.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 1:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/doubles/DoubleComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(DD)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/doubles/DoubleComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/doubles/Double2ObjectFunction;DD)I")) {
            return (k1, k2) -> {
               return ((Comparable)keyExtractor.get(k1)).compareTo(keyExtractor.get(k2));
            };
         }
         break;
      case 2:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/doubles/DoubleComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(DD)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/doubles/DoubleComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/doubles/Double2DoubleFunction;DD)I")) {
            return (k1, k2) -> {
               return Double.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 3:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/doubles/DoubleComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(DD)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/doubles/DoubleComparator") && lambda.getImplMethodSignature().equals("(Ljava/util/Comparator;Lac/grim/grimac/shaded/fastutil/doubles/Double2ObjectFunction;DD)I")) {
            return (k1, k2) -> {
               return keyComparator.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 4:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/doubles/DoubleComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(DD)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/doubles/DoubleComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/doubles/Double2IntFunction;DD)I")) {
            return (k1, k2) -> {
               return Integer.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 5:
         if (lambda.getImplMethodKind() == 7 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/doubles/DoubleComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(DD)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/doubles/DoubleComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/doubles/DoubleComparator;DD)I")) {
            DoubleComparator var10000 = (DoubleComparator)lambda.getCapturedArg(0);
            return (k1, k2) -> {
               int comp = this.compare(k1, k2);
               return comp == 0 ? second.compare(k1, k2) : comp;
            };
         }
      }

      throw new IllegalArgumentException("Invalid lambda deserialization");
   }
}
