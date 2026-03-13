package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class EventUtils {
   public static boolean setCancelled(Event event, boolean cancel) {
      Validate.notNull(event, (String)"event is null");
      if (event instanceof Cancellable) {
         ((Cancellable)event).setCancelled(cancel);
         return true;
      } else {
         return false;
      }
   }

   public static <E extends Event> EventExecutor eventExecutor(Class<? extends E> eventClass, Consumer<? super E> eventConsumer) {
      Validate.notNull(eventClass, (String)"eventClass is null");
      Validate.notNull(eventConsumer, (String)"eventConsumer is null");
      return (listener, event) -> {
         if (eventClass.isAssignableFrom(event.getClass())) {
            eventConsumer.accept(event);
         }
      };
   }

   public static HandlerList getHandlerList(Class<? extends Event> eventClass) {
      Class<? extends Event> eventRegistrationClass = getEventRegistrationClass(eventClass);

      assert eventRegistrationClass != null;

      HandlerList handlerList;
      try {
         Method method = eventRegistrationClass.getDeclaredMethod("getHandlerList");
         method.setAccessible(true);
         handlerList = (HandlerList)method.invoke(Unsafe.uncheckedNull());
      } catch (Exception var4) {
         throw new IllegalArgumentException("Could not retrieve the handler list from the event registration class for event " + eventClass.getName());
      }

      if (handlerList == null) {
         throw new IllegalArgumentException("The event registration class for event " + eventClass.getName() + " returned a null handler list!");
      } else {
         return handlerList;
      }
   }

   public static Class<? extends Event> getEventRegistrationClass(Class<? extends Event> eventClass) {
      Validate.notNull(eventClass, (String)"eventClass is null");

      try {
         eventClass.getDeclaredMethod("getHandlerList");
         return eventClass;
      } catch (NoSuchMethodException var3) {
         Class<?> superClass = eventClass.getSuperclass();
         if (superClass != null && !superClass.equals(Event.class) && Event.class.isAssignableFrom(superClass)) {
            return getEventRegistrationClass(superClass.asSubclass(Event.class));
         } else {
            throw new IllegalArgumentException("Could not find the event registration class for event " + eventClass.getName());
         }
      }
   }

   public static void enforceExecuteFirst(Class<? extends Event> eventClass, EventPriority eventPriority, Listener listener) {
      Validate.notNull(listener, (String)"listener is null");
      enforceExecuteFirst(eventClass, eventPriority, (registeredListener) -> {
         return registeredListener.getListener() == listener;
      });
   }

   public static void enforceExecuteFirst(Class<? extends Event> eventClass, EventPriority eventPriority, Plugin plugin) {
      Validate.notNull(plugin, (String)"plugin is null");
      enforceExecuteFirst(eventClass, eventPriority, (registeredListener) -> {
         return registeredListener.getPlugin() == plugin;
      });
   }

   public static void enforceExecuteFirst(Class<? extends Event> eventClass, EventPriority eventPriority, Predicate<? super RegisteredListener> affectedEventHandlers) {
      Validate.notNull(eventClass, (String)"eventClass is null");
      HandlerList handlerList = getHandlerList(eventClass);

      assert handlerList != null;

      enforceExecuteFirst(handlerList, eventClass, eventPriority, affectedEventHandlers, true);
   }

   public static void enforceExecuteFirst(HandlerList handlerList, @Nullable Class<? extends Event> eventClass, EventPriority eventPriority, Predicate<? super RegisteredListener> affectedEventHandlers, boolean verbose) {
      Validate.notNull(handlerList, (String)"handlerList is null");
      Validate.notNull(eventPriority, (String)"eventPriority is null");
      Validate.notNull(affectedEventHandlers, (String)"affectedEventHandlers is null");
      synchronized(handlerList) {
         RegisteredListener[] registeredListeners = handlerList.getRegisteredListeners();
         int eventHandlerCount = registeredListeners.length;
         int lastAffectedEventHandlerIndex = -1;
         boolean foundUnaffectedEventHandler = false;
         boolean reorderRequired = false;

         int i;
         RegisteredListener registeredListener;
         for(i = 0; i < eventHandlerCount; ++i) {
            registeredListener = registeredListeners[i];
            if (registeredListener.getPriority() == eventPriority) {
               if (affectedEventHandlers.test(registeredListener)) {
                  lastAffectedEventHandlerIndex = i;
                  if (foundUnaffectedEventHandler) {
                     reorderRequired = true;
                  }
               } else {
                  foundUnaffectedEventHandler = true;
               }
            }
         }

         if (reorderRequired) {
            assert lastAffectedEventHandlerIndex >= 0;

            for(i = 0; i < eventHandlerCount; ++i) {
               registeredListener = registeredListeners[i];
               if (registeredListener.getPriority() == eventPriority && !affectedEventHandlers.test(registeredListener)) {
                  if (i < lastAffectedEventHandlerIndex && verbose) {
                     Log.debug(() -> {
                        String var10000 = eventClass != null ? "'" + eventClass.getSimpleName() + "'" : "<unspecified>";
                        return "Moving a handler for event " + var10000 + " at priority " + eventPriority.name() + " in front of an event handler of plugin " + registeredListener.getPlugin().getName();
                     });
                  }

                  handlerList.unregister(registeredListener);

                  try {
                     handlerList.register(registeredListener);
                  } catch (Exception var15) {
                     Log.severe((String)("Failed to re-register a listener of plugin '" + registeredListener.getPlugin().getName() + "' for event " + (eventClass != null ? "'" + eventClass.getName() + "'" : "<unspecified>") + " at priority " + eventPriority.name() + "!"), (Throwable)var15);
                     Log.severe("This issue might be caused by one of your other plugins on your server. Check below for anything that indicates the involvement of one of your plugins.");
                     inspectHandlerListInternals(handlerList, eventClass, eventPriority, registeredListener);
                  }
               }
            }

         }
      }
   }

   private static void inspectHandlerListInternals(HandlerList handlerList, @Nullable Class<? extends Event> eventClass, EventPriority eventPriority, RegisteredListener targetListener) {
      assert handlerList != null && eventPriority != null && targetListener != null;

      assert eventClass == null || getHandlerList(eventClass) == handlerList;

      String var10000 = eventClass != null ? "'" + eventClass.getName() + "'" : "<unspecified>";
      Log.info("Inspecting the HandlerList internals of event " + var10000 + " and priority " + String.valueOf(eventPriority) + ":");

      try {
         Log.info("  Target RegisteredListener implementation: " + targetListener.getClass().getName());
         Log.info("  HandlerList implementation: " + handlerList.getClass().getName());
         Field handlerslotsField = HandlerList.class.getDeclaredField("handlerslots");
         handlerslotsField.setAccessible(true);
         Object handlerslots = Unsafe.assertNonNull(handlerslotsField.get(handlerList));
         Log.info("  handlerslots implementation: " + handlerslots.getClass().getName());
         Map<EventPriority, ?> handlerslotsMap = (Map)Unsafe.castNonNull(handlerslots);
         Object handlerslotsList = Unsafe.assertNonNull(handlerslotsMap.get(eventPriority));
         Log.info("  handlerslots list implementation: " + handlerslotsList.getClass().getName());
         List<RegisteredListener> registeredListeners = (List)Unsafe.castNonNull(handlerslotsList);
         Set<String> registeredListenerClasses = new LinkedHashSet();
         Iterator var10 = registeredListeners.iterator();

         while(var10.hasNext()) {
            RegisteredListener registeredListener = (RegisteredListener)var10.next();
            registeredListenerClasses.add(registeredListener.getClass().getName());
         }

         Log.info("  RegisteredListener implementations: " + String.valueOf(registeredListenerClasses));
      } catch (Exception var12) {
         Log.severe((String)"Error during HandlerList inspection!", (Throwable)var12);
      }

   }

   public static void printRegisteredListeners(Event event) {
      HandlerList handlerList = event.getHandlers();
      Log.info("Registered listeners for event " + event.getEventName() + ":");
      RegisteredListener[] var2 = handlerList.getRegisteredListeners();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         RegisteredListener rl = var2[var4];
         String var10000 = rl.getPlugin().getName();
         Log.info(" - " + var10000 + " (" + rl.getListener().getClass().getName() + "), priority: " + String.valueOf(rl.getPriority()) + ", ignoring cancelled: " + rl.isIgnoringCancelled());
      }

   }

   private EventUtils() {
   }
}
