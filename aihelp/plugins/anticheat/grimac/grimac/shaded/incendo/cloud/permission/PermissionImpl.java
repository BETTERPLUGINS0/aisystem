package ac.grim.grimac.shaded.incendo.cloud.permission;

import java.util.Objects;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Generated;

@ParametersAreNonnullByDefault
@CheckReturnValue
@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
@Generated(
   from = "Permission",
   generator = "Immutables"
)
@Immutable
final class PermissionImpl implements Permission {
   @NonNull
   private final String permissionString;

   private PermissionImpl(@NonNull String permissionString) {
      this.permissionString = (String)Objects.requireNonNull(permissionString, "permissionString");
   }

   private PermissionImpl(PermissionImpl original, @NonNull String permissionString) {
      this.permissionString = permissionString;
   }

   @NonNull
   public String permissionString() {
      return this.permissionString;
   }

   public final PermissionImpl withPermissionString(String value) {
      String newValue = (String)Objects.requireNonNull(value, "permissionString");
      return this.permissionString.equals(newValue) ? this : new PermissionImpl(this, newValue);
   }

   public boolean equals(@Nullable Object another) {
      if (this == another) {
         return true;
      } else {
         return another instanceof PermissionImpl && this.equalTo(0, (PermissionImpl)another);
      }
   }

   private boolean equalTo(int synthetic, PermissionImpl another) {
      return this.permissionString.equals(another.permissionString);
   }

   public int hashCode() {
      int h = 5381;
      int h = h + (h << 5) + this.permissionString.hashCode();
      return h;
   }

   public String toString() {
      return "Permission{permissionString=" + this.permissionString + "}";
   }

   public static PermissionImpl of(@NonNull String permissionString) {
      return new PermissionImpl(permissionString);
   }

   public static PermissionImpl copyOf(Permission instance) {
      return instance instanceof PermissionImpl ? (PermissionImpl)instance : of(instance.permissionString());
   }
}
