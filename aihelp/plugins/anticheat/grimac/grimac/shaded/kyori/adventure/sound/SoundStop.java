package ac.grim.grimac.shaded.kyori.adventure.sound;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import java.util.Objects;
import java.util.function.Supplier;

@ApiStatus.NonExtendable
public interface SoundStop extends Examinable {
   @NotNull
   static SoundStop all() {
      return SoundStopImpl.ALL;
   }

   @NotNull
   static SoundStop named(@NotNull final Key sound) {
      Objects.requireNonNull(sound, "sound");
      return new SoundStopImpl((Sound.Source)null) {
         @NotNull
         public Key sound() {
            return sound;
         }
      };
   }

   @NotNull
   static SoundStop named(@NotNull final Sound.Type sound) {
      Objects.requireNonNull(sound, "sound");
      return new SoundStopImpl((Sound.Source)null) {
         @NotNull
         public Key sound() {
            return sound.key();
         }
      };
   }

   @NotNull
   static SoundStop named(@NotNull final Supplier<? extends Sound.Type> sound) {
      Objects.requireNonNull(sound, "sound");
      return new SoundStopImpl((Sound.Source)null) {
         @NotNull
         public Key sound() {
            return ((Sound.Type)sound.get()).key();
         }
      };
   }

   @NotNull
   static SoundStop source(@NotNull final Sound.Source source) {
      Objects.requireNonNull(source, "source");
      return new SoundStopImpl(source) {
         @Nullable
         public Key sound() {
            return null;
         }
      };
   }

   @NotNull
   static SoundStop namedOnSource(@NotNull final Key sound, @NotNull final Sound.Source source) {
      Objects.requireNonNull(sound, "sound");
      Objects.requireNonNull(source, "source");
      return new SoundStopImpl(source) {
         @NotNull
         public Key sound() {
            return sound;
         }
      };
   }

   @NotNull
   static SoundStop namedOnSource(@NotNull final Sound.Type sound, @NotNull final Sound.Source source) {
      Objects.requireNonNull(sound, "sound");
      return namedOnSource(sound.key(), source);
   }

   @NotNull
   static SoundStop namedOnSource(@NotNull final Supplier<? extends Sound.Type> sound, @NotNull final Sound.Source source) {
      Objects.requireNonNull(sound, "sound");
      Objects.requireNonNull(source, "source");
      return new SoundStopImpl(source) {
         @NotNull
         public Key sound() {
            return ((Sound.Type)sound.get()).key();
         }
      };
   }

   @Nullable
   Key sound();

   @Nullable
   Sound.Source source();
}
