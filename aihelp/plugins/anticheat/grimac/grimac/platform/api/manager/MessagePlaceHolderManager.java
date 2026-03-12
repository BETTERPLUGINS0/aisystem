package ac.grim.grimac.platform.api.manager;

import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public interface MessagePlaceHolderManager {
   @NotNull
   String replacePlaceholders(@Nullable PlatformPlayer var1, @NotNull String var2);
}
