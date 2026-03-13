package com.nisovin.shopkeepers.input.interaction;

import com.nisovin.shopkeepers.input.InputManager;
import com.nisovin.shopkeepers.input.InputRequest;
import com.nisovin.shopkeepers.util.bukkit.EventUtils;
import com.nisovin.shopkeepers.util.interaction.TestPlayerInteractEntityEvent;
import com.nisovin.shopkeepers.util.interaction.TestPlayerInteractEvent;
import com.nisovin.shopkeepers.util.java.MutableLong;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class InteractionInput extends InputManager<Event> implements Listener {
   private static final long INTERACTION_DELAY_MILLIS = 50L;
   private final Map<UUID, MutableLong> lastHandledPlayerInteractionsMillis = new HashMap();

   public InteractionInput(Plugin plugin) {
      super(plugin);
   }

   public void onEnable() {
      super.onEnable();
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
      EventUtils.enforceExecuteFirst(PlayerInteractEvent.class, EventPriority.LOWEST, (Listener)this);
      EventUtils.enforceExecuteFirst(PlayerInteractEntityEvent.class, EventPriority.LOWEST, (Listener)this);
      EventUtils.enforceExecuteFirst(PlayerInteractAtEntityEvent.class, EventPriority.LOWEST, (Listener)this);
   }

   public void onDisable() {
      super.onDisable();
      HandlerList.unregisterAll(this);
      this.lastHandledPlayerInteractionsMillis.clear();
   }

   protected void onPlayerQuit(Player player) {
      super.onPlayerQuit(player);
      this.lastHandledPlayerInteractionsMillis.remove(player.getUniqueId());
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = false
   )
   void onPlayerInteract(PlayerInteractEvent event) {
      if (!(event instanceof TestPlayerInteractEvent)) {
         this.handleInteraction(event);
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = false
   )
   void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
      if (!(event instanceof TestPlayerInteractEntityEvent)) {
         this.handleInteraction(event);
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = false
   )
   void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
      this.onPlayerInteractEntity(event);
   }

   private <E extends PlayerEvent & Cancellable> void handleInteraction(E event) {
      Player player = event.getPlayer();
      InputRequest<Event> request = this.getRequest(player);
      if (request != null) {
         MutableLong lastHandledInteractionMillis = (MutableLong)this.lastHandledPlayerInteractionsMillis.computeIfAbsent(player.getUniqueId(), (uuid) -> {
            return new MutableLong();
         });

         assert lastHandledInteractionMillis != null;

         long nowMillis = System.currentTimeMillis();
         long millisSinceLastInteraction = nowMillis - lastHandledInteractionMillis.getValue();
         if (millisSinceLastInteraction < 50L) {
            Log.debug(() -> {
               String var10000 = event.getEventName();
               return "Interaction input: Ignoring interaction (" + var10000 + ") of player " + player.getName() + ": Last handled interaction was " + millisSinceLastInteraction + " ms ago.";
            });
            ((Cancellable)event).setCancelled(true);
         } else {
            lastHandledInteractionMillis.setValue(nowMillis);
            if (this.isInteractionAccepted(request, event)) {
               this.removeRequest(player);
               request.onInput(event);
            }
         }
      }
   }

   private boolean isInteractionAccepted(InputRequest<Event> request, Event event) {
      if (!(request instanceof InteractionInput.Request)) {
         return true;
      } else {
         InteractionInput.Request interactRequest = (InteractionInput.Request)request;
         if (event instanceof PlayerInteractEvent) {
            return interactRequest.accepts((PlayerInteractEvent)event);
         } else if (event instanceof PlayerInteractEntityEvent) {
            return interactRequest.accepts((PlayerInteractEntityEvent)event);
         } else {
            throw new IllegalArgumentException("Unexpected interaction event: " + event.getClass().getName());
         }
      }
   }

   public interface Request extends InputRequest<Event> {
      default boolean accepts(PlayerInteractEvent event) {
         return true;
      }

      default boolean accepts(PlayerInteractEntityEvent event) {
         return false;
      }

      default void onInteract(PlayerInteractEvent event) {
      }

      default void onEntityInteract(PlayerInteractEntityEvent event) {
      }

      default void onInput(Event input) {
         if (input instanceof PlayerInteractEvent) {
            this.onInteract((PlayerInteractEvent)input);
         } else {
            if (!(input instanceof PlayerInteractEntityEvent)) {
               throw new IllegalArgumentException("Invalid interaction input: " + input.getClass().getName());
            }

            this.onEntityInteract((PlayerInteractEntityEvent)input);
         }

      }
   }
}
