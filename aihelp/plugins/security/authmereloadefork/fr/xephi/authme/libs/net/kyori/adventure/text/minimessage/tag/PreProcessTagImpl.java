package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag;

import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class PreProcessTagImpl extends AbstractTag implements PreProcess {
   private final String value;

   PreProcessTagImpl(final String value) {
      this.value = value;
   }

   @NotNull
   public String value() {
      return this.value;
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.value});
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof PreProcessTagImpl)) {
         return false;
      } else {
         PreProcessTagImpl that = (PreProcessTagImpl)other;
         return Objects.equals(this.value, that.value);
      }
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("value", this.value));
   }
}
