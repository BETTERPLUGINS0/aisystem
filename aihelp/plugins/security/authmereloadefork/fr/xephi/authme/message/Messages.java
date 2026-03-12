package fr.xephi.authme.message;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableMap;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.mail.EmailService;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.util.expiring.Duration;
import fr.xephi.authme.util.message.I18NUtils;
import fr.xephi.authme.util.message.MiniMessageUtils;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages {
   private static final String NEWLINE_TAG = "%nl%";
   private static final String USERNAME_TAG = "%username%";
   private static final String DISPLAYNAME_TAG = "%displayname%";
   private static final Map<TimeUnit, MessageKey> TIME_UNIT_SINGULARS;
   private static final Map<TimeUnit, MessageKey> TIME_UNIT_PLURALS;
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(EmailService.class);
   private MessagesFileHandler messagesFileHandler;

   @Inject
   Messages(MessagesFileHandler messagesFileHandler) {
      this.messagesFileHandler = messagesFileHandler;
   }

   public void send(CommandSender sender, MessageKey key) {
      String[] lines = this.retrieve(key, sender);
      String[] var4 = lines;
      int var5 = lines.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String line = var4[var6];
         sender.sendMessage(line);
      }

   }

   public void send(CommandSender sender, MessageKey key, String... replacements) {
      String message = this.retrieveSingle(sender, key, replacements);
      String[] var5 = message.split("\n");
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String line = var5[var7];
         sender.sendMessage(line);
      }

   }

   public String[] retrieve(MessageKey key, CommandSender sender) {
      String message = this.retrieveMessage(key, sender);
      return message.isEmpty() ? new String[0] : message.split("\n");
   }

   public String formatDuration(Duration duration) {
      long value = duration.getDuration();
      MessageKey timeUnitKey = value == 1L ? (MessageKey)TIME_UNIT_SINGULARS.get(duration.getTimeUnit()) : (MessageKey)TIME_UNIT_PLURALS.get(duration.getTimeUnit());
      return value + " " + this.retrieveMessage(timeUnitKey, "");
   }

   private String retrieveMessage(MessageKey key, CommandSender sender) {
      String locale = sender instanceof Player ? I18NUtils.getLocale((Player)sender) : null;
      String message = this.messagesFileHandler.getMessageByLocale(key.getKey(), locale);
      String displayName = sender.getName();
      if (sender instanceof Player) {
         displayName = ((Player)sender).getDisplayName();
      }

      return ChatColor.translateAlternateColorCodes('&', MiniMessageUtils.parseMiniMessageToLegacy(message)).replace("%nl%", "\n").replace("%username%", sender.getName()).replace("%displayname%", displayName);
   }

   private String retrieveMessage(MessageKey key, String name) {
      String message = this.messagesFileHandler.getMessage(key.getKey());
      return ChatColor.translateAlternateColorCodes('&', MiniMessageUtils.parseMiniMessageToLegacy(message)).replace("%nl%", "\n").replace("%username%", name).replace("%displayname%", name);
   }

   public String retrieveSingle(CommandSender sender, MessageKey key, String... replacements) {
      String message = this.retrieveMessage(key, sender);
      String[] tags = key.getTags();
      if (replacements.length == tags.length) {
         for(int i = 0; i < tags.length; ++i) {
            message = message.replace(tags[i], replacements[i]);
         }
      } else {
         this.logger.warning("Invalid number of replacements for message key '" + key + "'");
      }

      return message;
   }

   public String retrieveSingle(String name, MessageKey key, String... replacements) {
      String message = this.retrieveMessage(key, name);
      String[] tags = key.getTags();
      if (replacements.length == tags.length) {
         for(int i = 0; i < tags.length; ++i) {
            message = message.replace(tags[i], replacements[i]);
         }
      } else {
         this.logger.warning("Invalid number of replacements for message key '" + key + "'");
      }

      return message;
   }

   static {
      TIME_UNIT_SINGULARS = ImmutableMap.builder().put(TimeUnit.SECONDS, MessageKey.SECOND).put(TimeUnit.MINUTES, MessageKey.MINUTE).put(TimeUnit.HOURS, MessageKey.HOUR).put(TimeUnit.DAYS, MessageKey.DAY).build();
      TIME_UNIT_PLURALS = ImmutableMap.builder().put(TimeUnit.SECONDS, MessageKey.SECONDS).put(TimeUnit.MINUTES, MessageKey.MINUTES).put(TimeUnit.HOURS, MessageKey.HOURS).put(TimeUnit.DAYS, MessageKey.DAYS).build();
   }
}
