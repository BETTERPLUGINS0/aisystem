package ac.grim.grimac.shaded.incendo.cloud.permission;

import ac.grim.grimac.shaded.incendo.cloud.key.CloudKey;
import ac.grim.grimac.shaded.incendo.cloud.key.CloudKeyHolder;
import java.util.function.Predicate;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
@API(
   status = Status.STABLE
)
public interface PredicatePermission<C> extends Permission, CloudKeyHolder<Void> {
   static <C> PredicatePermission<C> of(@NonNull final CloudKey<Void> key, @NonNull final Predicate<C> predicate) {
      return new WrappingPredicatePermission(key, predicate);
   }

   static <C> PredicatePermission<C> of(@NonNull final Predicate<C> predicate) {
      return new PredicatePermission<C>() {
         @NonNull
         public PermissionResult testPermission(@NonNull final C sender) {
            return PermissionResult.of(predicate.test(sender), this);
         }
      };
   }

   @NonNull
   default CloudKey<Void> key() {
      return CloudKey.of(this.getClass().getSimpleName());
   }

   @NonNull
   default String permissionString() {
      return this.key().name();
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   PermissionResult testPermission(@NonNull C sender);

   default boolean isEmpty() {
      return false;
   }
}
