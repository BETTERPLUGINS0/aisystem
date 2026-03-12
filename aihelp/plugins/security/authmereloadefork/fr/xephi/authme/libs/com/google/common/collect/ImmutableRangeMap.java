package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotCall;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public class ImmutableRangeMap<K extends Comparable<?>, V> implements RangeMap<K, V>, Serializable {
   private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY = new ImmutableRangeMap(ImmutableList.of(), ImmutableList.of());
   private final transient ImmutableList<Range<K>> ranges;
   private final transient ImmutableList<V> values;
   private static final long serialVersionUID = 0L;

   public static <T, K extends Comparable<? super K>, V> Collector<T, ?, ImmutableRangeMap<K, V>> toImmutableRangeMap(Function<? super T, Range<K>> keyFunction, Function<? super T, ? extends V> valueFunction) {
      return CollectCollectors.toImmutableRangeMap(keyFunction, valueFunction);
   }

   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of() {
      return EMPTY;
   }

   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of(Range<K> range, V value) {
      return new ImmutableRangeMap(ImmutableList.of(range), ImmutableList.of(value));
   }

   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(RangeMap<K, ? extends V> rangeMap) {
      if (rangeMap instanceof ImmutableRangeMap) {
         return (ImmutableRangeMap)rangeMap;
      } else {
         Map<Range<K>, ? extends V> map = rangeMap.asMapOfRanges();
         ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder(map.size());
         ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder(map.size());
         Iterator var4 = map.entrySet().iterator();

         while(var4.hasNext()) {
            Entry<Range<K>, ? extends V> entry = (Entry)var4.next();
            rangesBuilder.add((Object)((Range)entry.getKey()));
            valuesBuilder.add(entry.getValue());
         }

         return new ImmutableRangeMap(rangesBuilder.build(), valuesBuilder.build());
      }
   }

   public static <K extends Comparable<?>, V> ImmutableRangeMap.Builder<K, V> builder() {
      return new ImmutableRangeMap.Builder();
   }

   ImmutableRangeMap(ImmutableList<Range<K>> ranges, ImmutableList<V> values) {
      this.ranges = ranges;
      this.values = values;
   }

   @CheckForNull
   public V get(K key) {
      int index = SortedLists.binarySearch(this.ranges, (fr.xephi.authme.libs.com.google.common.base.Function)Range.lowerBoundFn(), (Comparable)Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
      if (index == -1) {
         return null;
      } else {
         Range<K> range = (Range)this.ranges.get(index);
         return range.contains(key) ? this.values.get(index) : null;
      }
   }

   @CheckForNull
   public Entry<Range<K>, V> getEntry(K key) {
      int index = SortedLists.binarySearch(this.ranges, (fr.xephi.authme.libs.com.google.common.base.Function)Range.lowerBoundFn(), (Comparable)Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
      if (index == -1) {
         return null;
      } else {
         Range<K> range = (Range)this.ranges.get(index);
         return range.contains(key) ? Maps.immutableEntry(range, this.values.get(index)) : null;
      }
   }

   public Range<K> span() {
      if (this.ranges.isEmpty()) {
         throw new NoSuchElementException();
      } else {
         Range<K> firstRange = (Range)this.ranges.get(0);
         Range<K> lastRange = (Range)this.ranges.get(this.ranges.size() - 1);
         return Range.create(firstRange.lowerBound, lastRange.upperBound);
      }
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final void put(Range<K> range, V value) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final void putCoalescing(Range<K> range, V value) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final void putAll(RangeMap<K, V> rangeMap) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final void clear() {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final void remove(Range<K> range) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final void merge(Range<K> range, @CheckForNull V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
      throw new UnsupportedOperationException();
   }

   public ImmutableMap<Range<K>, V> asMapOfRanges() {
      if (this.ranges.isEmpty()) {
         return ImmutableMap.of();
      } else {
         RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet(this.ranges, Range.rangeLexOrdering());
         return new ImmutableSortedMap(rangeSet, this.values);
      }
   }

   public ImmutableMap<Range<K>, V> asDescendingMapOfRanges() {
      if (this.ranges.isEmpty()) {
         return ImmutableMap.of();
      } else {
         RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet(this.ranges.reverse(), Range.rangeLexOrdering().reverse());
         return new ImmutableSortedMap(rangeSet, this.values.reverse());
      }
   }

   public ImmutableRangeMap<K, V> subRangeMap(final Range<K> range) {
      if (((Range)Preconditions.checkNotNull(range)).isEmpty()) {
         return of();
      } else if (!this.ranges.isEmpty() && !range.encloses(this.span())) {
         final int lowerIndex = SortedLists.binarySearch(this.ranges, (fr.xephi.authme.libs.com.google.common.base.Function)Range.upperBoundFn(), (Comparable)range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
         int upperIndex = SortedLists.binarySearch(this.ranges, (fr.xephi.authme.libs.com.google.common.base.Function)Range.lowerBoundFn(), (Comparable)range.upperBound, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
         if (lowerIndex >= upperIndex) {
            return of();
         } else {
            final int len = upperIndex - lowerIndex;
            ImmutableList<Range<K>> subRanges = new ImmutableList<Range<K>>() {
               public int size() {
                  return len;
               }

               public Range<K> get(int index) {
                  Preconditions.checkElementIndex(index, len);
                  return index != 0 && index != len - 1 ? (Range)ImmutableRangeMap.this.ranges.get(index + lowerIndex) : ((Range)ImmutableRangeMap.this.ranges.get(index + lowerIndex)).intersection(range);
               }

               boolean isPartialView() {
                  return true;
               }
            };
            return new ImmutableRangeMap<K, V>(this, subRanges, this.values.subList(lowerIndex, upperIndex)) {
               public ImmutableRangeMap<K, V> subRangeMap(Range<K> subRange) {
                  return range.isConnected(subRange) ? ImmutableRangeMap.this.subRangeMap(subRange.intersection(range)) : ImmutableRangeMap.of();
               }
            };
         }
      } else {
         return this;
      }
   }

   public int hashCode() {
      return this.asMapOfRanges().hashCode();
   }

   public boolean equals(@CheckForNull Object o) {
      if (o instanceof RangeMap) {
         RangeMap<?, ?> rangeMap = (RangeMap)o;
         return this.asMapOfRanges().equals(rangeMap.asMapOfRanges());
      } else {
         return false;
      }
   }

   public String toString() {
      return this.asMapOfRanges().toString();
   }

   Object writeReplace() {
      return new ImmutableRangeMap.SerializedForm(this.asMapOfRanges());
   }

   private static class SerializedForm<K extends Comparable<?>, V> implements Serializable {
      private final ImmutableMap<Range<K>, V> mapOfRanges;
      private static final long serialVersionUID = 0L;

      SerializedForm(ImmutableMap<Range<K>, V> mapOfRanges) {
         this.mapOfRanges = mapOfRanges;
      }

      Object readResolve() {
         return this.mapOfRanges.isEmpty() ? ImmutableRangeMap.of() : this.createRangeMap();
      }

      Object createRangeMap() {
         ImmutableRangeMap.Builder<K, V> builder = new ImmutableRangeMap.Builder();
         UnmodifiableIterator var2 = this.mapOfRanges.entrySet().iterator();

         while(var2.hasNext()) {
            Entry<Range<K>, V> entry = (Entry)var2.next();
            builder.put((Range)entry.getKey(), entry.getValue());
         }

         return builder.build();
      }
   }

   @DoNotMock
   public static final class Builder<K extends Comparable<?>, V> {
      private final List<Entry<Range<K>, V>> entries = Lists.newArrayList();

      @CanIgnoreReturnValue
      public ImmutableRangeMap.Builder<K, V> put(Range<K> range, V value) {
         Preconditions.checkNotNull(range);
         Preconditions.checkNotNull(value);
         Preconditions.checkArgument(!range.isEmpty(), "Range must not be empty, but was %s", (Object)range);
         this.entries.add(Maps.immutableEntry(range, value));
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableRangeMap.Builder<K, V> putAll(RangeMap<K, ? extends V> rangeMap) {
         Iterator var2 = rangeMap.asMapOfRanges().entrySet().iterator();

         while(var2.hasNext()) {
            Entry<Range<K>, ? extends V> entry = (Entry)var2.next();
            this.put((Range)entry.getKey(), entry.getValue());
         }

         return this;
      }

      @CanIgnoreReturnValue
      ImmutableRangeMap.Builder<K, V> combine(ImmutableRangeMap.Builder<K, V> builder) {
         this.entries.addAll(builder.entries);
         return this;
      }

      public ImmutableRangeMap<K, V> build() {
         Collections.sort(this.entries, Range.rangeLexOrdering().onKeys());
         ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder(this.entries.size());
         ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder(this.entries.size());

         for(int i = 0; i < this.entries.size(); ++i) {
            Range<K> range = (Range)((Entry)this.entries.get(i)).getKey();
            if (i > 0) {
               Range<K> prevRange = (Range)((Entry)this.entries.get(i - 1)).getKey();
               if (range.isConnected(prevRange) && !range.intersection(prevRange).isEmpty()) {
                  String var6 = String.valueOf(prevRange);
                  String var7 = String.valueOf(range);
                  throw new IllegalArgumentException((new StringBuilder(47 + String.valueOf(var6).length() + String.valueOf(var7).length())).append("Overlapping ranges: range ").append(var6).append(" overlaps with entry ").append(var7).toString());
               }
            }

            rangesBuilder.add((Object)range);
            valuesBuilder.add(((Entry)this.entries.get(i)).getValue());
         }

         return new ImmutableRangeMap(rangesBuilder.build(), valuesBuilder.build());
      }
   }
}
