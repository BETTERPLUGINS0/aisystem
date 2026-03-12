package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.util.NoSuchElementException;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class AbstractSequentialIterator<T> extends UnmodifiableIterator<T> {
   @CheckForNull
   private T nextOrNull;

   protected AbstractSequentialIterator(@CheckForNull T firstOrNull) {
      this.nextOrNull = firstOrNull;
   }

   @CheckForNull
   protected abstract T computeNext(T var1);

   public final boolean hasNext() {
      return this.nextOrNull != null;
   }

   public final T next() {
      if (this.nextOrNull == null) {
         throw new NoSuchElementException();
      } else {
         T oldNext = this.nextOrNull;
         this.nextOrNull = this.computeNext(oldNext);
         return oldNext;
      }
   }
}
