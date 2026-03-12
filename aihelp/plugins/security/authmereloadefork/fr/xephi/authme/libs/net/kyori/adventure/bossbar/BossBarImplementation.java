package fr.xephi.authme.libs.net.kyori.adventure.bossbar;

import java.util.Collections;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

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
