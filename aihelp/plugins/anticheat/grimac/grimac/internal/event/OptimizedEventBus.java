package ac.grim.grimac.internal.event;

import ac.grim.grimac.api.event.EventBus;
import ac.grim.grimac.api.event.GrimEvent;
import ac.grim.grimac.api.event.GrimEventHandler;
import ac.grim.grimac.api.event.GrimEventListener;
import ac.grim.grimac.api.event.events.CommandExecuteEvent;
import ac.grim.grimac.api.event.events.CompletePredictionEvent;
import ac.grim.grimac.api.event.events.FlagEvent;
import ac.grim.grimac.api.event.events.GrimJoinEvent;
import ac.grim.grimac.api.event.events.GrimQuitEvent;
import ac.grim.grimac.api.event.events.GrimReloadEvent;
import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.internal.plugin.resolver.GrimExtensionManager;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class OptimizedEventBus implements EventBus {
   private final Lookup lookup = MethodHandles.lookup();
   private final GrimExtensionManager extensionManager;
   private final Map<Class<? extends GrimEvent>, AtomicReference<OptimizedEventBus.OptimizedListener[]>> listenerMap = new ConcurrentHashMap();

   public OptimizedEventBus(GrimExtensionManager extensionManager) {
      this.extensionManager = extensionManager;
      this.prefillKnownEventTypes(this.listenerMap);
   }

   private void prefillKnownEventTypes(Map<Class<? extends GrimEvent>, AtomicReference<OptimizedEventBus.OptimizedListener[]>> map) {
      map.put(GrimReloadEvent.class, new AtomicReference(new OptimizedEventBus.OptimizedListener[0]));
      map.put(GrimQuitEvent.class, new AtomicReference(new OptimizedEventBus.OptimizedListener[0]));
      map.put(GrimJoinEvent.class, new AtomicReference(new OptimizedEventBus.OptimizedListener[0]));
      map.put(FlagEvent.class, new AtomicReference(new OptimizedEventBus.OptimizedListener[0]));
      map.put(CommandExecuteEvent.class, new AtomicReference(new OptimizedEventBus.OptimizedListener[0]));
      map.put(CompletePredictionEvent.class, new AtomicReference(new OptimizedEventBus.OptimizedListener[0]));
      map.put(GrimEvent.class, new AtomicReference(new OptimizedEventBus.OptimizedListener[0]));
   }

   public void registerAnnotatedListeners(@NotNull Object pluginContext, @NotNull Object listener) {
      GrimPlugin plugin = this.extensionManager.getPlugin(pluginContext);
      this.registerAnnotatedListeners(plugin, listener);
   }

   public void registerAnnotatedListeners(GrimPlugin plugin, @NotNull Object listener) {
      this.registerMethods(plugin, listener, listener.getClass());
   }

   public void registerStaticAnnotatedListeners(@NotNull Object pluginContext, @NotNull Class<?> clazz) {
      GrimPlugin plugin = this.extensionManager.getPlugin(pluginContext);
      this.registerStaticAnnotatedListeners(plugin, clazz);
   }

   public void registerStaticAnnotatedListeners(GrimPlugin plugin, @NotNull Class<?> clazz) {
      this.registerMethods(plugin, (Object)null, clazz);
   }

   public void unregisterListeners(@NotNull Object pluginContext, @NotNull Object listener) {
      GrimPlugin plugin = this.extensionManager.getPlugin(pluginContext);
      this.unregisterListeners(plugin, listener);
   }

   private void registerMethods(GrimPlugin plugin, @Nullable Object instance, @NotNull Class<?> clazz) {
      Method[] var4 = clazz.getDeclaredMethods();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Method method = var4[var6];
         GrimEventHandler annotation = (GrimEventHandler)method.getAnnotation(GrimEventHandler.class);
         if (annotation != null && method.getParameterCount() == 1) {
            Class<?> eventType = method.getParameterTypes()[0];
            if (GrimEvent.class.isAssignableFrom(eventType)) {
               try {
                  if (instance != null || Modifier.isStatic(method.getModifiers())) {
                     method.setAccessible(true);
                     MethodHandle handle = this.lookup.unreflect(method);
                     GrimEventListener<GrimEvent> listener = this.getGrimEventListener(instance, handle, eventType);
                     OptimizedEventBus.OptimizedListener optimizedListener = new OptimizedEventBus.OptimizedListener(plugin, listener, annotation.priority(), annotation.ignoreCancelled(), method.getDeclaringClass(), instance);
                     this.addListener(eventType, optimizedListener);
                  }
               } catch (IllegalAccessException var13) {
                  var13.printStackTrace();
               }
            }
         }
      }

   }

   @NotNull
   private GrimEventListener<GrimEvent> getGrimEventListener(@Nullable Object instance, MethodHandle handle, Class<?> eventType) {
      GrimEventListener listener;
      if (instance != null) {
         listener = (event) -> {
            try {
               handle.invoke(instance, event);
            } catch (Throwable var5) {
               throw new RuntimeException("Failed to invoke listener for " + eventType.getName(), var5);
            }
         };
      } else {
         listener = (event) -> {
            try {
               handle.invoke(event);
            } catch (Throwable var4) {
               throw new RuntimeException("Failed to invoke listener for " + eventType.getName(), var4);
            }
         };
      }

      return listener;
   }

   private void addListener(Class<? extends GrimEvent> eventType, OptimizedEventBus.OptimizedListener newListener) {
      AtomicReference ref = (AtomicReference)this.listenerMap.computeIfAbsent(eventType, (k) -> {
         return new AtomicReference(new OptimizedEventBus.OptimizedListener[0]);
      });

      OptimizedEventBus.OptimizedListener[] oldArray;
      OptimizedEventBus.OptimizedListener[] newArray;
      do {
         oldArray = (OptimizedEventBus.OptimizedListener[])ref.get();
         int insertionPoint = Arrays.binarySearch(oldArray, newListener, (a, b) -> {
            return Integer.compare(b.priority, a.priority);
         });
         if (insertionPoint < 0) {
            insertionPoint = -(insertionPoint + 1);
         } else {
            while(insertionPoint < oldArray.length - 1 && oldArray[insertionPoint + 1].priority == newListener.priority) {
               ++insertionPoint;
            }

            ++insertionPoint;
         }

         newArray = new OptimizedEventBus.OptimizedListener[oldArray.length + 1];
         System.arraycopy(oldArray, 0, newArray, 0, insertionPoint);
         newArray[insertionPoint] = newListener;
         System.arraycopy(oldArray, insertionPoint, newArray, insertionPoint + 1, oldArray.length - insertionPoint);
      } while(!ref.compareAndSet(oldArray, newArray));

   }

   public void unregisterListeners(GrimPlugin plugin, Object instance) {
      Iterator var3 = this.listenerMap.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<Class<? extends GrimEvent>, AtomicReference<OptimizedEventBus.OptimizedListener[]>> entry = (Entry)var3.next();
         this.removeListeners((Class)entry.getKey(), (AtomicReference)entry.getValue(), (listener) -> {
            return listener.plugin.equals(plugin) && listener.instance != null && listener.instance.equals(instance);
         });
      }

   }

   public void unregisterStaticListeners(@NotNull Object pluginContext, @NotNull Class<?> clazz) {
      GrimPlugin plugin = this.extensionManager.getPlugin(pluginContext);
      this.unregisterStaticListeners(plugin, clazz);
   }

   public void unregisterStaticListeners(GrimPlugin plugin, Class<?> clazz) {
      Iterator var3 = this.listenerMap.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<Class<? extends GrimEvent>, AtomicReference<OptimizedEventBus.OptimizedListener[]>> entry = (Entry)var3.next();
         this.removeListeners((Class)entry.getKey(), (AtomicReference)entry.getValue(), (listener) -> {
            return listener.plugin.equals(plugin) && listener.instance == null && listener.declaringClass.equals(clazz);
         });
      }

   }

   public void unregisterAllListeners(@NotNull Object pluginContext) {
      GrimPlugin plugin = this.extensionManager.getPlugin(pluginContext);
      this.unregisterAllListeners(plugin);
   }

   public void unregisterAllListeners(GrimPlugin plugin) {
      Iterator var2 = this.listenerMap.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<Class<? extends GrimEvent>, AtomicReference<OptimizedEventBus.OptimizedListener[]>> entry = (Entry)var2.next();
         this.removeListeners((Class)entry.getKey(), (AtomicReference)entry.getValue(), (listener) -> {
            return listener.plugin.equals(plugin);
         });
      }

   }

   public void unregisterListener(@NotNull Object pluginContext, @NotNull GrimEventListener<?> listener) {
      GrimPlugin plugin = this.extensionManager.getPlugin(pluginContext);
      this.unregisterListener(plugin, listener);
   }

   public void unregisterListener(GrimPlugin plugin, GrimEventListener<?> eventListener) {
      Iterator var3 = this.listenerMap.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<Class<? extends GrimEvent>, AtomicReference<OptimizedEventBus.OptimizedListener[]>> entry = (Entry)var3.next();
         this.removeListeners((Class)entry.getKey(), (AtomicReference)entry.getValue(), (listener) -> {
            return listener.plugin.equals(plugin) && listener.listener.equals(eventListener);
         });
      }

   }

   private void removeListeners(Class<? extends GrimEvent> eventType, AtomicReference<OptimizedEventBus.OptimizedListener[]> ref, Predicate<OptimizedEventBus.OptimizedListener> filter) {
      OptimizedEventBus.OptimizedListener[] oldArray;
      OptimizedEventBus.OptimizedListener[] newArray;
      do {
         oldArray = (OptimizedEventBus.OptimizedListener[])ref.get();
         int remaining = 0;
         newArray = oldArray;
         int index = oldArray.length;

         for(int var8 = 0; var8 < index; ++var8) {
            OptimizedEventBus.OptimizedListener listener = newArray[var8];
            if (!filter.test(listener)) {
               ++remaining;
            }
         }

         if (remaining == oldArray.length) {
            return;
         }

         newArray = new OptimizedEventBus.OptimizedListener[remaining];
         index = 0;
         OptimizedEventBus.OptimizedListener[] var12 = oldArray;
         int var13 = oldArray.length;

         for(int var10 = 0; var10 < var13; ++var10) {
            OptimizedEventBus.OptimizedListener listener = var12[var10];
            if (!filter.test(listener)) {
               newArray[index++] = listener;
            }
         }
      } while(!ref.compareAndSet(oldArray, newArray));

      if (newArray.length == 0) {
         this.listenerMap.remove(eventType);
      }

   }

   public void post(@NotNull GrimEvent event) {
      for(Class currentEventType = event.getClass(); GrimEvent.class.isAssignableFrom(currentEventType); currentEventType = currentEventType.getSuperclass()) {
         AtomicReference<OptimizedEventBus.OptimizedListener[]> ref = (AtomicReference)this.listenerMap.get(currentEventType);
         if (ref != null) {
            OptimizedEventBus.OptimizedListener[] listeners = (OptimizedEventBus.OptimizedListener[])ref.get();
            OptimizedEventBus.OptimizedListener[] var5 = listeners;
            int var6 = listeners.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               OptimizedEventBus.OptimizedListener listener = var5[var7];

               try {
                  if (!event.isCancelled() || listener.ignoreCancelled) {
                     listener.listener.handle(event);
                  }
               } catch (Throwable var10) {
                  var10.printStackTrace();
               }
            }
         }
      }

   }

   public <T extends GrimEvent> void subscribe(@NotNull Object pluginContext, @NotNull Class<T> eventType, @NotNull GrimEventListener<T> listener, int priority, boolean ignoreCancelled, @NotNull Class<?> declaringClass) {
   }

   public <T extends GrimEvent> void subscribe(GrimPlugin plugin, @NotNull Class<T> eventType, @NotNull GrimEventListener<T> listener, int priority, boolean ignoreCancelled, @NotNull Class<?> declaringClass) {
      OptimizedEventBus.OptimizedListener optimizedListener = new OptimizedEventBus.OptimizedListener(plugin, listener, priority, ignoreCancelled, declaringClass, (Object)null);
      this.addListener(eventType, optimizedListener);
   }

   private static class OptimizedListener {
      final GrimPlugin plugin;
      final GrimEventListener<GrimEvent> listener;
      final int priority;
      final boolean ignoreCancelled;
      final Class<?> declaringClass;
      final Object instance;

      OptimizedListener(GrimPlugin plugin, GrimEventListener<GrimEvent> listener, int priority, boolean ignoreCancelled, Class<?> declaringClass, Object instance) {
         this.plugin = plugin;
         this.listener = listener;
         this.priority = priority;
         this.ignoreCancelled = ignoreCancelled;
         this.declaringClass = declaringClass;
         this.instance = instance;
      }
   }
}
