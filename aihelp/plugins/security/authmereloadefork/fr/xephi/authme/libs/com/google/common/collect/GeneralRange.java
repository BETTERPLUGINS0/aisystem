package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Objects;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Comparator;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   serializable = true
)
final class GeneralRange<T> implements Serializable {
   private final Comparator<? super T> comparator;
   private final boolean hasLowerBound;
   @CheckForNull
   private final T lowerEndpoint;
   private final BoundType lowerBoundType;
   private final boolean hasUpperBound;
   @CheckForNull
   private final T upperEndpoint;
   private final BoundType upperBoundType;
   @CheckForNull
   private transient GeneralRange<T> reverse;

   static <T extends Comparable> GeneralRange<T> from(Range<T> range) {
      T lowerEndpoint = range.hasLowerBound() ? range.lowerEndpoint() : null;
      BoundType lowerBoundType = range.hasLowerBound() ? range.lowerBoundType() : BoundType.OPEN;
      T upperEndpoint = range.hasUpperBound() ? range.upperEndpoint() : null;
      BoundType upperBoundType = range.hasUpperBound() ? range.upperBoundType() : BoundType.OPEN;
      return new GeneralRange(Ordering.natural(), range.hasLowerBound(), lowerEndpoint, lowerBoundType, range.hasUpperBound(), upperEndpoint, upperBoundType);
   }

   static <T> GeneralRange<T> all(Comparator<? super T> comparator) {
      return new GeneralRange(comparator, false, (Object)null, BoundType.OPEN, false, (Object)null, BoundType.OPEN);
   }

   static <T> GeneralRange<T> downTo(Comparator<? super T> comparator, @ParametricNullness T endpoint, BoundType boundType) {
      return new GeneralRange(comparator, true, endpoint, boundType, false, (Object)null, BoundType.OPEN);
   }

   static <T> GeneralRange<T> upTo(Comparator<? super T> comparator, @ParametricNullness T endpoint, BoundType boundType) {
      return new GeneralRange(comparator, false, (Object)null, BoundType.OPEN, true, endpoint, boundType);
   }

   static <T> GeneralRange<T> range(Comparator<? super T> comparator, @ParametricNullness T lower, BoundType lowerType, @ParametricNullness T upper, BoundType upperType) {
      return new GeneralRange(comparator, true, lower, lowerType, true, upper, upperType);
   }

   private GeneralRange(Comparator<? super T> comparator, boolean hasLowerBound, @CheckForNull T lowerEndpoint, BoundType lowerBoundType, boolean hasUpperBound, @CheckForNull T upperEndpoint, BoundType upperBoundType) {
      this.comparator = (Comparator)Preconditions.checkNotNull(comparator);
      this.hasLowerBound = hasLowerBound;
      this.hasUpperBound = hasUpperBound;
      this.lowerEndpoint = lowerEndpoint;
      this.lowerBoundType = (BoundType)Preconditions.checkNotNull(lowerBoundType);
      this.upperEndpoint = upperEndpoint;
      this.upperBoundType = (BoundType)Preconditions.checkNotNull(upperBoundType);
      if (hasLowerBound) {
         comparator.compare(NullnessCasts.uncheckedCastNullableTToT(lowerEndpoint), NullnessCasts.uncheckedCastNullableTToT(lowerEndpoint));
      }

      if (hasUpperBound) {
         comparator.compare(NullnessCasts.uncheckedCastNullableTToT(upperEndpoint), NullnessCasts.uncheckedCastNullableTToT(upperEndpoint));
      }

      if (hasLowerBound && hasUpperBound) {
         int cmp = comparator.compare(NullnessCasts.uncheckedCastNullableTToT(lowerEndpoint), NullnessCasts.uncheckedCastNullableTToT(upperEndpoint));
         Preconditions.checkArgument(cmp <= 0, "lowerEndpoint (%s) > upperEndpoint (%s)", lowerEndpoint, upperEndpoint);
         if (cmp == 0) {
            Preconditions.checkArgument(lowerBoundType != BoundType.OPEN || upperBoundType != BoundType.OPEN);
         }
      }

   }

   Comparator<? super T> comparator() {
      return this.comparator;
   }

   boolean hasLowerBound() {
      return this.hasLowerBound;
   }

   boolean hasUpperBound() {
      return this.hasUpperBound;
   }

   boolean isEmpty() {
      return this.hasUpperBound() && this.tooLow(NullnessCasts.uncheckedCastNullableTToT(this.getUpperEndpoint())) || this.hasLowerBound() && this.tooHigh(NullnessCasts.uncheckedCastNullableTToT(this.getLowerEndpoint()));
   }

   boolean tooLow(@ParametricNullness T t) {
      if (!this.hasLowerBound()) {
         return false;
      } else {
         T lbound = NullnessCasts.uncheckedCastNullableTToT(this.getLowerEndpoint());
         int cmp = this.comparator.compare(t, lbound);
         return cmp < 0 | cmp == 0 & this.getLowerBoundType() == BoundType.OPEN;
      }
   }

   boolean tooHigh(@ParametricNullness T t) {
      if (!this.hasUpperBound()) {
         return false;
      } else {
         T ubound = NullnessCasts.uncheckedCastNullableTToT(this.getUpperEndpoint());
         int cmp = this.comparator.compare(t, ubound);
         return cmp > 0 | cmp == 0 & this.getUpperBoundType() == BoundType.OPEN;
      }
   }

   boolean contains(@ParametricNullness T t) {
      return !this.tooLow(t) && !this.tooHigh(t);
   }

   GeneralRange<T> intersect(GeneralRange<T> other) {
      Preconditions.checkNotNull(other);
      Preconditions.checkArgument(this.comparator.equals(other.comparator));
      boolean hasLowBound = this.hasLowerBound;
      T lowEnd = this.getLowerEndpoint();
      BoundType lowType = this.getLowerBoundType();
      if (!this.hasLowerBound()) {
         hasLowBound = other.hasLowerBound;
         lowEnd = other.getLowerEndpoint();
         lowType = other.getLowerBoundType();
      } else if (other.hasLowerBound()) {
         int cmp = this.comparator.compare(this.getLowerEndpoint(), other.getLowerEndpoint());
         if (cmp < 0 || cmp == 0 && other.getLowerBoundType() == BoundType.OPEN) {
            lowEnd = other.getLowerEndpoint();
            lowType = other.getLowerBoundType();
         }
      }

      boolean hasUpBound = this.hasUpperBound;
      T upEnd = this.getUpperEndpoint();
      BoundType upType = this.getUpperBoundType();
      int cmp;
      if (!this.hasUpperBound()) {
         hasUpBound = other.hasUpperBound;
         upEnd = other.getUpperEndpoint();
         upType = other.getUpperBoundType();
      } else if (other.hasUpperBound()) {
         cmp = this.comparator.compare(this.getUpperEndpoint(), other.getUpperEndpoint());
         if (cmp > 0 || cmp == 0 && other.getUpperBoundType() == BoundType.OPEN) {
            upEnd = other.getUpperEndpoint();
            upType = other.getUpperBoundType();
         }
      }

      if (hasLowBound && hasUpBound) {
         cmp = this.comparator.compare(lowEnd, upEnd);
         if (cmp > 0 || cmp == 0 && lowType == BoundType.OPEN && upType == BoundType.OPEN) {
            lowEnd = upEnd;
            lowType = BoundType.OPEN;
            upType = BoundType.CLOSED;
         }
      }

      return new GeneralRange(this.comparator, hasLowBound, lowEnd, lowType, hasUpBound, upEnd, upType);
   }

   public boolean equals(@CheckForNull Object obj) {
      if (!(obj instanceof GeneralRange)) {
         return false;
      } else {
         GeneralRange<?> r = (GeneralRange)obj;
         return this.comparator.equals(r.comparator) && this.hasLowerBound == r.hasLowerBound && this.hasUpperBound == r.hasUpperBound && this.getLowerBoundType().equals(r.getLowerBoundType()) && this.getUpperBoundType().equals(r.getUpperBoundType()) && Objects.equal(this.getLowerEndpoint(), r.getLowerEndpoint()) && Objects.equal(this.getUpperEndpoint(), r.getUpperEndpoint());
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.comparator, this.getLowerEndpoint(), this.getLowerBoundType(), this.getUpperEndpoint(), this.getUpperBoundType());
   }

   GeneralRange<T> reverse() {
      GeneralRange<T> result = this.reverse;
      if (result == null) {
         result = new GeneralRange(Ordering.from(this.comparator).reverse(), this.hasUpperBound, this.getUpperEndpoint(), this.getUpperBoundType(), this.hasLowerBound, this.getLowerEndpoint(), this.getLowerBoundType());
         result.reverse = this;
         return this.reverse = result;
      } else {
         return result;
      }
   }

   public String toString() {
      String var1 = String.valueOf(this.comparator);
      int var2 = this.lowerBoundType == BoundType.CLOSED ? 91 : 40;
      String var3 = String.valueOf(this.hasLowerBound ? this.lowerEndpoint : "-∞");
      String var4 = String.valueOf(this.hasUpperBound ? this.upperEndpoint : "∞");
      int var5 = this.upperBoundType == BoundType.CLOSED ? 93 : 41;
      return (new StringBuilder(4 + String.valueOf(var1).length() + String.valueOf(var3).length() + String.valueOf(var4).length())).append(var1).append(":").append((char)var2).append(var3).append(',').append(var4).append((char)var5).toString();
   }

   @CheckForNull
   T getLowerEndpoint() {
      return this.lowerEndpoint;
   }

   BoundType getLowerBoundType() {
      return this.lowerBoundType;
   }

   @CheckForNull
   T getUpperEndpoint() {
      return this.upperEndpoint;
   }

   BoundType getUpperBoundType() {
      return this.upperBoundType;
   }
}
