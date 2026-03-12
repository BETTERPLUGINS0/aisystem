package ac.grim.grimac.utils.item;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.utils.latency.CompensatedWorld;

public class TridentItem extends ItemBehaviour {
   public static final TridentItem INSTANCE = new TridentItem();

   public boolean canUse(ItemStack item, CompensatedWorld world, GrimPlayer player, InteractionHand hand) {
      if (this.nextDamageWillBreak(item)) {
         return false;
      } else {
         return item.getEnchantmentLevel(EnchantmentTypes.RIPTIDE) <= 0;
      }
   }

   private boolean nextDamageWillBreak(ItemStack item) {
      return item.isDamageableItem() && item.getDamageValue() >= item.getMaxDamage() - 1;
   }
}
