package ac.grim.grimac.shaded.incendo.cloud.type.range;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public interface Range<N extends Number> {
   @NonNull
   N min();

   @NonNull
   N max();

   @NonNull
   static ByteRange byteRange(final byte min, final byte max) {
      return ByteRangeImpl.of(min, max);
   }

   @NonNull
   static DoubleRange doubleRange(final double min, final double max) {
      return DoubleRangeImpl.of(min, max);
   }

   @NonNull
   static FloatRange floatRange(final float min, final float max) {
      return FloatRangeImpl.of(min, max);
   }

   @NonNull
   static IntRange intRange(final int min, final int max) {
      return IntRangeImpl.of(min, max);
   }

   @NonNull
   static LongRange longRange(final long min, final long max) {
      return LongRangeImpl.of(min, max);
   }

   @NonNull
   static ShortRange shortRange(final short min, final short max) {
      return ShortRangeImpl.of(min, max);
   }
}
