package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.util.Spliterator;
import java.util.function.Consumer;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
abstract class IndexedImmutableSet<E> extends ImmutableSet.CachingAsList<E> {
   abstract E get(int var1);

   public UnmodifiableIterator<E> iterator() {
      return this.asList().iterator();
   }

   public Spliterator<E> spliterator() {
      return CollectSpliterators.indexed(this.size(), 1297, this::get);
   }

   public void forEach(Consumer<? super E> consumer) {
      Preconditions.checkNotNull(consumer);
      int n = this.size();

      for(int i = 0; i < n; ++i) {
         consumer.accept(this.get(i));
      }

   }

   @GwtIncompatible
   int copyIntoArray(Object[] dst, int offset) {
      return this.asList().copyIntoArray(dst, offset);
   }

   ImmutableList<E> createAsList() {
      return new ImmutableAsList<E>() {
         public E get(int index) {
            return IndexedImmutableSet.this.get(index);
         }

         boolean isPartialView() {
            return IndexedImmutableSet.this.isPartialView();
         }

         public int size() {
            return IndexedImmutableSet.this.size();
         }

         ImmutableCollection<E> delegateCollection() {
            return IndexedImmutableSet.this;
         }
      };
   }
}
