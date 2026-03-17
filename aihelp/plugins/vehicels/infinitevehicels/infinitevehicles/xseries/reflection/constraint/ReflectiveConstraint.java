package me.PM2.infinitevehicles.xseries.reflection.constraint;

import me.PM2.infinitevehicles.xseries.reflection.ReflectiveHandle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

@Experimental
public interface ReflectiveConstraint {
   @Contract(
      pure = true
   )
   String category();

   @Contract(
      pure = true
   )
   String name();

   @NotNull
   @Contract(
      pure = true
   )
   ReflectiveConstraint.Result appliesTo(@NotNull ReflectiveHandle<?> var1, @NotNull Object var2);

   public static enum Result {
      INCOMPATIBLE,
      NOT_MATCHED,
      MATCHED;

      @Internal
      public static ReflectiveConstraint.Result of(boolean var0) {
         return var0 ? MATCHED : NOT_MATCHED;
      }

      // $FF: synthetic method
      private static ReflectiveConstraint.Result[] $values() {
         return new ReflectiveConstraint.Result[]{INCOMPATIBLE, NOT_MATCHED, MATCHED};
      }
   }
}
