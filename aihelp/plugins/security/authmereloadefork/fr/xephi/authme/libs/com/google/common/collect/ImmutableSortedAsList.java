package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import java.util.Comparator;
import java.util.Objects;
import java.util.Spliterator;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
final class ImmutableSortedAsList<E> extends RegularImmutableAsList<E> implements SortedIterable<E> {
   ImmutableSortedAsList(ImmutableSortedSet<E> backingSet, ImmutableList<E> backingList) {
      super(backingSet, (ImmutableList)backingList);
   }

   ImmutableSortedSet<E> delegateCollection() {
      return (ImmutableSortedSet)super.delegateCollection();
   }

   public Comparator<? super E> comparator() {
      return this.delegateCollection().comparator();
   }

   @GwtIncompatible
   public int indexOf(@CheckForNull Object target) {
      int index = this.delegateCollection().indexOf(target);
      return index >= 0 && this.get(index).equals(target) ? index : -1;
   }

   @GwtIncompatible
   public int lastIndexOf(@CheckForNull Object target) {
      return this.indexOf(target);
   }

   public boolean contains(@CheckForNull Object target) {
      return this.indexOf(target) >= 0;
   }

   @GwtIncompatible
   ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
      ImmutableList<E> parentSubList = super.subListUnchecked(fromIndex, toIndex);
      return (new RegularImmutableSortedSet(parentSubList, this.comparator())).asList();
   }

   public Spliterator<E> spliterator() {
      int var10000 = this.size();
      ImmutableList var10002 = this.delegateList();
      Objects.requireNonNull(var10002);
      return CollectSpliterators.indexed(var10000, 1301, var10002::get, this.comparator());
   }
}
