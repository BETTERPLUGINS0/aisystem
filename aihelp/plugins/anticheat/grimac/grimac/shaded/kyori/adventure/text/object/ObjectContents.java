package ac.grim.grimac.shaded.kyori.adventure.text.object;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@ApiStatus.NonExtendable
public interface ObjectContents extends Examinable {
   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static SpriteObjectContents sprite(@NotNull final Key atlas, @NotNull final Key sprite) {
      return new SpriteObjectContentsImpl((Key)Objects.requireNonNull(atlas, "atlas"), (Key)Objects.requireNonNull(sprite, "sprite"));
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static SpriteObjectContents sprite(@NotNull final Key sprite) {
      return new SpriteObjectContentsImpl(SpriteObjectContents.DEFAULT_ATLAS, (Key)Objects.requireNonNull(sprite, "sprite"));
   }

   @Contract(
      value = "-> new",
      pure = true
   )
   @NotNull
   static PlayerHeadObjectContents.Builder playerHead() {
      return new PlayerHeadObjectContentsImpl.BuilderImpl();
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static PlayerHeadObjectContents playerHead(@NotNull final String name) {
      return new PlayerHeadObjectContentsImpl(name, (UUID)null, Collections.emptyList(), true, (Key)null);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static PlayerHeadObjectContents playerHead(@NotNull final UUID id) {
      return new PlayerHeadObjectContentsImpl((String)null, id, Collections.emptyList(), true, (Key)null);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static PlayerHeadObjectContents playerHead(@NotNull final PlayerHeadObjectContents.SkinSource skinSource) {
      return playerHead().skin(skinSource).build();
   }
}
