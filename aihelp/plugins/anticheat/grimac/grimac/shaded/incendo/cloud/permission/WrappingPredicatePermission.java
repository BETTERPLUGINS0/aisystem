package ac.grim.grimac.shaded.incendo.cloud.permission;

import ac.grim.grimac.shaded.incendo.cloud.key.CloudKey;
import java.util.function.Predicate;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
final class WrappingPredicatePermission<C> implements PredicatePermission<C> {
   private final CloudKey<Void> key;
   private final Predicate<C> predicate;

   WrappingPredicatePermission(@NonNull final CloudKey<Void> key, @NonNull final Predicate<C> predicate) {
      this.key = key;
      this.predicate = predicate;
   }

   @NonNull
   public PermissionResult testPermission(@NonNull final C sender) {
      return PermissionResult.of(this.predicate.test(sender), this);
   }

   @NonNull
   public CloudKey<Void> key() {
      return this.key;
   }

   public String toString() {
      return this.key.name();
   }
}
