package fr.xephi.authme.data.limbo;

import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.data.captcha.RegistrationCaptchaManager;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.task.MessageTask;
import fr.xephi.authme.task.TimeoutTask;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class LimboPlayerTaskManager {
   @Inject
   private Messages messages;
   @Inject
   private Settings settings;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private PlayerCache playerCache;
   @Inject
   private RegistrationCaptchaManager registrationCaptchaManager;

   void registerMessageTask(Player player, LimboPlayer limbo, LimboMessageType messageType) {
      int interval = (Integer)this.settings.getProperty(RegistrationSettings.MESSAGE_INTERVAL);
      LimboPlayerTaskManager.MessageResult result = this.getMessageKey(player.getName(), messageType);
      if (interval > 0) {
         String[] joinMessage = this.messages.retrieveSingle((CommandSender)player, result.messageKey, result.args).split("\n");
         MessageTask messageTask = new MessageTask(player, joinMessage);
         this.bukkitService.runTaskTimer(messageTask, 40L, (long)interval * 20L);
         limbo.setMessageTask(messageTask);
      }

   }

   void registerTimeoutTask(Player player, LimboPlayer limbo) {
      int timeout = (Integer)this.settings.getProperty(RestrictionSettings.TIMEOUT) * 20;
      if (timeout > 0) {
         String message = this.messages.retrieveSingle((CommandSender)player, MessageKey.LOGIN_TIMEOUT_ERROR);
         MyScheduledTask task = this.bukkitService.runTaskLater(new TimeoutTask(player, message, this.playerCache), (long)timeout);
         limbo.setTimeoutTask(task);
      }

   }

   static void setMuted(MessageTask task, boolean isMuted) {
      if (task != null) {
         task.setMuted(isMuted);
      }

   }

   private LimboPlayerTaskManager.MessageResult getMessageKey(String name, LimboMessageType messageType) {
      if (messageType == LimboMessageType.LOG_IN) {
         return new LimboPlayerTaskManager.MessageResult(MessageKey.LOGIN_MESSAGE, new String[0]);
      } else if (messageType == LimboMessageType.TOTP_CODE) {
         return new LimboPlayerTaskManager.MessageResult(MessageKey.TWO_FACTOR_CODE_REQUIRED, new String[0]);
      } else if (this.registrationCaptchaManager.isCaptchaRequired(name)) {
         String captchaCode = this.registrationCaptchaManager.getCaptchaCodeOrGenerateNew(name);
         return new LimboPlayerTaskManager.MessageResult(MessageKey.CAPTCHA_FOR_REGISTRATION_REQUIRED, new String[]{captchaCode});
      } else {
         return new LimboPlayerTaskManager.MessageResult(MessageKey.REGISTER_MESSAGE, new String[0]);
      }
   }

   private static final class MessageResult {
      private final MessageKey messageKey;
      private final String[] args;

      MessageResult(MessageKey messageKey, String... args) {
         this.messageKey = messageKey;
         this.args = args;
      }
   }
}
