package fr.xephi.authme.libs.org.postgresql.util;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface Gettable<K, V> {
   @Nullable
   V get(K var1);
}
