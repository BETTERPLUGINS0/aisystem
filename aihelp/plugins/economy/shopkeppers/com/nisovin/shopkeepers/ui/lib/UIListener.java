package com.nisovin.shopkeepers.ui.lib;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.bukkit.EventUtils;
import com.nisovin.shopkeepers.util.interaction.TestPlayerInteractEvent;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;

class UIListener implements Listener {
   private static final Set<? extends Class<? extends InventoryEvent>> DEFAULT_INVENTORY_EVENTS = Collections.unmodifiableSet(new HashSet(Arrays.asList(InventoryClickEvent.class, InventoryDragEvent.class, InventoryCloseEvent.class)));
   private static final Object NO_VIEW = new Object();
   private final Plugin plugin;
   private final UISessionManager uiSessionManager;
   private final Set<Class<? extends Event>> handledEventTypes = new HashSet();
   private final Deque<Object> eventHandlerStack = new ArrayDeque();

   UIListener(Plugin plugin, UISessionManager uiSessionManager) {
      Validate.notNull(plugin, (String)"plugin is null");
      Validate.notNull(uiSessionManager, (String)"uiSessionManager is null");
      this.plugin = plugin;
      this.uiSessionManager = uiSessionManager;
   }

   void onEnable() {
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
      DEFAULT_INVENTORY_EVENTS.forEach(this::registerEventType);
   }

   void onDisable() {
      HandlerList.unregisterAll(this);
      this.handledEventTypes.clear();
   }

   void registerEventType(Class<? extends InventoryEvent> eventClass) {
      Validate.notNull(eventClass, (String)"eventClass is null");
      if (!this.handledEventTypes.contains(eventClass)) {
         Class<? extends Event> registrationClass = EventUtils.getEventRegistrationClass(eventClass);
         if (this.handledEventTypes.add(registrationClass)) {
            this.handledEventTypes.add(eventClass);
            Bukkit.getPluginManager().registerEvent(registrationClass, this, EventPriority.LOW, EventUtils.eventExecutor(InventoryEvent.class, this::onInventoryEventEarly), this.plugin, false);
            Bukkit.getPluginManager().registerEvent(registrationClass, this, EventPriority.HIGH, EventUtils.eventExecutor(InventoryEvent.class, this::onInventoryEventLate), this.plugin, false);
         }
      }
   }

   @Nullable
   private View getView(HumanEntity human) {
      if (human.getType() != EntityType.PLAYER) {
         return null;
      } else {
         Player player = (Player)human;
         return this.uiSessionManager.getUISession(player);
      }
   }

   private void onInventoryEventEarly(InventoryEvent event) {
      View view = this.getView(event.getView().getPlayer());
      if (view != null) {
         boolean handled = view.informOnInventoryEventEarly(event);
         if (!handled) {
            view = null;
         }
      }

      this.eventHandlerStack.push(view != null ? view : NO_VIEW);
   }

   private void onInventoryEventLate(InventoryEvent event) {
      Object handlingView = Unsafe.assertNonNull(this.eventHandlerStack.pop());
      if (handlingView != NO_VIEW) {
         View view = (View)handlingView;
         view.informOnInventoryEventLate(event);
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onInventoryClose(InventoryCloseEvent event) {
      this.uiSessionManager.onInventoryClose(event);
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = false
   )
   void onPlayerInteract(PlayerInteractEvent event) {
      if (!(event instanceof TestPlayerInteractEvent)) {
         Player player = event.getPlayer();
         View view = this.getView(player);
         if (view != null) {
            Log.debug(() -> {
               return "Canceling interaction of player '" + player.getName() + "' while a UI is open.";
            });
            event.setCancelled(true);
         }

      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onPlayerQuit(PlayerQuitEvent event) {
      this.uiSessionManager.onPlayerQuit(event.getPlayer());
   }
}
