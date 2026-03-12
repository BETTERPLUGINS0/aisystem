package emanondev.itemtag.activity;

import emanondev.itemedit.utility.InventoryUtils;
import emanondev.itemedit.utility.InventoryUtils.ExcessMode;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.TagItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TriggerType<E extends Event> {
   private final String id;
   private final Class<E> eventType;

   public TriggerType(@NotNull String id, Class<E> e) {
      if (!Pattern.compile("[a-z][_a-z0-9]*").matcher(id).matches()) {
         throw new IllegalArgumentException();
      } else {
         this.id = id;
         this.eventType = e;
      }
   }

   public String getId() {
      return this.id;
   }

   public ItemStack handle(E event, @NotNull Player p, ItemStack item, EquipmentSlot slot) {
      TagItem tag = ItemTag.getTagItem(item);
      if (!tag.isValid()) {
         return item;
      } else {
         Activity activity = TriggerHandler.getTriggerActivity(this, tag);
         if (activity == null) {
            return item;
         } else if (!TriggerHandler.getAllowedSlots(this, tag).contains(slot)) {
            return item;
         } else {
            long ms = TriggerHandler.getCooldownAmountMs(this, tag);
            if (ms > 0L && ItemTag.get().getCooldownAPI().hasCooldown(p, TriggerHandler.getCooldownId(this))) {
               return item;
            } else {
               boolean satisfied = true;
               Iterator var10 = activity.getConditions().iterator();

               while(var10.hasNext()) {
                  ConditionType.Condition cond = (ConditionType.Condition)var10.next();
                  if (!cond.isCompatible(event)) {
                     ItemTag.get().log("Incompatible Condition &e" + cond + "&f from Activity &e" + activity.getId() + "&f used on Trigger &e" + this.getId());
                     return item;
                  }

                  try {
                     if (!cond.evaluate(p, item, event)) {
                        satisfied = false;
                        break;
                     }
                  } catch (Exception var14) {
                     var14.printStackTrace();
                     satisfied = false;
                     break;
                  }
               }

               if (!satisfied) {
                  this.executeActions(activity.getAlternativeActions(), event, item, p, activity, "(alternative actions)");
                  return item;
               } else {
                  int uses = TriggerHandler.getUsesLeft(tag);
                  int newUses = uses;
                  if (uses >= 0) {
                     int consumes = activity.getConsumes();
                     if (consumes > uses) {
                        this.executeActions(activity.getNoConsumesActions(), event, item, p, activity, "(no consume actions)");
                        return item;
                     }

                     int maxUses = TriggerHandler.getMaxUses(tag);
                     newUses = Math.max(0, maxUses > 0 ? Math.min(maxUses, uses - consumes) : uses - consumes);
                  }

                  this.executeActions(activity.getActions(), event, item, p, activity, "(actions)");
                  if (uses >= 0 && newUses != uses) {
                     if (item.getAmount() == 1) {
                        if (newUses == 0 && TriggerHandler.isConsumeAtUsesEnd(tag)) {
                           return null;
                        } else {
                           TriggerHandler.setUsesLeft(tag, newUses);
                           return item;
                        }
                     } else if (newUses == 0 && TriggerHandler.isConsumeAtUsesEnd(tag)) {
                        item.setAmount(item.getAmount() - 1);
                        return item;
                     } else {
                        ItemStack toGive = new ItemStack(item);
                        item.setAmount(item.getAmount() - 1);
                        TagItem toGiveTag = ItemTag.getTagItem(toGive);
                        TriggerHandler.setUsesLeft(toGiveTag, newUses);
                        this.updateUsesDisplay(item);
                        InventoryUtils.giveAmount(p, toGive, 1, ExcessMode.DROP_EXCESS);
                        return item;
                     }
                  } else {
                     return item;
                  }
               }
            }
         }
      }
   }

   private void executeActions(Collection<ActionType.Action> actions, E event, ItemStack item, Player p, Activity activity, String actionType) {
      Iterator var7 = actions.iterator();

      while(var7.hasNext()) {
         ActionType.Action action = (ActionType.Action)var7.next();
         if (!action.isAssignable(event)) {
            ItemTag.get().log("Incompatible Action &e" + action + "&f from Activity " + actionType + " &e" + activity.getId() + "&f used on Trigger &e" + this.getId() + "&f, skipping it");
         } else {
            try {
               action.execute(p, item, event);
            } catch (Exception var10) {
               var10.printStackTrace();
            }
         }
      }

   }

   private void updateUsesDisplay(ItemStack item) {
      TagItem tagItem = ItemTag.getTagItem(item);
      boolean show = TriggerHandler.isDisplayUses(tagItem);
      Map<String, Object> metaMap = new LinkedHashMap(item.getItemMeta().serialize());
      if (show && !metaMap.containsKey("lore")) {
         metaMap.put("lore", new ArrayList());
      }

      if (metaMap.containsKey("lore")) {
         List<String> lore = new ArrayList((Collection)metaMap.get("lore"));
         lore.removeIf((line) -> {
            return line.startsWith("{\"italic\":false,\"color\":\"white\",\"translate\":\"item.durability\",\"with\":[{\"text\":\"") && line.endsWith("\"}]}") || line.startsWith("{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"white\",\"text\":\"Durability:") && line.endsWith("\"}],\"text\":\"\"}");
         });
         if (show) {
            int uses = TriggerHandler.getUsesLeft(tagItem);
            int maxUses = TriggerHandler.getMaxUses(tagItem);
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

   public ItemStack getGuiItem(@Nullable Player player) {
      ItemStack mat;
      try {
         mat = new ItemStack(Material.valueOf(ItemTag.get().getConfig("triggers.yml").getString(this.getId() + ".gui_material", Material.STONE.name()).toUpperCase(Locale.ENGLISH)));
      } catch (Exception var4) {
         var4.printStackTrace();
         mat = new ItemStack(Material.STONE);
      }

      ItemMeta meta = mat.getItemMeta();
      meta.addItemFlags(ItemFlag.values());
      meta.setDisplayName(ChatColor.AQUA + this.getId());
      meta.setLore(ItemTag.get().getLanguageConfig(player).loadMultiMessage("trigger." + this.getId() + ".description", new ArrayList(), new String[0]));
      mat.setItemMeta(meta);
      return mat;
   }
}
