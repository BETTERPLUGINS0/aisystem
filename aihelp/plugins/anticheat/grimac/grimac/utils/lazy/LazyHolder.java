package ac.grim.grimac.utils.lazy;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.function.Supplier;

public interface LazyHolder<T> {
   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static <T> LazyHolder<T> threadSafe(Supplier<T> supplier) {
      return new ThreadSafeLazyHolder(supplier);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static <T> LazyHolder<T> simple(Supplier<T> supplier) {
      return new SimpleLazyHolder(supplier);
   }

   T get();
}
