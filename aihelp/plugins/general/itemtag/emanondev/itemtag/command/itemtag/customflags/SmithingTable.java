package emanondev.itemtag.command.itemtag.customflags;

import emanondev.itemtag.ItemTag;
import emanondev.itemtag.command.itemtag.Flag;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.ItemStack;

public class SmithingTable extends CustomFlag {
   private static final String SMITHING_TABLE;

   public SmithingTable(Flag cmd) {
      super("smithing_table", SMITHING_TABLE, cmd);
   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void event(SmithItemEvent event) {
      ItemStack[] var2 = event.getInventory().getContents();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemStack item = var2[var4];
         if (ItemTag.getTagItem(item).hasBooleanTag(SMITHING_TABLE)) {
            event.setCancelled(true);
            return;
         }
      }

   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void event(PrepareSmithingEvent event) {
      ItemStack[] var2 = event.getInventory().getContents();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemStack item = var2[var4];
         if (ItemTag.getTagItem(item).hasBooleanTag(SMITHING_TABLE)) {
            event.setResult((ItemStack)null);
            return;
         }
      }

   }

   public ItemStack getGuiItem() {
      return new ItemStack(Material.SMITHING_TABLE);
   }

   static {
      SMITHING_TABLE = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":smithing_table";
   }
}
