package ac.grim.grimac.shaded.fastutil.doubles;

import ac.grim.grimac.shaded.fastutil.Pair;
import java.util.Comparator;

public interface DoubleIntPair extends Pair<Double, Integer> {
   double leftDouble();

   /** @deprecated */
   @Deprecated
   default Double left() {
      return this.leftDouble();
   }

   default DoubleIntPair left(double l) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default DoubleIntPair left(Double l) {
      return this.left(l);
   }

   default double firstDouble() {
      return this.leftDouble();
   }

   /** @deprecated */
   @Deprecated
   default Double first() {
      return this.firstDouble();
   }

   default DoubleIntPair first(double l) {
      return this.left(l);
   }

   /** @deprecated */
   @Deprecated
   default DoubleIntPair first(Double l) {
      return this.first(l);
   }

   default double keyDouble() {
      return this.firstDouble();
   }

   /** @deprecated */
   @Deprecated
   default Double key() {
      return this.keyDouble();
   }

   default DoubleIntPair key(double l) {
      return this.left(l);
   }

   /** @deprecated */
   @Deprecated
   default DoubleIntPair key(Double l) {
      return this.key(l);
   }

   int rightInt();

   /** @deprecated */
   @Deprecated
   default Integer right() {
      return this.rightInt();
   }

   default DoubleIntPair right(int r) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default DoubleIntPair right(Integer l) {
      return this.right(l);
   }

   default int secondInt() {
      return this.rightInt();
   }

   /** @deprecated */
   @Deprecated
   default Integer second() {
      return this.secondInt();
   }

   default DoubleIntPair second(int r) {
      return this.right(r);
   }

   /** @deprecated */
   @Deprecated
   default DoubleIntPair second(Integer l) {
      return this.second(l);
   }

   default int valueInt() {
      return this.rightInt();
   }

   /** @deprecated */
   @Deprecated
   default Integer value() {
      return this.valueInt();
   }

   default DoubleIntPair value(int r) {
      return this.right(r);
   }

   /** @deprecated */
   @Deprecated
   default DoubleIntPair value(Integer l) {
      return this.value(l);
   }

   static DoubleIntPair of(double left, int right) {
      return new DoubleIntImmutablePair(left, right);
   }

   static Comparator<DoubleIntPair> lexComparator() {
      return (x, y) -> {
         int t = Double.compare(x.leftDouble(), y.leftDouble());
         return t != 0 ? t : Integer.compare(x.rightInt(), y.rightInt());
      };
   }
}
