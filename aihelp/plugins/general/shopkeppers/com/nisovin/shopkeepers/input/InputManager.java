package com.nisovin.shopkeepers.input;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.java.Box;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class InputManager<T> {
   protected final Plugin plugin;
   private final Listener playerQuitListener;
   private final Map<UUID, InputRequest<T>> pendingRequests;

   public InputManager(Plugin plugin) {
      this(plugin, false);
   }

   public InputManager(Plugin plugin, boolean threadSafe) {
      this.playerQuitListener = new InputManager.PlayerQuitListener();
      Validate.notNull(plugin, (String)"plugin is null");
      this.plugin = plugin;
      if (threadSafe) {
         this.pendingRequests = new ConcurrentHashMap();
      } else {
         this.pendingRequests = new HashMap();
      }

   }

   public void onEnable() {
      Bukkit.getPluginManager().registerEvents(this.playerQuitListener, this.plugin);
   }

   public void onDisable() {
      HandlerList.unregisterAll(this.playerQuitListener);
      this.pendingRequests.values().forEach(InputRequest::onAborted);
      this.pendingRequests.clear();
   }

   protected void onPlayerQuit(Player player) {
      this.abortRequest(player);
   }

   public void request(Player player, InputRequest<T> request) {
      Validate.notNull(player, (String)"player is null");
      Validate.notNull(request, (String)"request is null");
      InputRequest<T> previousRequest = (InputRequest)this.pendingRequests.put(player.getUniqueId(), request);
      if (previousRequest != null) {
         previousRequest.onAborted();
      }

   }

   @Nullable
   public InputRequest<T> getRequest(Player player) {
      Validate.notNull(player, (String)"player is null");
      return (InputRequest)this.pendingRequests.get(player.getUniqueId());
   }

   public boolean hasPendingRequest(Player player) {
      return this.pendingRequests.containsKey(player.getUniqueId());
   }

   public InputRequest<T> abortRequest(Player player) {
      InputRequest<T> request = this.removeRequest(player);
      if (request != null) {
         request.onAborted();
      }

      return request;
   }

   public void abortRequest(Player player, InputRequest<T> request) {
      Validate.notNull(player, (String)"player is null");
      Validate.notNull(request, (String)"request is null");
      if (this.pendingRequests.remove(player.getUniqueId(), request)) {
         request.onAborted();
      }

   }

   @Nullable
   protected final InputRequest<T> removeRequest(Player player) {
      Validate.notNull(player, (String)"player is null");
      return (InputRequest)this.pendingRequests.remove(player.getUniqueId());
   }

   protected final InputRequest<T> removeRequestIf(Player player, Predicate<? super InputRequest<T>> predicate) {
      Validate.notNull(player, (String)"player is null");
      Validate.notNull(predicate, (String)"predicate is null");
      Box<InputRequest<T>> removedRequest = new Box();
      this.pendingRequests.computeIfPresent(player.getUniqueId(), (id, request) -> {
         if (predicate.test(request)) {
            removedRequest.setValue(request);
            return (InputRequest)Unsafe.uncheckedNull();
         } else {
            return request;
         }
      });
      return (InputRequest)removedRequest.getValue();
   }

   private class PlayerQuitListener implements Listener {
      PlayerQuitListener() {
      }

      @EventHandler(
         priority = EventPriority.MONITOR,
         ignoreCancelled = true
      )
      void onPlayerQuitEvent(PlayerQuitEvent event) {
         Player player = event.getPlayer();
         InputManager.this.onPlayerQuit(player);
      }
   }
}
