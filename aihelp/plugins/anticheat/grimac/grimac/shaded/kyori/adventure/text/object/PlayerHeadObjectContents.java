package ac.grim.grimac.shaded.kyori.adventure.text.object;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.Unmodifiable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.util.PlatformAPI;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@ApiStatus.NonExtendable
public interface PlayerHeadObjectContents extends ObjectContents {
   boolean DEFAULT_HAT = true;

   @Nullable
   String name();

   @Nullable
   UUID id();

   @NotNull
   @Unmodifiable
   List<PlayerHeadObjectContents.ProfileProperty> profileProperties();

   boolean hat();

   @Nullable
   Key texture();

   @Contract(
      value = "-> new",
      pure = true
   )
   @NotNull
   PlayerHeadObjectContents.Builder toBuilder();

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   static PlayerHeadObjectContents.ProfileProperty property(@NotNull final String name, @NotNull final String value) {
      return new PlayerHeadObjectContentsImpl.ProfilePropertyImpl((String)Objects.requireNonNull(name, "name"), (String)Objects.requireNonNull(value, "value"), (String)null);
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   static PlayerHeadObjectContents.ProfileProperty property(@NotNull final String name, @NotNull final String value, @Nullable final String signature) {
      return new PlayerHeadObjectContentsImpl.ProfilePropertyImpl((String)Objects.requireNonNull(name, "name"), (String)Objects.requireNonNull(value, "value"), signature);
   }

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("name", this.name()), ExaminableProperty.of("id", (Object)this.id()), ExaminableProperty.of("profileProperties", (Object)this.profileProperties()), ExaminableProperty.of("hat", this.hat()), ExaminableProperty.of("texture", (Object)this.texture()));
   }

   public interface SkinSource {
      @PlatformAPI
      @ApiStatus.Internal
      void applySkinToPlayerHeadContents(@NotNull PlayerHeadObjectContents.Builder builder);
   }

   public interface Builder {
      @Contract("_ -> this")
      @NotNull
      PlayerHeadObjectContents.Builder name(@Nullable final String name);

      @Contract("_ -> this")
      @NotNull
      PlayerHeadObjectContents.Builder id(@Nullable final UUID id);

      @Contract("_ -> this")
      @NotNull
      PlayerHeadObjectContents.Builder profileProperty(@NotNull final PlayerHeadObjectContents.ProfileProperty property);

      @Contract("_ -> this")
      @NotNull
      PlayerHeadObjectContents.Builder profileProperties(@NotNull final Collection<PlayerHeadObjectContents.ProfileProperty> properties);

      @Contract("_ -> this")
      @NotNull
      PlayerHeadObjectContents.Builder skin(@NotNull final PlayerHeadObjectContents.SkinSource skinSource);

      @Contract("_ -> this")
      @NotNull
      PlayerHeadObjectContents.Builder hat(final boolean hat);

      @Contract("_ -> this")
      @NotNull
      PlayerHeadObjectContents.Builder texture(@Nullable final Key texture);

      @Contract(
         value = "-> new",
         pure = true
      )
      @NotNull
      PlayerHeadObjectContents build();
   }

   public interface ProfileProperty extends Examinable {
      @NotNull
      String name();

      @NotNull
      String value();

      @Nullable
      String signature();

      @NotNull
      default Stream<? extends ExaminableProperty> examinableProperties() {
         return Stream.of(ExaminableProperty.of("name", this.name()), ExaminableProperty.of("value", this.value()), ExaminableProperty.of("signature", this.signature()));
      }
   }
}
