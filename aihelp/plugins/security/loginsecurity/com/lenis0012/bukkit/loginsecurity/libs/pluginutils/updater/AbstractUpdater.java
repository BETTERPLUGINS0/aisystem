package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.updater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public abstract class AbstractUpdater implements Updater {
   protected final Plugin plugin;
   protected BukkitTask task;
   private VersionNumber currentVersion;
   private Version latestVersion;

   public AbstractUpdater(Plugin plugin, Duration frequency) {
      this.plugin = plugin;
      this.currentVersion = VersionNumber.of(plugin.getDescription().getVersion());
      this.task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::check, 0L, frequency.getSeconds() * 20L);
      plugin.getServer().getPluginManager().registerEvents(new AbstractUpdater.CommandInterceptor(), plugin);
   }

   private void check() {
      try {
         this.latestVersion = this.fetchLatestVersion();
      } catch (Exception var2) {
         this.verboseLog("Failed to fetch latest version", var2);
      }

   }

   protected abstract Version fetchLatestVersion();

   protected boolean isCompatible(Version version, UpdateChannel targetChannel) {
      VersionNumber bukkitVersion = VersionNumber.ofBukkit();
      if (version.getMinMinecraftVersion() != null && version.getMinMinecraftVersion().greaterThan(bukkitVersion)) {
         return false;
      } else if (version.getMaxMinecraftVersion() != null && version.getMaxMinecraftVersion().lessThan(bukkitVersion)) {
         return false;
      } else {
         return version.getChannel() == null || version.getChannel().ordinal() <= targetChannel.ordinal();
      }
   }

   protected void verboseLog(String message) {
      if (this.plugin.getDescription().getVersion().contains("SNAPSHOT")) {
         this.plugin.getLogger().log(Level.WARNING, message);
      }

   }

   protected void verboseLog(String message, Exception e) {
      if (this.plugin.getDescription().getVersion().contains("SNAPSHOT")) {
         this.plugin.getLogger().log(Level.WARNING, message, e);
      }

   }

   public boolean isUpdateAvailable() {
      return this.latestVersion != null && this.latestVersion.getVersionNumber().greaterThan(this.currentVersion);
   }

   public Version getLatestVersion() {
      return this.latestVersion;
   }

   public CompletableFuture<InstalledVersion> downloadLatestVersion() {
      if (this.latestVersion == null) {
         throw new IllegalStateException("No update available");
      } else if (this.latestVersion.getDownloadUrl() == null) {
         CompletableFuture<InstalledVersion> future = new CompletableFuture();
         future.completeExceptionally(new IllegalStateException("No download url available"));
         return future;
      } else {
         return CompletableFuture.supplyAsync(() -> {
            InputStream input = null;
            FileOutputStream output = null;

            try {
               URL url = new URL(this.latestVersion.getDownloadUrl());
               input = url.openStream();
               Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
               getFileMethod.setAccessible(true);
               File file = (File)getFileMethod.invoke(this.plugin);
               output = new FileOutputStream(new File(Bukkit.getUpdateFolderFile(), file.getName()));
               byte[] buffer = new byte[1024];

               int length;
               while((length = input.read(buffer, 0, buffer.length)) != -1) {
                  output.write(buffer, 0, length);
               }

               this.plugin.getLogger().log(Level.INFO, "Download complete!");
               InstalledVersion var8 = new InstalledVersion(this.latestVersion);
               return var8;
            } catch (Exception var20) {
               throw new RuntimeException("Failed to download update", var20);
            } finally {
               if (input != null) {
                  try {
                     input.close();
                  } catch (IOException var19) {
                  }
               }

               if (output != null) {
                  try {
                     output.close();
                  } catch (IOException var18) {
                  }
               }

            }
         }, (task) -> {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, task);
         });
      }
   }

   public void notifyIfUpdateAvailable(Player player) {
      if (this.latestVersion != null && !this.latestVersion.getVersionNumber().lessThanOrEqual(this.currentVersion)) {
         if (VersionNumber.ofBukkit().greaterThanOrEqual(VersionNumber.of("1.16")) && player.getPersistentDataContainer().has(new NamespacedKey(this.plugin, "ignored-update"), PersistentDataType.STRING)) {
            String ignoredVersion = (String)player.getPersistentDataContainer().get(new NamespacedKey(this.plugin, "ignored-update"), PersistentDataType.STRING);
            if (VersionNumber.of(ignoredVersion).greaterThanOrEqual(this.latestVersion.getVersionNumber())) {
               return;
            }
         }

         player.spigot().sendMessage((new ComponentBuilder("A new version of ")).retain(FormatRetention.FORMATTING).color(ChatColor.GREEN).append(this.plugin.getName()).append(" is available: ").append("v" + this.latestVersion.getVersionNumber().toString()).color(ChatColor.DARK_GREEN).append(" (you are running ").color(ChatColor.GREEN).append("v" + this.currentVersion.toString()).color(ChatColor.DARK_GREEN).append(")").color(ChatColor.GREEN).create());
         ComponentBuilder footer;
         if (this.latestVersion.getMinMinecraftVersion() != null) {
            footer = (new ComponentBuilder("This version is made for Minecraft ")).color(ChatColor.GREEN);
            footer.append(this.latestVersion.getMinMinecraftVersion().toString()).color(ChatColor.DARK_GREEN);
            if (this.latestVersion.getMaxMinecraftVersion() != null) {
               footer.append(" - " + this.latestVersion.getMaxMinecraftVersion()).color(ChatColor.DARK_GREEN);
            } else {
               footer.append(" and above");
            }

            player.spigot().sendMessage(footer.create());
         }

         footer = new ComponentBuilder("    ");
         if (this.latestVersion.getDownloadUrl() != null) {
            footer.append("[Download]").color(ChatColor.GREEN).bold(true).event(new HoverEvent(Action.SHOW_TEXT, (new ComponentBuilder("Click to download the update")).create())).event(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/" + this.plugin.getName() + ":updater download")).append("  ");
         }

         footer.append("[Changelog]").color(ChatColor.GREEN).bold(true).event(new HoverEvent(Action.SHOW_TEXT, (new ComponentBuilder("Click to view the changelog")).create())).event(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL, this.latestVersion.getChangelogUrl())).append("  ");
         if (VersionNumber.ofBukkit().greaterThanOrEqual(VersionNumber.of("1.16"))) {
            footer.append("Dismiss").color(ChatColor.GRAY).bold(true).event(new HoverEvent(Action.SHOW_TEXT, (new ComponentBuilder("Click to dismiss this message")).create())).event(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/" + this.plugin.getName() + ":updater dismiss"));
         }

         player.spigot().sendMessage(footer.create());
      }
   }

   class CommandInterceptor implements Listener {
      @EventHandler(
         priority = EventPriority.LOWEST
      )
      public void onCommand(PlayerCommandPreprocessEvent event) {
         Player player = event.getPlayer();
         if (event.getMessage().equals("/" + AbstractUpdater.this.plugin.getName() + ":updater download")) {
            event.setCancelled(true);
            if (AbstractUpdater.this.latestVersion == null) {
               return;
            }

            player.sendMessage(ChatColor.GREEN + "Downloading update...");
            AbstractUpdater.this.downloadLatestVersion().thenAccept((version) -> {
               player.sendMessage(ChatColor.GREEN + "Update downloaded!");
               player.sendMessage(ChatColor.GREEN + "Restart your server to apply the update.");
            }).exceptionally((e) -> {
               AbstractUpdater.this.plugin.getLogger().log(Level.WARNING, "Failed to download update", e);
               player.sendMessage(ChatColor.RED + "Failed to download update! Please download it manually from " + AbstractUpdater.this.latestVersion.getDownloadUrl());
               return null;
            });
         } else if (event.getMessage().equals("/" + AbstractUpdater.this.plugin.getName() + ":updater dismiss")) {
            event.setCancelled(true);
            player.getPersistentDataContainer().set(NamespacedKey.fromString("ignored-update", AbstractUpdater.this.plugin), PersistentDataType.STRING, AbstractUpdater.this.latestVersion.getVersionNumber().toString());
            player.sendMessage(ChatColor.GREEN + "Update notifications dismissed for this version");
         }

      }
   }
}
