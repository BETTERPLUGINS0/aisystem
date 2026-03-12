package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

@ApiStatus.NonExtendable
public interface VirtualComponent extends TextComponent {
   @NotNull
   Class<?> contextType();

   @NotNull
   VirtualComponentRenderer<?> renderer();
}
