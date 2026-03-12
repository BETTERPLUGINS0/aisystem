package ac.grim.grimac.shaded.kyori.adventure.util;

import ac.grim.grimac.shaded.jetbrains.annotations.CheckReturnValue;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.builder.AbstractBuilder;

public interface InheritanceAwareMap<C, V> {
   @NotNull
   static <K, E> InheritanceAwareMap<K, E> empty() {
      return InheritanceAwareMapImpl.EMPTY;
   }

   @NotNull
   static <K, E> InheritanceAwareMap.Builder<K, E> builder() {
      return new InheritanceAwareMapImpl.BuilderImpl();
   }

   @NotNull
   static <K, E> InheritanceAwareMap.Builder<K, E> builder(final InheritanceAwareMap<? extends K, ? extends E> existing) {
      return (new InheritanceAwareMapImpl.BuilderImpl()).putAll(existing);
   }

   boolean containsKey(@NotNull final Class<? extends C> clazz);

   @Nullable
   V get(@NotNull final Class<? extends C> clazz);

   @CheckReturnValue
   @NotNull
   InheritanceAwareMap<C, V> with(@NotNull final Class<? extends C> clazz, @NotNull final V value);

   @CheckReturnValue
   @NotNull
   InheritanceAwareMap<C, V> without(@NotNull final Class<? extends C> clazz);

   public interface Builder<C, V> extends AbstractBuilder<InheritanceAwareMap<C, V>> {
      @NotNull
      InheritanceAwareMap.Builder<C, V> strict(final boolean strict);

      @NotNull
      InheritanceAwareMap.Builder<C, V> put(@NotNull final Class<? extends C> clazz, @NotNull final V value);

      @NotNull
      InheritanceAwareMap.Builder<C, V> remove(@NotNull final Class<? extends C> clazz);

      @NotNull
      InheritanceAwareMap.Builder<C, V> putAll(@NotNull final InheritanceAwareMap<? extends C, ? extends V> map);
   }
}
