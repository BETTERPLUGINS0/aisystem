package emanondev.itemtag.actions;

import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.TagItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

public final class ActionsUtility {
   public static final String TYPE_SEPARATOR = "%%:%%";
   public static final String ACTIONS_KEY;
   public static final String ACTION_USES_KEY;
   public static final String ACTION_MAXUSES_KEY;
   public static final String ACTION_DISPLAYUSES_KEY;
   public static final String ACTION_CONSUME_AT_END_KEY;
   public static final String ACTION_VISUAL_COOLDOWN;
   public static final String ACTION_COOLDOWN_KEY;
   public static final String ACTION_COOLDOWN_MSG_KEY;
   public static final String ACTION_COOLDOWN_MSG_TYPE_KEY;
   public static final String ACTION_COOLDOWN_ID_KEY;
   public static final String ACTION_PERMISSION_KEY;
   private static final String DEFAULT_COOLDOWN_ID = "default";

   public static boolean hasActions(TagItem item) {
      return item.hasStringListTag(ACTIONS_KEY);
   }

   @Nullable
   public static List<String> getActions(TagItem item) {
      return item.getStringList(ACTIONS_KEY);
   }

   public static void setActions(TagItem item, List<String> actions) {
      if (actions != null && !actions.isEmpty()) {
         item.setTag(ACTIONS_KEY, actions);
      } else {
         item.removeTag(ACTIONS_KEY);
      }

   }

   public static int getUses(TagItem item) {
      return item.getInteger(ACTION_USES_KEY, 1);
   }

   public static void setUses(TagItem item, int amount) {
      if (amount == 1) {
         item.removeTag(ACTION_USES_KEY);
      } else {
         item.setTag(ACTION_USES_KEY, Math.max(-1, amount));
      }

   }

   public static int getMaxUses(TagItem item) {
      return item.getInteger(ACTION_MAXUSES_KEY, -1);
   }

   public static void setMaxUses(TagItem item, int amount) {
      if (amount <= -1) {
         item.removeTag(ACTION_MAXUSES_KEY);
      } else {
         item.setTag(ACTION_MAXUSES_KEY, amount);
      }

   }

   public static boolean getDisplayUses(TagItem item) {
      return item.hasBooleanTag(ACTION_DISPLAYUSES_KEY);
   }

   public static void setDisplayUses(TagItem item, boolean value) {
      if (!value) {
         item.removeTag(ACTION_DISPLAYUSES_KEY);
      } else {
         item.setTag(ACTION_DISPLAYUSES_KEY, true);
      }

   }

   public static boolean getConsume(TagItem item) {
      return !item.hasBooleanTag(ACTION_CONSUME_AT_END_KEY);
   }

   public static void setConsume(TagItem item, boolean value) {
      if (value) {
         item.removeTag(ACTION_CONSUME_AT_END_KEY);
      } else {
         item.setTag(ACTION_CONSUME_AT_END_KEY, true);
      }

   }

   public static int getCooldownMs(TagItem item) {
      return item.getInteger(ACTION_COOLDOWN_KEY, 0);
   }

   public static void setCooldownMs(TagItem item, int amount) {
      if (amount <= 0) {
         item.removeTag(ACTION_COOLDOWN_KEY);
      } else {
         item.setTag(ACTION_COOLDOWN_KEY, amount);
      }

   }

   public static String getCooldownId(TagItem item) {
      return item.getString(ACTION_COOLDOWN_ID_KEY, "default");
   }

   public static void setCooldownId(TagItem item, String value) {
      if (value != null && !value.isEmpty() && !value.equalsIgnoreCase("default")) {
         item.setTag(ACTION_COOLDOWN_ID_KEY, value.toLowerCase(Locale.ENGLISH));
      } else {
         item.removeTag(ACTION_COOLDOWN_ID_KEY);
      }

   }

   public static String getPermission(TagItem item) {
      return item.getString(ACTION_PERMISSION_KEY, (String)null);
   }

   public static void setPermission(TagItem item, String value) {
      if (value != null && !value.isEmpty()) {
         item.setTag(ACTION_PERMISSION_KEY, value.toLowerCase(Locale.ENGLISH));
      } else {
         item.removeTag(ACTION_PERMISSION_KEY);
      }

   }

   public static String getDefaultCooldownId() {
      return "default";
   }

   public static void setVisualCooldown(TagItem item, boolean value) {
      if (value) {
         item.setTag(ACTION_VISUAL_COOLDOWN, true);
      } else {
         item.removeTag(ACTION_VISUAL_COOLDOWN);
      }

   }

   public static boolean getVisualCooldown(TagItem item) {
      return item.hasBooleanTag(ACTION_VISUAL_COOLDOWN);
   }

   public static void updateUsesDisplay(ItemStack item) {
      TagItem tagItem = ItemTag.getTagItem(item);
      boolean show = getDisplayUses(tagItem);
      Map<String, Object> metaMap = new LinkedHashMap(ItemUtils.getMeta(item).serialize());
      if (show && !metaMap.containsKey("lore")) {
         metaMap.put("lore", new ArrayList());
      }

      if (metaMap.containsKey("lore")) {
         List<String> lore = new ArrayList((Collection)metaMap.get("lore"));
         lore.removeIf((line) -> {
            return line.contains("\"translate\":\"item.durability\"") || line.contains("\"text\":\"Durability: ") && line.contains(" / ") && line.contains("\"color\":\"white\"");
         });
         if (show) {
            int uses = getUses(tagItem);
            int maxUses = getMaxUses(tagItem);
            lore.add("{\"italic\":false,\"color\":\"white\",\"translate\":\"item.durability\",\"with\":[{\"text\":\"" + (uses == -1 ? "∞" : uses) + "\"},{\"text\":\"" + (maxUses == -1 ? "∞" : maxUses) + "\"}]}");
         }

         if (!lore.isEmpty()) {
            metaMap.put("lore", lore);
         } else {
            metaMap.remove("lore");
         }
      }

      metaMap.put("==", "ItemMeta");
      item.setItemMeta((ItemMeta)ConfigurationSerialization.deserializeObject(metaMap));
   }

   public static void setCooldownMsg(TagItem item, String value) {
      if (value != null && !value.isEmpty()) {
         item.setTag(ACTION_COOLDOWN_MSG_KEY, value);
      } else {
         item.removeTag(ACTION_COOLDOWN_MSG_KEY);
      }

   }

   public static void setCooldownMsgType(TagItem item, String value) {
      if (value != null && !value.isEmpty() && !Objects.equals(value, "chat")) {
         item.setTag(ACTION_COOLDOWN_MSG_TYPE_KEY, value.toLowerCase(Locale.ENGLISH));
      } else {
         item.removeTag(ACTION_COOLDOWN_MSG_TYPE_KEY);
      }

   }

   public static String getCooldownMsg(TagItem item) {
      return item.getString(ACTION_COOLDOWN_MSG_KEY, (String)null);
   }

   public static String getCooldownMsgType(TagItem item) {
      return item.getString(ACTION_COOLDOWN_MSG_TYPE_KEY, "chat");
   }

   static {
      ACTIONS_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":actions";
      ACTION_USES_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":uses";
      ACTION_MAXUSES_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":maxuses";
      ACTION_DISPLAYUSES_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":displayuses";
      ACTION_CONSUME_AT_END_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":consume";
      ACTION_VISUAL_COOLDOWN = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":visualcooldown";
      ACTION_COOLDOWN_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":cooldown";
      ACTION_COOLDOWN_MSG_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":cooldown_msg";
      ACTION_COOLDOWN_MSG_TYPE_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":cooldown_msg_type";
      ACTION_COOLDOWN_ID_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":cooldown_id";
      ACTION_PERMISSION_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":permission";
   }
}
