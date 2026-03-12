package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEventSource;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.format.StyleBuilderApplicable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextDecoration;
import ac.grim.grimac.shaded.kyori.adventure.util.ARGBLike;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public interface ScopedComponent<C extends Component> extends Component {
   @NotNull
   default C asComponent() {
      return Component.super.asComponent();
   }

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
   default C style(@NotNull final Consumer<Style.Builder> consumer, @NotNull final Style.Merge.Strategy strategy) {
      return Component.super.style(consumer, strategy);
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
   default C append(@NotNull final List<? extends ComponentLike> components) {
      return Component.super.append(components);
   }

   @NotNull
   default C append(@NotNull final ComponentLike... components) {
      return Component.super.append(components);
   }

   @NotNull
   default C appendNewline() {
      return Component.super.appendNewline();
   }

   @NotNull
   default C appendSpace() {
      return Component.super.appendSpace();
   }

   @NotNull
   default C applyFallbackStyle(@NotNull final StyleBuilderApplicable... style) {
      return Component.super.applyFallbackStyle(style);
   }

   @NotNull
   default C applyFallbackStyle(@NotNull final Style style) {
      return Component.super.applyFallbackStyle(style);
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
   default C shadowColor(@Nullable final ARGBLike argb) {
      return Component.super.shadowColor(argb);
   }

   @NotNull
   default C shadowColorIfAbsent(@Nullable final ARGBLike argb) {
      return Component.super.shadowColorIfAbsent(argb);
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
   default C decorationIfAbsent(@NotNull final TextDecoration decoration, @NotNull final TextDecoration.State state) {
      return Component.super.decorationIfAbsent(decoration, state);
   }

   @NotNull
   default C decorations(@NotNull final Map<TextDecoration, TextDecoration.State> decorations) {
      return Component.super.decorations(decorations);
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

   @NotNull
   default C font(@Nullable final Key key) {
      return Component.super.font(key);
   }
}
