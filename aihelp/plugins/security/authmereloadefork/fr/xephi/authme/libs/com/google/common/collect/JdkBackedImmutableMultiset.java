package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.primitives.Ints;
import java.util.Collection;
import java.util.Map;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class JdkBackedImmutableMultiset<E> extends ImmutableMultiset<E> {
   private final Map<E, Integer> delegateMap;
   private final ImmutableList<Multiset.Entry<E>> entries;
   private final long size;
   @CheckForNull
   private transient ImmutableSet<E> elementSet;

   static <E> ImmutableMultiset<E> create(Collection<? extends Multiset.Entry<? extends E>> entries) {
      Multiset.Entry<E>[] entriesArray = (Multiset.Entry[])entries.toArray(new Multiset.Entry[0]);
      Map<E, Integer> delegateMap = Maps.newHashMapWithExpectedSize(entriesArray.length);
      long size = 0L;

      for(int i = 0; i < entriesArray.length; ++i) {
         Multiset.Entry<E> entry = entriesArray[i];
         int count = entry.getCount();
         size += (long)count;
         E element = Preconditions.checkNotNull(entry.getElement());
         delegateMap.put(element, count);
         if (!(entry instanceof Multisets.ImmutableEntry)) {
            entriesArray[i] = Multisets.immutableEntry(element, count);
         }
      }

      return new JdkBackedImmutableMultiset(delegateMap, ImmutableList.asImmutableList(entriesArray), size);
   }

   private JdkBackedImmutableMultiset(Map<E, Integer> delegateMap, ImmutableList<Multiset.Entry<E>> entries, long size) {
      this.delegateMap = delegateMap;
      this.entries = entries;
      this.size = size;
   }

   public int count(@CheckForNull Object element) {
      return (Integer)this.delegateMap.getOrDefault(element, 0);
   }

   public ImmutableSet<E> elementSet() {
      ImmutableSet<E> result = this.elementSet;
      return result == null ? (this.elementSet = new ImmutableMultiset.ElementSet(this.entries, this)) : result;
   }

   Multiset.Entry<E> getEntry(int index) {
      return (Multiset.Entry)this.entries.get(index);
   }

   boolean isPartialView() {
      return false;
   }

   public int size() {
      return Ints.saturatedCast(this.size);
   }
}
