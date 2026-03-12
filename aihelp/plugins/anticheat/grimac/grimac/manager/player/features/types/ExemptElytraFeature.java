package ac.grim.grimac.manager.player.features.types;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.feature.FeatureState;
import ac.grim.grimac.player.GrimPlayer;

public class ExemptElytraFeature implements GrimFeature {
   public String getName() {
      return "ExemptElytra";
   }

   public void setState(GrimPlayer player, ConfigManager config, FeatureState state) {
      switch(state) {
      case ENABLED:
         player.setExemptElytra(true);
         break;
      case DISABLED:
         player.setExemptElytra(false);
         break;
      default:
         player.setExemptElytra(this.isEnabledInConfig(player, config));
      }

   }

   public boolean isEnabled(GrimPlayer player) {
      return player.isExemptElytra();
   }

   public boolean isEnabledInConfig(GrimPlayer player, ConfigManager config) {
      return config.getBooleanElse("exempt-elytra", false);
   }
}
