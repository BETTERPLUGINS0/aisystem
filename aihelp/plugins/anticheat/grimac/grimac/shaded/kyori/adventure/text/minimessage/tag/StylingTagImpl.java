package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.format.StyleBuilderApplicable;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.Arrays;
import java.util.stream.Stream;

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
