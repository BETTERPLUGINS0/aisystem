package org.terraform.main;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;
import org.terraform.main.config.TConfig;

public class TLogger {
   private static final Logger LOGGER = Logger.getLogger("TerraformGenerator-Custom");
   private static boolean suppressConsoleLogs = false;

   public TLogger() {
      suppressConsoleLogs = TConfig.c.DEVSTUFF_SUPPRESS_CONSOLE_LOGS;
      if (suppressConsoleLogs) {
         try {
            Handler consoleHandler = new ConsoleHandler();
            Handler fileHandler = new FileHandler("plugins" + File.separator + "TerraformGenerator" + File.separator + "terraform.log", true);
            fileHandler.setFormatter(new SimpleFormatter() {
               private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

               @NotNull
               public synchronized String format(@NotNull LogRecord lr) {
                  return String.format("[%1$tF %1$tT] [%2$-7s] %3$s %n", new Date(lr.getMillis()), lr.getLevel().getLocalizedName(), ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', lr.getMessage())));
               }
            });
            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(consoleHandler);
            LOGGER.addHandler(fileHandler);
            consoleHandler.setLevel(Level.OFF);
            fileHandler.setLevel(Level.ALL);
            LOGGER.config("Configuration done.");
            this.stdout("Custom Logger Initialized");
         } catch (IOException var4) {
            Bukkit.getLogger().severe("Error occur in FileHandler." + String.valueOf(var4));
            suppressConsoleLogs = false;
         }
      }

   }

   public void stdout(@NotNull String message) {
      Bukkit.getConsoleSender().sendMessage("[TerraformGenerator] " + ChatColor.translateAlternateColorCodes('&', message));
      if (suppressConsoleLogs) {
         LOGGER.log(Level.INFO, " " + message);
      }

   }

   public void error(@NotNull String message) {
      if (suppressConsoleLogs) {
         LOGGER.log(Level.SEVERE, "[!] " + message);
      } else {
         ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
         String var10001 = String.valueOf(ChatColor.RED);
         var10000.sendMessage(var10001 + "[TerraformGenerator][!] " + ChatColor.translateAlternateColorCodes('&', message));
      }

   }

   public void info(@NotNull String message) {
      if (suppressConsoleLogs) {
         LOGGER.log(Level.INFO, message);
      } else {
         Bukkit.getConsoleSender().sendMessage("[TerraformGenerator] " + ChatColor.translateAlternateColorCodes('&', message));
      }

   }

   public void debug(@NotNull String message) {
      if (TConfig.c.DEVSTUFF_DEBUG_MODE) {
         if (suppressConsoleLogs) {
            LOGGER.log(Level.INFO, "[v] " + message);
         } else {
            Bukkit.getConsoleSender().sendMessage("[TerraformGenerator][v] " + ChatColor.translateAlternateColorCodes('&', message));
         }
      }

   }

   public void stackTrace(@NotNull Throwable e) {
      ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
      String var10001 = String.valueOf(ChatColor.RED);
      var10000.sendMessage(var10001 + "[TerraformGenerator][!] " + e.getMessage());
      StackTraceElement[] var2 = e.getStackTrace();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         StackTraceElement stackTraceElement = var2[var4];
         String message = stackTraceElement.toString();
         var10000 = Bukkit.getConsoleSender();
         var10001 = String.valueOf(ChatColor.RED);
         var10000.sendMessage(var10001 + "[TerraformGenerator][!] " + message);
      }

   }
}
