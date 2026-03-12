package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.util.Comparator;
import java.util.Iterator;

@ElementTypesAreNonnullByDefault
@GwtCompatible
interface SortedIterable<T> extends Iterable<T> {
   Comparator<? super T> comparator();

   Iterator<T> iterator();
}
