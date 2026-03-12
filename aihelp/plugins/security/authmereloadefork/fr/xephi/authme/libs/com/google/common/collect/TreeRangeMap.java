package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.MoreObjects;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.base.Predicate;
import fr.xephi.authme.libs.com.google.common.base.Predicates;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class TreeRangeMap<K extends Comparable, V> implements RangeMap<K, V> {
   private final NavigableMap<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> entriesByLowerBound = Maps.newTreeMap();
   private static final RangeMap<Comparable<?>, Object> EMPTY_SUB_RANGE_MAP = new RangeMap<Comparable<?>, Object>() {
      @CheckForNull
      public Object get(Comparable<?> key) {
         return null;
      }

      @CheckForNull
      public Entry<Range<Comparable<?>>, Object> getEntry(Comparable<?> key) {
         return null;
      }

      public Range<Comparable<?>> span() {
         throw new NoSuchElementException();
      }

      public void put(Range<Comparable<?>> range, Object value) {
         Preconditions.checkNotNull(range);
         String var3 = String.valueOf(range);
         throw new IllegalArgumentException((new StringBuilder(46 + String.valueOf(var3).length())).append("Cannot insert range ").append(var3).append(" into an empty subRangeMap").toString());
      }

      public void putCoalescing(Range<Comparable<?>> range, Object value) {
         Preconditions.checkNotNull(range);
         String var3 = String.valueOf(range);
         throw new IllegalArgumentException((new StringBuilder(46 + String.valueOf(var3).length())).append("Cannot insert range ").append(var3).append(" into an empty subRangeMap").toString());
      }

      public void putAll(RangeMap<Comparable<?>, Object> rangeMap) {
         if (!rangeMap.asMapOfRanges().isEmpty()) {
            throw new IllegalArgumentException("Cannot putAll(nonEmptyRangeMap) into an empty subRangeMap");
         }
      }

      public void clear() {
      }

      public void remove(Range<Comparable<?>> range) {
         Preconditions.checkNotNull(range);
      }

      public void merge(Range<Comparable<?>> range, @CheckForNull Object value, BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
         Preconditions.checkNotNull(range);
         String var4 = String.valueOf(range);
         throw new IllegalArgumentException((new StringBuilder(45 + String.valueOf(var4).length())).append("Cannot merge range ").append(var4).append(" into an empty subRangeMap").toString());
      }

      public Map<Range<Comparable<?>>, Object> asMapOfRanges() {
         return Collections.emptyMap();
      }

      public Map<Range<Comparable<?>>, Object> asDescendingMapOfRanges() {
         return Collections.emptyMap();
      }

      public RangeMap<Comparable<?>, Object> subRangeMap(Range<Comparable<?>> range) {
         Preconditions.checkNotNull(range);
         return this;
      }
   };

   public static <K extends Comparable, V> TreeRangeMap<K, V> create() {
      return new TreeRangeMap();
   }

   private TreeRangeMap() {
   }

   @CheckForNull
   public V get(K key) {
      Entry<Range<K>, V> entry = this.getEntry(key);
      return entry == null ? null : entry.getValue();
   }

   @CheckForNull
   public Entry<Range<K>, V> getEntry(K key) {
      Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> mapEntry = this.entriesByLowerBound.floorEntry(Cut.belowValue(key));
      return mapEntry != null && ((TreeRangeMap.RangeMapEntry)mapEntry.getValue()).contains(key) ? (Entry)mapEntry.getValue() : null;
   }

   public void put(Range<K> range, V value) {
      if (!range.isEmpty()) {
         Preconditions.checkNotNull(value);
         this.remove(range);
         this.entriesByLowerBound.put(range.lowerBound, new TreeRangeMap.RangeMapEntry(range, value));
      }

   }

   public void putCoalescing(Range<K> range, V value) {
      if (this.entriesByLowerBound.isEmpty()) {
         this.put(range, value);
      } else {
         Range<K> coalescedRange = this.coalescedRange(range, Preconditions.checkNotNull(value));
         this.put(coalescedRange, value);
      }
   }

   private Range<K> coalescedRange(Range<K> range, V value) {
      Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> lowerEntry = this.entriesByLowerBound.lowerEntry(range.lowerBound);
      Range<K> coalescedRange = coalesce(range, value, lowerEntry);
      Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> higherEntry = this.entriesByLowerBound.floorEntry(range.upperBound);
      coalescedRange = coalesce(coalescedRange, value, higherEntry);
      return coalescedRange;
   }

   private static <K extends Comparable, V> Range<K> coalesce(Range<K> range, V value, @CheckForNull Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> entry) {
      return entry != null && ((TreeRangeMap.RangeMapEntry)entry.getValue()).getKey().isConnected(range) && ((TreeRangeMap.RangeMapEntry)entry.getValue()).getValue().equals(value) ? range.span(((TreeRangeMap.RangeMapEntry)entry.getValue()).getKey()) : range;
   }

   public void putAll(RangeMap<K, V> rangeMap) {
      Iterator var2 = rangeMap.asMapOfRanges().entrySet().iterator();

      while(var2.hasNext()) {
         Entry<Range<K>, V> entry = (Entry)var2.next();
         this.put((Range)entry.getKey(), entry.getValue());
      }

   }

   public void clear() {
      this.entriesByLowerBound.clear();
   }

   public Range<K> span() {
      Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> firstEntry = this.entriesByLowerBound.firstEntry();
      Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> lastEntry = this.entriesByLowerBound.lastEntry();
      if (firstEntry != null && lastEntry != null) {
         return Range.create(((TreeRangeMap.RangeMapEntry)firstEntry.getValue()).getKey().lowerBound, ((TreeRangeMap.RangeMapEntry)lastEntry.getValue()).getKey().upperBound);
      } else {
         throw new NoSuchElementException();
      }
   }

   private void putRangeMapEntry(Cut<K> lowerBound, Cut<K> upperBound, V value) {
      this.entriesByLowerBound.put(lowerBound, new TreeRangeMap.RangeMapEntry(lowerBound, upperBound, value));
   }

   public void remove(Range<K> rangeToRemove) {
      if (!rangeToRemove.isEmpty()) {
         Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> mapEntryBelowToTruncate = this.entriesByLowerBound.lowerEntry(rangeToRemove.lowerBound);
         if (mapEntryBelowToTruncate != null) {
            TreeRangeMap.RangeMapEntry<K, V> rangeMapEntry = (TreeRangeMap.RangeMapEntry)mapEntryBelowToTruncate.getValue();
            if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.lowerBound) > 0) {
               if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0) {
                  this.putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry.getUpperBound(), ((TreeRangeMap.RangeMapEntry)mapEntryBelowToTruncate.getValue()).getValue());
               }

               this.putRangeMapEntry(rangeMapEntry.getLowerBound(), rangeToRemove.lowerBound, ((TreeRangeMap.RangeMapEntry)mapEntryBelowToTruncate.getValue()).getValue());
            }
         }

         Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> mapEntryAboveToTruncate = this.entriesByLowerBound.lowerEntry(rangeToRemove.upperBound);
         if (mapEntryAboveToTruncate != null) {
            TreeRangeMap.RangeMapEntry<K, V> rangeMapEntry = (TreeRangeMap.RangeMapEntry)mapEntryAboveToTruncate.getValue();
            if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0) {
               this.putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry.getUpperBound(), ((TreeRangeMap.RangeMapEntry)mapEntryAboveToTruncate.getValue()).getValue());
            }
         }

         this.entriesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
      }
   }

   private void split(Cut<K> cut) {
      Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> mapEntryToSplit = this.entriesByLowerBound.lowerEntry(cut);
      if (mapEntryToSplit != null) {
         TreeRangeMap.RangeMapEntry<K, V> rangeMapEntry = (TreeRangeMap.RangeMapEntry)mapEntryToSplit.getValue();
         if (rangeMapEntry.getUpperBound().compareTo(cut) > 0) {
            this.putRangeMapEntry(rangeMapEntry.getLowerBound(), cut, rangeMapEntry.getValue());
            this.putRangeMapEntry(cut, rangeMapEntry.getUpperBound(), rangeMapEntry.getValue());
         }
      }
   }

   public void merge(Range<K> range, @CheckForNull V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
      Preconditions.checkNotNull(range);
      Preconditions.checkNotNull(remappingFunction);
      if (!range.isEmpty()) {
         this.split(range.lowerBound);
         this.split(range.upperBound);
         Set<Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>>> entriesInMergeRange = this.entriesByLowerBound.subMap(range.lowerBound, range.upperBound).entrySet();
         ImmutableMap.Builder<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> gaps = ImmutableMap.builder();
         Iterator backingItr;
         if (value != null) {
            backingItr = entriesInMergeRange.iterator();

            Cut lowerBound;
            TreeRangeMap.RangeMapEntry entry;
            for(lowerBound = range.lowerBound; backingItr.hasNext(); lowerBound = entry.getUpperBound()) {
               entry = (TreeRangeMap.RangeMapEntry)((Entry)backingItr.next()).getValue();
               Cut<K> upperBound = entry.getLowerBound();
               if (!lowerBound.equals(upperBound)) {
                  gaps.put(lowerBound, new TreeRangeMap.RangeMapEntry(lowerBound, upperBound, value));
               }
            }

            if (!lowerBound.equals(range.upperBound)) {
               gaps.put(lowerBound, new TreeRangeMap.RangeMapEntry(lowerBound, range.upperBound, value));
            }
         }

         backingItr = entriesInMergeRange.iterator();

         while(backingItr.hasNext()) {
            Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> entry = (Entry)backingItr.next();
            V newValue = remappingFunction.apply(((TreeRangeMap.RangeMapEntry)entry.getValue()).getValue(), value);
            if (newValue == null) {
               backingItr.remove();
            } else {
               entry.setValue(new TreeRangeMap.RangeMapEntry(((TreeRangeMap.RangeMapEntry)entry.getValue()).getLowerBound(), ((TreeRangeMap.RangeMapEntry)entry.getValue()).getUpperBound(), newValue));
            }
         }

         this.entriesByLowerBound.putAll(gaps.build());
      }
   }

   public Map<Range<K>, V> asMapOfRanges() {
      return new TreeRangeMap.AsMapOfRanges(this.entriesByLowerBound.values());
   }

   public Map<Range<K>, V> asDescendingMapOfRanges() {
      return new TreeRangeMap.AsMapOfRanges(this.entriesByLowerBound.descendingMap().values());
   }

   public RangeMap<K, V> subRangeMap(Range<K> subRange) {
      return (RangeMap)(subRange.equals(Range.all()) ? this : new TreeRangeMap.SubRangeMap(subRange));
   }

   private RangeMap<K, V> emptySubRangeMap() {
      return EMPTY_SUB_RANGE_MAP;
   }

   public boolean equals(@CheckForNull Object o) {
      if (o instanceof RangeMap) {
         RangeMap<?, ?> rangeMap = (RangeMap)o;
         return this.asMapOfRanges().equals(rangeMap.asMapOfRanges());
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.asMapOfRanges().hashCode();
   }

   public String toString() {
      return this.entriesByLowerBound.values().toString();
   }

   private class SubRangeMap implements RangeMap<K, V> {
      private final Range<K> subRange;

      SubRangeMap(Range<K> subRange) {
         this.subRange = subRange;
      }

      @CheckForNull
      public V get(K key) {
         return this.subRange.contains(key) ? TreeRangeMap.this.get(key) : null;
      }

      @CheckForNull
      public Entry<Range<K>, V> getEntry(K key) {
         if (this.subRange.contains(key)) {
            Entry<Range<K>, V> entry = TreeRangeMap.this.getEntry(key);
            if (entry != null) {
               return Maps.immutableEntry(((Range)entry.getKey()).intersection(this.subRange), entry.getValue());
            }
         }

         return null;
      }

      public Range<K> span() {
         Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> lowerEntry = TreeRangeMap.this.entriesByLowerBound.floorEntry(this.subRange.lowerBound);
         Cut lowerBound;
         if (lowerEntry != null && ((TreeRangeMap.RangeMapEntry)lowerEntry.getValue()).getUpperBound().compareTo(this.subRange.lowerBound) > 0) {
            lowerBound = this.subRange.lowerBound;
         } else {
            lowerBound = (Cut)TreeRangeMap.this.entriesByLowerBound.ceilingKey(this.subRange.lowerBound);
            if (lowerBound == null || lowerBound.compareTo(this.subRange.upperBound) >= 0) {
               throw new NoSuchElementException();
            }
         }

         Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> upperEntry = TreeRangeMap.this.entriesByLowerBound.lowerEntry(this.subRange.upperBound);
         if (upperEntry == null) {
            throw new NoSuchElementException();
         } else {
            Cut upperBound;
            if (((TreeRangeMap.RangeMapEntry)upperEntry.getValue()).getUpperBound().compareTo(this.subRange.upperBound) >= 0) {
               upperBound = this.subRange.upperBound;
            } else {
               upperBound = ((TreeRangeMap.RangeMapEntry)upperEntry.getValue()).getUpperBound();
            }

            return Range.create(lowerBound, upperBound);
         }
      }

      public void put(Range<K> range, V value) {
         Preconditions.checkArgument(this.subRange.encloses(range), "Cannot put range %s into a subRangeMap(%s)", range, this.subRange);
         TreeRangeMap.this.put(range, value);
      }

      public void putCoalescing(Range<K> range, V value) {
         if (!TreeRangeMap.this.entriesByLowerBound.isEmpty() && this.subRange.encloses(range)) {
            Range<K> coalescedRange = TreeRangeMap.this.coalescedRange(range, Preconditions.checkNotNull(value));
            this.put(coalescedRange.intersection(this.subRange), value);
         } else {
            this.put(range, value);
         }
      }

      public void putAll(RangeMap<K, V> rangeMap) {
         if (!rangeMap.asMapOfRanges().isEmpty()) {
            Range<K> span = rangeMap.span();
            Preconditions.checkArgument(this.subRange.encloses(span), "Cannot putAll rangeMap with span %s into a subRangeMap(%s)", span, this.subRange);
            TreeRangeMap.this.putAll(rangeMap);
         }
      }

      public void clear() {
         TreeRangeMap.this.remove(this.subRange);
      }

      public void remove(Range<K> range) {
         if (range.isConnected(this.subRange)) {
            TreeRangeMap.this.remove(range.intersection(this.subRange));
         }

      }

      public void merge(Range<K> range, @CheckForNull V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
         Preconditions.checkArgument(this.subRange.encloses(range), "Cannot merge range %s into a subRangeMap(%s)", range, this.subRange);
         TreeRangeMap.this.merge(range, value, remappingFunction);
      }

      public RangeMap<K, V> subRangeMap(Range<K> range) {
         return !range.isConnected(this.subRange) ? TreeRangeMap.this.emptySubRangeMap() : TreeRangeMap.this.subRangeMap(range.intersection(this.subRange));
      }

      public Map<Range<K>, V> asMapOfRanges() {
         return new TreeRangeMap.SubRangeMap.SubRangeMapAsMap();
      }

      public Map<Range<K>, V> asDescendingMapOfRanges() {
         return new TreeRangeMap<K, V>.SubRangeMap.SubRangeMapAsMap() {
            Iterator<Entry<Range<K>, V>> entryIterator() {
               if (SubRangeMap.this.subRange.isEmpty()) {
                  return Iterators.emptyIterator();
               } else {
                  final Iterator<TreeRangeMap.RangeMapEntry<K, V>> backingItr = TreeRangeMap.this.entriesByLowerBound.headMap(SubRangeMap.this.subRange.upperBound, false).descendingMap().values().iterator();
                  return new AbstractIterator<Entry<Range<K>, V>>() {
                     @CheckForNull
                     protected Entry<Range<K>, V> computeNext() {
                        if (backingItr.hasNext()) {
                           TreeRangeMap.RangeMapEntry<K, V> entry = (TreeRangeMap.RangeMapEntry)backingItr.next();
                           return entry.getUpperBound().compareTo(SubRangeMap.this.subRange.lowerBound) <= 0 ? (Entry)this.endOfData() : Maps.immutableEntry(entry.getKey().intersection(SubRangeMap.this.subRange), entry.getValue());
                        } else {
                           return (Entry)this.endOfData();
                        }
                     }
                  };
               }
            }
         };
      }

      public boolean equals(@CheckForNull Object o) {
         if (o instanceof RangeMap) {
            RangeMap<?, ?> rangeMap = (RangeMap)o;
            return this.asMapOfRanges().equals(rangeMap.asMapOfRanges());
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.asMapOfRanges().hashCode();
      }

      public String toString() {
         return this.asMapOfRanges().toString();
      }

      class SubRangeMapAsMap extends AbstractMap<Range<K>, V> {
         public boolean containsKey(@CheckForNull Object key) {
            return this.get(key) != null;
         }

         @CheckForNull
         public V get(@CheckForNull Object key) {
            try {
               if (key instanceof Range) {
                  Range<K> r = (Range)key;
                  if (!SubRangeMap.this.subRange.encloses(r) || r.isEmpty()) {
                     return null;
                  }

                  TreeRangeMap.RangeMapEntry<K, V> candidate = null;
                  if (r.lowerBound.compareTo(SubRangeMap.this.subRange.lowerBound) == 0) {
                     Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> entry = TreeRangeMap.this.entriesByLowerBound.floorEntry(r.lowerBound);
                     if (entry != null) {
                        candidate = (TreeRangeMap.RangeMapEntry)entry.getValue();
                     }
                  } else {
                     candidate = (TreeRangeMap.RangeMapEntry)TreeRangeMap.this.entriesByLowerBound.get(r.lowerBound);
                  }

                  if (candidate != null && candidate.getKey().isConnected(SubRangeMap.this.subRange) && candidate.getKey().intersection(SubRangeMap.this.subRange).equals(r)) {
                     return candidate.getValue();
                  }
               }

               return null;
            } catch (ClassCastException var5) {
               return null;
            }
         }

         @CheckForNull
         public V remove(@CheckForNull Object key) {
            V value = this.get(key);
            if (value != null) {
               Range<K> range = (Range)Objects.requireNonNull(key);
               TreeRangeMap.this.remove(range);
               return value;
            } else {
               return null;
            }
         }

         public void clear() {
            SubRangeMap.this.clear();
         }

         private boolean removeEntryIf(Predicate<? super Entry<Range<K>, V>> predicate) {
            List<Range<K>> toRemove = Lists.newArrayList();
            Iterator var3 = this.entrySet().iterator();

            while(var3.hasNext()) {
               Entry<Range<K>, V> entry = (Entry)var3.next();
               if (predicate.apply(entry)) {
                  toRemove.add((Range)entry.getKey());
               }
            }

            var3 = toRemove.iterator();

            while(var3.hasNext()) {
               Range<K> range = (Range)var3.next();
               TreeRangeMap.this.remove(range);
            }

            return !toRemove.isEmpty();
         }

         public Set<Range<K>> keySet() {
            return new Maps.KeySet<Range<K>, V>(this) {
               public boolean remove(@CheckForNull Object o) {
                  return SubRangeMapAsMap.this.remove(o) != null;
               }

               public boolean retainAll(Collection<?> c) {
                  return SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.not(Predicates.in(c)), Maps.keyFunction()));
               }
            };
         }

         public Set<Entry<Range<K>, V>> entrySet() {
            return new Maps.EntrySet<Range<K>, V>() {
               Map<Range<K>, V> map() {
                  return SubRangeMapAsMap.this;
               }

               public Iterator<Entry<Range<K>, V>> iterator() {
                  return SubRangeMapAsMap.this.entryIterator();
               }

               public boolean retainAll(Collection<?> c) {
                  return SubRangeMapAsMap.this.removeEntryIf(Predicates.not(Predicates.in(c)));
               }

               public int size() {
                  return Iterators.size(this.iterator());
               }

               public boolean isEmpty() {
                  return !this.iterator().hasNext();
               }
            };
         }

         Iterator<Entry<Range<K>, V>> entryIterator() {
            if (SubRangeMap.this.subRange.isEmpty()) {
               return Iterators.emptyIterator();
            } else {
               Cut<K> cutToStart = (Cut)MoreObjects.firstNonNull((Cut)TreeRangeMap.this.entriesByLowerBound.floorKey(SubRangeMap.this.subRange.lowerBound), SubRangeMap.this.subRange.lowerBound);
               final Iterator<TreeRangeMap.RangeMapEntry<K, V>> backingItr = TreeRangeMap.this.entriesByLowerBound.tailMap(cutToStart, true).values().iterator();
               return new AbstractIterator<Entry<Range<K>, V>>() {
                  @CheckForNull
                  protected Entry<Range<K>, V> computeNext() {
                     while(true) {
                        if (backingItr.hasNext()) {
                           TreeRangeMap.RangeMapEntry<K, V> entry = (TreeRangeMap.RangeMapEntry)backingItr.next();
                           if (entry.getLowerBound().compareTo(SubRangeMap.this.subRange.upperBound) >= 0) {
                              return (Entry)this.endOfData();
                           }

                           if (entry.getUpperBound().compareTo(SubRangeMap.this.subRange.lowerBound) <= 0) {
                              continue;
                           }

                           return Maps.immutableEntry(entry.getKey().intersection(SubRangeMap.this.subRange), entry.getValue());
                        }

                        return (Entry)this.endOfData();
                     }
                  }
               };
            }
         }

         public Collection<V> values() {
            return new Maps.Values<Range<K>, V>(this) {
               public boolean removeAll(Collection<?> c) {
                  return SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.in(c), Maps.valueFunction()));
               }

               public boolean retainAll(Collection<?> c) {
                  return SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.not(Predicates.in(c)), Maps.valueFunction()));
               }
            };
         }
      }
   }

   private final class AsMapOfRanges extends Maps.IteratorBasedAbstractMap<Range<K>, V> {
      final Iterable<Entry<Range<K>, V>> entryIterable;

      AsMapOfRanges(Iterable<TreeRangeMap.RangeMapEntry<K, V>> entryIterable) {
         this.entryIterable = entryIterable;
      }

      public boolean containsKey(@CheckForNull Object key) {
         return this.get(key) != null;
      }

      @CheckForNull
      public V get(@CheckForNull Object key) {
         if (key instanceof Range) {
            Range<?> range = (Range)key;
            TreeRangeMap.RangeMapEntry<K, V> rangeMapEntry = (TreeRangeMap.RangeMapEntry)TreeRangeMap.this.entriesByLowerBound.get(range.lowerBound);
            if (rangeMapEntry != null && rangeMapEntry.getKey().equals(range)) {
               return rangeMapEntry.getValue();
            }
         }

         return null;
      }

      public int size() {
         return TreeRangeMap.this.entriesByLowerBound.size();
      }

      Iterator<Entry<Range<K>, V>> entryIterator() {
         return this.entryIterable.iterator();
      }
   }

   private static final class RangeMapEntry<K extends Comparable, V> extends AbstractMapEntry<Range<K>, V> {
      private final Range<K> range;
      private final V value;

      RangeMapEntry(Cut<K> lowerBound, Cut<K> upperBound, V value) {
         this(Range.create(lowerBound, upperBound), value);
      }

      RangeMapEntry(Range<K> range, V value) {
         this.range = range;
         this.value = value;
      }

      public Range<K> getKey() {
         return this.range;
      }

      public V getValue() {
         return this.value;
      }

      public boolean contains(K value) {
         return this.range.contains(value);
      }

      Cut<K> getLowerBound() {
         return this.range.lowerBound;
      }

      Cut<K> getUpperBound() {
         return this.range.upperBound;
      }
   }
}
