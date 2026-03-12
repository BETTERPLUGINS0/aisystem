package ac.grim.grimac.shaded.kyori.adventure.text.event;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.function.UnaryOperator;

public interface HoverEventSource<V> {
   @Nullable
   static <V> HoverEvent<V> unbox(@Nullable final HoverEventSource<V> source) {
      return source != null ? source.asHoverEvent() : null;
   }

   @NotNull
   default HoverEvent<V> asHoverEvent() {
      return this.asHoverEvent(UnaryOperator.identity());
   }

   @NotNull
   HoverEvent<V> asHoverEvent(@NotNull final UnaryOperator<V> op);
}
