package me.SuperRonanCraft.BetterRTP.player.rtp;

import java.util.HashMap;
import java.util.List;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.customEvents.RTP_SettingUpEvent;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import me.SuperRonanCraft.BetterRTP.references.helpers.HelperRTP;
import me.SuperRonanCraft.BetterRTP.references.helpers.HelperRTP_Check;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.PermissionGroup;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.RTPWorld;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WORLD_TYPE;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldDefault;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RTP {
   final RTPTeleport teleport = new RTPTeleport();
   public final HashMap<String, String> overriden = new HashMap();
   List<String> disabledWorlds;
   List<String> blockList;
   int maxAttempts;
   int delayTime;
   boolean cancelOnMove;
   boolean cancelOnDamage;
   public final HashMap<String, WORLD_TYPE> world_type = new HashMap();
   private final WorldDefault RTPdefaultWorld = new WorldDefault();
   private final HashMap<String, RTPWorld> RTPcustomWorld = new HashMap();
   private final HashMap<String, RTPWorld> RTPworldLocations = new HashMap();
   private final HashMap<String, PermissionGroup> permissionGroups = new HashMap();

   public void load() {
      FileOther.FILETYPE config = FileOther.FILETYPE.CONFIG;
      this.disabledWorlds = config.getStringList("DisabledWorlds");
      this.maxAttempts = config.getInt("Settings.MaxAttempts");
      this.delayTime = config.getInt("Settings.Delay.Time");
      this.cancelOnMove = config.getBoolean("Settings.Delay.CancelOnMove");
      this.cancelOnDamage = config.getBoolean("Settings.Delay.CancelOnDamage");
      this.blockList = config.getStringList("BlacklistedBlocks");
      RTPLoader.loadOverrides(this.overriden);
      RTPLoader.loadWorldTypes(this.world_type);
      this.loadWorlds();
      this.loadLocations();
      this.loadPermissionGroups();
      this.teleport.load();
   }

   public void loadWorlds() {
      RTPLoader.loadWorlds(this.RTPdefaultWorld, this.RTPcustomWorld);
   }

   public void loadLocations() {
      RTPLoader.loadLocations(this.RTPworldLocations);
   }

   public void loadPermissionGroups() {
      RTPLoader.loadPermissionGroups(this.permissionGroups);
   }

   public void start(RTPSetupInformation setup_info) {
      this.start(HelperRTP.getPlayerWorld(setup_info));
   }

   public void start(WorldPlayer pWorld) {
      RTP_SettingUpEvent setup = new RTP_SettingUpEvent(pWorld.getPlayer());
      Bukkit.getPluginManager().callEvent(setup);
      if (!setup.isCancelled()) {
         this.rtp(pWorld.getSendi(), pWorld, pWorld.getRtp_type());
      }
   }

   private void rtp(CommandSender sendi, WorldPlayer pWorld, RTP_TYPE type) {
      Player p = pWorld.getPlayer();
      this.getPl().getPInfo().getRtping().put(p, true);
      RTPPlayer rtpPlayer = new RTPPlayer(p, this, pWorld, type);
      if (pWorld.getPlayerInfo().applyDelay && HelperRTP_Check.applyDelay(pWorld.getPlayer())) {
         new RTPDelay(sendi, rtpPlayer, this.delayTime, this.cancelOnMove, this.cancelOnDamage);
      } else if (!this.teleport.beforeTeleportInstant(sendi, p)) {
         rtpPlayer.randomlyTeleport(sendi);
      }

   }

   private BetterRTP getPl() {
      return BetterRTP.getInstance();
   }

   public RTPTeleport getTeleport() {
      return this.teleport;
   }

   public List<String> getDisabledWorlds() {
      return this.disabledWorlds;
   }

   public List<String> getBlockList() {
      return this.blockList;
   }

   public WorldDefault getRTPdefaultWorld() {
      return this.RTPdefaultWorld;
   }

   public HashMap<String, RTPWorld> getRTPcustomWorld() {
      return this.RTPcustomWorld;
   }

   public HashMap<String, RTPWorld> getRTPworldLocations() {
      return this.RTPworldLocations;
   }

   public HashMap<String, PermissionGroup> getPermissionGroups() {
      return this.permissionGroups;
   }
}
