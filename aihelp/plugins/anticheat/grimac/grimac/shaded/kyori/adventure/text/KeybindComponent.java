package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.stream.Stream;

public interface KeybindComponent extends BuildableComponent<KeybindComponent, KeybindComponent.Builder>, ScopedComponent<KeybindComponent> {
   @NotNull
   String keybind();

   @Contract(
      pure = true
   )
   @NotNull
   KeybindComponent keybind(@NotNull final String keybind);

   @Contract(
      pure = true
   )
   @NotNull
   default KeybindComponent keybind(@NotNull final KeybindComponent.KeybindLike keybind) {
      return this.keybind(((KeybindComponent.KeybindLike)Objects.requireNonNull(keybind, "keybind")).asKeybind());
   }

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.concat(Stream.of(ExaminableProperty.of("keybind", this.keybind())), BuildableComponent.super.examinableProperties());
   }

   public interface KeybindLike {
      @NotNull
      String asKeybind();
   }

   public interface Builder extends ComponentBuilder<KeybindComponent, KeybindComponent.Builder> {
      @Contract("_ -> this")
      @NotNull
      KeybindComponent.Builder keybind(@NotNull final String keybind);

      @Contract(
         pure = true
      )
      @NotNull
      default KeybindComponent.Builder keybind(@NotNull final KeybindComponent.KeybindLike keybind) {
         return this.keybind(((KeybindComponent.KeybindLike)Objects.requireNonNull(keybind, "keybind")).asKeybind());
      }
   }
}
