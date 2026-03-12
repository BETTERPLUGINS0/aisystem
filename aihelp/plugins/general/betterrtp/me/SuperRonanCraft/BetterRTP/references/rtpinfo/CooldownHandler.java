package me.SuperRonanCraft.BetterRTP.references.rtpinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.database.DatabaseCooldowns;
import me.SuperRonanCraft.BetterRTP.references.database.DatabaseHandler;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import me.SuperRonanCraft.BetterRTP.references.player.HelperPlayer;
import me.SuperRonanCraft.BetterRTP.references.player.playerdata.PlayerData;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldPlayer;
import me.SuperRonanCraft.BetterRTP.versions.AsyncHandler;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CooldownHandler {
   boolean enabled;
   boolean loaded;
   boolean cooldownByWorld;
   private int defaultCooldownTime;
   private int lockedAfter;
   private final List<Player> downloading = new ArrayList();

   public void load() {
      FileOther.FILETYPE config = FileOther.FILETYPE.CONFIG;
      this.enabled = config.getBoolean("Settings.Cooldown.Enabled");
      this.downloading.clear();
      this.loaded = false;
      if (this.enabled) {
         this.defaultCooldownTime = config.getInt("Settings.Cooldown.Time");
         BetterRTP.debug("Cooldown = " + this.defaultCooldownTime);
         this.lockedAfter = config.getInt("Settings.Cooldown.LockAfter");
         this.cooldownByWorld = config.getBoolean("Settings.Cooldown.PerWorld");
      }

      this.queueDownload();
   }

   private void queueDownload() {
      AsyncHandler.asyncLater(() -> {
         if (this.cooldownByWorld && !DatabaseHandler.getCooldowns().isLoaded()) {
            this.queueDownload();
         } else if (!DatabaseHandler.getPlayers().isLoaded()) {
            this.queueDownload();
         } else {
            Iterator var1 = Bukkit.getOnlinePlayers().iterator();

            while(var1.hasNext()) {
               Player p = (Player)var1.next();
               this.loadPlayer(p);
            }

            this.loaded = true;
         }
      }, 10L);
   }

   public void add(Player player, World world) {
      if (this.enabled) {
         PlayerData playerData = this.getData(player);
         if (this.cooldownByWorld) {
            HashMap<World, CooldownData> cooldowns = playerData.getCooldowns();
            CooldownData data = (CooldownData)cooldowns.getOrDefault(world, new CooldownData(player.getUniqueId(), 0L));
            playerData.setRtpCount(playerData.getRtpCount() + 1);
            data.setTime(System.currentTimeMillis());
            playerData.setGlobalCooldown(data.getTime());
            cooldowns.put(world, data);
            this.savePlayer(player, world, data, false);
         } else {
            this.add(player);
         }

      }
   }

   private void add(Player player) {
      if (this.enabled) {
         PlayerData playerData = this.getData(player);
         playerData.setRtpCount(playerData.getRtpCount() + 1);
         playerData.setGlobalCooldown(System.currentTimeMillis());
         this.savePlayer(player, (World)null, (CooldownData)null, false);
      }
   }

   @Nullable
   public CooldownData get(Player p, World world) {
      PlayerData data = this.getData(p);
      if (this.cooldownByWorld) {
         HashMap<World, CooldownData> cooldownData = this.getData(p).getCooldowns();
         if (data != null) {
            return (CooldownData)cooldownData.getOrDefault(world, (Object)null);
         }
      } else if (data.getGlobalCooldown() > 0L) {
         return new CooldownData(p.getUniqueId(), data.getGlobalCooldown());
      }

      return null;
   }

   public long timeLeft(CommandSender sendi, CooldownData data, WorldPlayer pWorld) {
      long cooldown = data.getTime();
      long timeLeft = cooldown / 1000L + pWorld.getCooldown() - System.currentTimeMillis() / 1000L;
      return timeLeft * 1000L;
   }

   public boolean locked(Player player) {
      return this.lockedAfter > 0 && this.getData(player).getRtpCount() >= this.lockedAfter;
   }

   public void removeCooldown(Player player, World world) {
      if (this.enabled) {
         PlayerData playerData = this.getData(player);
         CooldownData cooldownData = (CooldownData)playerData.getCooldowns().getOrDefault(world, (Object)null);
         if (cooldownData != null) {
            if (this.lockedAfter > 0) {
               if (playerData.getRtpCount() <= 0) {
                  this.savePlayer(player, world, cooldownData, true);
                  this.getData(player).getCooldowns().put(world, (Object)null);
               } else {
                  this.savePlayer(player, world, cooldownData, false);
               }
            } else {
               this.getData(player).getCooldowns().remove(world);
               this.savePlayer(player, world, cooldownData, true);
            }
         } else if (!this.cooldownByWorld) {
            this.getData(player).setGlobalCooldown(0L);
            this.savePlayer(player, (World)null, (CooldownData)null, true);
         }

      }
   }

   private void savePlayer(Player player, @Nullable World world, @Nullable CooldownData data, boolean remove) {
      AsyncHandler.async(() -> {
         if (world != null && data != null && this.getDatabaseWorlds() != null) {
            if (!remove) {
               this.getDatabaseWorlds().setCooldown(world, data);
            } else {
               this.getDatabaseWorlds().removePlayer(data.getUuid(), world);
            }
         }

         DatabaseHandler.getPlayers().setData(this.getData(player));
      });
   }

   public void loadPlayer(Player player) {
      if (this.isEnabled()) {
         this.downloading.add(player);
         PlayerData playerData = this.getData(player);
         if (this.getDatabaseWorlds() != null) {
            Iterator var3 = Bukkit.getWorlds().iterator();

            while(var3.hasNext()) {
               World world = (World)var3.next();
               CooldownData cooldown = this.getDatabaseWorlds().getCooldown(player.getUniqueId(), world);
               if (cooldown != null) {
                  playerData.getCooldowns().put(world, cooldown);
               }
            }
         }

         DatabaseHandler.getPlayers().setupData(playerData);
         this.downloading.remove(player);
      }
   }

   public boolean loadedPlayer(Player player) {
      return !this.downloading.contains(player);
   }

   @Nullable
   private DatabaseCooldowns getDatabaseWorlds() {
      return this.cooldownByWorld ? DatabaseHandler.getCooldowns() : null;
   }

   private PlayerData getData(Player p) {
      return HelperPlayer.getData(p);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public boolean isLoaded() {
      return this.loaded;
   }

   public boolean isCooldownByWorld() {
      return this.cooldownByWorld;
   }

   public int getDefaultCooldownTime() {
      return this.defaultCooldownTime;
   }
}
