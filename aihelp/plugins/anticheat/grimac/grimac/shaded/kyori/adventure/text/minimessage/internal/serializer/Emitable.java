package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

@FunctionalInterface
public interface Emitable {
   void emit(@NotNull final TokenEmitter emitter);

   @Nullable
   default Component substitute() {
      return null;
   }
}
