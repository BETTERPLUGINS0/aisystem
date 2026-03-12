package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.util.java.CollectionUtils;
import com.nisovin.shopkeepers.util.java.PredicateUtils;
import java.util.List;
import java.util.function.Predicate;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.checkerframework.checker.nullness.qual.NonNull;

public class RegistryUtils {
   public static NamespacedKey getKeyOrThrow(Keyed keyed) {
      return keyed.getKey();
   }

   public static <T extends Keyed> List<T> getValues(Registry<T> registry) {
      return registry.stream().toList();
   }

   public static <T extends Keyed> List<NamespacedKey> getKeys(Registry<T> registry) {
      return registry.stream().map(Keyed::getKey).toList();
   }

   @NonNull
   public static <T extends Keyed> T cycleKeyed(Class<T> type, @NonNull T current, boolean backwards) {
      return cycleKeyed(type, current, backwards, PredicateUtils.alwaysTrue());
   }

   public static <T extends Keyed> T cycleKeyed(Class<T> type, T current, boolean backwards, Predicate<? super T> predicate) {
      Registry<T> registry = Compat.getProvider().getRegistry(type);
      List<T> values = getValues(registry);
      return (Keyed)CollectionUtils.cycleValue(values, current, backwards, predicate);
   }

   private RegistryUtils() {
   }
}
