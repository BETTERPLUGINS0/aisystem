package ac.grim.grimac.utils.inventory.slot;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.utils.inventory.EquipmentType;
import ac.grim.grimac.utils.inventory.InventoryStorage;

public class EquipmentSlot extends Slot {
   private final EquipmentType type;

   public EquipmentSlot(EquipmentType type, InventoryStorage menu, int slot) {
      super(menu, slot);
      this.type = type;
   }

   public int getMaxStackSize() {
      return 1;
   }

   public boolean mayPlace(ItemStack itemStack) {
      return this.type == EquipmentType.getEquipmentSlotForItem(itemStack);
   }

   public boolean mayPickup(GrimPlayer player) {
      ItemStack itemstack = this.getItem();
      return (itemstack.isEmpty() || player.gamemode == GameMode.CREATIVE || itemstack.getEnchantmentLevel(EnchantmentTypes.BINDING_CURSE) == 0) && super.mayPickup(player);
   }
}
