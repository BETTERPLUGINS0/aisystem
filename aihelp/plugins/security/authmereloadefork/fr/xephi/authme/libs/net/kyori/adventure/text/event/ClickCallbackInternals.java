package fr.xephi.authme.libs.net.kyori.adventure.text.event;

import fr.xephi.authme.libs.net.kyori.adventure.audience.Audience;
import fr.xephi.authme.libs.net.kyori.adventure.permission.PermissionChecker;
import fr.xephi.authme.libs.net.kyori.adventure.util.Services;
import fr.xephi.authme.libs.net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.NotNull;

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
