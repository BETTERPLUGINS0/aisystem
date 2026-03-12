package ac.grim.grimac.platform.api.manager;

import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public interface ItemResetHandler {
   void resetItemUsage(@Nullable PlatformPlayer var1);

   @Contract("null -> null")
   @Nullable
   InteractionHand getItemUsageHand(@Nullable PlatformPlayer var1);

   @Contract("null -> false")
   boolean isUsingItem(@Nullable PlatformPlayer var1);
}
