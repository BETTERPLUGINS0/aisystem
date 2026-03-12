package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import java.util.SortedSet;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
interface SortedMultisetBridge<E> extends Multiset<E> {
   SortedSet<E> elementSet();
}
