package ac.grim.grimac.shaded.kyori.adventure.identity;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import java.util.UUID;

final class IdentityImpl implements Examinable, Identity {
   private final UUID uuid;

   IdentityImpl(final UUID uuid) {
      this.uuid = uuid;
   }

   @NotNull
   public UUID uuid() {
      return this.uuid;
   }

   public String toString() {
      return Internals.toString(this);
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof Identity)) {
         return false;
      } else {
         Identity that = (Identity)other;
         return this.uuid.equals(that.uuid());
      }
   }

   public int hashCode() {
      return this.uuid.hashCode();
   }
}
