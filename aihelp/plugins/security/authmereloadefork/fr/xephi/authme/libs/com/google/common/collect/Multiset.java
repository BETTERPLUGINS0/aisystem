package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CompatibleWith;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface Multiset<E> extends Collection<E> {
   int size();

   int count(@CheckForNull @CompatibleWith("E") Object var1);

   @CanIgnoreReturnValue
   int add(@ParametricNullness E var1, int var2);

   @CanIgnoreReturnValue
   boolean add(@ParametricNullness E var1);

   @CanIgnoreReturnValue
   int remove(@CheckForNull @CompatibleWith("E") Object var1, int var2);

   @CanIgnoreReturnValue
   boolean remove(@CheckForNull Object var1);

   @CanIgnoreReturnValue
   int setCount(@ParametricNullness E var1, int var2);

   @CanIgnoreReturnValue
   boolean setCount(@ParametricNullness E var1, int var2, int var3);

   Set<E> elementSet();

   Set<Multiset.Entry<E>> entrySet();

   @Beta
   default void forEachEntry(ObjIntConsumer<? super E> action) {
      Preconditions.checkNotNull(action);
      this.entrySet().forEach((entry) -> {
         action.accept(entry.getElement(), entry.getCount());
      });
   }

   boolean equals(@CheckForNull Object var1);

   int hashCode();

   String toString();

   Iterator<E> iterator();

   boolean contains(@CheckForNull Object var1);

   boolean containsAll(Collection<?> var1);

   @CanIgnoreReturnValue
   boolean removeAll(Collection<?> var1);

   @CanIgnoreReturnValue
   boolean retainAll(Collection<?> var1);

   default void forEach(Consumer<? super E> action) {
      Preconditions.checkNotNull(action);
      this.entrySet().forEach((entry) -> {
         E elem = entry.getElement();
         int count = entry.getCount();

         for(int i = 0; i < count; ++i) {
            action.accept(elem);
         }

      });
   }

   default Spliterator<E> spliterator() {
      return Multisets.spliteratorImpl(this);
   }

   public interface Entry<E> {
      @ParametricNullness
      E getElement();

      int getCount();

      boolean equals(@CheckForNull Object var1);

      int hashCode();

      String toString();
   }
}
