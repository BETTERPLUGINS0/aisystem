package ac.grim.grimac.shaded.incendo.cloud.description;

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
   from = "Description",
   generator = "Immutables"
)
@Immutable
final class DescriptionImpl implements Description {
   @NonNull
   private final String textDescription;

   private DescriptionImpl(@NonNull String textDescription) {
      this.textDescription = (String)Objects.requireNonNull(textDescription, "textDescription");
   }

   private DescriptionImpl(DescriptionImpl original, @NonNull String textDescription) {
      this.textDescription = textDescription;
   }

   @NonNull
   public String textDescription() {
      return this.textDescription;
   }

   public final DescriptionImpl withTextDescription(String value) {
      String newValue = (String)Objects.requireNonNull(value, "textDescription");
      return this.textDescription.equals(newValue) ? this : new DescriptionImpl(this, newValue);
   }

   public boolean equals(@Nullable Object another) {
      if (this == another) {
         return true;
      } else {
         return another instanceof DescriptionImpl && this.equalTo(0, (DescriptionImpl)another);
      }
   }

   private boolean equalTo(int synthetic, DescriptionImpl another) {
      return this.textDescription.equals(another.textDescription);
   }

   public int hashCode() {
      int h = 5381;
      int h = h + (h << 5) + this.textDescription.hashCode();
      return h;
   }

   public String toString() {
      return "Description{textDescription=" + this.textDescription + "}";
   }

   public static DescriptionImpl of(@NonNull String textDescription) {
      return new DescriptionImpl(textDescription);
   }

   public static DescriptionImpl copyOf(Description instance) {
      return instance instanceof DescriptionImpl ? (DescriptionImpl)instance : of(instance.textDescription());
   }
}
