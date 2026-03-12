package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Predicate;
import java.util.Map.Entry;

@ElementTypesAreNonnullByDefault
@GwtCompatible
interface FilteredMultimap<K, V> extends Multimap<K, V> {
   Multimap<K, V> unfiltered();

   Predicate<? super Entry<K, V>> entryPredicate();
}
