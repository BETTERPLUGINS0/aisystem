package fr.xephi.authme.libs.net.kyori.option;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public interface Option<V> {
   static Option<Boolean> booleanOption(final String id, final boolean defaultValue) {
      return OptionImpl.option(id, Boolean.class, defaultValue);
   }

   static <E extends Enum<E>> Option<E> enumOption(final String id, final Class<E> enumClazz, final E defaultValue) {
      return OptionImpl.option(id, enumClazz, defaultValue);
   }

   @NotNull
   String id();

   @NotNull
   Class<V> type();

   @Nullable
   V defaultValue();
}
