package ac.grim.grimac.utils.item;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.utils.latency.CompensatedWorld;

public class AlwaysUseItem extends ItemBehaviour {
   public static final AlwaysUseItem INSTANCE = new AlwaysUseItem();

   public boolean canUse(ItemStack item, CompensatedWorld world, GrimPlayer player, InteractionHand hand) {
      return true;
   }
}
