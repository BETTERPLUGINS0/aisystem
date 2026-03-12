package emanondev.itemtag.compability;

import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.actions.ActionsUtility;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {
   public Placeholders() {
      ItemTag.get().log("Hooked into PlaceHolderAPI:");
      ItemTag.get().log("placeholders:");
      ItemTag.get().log("  &e%itemtag_cooldown_&6<timeunit>&e_&6[cooldownid]&e%");
      ItemTag.get().log("    shows how much cooldown has selected cooldownid for player");
      ItemTag.get().log("    <timeunit> may be &eh&f, &es &for &ems");
      ItemTag.get().log("    [cooldownid] for cooldown type, by default &adefault");
      ItemTag.get().log("    example: %itemtag_cooldown_s_anid%");
      ItemTag.get().log("  &e%itemtag_handcooldown_&6<timeunit>&e%");
      ItemTag.get().log("    shows how much cooldown has player on the item in his hand");
      ItemTag.get().log("    <timeunit> may be &eh&f, &es &for &ems");
      ItemTag.get().log("    example: %itemtag_handcooldown_s%");
   }

   @NotNull
   public String getAuthor() {
      return "emanon";
   }

   @NotNull
   public String getIdentifier() {
      return "itemtag";
   }

   public String getRequiredPlugin() {
      return ItemTag.get().getName();
   }

   @NotNull
   public String getVersion() {
      return "1.0";
   }

   public String onPlaceholderRequest(Player player, @NotNull String value) {
      if (player == null) {
         return "";
      } else {
         try {
            String[] args = value.split("_");
            String var5 = args[0].toLowerCase(Locale.ENGLISH);
            byte var6 = -1;
            switch(var5.hashCode()) {
            case -546109589:
               if (var5.equals("cooldown")) {
                  var6 = 0;
               }
               break;
            case -264845869:
               if (var5.equals("usesleft")) {
                  var6 = 2;
               }
               break;
            case 1284499674:
               if (var5.equals("handcooldown")) {
                  var6 = 1;
               }
            }

            String id;
            ItemStack item;
            switch(var6) {
            case 0:
               id = args.length >= 3 ? value.substring(2 + args[0].length() + args[1].length()) : ActionsUtility.getDefaultCooldownId();
               break;
            case 1:
               item = ItemUtils.getHandItem(player);
               if (ItemUtils.isAirOrNull(item)) {
                  return "0";
               }

               id = ActionsUtility.getCooldownId(ItemTag.getTagItem(item));
               break;
            case 2:
               item = ItemUtils.getHandItem(player);
               if (ItemUtils.isAirOrNull(item)) {
                  return "0";
               }

               return String.valueOf(ActionsUtility.getUses(ItemTag.getTagItem(item)));
            default:
               throw new IllegalStateException();
            }

            var5 = args[1].toLowerCase(Locale.ENGLISH);
            var6 = -1;
            switch(var5.hashCode()) {
            case 104:
               if (var5.equals("h")) {
                  var6 = 0;
               }
               break;
            case 109:
               if (var5.equals("m")) {
                  var6 = 2;
               }
               break;
            case 115:
               if (var5.equals("s")) {
                  var6 = 1;
               }
               break;
            case 3494:
               if (var5.equals("ms")) {
                  var6 = 3;
               }
            }

            switch(var6) {
            case 0:
               return String.valueOf(ItemTag.get().getCooldownAPI().getCooldown(player, id, TimeUnit.HOURS));
            case 1:
               return String.valueOf(ItemTag.get().getCooldownAPI().getCooldown(player, id, TimeUnit.SECONDS));
            case 2:
               return String.valueOf(ItemTag.get().getCooldownAPI().getCooldown(player, id, TimeUnit.MINUTES));
            case 3:
               return String.valueOf(ItemTag.get().getCooldownAPI().getCooldown(player, id, TimeUnit.MILLISECONDS));
            default:
               throw new IllegalStateException();
            }
         } catch (Exception var8) {
            ItemTag.get().log("&c! &fWrong PlaceHolderValue %" + this.getIdentifier() + "_" + ChatColor.YELLOW + value + ChatColor.WHITE + "%");
            var8.printStackTrace();
            return null;
         }
      }
   }

   public boolean persist() {
      return true;
   }

   public boolean canRegister() {
      return true;
   }
}
