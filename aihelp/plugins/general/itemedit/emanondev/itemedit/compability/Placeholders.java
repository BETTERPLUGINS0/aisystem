package emanondev.itemedit.compability;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.VersionUtils;
import java.util.Locale;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {
   public Placeholders() {
      ItemEdit.get().log("placeholders:");
      ItemEdit.get().log("  &e%itemedit_amount_&6<{itemid}>&e_&6[{slot}]&e_&6[{player}]&e%");
      ItemEdit.get().log("    shows how many &6itemid player &fhas on &6slot");
      ItemEdit.get().log("    <{itemid}> for item id on serveritem");
      ItemEdit.get().log("    [{slot}] for the slot where the item should be counted, by default &ainventory");
      ItemEdit.get().log("      Values: &einventory&f (include offhand), &eequip&f (include offhand), &einventoryandequip&f (include offhand), &ehand&f, &eoffhand&f, &ehead&f, &echest&f, &elegs&f, &efeet");
      ItemEdit.get().log("    [{player}] for the player, by default &aself");
      ItemEdit.get().log("    example: %itemedit_amount_{&6my_item_id&f}_{&6hand&f}%");
   }

   @NotNull
   public String getAuthor() {
      return "emanon";
   }

   @NotNull
   public String getIdentifier() {
      return "itemedit";
   }

   public String getRequiredPlugin() {
      return ItemEdit.get().getName();
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
            String var4 = args[0].toLowerCase(Locale.ENGLISH);
            byte var5 = -1;
            switch(var4.hashCode()) {
            case -1413853096:
               if (var4.equals("amount")) {
                  var5 = 0;
               }
            default:
               switch(var5) {
               case 0:
                  return this.amount(player, value.substring("amount_".length()));
               default:
                  throw new IllegalStateException();
               }
            }
         } catch (Exception var6) {
            ItemEdit.get().log("&c! &fWrong PlaceHolderValue %" + this.getIdentifier() + "_" + ChatColor.YELLOW + value + ChatColor.WHITE + "% " + var6.getMessage());
            return null;
         }
      }
   }

   private String amount(Player player, String value) {
      String slot = "inventory";
      int amount = 0;
      int indexStart = value.indexOf("{");
      int indexEnd = value.indexOf("}", indexStart);
      if (indexStart == 0 && indexEnd != -1) {
         String id = value.substring(indexStart + 1, indexEnd);
         ItemStack item = ItemEdit.get().getServerStorage().getItem(id, player);
         if (item == null) {
            throw new IllegalStateException("item id '" + id + "' is invalid or doesn't exist");
         } else {
            value = value.substring(indexEnd + 1);
            if (!value.isEmpty()) {
               indexStart = value.indexOf("{");
               indexEnd = value.indexOf("}");
               if (indexStart != 1) {
                  throw new IllegalStateException("bad formatting");
               }

               if (indexEnd == -1) {
                  throw new IllegalStateException("slot value not closed inside { }");
               }

               slot = value.substring(indexStart + 1, indexEnd).toLowerCase(Locale.ENGLISH);
               ItemEdit.get().log(id + " " + slot + " " + player.getName());
               value = value.substring(indexEnd + 1);
               indexStart = value.indexOf("{", indexEnd);
               indexEnd = value.indexOf("}", indexStart);
               if (indexStart > indexEnd) {
                  throw new IllegalStateException();
               }

               if (indexStart != -1) {
                  player = Bukkit.getPlayer(value.substring(indexStart + 1, indexEnd));
               }

               if (player == null) {
                  throw new IllegalStateException();
               }
            }

            String var9 = slot.toLowerCase(Locale.ENGLISH);
            byte var10 = -1;
            switch(var9.hashCode()) {
            case -2020599460:
               if (var9.equals("inventory")) {
                  var10 = 9;
               }
               break;
            case -1548738978:
               if (var9.equals("offhand")) {
                  var10 = 4;
               }
               break;
            case -1230538219:
               if (var9.equals("inventoryandequip")) {
                  var10 = 11;
               }
               break;
            case -774383297:
               if (var9.equals("off_hand")) {
                  var10 = 3;
               }
               break;
            case -251388107:
               if (var9.equals("main_hand")) {
                  var10 = 0;
               }
               break;
            case -7847512:
               if (var9.equals("mainhand")) {
                  var10 = 1;
               }
               break;
            case 3138990:
               if (var9.equals("feet")) {
                  var10 = 8;
               }
               break;
            case 3194991:
               if (var9.equals("hand")) {
                  var10 = 2;
               }
               break;
            case 3198432:
               if (var9.equals("head")) {
                  var10 = 7;
               }
               break;
            case 3317797:
               if (var9.equals("legs")) {
                  var10 = 5;
               }
               break;
            case 94627585:
               if (var9.equals("chest")) {
                  var10 = 6;
               }
               break;
            case 96757808:
               if (var9.equals("equip")) {
                  var10 = 10;
               }
            }

            ItemStack[] var11;
            int var12;
            int var13;
            ItemStack copy;
            ItemStack copy;
            switch(var10) {
            case 0:
            case 1:
            case 2:
               copy = ItemUtils.getHandItem(player);
               if (item.isSimilar(copy)) {
                  amount += copy.getAmount();
               }
               break;
            case 3:
            case 4:
               copy = player.getEquipment().getItemInOffHand();
               if (item.isSimilar(copy)) {
                  amount += copy.getAmount();
               }
               break;
            case 5:
               copy = player.getEquipment().getLeggings();
               if (item.isSimilar(copy)) {
                  amount += copy.getAmount();
               }
               break;
            case 6:
               copy = player.getEquipment().getChestplate();
               if (item.isSimilar(copy)) {
                  amount += copy.getAmount();
               }
               break;
            case 7:
               copy = player.getEquipment().getHelmet();
               if (item.isSimilar(copy)) {
                  amount += copy.getAmount();
               }
               break;
            case 8:
               copy = player.getEquipment().getBoots();
               if (item.isSimilar(copy)) {
                  amount += copy.getAmount();
               }
               break;
            case 9:
               var11 = player.getInventory().getStorageContents();
               var12 = var11.length;

               for(var13 = 0; var13 < var12; ++var13) {
                  copy = var11[var13];
                  if (item.isSimilar(copy)) {
                     amount += copy.getAmount();
                  }
               }

               if (VersionUtils.isVersionAfter(1, 9) && item.isSimilar(player.getInventory().getItemInOffHand())) {
                  amount += player.getInventory().getItemInOffHand().getAmount();
               }
               break;
            case 10:
               var11 = player.getInventory().getArmorContents();
               var12 = var11.length;

               for(var13 = 0; var13 < var12; ++var13) {
                  copy = var11[var13];
                  if (item.isSimilar(copy)) {
                     amount += copy.getAmount();
                  }
               }

               if (VersionUtils.isVersionAfter(1, 9) && item.isSimilar(player.getInventory().getItemInOffHand())) {
                  amount += player.getInventory().getItemInOffHand().getAmount();
               }
               break;
            case 11:
               var11 = player.getInventory().getStorageContents();
               var12 = var11.length;

               for(var13 = 0; var13 < var12; ++var13) {
                  copy = var11[var13];
                  if (item.isSimilar(copy)) {
                     amount += copy.getAmount();
                  }
               }

               var11 = player.getInventory().getArmorContents();
               var12 = var11.length;

               for(var13 = 0; var13 < var12; ++var13) {
                  copy = var11[var13];
                  if (item.isSimilar(copy)) {
                     amount += copy.getAmount();
                  }
               }

               if (VersionUtils.isVersionAfter(1, 9) && item.isSimilar(player.getInventory().getItemInOffHand())) {
                  amount += player.getInventory().getItemInOffHand().getAmount();
               }
               break;
            default:
               throw new IllegalStateException();
            }

            return String.valueOf(amount);
         }
      } else {
         throw new IllegalStateException("item id not closed inside { }");
      }
   }

   public boolean persist() {
      return true;
   }

   public boolean canRegister() {
      return true;
   }
}
