package ac.grim.grimac.platform.api.sender;

import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.UUID;

public interface Sender {
   UUID CONSOLE_UUID = new UUID(0L, 0L);
   String CONSOLE_NAME = "Console";

   String getName();

   UUID getUniqueId();

   void sendMessage(String var1);

   void sendMessage(Component var1);

   boolean hasPermission(String var1);

   boolean hasPermission(String var1, boolean var2);

   void performCommand(String var1);

   boolean isConsole();

   boolean isPlayer();

   default boolean isValid() {
      return true;
   }

   @NotNull
   Object getNativeSender();

   @Nullable
   PlatformPlayer getPlatformPlayer();
}
