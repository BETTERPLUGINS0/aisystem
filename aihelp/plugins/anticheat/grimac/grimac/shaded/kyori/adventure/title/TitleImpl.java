package ac.grim.grimac.shaded.kyori.adventure.title;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.UnknownNullability;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.time.Duration;
import java.util.Objects;
import java.util.stream.Stream;

final class TitleImpl implements Title {
   private final Component title;
   private final Component subtitle;
   @Nullable
   private final Title.Times times;

   TitleImpl(@NotNull final Component title, @NotNull final Component subtitle, @Nullable final Title.Times times) {
      this.title = (Component)Objects.requireNonNull(title, "title");
      this.subtitle = (Component)Objects.requireNonNull(subtitle, "subtitle");
      this.times = times;
   }

   @NotNull
   public Component title() {
      return this.title;
   }

   @NotNull
   public Component subtitle() {
      return this.subtitle;
   }

   @Nullable
   public Title.Times times() {
      return this.times;
   }

   @UnknownNullability
   public <T> T part(@NotNull final TitlePart<T> part) {
      Objects.requireNonNull(part, "part");
      if (part == TitlePart.TITLE) {
         return this.title;
      } else if (part == TitlePart.SUBTITLE) {
         return this.subtitle;
      } else if (part == TitlePart.TIMES) {
         return this.times;
      } else {
         throw new IllegalArgumentException("Don't know what " + part + " is.");
      }
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         TitleImpl that = (TitleImpl)other;
         return this.title.equals(that.title) && this.subtitle.equals(that.subtitle) && Objects.equals(this.times, that.times);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.title.hashCode();
      result = 31 * result + this.subtitle.hashCode();
      result = 31 * result + Objects.hashCode(this.times);
      return result;
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("title", (Object)this.title), ExaminableProperty.of("subtitle", (Object)this.subtitle), ExaminableProperty.of("times", (Object)this.times));
   }

   public String toString() {
      return Internals.toString(this);
   }

   static class TimesImpl implements Title.Times {
      private final Duration fadeIn;
      private final Duration stay;
      private final Duration fadeOut;

      TimesImpl(@NotNull final Duration fadeIn, @NotNull final Duration stay, @NotNull final Duration fadeOut) {
         this.fadeIn = (Duration)Objects.requireNonNull(fadeIn, "fadeIn");
         this.stay = (Duration)Objects.requireNonNull(stay, "stay");
         this.fadeOut = (Duration)Objects.requireNonNull(fadeOut, "fadeOut");
      }

      @NotNull
      public Duration fadeIn() {
         return this.fadeIn;
      }

      @NotNull
      public Duration stay() {
         return this.stay;
      }

      @NotNull
      public Duration fadeOut() {
         return this.fadeOut;
      }

      public boolean equals(@Nullable final Object other) {
         if (this == other) {
            return true;
         } else if (!(other instanceof TitleImpl.TimesImpl)) {
            return false;
         } else {
            TitleImpl.TimesImpl that = (TitleImpl.TimesImpl)other;
            return this.fadeIn.equals(that.fadeIn) && this.stay.equals(that.stay) && this.fadeOut.equals(that.fadeOut);
         }
      }

      public int hashCode() {
         int result = this.fadeIn.hashCode();
         result = 31 * result + this.stay.hashCode();
         result = 31 * result + this.fadeOut.hashCode();
         return result;
      }

      @NotNull
      public Stream<? extends ExaminableProperty> examinableProperties() {
         return Stream.of(ExaminableProperty.of("fadeIn", (Object)this.fadeIn), ExaminableProperty.of("stay", (Object)this.stay), ExaminableProperty.of("fadeOut", (Object)this.fadeOut));
      }

      public String toString() {
         return Internals.toString(this);
      }
   }
}
