package me.PM2.infinitevehicles.xseries.base;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface XBase<XForm extends XBase<XForm, BukkitForm>, BukkitForm> {
   @NotNull
   @Contract(
      pure = true
   )
   String name();

   @Internal
   @Contract(
      pure = true
   )
   String[] getNames();

   @NotNull
   @Contract(
      pure = true
   )
   default String friendlyName() {
      return (String)Arrays.stream(this.name().split("_")).map((t) -> {
         return t.charAt(0) + t.substring(1).toLowerCase(Locale.ENGLISH);
      }).collect(Collectors.joining(" "));
   }

   @Nullable
   @Contract(
      pure = true
   )
   BukkitForm get();

   @Contract(
      pure = true
   )
   default boolean isSupported() {
      return this.get() != null;
   }

   @NotNull
   @Contract(
      pure = true
   )
   default XForm or(XForm other) {
      return this.isSupported() ? this : other;
   }

   @Internal
   default XModuleMetadata getMetadata() {
      XRegistry<XForm, BukkitForm> registry = XRegistry.registryOf(this.getClass());
      return registry.getOrRegisterMetadata(this, registry.getBackingField(this), false);
   }
}
