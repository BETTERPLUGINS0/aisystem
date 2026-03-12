package com.nisovin.shopkeepers.util.java;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface KeyValueStore {
   @Nullable
   <T> T get(String var1);

   void set(String var1, @Nullable Object var2);
}
