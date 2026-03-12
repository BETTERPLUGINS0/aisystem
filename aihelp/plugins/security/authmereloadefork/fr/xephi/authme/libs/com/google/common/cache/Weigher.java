package fr.xephi.authme.libs.com.google.common.cache;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface Weigher<K, V> {
   int weigh(K var1, V var2);
}
