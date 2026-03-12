package me.SuperRonanCraft.BetterRTP.references.invs;

import java.util.HashMap;
import java.util.Iterator;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.invs.enums.RTPInventory_Defaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RTPInventories {
   private final HashMap<RTP_INV_SETTINGS, RTPInventory_Defaults> invs = new HashMap();

   public void load() {
      this.invs.clear();
      RTP_INV_SETTINGS[] var1 = RTP_INV_SETTINGS.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         RTP_INV_SETTINGS type = var1[var3];
         type.load(type);
         this.invs.put(type, type.getInv());
      }

   }

   public void closeAll() {
      BetterRTP main = BetterRTP.getInstance();
      Iterator var2 = Bukkit.getOnlinePlayers().iterator();

      while(var2.hasNext()) {
         Player p = (Player)var2.next();
         if (main.getPInfo().playerExists(p)) {
            main.getPInfo().clearInvs(p);
            p.closeInventory();
         }
      }

   }

   public RTPInventory_Defaults getInv(RTP_INV_SETTINGS type) {
      return (RTPInventory_Defaults)this.invs.get(type);
   }
}
