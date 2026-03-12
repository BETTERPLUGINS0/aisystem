package ac.grim.grimac.utils.item;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.FoodProperties;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemBlocksAttacks;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemConsumable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEquippable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.utils.latency.CompensatedWorld;

public class ItemBehaviour {
   public static final ItemBehaviour INSTANCE = new ItemBehaviour();

   public boolean canUse(ItemStack item, CompensatedWorld world, GrimPlayer player, InteractionHand hand) {
      ItemConsumable consumable = (ItemConsumable)item.getComponentOr(ComponentTypes.CONSUMABLE, (Object)null);
      if (consumable != null) {
         return this.testConsumableComponent(item, world, player, hand, consumable);
      } else if (!player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_5)) {
         return false;
      } else {
         ItemBlocksAttacks blocksAttacks = (ItemBlocksAttacks)item.getComponentOr(ComponentTypes.BLOCKS_ATTACKS, (Object)null);
         ItemEquippable equippable = (ItemEquippable)item.getComponentOr(ComponentTypes.EQUIPPABLE, (Object)null);
         return (equippable == null || !equippable.isSwappable()) && blocksAttacks != null;
      }
   }

   protected boolean testConsumableComponent(ItemStack item, CompensatedWorld world, GrimPlayer player, InteractionHand hand, ItemConsumable consumable) {
      if (!this.testFoodComponent(item, world, player, hand)) {
         return false;
      } else {
         return consumable.getConsumeSeconds() * 20.0F > 0.0F;
      }
   }

   protected boolean testFoodComponent(ItemStack item, CompensatedWorld world, GrimPlayer player, InteractionHand hand) {
      FoodProperties foodProperties = (FoodProperties)item.getComponentOr(ComponentTypes.FOOD, (Object)null);
      return foodProperties != null ? foodProperties.isCanAlwaysEat() || player.food < 20 || player.gamemode == GameMode.CREATIVE : true;
   }
}
