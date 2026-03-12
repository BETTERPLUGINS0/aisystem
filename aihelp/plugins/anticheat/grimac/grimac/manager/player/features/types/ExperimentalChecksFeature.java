package ac.grim.grimac.manager.player.features.types;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.feature.FeatureState;
import ac.grim.grimac.player.GrimPlayer;

public class ExperimentalChecksFeature implements GrimFeature {
   public String getName() {
      return "ExperimentalChecks";
   }

   public void setState(GrimPlayer player, ConfigManager config, FeatureState state) {
      switch(state) {
      case ENABLED:
         player.setExperimentalChecks(true);
         break;
      case DISABLED:
         player.setExperimentalChecks(false);
         break;
      default:
         player.setExperimentalChecks(this.isEnabledInConfig(player, config));
      }

   }

   public boolean isEnabled(GrimPlayer player) {
      return player.isExperimentalChecks();
   }

   public boolean isEnabledInConfig(GrimPlayer player, ConfigManager config) {
      return config.getBooleanElse("experimental-checks", false);
   }
}
