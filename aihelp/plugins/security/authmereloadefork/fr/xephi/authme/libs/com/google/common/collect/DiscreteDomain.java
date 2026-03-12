package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.primitives.Ints;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.NoSuchElementException;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class DiscreteDomain<C extends Comparable> {
   final boolean supportsFastOffset;

   public static DiscreteDomain<Integer> integers() {
      return DiscreteDomain.IntegerDomain.INSTANCE;
   }

   public static DiscreteDomain<Long> longs() {
      return DiscreteDomain.LongDomain.INSTANCE;
   }

   public static DiscreteDomain<BigInteger> bigIntegers() {
      return DiscreteDomain.BigIntegerDomain.INSTANCE;
   }

   protected DiscreteDomain() {
      this(false);
   }

   private DiscreteDomain(boolean supportsFastOffset) {
      this.supportsFastOffset = supportsFastOffset;
   }

   C offset(C origin, long distance) {
      C current = origin;
      CollectPreconditions.checkNonnegative(distance, "distance");

      for(long i = 0L; i < distance; ++i) {
         current = this.next(current);
         if (current == null) {
            String var7 = String.valueOf(origin);
            throw new IllegalArgumentException((new StringBuilder(51 + String.valueOf(var7).length())).append("overflowed computing offset(").append(var7).append(", ").append(distance).append(")").toString());
         }
      }

      return current;
   }

   @CheckForNull
   public abstract C next(C var1);

   @CheckForNull
   public abstract C previous(C var1);

   public abstract long distance(C var1, C var2);

   @CanIgnoreReturnValue
   public C minValue() {
      throw new NoSuchElementException();
   }

   @CanIgnoreReturnValue
   public C maxValue() {
      throw new NoSuchElementException();
   }

   // $FF: synthetic method
   DiscreteDomain(boolean x0, Object x1) {
      this(x0);
   }

   private static final class BigIntegerDomain extends DiscreteDomain<BigInteger> implements Serializable {
      private static final DiscreteDomain.BigIntegerDomain INSTANCE = new DiscreteDomain.BigIntegerDomain();
      private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
      private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
      private static final long serialVersionUID = 0L;

      BigIntegerDomain() {
         super(true, null);
      }

      public BigInteger next(BigInteger value) {
         return value.add(BigInteger.ONE);
      }

      public BigInteger previous(BigInteger value) {
         return value.subtract(BigInteger.ONE);
      }

      BigInteger offset(BigInteger origin, long distance) {
         CollectPreconditions.checkNonnegative(distance, "distance");
         return origin.add(BigInteger.valueOf(distance));
      }

      public long distance(BigInteger start, BigInteger end) {
         return end.subtract(start).max(MIN_LONG).min(MAX_LONG).longValue();
      }

      private Object readResolve() {
         return INSTANCE;
      }

      public String toString() {
         return "DiscreteDomain.bigIntegers()";
      }
   }

   private static final class LongDomain extends DiscreteDomain<Long> implements Serializable {
      private static final DiscreteDomain.LongDomain INSTANCE = new DiscreteDomain.LongDomain();
      private static final long serialVersionUID = 0L;

      LongDomain() {
         super(true, null);
      }

      @CheckForNull
      public Long next(Long value) {
         long l = value;
         return l == Long.MAX_VALUE ? null : l + 1L;
      }

      @CheckForNull
      public Long previous(Long value) {
         long l = value;
         return l == Long.MIN_VALUE ? null : l - 1L;
      }

      Long offset(Long origin, long distance) {
         CollectPreconditions.checkNonnegative(distance, "distance");
         long result = origin + distance;
         if (result < 0L) {
            Preconditions.checkArgument(origin < 0L, "overflow");
         }

         return result;
      }

      public long distance(Long start, Long end) {
         long result = end - start;
         if (end > start && result < 0L) {
            return Long.MAX_VALUE;
         } else {
            return end < start && result > 0L ? Long.MIN_VALUE : result;
         }
      }

      public Long minValue() {
         return Long.MIN_VALUE;
      }

      public Long maxValue() {
         return Long.MAX_VALUE;
      }

      private Object readResolve() {
         return INSTANCE;
      }

      public String toString() {
         return "DiscreteDomain.longs()";
      }
   }

   private static final class IntegerDomain extends DiscreteDomain<Integer> implements Serializable {
      private static final DiscreteDomain.IntegerDomain INSTANCE = new DiscreteDomain.IntegerDomain();
      private static final long serialVersionUID = 0L;

      IntegerDomain() {
         super(true, null);
      }

      @CheckForNull
      public Integer next(Integer value) {
         int i = value;
         return i == Integer.MAX_VALUE ? null : i + 1;
      }

      @CheckForNull
      public Integer previous(Integer value) {
         int i = value;
         return i == Integer.MIN_VALUE ? null : i - 1;
      }

      Integer offset(Integer origin, long distance) {
         CollectPreconditions.checkNonnegative(distance, "distance");
         return Ints.checkedCast(origin.longValue() + distance);
      }

      public long distance(Integer start, Integer end) {
         return (long)end - (long)start;
      }

      public Integer minValue() {
         return Integer.MIN_VALUE;
      }

      public Integer maxValue() {
         return Integer.MAX_VALUE;
      }

      private Object readResolve() {
         return INSTANCE;
      }

      public String toString() {
         return "DiscreteDomain.integers()";
      }
   }
}
