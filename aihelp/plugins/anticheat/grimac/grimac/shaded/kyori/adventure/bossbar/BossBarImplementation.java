package ac.grim.grimac.shaded.kyori.adventure.bossbar;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.Collections;

@ApiStatus.Internal
public interface BossBarImplementation {
   @ApiStatus.Internal
   @NotNull
   static <I extends BossBarImplementation> I get(@NotNull final BossBar bar, @NotNull final Class<I> type) {
      return BossBarImpl.ImplementationAccessor.get(bar, type);
   }

   @ApiStatus.Internal
   @NotNull
   default Iterable<? extends BossBarViewer> viewers() {
      return Collections.emptyList();
   }

   @ApiStatus.Internal
   public interface Provider {
      @ApiStatus.Internal
      @NotNull
      BossBarImplementation create(@NotNull final BossBar bar);
   }
}
