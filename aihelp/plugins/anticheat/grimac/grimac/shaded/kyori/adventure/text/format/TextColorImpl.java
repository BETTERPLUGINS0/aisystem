package ac.grim.grimac.shaded.kyori.adventure.text.format;

import ac.grim.grimac.shaded.jetbrains.annotations.Debug;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.util.HSVLike;

@Debug.Renderer(
   text = "asHexString()"
)
final class TextColorImpl implements TextColor {
   private final int value;

   TextColorImpl(final int value) {
      this.value = value;
   }

   public int value() {
      return this.value;
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof TextColorImpl)) {
         return false;
      } else {
         TextColorImpl that = (TextColorImpl)other;
         return this.value == that.value;
      }
   }

   public int hashCode() {
      return this.value;
   }

   public String toString() {
      return this.asHexString();
   }

   static float distance(@NotNull final HSVLike self, @NotNull final HSVLike other) {
      float hueDistance = 3.0F * Math.min(Math.abs(self.h() - other.h()), 1.0F - Math.abs(self.h() - other.h()));
      float saturationDiff = self.s() - other.s();
      float valueDiff = self.v() - other.v();
      return hueDistance * hueDistance + saturationDiff * saturationDiff + valueDiff * valueDiff;
   }
}
