package emanondev.itemtag.command.itemtag;

import emanondev.itemedit.CooldownAPI;
import emanondev.itemedit.Util;
import emanondev.itemedit.UtilsString;
import emanondev.itemedit.command.AbstractCommand;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.InventoryUtils;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.InventoryUtils.ExcessMode;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.TagItem;
import emanondev.itemtag.command.ListenerSubCmd;
import emanondev.itemtag.equipmentchange.EquipmentChangeEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WearPermission extends ListenerSubCmd {
   private static final String WEAR_KEY;
   private static final String WEARMSG_KEY;

   public WearPermission(AbstractCommand cmd) {
      super("wearpermission", cmd, true, true);
   }

   public static void setUseKey(@NotNull TagItem item, @Nullable String value) {
      if (value != null && !value.isEmpty()) {
         item.setTag(WEAR_KEY, value);
      } else {
         item.removeTag(WEAR_KEY);
      }

   }

   public static void setUseMsgKey(@NotNull TagItem item, @Nullable String value) {
      if (value != null && !value.isEmpty()) {
         item.setTag(WEARMSG_KEY, value);
      } else {
         item.removeTag(WEARMSG_KEY);
      }

   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      if (args.length == 1) {
         this.onFail(p, alias);
      } else {
         ItemStack item = this.getItemInHand(p);
         TagItem tagItem = ItemTag.getTagItem(item);
         String var7 = args[1].toLowerCase(Locale.ENGLISH);
         byte var8 = -1;
         switch(var7.hashCode()) {
         case -2034700367:
            if (var7.equals("setpermission")) {
               var8 = 0;
            }
            break;
         case -2031029915:
            if (var7.equals("setmessage")) {
               var8 = 1;
            }
         }

         String permission;
         String feedback;
         switch(var8) {
         case 0:
            if (args.length > 3) {
               this.onFail(p, alias);
               return;
            }

            permission = args.length == 2 ? null : args[2].toLowerCase(Locale.ENGLISH);
            setUseKey(tagItem, permission);
            if (permission != null) {
               feedback = this.getLanguageString("setpermission.feedback", (String)null, p, new String[]{"%value%", permission});
            } else {
               feedback = this.getLanguageString("setpermission.feedback-reset", (String)null, p, new String[0]);
            }

            Util.sendMessage(p, feedback);
            return;
         case 1:
            if (args.length == 2) {
               setUseMsgKey(tagItem, (String)null);
               permission = this.getLanguageString("setmessage.feedback-reset", (String)null, p, new String[0]);
               Util.sendMessage(p, permission);
               return;
            }

            permission = UtilsString.fix(String.join(" ", Arrays.asList(args).subList(2, args.length)), (Player)null, true, new String[0]);
            setUseMsgKey(tagItem, permission);
            feedback = this.getLanguageString("setmessage.feedback", (String)null, p, new String[]{"%value%", permission});
            Util.sendMessage(p, feedback);
            return;
         default:
         }
      }
   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return args.length == 2 ? CompleteUtility.complete(args[1], new String[]{"setpermission", "setmessage"}) : Collections.emptyList();
   }

   @EventHandler
   public void event(EquipmentChangeEvent event) {
      if (Objects.requireNonNull(event.getSlotType()) != EquipmentSlot.HAND) {
         Player player = event.getPlayer();
         if (!this.canWear(player, event.getTo())) {
            Bukkit.getScheduler().runTaskLater(this.getPlugin(), () -> {
               if (player.isOnline()) {
                  ItemStack originalItem = InventoryUtils.getItem(player, event.getSlotType());
                  if (!ItemUtils.isAirOrNull(originalItem)) {
                     ItemStack item = originalItem.clone();
                     if (!this.canWear(player, originalItem)) {
                        player.getInventory().setItem(event.getSlotType(), (ItemStack)null);
                        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
                        InventoryUtils.giveAmount(player, item, item.getAmount(), ExcessMode.DROP_EXCESS);
                        ItemTag.get().getEquipChangeListener().onEquipChange(player, EquipmentChangeEvent.EquipMethod.UNKNOWN, event.getSlotType(), item, (ItemStack)null);
                        TagItem tagItem = ItemTag.getTagItem(item);
                        String perm = tagItem.getString(WEAR_KEY);
                        if (tagItem.hasStringTag(WEARMSG_KEY)) {
                           CooldownAPI api = ItemTag.get().getCooldownAPI();
                           String cooldownKey = "wear_" + perm.replace(".", "_");
                           if (api.hasCooldown(player, cooldownKey)) {
                              return;
                           }

                           api.setCooldown(player, cooldownKey, 1L, TimeUnit.SECONDS);
                           Util.sendMessage(player, UtilsString.fix(tagItem.getString(WEARMSG_KEY), player, true, new String[]{"%permission%", perm}));
                        }

                     }
                  }
               }
            }, 1L);
         }
      }
   }

   private boolean canWear(Player player, ItemStack itemStack) {
      if (ItemUtils.isAirOrNull(itemStack)) {
         return true;
      } else {
         TagItem tagItem = ItemTag.getTagItem(itemStack);
         if (!tagItem.hasStringTag(WEAR_KEY)) {
            return true;
         } else {
            String perm = tagItem.getString(WEAR_KEY);
            return player.hasPermission(perm);
         }
      }
   }

   static {
      WEAR_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":wearperm";
      WEARMSG_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":wearpermmsg";
   }
}
