package emanondev.itemedit.gui;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.UtilsString;
import emanondev.itemedit.utility.ItemUtils;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ColorGui implements Gui {
   private static final String subPath = "gui.color.";
   private final Player target;
   private final Inventory inventory;
   private final ItemStack colorable;
   private final ItemMeta colorableMeta;
   private final ItemMeta cleanColorableMeta;

   public ColorGui(@NotNull Player target) {
      String title = this.getLanguageMessage("gui.color.title", new String[0]);
      this.inventory = Bukkit.createInventory(this, 54, title);
      this.target = target;
      this.colorable = ItemUtils.getHandItem(this.getTargetPlayer());
      this.colorableMeta = ItemUtils.getMeta(this.colorable);
      this.cleanColorableMeta = this.colorableMeta.clone();
      this.cleanColorableMeta.addItemFlags(ItemFlag.values());
      this.cleanColorableMeta.setDisplayName((String)null);
      this.cleanColorableMeta.setLore((List)null);

      try {
         this.cleanColorableMeta.setMaxStackSize(100);
      } catch (Throwable var4) {
      }

      this.updateInventory();
   }

   public void onClose(InventoryCloseEvent event) {
      try {
         this.target.getInventory().setItemInMainHand(this.colorable);
      } catch (Throwable var3) {
         this.target.getInventory().setItemInHand(this.colorable);
      }

   }

   public void onClick(InventoryClickEvent event) {
      Color original = ItemUtils.getColor(this.colorableMeta);
      int[] colors = this.fromColor(original);
      int var10002;
      switch(event.getSlot()) {
      case 0:
         var10002 = colors[0]++;
         break;
      case 1:
         colors[0] += 10;
         break;
      case 2:
         colors[0] += 50;
         break;
      case 3:
         var10002 = colors[1]++;
         break;
      case 4:
         colors[1] += 10;
         break;
      case 5:
         colors[1] += 50;
         break;
      case 6:
         var10002 = colors[2]++;
         break;
      case 7:
         colors[2] += 10;
         break;
      case 8:
         colors[2] += 50;
         break;
      case 9:
         var10002 = colors[0]--;
         break;
      case 10:
         colors[0] -= 10;
         break;
      case 11:
         colors[0] -= 50;
         break;
      case 12:
         var10002 = colors[1]--;
         break;
      case 13:
         colors[1] -= 10;
         break;
      case 14:
         colors[1] -= 50;
         break;
      case 15:
         var10002 = colors[2]--;
         break;
      case 16:
         colors[2] -= 10;
         break;
      case 17:
         colors[2] -= 50;
         break;
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 38:
      case 39:
      case 40:
      case 41:
      case 42:
      default:
         return;
      case 27:
         colors = new int[]{255, 255, 255};
         break;
      case 28:
         colors = new int[]{0, 0, 0};
         break;
      case 34:
         colors[0] += 5;
         colors[1] += 5;
         colors[2] += 5;
         break;
      case 35:
         colors[0] += 25;
         colors[1] += 25;
         colors[2] += 25;
         break;
      case 36:
         colors = new int[]{170, 170, 170};
         break;
      case 37:
         colors = new int[]{85, 85, 85};
         break;
      case 43:
         colors[0] -= 5;
         colors[1] -= 5;
         colors[2] -= 5;
         break;
      case 44:
         colors[0] -= 25;
         colors[1] -= 25;
         colors[2] -= 25;
         break;
      case 45:
         colors = new int[]{65, 105, 225};
         break;
      case 46:
         colors = new int[]{125, 249, 255};
         break;
      case 47:
         colors = new int[]{15, 255, 192};
         break;
      case 48:
         colors = new int[]{57, 255, 20};
         break;
      case 49:
         colors = new int[]{255, 255, 51};
         break;
      case 50:
         colors = new int[]{255, 179, 0};
         break;
      case 51:
         colors = new int[]{255, 95, 31};
         break;
      case 52:
         colors = new int[]{255, 105, 180};
         break;
      case 53:
         colors = new int[]{191, 0, 255};
      }

      Color newColor = this.toColor(colors);
      if (original.equals(newColor)) {
         this.getTargetPlayer().playSound(this.getTargetPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5F, 1.0F);
      } else {
         ItemUtils.setColor(this.colorableMeta, newColor);
         ItemUtils.setColor(this.cleanColorableMeta, newColor);
         this.updateInventory();
      }
   }

   private void updateInventory() {
      this.colorable.setItemMeta(this.colorableMeta);
      this.inventory.setItem(31, this.colorable);
      Color current = ItemUtils.getColor(this.colorableMeta);
      this.setupChannel(current, 0, 9, 255, 0, 0, "&c█ %amount%", 'R');
      this.setupChannel(current, 6, 15, 0, 0, 255, "&9█ %amount%", 'B');
      this.setupChannel(current, 3, 12, 0, 255, 0, "&a█ %amount%", 'G');
      this.setupAllChannels(current);
      this.setupPresetColors(current);
   }

   private void setupChannel(Color current, int incStart, int decStart, int r, int g, int b, String title, char channel) {
      int[] amounts = new int[]{1, 10, 50};

      int i;
      int amount;
      for(i = 0; i < amounts.length; ++i) {
         amount = amounts[i];
         this.inventory.setItem(incStart + i, this.createItem(amount, current, this.toColor(r, g, b), channel == 'R' ? amount : 0, channel == 'G' ? amount : 0, channel == 'B' ? amount : 0, title));
      }

      for(i = 0; i < amounts.length; ++i) {
         amount = -amounts[i];
         this.inventory.setItem(decStart + i, this.createItem(amount, current, this.toColor(r, g, b), channel == 'R' ? amount : 0, channel == 'G' ? amount : 0, channel == 'B' ? amount : 0, title));
      }

   }

   private void setupAllChannels(Color current) {
      this.inventory.setItem(34, this.createItem(5, current, this.toColor(255, 255, 255), 5, 5, 5, "&f█ %amount%"));
      this.inventory.setItem(35, this.createItem(25, current, this.toColor(255, 255, 255), 25, 25, 25, "&f█ %amount%"));
      this.inventory.setItem(43, this.createItem(-5, current, this.toColor(0, 0, 0), -5, -5, -5, "&f█ %amount%"));
      this.inventory.setItem(44, this.createItem(-25, current, this.toColor(0, 0, 0), -25, -25, -25, "&f█ %amount%"));
   }

   private void setupPresetColors(Color current) {
      this.inventory.setItem(27, this.createItem(current, this.toColor(255, 255, 255)));
      this.inventory.setItem(36, this.createItem(current, this.toColor(170, 170, 170)));
      this.inventory.setItem(37, this.createItem(current, this.toColor(85, 85, 85)));
      this.inventory.setItem(28, this.createItem(current, this.toColor(0, 0, 0)));
      this.inventory.setItem(45, this.createItem(current, this.toColor(65, 105, 225)));
      this.inventory.setItem(46, this.createItem(current, this.toColor(125, 249, 255)));
      this.inventory.setItem(47, this.createItem(current, this.toColor(15, 255, 192)));
      this.inventory.setItem(48, this.createItem(current, this.toColor(57, 255, 20)));
      this.inventory.setItem(49, this.createItem(current, this.toColor(255, 255, 51)));
      this.inventory.setItem(50, this.createItem(current, this.toColor(255, 179, 0)));
      this.inventory.setItem(51, this.createItem(current, this.toColor(255, 95, 31)));
      this.inventory.setItem(52, this.createItem(current, this.toColor(255, 105, 180)));
      this.inventory.setItem(53, this.createItem(current, this.toColor(191, 0, 255)));
   }

   private ItemStack createItem(Color currentColor, Color color) {
      ItemStack stack = this.colorable.clone();
      stack.setItemMeta(ItemUtils.setColor(this.cleanColorableMeta.clone(), color));
      stack.setAmount(1);
      int red = color.getRed();
      int green = color.getGreen();
      int blue = color.getBlue();
      String hex = String.format("%02X%02X%02X", red, green, blue);
      String currentHex = String.format("%02X%02X%02X", currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue());
      List<String> description = Arrays.asList("&x&" + String.join("&", currentHex.split("")) + "█ &f-> &x&" + String.join("&", hex.split("")) + "█", "", "", "&f -> (&c█ %red%&f, &a█ %green%&f, &9█ %blue%&f)", "&f -> HEX: #%hex%");
      return UtilsString.setDescription(stack, description, this.target, true, "%red%", String.valueOf(red), "%green%", String.valueOf(green), "%blue%", String.valueOf(blue), "%hex%", hex);
   }

   private ItemStack createItem(int amount, Color currentColor, Color baseColor, int redDiff, int greenDiff, int blueDiff, String title) {
      ItemStack stack = this.colorable.clone();
      int nextRed = this.limit(currentColor.getRed() + redDiff);
      int nextGreen = this.limit(currentColor.getGreen() + greenDiff);
      int nextBlue = this.limit(currentColor.getBlue() + blueDiff);
      ItemMeta meta = ItemUtils.setColor(this.cleanColorableMeta.clone(), baseColor);

      try {
         meta.setMaxStackSize(100);
      } catch (Throwable var16) {
      }

      stack.setItemMeta(meta);
      stack.setAmount(Math.abs(amount));
      String nextHex = String.format("%02X%02X%02X", nextRed, nextGreen, nextBlue);
      String currentHex = String.format("%02X%02X%02X", currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue());
      List<String> description = Arrays.asList("&x&" + String.join("&", currentHex.split("")) + "█ &f-> &x&" + String.join("&", nextHex.split("")) + "█", title, "", "&f(&c█ %red%&f, &a█ %green%&f, &9█ %blue%&f)", "&fHEX: #%hex%");
      return UtilsString.setDescription(stack, description, this.target, true, "%red%", String.valueOf(currentColor.getRed()), "%green%", String.valueOf(currentColor.getGreen()), "%blue%", String.valueOf(currentColor.getBlue()), "%hex%", currentHex, "%amount%", (amount > 0 ? "+" : "") + amount);
   }

   public void onDrag(InventoryDragEvent event) {
   }

   public void onOpen(InventoryOpenEvent event) {
      this.updateInventory();
   }

   @NotNull
   public Inventory getInventory() {
      return this.inventory;
   }

   public Player getTargetPlayer() {
      return this.target;
   }

   @NotNull
   public ItemEdit getPlugin() {
      return ItemEdit.get();
   }

   private Color toColor(int red, int green, int blue) {
      return Color.fromRGB(this.limit(red), this.limit(green), this.limit(blue));
   }

   private Color toColor(int[] colors) {
      return Color.fromRGB(this.limit(colors[0]), this.limit(colors[1]), this.limit(colors[2]));
   }

   private int[] fromColor(Color color) {
      return new int[]{color.getRed(), color.getGreen(), color.getBlue()};
   }

   private int limit(int color) {
      return Math.max(0, Math.min(255, color));
   }
}
