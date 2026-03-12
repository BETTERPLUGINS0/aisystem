package fr.xephi.authme.libs.net.kyori.adventure.text.format;

import fr.xephi.authme.libs.net.kyori.adventure.internal.Internals;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TextDecorationAndStateImpl implements TextDecorationAndState {
   private final TextDecoration decoration;
   private final TextDecoration.State state;

   TextDecorationAndStateImpl(final TextDecoration decoration, final TextDecoration.State state) {
      this.decoration = decoration;
      this.state = (TextDecoration.State)Objects.requireNonNull(state, "state");
   }

   @NotNull
   public TextDecoration decoration() {
      return this.decoration;
   }

   @NotNull
   public TextDecoration.State state() {
      return this.state;
   }

   public String toString() {
      return Internals.toString(this);
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         TextDecorationAndStateImpl that = (TextDecorationAndStateImpl)other;
         return this.decoration == that.decoration && this.state == that.state;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.decoration.hashCode();
      result = 31 * result + this.state.hashCode();
      return result;
   }
}
