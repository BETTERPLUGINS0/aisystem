package ac.grim.grimac.api.feature;

import ac.grim.grimac.api.common.BasicReloadable;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Collection;

public interface FeatureManager extends BasicReloadable {
   Collection<String> getFeatureKeys();

   @Nullable
   FeatureState getFeatureState(String var1);

   boolean isFeatureEnabled(String var1);

   boolean setFeatureState(String var1, FeatureState var2);
}
