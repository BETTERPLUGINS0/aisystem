package ac.grim.grimac.internal.plugin.resolver;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

@ApiStatus.Internal
public final class GrimExtensionManager {
   private final List<GrimExtensionResolver> resolvers = new CopyOnWriteArrayList();
   private ResolutionFailureHandler failureHandler = (failedContext) -> {
      return new IllegalArgumentException("Unable to resolve plugin context for type: " + failedContext.getClass().getName() + ". Ensure you are passing a valid platform plugin instance or a pre-existing GrimPlugin.");
   };

   public void setFailureHandler(@NotNull ResolutionFailureHandler failureHandler) {
      this.failureHandler = (ResolutionFailureHandler)Objects.requireNonNull(failureHandler, "failureHandler cannot be null");
   }

   public void registerResolver(@NotNull GrimExtensionResolver resolver) {
      Objects.requireNonNull(resolver, "resolver cannot be null");
      this.resolvers.add(resolver);
   }

   @NotNull
   public GrimPlugin getPlugin(@NotNull Object context) {
      Objects.requireNonNull(context, "context cannot be null");
      if (context instanceof GrimPlugin) {
         return (GrimPlugin)context;
      } else {
         Iterator var2 = this.resolvers.iterator();

         GrimPlugin resolved;
         do {
            if (!var2.hasNext()) {
               throw this.failureHandler.createExceptionFor(context);
            }

            GrimExtensionResolver resolver = (GrimExtensionResolver)var2.next();
            resolved = resolver.resolve(context);
         } while(resolved == null);

         return resolved;
      }
   }
}
