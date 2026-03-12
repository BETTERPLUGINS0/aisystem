package ac.grim.grimac.utils.anticheat;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TextReplacementConfig;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.MiniMessage;
import ac.grim.grimac.utils.data.webhook.discord.CompiledDiscordTemplate;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;

public final class MessageUtil {
   private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-ORX]");
   private static final Pattern HEX_PATTERN = Pattern.compile("([&§]#[A-Fa-f0-9]{6})|([&§]x([&§][A-Fa-f0-9]){6})");
   private static final char PLACEHOLDER_ESCAPE_CHAR = '\uffff';
   private static final Pattern UNIFIED_PLACEHOLDER_PATTERN = Pattern.compile("%([a-zA-Z0-9_]+)%");

   @NotNull
   public static String toUnlabledString(@Nullable Vector3i vec) {
      return vec == null ? "null" : vec.x + ", " + vec.y + ", " + vec.z;
   }

   @NotNull
   public static String toUnlabledString(@Nullable Vector3f vec) {
      return vec == null ? "null" : vec.x + ", " + vec.y + ", " + vec.z;
   }

   @Contract("_, null, _ -> null; _, !null, _ -> !null")
   @Nullable
   public static String replacePlaceholders(@Nullable GrimPlayer player, @Nullable String string, boolean removeFormatting) {
      return replacePlaceholders(player, player == null ? null : player.platformPlayer, string, removeFormatting);
   }

   @Contract("_, null -> null; _, !null -> !null")
   @Nullable
   public static String replacePlaceholders(@Nullable GrimPlayer player, @Nullable String string) {
      return replacePlaceholders(player, player == null ? null : player.platformPlayer, string, false);
   }

   @Contract("_, null -> null; _, !null -> !null")
   @Nullable
   public static String replacePlaceholders(@Nullable Sender sender, @Nullable String string) {
      return replacePlaceholders(sender != null ? sender.getPlatformPlayer() : null, string);
   }

   @Contract("_, null -> null; _, !null -> !null")
   @Nullable
   public static String replacePlaceholders(@Nullable PlatformPlayer player, @Nullable String string) {
      return replacePlaceholders(player == null ? null : GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(player.getUniqueId()), player, string, false);
   }

   @Contract("_, _, null, _ -> null; _, _, !null, _ -> !null")
   @Nullable
   private static String replacePlaceholders(@Nullable GrimPlayer grimPlayer, @Nullable PlatformPlayer platformPlayer, @Nullable String string, boolean removeFormatting) {
      if (string == null) {
         return null;
      } else if (string.indexOf(37) == -1) {
         return string;
      } else {
         Matcher matcher = UNIFIED_PLACEHOLDER_PATTERN.matcher(string);
         if (!matcher.find()) {
            return GrimAPI.INSTANCE.getMessagePlaceHolderManager().replacePlaceholders(platformPlayer, string);
         } else {
            Map<String, String> staticReplacements = GrimAPI.INSTANCE.getExternalAPI().getStaticReplacements();
            Map<String, Function<GrimUser, String>> variableReplacements = GrimAPI.INSTANCE.getExternalAPI().getVariableReplacements();
            StringBuilder sb = new StringBuilder(string.length() + 32);

            String grimReplaced;
            do {
               grimReplaced = matcher.group(0);
               String value = null;
               String staticValue = (String)staticReplacements.get(grimReplaced);
               if (staticValue != null) {
                  value = staticValue;
               } else if (grimPlayer != null) {
                  Function<GrimUser, String> func = (Function)variableReplacements.get(grimReplaced);
                  if (func != null) {
                     value = (String)func.apply(grimPlayer);
                  }
               }

               if (value == null) {
                  value = grimReplaced;
               } else if (removeFormatting) {
                  value = CompiledDiscordTemplate.escapeMarkdown(value);
               }

               matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
            } while(matcher.find());

            matcher.appendTail(sb);
            grimReplaced = sb.toString();
            return GrimAPI.INSTANCE.getMessagePlaceHolderManager().replacePlaceholders(platformPlayer, grimReplaced).replace('\uffff', '%');
         }
      }
   }

   @NotNull
   public static Component replacePlaceholders(@NotNull GrimPlayer player, @NotNull Component component) {
      TextReplacementConfig safeReplacement = (TextReplacementConfig)TextReplacementConfig.builder().match("%[a-zA-Z0-9_]+%").replacement((placeholder) -> {
         return Component.text(replacePlaceholders(player, placeholder.content()));
      }).build();
      return component.replaceText(safeReplacement);
   }

   @NotNull
   public static Component miniMessage(@NotNull String string) {
      string = string.replace("%prefix%", GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("prefix", "&bGrim &8»"));
      Matcher matcher = HEX_PATTERN.matcher(string);
      StringBuilder sb = new StringBuilder(string.length());

      while(matcher.find()) {
         String var10002 = matcher.group(0);
         matcher.appendReplacement(sb, "<#" + var10002.replaceAll("[&§#x]", "") + ">");
      }

      string = matcher.appendTail(sb).toString();
      string = translateAlternateColorCodes('&', string).replace("§0", "<!b><!i><!u><!st><!obf><black>").replace("§1", "<!b><!i><!u><!st><!obf><dark_blue>").replace("§2", "<!b><!i><!u><!st><!obf><dark_green>").replace("§3", "<!b><!i><!u><!st><!obf><dark_aqua>").replace("§4", "<!b><!i><!u><!st><!obf><dark_red>").replace("§5", "<!b><!i><!u><!st><!obf><dark_purple>").replace("§6", "<!b><!i><!u><!st><!obf><gold>").replace("§7", "<!b><!i><!u><!st><!obf><gray>").replace("§8", "<!b><!i><!u><!st><!obf><dark_gray>").replace("§9", "<!b><!i><!u><!st><!obf><blue>").replace("§a", "<!b><!i><!u><!st><!obf><green>").replace("§b", "<!b><!i><!u><!st><!obf><aqua>").replace("§c", "<!b><!i><!u><!st><!obf><red>").replace("§d", "<!b><!i><!u><!st><!obf><light_purple>").replace("§e", "<!b><!i><!u><!st><!obf><yellow>").replace("§f", "<!b><!i><!u><!st><!obf><white>").replace("§r", "<reset>").replace("§k", "<obfuscated>").replace("§l", "<bold>").replace("§m", "<strikethrough>").replace("§n", "<underlined>").replace("§o", "<italic>");
      return MiniMessage.miniMessage().deserialize(string).compact();
   }

   public static Component getParsedComponent(Sender sender, String key, String fallbackText) {
      String message = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse(key, fallbackText);
      message = replacePlaceholders(sender, message);
      return miniMessage(message);
   }

   @Contract("_, _ -> new")
   @NotNull
   public static String translateAlternateColorCodes(char altColorChar, @NotNull String textToTranslate) {
      char[] b = textToTranslate.toCharArray();

      for(int i = 0; i < b.length - 1; ++i) {
         if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
            b[i] = 167;
            b[i + 1] = Character.toLowerCase(b[i + 1]);
         }
      }

      return new String(b);
   }

   @Contract("!null -> !null; null -> null")
   @Nullable
   public static String stripColor(@Nullable String input) {
      return input == null ? null : STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
   }

   @Generated
   private MessageUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
