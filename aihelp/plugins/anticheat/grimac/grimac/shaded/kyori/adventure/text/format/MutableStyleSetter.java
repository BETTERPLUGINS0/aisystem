package ac.grim.grimac.shaded.kyori.adventure.text.format;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;

@ApiStatus.NonExtendable
public interface MutableStyleSetter<T extends MutableStyleSetter<?>> extends StyleSetter<T> {
   @Contract("_ -> this")
   @NotNull
   default T decorate(@NotNull final TextDecoration... decorations) {
      int i = 0;

      for(int length = decorations.length; i < length; ++i) {
         this.decorate((TextDecoration)decorations[i]);
      }

      return this;
   }

   @Contract("_ -> this")
   @NotNull
   default T decorations(@NotNull final Map<TextDecoration, TextDecoration.State> decorations) {
      Objects.requireNonNull(decorations, "decorations");
      Iterator var2 = decorations.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<TextDecoration, TextDecoration.State> entry = (Entry)var2.next();
         this.decoration((TextDecoration)entry.getKey(), (TextDecoration.State)entry.getValue());
      }

      return this;
   }

   @Contract("_, _ -> this")
   @NotNull
   default T decorations(@NotNull final Set<TextDecoration> decorations, final boolean flag) {
      TextDecoration.State state = TextDecoration.State.byBoolean(flag);
      decorations.forEach((decoration) -> {
         this.decoration(decoration, state);
      });
      return this;
   }
}
