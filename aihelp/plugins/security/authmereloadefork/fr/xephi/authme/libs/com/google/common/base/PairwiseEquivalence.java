package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   serializable = true
)
final class PairwiseEquivalence<E, T extends E> extends Equivalence<Iterable<T>> implements Serializable {
   final Equivalence<E> elementEquivalence;
   private static final long serialVersionUID = 1L;

   PairwiseEquivalence(Equivalence<E> elementEquivalence) {
      this.elementEquivalence = (Equivalence)Preconditions.checkNotNull(elementEquivalence);
   }

   protected boolean doEquivalent(Iterable<T> iterableA, Iterable<T> iterableB) {
      Iterator<T> iteratorA = iterableA.iterator();
      Iterator iteratorB = iterableB.iterator();

      while(iteratorA.hasNext() && iteratorB.hasNext()) {
         if (!this.elementEquivalence.equivalent(iteratorA.next(), iteratorB.next())) {
            return false;
         }
      }

      return !iteratorA.hasNext() && !iteratorB.hasNext();
   }

   protected int doHash(Iterable<T> iterable) {
      int hash = 78721;

      Object element;
      for(Iterator var3 = iterable.iterator(); var3.hasNext(); hash = hash * 24943 + this.elementEquivalence.hash(element)) {
         element = var3.next();
      }

      return hash;
   }

   public boolean equals(@CheckForNull Object object) {
      if (object instanceof PairwiseEquivalence) {
         PairwiseEquivalence<?, ?> that = (PairwiseEquivalence)object;
         return this.elementEquivalence.equals(that.elementEquivalence);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.elementEquivalence.hashCode() ^ 1185147655;
   }

   public String toString() {
      String var1 = String.valueOf(this.elementEquivalence);
      return (new StringBuilder(11 + String.valueOf(var1).length())).append(var1).append(".pairwise()").toString();
   }
}
