package github.nighter.smartspawner.logging.discord;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.logging.SpawnerEventType;
import github.nighter.smartspawner.logging.SpawnerLogEntry;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Location;

public class DiscordEmbedBuilder {
   private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());

   public static DiscordEmbed buildEmbed(SpawnerLogEntry entry, DiscordWebhookConfig config, SmartSpawner plugin) {
      DiscordEmbed embed = new DiscordEmbed();
      embed.setColor(config.getColorForEvent(entry.getEventType()));
      Map<String, String> placeholders = buildPlaceholders(entry);
      String eventIcon = getEventIcon(entry.getEventType());
      String title = eventIcon + " " + replacePlaceholders(config.getEmbedTitle(), placeholders);
      embed.setTitle(title);
      String description = buildCompactDescription(entry, placeholders, config);
      embed.setDescription(description);
      String footer = replacePlaceholders(config.getEmbedFooter(), placeholders);
      embed.setFooter(footer, "https://images.minecraft-heads.com/render2d/head/2e/2eaa2d8b7e9a098ebd33fcb6cf1120f4.webp");
      embed.setTimestamp(Instant.ofEpochMilli(System.currentTimeMillis()));
      if (config.isShowPlayerHead() && entry.getPlayerName() != null) {
         embed.setThumbnail(getPlayerAvatarUrl(entry.getPlayerName()));
      }

      addCompactFields(embed, entry);
      Iterator var9 = config.getCustomFields().iterator();

      while(var9.hasNext()) {
         DiscordWebhookConfig.EmbedField customField = (DiscordWebhookConfig.EmbedField)var9.next();
         String fieldName = replacePlaceholders(customField.getName(), placeholders);
         String fieldValue = replacePlaceholders(customField.getValue(), placeholders);
         embed.addField(fieldName, fieldValue, customField.isInline());
      }

      return embed;
   }

   private static String buildCompactDescription(SpawnerLogEntry entry, Map<String, String> placeholders, DiscordWebhookConfig config) {
      StringBuilder desc = new StringBuilder();
      String mainDesc = replacePlaceholders(config.getEmbedDescription(), placeholders);
      desc.append(mainDesc);
      desc.append("\n\n");
      if (entry.getPlayerName() != null) {
         desc.append("\ud83d\udc64 `").append(entry.getPlayerName()).append("`");
      }

      if (entry.getLocation() != null) {
         Location loc = entry.getLocation();
         if (entry.getPlayerName() != null) {
            desc.append(" • ");
         }

         desc.append("\ud83d\udccd `").append(loc.getWorld().getName()).append(" (").append(loc.getBlockX()).append(", ").append(loc.getBlockY()).append(", ").append(loc.getBlockZ()).append(")`");
      }

      if (entry.getEntityType() != null) {
         desc.append("\n\ud83d\udc3e `").append(formatEntityName(entry.getEntityType().name())).append("`");
      }

      return desc.toString();
   }

   private static void addCompactFields(DiscordEmbed embed, SpawnerLogEntry entry) {
      Map<String, Object> metadata = entry.getMetadata();
      if (!metadata.isEmpty()) {
         int fieldCount = 0;
         int maxFields = 6;

         for(Iterator var5 = metadata.entrySet().iterator(); var5.hasNext(); ++fieldCount) {
            Entry<String, Object> metaEntry = (Entry)var5.next();
            if (fieldCount >= maxFields) {
               break;
            }

            String key = formatFieldName((String)metaEntry.getKey());
            String icon = getFieldIcon((String)metaEntry.getKey());
            Object value = metaEntry.getValue();
            String formattedValue = formatCompactValue(value);
            embed.addField(icon + " " + key, formattedValue, true);
         }

      }
   }

   private static String formatCompactValue(Object value) {
      if (value == null) {
         return "`N/A`";
      } else if (value instanceof Number) {
         Number num = (Number)value;
         if (!(value instanceof Double) && !(value instanceof Float)) {
            return "`" + num.toString() + "`";
         } else {
            Object[] var10001 = new Object[]{num.doubleValue()};
            return "`" + String.format("%.2f", var10001) + "`";
         }
      } else {
         String str = String.valueOf(value);
         if (str.length() > 50) {
            str = str.substring(0, 47) + "...";
         }

         return "`" + str + "`";
      }
   }

   private static Map<String, String> buildPlaceholders(SpawnerLogEntry entry) {
      Map<String, String> placeholders = new HashMap();
      placeholders.put("description", entry.getEventType().getDescription());
      placeholders.put("event_type", entry.getEventType().name());
      placeholders.put("time", FORMATTER.format(Instant.ofEpochMilli(System.currentTimeMillis())));
      if (entry.getPlayerName() != null) {
         placeholders.put("player", entry.getPlayerName());
      }

      if (entry.getPlayerUuid() != null) {
         placeholders.put("player_uuid", entry.getPlayerUuid().toString());
      }

      if (entry.getLocation() != null) {
         Location loc = entry.getLocation();
         placeholders.put("world", loc.getWorld().getName());
         placeholders.put("x", String.valueOf(loc.getBlockX()));
         placeholders.put("y", String.valueOf(loc.getBlockY()));
         placeholders.put("z", String.valueOf(loc.getBlockZ()));
         placeholders.put("location", String.format("%s (%d, %d, %d)", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
      }

      if (entry.getEntityType() != null) {
         placeholders.put("entity", formatEntityName(entry.getEntityType().name()));
      }

      Iterator var4 = entry.getMetadata().entrySet().iterator();

      while(var4.hasNext()) {
         Entry<String, Object> metaEntry = (Entry)var4.next();
         placeholders.put((String)metaEntry.getKey(), String.valueOf(metaEntry.getValue()));
      }

      return placeholders;
   }

   private static String replacePlaceholders(String text, Map<String, String> placeholders) {
      if (text == null) {
         return "";
      } else {
         String result = text;

         Entry entry;
         for(Iterator var3 = placeholders.entrySet().iterator(); var3.hasNext(); result = result.replace("{" + (String)entry.getKey() + "}", (CharSequence)entry.getValue())) {
            entry = (Entry)var3.next();
         }

         return result;
      }
   }

   private static String getPlayerAvatarUrl(String playerName) {
      return "https://mc-heads.net/avatar/" + playerName + "/64.png";
   }

   private static String formatFieldName(String fieldName) {
      String[] words = fieldName.split("_");
      StringBuilder result = new StringBuilder();
      String[] var3 = words;
      int var4 = words.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String word = var3[var5];
         if (!word.isEmpty()) {
            result.append(Character.toUpperCase(word.charAt(0)));
            if (word.length() > 1) {
               result.append(word.substring(1).toLowerCase());
            }

            result.append(" ");
         }
      }

      return result.toString().trim();
   }

   private static String formatEntityName(String entityType) {
      if (entityType != null && !entityType.isEmpty()) {
         String[] words = entityType.toLowerCase().split("_");
         StringBuilder result = new StringBuilder();
         String[] var3 = words;
         int var4 = words.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String word = var3[var5];
            if (!word.isEmpty()) {
               result.append(Character.toUpperCase(word.charAt(0)));
               if (word.length() > 1) {
                  result.append(word.substring(1));
               }

               result.append(" ");
            }
         }

         return result.toString().trim();
      } else {
         return entityType;
      }
   }

   private static String getFieldIcon(String fieldName) {
      String lower = fieldName.toLowerCase();
      if (lower.contains("command")) {
         return "⚙️";
      } else if (!lower.contains("amount") && !lower.contains("count")) {
         if (lower.contains("quantity")) {
            return "\ud83d\udcca";
         } else if (!lower.contains("price") && !lower.contains("cost") && !lower.contains("money")) {
            if (!lower.contains("exp") && !lower.contains("experience")) {
               if (lower.contains("stack")) {
                  return "\ud83d\udcda";
               } else {
                  return lower.contains("type") ? "\ud83c\udff7️" : "•";
               }
            } else {
               return "✨";
            }
         } else {
            return "\ud83d\udcb0";
         }
      } else {
         return "\ud83d\udd22";
      }
   }

   private static String getEventIcon(SpawnerEventType eventType) {
      String eventName = eventType.name();
      if (eventName.startsWith("COMMAND_")) {
         if (eventName.contains("PLAYER")) {
            return "\ud83d\udc64";
         } else if (eventName.contains("CONSOLE")) {
            return "\ud83d\udda5️";
         } else {
            return eventName.contains("RCON") ? "\ud83d\udd0c" : "⚙️";
         }
      } else if (eventName.equals("SPAWNER_PLACE")) {
         return "✅";
      } else if (eventName.equals("SPAWNER_BREAK")) {
         return "❌";
      } else if (eventName.equals("SPAWNER_EXPLODE")) {
         return "\ud83d\udca5";
      } else if (eventName.contains("STACK_HAND")) {
         return "✋";
      } else if (eventName.contains("STACK_GUI")) {
         return "\ud83d\udce6";
      } else if (eventName.contains("DESTACK")) {
         return "\ud83d\udce4";
      } else if (eventName.contains("GUI_OPEN")) {
         return "\ud83d\udccb";
      } else if (eventName.contains("STORAGE_OPEN")) {
         return "\ud83d\udce6";
      } else if (eventName.contains("STACKER_OPEN")) {
         return "\ud83d\udd22";
      } else if (eventName.contains("EXP_CLAIM")) {
         return "✨";
      } else if (eventName.contains("SELL_ALL")) {
         return "\ud83d\udcb0";
      } else if (eventName.contains("ITEM_TAKE_ALL")) {
         return "\ud83c\udf92";
      } else if (eventName.contains("ITEM_DROP")) {
         return "\ud83d\uddd1️";
      } else if (eventName.contains("ITEMS_SORT")) {
         return "\ud83d\udd03";
      } else if (eventName.contains("ITEM_FILTER")) {
         return "\ud83d\udd0d";
      } else if (eventName.contains("DROP_PAGE_ITEMS")) {
         return "\ud83d\udcc4";
      } else {
         return eventName.contains("EGG_CHANGE") ? "\ud83e\udd5a" : "\ud83d\udccc";
      }
   }
}
