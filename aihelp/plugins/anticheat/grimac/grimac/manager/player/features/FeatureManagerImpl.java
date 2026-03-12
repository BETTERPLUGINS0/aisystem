package ac.grim.grimac.manager.player.features;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.feature.FeatureManager;
import ac.grim.grimac.api.feature.FeatureState;
import ac.grim.grimac.manager.player.features.types.ExemptElytraFeature;
import ac.grim.grimac.manager.player.features.types.ExperimentalChecksFeature;
import ac.grim.grimac.manager.player.features.types.ForceSlowMovementFeature;
import ac.grim.grimac.manager.player.features.types.ForceStuckSpeedFeature;
import ac.grim.grimac.manager.player.features.types.GrimFeature;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.common.ConfigReloadObserver;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class FeatureManagerImpl implements FeatureManager, ConfigReloadObserver {
   private static final Map<String, GrimFeature> FEATURES;
   private final Map<String, FeatureState> states = new HashMap();
   private final GrimPlayer player;

   /** @deprecated */
   @Deprecated
   @Contract(
      pure = true
   )
   public static Map<String, GrimFeature> getFEATURES() {
      return getFeatures();
   }

   @Contract(
      pure = true
   )
   public static Map<String, GrimFeature> getFeatures() {
      return FEATURES;
   }

   public FeatureManagerImpl(GrimPlayer player) {
      this.player = player;
      Iterator var2 = FEATURES.values().iterator();

      while(var2.hasNext()) {
         GrimFeature value = (GrimFeature)var2.next();
         this.states.put(value.getName(), FeatureState.UNSET);
      }

   }

   public Collection<String> getFeatureKeys() {
      return ImmutableSet.copyOf(FEATURES.keySet());
   }

   @Nullable
   public FeatureState getFeatureState(String key) {
      return (FeatureState)this.states.get(key);
   }

   public boolean isFeatureEnabled(String key) {
      GrimFeature feature = (GrimFeature)FEATURES.get(key);
      return feature == null ? false : feature.isEnabled(this.player);
   }

   public boolean setFeatureState(String key, FeatureState tristate) {
      GrimFeature feature = (GrimFeature)FEATURES.get(key);
      if (feature == null) {
         return false;
      } else {
         this.states.put(key, tristate);
         return true;
      }
   }

   public void reload() {
      this.onReload(GrimAPI.INSTANCE.getExternalAPI().getConfigManager());
   }

   public void onReload(ConfigManager config) {
      Iterator var2 = this.states.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<String, FeatureState> entry = (Entry)var2.next();
         String key = (String)entry.getKey();
         FeatureState state = (FeatureState)entry.getValue();
         GrimFeature feature = (GrimFeature)FEATURES.get(key);
         if (feature != null) {
            feature.setState(this.player, config, state);
         }
      }

   }

   static {
      FeatureBuilder builder = new FeatureBuilder();
      builder.register(new ExperimentalChecksFeature());
      builder.register(new ExemptElytraFeature());
      builder.register(new ForceStuckSpeedFeature());
      builder.register(new ForceSlowMovementFeature());
      FEATURES = builder.buildMap();
   }
}
