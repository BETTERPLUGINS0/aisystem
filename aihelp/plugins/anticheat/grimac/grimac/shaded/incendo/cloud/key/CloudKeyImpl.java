package ac.grim.grimac.shaded.incendo.cloud.key;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import java.util.Objects;
import javax.annotation.CheckReturnValue;
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
   from = "CloudKey",
   generator = "Immutables"
)
@Immutable
final class CloudKeyImpl<T> extends CloudKey<T> {
   @NonNull
   private final String name;
   @NonNull
   private final TypeToken<T> type;

   private CloudKeyImpl(@NonNull String name, @NonNull TypeToken<T> type) {
      this.name = (String)Objects.requireNonNull(name, "name");
      this.type = (TypeToken)Objects.requireNonNull(type, "type");
   }

   private CloudKeyImpl(CloudKeyImpl<T> original, @NonNull String name, @NonNull TypeToken<T> type) {
      this.name = name;
      this.type = type;
   }

   @NonNull
   public String name() {
      return this.name;
   }

   @NonNull
   public TypeToken<T> type() {
      return this.type;
   }

   public final CloudKeyImpl<T> withName(String value) {
      String newValue = (String)Objects.requireNonNull(value, "name");
      return this.name.equals(newValue) ? this : new CloudKeyImpl(this, newValue, this.type);
   }

   public final CloudKeyImpl<T> withType(TypeToken<T> value) {
      if (this.type == value) {
         return this;
      } else {
         TypeToken<T> newValue = (TypeToken)Objects.requireNonNull(value, "type");
         return new CloudKeyImpl(this, this.name, newValue);
      }
   }

   public String toString() {
      return "CloudKey{name=" + this.name + ", type=" + this.type + "}";
   }

   public static <T> CloudKeyImpl<T> of(@NonNull String name, @NonNull TypeToken<T> type) {
      return new CloudKeyImpl(name, type);
   }

   public static <T> CloudKeyImpl<T> copyOf(CloudKey<T> instance) {
      return instance instanceof CloudKeyImpl ? (CloudKeyImpl)instance : of(instance.name(), instance.type());
   }
}
