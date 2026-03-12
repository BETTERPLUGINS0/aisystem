package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class AbstractMultiset<E> extends AbstractCollection<E> implements Multiset<E> {
   @LazyInit
   @CheckForNull
   private transient Set<E> elementSet;
   @LazyInit
   @CheckForNull
   private transient Set<Multiset.Entry<E>> entrySet;

   public boolean isEmpty() {
      return this.entrySet().isEmpty();
   }

   public boolean contains(@CheckForNull Object element) {
      return this.count(element) > 0;
   }

   @CanIgnoreReturnValue
   public final boolean add(@ParametricNullness E element) {
      this.add(element, 1);
      return true;
   }

   @CanIgnoreReturnValue
   public int add(@ParametricNullness E element, int occurrences) {
      throw new UnsupportedOperationException();
   }

   @CanIgnoreReturnValue
   public final boolean remove(@CheckForNull Object element) {
      return this.remove(element, 1) > 0;
   }

   @CanIgnoreReturnValue
   public int remove(@CheckForNull Object element, int occurrences) {
      throw new UnsupportedOperationException();
   }

   @CanIgnoreReturnValue
   public int setCount(@ParametricNullness E element, int count) {
      return Multisets.setCountImpl(this, element, count);
   }

   @CanIgnoreReturnValue
   public boolean setCount(@ParametricNullness E element, int oldCount, int newCount) {
      return Multisets.setCountImpl(this, element, oldCount, newCount);
   }

   @CanIgnoreReturnValue
   public final boolean addAll(Collection<? extends E> elementsToAdd) {
      return Multisets.addAllImpl(this, (Collection)elementsToAdd);
   }

   @CanIgnoreReturnValue
   public final boolean removeAll(Collection<?> elementsToRemove) {
      return Multisets.removeAllImpl(this, elementsToRemove);
   }

   @CanIgnoreReturnValue
   public final boolean retainAll(Collection<?> elementsToRetain) {
      return Multisets.retainAllImpl(this, elementsToRetain);
   }

   public abstract void clear();

   public Set<E> elementSet() {
      Set<E> result = this.elementSet;
      if (result == null) {
         this.elementSet = result = this.createElementSet();
      }

      return result;
   }

   Set<E> createElementSet() {
      return new AbstractMultiset.ElementSet();
   }

   abstract Iterator<E> elementIterator();

   public Set<Multiset.Entry<E>> entrySet() {
      Set<Multiset.Entry<E>> result = this.entrySet;
      if (result == null) {
         this.entrySet = result = this.createEntrySet();
      }

      return result;
   }

   Set<Multiset.Entry<E>> createEntrySet() {
      return new AbstractMultiset.EntrySet();
   }

   abstract Iterator<Multiset.Entry<E>> entryIterator();

   abstract int distinctElements();

   public final boolean equals(@CheckForNull Object object) {
      return Multisets.equalsImpl(this, object);
   }

   public final int hashCode() {
      return this.entrySet().hashCode();
   }

   public final String toString() {
      return this.entrySet().toString();
   }

   class EntrySet extends Multisets.EntrySet<E> {
      Multiset<E> multiset() {
         return AbstractMultiset.this;
      }

      public Iterator<Multiset.Entry<E>> iterator() {
         return AbstractMultiset.this.entryIterator();
      }

      public int size() {
         return AbstractMultiset.this.distinctElements();
      }
   }

   class ElementSet extends Multisets.ElementSet<E> {
      Multiset<E> multiset() {
         return AbstractMultiset.this;
      }

      public Iterator<E> iterator() {
         return AbstractMultiset.this.elementIterator();
      }
   }
}
