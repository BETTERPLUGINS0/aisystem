package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.Style;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.StyleBuilderApplicable;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.Arrays;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class StylingTagImpl extends AbstractTag implements Inserting {
   private final StyleBuilderApplicable[] styles;

   StylingTagImpl(final StyleBuilderApplicable[] styles) {
      this.styles = styles;
   }

   @NotNull
   public Component value() {
      return Component.text("", Style.style(this.styles));
   }

   public int hashCode() {
      return 31 + Arrays.hashCode(this.styles);
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof StylingTagImpl)) {
         return false;
      } else {
         StylingTagImpl that = (StylingTagImpl)other;
         return Arrays.equals(this.styles, that.styles);
      }
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("styles", (Object)this.styles));
   }
}
