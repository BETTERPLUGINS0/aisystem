package fr.xephi.authme.libs.net.kyori.adventure.text.renderer;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public interface ComponentRenderer<C> {
   @NotNull
   Component render(@NotNull final Component component, @NotNull final C context);

   default <T> ComponentRenderer<T> mapContext(final Function<T, C> transformer) {
      return (component, ctx) -> {
         return this.render(component, transformer.apply(ctx));
      };
   }
}
