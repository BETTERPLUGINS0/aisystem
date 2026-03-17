package com.bergerkiller.bukkit.tc.dep.softdependency;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public abstract class SoftDependency<T> implements SoftDetectableDependency {
   protected final Plugin owningPlugin;
   protected final String dependencyName;
   protected final T defaultValue;
   private Plugin currentPlugin;
   private T current;
   private boolean detecting;
   private boolean enabled;

   public static <T> SoftDependency.Builder<T> build(Plugin owningPlugin, String dependencyName) {
      return new SoftDependency.Builder(owningPlugin, dependencyName);
   }

   public SoftDependency(Plugin owningPlugin, String dependencyName) {
      this(owningPlugin, dependencyName, (Object)null);
   }

   public SoftDependency(Plugin owningPlugin, String dependencyName, T defaultValue) {
      this.currentPlugin = null;
      this.detecting = false;
      this.enabled = true;
      this.owningPlugin = owningPlugin;
      this.dependencyName = dependencyName;
      this.defaultValue = defaultValue;
      this.current = defaultValue;
      whenEnabled(owningPlugin, this::detect);
   }

   protected abstract T initialize(Plugin var1) throws Error, Exception;

   public void setEnabled(boolean enabled) {
      if (this.enabled != enabled) {
         this.enabled = enabled;
         if (enabled) {
            this.detect();
         } else if (this.currentPlugin != null) {
            this.handleDisable(this.currentPlugin);
         }
      }

   }

   public static void detectAll(Object fieldContainer) {
      SoftDetectableDependency.detectAll(fieldContainer);
   }

   public void detect() {
      if (this.enabled && this.owningPlugin.isEnabled()) {
         if (!this.detecting) {
            this.detecting = true;
            Bukkit.getPluginManager().registerEvents(new Listener() {
               @EventHandler
               public void onPluginEnabled(PluginEnableEvent event) {
                  if (SoftDependency.this.enabled) {
                     Plugin plugin = Bukkit.getPluginManager().getPlugin(SoftDependency.this.dependencyName);
                     if (plugin != null && event.getPlugin() == plugin) {
                        SoftDependency.this.handleEnable(plugin);
                     }

                  }
               }

               @EventHandler
               public void onPluginDisable(PluginDisableEvent event) {
                  if (SoftDependency.this.enabled) {
                     if (event.getPlugin() == SoftDependency.this.owningPlugin) {
                        SoftDependency.this.setEnabled(false);
                     } else {
                        if (event.getPlugin() == SoftDependency.this.currentPlugin) {
                           SoftDependency.this.handleDisable(event.getPlugin());
                        }

                     }
                  }
               }
            }, this.owningPlugin);
         }

         Plugin plugin = Bukkit.getPluginManager().getPlugin(this.dependencyName);
         if (plugin != null && plugin.isEnabled()) {
            this.handleEnable(plugin);
         }

      }
   }

   public Plugin owner() {
      return this.owningPlugin;
   }

   public String name() {
      return this.dependencyName;
   }

   public T get() {
      return this.current;
   }

   public Plugin getPlugin() {
      return this.currentPlugin;
   }

   public boolean isEnabled() {
      return this.currentPlugin != null;
   }

   protected void onEnable() {
   }

   protected void onDisable() {
   }

   private void handleEnable(Plugin plugin) {
      if (this.currentPlugin != null && this.currentPlugin != plugin) {
         this.handleDisable(this.currentPlugin);
      }

      Object initialized;
      try {
         initialized = this.initialize(plugin);
      } catch (Throwable var5) {
         this.owningPlugin.getLogger().log(Level.SEVERE, "An error occurred while initializing use of dependency " + plugin.getName(), var5);
         return;
      }

      this.current = initialized;
      this.currentPlugin = plugin;

      try {
         this.onEnable();
      } catch (Throwable var4) {
         this.owningPlugin.getLogger().log(Level.SEVERE, "An error occurred while enabling use of dependency " + plugin.getName(), var4);
         this.current = this.defaultValue;
         this.currentPlugin = null;
      }

   }

   private void handleDisable(Plugin plugin) {
      try {
         this.onDisable();
      } catch (Throwable var3) {
         this.owningPlugin.getLogger().log(Level.SEVERE, "An error occurred while disabling use of dependency " + plugin.getName(), var3);
      }

      this.current = this.defaultValue;
      this.currentPlugin = null;
   }

   public static void whenEnabled(Plugin plugin, Runnable callback) {
      SoftDependency.EnableEntry e = new SoftDependency.EnableEntry(plugin, callback);
      if (e.plugin.isEnabled()) {
         e.run();
      } else {
         SoftDependency.AfterPluginEnableHook.INSTANCE.schedule(e);
      }

   }

   static {
      PluginEnableEvent.getHandlerList();
      PluginDisableEvent.getHandlerList();
   }

   public static class Builder<T> {
      private static final Consumer NOOP_CALLBACK = (s) -> {
      };
      private final Plugin owningPlugin;
      private final String dependencyName;
      private T defaultValue;
      private SoftDependency.Initializer<T> initializer;
      private Consumer<SoftDependency<T>> whenEnable;
      private Consumer<SoftDependency<T>> whenDisable;

      private static <T> Consumer<SoftDependency<T>> noop_callback() {
         return NOOP_CALLBACK;
      }

      private static <T> Consumer<T> chainConsumer(Consumer<T> prev, Consumer<T> next) {
         if (next == null) {
            return NOOP_CALLBACK;
         } else {
            return prev == NOOP_CALLBACK ? next : (input) -> {
               prev.accept(input);
               next.accept(input);
            };
         }
      }

      private Builder(Plugin owningPlugin, String dependencyName) {
         this.defaultValue = null;
         this.owningPlugin = owningPlugin;
         this.dependencyName = dependencyName;
         this.initializer = null;
         this.whenEnable = noop_callback();
         this.whenDisable = noop_callback();
      }

      public <T2> SoftDependency.Builder<T2> withDefaultValue(T2 defaultValue) {
         return this.update((b) -> {
            b.defaultValue = defaultValue;
         });
      }

      public <T2> SoftDependency.Builder<T2> withInitializer(SoftDependency.Initializer<T2> initializer) {
         return this.update((b) -> {
            b.initializer = initializer;
         });
      }

      public <T2> SoftDependency.Builder<T2> withInitializer(SoftDependency.InitializerOnlyPlugin<T2> initializer) {
         return this.withInitializer(initializer == null ? null : (s, p) -> {
            return initializer.initialize(p);
         });
      }

      public SoftDependency.Builder<T> whenEnable(Consumer<SoftDependency<T>> callback) {
         this.whenEnable = chainConsumer(this.whenEnable, callback);
         return this;
      }

      public SoftDependency.Builder<T> whenEnable(Runnable callback) {
         return this.whenEnable((s) -> {
            callback.run();
         });
      }

      public SoftDependency.Builder<T> whenDisable(Consumer<SoftDependency<T>> callback) {
         this.whenDisable = chainConsumer(this.whenDisable, callback);
         return this;
      }

      public SoftDependency.Builder<T> whenDisable(Runnable callback) {
         return this.whenDisable((s) -> {
            callback.run();
         });
      }

      public <T2> SoftDependency<T2> create() {
         return new SoftDependency.CallbackBasedSoftDependency(this.update((b) -> {
         }));
      }

      private <T2> SoftDependency.Builder<T2> update(Consumer<SoftDependency.Builder<T2>> updator) {
         updator.accept(this);
         return this;
      }

      // $FF: synthetic method
      Builder(Plugin x0, String x1, Object x2) {
         this(x0, x1);
      }
   }

   private static class EnableEntry {
      public final Plugin plugin;
      public final Runnable callback;

      public EnableEntry(Plugin plugin, Runnable callback) {
         this.plugin = plugin;
         this.callback = callback;
      }

      public void run() {
         try {
            this.callback.run();
         } catch (Throwable var2) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to run post-enable task", var2);
         }

      }
   }

   private static class AfterPluginEnableHook extends HandlerList {
      public static final SoftDependency.AfterPluginEnableHook INSTANCE = new SoftDependency.AfterPluginEnableHook();
      private final ArrayList<SoftDependency.EnableEntry> pending = new ArrayList();

      public synchronized void schedule(SoftDependency.EnableEntry entry) {
         if (entry.plugin.isEnabled()) {
            entry.run();
         } else {
            this.pending.add(entry);
         }

      }

      public synchronized void unregister(Plugin plugin) {
         super.unregister(plugin);
         Iterator iter = this.pending.iterator();

         while(iter.hasNext()) {
            if (((SoftDependency.EnableEntry)iter.next()).plugin == plugin) {
               iter.remove();
            }
         }

      }

      public synchronized void bake() {
         super.bake();
         Iterator iter = this.pending.iterator();

         while(iter.hasNext()) {
            SoftDependency.EnableEntry e = (SoftDependency.EnableEntry)iter.next();
            if (e.plugin.isEnabled()) {
               iter.remove();
               e.run();
            }
         }

      }
   }

   private static class CallbackBasedSoftDependency<T> extends SoftDependency<T> {
      private final T defaultValue;
      private final SoftDependency.Initializer<T> initializer;
      private final Consumer<SoftDependency<T>> whenEnable;
      private final Consumer<SoftDependency<T>> whenDisable;

      public CallbackBasedSoftDependency(SoftDependency.Builder<T> builder) {
         super(builder.owningPlugin, builder.dependencyName);
         this.defaultValue = builder.defaultValue;
         this.initializer = builder.initializer == null ? (s, p) -> {
            return this.defaultValue;
         } : builder.initializer;
         this.whenEnable = builder.whenEnable;
         this.whenDisable = builder.whenDisable;
      }

      protected T initialize(Plugin plugin) throws Error, Exception {
         return this.initializer.initialize(this, plugin);
      }

      protected void onEnable() {
         this.whenEnable.accept(this);
      }

      protected void onDisable() {
         this.whenDisable.accept(this);
      }
   }

   @FunctionalInterface
   public interface InitializerOnlyPlugin<T> {
      T initialize(Plugin var1) throws Error, Exception;
   }

   @FunctionalInterface
   public interface Initializer<T> {
      T initialize(SoftDependency<T> var1, Plugin var2) throws Error, Exception;
   }
}
