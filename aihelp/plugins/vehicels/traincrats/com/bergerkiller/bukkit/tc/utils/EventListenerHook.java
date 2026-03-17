package com.bergerkiller.bukkit.tc.utils;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.mountiplex.reflection.ClassHook;
import com.bergerkiller.mountiplex.reflection.ClassHook.HookMethod;
import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;

public class EventListenerHook {
   public static void unhook(Class<? extends Event> eventClass) {
      hook(eventClass, (EventListenerHook.Handler)null);
   }

   public static <T extends Event> void hook(Class<T> eventClass, EventListenerHook.Handler<T> handler) {
      HandlerList handlerlist = CommonUtil.getEventHandlerList(eventClass);
      if (handlerlist == null) {
         throw new IllegalArgumentException("Event class " + eventClass.getName() + " has no HandlerList");
      } else {
         EnumMap map;
         try {
            Field f = HandlerList.class.getDeclaredField("handlerslots");
            boolean wasAccessible = f.isAccessible();
            f.setAccessible(true);
            map = (EnumMap)CommonUtil.unsafeCast(f.get(handlerlist));
            f.setAccessible(wasAccessible);
         } catch (Throwable var11) {
            throw new RuntimeException("Failed to modify HandlerList", var11);
         }

         Function mutator;
         if (handler != null) {
            EventListenerHook.Hook hook = new EventListenerHook.Hook(handler);
            mutator = (l) -> {
               return (RegisteredListener)hook.hook((RegisteredListener)ClassHook.unhook(l));
            };
         } else {
            mutator = (l) -> {
               return (RegisteredListener)EventListenerHook.Hook.unhook(l);
            };
         }

         synchronized(handlerlist) {
            Iterator var6 = map.values().iterator();

            while(var6.hasNext()) {
               List<RegisteredListener> list = (List)var6.next();
               ListIterator iter = list.listIterator();

               while(iter.hasNext()) {
                  iter.set((RegisteredListener)mutator.apply((RegisteredListener)iter.next()));
               }
            }

            try {
               Field f = HandlerList.class.getDeclaredField("handlers");
               boolean wasAccessible = f.isAccessible();
               f.setAccessible(true);
               f.set(handlerlist, (Object)null);
               f.setAccessible(wasAccessible);
            } catch (Throwable var10) {
               throw new RuntimeException("Failed to modify HandlerList", var10);
            }
         }

         handlerlist.bake();
      }
   }

   @FunctionalInterface
   public interface Handler<T extends Event> {
      void handle(RegisteredListener var1, Consumer<Event> var2, T var3);
   }

   public static class Hook extends ClassHook<EventListenerHook.Hook> {
      private final EventListenerHook.Handler<Event> handler;

      private Hook(EventListenerHook.Handler<?> handler) {
         this.handler = (EventListenerHook.Handler)CommonUtil.unsafeCast(handler);
      }

      @HookMethod("public void callEvent(org.bukkit.event.Event event)")
      public void callEvent(Event event) {
         RegisteredListener listener = (RegisteredListener)this.instance();
         EventListenerHook.Handler var10000 = this.handler;
         EventListenerHook.Hook var10002 = (EventListenerHook.Hook)this.base;
         Objects.requireNonNull(var10002);
         var10000.handle(listener, var10002::callEvent, event);
      }

      // $FF: synthetic method
      Hook(EventListenerHook.Handler x0, Object x1) {
         this(x0);
      }
   }
}
