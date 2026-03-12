package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.UnknownNullability;

public interface VirtualComponentRenderer<C> {
   @UnknownNullability
   ComponentLike apply(@NotNull final C context);

   @NotNull
   default String fallbackString() {
      return "";
   }
}
