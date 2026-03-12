package fr.xephi.authme.libs.net.kyori.adventure.sound;

import fr.xephi.authme.libs.net.kyori.adventure.internal.Internals;
import fr.xephi.authme.libs.net.kyori.adventure.key.Key;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class SoundStopImpl implements SoundStop {
   static final SoundStop ALL = new SoundStopImpl((Sound.Source)null) {
      @Nullable
      public Key sound() {
         return null;
      }
   };
   @Nullable
   private final Sound.Source source;

   SoundStopImpl(@Nullable final Sound.Source source) {
      this.source = source;
   }

   @Nullable
   public Sound.Source source() {
      return this.source;
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof SoundStopImpl)) {
         return false;
      } else {
         SoundStopImpl that = (SoundStopImpl)other;
         return Objects.equals(this.sound(), that.sound()) && Objects.equals(this.source, that.source);
      }
   }

   public int hashCode() {
      int result = Objects.hashCode(this.sound());
      result = 31 * result + Objects.hashCode(this.source);
      return result;
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("name", (Object)this.sound()), ExaminableProperty.of("source", (Object)this.source));
   }

   public String toString() {
      return Internals.toString(this);
   }
}
