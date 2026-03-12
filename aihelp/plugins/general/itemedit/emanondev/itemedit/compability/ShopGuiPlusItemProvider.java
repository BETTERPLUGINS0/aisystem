package emanondev.itemedit.compability;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.utility.ItemUtils;
import java.util.Iterator;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.provider.item.ItemProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class ShopGuiPlusItemProvider extends ItemProvider {
   public ShopGuiPlusItemProvider() {
      super("ServerItem");
   }

   public boolean isValidItem(ItemStack item) {
      return this.getCustomId(item) != null;
   }

   public ItemStack loadItem(ConfigurationSection section) {
      String id = section.getString("serveritem");
      if (id == null) {
         Iterator var3 = section.getKeys(false).iterator();

         while(var3.hasNext()) {
            String key = (String)var3.next();
            if (key.equalsIgnoreCase("serveritem")) {
               id = section.getString(key);
               break;
            }
         }

         if (id == null) {
            return null;
         }
      }

      try {
         ItemStack result = ItemEdit.get().getServerStorage().getItem(id);
         if (result != null) {
            result.setAmount(section.getInt("quantity", 1));
            return result;
         } else {
            ItemEdit.get().log("Invalid ServerItem id on ShopGuiPlus config for &e" + id + " &fon path &e" + section.getCurrentPath() + ".serveritem");
            return null;
         }
      } catch (Exception var5) {
         ItemEdit.get().log("Invalid ServerItem id on ShopGuiPlus config for &e" + id + " &fon path &e" + section.getCurrentPath() + ".serveritem");
         return null;
      }
   }

   public boolean compare(ItemStack item1, ItemStack item2) {
      String id1 = this.getCustomId(item1);
      return id1 == null ? false : id1.equals(this.getCustomId(item2));
   }

   private String getCustomId(ItemStack item) {
      return ItemUtils.isAirOrNull(item) ? null : ItemEdit.get().getServerStorage().getId(item);
   }

   public void register() {
      ShopGuiPlusApi.registerItemProvider(this);
   }
}
