package me.SuperRonanCraft.BetterRTP.references.player.playerdata;

import java.util.HashMap;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.CooldownData;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PlayerData {
   public boolean loading;
   public final Player player;
   final PlayerData_Menus menu = new PlayerData_Menus();
   final HashMap<World, CooldownData> cooldowns = new HashMap();
   boolean rtping;
   int rtpCount;
   long globalCooldown;
   long invincibleEndTime;

   PlayerData(Player player) {
      this.player = player;
   }

   public void load(boolean joined) {
   }

   public PlayerData_Menus getMenu() {
      return this.menu;
   }

   public HashMap<World, CooldownData> getCooldowns() {
      return this.cooldowns;
   }

   public boolean isRtping() {
      return this.rtping;
   }

   public void setRtping(boolean rtping) {
      this.rtping = rtping;
   }

   public int getRtpCount() {
      return this.rtpCount;
   }

   public void setRtpCount(int rtpCount) {
      this.rtpCount = rtpCount;
   }

   public long getGlobalCooldown() {
      return this.globalCooldown;
   }

   public void setGlobalCooldown(long globalCooldown) {
      this.globalCooldown = globalCooldown;
   }

   public long getInvincibleEndTime() {
      return this.invincibleEndTime;
   }

   public void setInvincibleEndTime(long invincibleEndTime) {
      this.invincibleEndTime = invincibleEndTime;
   }
}
