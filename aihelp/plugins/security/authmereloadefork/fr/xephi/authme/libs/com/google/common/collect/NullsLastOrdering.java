package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   serializable = true
)
final class NullsLastOrdering<T> extends Ordering<T> implements Serializable {
   final Ordering<? super T> ordering;
   private static final long serialVersionUID = 0L;

   NullsLastOrdering(Ordering<? super T> ordering) {
      this.ordering = ordering;
   }

   public int compare(@CheckForNull T left, @CheckForNull T right) {
      if (left == right) {
         return 0;
      } else if (left == null) {
         return 1;
      } else {
         return right == null ? -1 : this.ordering.compare(left, right);
      }
   }

   public <S extends T> Ordering<S> reverse() {
      return this.ordering.reverse().nullsFirst();
   }

   public <S extends T> Ordering<S> nullsFirst() {
      return this.ordering.nullsFirst();
   }

   public <S extends T> Ordering<S> nullsLast() {
      return this;
   }

   public boolean equals(@CheckForNull Object object) {
      if (object == this) {
         return true;
      } else if (object instanceof NullsLastOrdering) {
         NullsLastOrdering<?> that = (NullsLastOrdering)object;
         return this.ordering.equals(that.ordering);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.ordering.hashCode() ^ -921210296;
   }

   public String toString() {
      String var1 = String.valueOf(this.ordering);
      return (new StringBuilder(12 + String.valueOf(var1).length())).append(var1).append(".nullsLast()").toString();
   }
}
