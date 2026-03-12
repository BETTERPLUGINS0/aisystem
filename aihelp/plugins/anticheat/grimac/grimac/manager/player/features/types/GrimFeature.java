package ac.grim.grimac.manager.player.features.types;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.feature.FeatureState;
import ac.grim.grimac.player.GrimPlayer;

public interface GrimFeature {
   String getName();

   void setState(GrimPlayer var1, ConfigManager var2, FeatureState var3);

   boolean isEnabled(GrimPlayer var1);

   boolean isEnabledInConfig(GrimPlayer var1, ConfigManager var2);
}
