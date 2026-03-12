package me.SuperRonanCraft.BetterRTP.references.invs.types;

import java.util.Iterator;
import java.util.List;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.invs.RTP_INV_SETTINGS;
import me.SuperRonanCraft.BetterRTP.references.invs.enums.RTPInventory;
import me.SuperRonanCraft.BetterRTP.references.invs.enums.RTP_INV_ITEMS;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RTPInvBlacklist extends RTPInventory {
   public void show(Player p) {
      int slots = (Bukkit.getWorlds().size() - Bukkit.getWorlds().size() % 9 + 1) * 9;
      if (slots < 54) {
         slots += 9;
      }

      Inventory inv = this.createInv(slots, "Settings: &lBlacklist");
      int _index = 0;

      for(Iterator var5 = Bukkit.getWorlds().iterator(); var5.hasNext(); ++_index) {
         World world = (World)var5.next();
         if (_index > 45) {
            break;
         }

         ItemStack _item = this.createItem(RTP_INV_ITEMS.NORMAL.item, RTP_INV_ITEMS.NORMAL.amt, world.getName(), (List)null);
         inv.setItem(_index, _item);
      }

      ItemStack _item = this.createItem(RTP_INV_ITEMS.BACK.item, RTP_INV_ITEMS.BACK.amt, RTP_INV_ITEMS.BACK.name, (List)null);
      inv.setItem(inv.getSize() - 9 + RTP_INV_ITEMS.BACK.slot, _item);
      p.openInventory(inv);
      this.cacheInv(p, inv, this.type);
   }

   public void clickEvent(InventoryClickEvent e) {
      int slot = e.getSlot();
      RTP_INV_ITEMS[] var3 = RTP_INV_ITEMS.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         RTP_INV_ITEMS type = var3[var5];
         if (type.slot != -1) {
            switch(type) {
            case BACK:
               if (slot == e.getInventory().getSize() - 9 + type.slot) {
                  BetterRTP.getInstance().getInvs().getInv(RTP_INV_SETTINGS.MAIN).show((Player)e.getWhoClicked());
               }
            }
         }
      }

   }
}
