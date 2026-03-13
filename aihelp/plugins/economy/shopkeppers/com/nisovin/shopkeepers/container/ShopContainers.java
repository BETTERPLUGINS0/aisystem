package com.nisovin.shopkeepers.container;

import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;

public class ShopContainers {
   private ShopContainers() {
   }

   public static boolean isSupportedContainer(Material material) {
      return ItemUtils.isChest(material) || material == Material.BARREL || ItemUtils.isShulkerBox(material);
   }

   public static Inventory getInventory(Block containerBlock) {
      Validate.notNull(containerBlock, (String)"containerBlock is null");
      Validate.isTrue(isSupportedContainer(containerBlock.getType()), () -> {
         return "containerBlock is of unsupported type: " + String.valueOf(containerBlock.getType());
      });
      BlockState state = containerBlock.getState();

      assert state instanceof Container;

      Container container = (Container)state;
      return container.getInventory();
   }
}
