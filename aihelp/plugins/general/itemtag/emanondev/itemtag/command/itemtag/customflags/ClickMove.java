package emanondev.itemtag.command.itemtag.customflags;

import emanondev.itemtag.ItemTag;
import emanondev.itemtag.command.itemtag.Flag;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ClickMove extends CustomFlag {
   private static final String CLICKMOVE_KEY;

   public ClickMove(Flag cmd) {
      super("clickmove", CLICKMOVE_KEY, cmd);
   }

   @EventHandler
   public void event(InventoryClickEvent event) {
      if (!event.getWhoClicked().hasPermission("itemtag.itemtag.flag.bypass_clickmove")) {
         if (ItemTag.getTagItem(event.getCurrentItem()).hasBooleanTag(CLICKMOVE_KEY)) {
            event.setCancelled(true);
         } else if (event.getClick() == ClickType.NUMBER_KEY && ItemTag.getTagItem(event.getWhoClicked().getInventory().getItem(event.getHotbarButton())).hasBooleanTag(CLICKMOVE_KEY)) {
            event.setCancelled(true);
         } else {
            if (event.getClick().name().equals("SWAP_OFFHAND") && ItemTag.getTagItem(event.getWhoClicked().getInventory().getItem(EquipmentSlot.OFF_HAND)).hasBooleanTag(CLICKMOVE_KEY)) {
               event.setCancelled(true);
            }

         }
      }
   }

   public ItemStack getGuiItem() {
      return new ItemStack(Material.BEDROCK);
   }

   static {
      CLICKMOVE_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":clickmove";
   }
}
