package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public enum CreakingHeartState {
   @ApiStatus.Obsolete
   DISABLED,
   @ApiStatus.Obsolete
   ACTIVE,
   DORMANT,
   UPROOTED,
   AWAKE;

   // $FF: synthetic method
   private static CreakingHeartState[] $values() {
      return new CreakingHeartState[]{DISABLED, ACTIVE, DORMANT, UPROOTED, AWAKE};
   }
}
