package emanondev.itemedit.compability;

import com.jojodmo.itembridge.ItemBridge;
import com.jojodmo.itembridge.ItemBridgeListener;
import emanondev.itemedit.ItemEdit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class ItemBridgeItemProvider implements ItemBridgeListener {
   public static void setup(Plugin plugin) {
      (new ItemBridge(plugin, new String[]{"serveritem", "si"})).registerListener(new ItemBridgeItemProvider());
   }

   public ItemStack fetchItemStack(@NotNull String item) {
      return ItemEdit.get().getServerStorage().getItem(item);
   }

   public String getItemName(@NotNull ItemStack stack) {
      return ItemEdit.get().getServerStorage().getId(stack);
   }
}
