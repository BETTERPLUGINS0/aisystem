package com.nisovin.shopkeepers.api.types;

import java.util.Collection;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface TypeRegistry<T extends Type> {
   void register(@NonNull T var1);

   void registerAll(Collection<? extends T> var1);

   Collection<? extends T> getRegisteredTypes();

   @Nullable
   T get(String var1);

   @Nullable
   T match(String var1);

   void clearAll();
}
