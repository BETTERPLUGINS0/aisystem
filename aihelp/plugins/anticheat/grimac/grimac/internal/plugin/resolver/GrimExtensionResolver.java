package ac.grim.grimac.internal.plugin.resolver;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface GrimExtensionResolver {
   @Nullable
   GrimPlugin resolve(@NotNull Object var1);
}
