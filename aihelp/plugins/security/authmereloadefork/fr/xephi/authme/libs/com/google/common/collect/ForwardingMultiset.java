package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Objects;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingMultiset<E> extends ForwardingCollection<E> implements Multiset<E> {
   protected ForwardingMultiset() {
   }

   protected abstract Multiset<E> delegate();

   public int count(@CheckForNull Object element) {
      return this.delegate().count(element);
   }

   @CanIgnoreReturnValue
   public int add(@ParametricNullness E element, int occurrences) {
      return this.delegate().add(element, occurrences);
   }

   @CanIgnoreReturnValue
   public int remove(@CheckForNull Object element, int occurrences) {
      return this.delegate().remove(element, occurrences);
   }

   public Set<E> elementSet() {
      return this.delegate().elementSet();
   }

   public Set<Multiset.Entry<E>> entrySet() {
      return this.delegate().entrySet();
   }

   public boolean equals(@CheckForNull Object object) {
      return object == this || this.delegate().equals(object);
   }

   public int hashCode() {
      return this.delegate().hashCode();
   }

   @CanIgnoreReturnValue
   public int setCount(@ParametricNullness E element, int count) {
      return this.delegate().setCount(element, count);
   }

   @CanIgnoreReturnValue
   public boolean setCount(@ParametricNullness E element, int oldCount, int newCount) {
      return this.delegate().setCount(element, oldCount, newCount);
   }

   protected boolean standardContains(@CheckForNull Object object) {
      return this.count(object) > 0;
   }

   protected void standardClear() {
      Iterators.clear(this.entrySet().iterator());
   }

   @Beta
   protected int standardCount(@CheckForNull Object object) {
      Iterator var2 = this.entrySet().iterator();

      Multiset.Entry entry;
      do {
         if (!var2.hasNext()) {
            return 0;
         }

         entry = (Multiset.Entry)var2.next();
      } while(!Objects.equal(entry.getElement(), object));

      return entry.getCount();
   }

   protected boolean standardAdd(@ParametricNullness E element) {
      this.add(element, 1);
      return true;
   }

   @Beta
   protected boolean standardAddAll(Collection<? extends E> elementsToAdd) {
      return Multisets.addAllImpl(this, (Collection)elementsToAdd);
   }

   protected boolean standardRemove(@CheckForNull Object element) {
      return this.remove(element, 1) > 0;
   }

   protected boolean standardRemoveAll(Collection<?> elementsToRemove) {
      return Multisets.removeAllImpl(this, elementsToRemove);
   }

   protected boolean standardRetainAll(Collection<?> elementsToRetain) {
      return Multisets.retainAllImpl(this, elementsToRetain);
   }

   protected int standardSetCount(@ParametricNullness E element, int count) {
      return Multisets.setCountImpl(this, element, count);
   }

   protected boolean standardSetCount(@ParametricNullness E element, int oldCount, int newCount) {
      return Multisets.setCountImpl(this, element, oldCount, newCount);
   }

   protected Iterator<E> standardIterator() {
      return Multisets.iteratorImpl(this);
   }

   protected int standardSize() {
      return Multisets.linearTimeSizeImpl(this);
   }

   protected boolean standardEquals(@CheckForNull Object object) {
      return Multisets.equalsImpl(this, object);
   }

   protected int standardHashCode() {
      return this.entrySet().hashCode();
   }

   protected String standardToString() {
      return this.entrySet().toString();
   }

   @Beta
   protected class StandardElementSet extends Multisets.ElementSet<E> {
      public StandardElementSet() {
      }

      Multiset<E> multiset() {
         return ForwardingMultiset.this;
      }

      public Iterator<E> iterator() {
         return Multisets.elementIterator(this.multiset().entrySet().iterator());
      }
   }
}
