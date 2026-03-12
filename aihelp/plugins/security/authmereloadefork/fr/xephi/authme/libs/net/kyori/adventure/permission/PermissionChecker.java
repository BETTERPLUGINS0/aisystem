package fr.xephi.authme.libs.net.kyori.adventure.permission;

import fr.xephi.authme.libs.net.kyori.adventure.key.Key;
import fr.xephi.authme.libs.net.kyori.adventure.pointer.Pointer;
import fr.xephi.authme.libs.net.kyori.adventure.util.TriState;
import java.util.Objects;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

public interface PermissionChecker extends Predicate<String> {
   Pointer<PermissionChecker> POINTER = Pointer.pointer(PermissionChecker.class, Key.key("adventure", "permission"));

   @NotNull
   static PermissionChecker always(@NotNull final TriState state) {
      Objects.requireNonNull(state);
      if (state == TriState.TRUE) {
         return PermissionCheckers.TRUE;
      } else {
         return state == TriState.FALSE ? PermissionCheckers.FALSE : PermissionCheckers.NOT_SET;
      }
   }

   @NotNull
   TriState value(@NotNull final String permission);

   default boolean test(@NotNull final String permission) {
      return this.value(permission) == TriState.TRUE;
   }
}
