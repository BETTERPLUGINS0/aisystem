package ac.grim.grimac.api.event;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public interface EventBus {
   void registerAnnotatedListeners(@NotNull Object var1, @NotNull Object var2);

   void registerAnnotatedListeners(@NotNull GrimPlugin var1, @NotNull Object var2);

   void registerStaticAnnotatedListeners(@NotNull Object var1, @NotNull Class<?> var2);

   void registerStaticAnnotatedListeners(@NotNull GrimPlugin var1, @NotNull Class<?> var2);

   void unregisterListeners(@NotNull Object var1, @NotNull Object var2);

   void unregisterListeners(@NotNull GrimPlugin var1, @NotNull Object var2);

   void unregisterStaticListeners(@NotNull Object var1, @NotNull Class<?> var2);

   void unregisterStaticListeners(@NotNull GrimPlugin var1, @NotNull Class<?> var2);

   void unregisterAllListeners(@NotNull Object var1);

   void unregisterAllListeners(@NotNull GrimPlugin var1);

   void unregisterListener(@NotNull Object var1, @NotNull GrimEventListener<?> var2);

   void unregisterListener(@NotNull GrimPlugin var1, @NotNull GrimEventListener<?> var2);

   void post(@NotNull GrimEvent var1);

   <T extends GrimEvent> void subscribe(@NotNull Object var1, @NotNull Class<T> var2, @NotNull GrimEventListener<T> var3, int var4, boolean var5, @NotNull Class<?> var6);

   <T extends GrimEvent> void subscribe(@NotNull GrimPlugin var1, @NotNull Class<T> var2, @NotNull GrimEventListener<T> var3, int var4, boolean var5, @NotNull Class<?> var6);

   default <T extends GrimEvent> void subscribe(@NotNull Object pluginContext, @NotNull Class<T> eventType, @NotNull GrimEventListener<T> listener, int priority, boolean ignoreCancelled) {
      this.subscribe(pluginContext, eventType, listener, priority, ignoreCancelled, listener.getClass());
   }

   default <T extends GrimEvent> void subscribe(@NotNull GrimPlugin plugin, @NotNull Class<T> eventType, @NotNull GrimEventListener<T> listener, int priority, boolean ignoreCancelled) {
      this.subscribe(plugin, eventType, listener, priority, ignoreCancelled, listener.getClass());
   }

   default <T extends GrimEvent> void subscribe(@NotNull Object pluginContext, @NotNull Class<T> eventType, @NotNull GrimEventListener<T> listener) {
      this.subscribe((Object)pluginContext, eventType, listener, 0, false);
   }

   default <T extends GrimEvent> void subscribe(@NotNull GrimPlugin plugin, @NotNull Class<T> eventType, @NotNull GrimEventListener<T> listener) {
      this.subscribe((GrimPlugin)plugin, eventType, listener, 0, false);
   }
}
