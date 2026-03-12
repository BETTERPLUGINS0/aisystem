package fr.xephi.authme.util.message;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.MiniMessage;
import fr.xephi.authme.libs.net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class MiniMessageUtils {
   private static final MiniMessage miniMessage = MiniMessage.miniMessage();

   public static String parseMiniMessageToLegacy(String message) {
      Component component = miniMessage.deserialize(message);
      return LegacyComponentSerializer.legacyAmpersand().serialize(component);
   }

   private MiniMessageUtils() {
   }
}
