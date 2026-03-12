package com.nisovin.shopkeepers.commands.lib.context;

import java.util.Map;
import java.util.function.Supplier;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface CommandContext {
   void put(String var1, Object var2);

   @NonNull
   <T> T get(String var1);

   @Nullable
   <T> T getOrNull(String var1);

   @Nullable
   <T> T getOrDefault(String var1, @Nullable T var2);

   @Nullable
   <T> T getOrDefault(String var1, Supplier<T> var2);

   boolean has(String var1);

   Map<? extends String, ?> getMapView();

   CommandContextView getView();

   CommandContext copy();
}
