package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Function;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.primitives.Ints;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotCall;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class ImmutableRangeSet<C extends Comparable> extends AbstractRangeSet<C> implements Serializable {
   private static final ImmutableRangeSet<Comparable<?>> EMPTY = new ImmutableRangeSet(ImmutableList.of());
   private static final ImmutableRangeSet<Comparable<?>> ALL = new ImmutableRangeSet(ImmutableList.of(Range.all()));
   private final transient ImmutableList<Range<C>> ranges;
   @LazyInit
   @CheckForNull
   private transient ImmutableRangeSet<C> complement;

   public static <E extends Comparable<? super E>> Collector<Range<E>, ?, ImmutableRangeSet<E>> toImmutableRangeSet() {
      return CollectCollectors.toImmutableRangeSet();
   }

   public static <C extends Comparable> ImmutableRangeSet<C> of() {
      return EMPTY;
   }

   public static <C extends Comparable> ImmutableRangeSet<C> of(Range<C> range) {
      Preconditions.checkNotNull(range);
      if (range.isEmpty()) {
         return of();
      } else {
         return range.equals(Range.all()) ? all() : new ImmutableRangeSet(ImmutableList.of(range));
      }
   }

   static <C extends Comparable> ImmutableRangeSet<C> all() {
      return ALL;
   }

   public static <C extends Comparable> ImmutableRangeSet<C> copyOf(RangeSet<C> rangeSet) {
      Preconditions.checkNotNull(rangeSet);
      if (rangeSet.isEmpty()) {
         return of();
      } else if (rangeSet.encloses(Range.all())) {
         return all();
      } else {
         if (rangeSet instanceof ImmutableRangeSet) {
            ImmutableRangeSet<C> immutableRangeSet = (ImmutableRangeSet)rangeSet;
            if (!immutableRangeSet.isPartialView()) {
               return immutableRangeSet;
            }
         }

         return new ImmutableRangeSet(ImmutableList.copyOf((Collection)rangeSet.asRanges()));
      }
   }

   public static <C extends Comparable<?>> ImmutableRangeSet<C> copyOf(Iterable<Range<C>> ranges) {
      return (new ImmutableRangeSet.Builder()).addAll(ranges).build();
   }

   public static <C extends Comparable<?>> ImmutableRangeSet<C> unionOf(Iterable<Range<C>> ranges) {
      return copyOf((RangeSet)TreeRangeSet.create(ranges));
   }

   ImmutableRangeSet(ImmutableList<Range<C>> ranges) {
      this.ranges = ranges;
   }

   private ImmutableRangeSet(ImmutableList<Range<C>> ranges, ImmutableRangeSet<C> complement) {
      this.ranges = ranges;
      this.complement = complement;
   }

   public boolean intersects(Range<C> otherRange) {
      int ceilingIndex = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), otherRange.lowerBound, Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
      if (ceilingIndex < this.ranges.size() && ((Range)this.ranges.get(ceilingIndex)).isConnected(otherRange) && !((Range)this.ranges.get(ceilingIndex)).intersection(otherRange).isEmpty()) {
         return true;
      } else {
         return ceilingIndex > 0 && ((Range)this.ranges.get(ceilingIndex - 1)).isConnected(otherRange) && !((Range)this.ranges.get(ceilingIndex - 1)).intersection(otherRange).isEmpty();
      }
   }

   public boolean encloses(Range<C> otherRange) {
      int index = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), otherRange.lowerBound, Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
      return index != -1 && ((Range)this.ranges.get(index)).encloses(otherRange);
   }

   @CheckForNull
   public Range<C> rangeContaining(C value) {
      int index = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), Cut.belowValue(value), Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
      if (index != -1) {
         Range<C> range = (Range)this.ranges.get(index);
         return range.contains(value) ? range : null;
      } else {
         return null;
      }
   }

   public Range<C> span() {
      if (this.ranges.isEmpty()) {
         throw new NoSuchElementException();
      } else {
         return Range.create(((Range)this.ranges.get(0)).lowerBound, ((Range)this.ranges.get(this.ranges.size() - 1)).upperBound);
      }
   }

   public boolean isEmpty() {
      return this.ranges.isEmpty();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public void add(Range<C> range) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public void addAll(RangeSet<C> other) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public void addAll(Iterable<Range<C>> other) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public void remove(Range<C> range) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public void removeAll(RangeSet<C> other) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public void removeAll(Iterable<Range<C>> other) {
      throw new UnsupportedOperationException();
   }

   public ImmutableSet<Range<C>> asRanges() {
      return (ImmutableSet)(this.ranges.isEmpty() ? ImmutableSet.of() : new RegularImmutableSortedSet(this.ranges, Range.rangeLexOrdering()));
   }

   public ImmutableSet<Range<C>> asDescendingSetOfRanges() {
      return (ImmutableSet)(this.ranges.isEmpty() ? ImmutableSet.of() : new RegularImmutableSortedSet(this.ranges.reverse(), Range.rangeLexOrdering().reverse()));
   }

   public ImmutableRangeSet<C> complement() {
      ImmutableRangeSet<C> result = this.complement;
      if (result != null) {
         return result;
      } else if (this.ranges.isEmpty()) {
         return this.complement = all();
      } else if (this.ranges.size() == 1 && ((Range)this.ranges.get(0)).equals(Range.all())) {
         return this.complement = of();
      } else {
         ImmutableList<Range<C>> complementRanges = new ImmutableRangeSet.ComplementRanges();
         result = this.complement = new ImmutableRangeSet(complementRanges, this);
         return result;
      }
   }

   public ImmutableRangeSet<C> union(RangeSet<C> other) {
      return unionOf(Iterables.concat(this.asRanges(), other.asRanges()));
   }

   public ImmutableRangeSet<C> intersection(RangeSet<C> other) {
      RangeSet<C> copy = TreeRangeSet.create((RangeSet)this);
      copy.removeAll(other.complement());
      return copyOf((RangeSet)copy);
   }

   public ImmutableRangeSet<C> difference(RangeSet<C> other) {
      RangeSet<C> copy = TreeRangeSet.create((RangeSet)this);
      copy.removeAll(other);
      return copyOf((RangeSet)copy);
   }

   private ImmutableList<Range<C>> intersectRanges(final Range<C> range) {
      if (!this.ranges.isEmpty() && !range.isEmpty()) {
         if (range.encloses(this.span())) {
            return this.ranges;
         } else {
            final int fromIndex;
            if (range.hasLowerBound()) {
               fromIndex = SortedLists.binarySearch(this.ranges, (Function)Range.upperBoundFn(), (Comparable)range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
            } else {
               fromIndex = 0;
            }

            int toIndex;
            if (range.hasUpperBound()) {
               toIndex = SortedLists.binarySearch(this.ranges, (Function)Range.lowerBoundFn(), (Comparable)range.upperBound, SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
            } else {
               toIndex = this.ranges.size();
            }

            final int length = toIndex - fromIndex;
            return length == 0 ? ImmutableList.of() : new ImmutableList<Range<C>>() {
               public int size() {
                  return length;
               }

               public Range<C> get(int index) {
                  Preconditions.checkElementIndex(index, length);
                  return index != 0 && index != length - 1 ? (Range)ImmutableRangeSet.this.ranges.get(index + fromIndex) : ((Range)ImmutableRangeSet.this.ranges.get(index + fromIndex)).intersection(range);
               }

               boolean isPartialView() {
                  return true;
               }
            };
         }
      } else {
         return ImmutableList.of();
      }
   }

   public ImmutableRangeSet<C> subRangeSet(Range<C> range) {
      if (!this.isEmpty()) {
         Range<C> span = this.span();
         if (range.encloses(span)) {
            return this;
         }

         if (range.isConnected(span)) {
            return new ImmutableRangeSet(this.intersectRanges(range));
         }
      }

      return of();
   }

   public ImmutableSortedSet<C> asSet(DiscreteDomain<C> domain) {
      Preconditions.checkNotNull(domain);
      if (this.isEmpty()) {
         return ImmutableSortedSet.of();
      } else {
         Range<C> span = this.span().canonical(domain);
         if (!span.hasLowerBound()) {
            throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded below");
         } else {
            if (!span.hasUpperBound()) {
               try {
                  domain.maxValue();
               } catch (NoSuchElementException var4) {
                  throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded above");
               }
            }

            return new ImmutableRangeSet.AsSet(domain);
         }
      }
   }

   boolean isPartialView() {
      return this.ranges.isPartialView();
   }

   public static <C extends Comparable<?>> ImmutableRangeSet.Builder<C> builder() {
      return new ImmutableRangeSet.Builder();
   }

   Object writeReplace() {
      return new ImmutableRangeSet.SerializedForm(this.ranges);
   }

   private static final class SerializedForm<C extends Comparable> implements Serializable {
      private final ImmutableList<Range<C>> ranges;

      SerializedForm(ImmutableList<Range<C>> ranges) {
         this.ranges = ranges;
      }

      Object readResolve() {
         if (this.ranges.isEmpty()) {
            return ImmutableRangeSet.of();
         } else {
            return this.ranges.equals(ImmutableList.of(Range.all())) ? ImmutableRangeSet.all() : new ImmutableRangeSet(this.ranges);
         }
      }
   }

   public static class Builder<C extends Comparable<?>> {
      private final List<Range<C>> ranges = Lists.newArrayList();

      @CanIgnoreReturnValue
      public ImmutableRangeSet.Builder<C> add(Range<C> range) {
         Preconditions.checkArgument(!range.isEmpty(), "range must not be empty, but was %s", (Object)range);
         this.ranges.add(range);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableRangeSet.Builder<C> addAll(RangeSet<C> ranges) {
         return this.addAll((Iterable)ranges.asRanges());
      }

      @CanIgnoreReturnValue
      public ImmutableRangeSet.Builder<C> addAll(Iterable<Range<C>> ranges) {
         Iterator var2 = ranges.iterator();

         while(var2.hasNext()) {
            Range<C> range = (Range)var2.next();
            this.add(range);
         }

         return this;
      }

      @CanIgnoreReturnValue
      ImmutableRangeSet.Builder<C> combine(ImmutableRangeSet.Builder<C> builder) {
         this.addAll((Iterable)builder.ranges);
         return this;
      }

      public ImmutableRangeSet<C> build() {
         ImmutableList.Builder<Range<C>> mergedRangesBuilder = new ImmutableList.Builder(this.ranges.size());
         Collections.sort(this.ranges, Range.rangeLexOrdering());

         Range range;
         for(PeekingIterator peekingItr = Iterators.peekingIterator(this.ranges.iterator()); peekingItr.hasNext(); mergedRangesBuilder.add((Object)range)) {
            for(range = (Range)peekingItr.next(); peekingItr.hasNext(); range = range.span((Range)peekingItr.next())) {
               Range<C> nextRange = (Range)peekingItr.peek();
               if (!range.isConnected(nextRange)) {
                  break;
               }

               Preconditions.checkArgument(range.intersection(nextRange).isEmpty(), "Overlapping ranges not permitted but found %s overlapping %s", range, nextRange);
            }
         }

         ImmutableList<Range<C>> mergedRanges = mergedRangesBuilder.build();
         if (mergedRanges.isEmpty()) {
            return ImmutableRangeSet.of();
         } else if (mergedRanges.size() == 1 && ((Range)Iterables.getOnlyElement(mergedRanges)).equals(Range.all())) {
            return ImmutableRangeSet.all();
         } else {
            return new ImmutableRangeSet(mergedRanges);
         }
      }
   }

   private static class AsSetSerializedForm<C extends Comparable> implements Serializable {
      private final ImmutableList<Range<C>> ranges;
      private final DiscreteDomain<C> domain;

      AsSetSerializedForm(ImmutableList<Range<C>> ranges, DiscreteDomain<C> domain) {
         this.ranges = ranges;
         this.domain = domain;
      }

      Object readResolve() {
         return (new ImmutableRangeSet(this.ranges)).asSet(this.domain);
      }
   }

   private final class AsSet extends ImmutableSortedSet<C> {
      private final DiscreteDomain<C> domain;
      @CheckForNull
      private transient Integer size;

      AsSet(DiscreteDomain<C> domain) {
         super(Ordering.natural());
         this.domain = domain;
      }

      public int size() {
         Integer result = this.size;
         if (result == null) {
            long total = 0L;
            UnmodifiableIterator var4 = ImmutableRangeSet.this.ranges.iterator();

            while(var4.hasNext()) {
               Range<C> range = (Range)var4.next();
               total += (long)ContiguousSet.create(range, this.domain).size();
               if (total >= 2147483647L) {
                  break;
               }
            }

            result = this.size = Ints.saturatedCast(total);
         }

         return result;
      }

      public UnmodifiableIterator<C> iterator() {
         return new AbstractIterator<C>() {
            final Iterator<Range<C>> rangeItr;
            Iterator<C> elemItr;

            {
               this.rangeItr = ImmutableRangeSet.this.ranges.iterator();
               this.elemItr = Iterators.emptyIterator();
            }

            @CheckForNull
            protected C computeNext() {
               while(true) {
                  if (!this.elemItr.hasNext()) {
                     if (this.rangeItr.hasNext()) {
                        this.elemItr = ContiguousSet.create((Range)this.rangeItr.next(), AsSet.this.domain).iterator();
                        continue;
                     }

                     return (Comparable)this.endOfData();
                  }

                  return (Comparable)this.elemItr.next();
               }
            }
         };
      }

      @GwtIncompatible("NavigableSet")
      public UnmodifiableIterator<C> descendingIterator() {
         return new AbstractIterator<C>() {
            final Iterator<Range<C>> rangeItr;
            Iterator<C> elemItr;

            {
               this.rangeItr = ImmutableRangeSet.this.ranges.reverse().iterator();
               this.elemItr = Iterators.emptyIterator();
            }

            @CheckForNull
            protected C computeNext() {
               while(true) {
                  if (!this.elemItr.hasNext()) {
                     if (this.rangeItr.hasNext()) {
                        this.elemItr = ContiguousSet.create((Range)this.rangeItr.next(), AsSet.this.domain).descendingIterator();
                        continue;
                     }

                     return (Comparable)this.endOfData();
                  }

                  return (Comparable)this.elemItr.next();
               }
            }
         };
      }

      ImmutableSortedSet<C> subSet(Range<C> range) {
         return ImmutableRangeSet.this.subRangeSet(range).asSet(this.domain);
      }

      ImmutableSortedSet<C> headSetImpl(C toElement, boolean inclusive) {
         return this.subSet(Range.upTo(toElement, BoundType.forBoolean(inclusive)));
      }

      ImmutableSortedSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
         return !fromInclusive && !toInclusive && Range.compareOrThrow(fromElement, toElement) == 0 ? ImmutableSortedSet.of() : this.subSet(Range.range(fromElement, BoundType.forBoolean(fromInclusive), toElement, BoundType.forBoolean(toInclusive)));
      }

      ImmutableSortedSet<C> tailSetImpl(C fromElement, boolean inclusive) {
         return this.subSet(Range.downTo(fromElement, BoundType.forBoolean(inclusive)));
      }

      public boolean contains(@CheckForNull Object o) {
         if (o == null) {
            return false;
         } else {
            try {
               C c = (Comparable)o;
               return ImmutableRangeSet.this.contains(c);
            } catch (ClassCastException var3) {
               return false;
            }
         }
      }

      int indexOf(@CheckForNull Object target) {
         if (this.contains(target)) {
            C c = (Comparable)Objects.requireNonNull(target);
            long total = 0L;

            Range range;
            for(UnmodifiableIterator var5 = ImmutableRangeSet.this.ranges.iterator(); var5.hasNext(); total += (long)ContiguousSet.create(range, this.domain).size()) {
               range = (Range)var5.next();
               if (range.contains(c)) {
                  return Ints.saturatedCast(total + (long)ContiguousSet.create(range, this.domain).indexOf(c));
               }
            }

            throw new AssertionError("impossible");
         } else {
            return -1;
         }
      }

      ImmutableSortedSet<C> createDescendingSet() {
         return new DescendingImmutableSortedSet(this);
      }

      boolean isPartialView() {
         return ImmutableRangeSet.this.ranges.isPartialView();
      }

      public String toString() {
         return ImmutableRangeSet.this.ranges.toString();
      }

      Object writeReplace() {
         return new ImmutableRangeSet.AsSetSerializedForm(ImmutableRangeSet.this.ranges, this.domain);
      }
   }

   private final class ComplementRanges extends ImmutableList<Range<C>> {
      private final boolean positiveBoundedBelow;
      private final boolean positiveBoundedAbove;
      private final int size;

      ComplementRanges() {
         this.positiveBoundedBelow = ((Range)ImmutableRangeSet.this.ranges.get(0)).hasLowerBound();
         this.positiveBoundedAbove = ((Range)Iterables.getLast(ImmutableRangeSet.this.ranges)).hasUpperBound();
         int size = ImmutableRangeSet.this.ranges.size() - 1;
         if (this.positiveBoundedBelow) {
            ++size;
         }

         if (this.positiveBoundedAbove) {
            ++size;
         }

         this.size = size;
      }

      public int size() {
         return this.size;
      }

      public Range<C> get(int index) {
         Preconditions.checkElementIndex(index, this.size);
         Cut lowerBound;
         if (this.positiveBoundedBelow) {
            lowerBound = index == 0 ? Cut.belowAll() : ((Range)ImmutableRangeSet.this.ranges.get(index - 1)).upperBound;
         } else {
            lowerBound = ((Range)ImmutableRangeSet.this.ranges.get(index)).upperBound;
         }

         Cut upperBound;
         if (this.positiveBoundedAbove && index == this.size - 1) {
            upperBound = Cut.aboveAll();
         } else {
            upperBound = ((Range)ImmutableRangeSet.this.ranges.get(index + (this.positiveBoundedBelow ? 0 : 1))).lowerBound;
         }

         return Range.create(lowerBound, upperBound);
      }

      boolean isPartialView() {
         return true;
      }
   }
}
