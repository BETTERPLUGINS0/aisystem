package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
abstract class AbstractRangeSet<C extends Comparable> implements RangeSet<C> {
   public boolean contains(C value) {
      return this.rangeContaining(value) != null;
   }

   @CheckForNull
   public abstract Range<C> rangeContaining(C var1);

   public boolean isEmpty() {
      return this.asRanges().isEmpty();
   }

   public void add(Range<C> range) {
      throw new UnsupportedOperationException();
   }

   public void remove(Range<C> range) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      this.remove(Range.all());
   }

   public boolean enclosesAll(RangeSet<C> other) {
      return this.enclosesAll(other.asRanges());
   }

   public void addAll(RangeSet<C> other) {
      this.addAll(other.asRanges());
   }

   public void removeAll(RangeSet<C> other) {
      this.removeAll(other.asRanges());
   }

   public boolean intersects(Range<C> otherRange) {
      return !this.subRangeSet(otherRange).isEmpty();
   }

   public abstract boolean encloses(Range<C> var1);

   public boolean equals(@CheckForNull Object obj) {
      if (obj == this) {
         return true;
      } else if (obj instanceof RangeSet) {
         RangeSet<?> other = (RangeSet)obj;
         return this.asRanges().equals(other.asRanges());
      } else {
         return false;
      }
   }

   public final int hashCode() {
      return this.asRanges().hashCode();
   }

   public final String toString() {
      return this.asRanges().toString();
   }
}
