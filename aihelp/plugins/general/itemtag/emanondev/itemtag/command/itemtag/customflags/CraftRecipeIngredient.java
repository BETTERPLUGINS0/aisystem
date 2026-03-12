package emanondev.itemtag.command.itemtag.customflags;

import emanondev.itemedit.utility.InventoryUtils;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.command.itemtag.Flag;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CraftRecipeIngredient extends CustomFlag {
   private static final String CRAFTING_INGREDIENT_KEY;

   public CraftRecipeIngredient(Flag cmd) {
      super("recipeingredient", CRAFTING_INGREDIENT_KEY, cmd);
   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void event(CraftItemEvent event) {
      ItemStack[] var2 = event.getInventory().getMatrix();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemStack item = var2[var4];
         if (ItemTag.getTagItem(item).hasBooleanTag(CRAFTING_INGREDIENT_KEY)) {
            event.setCancelled(true);
            return;
         }
      }

   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void event(InventoryClickEvent event) {
      Inventory inv = InventoryUtils.getTopInventory(event);
      if (inv != null && inv.getType().name().equals("CARTOGRAPHY")) {
         if (event.getSlotType() == SlotType.RESULT) {
            if (ItemTag.getTagItem(inv.getItem(0)).hasBooleanTag(CRAFTING_INGREDIENT_KEY)) {
               event.setCancelled(true);
            } else {
               if (ItemTag.getTagItem(inv.getItem(1)).hasBooleanTag(CRAFTING_INGREDIENT_KEY)) {
                  event.setCancelled(true);
               }

            }
         }
      }
   }

   public ItemStack getGuiItem() {
      return new ItemStack(Material.CRAFTING_TABLE);
   }

   static {
      CRAFTING_INGREDIENT_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":craft_ingredient";
   }
}
