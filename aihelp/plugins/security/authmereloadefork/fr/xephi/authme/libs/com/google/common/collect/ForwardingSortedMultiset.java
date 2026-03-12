package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
@GwtCompatible(
   emulated = true
)
public abstract class ForwardingSortedMultiset<E> extends ForwardingMultiset<E> implements SortedMultiset<E> {
   protected ForwardingSortedMultiset() {
   }

   protected abstract SortedMultiset<E> delegate();

   public NavigableSet<E> elementSet() {
      return this.delegate().elementSet();
   }

   public Comparator<? super E> comparator() {
      return this.delegate().comparator();
   }

   public SortedMultiset<E> descendingMultiset() {
      return this.delegate().descendingMultiset();
   }

   @CheckForNull
   public Multiset.Entry<E> firstEntry() {
      return this.delegate().firstEntry();
   }

   @CheckForNull
   protected Multiset.Entry<E> standardFirstEntry() {
      Iterator<Multiset.Entry<E>> entryIterator = this.entrySet().iterator();
      if (!entryIterator.hasNext()) {
         return null;
      } else {
         Multiset.Entry<E> entry = (Multiset.Entry)entryIterator.next();
         return Multisets.immutableEntry(entry.getElement(), entry.getCount());
      }
   }

   @CheckForNull
   public Multiset.Entry<E> lastEntry() {
      return this.delegate().lastEntry();
   }

   @CheckForNull
   protected Multiset.Entry<E> standardLastEntry() {
      Iterator<Multiset.Entry<E>> entryIterator = this.descendingMultiset().entrySet().iterator();
      if (!entryIterator.hasNext()) {
         return null;
      } else {
         Multiset.Entry<E> entry = (Multiset.Entry)entryIterator.next();
         return Multisets.immutableEntry(entry.getElement(), entry.getCount());
      }
   }

   @CheckForNull
   public Multiset.Entry<E> pollFirstEntry() {
      return this.delegate().pollFirstEntry();
   }

   @CheckForNull
   protected Multiset.Entry<E> standardPollFirstEntry() {
      Iterator<Multiset.Entry<E>> entryIterator = this.entrySet().iterator();
      if (!entryIterator.hasNext()) {
         return null;
      } else {
         Multiset.Entry<E> entry = (Multiset.Entry)entryIterator.next();
         entry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
         entryIterator.remove();
         return entry;
      }
   }

   @CheckForNull
   public Multiset.Entry<E> pollLastEntry() {
      return this.delegate().pollLastEntry();
   }

   @CheckForNull
   protected Multiset.Entry<E> standardPollLastEntry() {
      Iterator<Multiset.Entry<E>> entryIterator = this.descendingMultiset().entrySet().iterator();
      if (!entryIterator.hasNext()) {
         return null;
      } else {
         Multiset.Entry<E> entry = (Multiset.Entry)entryIterator.next();
         entry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
         entryIterator.remove();
         return entry;
      }
   }

   public SortedMultiset<E> headMultiset(@ParametricNullness E upperBound, BoundType boundType) {
      return this.delegate().headMultiset(upperBound, boundType);
   }

   public SortedMultiset<E> subMultiset(@ParametricNullness E lowerBound, BoundType lowerBoundType, @ParametricNullness E upperBound, BoundType upperBoundType) {
      return this.delegate().subMultiset(lowerBound, lowerBoundType, upperBound, upperBoundType);
   }

   protected SortedMultiset<E> standardSubMultiset(@ParametricNullness E lowerBound, BoundType lowerBoundType, @ParametricNullness E upperBound, BoundType upperBoundType) {
      return this.tailMultiset(lowerBound, lowerBoundType).headMultiset(upperBound, upperBoundType);
   }

   public SortedMultiset<E> tailMultiset(@ParametricNullness E lowerBound, BoundType boundType) {
      return this.delegate().tailMultiset(lowerBound, boundType);
   }

   protected abstract class StandardDescendingMultiset extends DescendingMultiset<E> {
      public StandardDescendingMultiset() {
      }

      SortedMultiset<E> forwardMultiset() {
         return ForwardingSortedMultiset.this;
      }
   }

   protected class StandardElementSet extends SortedMultisets.NavigableElementSet<E> {
      public StandardElementSet(ForwardingSortedMultiset this$0) {
         super(this$0);
      }
   }
}
