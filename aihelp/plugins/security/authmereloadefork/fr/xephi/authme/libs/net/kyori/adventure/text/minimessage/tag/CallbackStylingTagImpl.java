package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.Style;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

final class CallbackStylingTagImpl extends AbstractTag implements Inserting {
   private final Consumer<Style.Builder> styles;

   CallbackStylingTagImpl(final Consumer<Style.Builder> styles) {
      this.styles = styles;
   }

   @NotNull
   public Component value() {
      return Component.text("", Style.style(this.styles));
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.styles});
   }

   public boolean equals(final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof CallbackStylingTagImpl)) {
         return false;
      } else {
         CallbackStylingTagImpl that = (CallbackStylingTagImpl)other;
         return Objects.equals(this.styles, that.styles);
      }
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("styles", (Object)this.styles));
   }
}
