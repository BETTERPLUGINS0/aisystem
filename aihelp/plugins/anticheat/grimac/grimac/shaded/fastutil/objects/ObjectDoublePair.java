package ac.grim.grimac.shaded.fastutil.objects;

import ac.grim.grimac.shaded.fastutil.Pair;
import java.util.Comparator;

public interface ObjectDoublePair<K> extends Pair<K, Double> {
   double rightDouble();

   /** @deprecated */
   @Deprecated
   default Double right() {
      return this.rightDouble();
   }

   default ObjectDoublePair<K> right(double r) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default ObjectDoublePair<K> right(Double l) {
      return this.right(l);
   }

   default double secondDouble() {
      return this.rightDouble();
   }

   /** @deprecated */
   @Deprecated
   default Double second() {
      return this.secondDouble();
   }

   default ObjectDoublePair<K> second(double r) {
      return this.right(r);
   }

   /** @deprecated */
   @Deprecated
   default ObjectDoublePair<K> second(Double l) {
      return this.second(l);
   }

   default double valueDouble() {
      return this.rightDouble();
   }

   /** @deprecated */
   @Deprecated
   default Double value() {
      return this.valueDouble();
   }

   default ObjectDoublePair<K> value(double r) {
      return this.right(r);
   }

   /** @deprecated */
   @Deprecated
   default ObjectDoublePair<K> value(Double l) {
      return this.value(l);
   }

   static <K> ObjectDoublePair<K> of(K left, double right) {
      return new ObjectDoubleImmutablePair(left, right);
   }

   static <K> Comparator<ObjectDoublePair<K>> lexComparator() {
      return (x, y) -> {
         int t = ((Comparable)x.left()).compareTo(y.left());
         return t != 0 ? t : Double.compare(x.rightDouble(), y.rightDouble());
      };
   }
}
