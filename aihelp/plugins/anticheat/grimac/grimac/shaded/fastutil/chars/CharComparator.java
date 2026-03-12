package ac.grim.grimac.shaded.fastutil.chars;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.Comparator;
import java.util.Objects;

@FunctionalInterface
public interface CharComparator extends Comparator<Character> {
   int compare(char var1, char var2);

   default CharComparator reversed() {
      return CharComparators.oppositeComparator(this);
   }

   /** @deprecated */
   @Deprecated
   default int compare(Character ok1, Character ok2) {
      return this.compare(ok1, ok2);
   }

   default CharComparator thenComparing(CharComparator second) {
      return (CharComparator)((Serializable)((k1, k2) -> {
         int comp = this.compare(k1, k2);
         return comp == 0 ? second.compare(k1, k2) : comp;
      }));
   }

   default Comparator<Character> thenComparing(Comparator<? super Character> second) {
      return (Comparator)(second instanceof CharComparator ? this.thenComparing((CharComparator)second) : super.thenComparing(second));
   }

   static <U extends Comparable<? super U>> CharComparator comparing(Char2ObjectFunction<? extends U> keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (CharComparator)((Serializable)((k1, k2) -> {
         return ((Comparable)keyExtractor.get(k1)).compareTo(keyExtractor.get(k2));
      }));
   }

   static <U extends Comparable<? super U>> CharComparator comparing(Char2ObjectFunction<? extends U> keyExtractor, Comparator<? super U> keyComparator) {
      Objects.requireNonNull(keyExtractor);
      Objects.requireNonNull(keyComparator);
      return (CharComparator)((Serializable)((k1, k2) -> {
         return keyComparator.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static CharComparator comparingInt(Char2IntFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (CharComparator)((Serializable)((k1, k2) -> {
         return Integer.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static CharComparator comparingLong(Char2LongFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (CharComparator)((Serializable)((k1, k2) -> {
         return Long.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   static CharComparator comparingDouble(Char2DoubleFunction keyExtractor) {
      Objects.requireNonNull(keyExtractor);
      return (CharComparator)((Serializable)((k1, k2) -> {
         return Double.compare(keyExtractor.get(k1), keyExtractor.get(k2));
      }));
   }

   // $FF: synthetic method
   private static Object $deserializeLambda$(SerializedLambda lambda) {
      String var1 = lambda.getImplMethodName();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -1394272590:
         if (var1.equals("lambda$comparingLong$d01435c4$1")) {
            var2 = 1;
         }
         break;
      case -1352911883:
         if (var1.equals("lambda$thenComparing$2b1ecd07$1")) {
            var2 = 2;
         }
         break;
      case -1195062129:
         if (var1.equals("lambda$comparingDouble$757ae4da$1")) {
            var2 = 3;
         }
         break;
      case -181928089:
         if (var1.equals("lambda$comparing$267813b6$1")) {
            var2 = 0;
         }
         break;
      case 1876976956:
         if (var1.equals("lambda$comparingInt$e7399ff6$1")) {
            var2 = 4;
         }
         break;
      case 1918321626:
         if (var1.equals("lambda$comparing$98f60131$1")) {
            var2 = 5;
         }
      }

      switch(var2) {
      case 0:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/chars/CharComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(CC)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/chars/CharComparator") && lambda.getImplMethodSignature().equals("(Ljava/util/Comparator;Lac/grim/grimac/shaded/fastutil/chars/Char2ObjectFunction;CC)I")) {
            return (k1, k2) -> {
               return keyComparator.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 1:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/chars/CharComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(CC)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/chars/CharComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/chars/Char2LongFunction;CC)I")) {
            return (k1, k2) -> {
               return Long.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 2:
         if (lambda.getImplMethodKind() == 7 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/chars/CharComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(CC)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/chars/CharComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/chars/CharComparator;CC)I")) {
            CharComparator var10000 = (CharComparator)lambda.getCapturedArg(0);
            return (k1, k2) -> {
               int comp = this.compare(k1, k2);
               return comp == 0 ? second.compare(k1, k2) : comp;
            };
         }
         break;
      case 3:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/chars/CharComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(CC)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/chars/CharComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/chars/Char2DoubleFunction;CC)I")) {
            return (k1, k2) -> {
               return Double.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 4:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/chars/CharComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(CC)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/chars/CharComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/chars/Char2IntFunction;CC)I")) {
            return (k1, k2) -> {
               return Integer.compare(keyExtractor.get(k1), keyExtractor.get(k2));
            };
         }
         break;
      case 5:
         if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("ac/grim/grimac/shaded/fastutil/chars/CharComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(CC)I") && lambda.getImplClass().equals("ac/grim/grimac/shaded/fastutil/chars/CharComparator") && lambda.getImplMethodSignature().equals("(Lac/grim/grimac/shaded/fastutil/chars/Char2ObjectFunction;CC)I")) {
            return (k1, k2) -> {
               return ((Comparable)keyExtractor.get(k1)).compareTo(keyExtractor.get(k2));
            };
         }
      }

      throw new IllegalArgumentException("Invalid lambda deserialization");
   }
}
