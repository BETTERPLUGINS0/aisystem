package com.volmit.iris.util.plugin;

import com.volmit.iris.Iris;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.reflect.V;
import com.volmit.iris.util.scheduling.J;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class VolmitPlugin extends JavaPlugin implements Listener {
   public static final boolean bad = false;
   private KMap<KList<String>, VirtualCommand> commands;
   private KList<MortarCommand> commandCache;
   private KList<MortarPermission> permissionCache;

   public File getJarFile() {
      return this.getFile();
   }

   public void l(Object this) {
      Iris.info("[" + this.getName() + "]: " + String.valueOf(v1));
   }

   public void w(Object this) {
      Iris.warn("[" + this.getName() + "]: " + String.valueOf(v1));
   }

   public void f(Object this) {
      Iris.error("[" + this.getName() + "]: " + String.valueOf(v1));
   }

   public void v(Object this) {
      String var10000 = this.getName();
      Iris.verbose("[" + var10000 + "]: " + String.valueOf(v1));
   }

   public void onEnable() {
      loadConfig0();
      this.registerInstance();
      this.registerPermissions();
      this.registerCommands();
      J.a(this::outputInfo);
      this.registerListener(this);
      this.start();
   }

   public void unregisterAll() {
      this.unregisterListeners();
      this.unregisterCommands();
      this.unregisterPermissions();
      this.unregisterInstance();
   }

   private void outputInfo() {
      try {
         IO.delete(this.getDataFolder("info"));
         this.getDataFolder("info").mkdirs();
         this.outputPluginInfo();
         this.outputCommandInfo();
         this.outputPermissionInfo();
      } catch (Throwable var2) {
         Iris.reportError(var2);
      }

   }

   private void outputPermissionInfo() {
      YamlConfiguration v1 = new YamlConfiguration();
      Iterator v2 = this.permissionCache.iterator();

      while(v2.hasNext()) {
         MortarPermission v3 = (MortarPermission)v2.next();
         this.chain(v3, v1);
      }

      v1.save(this.getDataFile("info", "permissions.yml"));
   }

   private void chain(MortarPermission this, FileConfiguration v1) {
      KList v3 = new KList();
      Iterator v4 = v1.getChildren().iterator();

      MortarPermission v5;
      while(v4.hasNext()) {
         v5 = (MortarPermission)v4.next();
         v3.add((Object)v5.getFullNode());
      }

      v2.set(v1.getFullNode().replaceAll("\\Q.\\E", ",") + ".description", v1.getDescription());
      v2.set(v1.getFullNode().replaceAll("\\Q.\\E", ",") + ".default", v1.isDefault());
      v2.set(v1.getFullNode().replaceAll("\\Q.\\E", ",") + ".children", v3);
      v4 = v1.getChildren().iterator();

      while(v4.hasNext()) {
         v5 = (MortarPermission)v4.next();
         this.chain(v5, v2);
      }

   }

   private void outputCommandInfo() {
      YamlConfiguration v1 = new YamlConfiguration();
      Iterator v2 = this.commandCache.iterator();

      while(v2.hasNext()) {
         MortarCommand v3 = (MortarCommand)v2.next();
         this.chain(v3, "/", v1);
      }

      v1.save(this.getDataFile("info", "commands.yml"));
   }

   private void chain(MortarCommand this, String v1, FileConfiguration v2) {
      String v4 = v2 + (v2.length() == 1 ? "" : " ") + v1.getNode();
      v3.set(v4 + ".description", v1.getDescription());
      v3.set(v4 + ".required-permissions", v1.getRequiredPermissions());
      v3.set(v4 + ".aliases", v1.getAllNodes());
      Iterator v5 = v1.getChildren().iterator();

      while(v5.hasNext()) {
         MortarCommand v6 = (MortarCommand)v5.next();
         this.chain(v6, v4, v3);
      }

   }

   private void outputPluginInfo() {
      YamlConfiguration v1 = new YamlConfiguration();
      v1.set("version", this.getDescription().getVersion());
      v1.set("name", this.getDescription().getName());
      v1.save(this.getDataFile("info", "plugin.yml"));
   }

   private void registerPermissions() {
      this.permissionCache = new KList();
      Object v1 = this.getClass().getDeclaredFields();
      int i2 = v1.length;

      for(int i3 = 0; i3 < i2; ++i3) {
         Object v4 = v1[i3];
         if (v4.isAnnotationPresent(Permission.class)) {
            try {
               v4.setAccessible(true);
               Object v5 = (MortarPermission)v4.getType().getConstructor().newInstance();
               v4.set(Modifier.isStatic(v4.getModifiers()) ? null : this, v5);
               this.registerPermission(v5);
               this.permissionCache.add((Object)v5);
               String var10001 = v5.getFullNode();
               this.v("Registered Permissions " + var10001 + " (" + v4.getName() + ")");
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException | IllegalArgumentException var9) {
               Iris.reportError(var9);
               this.w("Failed to register permission (field " + v4.getName() + ")");
               var9.printStackTrace();
            }
         }
      }

      Iterator v1 = this.computePermissions().iterator();

      while(v1.hasNext()) {
         org.bukkit.permissions.Permission v2 = (org.bukkit.permissions.Permission)v1.next();

         try {
            Bukkit.getPluginManager().addPermission(v2);
         } catch (Throwable var8) {
            Iris.reportError(var8);
         }
      }

   }

   private KList<org.bukkit.permissions.Permission> computePermissions() {
      KList v1 = new KList();
      Field[] v2 = this.getClass().getDeclaredFields();
      int i3 = v2.length;

      for(int i4 = 0; i4 < i3; ++i4) {
         Object v5 = v2[i4];
         if (v5.isAnnotationPresent(Permission.class)) {
            try {
               Object v6 = (MortarPermission)v5.get(Modifier.isStatic(v5.getModifiers()) ? null : this);
               v1.add((Object)this.toPermission(v6));
               v1.addAll(this.computePermissions(v6));
            } catch (IllegalAccessException | SecurityException | IllegalArgumentException var7) {
               Iris.reportError(var7);
               var7.printStackTrace();
            }
         }
      }

      return v1.removeDuplicates();
   }

   private KList<org.bukkit.permissions.Permission> computePermissions(MortarPermission this) {
      KList v2 = new KList();
      if (v1 == null) {
         return v2;
      } else {
         Iterator v3 = v1.getChildren().iterator();

         while(v3.hasNext()) {
            MortarPermission v4 = (MortarPermission)v3.next();
            if (v4 != null) {
               v2.add((Object)this.toPermission(v4));
               v2.addAll(this.computePermissions(v4));
            }
         }

         return v2;
      }
   }

   private org.bukkit.permissions.Permission toPermission(MortarPermission this) {
      if (v1 == null) {
         return null;
      } else {
         String var10002 = v1.getFullNode();
         org.bukkit.permissions.Permission v2 = new org.bukkit.permissions.Permission(var10002 + (v1.hasParent() ? "" : ".*"));
         v2.setDescription(v1.getDescription() == null ? "" : v1.getDescription());
         v2.setDefault(v1.isDefault() ? PermissionDefault.TRUE : PermissionDefault.OP);
         Iterator v3 = v1.getChildren().iterator();

         while(v3.hasNext()) {
            MortarPermission v4 = (MortarPermission)v3.next();
            v2.getChildren().put(v4.getFullNode(), true);
         }

         return v2;
      }
   }

   private void registerPermission(MortarPermission this) {
   }

   public void onDisable() {
      this.stop();
      Bukkit.getScheduler().cancelTasks(this);
      this.unregisterListener(this);
      this.unregisterAll();
   }

   private void tickController(IController this) {
      if (v1.getTickInterval() >= 0) {
         ++M.tick;
         if (M.interval(v1.getTickInterval())) {
            try {
               v1.tick();
            } catch (Throwable var3) {
               this.w("Failed to tick controller " + v1.getName());
               var3.printStackTrace();
               Iris.reportError(var3);
            }
         }

      }
   }

   private void registerInstance() {
      Field[] v1 = this.getClass().getDeclaredFields();
      int i2 = v1.length;

      for(int i3 = 0; i3 < i2; ++i3) {
         Object v4 = v1[i3];
         if (v4.isAnnotationPresent(Instance.class)) {
            try {
               v4.setAccessible(true);
               v4.set(Modifier.isStatic(v4.getModifiers()) ? null : this, this);
               this.v("Registered Instance " + v4.getName());
            } catch (IllegalAccessException | SecurityException | IllegalArgumentException var6) {
               this.w("Failed to register instance (field " + v4.getName() + ")");
               var6.printStackTrace();
               Iris.reportError(var6);
            }
         }
      }

   }

   private void unregisterInstance() {
      Field[] v1 = this.getClass().getDeclaredFields();
      int i2 = v1.length;

      for(int i3 = 0; i3 < i2; ++i3) {
         Object v4 = v1[i3];
         if (v4.isAnnotationPresent(Instance.class)) {
            try {
               v4.setAccessible(true);
               v4.set(Modifier.isStatic(v4.getModifiers()) ? null : this, (Object)null);
               this.v("Unregistered Instance " + v4.getName());
            } catch (IllegalAccessException | SecurityException | IllegalArgumentException var6) {
               this.w("Failed to unregister instance (field " + v4.getName() + ")");
               var6.printStackTrace();
               Iris.reportError(var6);
            }
         }
      }

   }

   private void registerCommands() {
      this.commands = new KMap();
      this.commandCache = new KList();
      Field[] v1 = this.getClass().getDeclaredFields();
      int i2 = v1.length;

      for(int i3 = 0; i3 < i2; ++i3) {
         Object v4 = v1[i3];
         if (v4.isAnnotationPresent(Command.class)) {
            try {
               v4.setAccessible(true);
               Object v5 = (MortarCommand)v4.getType().getConstructor().newInstance();
               Command v6 = (Command)v4.getAnnotation(Command.class);
               this.registerCommand(v5, v6.value());
               this.commandCache.add((Object)v5);
               String var10001 = v5.getNode();
               this.v("Registered Commands /" + var10001 + " (" + v4.getName() + ")");
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException | IllegalArgumentException var7) {
               this.w("Failed to register command (field " + v4.getName() + ")");
               var7.printStackTrace();
               Iris.reportError(var7);
            }
         }
      }

   }

   public List<String> onTabComplete(CommandSender this, org.bukkit.command.Command v1, String v2, String[] v3) {
      KList v5 = new KList();
      Object v6 = v4;
      int i7 = v4.length;

      String v9;
      for(int i8 = 0; i8 < i7; ++i8) {
         v9 = v6[i8];
         if (!v9.trim().isEmpty()) {
            v5.add((Object)v9.trim());
         }
      }

      Iterator v6 = this.commands.k().iterator();

      while(v6.hasNext()) {
         KList v7 = (KList)v6.next();
         Iterator v8 = v7.iterator();

         while(v8.hasNext()) {
            v9 = (String)v8.next();
            if (v9.equalsIgnoreCase(v3)) {
               VirtualCommand v10 = (VirtualCommand)this.commands.get(v7);
               KList v11 = v10.hitTab(v1, v5.copy(), v3);
               if (v11 != null) {
                  return v11;
               }
            }
         }
      }

      return super.onTabComplete(v1, v2, v3, v4);
   }

   public boolean onCommand(CommandSender this, org.bukkit.command.Command v1, String v2, String[] v3) {
      KList v5 = new KList();
      v5.add((Object[])v4);
      Iterator v6 = this.commands.k().iterator();

      while(v6.hasNext()) {
         KList v7 = (KList)v6.next();
         Iterator v8 = v7.iterator();

         while(v8.hasNext()) {
            String v9 = (String)v8.next();
            if (v9.equalsIgnoreCase(v3)) {
               VirtualCommand v10 = (VirtualCommand)this.commands.get(v7);
               if (v10.hit(v1, v5.copy(), v3)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public void registerCommand(ICommand this) {
      this.registerCommand(v1, "");
   }

   public void registerCommand(ICommand this, String v1) {
      this.commands.put(v1.getAllNodes(), new VirtualCommand(v1, v2.trim().isEmpty() ? this.getTag() : this.getTag(v2.trim())));
      PluginCommand v3 = this.getCommand(v1.getNode().toLowerCase());
      String var10001;
      if (v3 != null) {
         v3.setExecutor(this);
         var10001 = this.getName();
         v3.setUsage(var10001 + ":" + this.getClass().toString() + ":" + v1.getNode());
      } else {
         RouterCommand v4 = new RouterCommand(v1, this);
         var10001 = this.getName();
         v4.setUsage(var10001 + ":" + this.getClass().toString());
         ((CommandMap)(new V(Bukkit.getServer())).get("commandMap")).register("", v4);
      }

   }

   public void unregisterCommand(ICommand this) {
      try {
         Object v2 = (SimpleCommandMap)(new V(Bukkit.getServer())).get("commandMap");
         Map v3 = (Map)(new V(v2)).get("knownCommands");
         Iterator v4 = v3.entrySet().iterator();

         while(v4.hasNext()) {
            Entry v5 = (Entry)v4.next();
            if (v5.getValue() instanceof org.bukkit.command.Command) {
               org.bukkit.command.Command v6 = (org.bukkit.command.Command)v5.getValue();
               String v7 = v6.getUsage();
               if (v7 != null) {
                  String var10001 = this.getName();
                  if (v7.equals(var10001 + ":" + this.getClass().toString() + ":" + v1.getNode())) {
                     if (v6.unregister(v2)) {
                        v4.remove();
                        this.v("Unregistered Command /" + v1.getNode());
                     } else {
                        ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
                        var10001 = this.getTag();
                        var10000.sendMessage(var10001 + "Failed to unregister command " + v6.getName());
                     }
                  }
               }
            }
         }
      } catch (Throwable var8) {
         var8.printStackTrace();
         Iris.reportError(var8);
      }

   }

   public String getTag() {
      return this.getTag("");
   }

   public void registerListener(Listener this) {
      Iris.debug("Register Listener " + v1.getClass().getSimpleName());
      Bukkit.getPluginManager().registerEvents(v1, this);
   }

   public void unregisterListener(Listener this) {
      Iris.debug("Register Listener " + v1.getClass().getSimpleName());
      HandlerList.unregisterAll(v1);
   }

   public void unregisterListeners() {
      HandlerList.unregisterAll(this);
   }

   public void unregisterCommands() {
      Iterator v1 = this.commands.v().iterator();

      while(v1.hasNext()) {
         VirtualCommand v2 = (VirtualCommand)v1.next();

         try {
            this.unregisterCommand(v2.getCommand());
         } catch (Throwable var4) {
            Iris.reportError(var4);
         }
      }

   }

   private void unregisterPermissions() {
      Iterator v1 = this.computePermissions().iterator();

      while(v1.hasNext()) {
         org.bukkit.permissions.Permission v2 = (org.bukkit.permissions.Permission)v1.next();
         Bukkit.getPluginManager().removePermission(v2);
         this.v("Unregistered Permission " + v2.getName());
      }

   }

   public File getDataFile(String... this) {
      File v2 = new File(this.getDataFolder(), (new KList(v1)).toString(File.separator));
      v2.getParentFile().mkdirs();
      return v2;
   }

   public File getDataFileList(String this, String[] v1) {
      KList v3 = new KList(v2);
      v3.add(0, v1);
      File v4 = new File(this.getDataFolder(), v3.toString(File.separator));
      v4.getParentFile().mkdirs();
      return v4;
   }

   public File getDataFolder(String... this) {
      if (v1.length == 0) {
         return super.getDataFolder();
      } else {
         File v2 = new File(this.getDataFolder(), (new KList(v1)).toString(File.separator));
         v2.mkdirs();
         return v2;
      }
   }

   public File getDataFolderNoCreate(String... this) {
      if (v1.length == 0) {
         return super.getDataFolder();
      } else {
         File v2 = new File(this.getDataFolder(), (new KList(v1)).toString(File.separator));
         return v2;
      }
   }

   public File getDataFolderList(String this, String[] v1) {
      KList v3 = new KList(v2);
      v3.add(0, v1);
      if (v3.size() == 0) {
         return super.getDataFolder();
      } else {
         File v4 = new File(this.getDataFolder(), v3.toString(File.separator));
         v4.mkdirs();
         return v4;
      }
   }

   public abstract void start();

   public abstract void stop();

   public abstract String getTag(String this);
}
