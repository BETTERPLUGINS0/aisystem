package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.primitives.Ints;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.ObjIntConsumer;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
abstract class AbstractMapBasedMultiset<E> extends AbstractMultiset<E> implements Serializable {
   private transient Map<E, Count> backingMap;
   private transient long size;
   @GwtIncompatible
   private static final long serialVersionUID = -2250766705698539974L;

   protected AbstractMapBasedMultiset(Map<E, Count> backingMap) {
      Preconditions.checkArgument(backingMap.isEmpty());
      this.backingMap = backingMap;
   }

   void setBackingMap(Map<E, Count> backingMap) {
      this.backingMap = backingMap;
   }

   public Set<Multiset.Entry<E>> entrySet() {
      return super.entrySet();
   }

   Iterator<E> elementIterator() {
      final Iterator<java.util.Map.Entry<E, Count>> backingEntries = this.backingMap.entrySet().iterator();
      return new Iterator<E>() {
         @CheckForNull
         java.util.Map.Entry<E, Count> toRemove;

         public boolean hasNext() {
            return backingEntries.hasNext();
         }

         @ParametricNullness
         public E next() {
            java.util.Map.Entry<E, Count> mapEntry = (java.util.Map.Entry)backingEntries.next();
            this.toRemove = mapEntry;
            return mapEntry.getKey();
         }

         public void remove() {
            Preconditions.checkState(this.toRemove != null, "no calls to next() since the last call to remove()");
            AbstractMapBasedMultiset.this.size = (long)((Count)this.toRemove.getValue()).getAndSet(0);
            backingEntries.remove();
            this.toRemove = null;
         }
      };
   }

   Iterator<Multiset.Entry<E>> entryIterator() {
      final Iterator<java.util.Map.Entry<E, Count>> backingEntries = this.backingMap.entrySet().iterator();
      return new Iterator<Multiset.Entry<E>>() {
         @CheckForNull
         java.util.Map.Entry<E, Count> toRemove;

         public boolean hasNext() {
            return backingEntries.hasNext();
         }

         public Multiset.Entry<E> next() {
            final java.util.Map.Entry<E, Count> mapEntry = (java.util.Map.Entry)backingEntries.next();
            this.toRemove = mapEntry;
            return new Multisets.AbstractEntry<E>() {
               @ParametricNullness
               public E getElement() {
                  return mapEntry.getKey();
               }

               public int getCount() {
                  Count count = (Count)mapEntry.getValue();
                  if (count == null || count.get() == 0) {
                     Count frequency = (Count)AbstractMapBasedMultiset.this.backingMap.get(this.getElement());
                     if (frequency != null) {
                        return frequency.get();
                     }
                  }

                  return count == null ? 0 : count.get();
               }
            };
         }

         public void remove() {
            Preconditions.checkState(this.toRemove != null, "no calls to next() since the last call to remove()");
            AbstractMapBasedMultiset.this.size = (long)((Count)this.toRemove.getValue()).getAndSet(0);
            backingEntries.remove();
            this.toRemove = null;
         }
      };
   }

   public void forEachEntry(ObjIntConsumer<? super E> action) {
      Preconditions.checkNotNull(action);
      this.backingMap.forEach((element, count) -> {
         action.accept(element, count.get());
      });
   }

   public void clear() {
      Iterator var1 = this.backingMap.values().iterator();

      while(var1.hasNext()) {
         Count frequency = (Count)var1.next();
         frequency.set(0);
      }

      this.backingMap.clear();
      this.size = 0L;
   }

   int distinctElements() {
      return this.backingMap.size();
   }

   public int size() {
      return Ints.saturatedCast(this.size);
   }

   public Iterator<E> iterator() {
      return new AbstractMapBasedMultiset.MapBasedMultisetIterator();
   }

   public int count(@CheckForNull Object element) {
      Count frequency = (Count)Maps.safeGet(this.backingMap, element);
      return frequency == null ? 0 : frequency.get();
   }

   @CanIgnoreReturnValue
   public int add(@ParametricNullness E element, int occurrences) {
      if (occurrences == 0) {
         return this.count(element);
      } else {
         Preconditions.checkArgument(occurrences > 0, "occurrences cannot be negative: %s", occurrences);
         Count frequency = (Count)this.backingMap.get(element);
         int oldCount;
         if (frequency == null) {
            oldCount = 0;
            this.backingMap.put(element, new Count(occurrences));
         } else {
            oldCount = frequency.get();
            long newCount = (long)oldCount + (long)occurrences;
            Preconditions.checkArgument(newCount <= 2147483647L, "too many occurrences: %s", newCount);
            frequency.add(occurrences);
         }

         this.size += (long)occurrences;
         return oldCount;
      }
   }

   @CanIgnoreReturnValue
   public int remove(@CheckForNull Object element, int occurrences) {
      if (occurrences == 0) {
         return this.count(element);
      } else {
         Preconditions.checkArgument(occurrences > 0, "occurrences cannot be negative: %s", occurrences);
         Count frequency = (Count)this.backingMap.get(element);
         if (frequency == null) {
            return 0;
         } else {
            int oldCount = frequency.get();
            int numberRemoved;
            if (oldCount > occurrences) {
               numberRemoved = occurrences;
            } else {
               numberRemoved = oldCount;
               this.backingMap.remove(element);
            }

            frequency.add(-numberRemoved);
            this.size -= (long)numberRemoved;
            return oldCount;
         }
      }
   }

   @CanIgnoreReturnValue
   public int setCount(@ParametricNullness E element, int count) {
      CollectPreconditions.checkNonnegative(count, "count");
      Count existingCounter;
      int oldCount;
      if (count == 0) {
         existingCounter = (Count)this.backingMap.remove(element);
         oldCount = getAndSet(existingCounter, count);
      } else {
         existingCounter = (Count)this.backingMap.get(element);
         oldCount = getAndSet(existingCounter, count);
         if (existingCounter == null) {
            this.backingMap.put(element, new Count(count));
         }
      }

      this.size += (long)(count - oldCount);
      return oldCount;
   }

   private static int getAndSet(@CheckForNull Count i, int count) {
      return i == null ? 0 : i.getAndSet(count);
   }

   @GwtIncompatible
   private void readObjectNoData() throws ObjectStreamException {
      throw new InvalidObjectException("Stream data required");
   }

   private class MapBasedMultisetIterator implements Iterator<E> {
      final Iterator<java.util.Map.Entry<E, Count>> entryIterator;
      @CheckForNull
      java.util.Map.Entry<E, Count> currentEntry;
      int occurrencesLeft;
      boolean canRemove;

      MapBasedMultisetIterator() {
         this.entryIterator = AbstractMapBasedMultiset.this.backingMap.entrySet().iterator();
      }

      public boolean hasNext() {
         return this.occurrencesLeft > 0 || this.entryIterator.hasNext();
      }

      @ParametricNullness
      public E next() {
         if (this.occurrencesLeft == 0) {
            this.currentEntry = (java.util.Map.Entry)this.entryIterator.next();
            this.occurrencesLeft = ((Count)this.currentEntry.getValue()).get();
         }

         --this.occurrencesLeft;
         this.canRemove = true;
         return ((java.util.Map.Entry)Objects.requireNonNull(this.currentEntry)).getKey();
      }

      public void remove() {
         CollectPreconditions.checkRemove(this.canRemove);
         int frequency = ((Count)((java.util.Map.Entry)Objects.requireNonNull(this.currentEntry)).getValue()).get();
         if (frequency <= 0) {
            throw new ConcurrentModificationException();
         } else {
            if (((Count)this.currentEntry.getValue()).addAndGet(-1) == 0) {
               this.entryIterator.remove();
            }

            AbstractMapBasedMultiset.this.size--;
            this.canRemove = false;
         }
      }
   }
}
