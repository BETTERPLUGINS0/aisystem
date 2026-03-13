package tntrun.utils;

import io.github.thatsmusic99.headsplus.inventories.InventoryManager;
import io.github.thatsmusic99.headsplus.inventories.InventoryManager.InventoryType;
import java.util.HashMap;
import org.bukkit.entity.Player;

public class Heads {
   public static void openMenu(Player player) {
      InventoryManager im2 = InventoryManager.getManager(player);
      im2.open(InventoryType.HEADS_MENU, new HashMap());
   }
}
