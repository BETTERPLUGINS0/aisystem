package ac.grim.grimac.shaded.fastutil.ints;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.Comparator;
import java.util.Objects;

@FunctionalInterface
public interface IntComparator extends Comparator<Integer> {
   int compare(int var1, int var2);

   default IntComparator reversed() {
      return IntComparators.oppositeComparator(this);
   }

   /** @deprecated */
   @Deprecated
   default int compare(Integer ok1, Integer ok2) {
      return this.compare(ok1, ok2);
   }

   default IntComparator thenComparing(IntComparator second) {
      return (IntComparator)((Serializable)((k1, k2) -> {
         int comp = this.compare(k1, k2);
         return comp == 0 ? second.compare(k1, k2) : comp;
      }));
   }

   default Comparator<Integer> thenComparing(Comparator<? super Integer> second) {
      return (Comparator)(second instanceof IntComparator ? this.thenComparing((IntComparator)second) : super.thenComparing(second));
   }

   static <U extends Comparable<? super U>> IntComparator comparing(Int2ObjectFunction<? extends U> keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (IntComparator)((Serializable)((k1, k2) -> {
         return ((Comparable)keyExtractor.get(k1)).compareTo(keyExtractor.get(k2));
      }));
   }

   static <U extends Comparable<? super U>> IntComparator comparing(Int2ObjectFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
      Objects.requireNonNull(keyExtractor);
      Objects.requireNonNull(keyComparator);
      return (IntComparator)((Serializable)((k1, k2) -> {
         return keyComparator.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static IntComparator comparingInt(Int2IntFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (IntComparator)((Serializable)((k1, k2) -> {
         return Integer.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static IntComparator comparingLong(Int2LongFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (IntComparator)((Serializable)((k1, k2) -> {
         return Long.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static IntComparator comparingDouble(Int2DoubleFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (IntComparator)((Serializable)((k1, k2) -> {
         return Double.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   // $FF: synthetic method
   private static Object $deserializeLambda$(SerializedLambda lambda) {
      String var1 = lambda.getImplMethodName();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -1554871547:
         if (var1.equals("lambda$thenComparing$931d6fed$1")) {
            var2 = 3;
         }
         break;
      case 52677956:
         if (var1.equals("lambda$comparing$942f8bdc$1")) {
            var2 = 4;
         }
         break;
      case 113038062:
         if (var1.equals("lambda$comparingInt$c94ca11c$1")) {
            var2 = 1;
         }
         break;
      case 1252210994:
         if (var1.equals("lambda$comparingDouble$4ab3f26c$1")) {
            var2 = 0;
         }
         break;
      case 1300821090:
         if (var1.equals("lambda$comparingLong$8c6cda96$1")) {
            var2 = 5;
         }
         break;
      case 2058531398:
         if (var1.equals("lambda$comparing$ee768883$1")) {
            var2 = 2;
         }
      }

      switch(var2) {
      case 0:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/ints/IntComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(II)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/ints/IntComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/ints/Int2DoubleFunction;II)I")) {
            return (k1, k2) -> {
               return Double.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 1:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/ints/IntComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(II)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/ints/IntComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/ints/Int2IntFunction;II)I")) {
            return (k1, k2) -> {
               return Integer.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 2:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/ints/IntComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(II)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/ints/IntComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/ints/Int2ObjectFunction;II)I")) {
            return (k1, k2) -> {
               return ((Comparable)keyExtractor.get(k1)).compareTo(keyExtractor.get(k2));
            };
         }
         break;
      case 3:
         if (lambda.getImplMethodKind() == 7 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/ints/IntComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(II)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/ints/IntComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/ints/IntComparator;II)I")) {
            IntComparator var10000 = (IntComparator)lambda.getCapturedArg(0);
            return (k1, k2) -> {
               int comp = this.compare(k1, k2);
               return comp == 0 ? second.compare(k1, k2) : comp;
            };
         }
         break;
      case 4:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/ints/IntComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(II)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/ints/IntComparator") && lambda.getImplMethodSignature().equals("(Ljava/util/Comparator;Lac/grim/grimac/shaded/fastutil/ints/Int2ObjectFunction;II)I")) {
            return (k1, k2) -> {
               return keyComparator.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 5:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/ints/IntComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(II)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/ints/IntComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/ints/Int2LongFunction;II)I")) {
            return (k1, k2) -> {
               return Long.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
      }

      throw new IllegalArgumentException("Invalid lambda deserialization");
   }
}
