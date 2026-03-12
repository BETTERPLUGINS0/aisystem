package ac.grim.grimac.shaded.incendo.cloud.permission;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Value.Immutable;

@API(
   status = Status.STABLE
)
@Immutable
public interface PermissionResult {
   boolean allowed();

   default boolean denied() {
      return !this.allowed();
   }

   @NonNull
   Permission permission();

   @NonNull
   static PermissionResult of(final boolean result, @NonNull final Permission permission) {
      return PermissionResultImpl.of(result, permission);
   }

   @NonNull
   static PermissionResult allowed(@NonNull final Permission permission) {
      return PermissionResultImpl.of(true, permission);
   }

   @NonNull
   static PermissionResult denied(@NonNull final Permission permission) {
      return PermissionResultImpl.of(false, permission);
   }
}
