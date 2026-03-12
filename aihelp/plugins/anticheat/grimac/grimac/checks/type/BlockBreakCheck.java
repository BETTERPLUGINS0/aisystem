package ac.grim.grimac.checks.type;

import ac.grim.grimac.utils.anticheat.update.BlockBreak;

public interface BlockBreakCheck extends PostPredictionCheck {
   default void onBlockBreak(BlockBreak blockBreak) {
   }

   default void onPostFlyingBlockBreak(BlockBreak blockBreak) {
   }
}
