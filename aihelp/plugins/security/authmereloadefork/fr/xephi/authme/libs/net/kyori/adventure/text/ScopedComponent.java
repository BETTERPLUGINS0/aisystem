package fr.xephi.authme.libs.net.kyori.adventure.text;

import fr.xephi.authme.libs.net.kyori.adventure.text.event.ClickEvent;
import fr.xephi.authme.libs.net.kyori.adventure.text.event.HoverEventSource;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.Style;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.TextColor;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.TextDecoration;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ScopedComponent<C extends Component> extends Component {
   @NotNull
   C children(@NotNull final List<? extends ComponentLike> children);

   @NotNull
   C style(@NotNull final Style style);

   @NotNull
   default C style(@NotNull final Consumer<Style.Builder> style) {
      return Component.super.style(style);
   }

   @NotNull
   default C style(@NotNull final Style.Builder style) {
      return Component.super.style(style);
   }

   @NotNull
   default C mergeStyle(@NotNull final Component that) {
      return Component.super.mergeStyle(that);
   }

   @NotNull
   default C mergeStyle(@NotNull final Component that, @NotNull final Style.Merge... merges) {
      return Component.super.mergeStyle(that, merges);
   }

   @NotNull
   default C append(@NotNull final Component component) {
      return Component.super.append(component);
   }

   @NotNull
   default C append(@NotNull final ComponentLike like) {
      return Component.super.append(like);
   }

   @NotNull
   default C append(@NotNull final ComponentBuilder<?, ?> builder) {
      return Component.super.append(builder);
   }

   @NotNull
   default C mergeStyle(@NotNull final Component that, @NotNull final Set<Style.Merge> merges) {
      return Component.super.mergeStyle(that, merges);
   }

   @NotNull
   default C color(@Nullable final TextColor color) {
      return Component.super.color(color);
   }

   @NotNull
   default C colorIfAbsent(@Nullable final TextColor color) {
      return Component.super.colorIfAbsent(color);
   }

   @NotNull
   default C decorate(@NotNull final TextDecoration decoration) {
      return Component.super.decorate(decoration);
   }

   @NotNull
   default C decoration(@NotNull final TextDecoration decoration, final boolean flag) {
      return Component.super.decoration(decoration, flag);
   }

   @NotNull
   default C decoration(@NotNull final TextDecoration decoration, @NotNull final TextDecoration.State state) {
      return Component.super.decoration(decoration, state);
   }

   @NotNull
   default C clickEvent(@Nullable final ClickEvent event) {
      return Component.super.clickEvent(event);
   }

   @NotNull
   default C hoverEvent(@Nullable final HoverEventSource<?> event) {
      return Component.super.hoverEvent(event);
   }

   @NotNull
   default C insertion(@Nullable final String insertion) {
      return Component.super.insertion(insertion);
   }
}
