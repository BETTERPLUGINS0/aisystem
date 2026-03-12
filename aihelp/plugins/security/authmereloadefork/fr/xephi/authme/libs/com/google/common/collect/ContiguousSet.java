package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotCall;
import java.util.NoSuchElementException;
import java.util.Objects;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
public abstract class ContiguousSet<C extends Comparable> extends ImmutableSortedSet<C> {
   final DiscreteDomain<C> domain;

   public static <C extends Comparable> ContiguousSet<C> create(Range<C> range, DiscreteDomain<C> domain) {
      Preconditions.checkNotNull(range);
      Preconditions.checkNotNull(domain);
      Range effectiveRange = range;

      try {
         if (!range.hasLowerBound()) {
            effectiveRange = effectiveRange.intersection(Range.atLeast(domain.minValue()));
         }

         if (!range.hasUpperBound()) {
            effectiveRange = effectiveRange.intersection(Range.atMost(domain.maxValue()));
         }
      } catch (NoSuchElementException var6) {
         throw new IllegalArgumentException(var6);
      }

      boolean empty;
      if (effectiveRange.isEmpty()) {
         empty = true;
      } else {
         C afterLower = (Comparable)Objects.requireNonNull(range.lowerBound.leastValueAbove(domain));
         C beforeUpper = (Comparable)Objects.requireNonNull(range.upperBound.greatestValueBelow(domain));
         empty = Range.compareOrThrow(afterLower, beforeUpper) > 0;
      }

      return (ContiguousSet)(empty ? new EmptyContiguousSet(domain) : new RegularContiguousSet(effectiveRange, domain));
   }

   @Beta
   public static ContiguousSet<Integer> closed(int lower, int upper) {
      return create(Range.closed(lower, upper), DiscreteDomain.integers());
   }

   @Beta
   public static ContiguousSet<Long> closed(long lower, long upper) {
      return create(Range.closed(lower, upper), DiscreteDomain.longs());
   }

   @Beta
   public static ContiguousSet<Integer> closedOpen(int lower, int upper) {
      return create(Range.closedOpen(lower, upper), DiscreteDomain.integers());
   }

   @Beta
   public static ContiguousSet<Long> closedOpen(long lower, long upper) {
      return create(Range.closedOpen(lower, upper), DiscreteDomain.longs());
   }

   ContiguousSet(DiscreteDomain<C> domain) {
      super(Ordering.natural());
      this.domain = domain;
   }

   public ContiguousSet<C> headSet(C toElement) {
      return this.headSetImpl((Comparable)Preconditions.checkNotNull(toElement), false);
   }

   @GwtIncompatible
   public ContiguousSet<C> headSet(C toElement, boolean inclusive) {
      return this.headSetImpl((Comparable)Preconditions.checkNotNull(toElement), inclusive);
   }

   public ContiguousSet<C> subSet(C fromElement, C toElement) {
      Preconditions.checkNotNull(fromElement);
      Preconditions.checkNotNull(toElement);
      Preconditions.checkArgument(this.comparator().compare(fromElement, toElement) <= 0);
      return this.subSetImpl(fromElement, true, toElement, false);
   }

   @GwtIncompatible
   public ContiguousSet<C> subSet(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
      Preconditions.checkNotNull(fromElement);
      Preconditions.checkNotNull(toElement);
      Preconditions.checkArgument(this.comparator().compare(fromElement, toElement) <= 0);
      return this.subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
   }

   public ContiguousSet<C> tailSet(C fromElement) {
      return this.tailSetImpl((Comparable)Preconditions.checkNotNull(fromElement), true);
   }

   @GwtIncompatible
   public ContiguousSet<C> tailSet(C fromElement, boolean inclusive) {
      return this.tailSetImpl((Comparable)Preconditions.checkNotNull(fromElement), inclusive);
   }

   abstract ContiguousSet<C> headSetImpl(C var1, boolean var2);

   abstract ContiguousSet<C> subSetImpl(C var1, boolean var2, C var3, boolean var4);

   abstract ContiguousSet<C> tailSetImpl(C var1, boolean var2);

   public abstract ContiguousSet<C> intersection(ContiguousSet<C> var1);

   public abstract Range<C> range();

   public abstract Range<C> range(BoundType var1, BoundType var2);

   @GwtIncompatible
   ImmutableSortedSet<C> createDescendingSet() {
      return new DescendingImmutableSortedSet(this);
   }

   public String toString() {
      return this.range().toString();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public static <E> ImmutableSortedSet.Builder<E> builder() {
      throw new UnsupportedOperationException();
   }
}
