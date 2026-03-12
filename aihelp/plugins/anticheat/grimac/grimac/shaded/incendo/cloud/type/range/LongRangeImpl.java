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
   from = "LongRange",
   generator = "Immutables"
)
@Immutable
final class LongRangeImpl implements LongRange {
   private final long minLong;
   private final long maxLong;

   private LongRangeImpl(long minLong, long maxLong) {
      this.minLong = minLong;
      this.maxLong = maxLong;
   }

   public long minLong() {
      return this.minLong;
   }

   public long maxLong() {
      return this.maxLong;
   }

   public final LongRangeImpl withMinLong(long value) {
      return this.minLong == value ? this : new LongRangeImpl(value, this.maxLong);
   }

   public final LongRangeImpl withMaxLong(long value) {
      return this.maxLong == value ? this : new LongRangeImpl(this.minLong, value);
   }

   public boolean equals(@Nullable Object another) {
      if (this == another) {
         return true;
      } else {
         return another instanceof LongRangeImpl && this.equalTo(0, (LongRangeImpl)another);
      }
   }

   private boolean equalTo(int synthetic, LongRangeImpl another) {
      return this.minLong == another.minLong && this.maxLong == another.maxLong;
   }

   public int hashCode() {
      int h = 5381;
      int h = h + (h << 5) + Long.hashCode(this.minLong);
      h += (h << 5) + Long.hashCode(this.maxLong);
      return h;
   }

   public String toString() {
      return "LongRange{minLong=" + this.minLong + ", maxLong=" + this.maxLong + "}";
   }

   public static LongRangeImpl of(long minLong, long maxLong) {
      return new LongRangeImpl(minLong, maxLong);
   }

   public static LongRangeImpl copyOf(LongRange instance) {
      return instance instanceof LongRangeImpl ? (LongRangeImpl)instance : of(instance.minLong(), instance.maxLong());
   }
}
