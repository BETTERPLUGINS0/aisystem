package ac.grim.grimac.utils.inventory.slot;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.utils.inventory.InventoryStorage;

public class ResultSlot extends Slot {
   public ResultSlot(InventoryStorage container, int slot) {
      super(container, slot);
   }

   public boolean mayPlace(ItemStack itemStack) {
      return false;
   }

   public void onTake(GrimPlayer player, ItemStack itemStack) {
   }
}
