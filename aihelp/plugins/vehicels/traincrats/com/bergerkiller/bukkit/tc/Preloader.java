package com.bergerkiller.bukkit.tc;

import com.google.common.collect.ImmutableList;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

public class Preloader extends JavaPlugin {
   private final String mainClassName;
   private final List<Preloader.Depend> dependList;
   private final List<String> preloaderCommands;
   private final List<Preloader.Depend> missingDepends = new ArrayList();
   private String loadError = null;

   public Preloader() {
      try {
         YamlConfiguration config = new YamlConfiguration();
         config.loadFromString(this.readPluginYAML(this));
         ConfigurationSection preloaderConfig = config.getConfigurationSection("preloader");
         if (preloaderConfig == null) {
            throw new IllegalStateException("plugin.yml has no preloader configuration");
         } else {
            this.mainClassName = preloaderConfig.getString("main");
            if (this.mainClassName == null) {
               throw new IllegalStateException("plugin.yml preloader configuration declares no main class");
            } else {
               List<?> dependConfigList = preloaderConfig.getList("depend");
               String name;
               if (dependConfigList != null) {
                  this.dependList = new ArrayList(dependConfigList.size());
                  Iterator var4 = dependConfigList.iterator();

                  while(var4.hasNext()) {
                     Object dependItem = var4.next();
                     if (dependItem instanceof Map) {
                        Map<String, Object> dependConfig = (Map)dependItem;
                        name = (String)dependConfig.getOrDefault("name", (Object)null);
                        if (name != null) {
                           String url = (String)dependConfig.getOrDefault("url", "");
                           this.dependList.add(new Preloader.Depend(name, url));
                        }
                     }
                  }
               } else {
                  ConfigurationSection dependConfig = preloaderConfig.getConfigurationSection("depend");
                  if (dependConfig == null) {
                     this.dependList = Collections.emptyList();
                  } else {
                     Set<String> names = dependConfig.getKeys(false);
                     this.dependList = new ArrayList(names.size());
                     Iterator var13 = names.iterator();

                     while(var13.hasNext()) {
                        name = (String)var13.next();
                        this.dependList.add(new Preloader.Depend(name, dependConfig.getString(name)));
                     }
                  }
               }

               List<String> preloaderCommandsTmp = preloaderConfig.getStringList("commands");
               if (preloaderCommandsTmp != null && !preloaderCommandsTmp.isEmpty()) {
                  this.preloaderCommands = new ArrayList(preloaderCommandsTmp);
               } else {
                  this.preloaderCommands = Collections.emptyList();
               }

            }
         }
      } catch (InvalidConfigurationException var9) {
         throw new IllegalStateException("Corrupt jar: Failed to load plugin.yml", var9);
      }
   }

   public void onLoad() {
      this.missingDepends.clear();
      Iterator var1 = this.dependList.iterator();

      while(var1.hasNext()) {
         Preloader.Depend depend = (Preloader.Depend)var1.next();
         if (this.getServer().getPluginManager().getPlugin(depend.name) == null) {
            this.missingDepends.add(depend);
         }
      }

      if (this.missingDepends.isEmpty()) {
         PluginDescriptionFile description = this.getDescription();
         List<String> newHardDepend = new ArrayList(description.getDepend());
         Iterator var3 = this.dependList.iterator();

         while(var3.hasNext()) {
            Preloader.Depend depend = (Preloader.Depend)var3.next();
            if (!newHardDepend.contains(depend.name)) {
               newHardDepend.add(depend.name);
            }
         }

         try {
            Field field = PluginDescriptionFile.class.getDeclaredField("depend");
            field.setAccessible(true);
            field.set(description, ImmutableList.copyOf(newHardDepend));
         } catch (Throwable var8) {
            this.getLogger().log(Level.SEVERE, "Failed to update depend list", var8);
         }

         String pluginName = this.getName();

         Class mainClass;
         try {
            mainClass = this.getClassLoader().loadClass(this.mainClassName);
         } catch (ClassNotFoundException var7) {
            this.getLogger().log(Level.SEVERE, "Failed to load the plugin main class", var7);
            this.loadError = "Failed to load the plugin main class - check server log!";
            return;
         }

         this.setLoaderPluginField((JavaPlugin)null, pluginName);

         JavaPlugin mainPlugin;
         try {
            mainPlugin = (JavaPlugin)mainClass.newInstance();
         } catch (Throwable var6) {
            this.getLogger().log(Level.SEVERE, "Failed to call plugin constructor", var6);
            this.loadError = "Failed to call plugin constructor - check server log!";
            this.setLoaderPluginField(this, pluginName);
            return;
         }

         this.swapPluginFieldEverywhere(this, mainPlugin, pluginName);

         try {
            mainPlugin.onLoad();
         } catch (Throwable var5) {
            this.getLogger().log(Level.SEVERE, "An error occurred during onLoad()", var5);
            this.loadError = "Failed to load the plugin - check server log!";
            this.swapPluginFieldEverywhere(mainPlugin, this, pluginName);
         }
      }
   }

   public void onEnable() {
      if (!this.missingDepends.isEmpty()) {
         this.missingDepends.forEach((depend) -> {
            PluginDescriptionFile desc = this.getDescription();
            this.getLogger().log(Level.SEVERE, "Plugin " + desc.getName() + " " + desc.getVersion() + " requires plugin " + depend.name + " to be installed! But it is not!");
            if (!depend.url.isEmpty()) {
               this.getLogger().log(Level.SEVERE, "Download " + depend.name + " from " + depend.url);
            }

         });
      }

      if (this.loadError == null && this.missingDepends.isEmpty()) {
         this.loadError = "Preloading failed - unsupported server?";
      }

      if (this.loadError != null) {
         this.getLogger().log(Level.SEVERE, "Not enabled because plugin could not be loaded! - Check server log");
      }

      this.preloaderCommands.forEach((commandName) -> {
         try {
            Constructor<PluginCommand> constr = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constr.setAccessible(true);
            PluginCommand command = (PluginCommand)constr.newInstance(commandName, this);
            command.setDescription("Plugin " + this.getName() + " could not be loaded!");
            command.setExecutor((sender, label, e_cmd, args) -> {
               this.showErrors(sender);
               return true;
            });
            Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap)commandMapField.get(Bukkit.getPluginManager());
            commandMap.register(this.getName(), command);
         } catch (Throwable var5) {
            this.getLogger().log(Level.SEVERE, "Failed to register preloader fallback command " + commandName, var5);
         }

      });
      this.getServer().getPluginManager().registerEvents(new Listener() {
         @EventHandler
         public void onPlayerJoin(PlayerJoinEvent event) {
            if (event.getPlayer().isOp()) {
               Preloader.this.showErrors(event.getPlayer());
            }

         }
      }, this);
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      this.showErrors(sender);
      return true;
   }

   private void showErrors(CommandSender sender) {
      if (this.loadError != null) {
         sender.sendMessage(ChatColor.RED + "There was a fatal error initializing " + this.getName());
         sender.sendMessage(ChatColor.RED + this.loadError);
      } else {
         sender.sendMessage(ChatColor.RED + "Plugin " + this.getName() + " could not be enabled!");
         sender.sendMessage(ChatColor.RED + "Please install these additional dependencies:");
         Iterator var2 = this.missingDepends.iterator();

         while(var2.hasNext()) {
            Preloader.Depend depend = (Preloader.Depend)var2.next();
            sender.sendMessage(ChatColor.RED + "  ======== " + depend.name + " ========");
            if (!depend.url.isEmpty()) {
               sender.sendMessage(ChatColor.RED + "  > " + ChatColor.WHITE + ChatColor.UNDERLINE + depend.url);
            }
         }
      }

   }

   private void swapPluginFieldEverywhere(JavaPlugin old_plugin, JavaPlugin plugin, String pluginName) {
      this.setLoaderPluginField(plugin, pluginName);
      PluginManager manager = this.getServer().getPluginManager();
      PluginManager paperManager = null;

      try {
         Field paperPluginManagerField = manager.getClass().getDeclaredField("paperPluginManager");
         paperPluginManagerField.setAccessible(true);
         paperManager = (PluginManager)paperPluginManagerField.get(manager);
      } catch (Throwable var7) {
      }

      if (paperManager != null) {
         this.swapPluginFieldEverywherePaper(paperManager, old_plugin, plugin, pluginName);
      } else {
         this.swapPluginFieldEverywhereSpigot(manager, old_plugin, plugin, pluginName);
      }
   }

   private void swapPluginFieldEverywhereSpigot(Object manager, JavaPlugin old_plugin, JavaPlugin plugin, String pluginName) {
      try {
         Field lookupNamesField = manager.getClass().getDeclaredField("plugins");
         lookupNamesField.setAccessible(true);
         List<Object> plugins = (List)lookupNamesField.get(manager);
         int index = plugins.indexOf(old_plugin);
         if (index == -1) {
            throw new IllegalStateException("Preloader does not exist in plugins list");
         }

         plugins.set(index, plugin);
         lookupNamesField = manager.getClass().getDeclaredField("lookupNames");
         lookupNamesField.setAccessible(true);
         Map<Object, Object> lookupNames = (Map)lookupNamesField.get(manager);
         boolean found = false;
         Iterator var8 = lookupNames.entrySet().iterator();

         while(var8.hasNext()) {
            Entry<Object, Object> e = (Entry)var8.next();
            if (e.getValue() == old_plugin) {
               e.setValue(plugin);
               found = true;
            }
         }

         if (!found) {
            throw new IllegalStateException("Preloader does not exist in lookupNames mapping");
         }
      } catch (Throwable var10) {
         this.getLogger().log(Level.SEVERE, "[Preloader] Failed to fully register the plugin into the server", var10);
      }

   }

   private void swapPluginFieldEverywherePaper(PluginManager manager, JavaPlugin old_plugin, JavaPlugin plugin, String pluginName) {
      Object instanceManager;
      try {
         Field instanceManagerField = manager.getClass().getDeclaredField("instanceManager");
         instanceManagerField.setAccessible(true);
         instanceManager = instanceManagerField.get(manager);
      } catch (Throwable var7) {
         this.getLogger().log(Level.SEVERE, "[Preloader] Failed to fully register the plugin into the server", var7);
         return;
      }

      this.swapPluginFieldEverywhereSpigot(instanceManager, old_plugin, plugin, pluginName);
   }

   private void setLoaderPluginField(JavaPlugin plugin, String pluginName) {
      ClassLoader loader = this.getClassLoader();

      Field pluginInitField;
      try {
         pluginInitField = loader.getClass().getDeclaredField("plugin");
         pluginInitField.setAccessible(true);
         pluginInitField.set(loader, plugin);
      } catch (Throwable var8) {
         this.getLogger().log(Level.SEVERE, "[Preloader] Failed to update 'plugin' field", var8);
      }

      try {
         pluginInitField = loader.getClass().getDeclaredField("pluginInit");
         pluginInitField.setAccessible(true);
         pluginInitField.set(loader, plugin);
      } catch (Throwable var7) {
         this.getLogger().log(Level.SEVERE, "[Preloader] Failed to update 'pluginInit' field", var7);
      }

      boolean isPaperLoader = false;

      try {
         Class<?> paperLoaderType = Class.forName("io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader");
         isPaperLoader = paperLoaderType.isInstance(loader);
      } catch (Throwable var6) {
      }

      if (isPaperLoader) {
         this.setLoaderPluginFieldPaper(loader, plugin, pluginName);
      } else {
         this.setLoaderPluginFieldSpigot(loader, plugin, pluginName);
      }
   }

   private void setLoaderPluginFieldPaper(ClassLoader loader, JavaPlugin plugin, String pluginName) {
   }

   private void setLoaderPluginFieldSpigot(ClassLoader loader, JavaPlugin plugin, String pluginName) {
      try {
         Field globalLoaderField = loader.getClass().getDeclaredField("loader");
         globalLoaderField.setAccessible(true);
         JavaPluginLoader globalLoader = (JavaPluginLoader)globalLoaderField.get(loader);
         Field globalLoaderPluginLoadersField = JavaPluginLoader.class.getDeclaredField("loaders");
         globalLoaderPluginLoadersField.setAccessible(true);
         Object rawLoaders = globalLoaderPluginLoadersField.get(globalLoader);
         if (rawLoaders instanceof List) {
            List<Object> pluginLoaders = (List)rawLoaders;
            if (plugin == null) {
               pluginLoaders.remove(loader);
            } else if (!pluginLoaders.contains(loader)) {
               pluginLoaders.add(loader);
            }
         } else {
            if (!(rawLoaders instanceof Map)) {
               throw new IllegalStateException("Unknown loaders field type: " + rawLoaders.getClass());
            }

            Map<String, Object> pluginLoaders = (Map)rawLoaders;
            if (plugin == null) {
               if (pluginLoaders.get(pluginName) == loader) {
                  pluginLoaders.remove(pluginName);
               }
            } else if (pluginLoaders.get(pluginName) == null) {
               pluginLoaders.put(pluginName, loader);
            }
         }
      } catch (Throwable var9) {
         this.getLogger().log(Level.SEVERE, "[Preloader] Failed to update class loader registry", var9);
      }

   }

   private String readPluginYAML(Plugin plugin) {
      InputStream found_stream = null;
      if (plugin instanceof JavaPlugin) {
         try {
            Method m = JavaPlugin.class.getDeclaredMethod("getClassLoader");
            m.setAccessible(true);
            ClassLoader loader = (ClassLoader)m.invoke(plugin);
            if (loader instanceof URLClassLoader) {
               URL resource = ((URLClassLoader)loader).findResource("plugin.yml");
               if (resource != null) {
                  URLConnection connection = resource.openConnection();
                  connection.setUseCaches(false);
                  found_stream = connection.getInputStream();
               }
            }
         } catch (Throwable var8) {
            this.getLogger().log(Level.WARNING, "Error selecting plugin.yml of " + plugin.getName() + ", trying fallback", var8);
         }
      }

      if (found_stream == null) {
         found_stream = plugin.getResource("plugin.yml");
      }

      if (found_stream == null) {
         throw new IllegalStateException("Failed to find plugin.yml");
      } else {
         try {
            InputStream stream = found_stream;

            String var15;
            try {
               ByteArrayOutputStream result = new ByteArrayOutputStream();
               byte[] buffer = new byte[1024];

               while(true) {
                  int length;
                  if ((length = stream.read(buffer)) == -1) {
                     var15 = new String(result.toByteArray(), StandardCharsets.UTF_8);
                     break;
                  }

                  result.write(buffer, 0, length);
               }
            } catch (Throwable var9) {
               if (found_stream != null) {
                  try {
                     stream.close();
                  } catch (Throwable var7) {
                     var9.addSuppressed(var7);
                  }
               }

               throw var9;
            }

            if (stream != null) {
               stream.close();
            }

            return var15;
         } catch (IOException var10) {
            throw new IllegalStateException("Failed to read plugin.yml", var10);
         }
      }
   }

   private static final class Depend {
      public final String name;
      public final String url;

      public Depend(String name, String url) {
         this.name = name.replace(' ', '_');
         this.url = url;
      }
   }
}
