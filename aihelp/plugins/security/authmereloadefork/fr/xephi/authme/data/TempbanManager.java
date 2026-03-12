package fr.xephi.authme.data;

import fr.xephi.authme.initialization.HasCleanup;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.util.PlayerUtils;
import fr.xephi.authme.util.expiring.TimedCounter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TempbanManager implements SettingsDependent, HasCleanup {
   private final Map<String, TimedCounter<String>> ipLoginFailureCounts = new ConcurrentHashMap();
   private final BukkitService bukkitService;
   private final Messages messages;
   private boolean isEnabled;
   private int threshold;
   private int length;
   private long resetThreshold;
   private String customCommand;

   @Inject
   TempbanManager(BukkitService bukkitService, Messages messages, Settings settings) {
      this.bukkitService = bukkitService;
      this.messages = messages;
      this.reload(settings);
   }

   public void increaseCount(String address, String name) {
      if (this.isEnabled) {
         TimedCounter<String> countsByName = (TimedCounter)this.ipLoginFailureCounts.computeIfAbsent(address, (k) -> {
            return new TimedCounter(this.resetThreshold, TimeUnit.MINUTES);
         });
         countsByName.increment(name);
      }

   }

   public void resetCount(String address, String name) {
      if (this.isEnabled) {
         TimedCounter<String> counter = (TimedCounter)this.ipLoginFailureCounts.get(address);
         if (counter != null) {
            counter.remove(name);
         }
      }

   }

   public boolean shouldTempban(String address) {
      if (this.isEnabled) {
         TimedCounter<String> countsByName = (TimedCounter)this.ipLoginFailureCounts.get(address);
         if (countsByName != null) {
            return countsByName.total() >= this.threshold;
         }
      }

      return false;
   }

   public void tempbanPlayer(Player player) {
      if (this.isEnabled) {
         String name = player.getName();
         String ip = PlayerUtils.getPlayerIp(player);
         String reason = this.messages.retrieveSingle((CommandSender)player, MessageKey.TEMPBAN_MAX_LOGINS);
         Date expires = new Date();
         long newTime = expires.getTime() + (long)this.length * 60000L;
         expires.setTime(newTime);
         this.bukkitService.runTask((Entity)player, () -> {
            if (this.customCommand.isEmpty()) {
               this.bukkitService.banIp(ip, reason, expires, "AuthMe");
               player.kickPlayer(reason);
            } else {
               String command = this.customCommand.replace("%player%", name).replace("%ip%", ip);
               this.bukkitService.dispatchConsoleCommand(command);
            }

         });
         this.ipLoginFailureCounts.remove(ip);
      }

   }

   public void reload(Settings settings) {
      this.isEnabled = (Boolean)settings.getProperty(SecuritySettings.TEMPBAN_ON_MAX_LOGINS);
      this.threshold = (Integer)settings.getProperty(SecuritySettings.MAX_LOGIN_TEMPBAN);
      this.length = (Integer)settings.getProperty(SecuritySettings.TEMPBAN_LENGTH);
      this.resetThreshold = (long)(Integer)settings.getProperty(SecuritySettings.TEMPBAN_MINUTES_BEFORE_RESET);
      this.customCommand = (String)settings.getProperty(SecuritySettings.TEMPBAN_CUSTOM_COMMAND);
   }

   public void performCleanup() {
      Iterator var1 = this.ipLoginFailureCounts.values().iterator();

      while(var1.hasNext()) {
         TimedCounter<String> countsByIp = (TimedCounter)var1.next();
         countsByIp.removeExpiredEntries();
      }

      this.ipLoginFailureCounts.entrySet().removeIf((e) -> {
         return ((TimedCounter)e.getValue()).isEmpty();
      });
   }
}
