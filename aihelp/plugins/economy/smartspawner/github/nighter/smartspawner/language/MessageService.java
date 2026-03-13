package github.nighter.smartspawner.language;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import lombok.Generated;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageService {
   private final JavaPlugin plugin;
   private final LanguageManager languageManager;
   private static final Map<String, String> EMPTY_PLACEHOLDERS = Collections.emptyMap();
   private final Map<String, Boolean> keyExistsCache = new ConcurrentHashMap(128);
   private static final Pattern COLOR_CODES = Pattern.compile("§[0-9a-fA-FxX]|§[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]|§[klmnorKLMNOR]");
   private static final Pattern HEX_CODES = Pattern.compile("&#[0-9a-fA-F]{6}");
   private static final Pattern AMPERSAND_CODES = Pattern.compile("&[0-9a-fA-FxXklmnorKLMNOR]");

   public void sendMessage(CommandSender sender, String key) {
      this.sendMessage(sender, key, EMPTY_PLACEHOLDERS);
   }

   public void sendMessage(Player player, String key) {
      this.sendMessage(player, key, EMPTY_PLACEHOLDERS);
   }

   public void sendMessage(Player player, String key, Map<String, String> placeholders) {
      this.sendMessage((CommandSender)player, key, placeholders);
   }

   public void sendMessage(CommandSender sender, String key, Map<String, String> placeholders) {
      if (!this.checkKeyExists(key)) {
         this.plugin.getLogger().warning("Message key not found: " + key);
         sender.sendMessage("§cMissing message key: " + key);
      } else {
         String message = this.languageManager.getMessage(key, placeholders);
         if (message != null && !message.startsWith("Missing message:")) {
            sender.sendMessage(message);
         }

         if (sender instanceof Player) {
            Player player = (Player)sender;
            this.sendPlayerSpecificContent(player, key, placeholders);
         }

      }
   }

   private boolean checkKeyExists(String key) {
      Map var10000 = this.keyExistsCache;
      LanguageManager var10002 = this.languageManager;
      Objects.requireNonNull(var10002);
      return (Boolean)var10000.computeIfAbsent(key, var10002::keyExists);
   }

   public void clearKeyExistsCache() {
      this.keyExistsCache.clear();
   }

   public void sendConsoleMessage(String key) {
      this.sendConsoleMessage(key, EMPTY_PLACEHOLDERS);
   }

   public void sendConsoleMessage(String key, Map<String, String> placeholders) {
      if (!this.languageManager.keyExists(key)) {
         this.plugin.getLogger().warning("Message key not found: " + key);
         this.plugin.getLogger().warning("§cMissing message key: " + key);
      } else {
         String message = this.languageManager.getMessageForConsole(key, placeholders);
         if (message != null && !message.startsWith("Missing message:")) {
            String consoleMessage = this.stripAllColorCodes(message);
            this.plugin.getLogger().info(consoleMessage);
         } else {
            this.plugin.getLogger().warning("Failed to retrieve message for key: " + key);
         }

      }
   }

   private String stripAllColorCodes(String message) {
      if (message == null) {
         return "";
      } else {
         String result = COLOR_CODES.matcher(message).replaceAll("");
         result = HEX_CODES.matcher(result).replaceAll("");
         result = AMPERSAND_CODES.matcher(result).replaceAll("");
         return result;
      }
   }

   private void sendPlayerSpecificContent(Player player, String key, Map<String, String> placeholders) {
      String title = this.languageManager.getTitle(key, placeholders);
      String subtitle = this.languageManager.getSubtitle(key, placeholders);
      if (title != null || subtitle != null) {
         player.sendTitle(title != null ? title : "", subtitle != null ? subtitle : "", 10, 70, 20);
      }

      String actionBar = this.languageManager.getActionBar(key, placeholders);
      if (actionBar != null) {
         player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
      }

      String soundName = this.languageManager.getSound(key);
      if (soundName != null) {
         try {
            player.playSound(player.getLocation(), soundName, 1.0F, 1.0F);
         } catch (Exception var9) {
            this.plugin.getLogger().warning("Invalid sound name for key " + key + ": " + soundName);
         }
      }

   }

   @Generated
   public MessageService(JavaPlugin plugin, LanguageManager languageManager) {
      this.plugin = plugin;
      this.languageManager = languageManager;
   }
}
