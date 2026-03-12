package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.modules;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ModuleRegistry {
   private final Map<Class<? extends Module>, Module> moduleMap = Maps.newLinkedHashMap();
   private final ModularPlugin plugin;
   private final ClassLoader classLoader;

   public ModuleRegistry(ModularPlugin plugin, ClassLoader classLoader) {
      this.plugin = plugin;
      this.classLoader = classLoader;
   }

   public void registerModules(String path) {
      ArrayList classes = Lists.newArrayList();

      try {
         ClassPath classPath = ClassPath.from(this.classLoader);
         UnmodifiableIterator var4 = classPath.getTopLevelClassesRecursive(path).iterator();

         while(var4.hasNext()) {
            ClassInfo info = (ClassInfo)var4.next();
            Class<?> clazz = Class.forName(info.getName());
            if (Module.class.isAssignableFrom(clazz)) {
               classes.add(clazz);
            }
         }
      } catch (Exception var7) {
         this.plugin.getLogger().log(Level.WARNING, "Failed to scan for modules", var7);
      }

      this.registerModules((Class[])classes.toArray(new Class[0]));
   }

   public void registerModules(Class<? extends Module>... modules) {
      this.registerModules(false, modules);
   }

   protected void registerModules(boolean local, Class<? extends Module>... modules) {
      Class[] var3 = modules;
      int var4 = modules.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Class moduleClass = var3[var5];

         try {
            Module instance = (Module)moduleClass.getConstructors()[0].newInstance(this.plugin);
            instance.local = local;
            this.moduleMap.put(moduleClass, instance);
         } catch (Exception var8) {
            this.plugin.getLogger().log(Level.WARNING, "Failed to init module", var8);
         }
      }

   }

   protected void enableModules(boolean local) {
      this.moduleMap.values().stream().filter((module) -> {
         return module.local == local;
      }).forEach(this::enableModule);
   }

   protected void disableModules(boolean local) {
      this.moduleMap.values().stream().filter((module) -> {
         return module.local == local;
      }).forEach((module) -> {
         module.disable();
         module.enabled = false;
      });
   }

   protected void reloadModules() {
      this.moduleMap.values().forEach(Module::reload);
   }

   private void enableModule(Module module) {
      if (!module.enabled) {
         List<Class<? extends Module>> required = module.getRequiredModules();
         Iterator var3 = required.iterator();

         while(var3.hasNext()) {
            Class<? extends Module> moduleClass = (Class)var3.next();
            Module dep = (Module)this.moduleMap.get(moduleClass);
            this.enableModule(dep);
         }

         module.enable();
         module.enabled = true;
      }
   }

   public <T extends Module> T getModule(Class<T> moduleClass) {
      return (Module)moduleClass.cast(this.moduleMap.get(moduleClass));
   }
}
