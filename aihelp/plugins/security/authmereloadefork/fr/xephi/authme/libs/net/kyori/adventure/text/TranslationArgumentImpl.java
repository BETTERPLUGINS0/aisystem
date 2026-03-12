package fr.xephi.authme.libs.net.kyori.adventure.text;

import fr.xephi.authme.libs.net.kyori.adventure.internal.Internals;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TranslationArgumentImpl implements TranslationArgument {
   private static final Component TRUE = Component.text("true");
   private static final Component FALSE = Component.text("false");
   private final Object value;

   TranslationArgumentImpl(final Object value) {
      this.value = value;
   }

   @NotNull
   public Object value() {
      return this.value;
   }

   @NotNull
   public Component asComponent() {
      if (this.value instanceof Component) {
         return (Component)this.value;
      } else if (this.value instanceof Boolean) {
         return (Boolean)this.value ? TRUE : FALSE;
      } else {
         return Component.text(String.valueOf(this.value));
      }
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         TranslationArgumentImpl that = (TranslationArgumentImpl)other;
         return Objects.equals(this.value, that.value);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.value});
   }

   public String toString() {
      return Internals.toString(this);
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("value", this.value));
   }
}
