package ac.grim.grimac.shaded.incendo.cloud.type.range;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.immutables.value.Generated;

@ParametersAreNonnullByDefault
@CheckReturnValue
@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
@Generated(
   from = "DoubleRange",
   generator = "Immutables"
)
@Immutable
final class DoubleRangeImpl implements DoubleRange {
   private final double minDouble;
   private final double maxDouble;

   private DoubleRangeImpl(double minDouble, double maxDouble) {
      this.minDouble = minDouble;
      this.maxDouble = maxDouble;
   }

   public double minDouble() {
      return this.minDouble;
   }

   public double maxDouble() {
      return this.maxDouble;
   }

   public final DoubleRangeImpl withMinDouble(double value) {
      return Double.doubleToLongBits(this.minDouble) == Double.doubleToLongBits(value) ? this : new DoubleRangeImpl(value, this.maxDouble);
   }

   public final DoubleRangeImpl withMaxDouble(double value) {
      return Double.doubleToLongBits(this.maxDouble) == Double.doubleToLongBits(value) ? this : new DoubleRangeImpl(this.minDouble, value);
   }

   public boolean equals(@Nullable Object another) {
      if (this == another) {
         return true;
      } else {
         return another instanceof DoubleRangeImpl && this.equalTo(0, (DoubleRangeImpl)another);
      }
   }

   private boolean equalTo(int synthetic, DoubleRangeImpl another) {
      return Double.doubleToLongBits(this.minDouble) == Double.doubleToLongBits(another.minDouble) && Double.doubleToLongBits(this.maxDouble) == Double.doubleToLongBits(another.maxDouble);
   }

   public int hashCode() {
      int h = 5381;
      int h = h + (h << 5) + Double.hashCode(this.minDouble);
      h += (h << 5) + Double.hashCode(this.maxDouble);
      return h;
   }

   public String toString() {
      return "DoubleRange{minDouble=" + this.minDouble + ", maxDouble=" + this.maxDouble + "}";
   }

   public static DoubleRangeImpl of(double minDouble, double maxDouble) {
      return new DoubleRangeImpl(minDouble, maxDouble);
   }

   public static DoubleRangeImpl copyOf(DoubleRange instance) {
      return instance instanceof DoubleRangeImpl ? (DoubleRangeImpl)instance : of(instance.minDouble(), instance.maxDouble());
   }
}
