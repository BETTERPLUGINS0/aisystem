package emanondev.itemtag.command.itemtag.customflags;

import emanondev.itemedit.utility.InventoryUtils;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.command.itemtag.Flag;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Grindable extends CustomFlag {
   private static final String GRINDABLE_KEY;

   public Grindable(Flag cmd) {
      super("grindable", GRINDABLE_KEY, cmd);
   }

   @EventHandler
   public void event(InventoryClickEvent event) {
      Inventory inv = InventoryUtils.getTopInventory(event);
      if (inv instanceof GrindstoneInventory) {
         boolean found = ItemTag.getTagItem(inv.getItem(0)).hasBooleanTag(GRINDABLE_KEY) || ItemTag.getTagItem(inv.getItem(1)).hasBooleanTag(GRINDABLE_KEY);
         if (found) {
            if (event.getSlotType() == SlotType.RESULT) {
               event.setCancelled(true);
            }

         }
      }
   }

   public ItemStack getGuiItem() {
      return new ItemStack(Material.GRINDSTONE);
   }

   static {
      GRINDABLE_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":grindable";
   }
}
