package ac.grim.grimac.shaded.kyori.adventure.text.event;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.audience.Audience;
import ac.grim.grimac.shaded.kyori.adventure.permission.PermissionChecker;
import ac.grim.grimac.shaded.kyori.adventure.util.Services;
import ac.grim.grimac.shaded.kyori.adventure.util.TriState;

final class ClickCallbackInternals {
   static final PermissionChecker ALWAYS_FALSE;
   static final ClickCallback.Provider PROVIDER;

   private ClickCallbackInternals() {
   }

   static {
      ALWAYS_FALSE = PermissionChecker.always(TriState.FALSE);
      PROVIDER = (ClickCallback.Provider)Services.service(ClickCallback.Provider.class).orElseGet(ClickCallbackInternals.Fallback::new);
   }

   static final class Fallback implements ClickCallback.Provider {
      @NotNull
      public ClickEvent create(@NotNull final ClickCallback<Audience> callback, @NotNull final ClickCallback.Options options) {
         return ClickEvent.suggestCommand("Callbacks are not supported on this platform!");
      }
   }
}
