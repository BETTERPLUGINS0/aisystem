package me.SuperRonanCraft.BetterRTP.references.player.playerdata;

import me.SuperRonanCraft.BetterRTP.references.invs.RTP_INV_SETTINGS;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;

public class PlayerData_Menus {
   private Inventory inv;
   RTP_INV_SETTINGS invType;
   World invWorld;
   RTP_INV_SETTINGS invNextInv;

   public Inventory getInv() {
      return this.inv;
   }

   public void setInv(Inventory inv) {
      this.inv = inv;
   }

   public RTP_INV_SETTINGS getInvType() {
      return this.invType;
   }

   public void setInvType(RTP_INV_SETTINGS invType) {
      this.invType = invType;
   }

   public World getInvWorld() {
      return this.invWorld;
   }

   public void setInvWorld(World invWorld) {
      this.invWorld = invWorld;
   }

   public RTP_INV_SETTINGS getInvNextInv() {
      return this.invNextInv;
   }

   public void setInvNextInv(RTP_INV_SETTINGS invNextInv) {
      this.invNextInv = invNextInv;
   }
}
