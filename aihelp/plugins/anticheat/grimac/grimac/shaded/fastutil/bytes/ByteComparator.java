package ac.grim.grimac.shaded.fastutil.bytes;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.Comparator;
import java.util.Objects;

@FunctionalInterface
public interface ByteComparator extends Comparator<Byte> {
   int compare(byte var1, byte var2);

   default ByteComparator reversed() {
      return ByteComparators.oppositeComparator(this);
   }

   /** @deprecated */
   @Deprecated
   default int compare(Byte ok1, Byte ok2) {
      return this.compare(ok1, ok2);
   }

   default ByteComparator thenComparing(ByteComparator second) {
      return (ByteComparator)((Serializable)((k1, k2) -> {
         int comp = this.compare(k1, k2);
         return comp == 0 ? second.compare(k1, k2) : comp;
      }));
   }

   default Comparator<Byte> thenComparing(Comparator<? super Byte> second) {
      return (Comparator)(second instanceof ByteComparator ? this.thenComparing((ByteComparator)second) : super.thenComparing(second));
   }

   static <U extends Comparable<? super U>> ByteComparator comparing(Byte2ObjectFunction<? extends U> keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (ByteComparator)((Serializable)((k1, k2) -> {
         return ((Comparable)keyExtractor.get(k1)).compareTo(keyExtractor.get(k2));
      }));
   }

   static <U extends Comparable<? super U>> ByteComparator comparing(Byte2ObjectFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
      Objects.requireNonNull(keyExtractor);
      Objects.requireNonNull(keyComparator);
      return (ByteComparator)((Serializable)((k1, k2) -> {
         return keyComparator.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static ByteComparator comparingInt(Byte2IntFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (ByteComparator)((Serializable)((k1, k2) -> {
         return Integer.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static ByteComparator comparingLong(Byte2LongFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (ByteComparator)((Serializable)((k1, k2) -> {
         return Long.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static ByteComparator comparingDouble(Byte2DoubleFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (ByteComparator)((Serializable)((k1, k2) -> {
         return Double.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   // $FF: synthetic method
   private static Object $deserializeLambda$(SerializedLambda lambda) {
      String var1 = lambda.getImplMethodName();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -1183018810:
         if (var1.equals("lambda$comparingInt$eb9931ae$1")) {
            var2 = 3;
         }
         break;
      case -151079021:
         if (var1.equals("lambda$comparing$905656c1$1")) {
            var2 = 5;
         }
         break;
      case 1506061917:
         if (var1.equals("lambda$comparingLong$56933f54$1")) {
            var2 = 1;
         }
         break;
      case 1879028614:
         if (var1.equals("lambda$comparingDouble$3d98a16a$1")) {
            var2 = 2;
         }
         break;
      case 1974965104:
         if (var1.equals("lambda$thenComparing$6e387fbf$1")) {
            var2 = 0;
         }
         break;
      case 1980841853:
         if (var1.equals("lambda$comparing$96e806e$1")) {
            var2 = 4;
         }
      }

      switch(var2) {
      case 0:
         if (lambda.getImplMethodKind() == 7 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/bytes/ByteComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(BB)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/bytes/ByteComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/bytes/ByteComparator;BB)I")) {
            ByteComparator var10000 = (ByteComparator)lambda.getCapturedArg(0);
            return (k1, k2) -> {
               int comp = this.compare(k1, k2);
               return comp == 0 ? second.compare(k1, k2) : comp;
            };
         }
         break;
      case 1:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/bytes/ByteComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(BB)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/bytes/ByteComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/bytes/Byte2LongFunction;BB)I")) {
            return (k1, k2) -> {
               return Long.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 2:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/bytes/ByteComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(BB)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/bytes/ByteComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/bytes/Byte2DoubleFunction;BB)I")) {
            return (k1, k2) -> {
               return Double.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 3:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/bytes/ByteComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(BB)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/bytes/ByteComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/bytes/Byte2IntFunction;BB)I")) {
            return (k1, k2) -> {
               return Integer.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 4:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/bytes/ByteComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(BB)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/bytes/ByteComparator") && lambda.getImplMethodSignature().equals("(Ljava/util/Comparator;Lac/grim/grimac/shaded/fastutil/bytes/Byte2ObjectFunction;BB)I")) {
            return (k1, k2) -> {
               return keyComparator.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 5:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/bytes/ByteComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(BB)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/bytes/ByteComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/bytes/Byte2ObjectFunction;BB)I")) {
            return (k1, k2) -> {
               return ((Comparable)keyExtractor.get(k1)).compareTo(keyExtractor.get(k2));
            };
         }
      }

      throw new IllegalArgumentException("Invalid lambda deserialization");
   }
}
