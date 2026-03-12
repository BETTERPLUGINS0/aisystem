package ac.grim.grimac.shaded.kyori.adventure.title;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.UnknownNullability;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.util.Ticks;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import java.time.Duration;

@ApiStatus.NonExtendable
public interface Title extends Examinable {
   Title.Times DEFAULT_TIMES = Title.Times.times(Ticks.duration(10L), Ticks.duration(70L), Ticks.duration(20L));

   @NotNull
   static Title title(@NotNull final Component title, @NotNull final Component subtitle) {
      return title(title, subtitle, DEFAULT_TIMES);
   }

   @NotNull
   static Title title(@NotNull final Component title, @NotNull final Component subtitle, @Nullable final Title.Times times) {
      return new TitleImpl(title, subtitle, times);
   }

   @NotNull
   static Title title(@NotNull final Component title, @NotNull final Component subtitle, final int fadeInTicks, final int stayTicks, final int fadeOutTicks) {
      return new TitleImpl(title, subtitle, Title.Times.times(Ticks.duration((long)fadeInTicks), Ticks.duration((long)stayTicks), Ticks.duration((long)fadeOutTicks)));
   }

   @NotNull
   Component title();

   @NotNull
   Component subtitle();

   @Nullable
   Title.Times times();

   @UnknownNullability
   <T> T part(@NotNull final TitlePart<T> part);

   public interface Times extends Examinable {
      /** @deprecated */
      @Deprecated
      @ApiStatus.ScheduledForRemoval(
         inVersion = "5.0.0"
      )
      @NotNull
      static Title.Times of(@NotNull final Duration fadeIn, @NotNull final Duration stay, @NotNull final Duration fadeOut) {
         return times(fadeIn, stay, fadeOut);
      }

      @NotNull
      static Title.Times times(@NotNull final Duration fadeIn, @NotNull final Duration stay, @NotNull final Duration fadeOut) {
         return new TitleImpl.TimesImpl(fadeIn, stay, fadeOut);
      }

      @NotNull
      Duration fadeIn();

      @NotNull
      Duration stay();

      @NotNull
      Duration fadeOut();
   }
}
