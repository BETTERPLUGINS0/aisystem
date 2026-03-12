package ac.grim.grimac.shaded.kyori.adventure.pointer;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.UnknownNullability;
import java.util.Optional;
import java.util.function.Supplier;

public interface Pointered {
   @NotNull
   default <T> Optional<T> get(@NotNull final Pointer<T> pointer) {
      return this.pointers().get(pointer);
   }

   @Contract("_, null -> _; _, !null -> !null")
   @Nullable
   default <T> T getOrDefault(@NotNull final Pointer<T> pointer, @Nullable final T defaultValue) {
      return this.pointers().getOrDefault(pointer, defaultValue);
   }

   @UnknownNullability
   default <T> T getOrDefaultFrom(@NotNull final Pointer<T> pointer, @NotNull final Supplier<? extends T> defaultValue) {
      return this.pointers().getOrDefaultFrom(pointer, defaultValue);
   }

   @NotNull
   default Pointers pointers() {
      return Pointers.empty();
   }
}
