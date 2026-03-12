package fr.xephi.authme.libs.com.google.common.math;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import java.math.BigDecimal;
import java.math.RoundingMode;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public class BigDecimalMath {
   private BigDecimalMath() {
   }

   public static double roundToDouble(BigDecimal x, RoundingMode mode) {
      return BigDecimalMath.BigDecimalToDoubleRounder.INSTANCE.roundToDouble(x, mode);
   }

   private static class BigDecimalToDoubleRounder extends ToDoubleRounder<BigDecimal> {
      static final BigDecimalMath.BigDecimalToDoubleRounder INSTANCE = new BigDecimalMath.BigDecimalToDoubleRounder();

      double roundToDoubleArbitrarily(BigDecimal bigDecimal) {
         return bigDecimal.doubleValue();
      }

      int sign(BigDecimal bigDecimal) {
         return bigDecimal.signum();
      }

      BigDecimal toX(double d, RoundingMode mode) {
         return new BigDecimal(d);
      }

      BigDecimal minus(BigDecimal a, BigDecimal b) {
         return a.subtract(b);
      }
   }
}
