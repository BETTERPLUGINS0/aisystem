package libs.com.ryderbelserion.vital.paper.util;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import libs.com.ryderbelserion.vital.common.util.AdvUtil;
import libs.com.ryderbelserion.vital.paper.api.enums.Support;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MsgUtil {
   private MsgUtil() {
      throw new AssertionError();
   }

   @NotNull
   public static Component parse(@NotNull String value, @Nullable UUID uuid) {
      if (value.isEmpty()) {
         return Component.empty();
      } else {
         return uuid != null ? parse(value, (Map)null, Bukkit.getPlayer(uuid)) : parse(value, (Map)null, (Player)null);
      }
   }

   @NotNull
   public static Component parse(@NotNull String value, @Nullable Map<String, String> placeholders, @Nullable Player player) {
      return (Component)(value.isEmpty() ? Component.empty() : AdvUtil.parse(parsePlaceholders(value, placeholders, player)));
   }

   public static String parsePlaceholders(@NotNull String value, @Nullable Map<String, String> placeholders, @Nullable Player player) {
      String clonedMessage = Support.placeholder_api.isEnabled() && player != null ? PlaceholderAPI.setPlaceholders(player, value) : value;
      String key;
      String entryValue;
      if (placeholders != null && !placeholders.isEmpty()) {
         for(Iterator var4 = placeholders.entrySet().iterator(); var4.hasNext(); clonedMessage = clonedMessage.replace(key, entryValue)) {
            Entry<String, String> entry = (Entry)var4.next();
            key = ((String)entry.getKey()).toLowerCase();
            entryValue = (String)entry.getValue();
         }
      }

      return clonedMessage;
   }
}
