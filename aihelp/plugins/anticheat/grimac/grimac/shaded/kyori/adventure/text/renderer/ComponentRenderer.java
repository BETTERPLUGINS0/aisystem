package ac.grim.grimac.shaded.kyori.adventure.text.renderer;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.function.Function;

public interface ComponentRenderer<C> {
   @NotNull
   Component render(@NotNull final Component component, @NotNull final C context);

   default <T> ComponentRenderer<T> mapContext(final Function<T, C> transformer) {
      return (component, ctx) -> {
         return this.render(component, transformer.apply(ctx));
      };
   }
}
