package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.util.ListIterator;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class TransformedListIterator<F, T> extends TransformedIterator<F, T> implements ListIterator<T> {
   TransformedListIterator(ListIterator<? extends F> backingIterator) {
      super(backingIterator);
   }

   private ListIterator<? extends F> backingIterator() {
      return Iterators.cast(this.backingIterator);
   }

   public final boolean hasPrevious() {
      return this.backingIterator().hasPrevious();
   }

   @ParametricNullness
   public final T previous() {
      return this.transform(this.backingIterator().previous());
   }

   public final int nextIndex() {
      return this.backingIterator().nextIndex();
   }

   public final int previousIndex() {
      return this.backingIterator().previousIndex();
   }

   public void set(@ParametricNullness T element) {
      throw new UnsupportedOperationException();
   }

   public void add(@ParametricNullness T element) {
      throw new UnsupportedOperationException();
   }
}
