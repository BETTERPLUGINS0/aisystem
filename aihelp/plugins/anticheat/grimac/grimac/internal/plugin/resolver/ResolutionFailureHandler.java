package ac.grim.grimac.internal.plugin.resolver;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ResolutionFailureHandler {
   RuntimeException createExceptionFor(@NotNull Object var1);
}
