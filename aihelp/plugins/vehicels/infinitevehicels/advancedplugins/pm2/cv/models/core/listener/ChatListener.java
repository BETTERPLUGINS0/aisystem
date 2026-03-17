package advancedplugins.pm2.cv.models.core.listener;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
   private static final Map<UUID, Predicate<String>> FETCH_MAP = Maps.newConcurrentMap();

   @EventHandler
   public void onAsyncChat(AsyncPlayerChatEvent var1) {
      UUID var2 = var1.getPlayer().getUniqueId();
      Predicate var3 = (Predicate)FETCH_MAP.get(var2);
      if (var3 != null) {
         var1.setCancelled(true);
         if (var3.test(ChatColor.stripColor(var1.getMessage()))) {
            FETCH_MAP.remove(var2);
         }
      }
   }

   public static void fetch(Player var0, Predicate<String> var1) {
      FETCH_MAP.put(var0.getUniqueId(), var1);
   }
}
