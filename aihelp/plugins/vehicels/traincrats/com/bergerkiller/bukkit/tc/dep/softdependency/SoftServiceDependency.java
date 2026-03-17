package com.bergerkiller.bukkit.tc.dep.softdependency;

import java.util.function.Consumer;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public abstract class SoftServiceDependency<T> implements SoftDetectableDependency {
   protected final Plugin owningPlugin;
   protected final String dependencyServiceClassName;
   protected final T defaultValue;
   private Object currentService;
   private Plugin currentServicePlugin;
   private T current;
   private boolean detecting;
   private boolean enabled;

   public static <T> SoftServiceDependency.Builder<T> build(Plugin owningPlugin, String serviceClassName) {
      return new SoftServiceDependency.Builder(owningPlugin, serviceClassName);
   }

   public SoftServiceDependency(Plugin owningPlugin, String serviceClassName) {
      this(owningPlugin, serviceClassName, (Object)null);
   }

   public SoftServiceDependency(Plugin owningPlugin, String serviceClassName, T defaultValue) {
      this.currentService = null;
      this.currentServicePlugin = null;
      this.detecting = false;
      this.enabled = true;
      this.owningPlugin = owningPlugin;
      this.dependencyServiceClassName = serviceClassName;
      this.defaultValue = defaultValue;
      this.current = defaultValue;
      SoftDependency.whenEnabled(owningPlugin, this::detect);
   }

   protected abstract T initialize(Object var1) throws Error, Exception;

   public void setEnabled(boolean enabled) {
      if (this.enabled != enabled) {
         this.enabled = enabled;
         if (enabled) {
            this.detect();
         } else if (this.currentServicePlugin != null) {
            this.handleDisable(this.currentServicePlugin);
         }
      }

   }

   public void detect() {
      if (this.enabled && this.owningPlugin.isEnabled()) {
         if (!this.detecting) {
            this.detecting = true;
            Bukkit.getPluginManager().registerEvents(new Listener() {
               @EventHandler
               public void onServiceEnable(ServiceRegisterEvent event) {
                  if (SoftServiceDependency.this.enabled) {
                     Class<?> serviceClass = SoftServiceDependency.this.tryGetServiceClass();
                     if (serviceClass != null && serviceClass.isAssignableFrom(event.getProvider().getService())) {
                        SoftServiceDependency.this.handleEnable(event.getProvider());
                     }

                  }
               }

               @EventHandler
               public void onPluginDisable(PluginDisableEvent event) {
                  if (SoftServiceDependency.this.enabled && event.getPlugin() == SoftServiceDependency.this.owningPlugin) {
                     SoftServiceDependency.this.setEnabled(false);
                  }

               }

               @EventHandler
               public void onServiceDisable(ServiceUnregisterEvent event) {
                  if (SoftServiceDependency.this.enabled && event.getProvider().getProvider() == SoftServiceDependency.this.currentService) {
                     SoftServiceDependency.this.handleDisable(event.getProvider().getPlugin());
                  }

               }
            }, this.owningPlugin);
         }

         Class<?> serviceClass = this.tryGetServiceClass();
         if (serviceClass != null) {
            RegisteredServiceProvider<?> provider = Bukkit.getServer().getServicesManager().getRegistration(serviceClass);
            if (provider != null) {
               this.handleEnable(provider);
            }
         }

      }
   }

   private Class<?> tryGetServiceClass() {
      try {
         return Class.forName(this.dependencyServiceClassName);
      } catch (Throwable var2) {
         return null;
      }
   }

   public Plugin owner() {
      return this.owningPlugin;
   }

   public String name() {
      return this.dependencyServiceClassName;
   }

   public T get() {
      return this.current;
   }

   public Object getService() {
      return this.currentService;
   }

   public Plugin getServicePlugin() {
      return this.currentServicePlugin;
   }

   public boolean isEnabled() {
      return this.currentService != null;
   }

   protected void onEnable() {
   }

   protected void onDisable() {
   }

   private void handleEnable(RegisteredServiceProvider<?> serviceProvider) {
      Object service = serviceProvider.getProvider();
      Plugin servicePlugin = serviceProvider.getPlugin();
      if (this.currentService != null && this.currentService != service) {
         this.handleDisable(this.currentServicePlugin);
      }

      Object initialized;
      try {
         initialized = this.initialize(service);
      } catch (Throwable var7) {
         this.owningPlugin.getLogger().log(Level.SEVERE, "An error occurred while initializing use of service dependency " + this.dependencyServiceClassName + " (" + servicePlugin.getName() + ")", var7);
         return;
      }

      this.current = initialized;
      this.currentService = service;
      this.currentServicePlugin = servicePlugin;

      try {
         this.onEnable();
      } catch (Throwable var6) {
         this.owningPlugin.getLogger().log(Level.SEVERE, "An error occurred while enabling use of service dependency " + this.dependencyServiceClassName + " (" + servicePlugin.getName() + ")", var6);
         this.current = this.defaultValue;
         this.currentService = null;
         this.currentServicePlugin = null;
      }

   }

   private void handleDisable(Plugin servicePlugin) {
      try {
         this.onDisable();
      } catch (Throwable var3) {
         this.owningPlugin.getLogger().log(Level.SEVERE, "An error occurred while disabling use of service dependency " + this.dependencyServiceClassName + " (" + servicePlugin.getName() + ")", var3);
      }

      this.current = this.defaultValue;
      this.currentService = null;
      this.currentServicePlugin = null;
   }

   static {
      PluginDisableEvent.getHandlerList();
      ServiceRegisterEvent.getHandlerList();
      ServiceUnregisterEvent.getHandlerList();
   }

   public static class Builder<T> {
      private static final Consumer NOOP_CALLBACK = (s) -> {
      };
      private final Plugin owningPlugin;
      private final String serviceClassName;
      private T defaultValue;
      private SoftServiceDependency.Initializer<T> initializer;
      private Consumer<SoftServiceDependency<T>> whenEnable;
      private Consumer<SoftServiceDependency<T>> whenDisable;

      private static <T> Consumer<SoftServiceDependency<T>> noop_callback() {
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

      private Builder(Plugin owningPlugin, String serviceClassName) {
         this.defaultValue = null;
         this.owningPlugin = owningPlugin;
         this.serviceClassName = serviceClassName;
         this.initializer = null;
         this.whenEnable = noop_callback();
         this.whenDisable = noop_callback();
      }

      public <T2> SoftServiceDependency.Builder<T2> withDefaultValue(T2 defaultValue) {
         return this.update((b) -> {
            b.defaultValue = defaultValue;
         });
      }

      public <T2> SoftServiceDependency.Builder<T2> withInitializer(SoftServiceDependency.Initializer<T2> initializer) {
         return this.update((b) -> {
            b.initializer = initializer;
         });
      }

      public <T2> SoftServiceDependency.Builder<T2> withInitializer(SoftServiceDependency.InitializerOnlyService<T2> initializer) {
         return this.withInitializer(initializer == null ? null : (s, p) -> {
            return initializer.initialize(p);
         });
      }

      public SoftServiceDependency.Builder<T> whenEnable(Consumer<SoftServiceDependency<T>> callback) {
         this.whenEnable = chainConsumer(this.whenEnable, callback);
         return this;
      }

      public SoftServiceDependency.Builder<T> whenEnable(Runnable callback) {
         return this.whenEnable((s) -> {
            callback.run();
         });
      }

      public SoftServiceDependency.Builder<T> whenDisable(Consumer<SoftServiceDependency<T>> callback) {
         this.whenDisable = chainConsumer(this.whenDisable, callback);
         return this;
      }

      public SoftServiceDependency.Builder<T> whenDisable(Runnable callback) {
         return this.whenDisable((s) -> {
            callback.run();
         });
      }

      public <T2> SoftServiceDependency<T2> create() {
         return new SoftServiceDependency.CallbackBasedSoftServiceDependency(this.update((b) -> {
         }));
      }

      private <T2> SoftServiceDependency.Builder<T2> update(Consumer<SoftServiceDependency.Builder<T2>> updator) {
         updator.accept(this);
         return this;
      }

      // $FF: synthetic method
      Builder(Plugin x0, String x1, Object x2) {
         this(x0, x1);
      }
   }

   private static class CallbackBasedSoftServiceDependency<T> extends SoftServiceDependency<T> {
      private final T defaultValue;
      private final SoftServiceDependency.Initializer<T> initializer;
      private final Consumer<SoftServiceDependency<T>> whenEnable;
      private final Consumer<SoftServiceDependency<T>> whenDisable;

      public CallbackBasedSoftServiceDependency(SoftServiceDependency.Builder<T> builder) {
         super(builder.owningPlugin, builder.serviceClassName);
         this.defaultValue = builder.defaultValue;
         this.initializer = builder.initializer == null ? (s, p) -> {
            return this.defaultValue;
         } : builder.initializer;
         this.whenEnable = builder.whenEnable;
         this.whenDisable = builder.whenDisable;
      }

      protected T initialize(Object service) throws Error, Exception {
         return this.initializer.initialize(this, service);
      }

      protected void onEnable() {
         this.whenEnable.accept(this);
      }

      protected void onDisable() {
         this.whenDisable.accept(this);
      }
   }

   @FunctionalInterface
   public interface InitializerOnlyService<T> {
      T initialize(Object var1) throws Error, Exception;
   }

   @FunctionalInterface
   public interface Initializer<T> {
      T initialize(SoftServiceDependency<T> var1, Object var2) throws Error, Exception;
   }
}
