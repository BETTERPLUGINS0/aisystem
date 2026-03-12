package me.SuperRonanCraft.BetterRTP.player;

import java.util.HashMap;
import me.SuperRonanCraft.BetterRTP.references.invs.RTP_INV_SETTINGS;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PlayerInfo {
   private final HashMap<Player, Inventory> invs = new HashMap();
   private final HashMap<Player, World> invWorld = new HashMap();
   private final HashMap<Player, RTP_INV_SETTINGS> invNextInv = new HashMap();
   private final HashMap<Player, Boolean> rtping = new HashMap();

   public void setInvWorld(Player p, World type) {
      this.invWorld.put(p, type);
   }

   public void setNextInv(Player p, RTP_INV_SETTINGS type) {
      this.invNextInv.put(p, type);
   }

   public Boolean playerExists(Player p) {
      return this.invs.containsKey(p);
   }

   private void unloadAll() {
      this.invs.clear();
      this.invWorld.clear();
      this.invNextInv.clear();
      this.rtping.clear();
   }

   private void unload(Player p) {
      this.clearInvs(p);
      this.rtping.remove(p);
   }

   public void clearInvs(Player p) {
      this.invs.remove(p);
      this.invWorld.remove(p);
      this.invNextInv.remove(p);
   }

   public HashMap<Player, World> getInvWorld() {
      return this.invWorld;
   }

   public HashMap<Player, RTP_INV_SETTINGS> getInvNextInv() {
      return this.invNextInv;
   }

   public HashMap<Player, Boolean> getRtping() {
      return this.rtping;
   }
}
