package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
public interface SortedMultiset<E> extends SortedMultisetBridge<E>, SortedIterable<E> {
   Comparator<? super E> comparator();

   @CheckForNull
   Multiset.Entry<E> firstEntry();

   @CheckForNull
   Multiset.Entry<E> lastEntry();

   @CheckForNull
   Multiset.Entry<E> pollFirstEntry();

   @CheckForNull
   Multiset.Entry<E> pollLastEntry();

   NavigableSet<E> elementSet();

   Set<Multiset.Entry<E>> entrySet();

   Iterator<E> iterator();

   SortedMultiset<E> descendingMultiset();

   SortedMultiset<E> headMultiset(@ParametricNullness E var1, BoundType var2);

   SortedMultiset<E> subMultiset(@ParametricNullness E var1, BoundType var2, @ParametricNullness E var3, BoundType var4);

   SortedMultiset<E> tailMultiset(@ParametricNullness E var1, BoundType var2);
}
