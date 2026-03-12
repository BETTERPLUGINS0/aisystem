package ac.grim.grimac.shaded.fastutil.booleans;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.Comparator;
import java.util.Objects;

@FunctionalInterface
public interface BooleanComparator extends Comparator<Boolean> {
   int compare(boolean var1, boolean var2);

   default BooleanComparator reversed() {
      return BooleanComparators.oppositeComparator(this);
   }

   /** @deprecated */
   @Deprecated
   default int compare(Boolean ok1, Boolean ok2) {
      return this.compare(ok1, ok2);
   }

   default BooleanComparator thenComparing(BooleanComparator second) {
      return (BooleanComparator)((Serializable)((k1, k2) -> {
         int comp = this.compare(k1, k2);
         return comp == 0 ? second.compare(k1, k2) : comp;
      }));
   }

   default Comparator<Boolean> thenComparing(Comparator<? super Boolean> second) {
      return (Comparator)(second instanceof BooleanComparator ? this.thenComparing((BooleanComparator)second) : super.thenComparing(second));
   }

   static <U extends Comparable<? super U>> BooleanComparator comparing(Boolean2ObjectFunction<? extends U> keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (BooleanComparator)((Serializable)((k1, k2) -> {
         return ((Comparable)keyExtractor.get(k1)).compareTo(keyExtractor.get(k2));
      }));
   }

   static <U extends Comparable<? super U>> BooleanComparator comparing(Boolean2ObjectFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
      Objects.requireNonNull(keyExtractor);
      Objects.requireNonNull(keyComparator);
      return (BooleanComparator)((Serializable)((k1, k2) -> {
         return keyComparator.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static BooleanComparator comparingInt(Boolean2IntFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (BooleanComparator)((Serializable)((k1, k2) -> {
         return Integer.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static BooleanComparator comparingLong(Boolean2LongFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (BooleanComparator)((Serializable)((k1, k2) -> {
         return Long.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static BooleanComparator comparingDouble(Boolean2DoubleFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (BooleanComparator)((Serializable)((k1, k2) -> {
         return Double.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   // $FF: synthetic method
   private static Object $deserializeLambda$(SerializedLambda lambda) {
      String var1 = lambda.getImplMethodName();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -2124727450:
         if (var1.equals("lambda$comparingDouble$1b10f46c$1")) {
            var2 = 4;
         }
         break;
      case -1995218219:
         if (var1.equals("lambda$comparingInt$7569105c$1")) {
            var2 = 0;
         }
         break;
      case -1692070962:
         if (var1.equals("lambda$thenComparing$e8be742d$1")) {
            var2 = 3;
         }
         break;
      case 1655702392:
         if (var1.equals("lambda$comparingLong$63d03596$1")) {
            var2 = 1;
         }
         break;
      case 1699108815:
         if (var1.equals("lambda$comparing$54a0cf1c$1")) {
            var2 = 2;
         }
         break;
      case 1829725904:
         if (var1.equals("lambda$comparing$b49bc183$1")) {
            var2 = 5;
         }
      }

      switch(var2) {
      case 0:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/booleans/BooleanComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(ZZ)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/booleans/BooleanComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/booleans/Boolean2IntFunction;ZZ)I")) {
            return (k1, k2) -> {
               return Integer.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 1:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/booleans/BooleanComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(ZZ)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/booleans/BooleanComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/booleans/Boolean2LongFunction;ZZ)I")) {
            return (k1, k2) -> {
               return Long.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 2:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/booleans/BooleanComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(ZZ)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/booleans/BooleanComparator") && lambda.getImplMethodSignature().equals("(Ljava/util/Comparator;Lac/grim/grimac/shaded/fastutil/booleans/Boolean2ObjectFunction;ZZ)I")) {
            return (k1, k2) -> {
               return keyComparator.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 3:
         if (lambda.getImplMethodKind() == 7 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/booleans/BooleanComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(ZZ)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/booleans/BooleanComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/booleans/BooleanComparator;ZZ)I")) {
            BooleanComparator var10000 = (BooleanComparator)lambda.getCapturedArg(0);
            return (k1, k2) -> {
               int comp = this.compare(k1, k2);
               return comp == 0 ? second.compare(k1, k2) : comp;
            };
         }
         break;
      case 4:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/booleans/BooleanComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(ZZ)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/booleans/BooleanComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/booleans/Boolean2DoubleFunction;ZZ)I")) {
            return (k1, k2) -> {
               return Double.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 5:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/booleans/BooleanComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(ZZ)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/booleans/BooleanComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/booleans/Boolean2ObjectFunction;ZZ)I")) {
            return (k1, k2) -> {
               return ((Comparable)keyExtractor.get(k1)).compareTo(keyExtractor.get(k2));
            };
         }
      }

      throw new IllegalArgumentException("Invalid lambda deserialization");
   }
}
