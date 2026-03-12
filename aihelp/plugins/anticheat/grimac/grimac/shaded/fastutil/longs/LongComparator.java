package ac.grim.grimac.shaded.fastutil.longs;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.Comparator;
import java.util.Objects;

@FunctionalInterface
public interface LongComparator extends Comparator<Long> {
   int compare(long var1, long var3);

   default LongComparator reversed() {
      return LongComparators.oppositeComparator(this);
   }

   /** @deprecated */
   @Deprecated
   default int compare(Long ok1, Long ok2) {
      return this.compare(ok1, ok2);
   }

   default LongComparator thenComparing(LongComparator second) {
      return (LongComparator)((Serializable)((k1, k2) -> {
         int comp = this.compare(k1, k2);
         return comp == 0 ? second.compare(k1, k2) : comp;
      }));
   }

   default Comparator<Long> thenComparing(Comparator<? super Long> second) {
      return (Comparator)(second instanceof LongComparator ? this.thenComparing((LongComparator)second) : super.thenComparing(second));
   }

   static <U extends Comparable<? super U>> LongComparator comparing(Long2ObjectFunction<? extends U> keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (LongComparator)((Serializable)((k1, k2) -> {
         return ((Comparable)keyExtractor.get(k1)).compareTo(keyExtractor.get(k2));
      }));
   }

   static <U extends Comparable<? super U>> LongComparator comparing(Long2ObjectFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
      Objects.requireNonNull(keyExtractor);
      Objects.requireNonNull(keyComparator);
      return (LongComparator)((Serializable)((k1, k2) -> {
         return keyComparator.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static LongComparator comparingInt(Long2IntFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (LongComparator)((Serializable)((k1, k2) -> {
         return Integer.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static LongComparator comparingLong(Long2LongFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (LongComparator)((Serializable)((k1, k2) -> {
         return Long.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static LongComparator comparingDouble(Long2DoubleFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (LongComparator)((Serializable)((k1, k2) -> {
         return Double.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   // $FF: synthetic method
   private static Object $deserializeLambda$(SerializedLambda lambda) {
      String var1 = lambda.getImplMethodName();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -2072572564:
         if (var1.equals("lambda$thenComparing$3d6e68ef$1")) {
            var2 = 2;
         }
         break;
      case -1229473380:
         if (var1.equals("lambda$comparingDouble$7610a30a$1")) {
            var2 = 3;
         }
         break;
      case 616474201:
         if (var1.equals("lambda$comparing$3b222d9e$1")) {
            var2 = 0;
         }
         break;
      case 748073752:
         if (var1.equals("lambda$comparingInt$7c990de$1")) {
            var2 = 5;
         }
         break;
      case 2027790820:
         if (var1.equals("lambda$comparing$d869261$1")) {
            var2 = 1;
         }
         break;
      case 2130130778:
         if (var1.equals("lambda$comparingLong$320382f4$1")) {
            var2 = 4;
         }
      }

      switch(var2) {
      case 0:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/longs/LongComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(JJ)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/longs/LongComparator") && lambda.getImplMethodSignature().equals("(Ljava/util/Comparator;Lac/grim/grimac/shaded/fastutil/longs/Long2ObjectFunction;JJ)I")) {
            return (k1, k2) -> {
               return keyComparator.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 1:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/longs/LongComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(JJ)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/longs/LongComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/longs/Long2ObjectFunction;JJ)I")) {
            return (k1, k2) -> {
               return ((Comparable)keyExtractor.get(k1)).compareTo(keyExtractor.get(k2));
            };
         }
         break;
      case 2:
         if (lambda.getImplMethodKind() == 7 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/longs/LongComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(JJ)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/longs/LongComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/longs/LongComparator;JJ)I")) {
            LongComparator var10000 = (LongComparator)lambda.getCapturedArg(0);
            return (k1, k2) -> {
               int comp = this.compare(k1, k2);
               return comp == 0 ? second.compare(k1, k2) : comp;
            };
         }
         break;
      case 3:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/longs/LongComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(JJ)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/longs/LongComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/longs/Long2DoubleFunction;JJ)I")) {
            return (k1, k2) -> {
               return Double.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 4:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/longs/LongComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(JJ)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/longs/LongComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/longs/Long2LongFunction;JJ)I")) {
            return (k1, k2) -> {
               return Long.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 5:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/longs/LongComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(JJ)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/longs/LongComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/longs/Long2IntFunction;JJ)I")) {
            return (k1, k2) -> {
               return Integer.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
      }

      throw new IllegalArgumentException("Invalid lambda deserialization");
   }
}
