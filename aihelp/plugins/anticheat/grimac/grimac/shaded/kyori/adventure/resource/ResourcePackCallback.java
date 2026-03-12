package ac.grim.grimac.shaded.kyori.adventure.resource;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.audience.Audience;
import java.util.UUID;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface ResourcePackCallback {
   @NotNull
   static ResourcePackCallback noOp() {
      return ResourcePackCallbacks.NO_OP;
   }

   @NotNull
   static ResourcePackCallback onTerminal(@NotNull final BiConsumer<UUID, Audience> success, @NotNull final BiConsumer<UUID, Audience> failure) {
      return (uuid, status, audience) -> {
         if (status == ResourcePackStatus.SUCCESSFULLY_LOADED) {
            success.accept(uuid, audience);
         } else if (!status.intermediate()) {
            failure.accept(uuid, audience);
         }

      };
   }

   void packEventReceived(@NotNull final UUID uuid, @NotNull final ResourcePackStatus status, @NotNull final Audience audience);
}
