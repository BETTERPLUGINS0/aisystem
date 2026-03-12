package me.SuperRonanCraft.BetterRTP.references.invs.types;

import java.util.List;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.invs.RTP_INV_SETTINGS;
import me.SuperRonanCraft.BetterRTP.references.invs.enums.RTPInventory;
import me.SuperRonanCraft.BetterRTP.references.invs.enums.RTP_INV_ITEMS;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RTPInvCoordinates extends RTPInventory {
   public void show(Player p) {
      if (BetterRTP.getInstance().getPInfo().getInvWorld().get(p) == null) {
         BetterRTP.getInstance().getPInfo().setNextInv(p, this.type);
         BetterRTP.getInstance().getInvs().getInv(RTP_INV_SETTINGS.WORLDS).show(p);
      } else {
         int slots = (RTP_COORDINATES_SETTINGS.values().length - RTP_COORDINATES_SETTINGS.values().length % 9 + 1) * 9;
         if (slots < 54) {
            slots += 9;
         }

         Inventory inv = this.createInv(slots, "Settings: &lCoordinates");
         int index = 0;
         RTP_COORDINATES_SETTINGS[] var5 = RTP_COORDINATES_SETTINGS.values();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            RTP_COORDINATES_SETTINGS set = var5[var7];
            ItemStack _item = this.createItem(RTP_INV_ITEMS.NORMAL.item, RTP_INV_ITEMS.NORMAL.amt, "&a&l" + set.getInfo()[1], (List)null);
            inv.setItem(index, _item);
            ++index;
         }

         ItemStack _item = this.createItem(RTP_INV_ITEMS.BACK.item, RTP_INV_ITEMS.BACK.amt, RTP_INV_ITEMS.BACK.name, (List)null);
         inv.setItem(inv.getSize() - 9 + RTP_INV_ITEMS.BACK.slot, _item);
         p.openInventory(inv);
         this.cacheInv(p, inv, this.type);
      }
   }

   public void clickEvent(InventoryClickEvent e) {
      int slot = e.getSlot();
      RTP_COORDINATES_SETTINGS[] var3 = RTP_COORDINATES_SETTINGS.values();
      int var4 = var3.length;

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         RTP_COORDINATES_SETTINGS var10000 = var3[var5];
      }

      RTP_INV_ITEMS[] var7 = RTP_INV_ITEMS.values();
      var4 = var7.length;

      for(var5 = 0; var5 < var4; ++var5) {
         RTP_INV_ITEMS type = var7[var5];
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
