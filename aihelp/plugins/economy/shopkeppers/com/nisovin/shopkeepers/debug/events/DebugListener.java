package com.nisovin.shopkeepers.debug.events;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.util.bukkit.EventUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;
import org.checkerframework.checker.nullness.qual.Nullable;

public class DebugListener implements Listener {
   private final Map<String, DebugListener.EventData> eventData = new HashMap();
   @Nullable
   private String lastLoggedEvent = null;
   private int lastLoggedEventCounter = 0;
   private final boolean logAllEvents;
   private final boolean printListeners;

   public static DebugListener register(boolean logAllEvents, boolean printListeners) {
      Log.info("Registering DebugListener.");
      DebugListener debugListener = new DebugListener(logAllEvents, printListeners);
      List<HandlerList> allHandlerLists = HandlerList.getHandlerLists();
      Iterator var4 = allHandlerLists.iterator();

      while(var4.hasNext()) {
         HandlerList handlerList = (HandlerList)var4.next();
         handlerList.register(new RegisteredListener(debugListener, (listener, event) -> {
            debugListener.handleEvent(event);
         }, EventPriority.LOWEST, SKShopkeepersPlugin.getInstance(), false));
         EventUtils.enforceExecuteFirst(handlerList, (Class)null, EventPriority.LOWEST, (registeredListener) -> {
            return registeredListener.getListener() == debugListener;
         }, false);
      }

      return debugListener;
   }

   private DebugListener(boolean logAllEvents, boolean printListeners) {
      this.logAllEvents = logAllEvents;
      this.printListeners = printListeners;
   }

   public void unregister() {
      HandlerList.unregisterAll(this);
   }

   private void handleEvent(Event event) {
      String eventName = event.getEventName();
      DebugListener.EventData data = (DebugListener.EventData)this.eventData.computeIfAbsent(eventName, (key) -> {
         return new DebugListener.EventData();
      });

      assert data != null;

      if (this.logAllEvents) {
         if (eventName.equals(this.lastLoggedEvent)) {
            ++this.lastLoggedEventCounter;
         } else {
            if (this.lastLoggedEventCounter > 0) {
               assert this.lastLoggedEvent != null;

               Log.info("[DebugListener] Event: " + this.lastLoggedEvent + " (" + this.lastLoggedEventCounter + "x)");
            }

            this.lastLoggedEvent = eventName;
            this.lastLoggedEventCounter = 1;
         }
      }

      if (this.printListeners && !data.printedListeners) {
         data.printedListeners = true;
         EventUtils.printRegisteredListeners(event);
      }

   }

   private static class EventData {
      boolean printedListeners = false;
   }
}
