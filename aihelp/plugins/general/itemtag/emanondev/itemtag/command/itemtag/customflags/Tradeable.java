package emanondev.itemtag.command.itemtag.customflags;

import emanondev.itemtag.ItemTag;
import emanondev.itemtag.command.itemtag.Flag;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;

public class Tradeable extends CustomFlag {
   private static final String TRADEABLE_KEY;

   public Tradeable(Flag cmd) {
      super("tradeable", TRADEABLE_KEY, cmd);
   }

   @EventHandler
   public void event(InventoryClickEvent event) {
      if (event.getClickedInventory() instanceof MerchantInventory) {
         if (event.getSlotType() == SlotType.RESULT) {
            MerchantInventory inv = (MerchantInventory)event.getClickedInventory();
            if (ItemTag.getTagItem(inv.getItem(0)).hasBooleanTag(TRADEABLE_KEY) || ItemTag.getTagItem(inv.getItem(1)).hasBooleanTag(TRADEABLE_KEY)) {
               event.setCancelled(true);
            }

         }
      }
   }

   public ItemStack getGuiItem() {
      return new ItemStack(Material.EMERALD);
   }

   static {
      TRADEABLE_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":tradeable";
   }
}
