package ac.grim.grimac.shaded.fastutil.shorts;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.Comparator;
import java.util.Objects;

@FunctionalInterface
public interface ShortComparator extends Comparator<Short> {
   int compare(short var1, short var2);

   default ShortComparator reversed() {
      return ShortComparators.oppositeComparator(this);
   }

   /** @deprecated */
   @Deprecated
   default int compare(Short ok1, Short ok2) {
      return this.compare(ok1, ok2);
   }

   default ShortComparator thenComparing(ShortComparator second) {
      return (ShortComparator)((Serializable)((k1, k2) -> {
         int comp = this.compare(k1, k2);
         return comp == 0 ? second.compare(k1, k2) : comp;
      }));
   }

   default Comparator<Short> thenComparing(Comparator<? super Short> second) {
      return (Comparator)(second instanceof ShortComparator ? this.thenComparing((ShortComparator)second) : super.thenComparing(second));
   }

   static <U extends Comparable<? super U>> ShortComparator comparing(Short2ObjectFunction<? extends U> keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (ShortComparator)((Serializable)((k1, k2) -> {
         return ((Comparable)keyExtractor.get(k1)).compareTo(keyExtractor.get(k2));
      }));
   }

   static <U extends Comparable<? super U>> ShortComparator comparing(Short2ObjectFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
      Objects.requireNonNull(keyExtractor);
      Objects.requireNonNull(keyComparator);
      return (ShortComparator)((Serializable)((k1, k2) -> {
         return keyComparator.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static ShortComparator comparingInt(Short2IntFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (ShortComparator)((Serializable)((k1, k2) -> {
         return Integer.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static ShortComparator comparingLong(Short2LongFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (ShortComparator)((Serializable)((k1, k2) -> {
         return Long.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static ShortComparator comparingDouble(Short2DoubleFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (ShortComparator)((Serializable)((k1, k2) -> {
         return Double.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   // $FF: synthetic method
   private static Object $deserializeLambda$(SerializedLambda lambda) {
      String var1 = lambda.getImplMethodName();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -1248899066:
         if (var1.equals("lambda$comparingInt$e1a8f01c$1")) {
            var2 = 4;
         }
         break;
      case -678963432:
         if (var1.equals("lambda$comparingDouble$76ca64ac$1")) {
            var2 = 5;
         }
         break;
      case 651886536:
         if (var1.equals("lambda$comparingLong$eeb7bd56$1")) {
            var2 = 1;
         }
         break;
      case 658922721:
         if (var1.equals("lambda$comparing$5b778a43$1")) {
            var2 = 2;
         }
         break;
      case 1233647318:
         if (var1.equals("lambda$thenComparing$953dd6d$1")) {
            var2 = 0;
         }
         break;
      case 1510343521:
         if (var1.equals("lambda$comparing$f85dc8dc$1")) {
            var2 = 3;
         }
      }

      switch(var2) {
      case 0:
         if (lambda.getImplMethodKind() == 7 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/shorts/ShortComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(SS)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/shorts/ShortComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/shorts/ShortComparator;SS)I")) {
            ShortComparator var10000 = (ShortComparator)lambda.getCapturedArg(0);
            return (k1, k2) -> {
               int comp = this.compare(k1, k2);
               return comp == 0 ? second.compare(k1, k2) : comp;
            };
         }
         break;
      case 1:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/shorts/ShortComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(SS)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/shorts/ShortComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/shorts/Short2LongFunction;SS)I")) {
            return (k1, k2) -> {
               return Long.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 2:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/shorts/ShortComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(SS)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/shorts/ShortComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/shorts/Short2ObjectFunction;SS)I")) {
            return (k1, k2) -> {
               return ((Comparable)keyExtractor.get(k1)).compareTo(keyExtractor.get(k2));
            };
         }
         break;
      case 3:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/shorts/ShortComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(SS)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/shorts/ShortComparator") && lambda.getImplMethodSignature().equals("(Ljava/util/Comparator;Lac/grim/grimac/shaded/fastutil/shorts/Short2ObjectFunction;SS)I")) {
            return (k1, k2) -> {
               return keyComparator.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 4:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/shorts/ShortComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(SS)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/shorts/ShortComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/shorts/Short2IntFunction;SS)I")) {
            return (k1, k2) -> {
               return Integer.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 5:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/shorts/ShortComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(SS)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/shorts/ShortComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/shorts/Short2DoubleFunction;SS)I")) {
            return (k1, k2) -> {
               return Double.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
      }

      throw new IllegalArgumentException("Invalid lambda deserialization");
   }
}
