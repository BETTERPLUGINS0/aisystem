package fr.xephi.authme.libs.com.google.common.math;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.math.RoundingMode;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
abstract class ToDoubleRounder<X extends Number & Comparable<X>> {
   abstract double roundToDoubleArbitrarily(X var1);

   abstract int sign(X var1);

   abstract X toX(double var1, RoundingMode var3);

   abstract X minus(X var1, X var2);

   final double roundToDouble(X x, RoundingMode mode) {
      Preconditions.checkNotNull(x, "x");
      Preconditions.checkNotNull(mode, "mode");
      double roundArbitrarily = this.roundToDoubleArbitrarily(x);
      if (Double.isInfinite(roundArbitrarily)) {
         switch(mode) {
         case DOWN:
         case HALF_EVEN:
         case HALF_DOWN:
         case HALF_UP:
            return Double.MAX_VALUE * (double)this.sign(x);
         case FLOOR:
            return roundArbitrarily == Double.POSITIVE_INFINITY ? Double.MAX_VALUE : Double.NEGATIVE_INFINITY;
         case CEILING:
            return roundArbitrarily == Double.POSITIVE_INFINITY ? Double.POSITIVE_INFINITY : -1.7976931348623157E308D;
         case UP:
            return roundArbitrarily;
         case UNNECESSARY:
            String var5 = String.valueOf(x);
            throw new ArithmeticException((new StringBuilder(44 + String.valueOf(var5).length())).append(var5).append(" cannot be represented precisely as a double").toString());
         }
      }

      X roundArbitrarilyAsX = this.toX(roundArbitrarily, RoundingMode.UNNECESSARY);
      int cmpXToRoundArbitrarily = ((Comparable)x).compareTo(roundArbitrarilyAsX);
      switch(mode) {
      case DOWN:
         if (this.sign(x) >= 0) {
            return cmpXToRoundArbitrarily >= 0 ? roundArbitrarily : DoubleUtils.nextDown(roundArbitrarily);
         }

         return cmpXToRoundArbitrarily <= 0 ? roundArbitrarily : Math.nextUp(roundArbitrarily);
      case HALF_EVEN:
      case HALF_DOWN:
      case HALF_UP:
         Number roundFloor;
         double roundFloorAsDouble;
         Number roundCeiling;
         double roundCeilingAsDouble;
         if (cmpXToRoundArbitrarily >= 0) {
            roundFloorAsDouble = roundArbitrarily;
            roundFloor = roundArbitrarilyAsX;
            roundCeilingAsDouble = Math.nextUp(roundArbitrarily);
            if (roundCeilingAsDouble == Double.POSITIVE_INFINITY) {
               return roundArbitrarily;
            }

            roundCeiling = this.toX(roundCeilingAsDouble, RoundingMode.CEILING);
         } else {
            roundCeilingAsDouble = roundArbitrarily;
            roundCeiling = roundArbitrarilyAsX;
            roundFloorAsDouble = DoubleUtils.nextDown(roundArbitrarily);
            if (roundFloorAsDouble == Double.NEGATIVE_INFINITY) {
               return roundArbitrarily;
            }

            roundFloor = this.toX(roundFloorAsDouble, RoundingMode.FLOOR);
         }

         X deltaToFloor = this.minus(x, roundFloor);
         X deltaToCeiling = this.minus(roundCeiling, x);
         int diff = ((Comparable)deltaToFloor).compareTo(deltaToCeiling);
         if (diff < 0) {
            return roundFloorAsDouble;
         } else if (diff > 0) {
            return roundCeilingAsDouble;
         } else {
            switch(mode) {
            case HALF_EVEN:
               return (Double.doubleToRawLongBits(roundFloorAsDouble) & 1L) == 0L ? roundFloorAsDouble : roundCeilingAsDouble;
            case HALF_DOWN:
               return this.sign(x) >= 0 ? roundFloorAsDouble : roundCeilingAsDouble;
            case HALF_UP:
               return this.sign(x) >= 0 ? roundCeilingAsDouble : roundFloorAsDouble;
            default:
               throw new AssertionError("impossible");
            }
         }
      case FLOOR:
         return cmpXToRoundArbitrarily >= 0 ? roundArbitrarily : DoubleUtils.nextDown(roundArbitrarily);
      case CEILING:
         return cmpXToRoundArbitrarily <= 0 ? roundArbitrarily : Math.nextUp(roundArbitrarily);
      case UP:
         if (this.sign(x) >= 0) {
            return cmpXToRoundArbitrarily <= 0 ? roundArbitrarily : Math.nextUp(roundArbitrarily);
         }

         return cmpXToRoundArbitrarily >= 0 ? roundArbitrarily : DoubleUtils.nextDown(roundArbitrarily);
      case UNNECESSARY:
         MathPreconditions.checkRoundingUnnecessary(cmpXToRoundArbitrarily == 0);
         return roundArbitrarily;
      default:
         throw new AssertionError("impossible");
      }
   }
}
