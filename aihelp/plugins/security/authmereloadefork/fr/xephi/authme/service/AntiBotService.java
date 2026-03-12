package fr.xephi.authme.service;

import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.permission.AdminPermission;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.ProtectionSettings;
import fr.xephi.authme.util.AtomicIntervalCounter;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

public class AntiBotService implements SettingsDependent {
   private final Messages messages;
   private final PermissionsManager permissionsManager;
   private final BukkitService bukkitService;
   private final CopyOnWriteArrayList<String> antibotKicked = new CopyOnWriteArrayList();
   private int duration;
   private AntiBotService.AntiBotStatus antiBotStatus;
   private boolean startup;
   private MyScheduledTask disableTask;
   private AtomicIntervalCounter flaggedCounter;

   @Inject
   AntiBotService(Settings settings, Messages messages, PermissionsManager permissionsManager, BukkitService bukkitService) {
      this.messages = messages;
      this.permissionsManager = permissionsManager;
      this.bukkitService = bukkitService;
      this.disableTask = null;
      this.antiBotStatus = AntiBotService.AntiBotStatus.DISABLED;
      this.startup = true;
      this.reload(settings);
   }

   public void reload(Settings settings) {
      this.duration = (Integer)settings.getProperty(ProtectionSettings.ANTIBOT_DURATION);
      int sensibility = (Integer)settings.getProperty(ProtectionSettings.ANTIBOT_SENSIBILITY);
      int interval = (Integer)settings.getProperty(ProtectionSettings.ANTIBOT_INTERVAL);
      this.flaggedCounter = new AtomicIntervalCounter(sensibility, interval * 1000);
      this.stopProtection();
      this.antiBotStatus = AntiBotService.AntiBotStatus.DISABLED;
      if ((Boolean)settings.getProperty(ProtectionSettings.ENABLE_ANTIBOT)) {
         Runnable enableTask = () -> {
            this.antiBotStatus = AntiBotService.AntiBotStatus.LISTENING;
         };
         if (this.startup) {
            int delay = (Integer)settings.getProperty(ProtectionSettings.ANTIBOT_DELAY);
            this.bukkitService.scheduleSyncDelayedTask(enableTask, (long)delay * 20L);
            this.startup = false;
         } else {
            enableTask.run();
         }

      }
   }

   private void startProtection() {
      if (this.antiBotStatus != AntiBotService.AntiBotStatus.ACTIVE) {
         if (this.disableTask != null) {
            this.disableTask.cancel();
         }

         this.disableTask = this.bukkitService.runTaskLater(this::stopProtection, (long)this.duration * 1200L);
         this.antiBotStatus = AntiBotService.AntiBotStatus.ACTIVE;
         this.bukkitService.scheduleSyncTaskFromOptionallyAsyncTask(() -> {
            this.bukkitService.getOnlinePlayers().stream().filter((player) -> {
               return this.permissionsManager.hasPermission(player, AdminPermission.ANTIBOT_MESSAGES);
            }).forEach((player) -> {
               this.messages.send(player, MessageKey.ANTIBOT_AUTO_ENABLED_MESSAGE);
            });
         });
      }
   }

   private void stopProtection() {
      if (this.antiBotStatus == AntiBotService.AntiBotStatus.ACTIVE) {
         this.antiBotStatus = AntiBotService.AntiBotStatus.LISTENING;
         this.flaggedCounter.reset();
         this.antibotKicked.clear();
         this.disableTask.cancel();
         this.disableTask = null;
         String durationString = Integer.toString(this.duration);
         this.bukkitService.getOnlinePlayers().stream().filter((player) -> {
            return this.permissionsManager.hasPermission(player, AdminPermission.ANTIBOT_MESSAGES);
         }).forEach((player) -> {
            this.messages.send(player, MessageKey.ANTIBOT_AUTO_DISABLED_MESSAGE, durationString);
         });
      }
   }

   public AntiBotService.AntiBotStatus getAntiBotStatus() {
      return this.antiBotStatus;
   }

   public void overrideAntiBotStatus(boolean started) {
      if (this.antiBotStatus != AntiBotService.AntiBotStatus.DISABLED) {
         if (started) {
            this.startProtection();
         } else {
            this.stopProtection();
         }
      }

   }

   public boolean shouldKick() {
      if (this.antiBotStatus == AntiBotService.AntiBotStatus.DISABLED) {
         return false;
      } else if (this.antiBotStatus == AntiBotService.AntiBotStatus.ACTIVE) {
         return true;
      } else if (this.flaggedCounter.handle()) {
         this.startProtection();
         return true;
      } else {
         return false;
      }
   }

   public boolean wasPlayerKicked(String name) {
      return this.antibotKicked.contains(name.toLowerCase(Locale.ROOT));
   }

   public void addPlayerKick(String name) {
      this.antibotKicked.addIfAbsent(name.toLowerCase(Locale.ROOT));
   }

   public static enum AntiBotStatus {
      LISTENING,
      DISABLED,
      ACTIVE;

      // $FF: synthetic method
      private static AntiBotService.AntiBotStatus[] $values() {
         return new AntiBotService.AntiBotStatus[]{LISTENING, DISABLED, ACTIVE};
      }
   }
}
