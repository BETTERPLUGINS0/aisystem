package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.modules;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class ModularPlugin extends JavaPlugin {
   private static ModularPlugin instance;
   protected final ModuleRegistry registry;

   private static void setInstance(ModularPlugin instance) {
      ModularPlugin.instance = instance;
   }

   public static ModularPlugin getInstance() {
      return instance;
   }

   public ModularPlugin(Class<? extends Module>... modules) {
      setInstance(this);
      this.registry = new ModuleRegistry(this, this.getClassLoader());
      this.registry.registerModules(true, modules);
   }

   public void onEnable() {
      this.registry.enableModules(true);
      this.enable();
      this.registry.enableModules(false);
   }

   public void onDisable() {
      this.registry.disableModules(false);
      this.disable();
      this.registry.disableModules(true);
   }

   public abstract void enable();

   public abstract void disable();

   public void reloadModules() {
      this.registry.reloadModules();
   }

   public <T extends Module> T getModule(Class<T> type) {
      return this.registry.getModule(type);
   }
}
