package emanondev.itemtag.activity;

import emanondev.itemtag.ItemTag;
import emanondev.itemtag.TagItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TriggerHandler {
   private static final String TRIGGERS_ACTIVITY_KEY;
   private static final String TRIGGERS_SLOTS_KEY;
   private static final String TRIGGER_COOLDOWN_KEY;
   private static final String TRIGGER_USES_KEY;
   private static final String TRIGGER_MAXUSES_KEY;
   private static final String TRIGGER_NOT_CONSUME_AT_END_KEY;
   private static final String TRIGGER_DISPLAY_USES;
   private static final String TRIGGER_VISUAL_COOLDOWN;
   private static final String TRIGGER_PERMISSION_KEY;
   private static final HashMap<String, TriggerType> triggers;

   public static boolean hasTrigger(@NotNull TriggerType trigger, @NotNull TagItem item) {
      return getTriggerActivity(trigger, item) != null;
   }

   public static String getTriggerActivityId(@NotNull TriggerType trigger, @NotNull TagItem item) {
      return item.getString(TRIGGERS_ACTIVITY_KEY + trigger.getId());
   }

   @Nullable
   public static Activity getTriggerActivity(@NotNull TriggerType trigger, @NotNull TagItem item) {
      String id = getTriggerActivityId(trigger, item);
      return id == null ? null : ActivityManager.getActivity(id);
   }

   public static void setTriggerActivity(@NotNull TriggerType trigger, @NotNull TagItem item, @Nullable Activity activity) {
      if (activity == null) {
         item.removeTag(TRIGGERS_ACTIVITY_KEY + trigger.getId());
      } else {
         item.setTag(TRIGGERS_ACTIVITY_KEY + trigger.getId(), activity.getId());
      }

   }

   @NotNull
   public static Collection<EquipmentSlot> getAllowedSlots(@NotNull TriggerType trigger, @NotNull TagItem item) {
      String values = item.getString(TRIGGERS_SLOTS_KEY + trigger.getId());
      if (values != null && !values.isEmpty()) {
         EnumSet<EquipmentSlot> slots = EnumSet.noneOf(EquipmentSlot.class);
         String[] var4 = values.split(",");
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String slot = var4[var6];
            slots.add(EquipmentSlot.valueOf(slot));
         }

         return slots;
      } else {
         return EnumSet.allOf(EquipmentSlot.class);
      }
   }

   public static void setAllowedSlot(@NotNull TriggerType trigger, @NotNull TagItem item, @NotNull Collection<EquipmentSlot> slots) {
      StringBuilder value = null;
      boolean started = false;
      if (!slots.isEmpty() && slots.size() != EquipmentSlot.values().length) {
         value = new StringBuilder();

         for(Iterator var5 = slots.iterator(); var5.hasNext(); started = true) {
            EquipmentSlot slot = (EquipmentSlot)var5.next();
            value.append(started ? "," : "").append(slot.name());
         }
      }

      if (value == null) {
         item.removeTag(TRIGGERS_SLOTS_KEY + trigger.getId());
      } else {
         item.setTag(TRIGGERS_SLOTS_KEY + trigger.getId(), value.toString());
      }

   }

   public static boolean isConsumeAtUsesEnd(@NotNull TagItem item) {
      return !item.hasBooleanTag(TRIGGER_NOT_CONSUME_AT_END_KEY);
   }

   public static void setConsumeAtUsesEnd(@NotNull TagItem item, boolean value) {
      if (value) {
         item.removeTag(TRIGGER_NOT_CONSUME_AT_END_KEY);
      } else {
         item.setTag(TRIGGER_NOT_CONSUME_AT_END_KEY, true);
      }

   }

   public static boolean isDisplayUses(@NotNull TagItem item) {
      return item.hasBooleanTag(TRIGGER_DISPLAY_USES);
   }

   public static void setDisplayUses(@NotNull TagItem item, boolean value) {
      if (!value) {
         item.removeTag(TRIGGER_DISPLAY_USES);
      } else {
         item.setTag(TRIGGER_DISPLAY_USES, false);
      }

   }

   public static int getUsesLeft(@NotNull TagItem item) {
      Integer value = item.getInteger(TRIGGER_USES_KEY);
      return value == null ? 1 : value;
   }

   public static void setUsesLeft(@NotNull TagItem item, int value) {
      if (value == 1) {
         item.removeTag(TRIGGER_USES_KEY);
      } else {
         item.setTag(TRIGGER_USES_KEY, Math.max(-1, value));
      }

   }

   public static int getMaxUses(@NotNull TagItem item) {
      Integer value = item.getInteger(TRIGGER_MAXUSES_KEY);
      return value == null ? 1 : value;
   }

   public static void setMaxUses(@NotNull TagItem item, int value) {
      if (value == 1) {
         item.removeTag(TRIGGER_MAXUSES_KEY);
      } else {
         item.setTag(TRIGGER_MAXUSES_KEY, Math.max(-1, value));
      }

   }

   public static boolean hasVisualCooldown(@NotNull TagItem item) {
      return item.hasBooleanTag(TRIGGER_VISUAL_COOLDOWN);
   }

   public static void setVisualCooldown(@NotNull TagItem item, boolean value) {
      if (!value) {
         item.removeTag(TRIGGER_VISUAL_COOLDOWN);
      } else {
         item.setTag(TRIGGER_VISUAL_COOLDOWN, true);
      }

   }

   public static long getCooldownAmountMs(@NotNull TriggerType trigger, @NotNull TagItem item) {
      Integer value = item.getInteger(TRIGGER_COOLDOWN_KEY + trigger.getId());
      return value == null ? 0L : (long)(value * 10);
   }

   public static void setCooldownAmountMs(@NotNull TriggerType trigger, @NotNull TagItem item, long value) {
      if (value == 0L) {
         item.removeTag(TRIGGER_COOLDOWN_KEY + trigger.getId());
      } else {
         item.setTag(TRIGGER_COOLDOWN_KEY + trigger.getId(), (int)value / 10);
      }

   }

   public static String getCooldownId(@NotNull TriggerType trigger) {
      return "t_cd_" + trigger.getId();
   }

   public static String getPermission(@NotNull TagItem item) {
      return item.getString(TRIGGER_PERMISSION_KEY);
   }

   public static void setPermission(@NotNull TagItem item, String value) {
      if (value != null && !value.isEmpty()) {
         item.setTag(TRIGGER_PERMISSION_KEY, value);
      } else {
         item.removeTag(TRIGGER_PERMISSION_KEY);
      }

   }

   public static void registerTrigger(TriggerType trigger) {
      if (trigger == null) {
         throw new NullPointerException();
      } else if (triggers.containsKey(trigger.getId())) {
         throw new IllegalArgumentException();
      } else {
         triggers.put(trigger.getId().toLowerCase(Locale.ENGLISH), trigger);
      }
   }

   public static void clearTriggers() {
      triggers.clear();
   }

   public static List<TriggerType> getTriggers() {
      return new ArrayList(triggers.values());
   }

   public static List<String> getTypes() {
      return new ArrayList(triggers.keySet());
   }

   public static TriggerType getTrigger(String type) {
      return (TriggerType)triggers.get(type.toLowerCase(Locale.ENGLISH));
   }

   static {
      TRIGGERS_ACTIVITY_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":t_activity_";
      TRIGGERS_SLOTS_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":t_slots_";
      TRIGGER_COOLDOWN_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":t_cooldown_";
      TRIGGER_USES_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":t_uses";
      TRIGGER_MAXUSES_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":t_maxuses";
      TRIGGER_NOT_CONSUME_AT_END_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":t_notconsume";
      TRIGGER_DISPLAY_USES = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":t_displayuses";
      TRIGGER_VISUAL_COOLDOWN = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":t_visualcooldown";
      TRIGGER_PERMISSION_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":t_perm";
      triggers = new HashMap();
   }
}
