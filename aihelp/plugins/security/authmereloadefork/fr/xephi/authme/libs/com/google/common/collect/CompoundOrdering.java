package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   serializable = true
)
final class CompoundOrdering<T> extends Ordering<T> implements Serializable {
   final Comparator<? super T>[] comparators;
   private static final long serialVersionUID = 0L;

   CompoundOrdering(Comparator<? super T> primary, Comparator<? super T> secondary) {
      this.comparators = new Comparator[]{primary, secondary};
   }

   CompoundOrdering(Iterable<? extends Comparator<? super T>> comparators) {
      this.comparators = (Comparator[])Iterables.toArray(comparators, (Object[])(new Comparator[0]));
   }

   public int compare(@ParametricNullness T left, @ParametricNullness T right) {
      for(int i = 0; i < this.comparators.length; ++i) {
         int result = this.comparators[i].compare(left, right);
         if (result != 0) {
            return result;
         }
      }

      return 0;
   }

   public boolean equals(@CheckForNull Object object) {
      if (object == this) {
         return true;
      } else if (object instanceof CompoundOrdering) {
         CompoundOrdering<?> that = (CompoundOrdering)object;
         return Arrays.equals(this.comparators, that.comparators);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Arrays.hashCode(this.comparators);
   }

   public String toString() {
      String var1 = Arrays.toString(this.comparators);
      return (new StringBuilder(19 + String.valueOf(var1).length())).append("Ordering.compound(").append(var1).append(")").toString();
   }
}
