package com.nisovin.shopkeepers.debug.events;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.EnumMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.checkerframework.checker.nullness.qual.NonNull;

public class EventDebugListener<E extends Event> implements Listener {
   private final Class<? extends E> eventClass;
   private final Map<EventPriority, EventExecutor> executors = new EnumMap(EventPriority.class);

   public EventDebugListener(Class<? extends E> eventClass, EventDebugListener.EventHandler<E> eventHandler) {
      Validate.notNull(eventClass, (String)"eventClass is null");
      Validate.notNull(eventHandler, (String)"eventHandler is null");
      this.eventClass = eventClass;
      EventPriority[] var3 = EventPriority.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EventPriority priority = var3[var5];
         this.executors.put(priority, (listener, event) -> {
            eventHandler.handleEvent(priority, (Event)Unsafe.cast(event));
         });
      }

   }

   public void register() {
      this.executors.forEach((priority, executor) -> {
         Bukkit.getPluginManager().registerEvent(this.eventClass, this, priority, executor, SKShopkeepersPlugin.getInstance(), false);
      });
   }

   public void unregister() {
      HandlerList.unregisterAll(this);
   }

   public interface EventHandler<E extends Event> {
      void handleEvent(EventPriority var1, @NonNull E var2);
   }
}
