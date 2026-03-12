package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingSortedSet<E> extends ForwardingSet<E> implements SortedSet<E> {
   protected ForwardingSortedSet() {
   }

   protected abstract SortedSet<E> delegate();

   @CheckForNull
   public Comparator<? super E> comparator() {
      return this.delegate().comparator();
   }

   @ParametricNullness
   public E first() {
      return this.delegate().first();
   }

   public SortedSet<E> headSet(@ParametricNullness E toElement) {
      return this.delegate().headSet(toElement);
   }

   @ParametricNullness
   public E last() {
      return this.delegate().last();
   }

   public SortedSet<E> subSet(@ParametricNullness E fromElement, @ParametricNullness E toElement) {
      return this.delegate().subSet(fromElement, toElement);
   }

   public SortedSet<E> tailSet(@ParametricNullness E fromElement) {
      return this.delegate().tailSet(fromElement);
   }

   @Beta
   protected boolean standardContains(@CheckForNull Object object) {
      try {
         Object ceiling = this.tailSet(object).first();
         return ForwardingSortedMap.unsafeCompare(this.comparator(), ceiling, object) == 0;
      } catch (NoSuchElementException | NullPointerException | ClassCastException var4) {
         return false;
      }
   }

   @Beta
   protected boolean standardRemove(@CheckForNull Object object) {
      try {
         Iterator<?> iterator = this.tailSet(object).iterator();
         if (iterator.hasNext()) {
            Object ceiling = iterator.next();
            if (ForwardingSortedMap.unsafeCompare(this.comparator(), ceiling, object) == 0) {
               iterator.remove();
               return true;
            }
         }

         return false;
      } catch (NullPointerException | ClassCastException var5) {
         return false;
      }
   }

   @Beta
   protected SortedSet<E> standardSubSet(@ParametricNullness E fromElement, @ParametricNullness E toElement) {
      return this.tailSet(fromElement).headSet(toElement);
   }
}
