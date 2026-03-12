package me.SuperRonanCraft.BetterRTP.references.invs.types;

import java.util.List;
import me.SuperRonanCraft.BetterRTP.references.invs.RTP_INV_SETTINGS;
import me.SuperRonanCraft.BetterRTP.references.invs.enums.RTPInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RTPInvMain extends RTPInventory {
   public void show(Player p) {
      Inventory inv = this.createInv(9, "&lSettings");
      int _index = 0;
      RTP_INV_SETTINGS[] var4 = RTP_INV_SETTINGS.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         RTP_INV_SETTINGS type = var4[var6];
         if (type.getShowMain()) {
            String _name = type.name();
            _name = _name.substring(0, 1).toUpperCase() + _name.substring(1).toLowerCase();
            ItemStack _item = this.createItem("paper", 1, _name, (List)null);
            inv.setItem(_index, _item);
            ++_index;
         }
      }

      p.openInventory(inv);
      this.cacheInv(p, inv, this.type);
   }

   public void clickEvent(InventoryClickEvent event) {
      Player p = (Player)event.getWhoClicked();
      int slot = event.getSlot();
      int _index = 0;
      RTP_INV_SETTINGS[] var5 = RTP_INV_SETTINGS.values();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         RTP_INV_SETTINGS type = var5[var7];
         if (type.getShowMain()) {
            if (_index == slot) {
               type.getInv().show(p);
               return;
            }

            ++_index;
         }
      }

   }
}
