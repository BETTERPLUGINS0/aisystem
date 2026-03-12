package com.nisovin.shopkeepers.input.chat;

import com.nisovin.shopkeepers.input.InputManager;
import com.nisovin.shopkeepers.input.InputRequest;
import com.nisovin.shopkeepers.util.bukkit.EventUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

public class ChatInput extends InputManager<String> implements Listener {
   public ChatInput(Plugin plugin) {
      super(plugin, true);
   }

   public void onEnable() {
      super.onEnable();
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
      EventUtils.enforceExecuteFirst(AsyncPlayerChatEvent.class, EventPriority.LOWEST, (Listener)this);
   }

   public void onDisable() {
      super.onDisable();
      HandlerList.unregisterAll(this);
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = false
   )
   void onChat(AsyncPlayerChatEvent event) {
      Player player = event.getPlayer();
      InputRequest<String> request = this.removeRequest(player);
      if (request != null) {
         event.setCancelled(true);
         String message = event.getMessage();
         Bukkit.getScheduler().runTask(this.plugin, () -> {
            request.onInput(message);
         });
      }
   }
}
