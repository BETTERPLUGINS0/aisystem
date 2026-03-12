package ac.grim.grimac.shaded.kyori.adventure.text.object;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;

@ApiStatus.NonExtendable
public interface SpriteObjectContents extends ObjectContents {
   Key DEFAULT_ATLAS = Key.key("minecraft:blocks");

   @NotNull
   Key atlas();

   @NotNull
   Key sprite();

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("atlas", (Object)this.atlas()), ExaminableProperty.of("sprite", (Object)this.sprite()));
   }
}
