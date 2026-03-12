package ac.grim.grimac.manager.player.features.types;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.feature.FeatureState;
import ac.grim.grimac.player.GrimPlayer;

public class ForceStuckSpeedFeature implements GrimFeature {
   public String getName() {
      return "ForceStuckSpeed";
   }

   public void setState(GrimPlayer player, ConfigManager config, FeatureState state) {
      switch(state) {
      case ENABLED:
         player.setForceStuckSpeed(true);
         break;
      case DISABLED:
         player.setForceStuckSpeed(false);
         break;
      default:
         player.setForceStuckSpeed(this.isEnabledInConfig(player, config));
      }

   }

   public boolean isEnabled(GrimPlayer player) {
      return player.isForceStuckSpeed();
   }

   public boolean isEnabledInConfig(GrimPlayer player, ConfigManager config) {
      return config.getBooleanElse("force-stuck-speed", true);
   }
}
