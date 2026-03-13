package tntrun.arena.handlers;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.gmail.nossr50.api.PartyAPI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.arena.structure.StructureManager;
import tntrun.events.PlayerJoinArenaEvent;
import tntrun.events.PlayerLeaveArenaEvent;
import tntrun.events.PlayerSpectateArenaEvent;
import tntrun.events.RewardWinnerEvent;
import tntrun.messages.Messages;
import tntrun.utils.Bars;
import tntrun.utils.FormattingCodesParser;
import tntrun.utils.TitleMsg;
import tntrun.utils.Utils;

public class PlayerHandler {
   private TNTRun plugin;
   private Arena arena;
   private Map<String, Integer> doublejumps = new HashMap();
   private List<String> pparty = new ArrayList();
   private HashSet<String> votes = new HashSet();
   private Map<String, Location> spawnmap = new HashMap();
   private String linkedKitName;
   private List<String> rewardedPlayers = new ArrayList();

   public PlayerHandler(TNTRun plugin, Arena arena) {
      this.plugin = plugin;
      this.arena = arena;
   }

   public boolean checkJoin(Player player) {
      return this.checkJoin(player, false);
   }

   public boolean checkJoin(Player player, boolean silent) {
      if (!this.preJoinChecks(player, silent)) {
         return false;
      } else if (this.arena.getStatusManager().isArenaRunning()) {
         if (!silent) {
            Messages.sendMessage(player, this.arena.getStatusManager().getFormattedMessage(Messages.arenarunning));
         }

         return false;
      } else if (!player.hasPermission("tntrun.join")) {
         if (!silent) {
            Messages.sendMessage(player, Messages.nopermission);
         }

         return false;
      } else if (this.arena.getPlayersManager().getPlayersCount() == this.arena.getStructureManager().getMaxPlayers()) {
         if (!silent) {
            Messages.sendMessage(player, Messages.limitreached);
         }

         return false;
      } else {
         return this.processFee(player, silent);
      }
   }

   public boolean processFee(Player player, boolean silent) {
      if (this.arena.getStructureManager().hasFee()) {
         double fee = this.arena.getStructureManager().getFee();
         if (!this.arena.getArenaEconomy().hasFunds(player, fee, silent)) {
            if (!silent) {
               Messages.sendMessage(player, Messages.arenanofee.replace("{FEE}", this.arena.getStructureManager().getArenaCost()));
            }

            return false;
         }

         if (!silent) {
            Messages.sendMessage(player, Messages.arenafee.replace("{FEE}", this.arena.getStructureManager().getArenaCost()));
         }
      }

      return true;
   }

   public boolean preJoinChecks(Player player, boolean silent) {
      if (!this.arena.getStatusManager().isArenaEnabled()) {
         if (!silent) {
            Messages.sendMessage(player, this.arena.getStatusManager().getFormattedMessage(Messages.arenadisabled));
         }

         return false;
      } else if (this.arena.getStructureManager().getWorld() == null) {
         if (!silent) {
            Messages.sendMessage(player, Messages.arenawolrdna);
         }

         return false;
      } else if (this.arena.getStatusManager().isArenaRegenerating()) {
         if (!silent) {
            Messages.sendMessage(player, this.arena.getStatusManager().getFormattedMessage(Messages.arenaregenerating));
         }

         return false;
      } else if (player.isInsideVehicle()) {
         if (!silent) {
            Messages.sendMessage(player, Messages.arenavehicle);
         }

         return false;
      } else if (this.plugin.amanager.getPlayerArena(player.getName()) != null) {
         if (!silent) {
            Messages.sendMessage(player, Messages.arenajoined);
         }

         return false;
      } else {
         return true;
      }
   }

   public boolean canSpectate(Player player) {
      if (!player.hasPermission("tntrun.spectate")) {
         Messages.sendMessage(player, Messages.nopermission);
         return false;
      } else if (this.arena.getStructureManager().getSpectatorSpawnVector() == null) {
         Messages.sendMessage(player, Messages.arenanospectatorspawn.replace("{ARENA}", this.arena.getArenaName()));
         return false;
      } else {
         return this.preJoinChecks(player, false);
      }
   }

   private Location getSpawnPoint(String playerName) {
      Location loc = null;
      if (this.spawnmap.containsKey(playerName) && this.arena.getStructureManager().getFreeSpawnList().contains(((Location)this.spawnmap.get(playerName)).toVector())) {
         loc = (Location)this.spawnmap.get(playerName);
         this.arena.getStructureManager().getFreeSpawnList().remove(loc.toVector());
      } else {
         loc = this.arena.getStructureManager().getSpawnPoint();
         if (this.arena.getStructureManager().hasAdditionalSpawnPoints()) {
            this.spawnmap.put(playerName, loc);
         }
      }

      return loc != null ? loc : this.arena.getStructureManager().getPrimarySpawnPoint();
   }

   public void spawnPlayer(final Player player, String msgtoarenaplayers) {
      this.plugin.getPData().storePlayerLocation(player);
      if (this.plugin.getConfig().getBoolean("UseRankInChat.enabled")) {
         Utils.cachePlayerGroupData(player);
      }

      player.teleport(this.getSpawnPoint(player.getName()));
      Iterator var4 = Bukkit.getOnlinePlayers().iterator();

      while(var4.hasNext()) {
         Player aplayer = (Player)var4.next();
         aplayer.showPlayer(this.plugin, player);
      }

      this.arena.getPlayersManager().add(player);
      if (Utils.debug()) {
         Logger var10000 = this.plugin.getLogger();
         String var10001 = player.getName();
         var10000.info("Player " + var10001 + " joined arena " + this.arena.getArenaName());
         this.plugin.getLogger().info("Players in arena: " + this.arena.getPlayersManager().getPlayersCount());
      }

      this.storePlayerData(player);
      if (this.plugin.isMCMMO() && !this.arena.getStructureManager().getDamageEnabled().equals(StructureManager.DamageEnabled.NO)) {
         this.allowFriendlyFire(player);
      }

      if (!this.arena.getStatusManager().isArenaStarting()) {
         this.arena.getGameHandler().count = this.arena.getStructureManager().getCountdown();
      }

      int playerCount = this.arena.getPlayersManager().getPlayersCount();
      if (playerCount == 1) {
         this.sendInvitationMessage(player);
      }

      Iterator var5 = this.arena.getPlayersManager().getPlayers().iterator();

      while(true) {
         Player oplayer;
         do {
            if (!var5.hasNext()) {
               (new BukkitRunnable() {
                  public void run() {
                     if (PlayerHandler.this.plugin.getConfig().getBoolean("items.leave.use")) {
                        PlayerHandler.this.addLeaveItem(player);
                     }

                     if (PlayerHandler.this.plugin.getConfig().getBoolean("items.vote.use")) {
                        PlayerHandler.this.addVote(player);
                     }

                     if (PlayerHandler.this.plugin.getConfig().getBoolean("items.shop.use")) {
                        PlayerHandler.this.addShop(player);
                     }

                     if (PlayerHandler.this.plugin.getConfig().getBoolean("items.info.use")) {
                        PlayerHandler.this.addInfo(player);
                     }

                     if (PlayerHandler.this.plugin.getConfig().getBoolean("items.stats.use")) {
                        PlayerHandler.this.addStats(player);
                     }

                     if (PlayerHandler.this.plugin.isHeadsPlus() && PlayerHandler.this.plugin.getConfig().getBoolean("items.heads.use")) {
                        PlayerHandler.this.addHeads(player);
                     }

                  }
               }).runTaskLater(this.plugin, 5L);
               this.cacheDoubleJumps(player);
               if (this.plugin.getConfig().getBoolean("special.UseBossBar")) {
                  Bars.addPlayerToBar(player, this.arena.getArenaName());
               } else {
                  String message = Messages.playerscountinarena;
                  message = message.replace("{COUNT}", String.valueOf(this.arena.getPlayersManager().getPlayersCount()));
                  Messages.sendMessage(player, message);
               }

               this.plugin.getSignEditor().modifySigns(this.arena.getArenaName());
               this.arena.getScoreboardHandler().createWaitingScoreBoard();
               if (!this.arena.getStatusManager().isArenaStarting()) {
                  double progress = (double)this.arena.getPlayersManager().getPlayersCount() / (double)this.arena.getStructureManager().getMinPlayers();
                  Bars.setBar(this.arena, Bars.waiting, this.arena.getPlayersManager().getPlayersCount(), 0, progress, this.plugin);
                  Iterator var7 = this.arena.getPlayersManager().getPlayers().iterator();

                  while(var7.hasNext()) {
                     Player oplayer = (Player)var7.next();
                     this.plugin.getSound().NOTE_PLING(oplayer, 5.0F, 999.0F);
                  }
               }

               this.plugin.getServer().getPluginManager().callEvent(new PlayerJoinArenaEvent(player, this.arena));
               if (!this.arena.getStatusManager().isArenaStarting() && this.arena.getPlayersManager().getPlayersCount() == this.arena.getStructureManager().getMinPlayers()) {
                  this.arena.getGameHandler().runArenaCountdown();
               }

               if (this.plugin.isBungeecord()) {
                  return;
               }

               if (this.plugin.isParties()) {
                  this.joinPartyMembers(player);
               } else if (this.plugin.isAdpParties()) {
                  this.joinAdpPartyMembers(player);
               }

               return;
            }

            oplayer = (Player)var5.next();
            TitleMsg.sendFullTitle(oplayer, TitleMsg.join.replace("{PLAYER}", player.getName()), TitleMsg.subjoin.replace("{PLAYER}", player.getName()), 10, 20, 20, this.plugin);
         } while(playerCount == 1 && this.plugin.getConfig().getBoolean("invitationmessage.enabled"));

         Messages.sendMessage(oplayer, this.getFormattedMessage(player, msgtoarenaplayers));
      }
   }

   public void spectatePlayer(final Player player, String msgtoplayer, String msgtoarenaplayers) {
      if (this.arena.getPlayersManager().isSpectator(player.getName())) {
         player.setAllowFlight(true);
         player.setFlying(true);
         player.teleport(this.arena.getStructureManager().getSpectatorSpawn());
      } else {
         boolean isSpectatorOnly = false;
         if (!this.arena.getPlayersManager().getPlayers().contains(player)) {
            isSpectatorOnly = true;
            this.plugin.getPData().storePlayerLocation(player);
            this.arena.getPlayersManager().addSpectatorOnly(player.getName());
         }

         player.teleport(this.arena.getStructureManager().getSpectatorSpawn());
         if (!isSpectatorOnly) {
            this.arena.getPlayersManager().remove(player);
            ++this.arena.getGameHandler().lostPlayers;
            if (this.plugin.getConfig().getBoolean("scoreboard.removefromspectators")) {
               this.arena.getScoreboardHandler().removeScoreboard(player);
            }
         } else {
            this.storePlayerData(player);
            if (!this.arena.getStatusManager().isArenaStarting()) {
               this.arena.getGameHandler().count = this.arena.getStructureManager().getCountdown();
            }

            if (!this.arena.getStatusManager().isArenaRunning() && !this.arena.getStatusManager().isArenaRegenerating() && this.plugin.getConfig().getBoolean("special.UseScoreboard")) {
               this.arena.getScoreboardHandler().updateWaitingScoreboard(player);
            }
         }

         player.getInventory().clear();
         player.getInventory().setArmorContents(new ItemStack[4]);
         this.clearPotionEffects(player);
         player.setAllowFlight(true);
         player.setFlying(true);
         Iterator var6 = Bukkit.getOnlinePlayers().iterator();

         Player oplayer;
         while(var6.hasNext()) {
            oplayer = (Player)var6.next();
            oplayer.hidePlayer(this.plugin, player);
         }

         Messages.sendMessage(player, this.getFormattedMessage(player, msgtoplayer));
         this.plugin.getSignEditor().modifySigns(this.arena.getArenaName());
         if (!isSpectatorOnly) {
            msgtoarenaplayers = this.getFormattedMessage(player, msgtoarenaplayers);
            var6 = this.arena.getPlayersManager().getAllParticipantsCopy().iterator();

            while(var6.hasNext()) {
               oplayer = (Player)var6.next();
               Messages.sendMessage(oplayer, msgtoarenaplayers);
            }
         }

         this.arena.getPlayersManager().addSpectator(player);
         (new BukkitRunnable() {
            public void run() {
               if (PlayerHandler.this.plugin.getConfig().getBoolean("items.leave.use")) {
                  PlayerHandler.this.addLeaveItem(player);
               }

               if (PlayerHandler.this.plugin.getConfig().getBoolean("items.info.use")) {
                  PlayerHandler.this.addInfo(player);
               }

               if (PlayerHandler.this.plugin.getConfig().getBoolean("items.stats.use")) {
                  PlayerHandler.this.addStats(player);
               }

               if (PlayerHandler.this.plugin.getConfig().getBoolean("items.tracker.use")) {
                  PlayerHandler.this.addTracker(player);
               }

            }
         }).runTaskLater(this.plugin, 5L);
         if (!isSpectatorOnly) {
            this.plugin.getServer().getPluginManager().callEvent(new PlayerSpectateArenaEvent(player, this.arena));
         }

      }
   }

   public void dispatchPlayer(Player player) {
      player.closeInventory();
      if (this.arena.getPlayersManager().getPlayersCount() == 1) {
         player.teleport(this.arena.getStructureManager().getSpawnPoint());
      } else if (this.arena.getStructureManager().getSpectatorSpawnVector() != null) {
         this.spectatePlayer(player, Messages.playerlosttoplayer, Messages.playerlosttoothers);
      } else {
         this.leavePlayer(player, Messages.playerlosttoplayer, Messages.playerlosttoothers);
      }

   }

   public void leavePlayer(Player player, String msgtoplayer, String msgtoarenaplayers) {
      boolean spectator = this.arena.getPlayersManager().isSpectator(player.getName());
      Player oplayer;
      Iterator var6;
      if (spectator) {
         this.arena.getPlayersManager().removeSpectator(player.getName());
         var6 = Bukkit.getOnlinePlayers().iterator();

         while(var6.hasNext()) {
            oplayer = (Player)var6.next();
            oplayer.showPlayer(this.plugin, player);
         }
      } else if (this.arena.getPlayersManager().getPlayersCount() == 1 && this.arena.getStatusManager().isArenaRunning() && !this.arena.getGameHandler().isTimedOut()) {
         if (!this.arena.getStructureManager().isTestMode()) {
            return;
         }
      } else if (this.arena.getStatusManager().isArenaRunning()) {
         ++this.arena.getGameHandler().lostPlayers;
      }

      player.setAllowFlight(false);
      player.setFlying(false);
      this.arena.getScoreboardHandler().removeScoreboard(player);
      this.removePlayerFromArenaAndRestoreState(player, false);
      this.plugin.getServer().getPluginManager().callEvent(new PlayerLeaveArenaEvent(player, this.arena));
      if (!spectator) {
         Messages.sendMessage(player, this.getFormattedMessage(player, msgtoplayer));
         TitleMsg.sendFullTitle(player, TitleMsg.leave.replace("{PLAYER}", player.getName()), TitleMsg.subleave.replace("{PLAYER}", player.getName()), 10, 20, 20, this.plugin);
         this.plugin.getSignEditor().modifySigns(this.arena.getArenaName());
         if (!this.arena.getStatusManager().isArenaRunning()) {
            this.arena.getScoreboardHandler().createWaitingScoreBoard();
            if (this.spawnmap.containsKey(player.getName())) {
               this.arena.getStructureManager().getFreeSpawnList().add(((Location)this.spawnmap.get(player.getName())).toVector());
            }
         }

         msgtoarenaplayers = this.getFormattedMessage(player, msgtoarenaplayers);
         var6 = this.arena.getPlayersManager().getAllParticipantsCopy().iterator();

         while(var6.hasNext()) {
            oplayer = (Player)var6.next();
            Messages.sendMessage(oplayer, msgtoarenaplayers);
            TitleMsg.sendFullTitle(oplayer, TitleMsg.leave.replace("{PLAYER}", player.getName()), TitleMsg.subleave.replace("{PLAYER}", player.getName()), 10, 20, 20, this.plugin);
            if (!this.arena.getStatusManager().isArenaStarting() && !this.arena.getStatusManager().isArenaRunning()) {
               double progress = (double)this.arena.getPlayersManager().getPlayersCount() / (double)this.arena.getStructureManager().getMinPlayers();
               Bars.setBar(this.arena, Bars.waiting, this.arena.getPlayersManager().getPlayersCount(), 0, progress, this.plugin);
            }
         }

      }
   }

   protected void leaveWinner(Player player, String msgtoplayer) {
      this.arena.getScoreboardHandler().removeScoreboard(player);
      player.setFlying(false);
      this.removePlayerFromArenaAndRestoreState(player, true);
      Messages.sendMessage(player, msgtoplayer);
      this.plugin.getSignEditor().modifySigns(this.arena.getArenaName());
      this.plugin.getSignEditor().refreshLeaderBoards();
      this.arena.getStructureManager().getFreeSpawnList().clear();
      this.spawnmap.clear();
      this.setLinkedKitName((String)null);
   }

   private void removePlayerFromArenaAndRestoreState(Player player, boolean winner) {
      this.votes.remove(player.getName());
      Bars.removeBar(player, this.arena.getArenaName());
      this.resetDoubleJumps(player);
      this.updateWinStreak(player, winner);
      this.arena.getPlayersManager().remove(player);
      this.arena.getPlayersManager().removeSpectatorOnly(player.getName());
      this.clearPotionEffects(player);
      if (Utils.debug()) {
         Logger var10000 = this.plugin.getLogger();
         String var10001 = player.getName();
         var10000.info("Player " + var10001 + " left arena " + this.arena.getArenaName());
         this.plugin.getLogger().info("Players in arena: " + this.arena.getPlayersManager().getPlayersCount());
         this.plugin.getLogger().info("Spectators in arena: " + this.arena.getPlayersManager().getSpectators().size());
      }

      this.restorePlayerData(player);
      if (this.plugin.isBungeecord() && this.plugin.getConfig().getBoolean("bungeecord.teleporttohub", true)) {
         this.plugin.getBungeeHandler().connectToHub(player);
      } else {
         this.connectToLobby(player);
      }

      if (winner) {
         this.plugin.getServer().getPluginManager().callEvent(new RewardWinnerEvent(player, this.arena));
         this.arena.getStructureManager().getRewards().rewardPlayer(player, 1);
      } else if (this.arena.getGameHandler().getPlaces().containsValue(player.getName()) && !this.rewardedPlayers.contains(player.getName())) {
         this.arena.getGameHandler().getPlaces().entrySet().forEach((e) -> {
            if (((String)e.getValue()).equalsIgnoreCase(player.getName())) {
               this.arena.getStructureManager().getRewards().rewardPlayer(player, (Integer)e.getKey());
               this.rewardedPlayers.add(player.getName());
            }

         });
      }

      this.plugin.getPData().restorePlayerGameMode(player);
      player.updateInventory();
      this.plugin.getPData().restorePlayerFlight(player);
      this.removeFriendlyFire(player);
      if (this.plugin.getConfig().getBoolean("shop.onleave.removepurchase")) {
         this.removePurchase(player);
      }

      if (player.getGameMode() == GameMode.CREATIVE) {
         player.setAllowFlight(true);
      }

      if (this.arena.getStatusManager().isArenaRunning() && this.arena.getPlayersManager().getPlayersCount() == 0) {
         if (Utils.debug()) {
            this.plugin.getLogger().info("PH calling stopArena...");
         }

         this.arena.getGameHandler().stopArena();
      }

   }

   private void connectToLobby(Player player) {
      if (this.arena.getStructureManager().getTeleportDestination() != StructureManager.TeleportDestination.LOBBY && !this.plugin.isBungeecord()) {
         this.plugin.getPData().restorePlayerLocation(player);
      } else {
         this.plugin.getGlobalLobby().joinLobby(player);
         this.plugin.getPData().clearPlayerLocation(player);
      }

   }

   public boolean vote(Player player) {
      if (!this.votes.contains(player.getName())) {
         this.votes.add(player.getName());
         this.arena.getScoreboardHandler().createWaitingScoreBoard();
         if (!this.arena.getStatusManager().isArenaStarting() && this.forceStart()) {
            this.arena.getGameHandler().runArenaCountdown();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean forceStart() {
      if (this.arena.getPlayersManager().getPlayersCount() > 1 && (double)this.votes.size() >= (double)this.arena.getStructureManager().getMinPlayers() * this.arena.getStructureManager().getVotePercent()) {
         return true;
      } else {
         return this.arena.getGameHandler().isForceStartByCommand();
      }
   }

   private void addInfo(Player player) {
      ItemStack item = new ItemStack(Material.getMaterial(this.plugin.getConfig().getString("items.info.material")));
      ItemMeta meta = item.getItemMeta();
      meta.setDisplayName(FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("items.info.name")));
      item.setItemMeta(meta);
      player.getInventory().setItem(this.plugin.getConfig().getInt("items.info.slot", 1), item);
   }

   private void addVote(Player player) {
      ItemStack item = new ItemStack(Material.getMaterial(this.plugin.getConfig().getString("items.vote.material")));
      ItemMeta meta = item.getItemMeta();
      meta.setDisplayName(FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("items.vote.name")));
      item.setItemMeta(meta);
      player.getInventory().setItem(this.plugin.getConfig().getInt("items.vote.slot", 0), item);
   }

   private void addShop(Player player) {
      if (this.arena.getStructureManager().isShopEnabled()) {
         ItemStack item = new ItemStack(Material.getMaterial(this.plugin.getConfig().getString("items.shop.material")));
         ItemMeta meta = item.getItemMeta();
         meta.setDisplayName(FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("items.shop.name")));
         item.setItemMeta(meta);
         player.getInventory().setItem(this.plugin.getConfig().getInt("items.shop.slot", 2), item);
      }
   }

   private void addStats(Player player) {
      Material statsMaterial = Material.getMaterial(this.plugin.getConfig().getString("items.stats.material"));
      ItemStack item = new ItemStack(statsMaterial);
      ItemMeta meta = item.getItemMeta();
      meta.setDisplayName(FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("items.stats.name")));
      item.setItemMeta(meta);
      if (statsMaterial == Material.PLAYER_HEAD) {
         SkullMeta skullMeta = (SkullMeta)item.getItemMeta();
         skullMeta.setOwningPlayer(player);
         item.setItemMeta(skullMeta);
      }

      player.getInventory().setItem(this.plugin.getConfig().getInt("items.stats.slot", 3), item);
   }

   private void addHeads(Player player) {
      ItemStack item = new ItemStack(Material.getMaterial(this.plugin.getConfig().getString("items.heads.material")));
      ItemMeta meta = item.getItemMeta();
      meta.setDisplayName(FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("items.heads.name")));
      item.setItemMeta(meta);
      player.getInventory().setItem(this.plugin.getConfig().getInt("items.heads.slot", 4), item);
   }

   private void addTracker(Player player) {
      ItemStack item = new ItemStack(Material.getMaterial(this.plugin.getConfig().getString("items.tracker.material")));
      ItemMeta meta = item.getItemMeta();
      meta.setDisplayName(FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("items.tracker.name")));
      item.setItemMeta(meta);
      player.getInventory().setItem(this.plugin.getConfig().getInt("items.tracker.slot", 5), item);
   }

   protected void addDoubleJumpItem(Player player) {
      if (this.arena.getStructureManager().isAllowDoublejumps()) {
         ItemStack item = new ItemStack(Material.getMaterial(this.plugin.getConfig().getString("items.doublejump.material")));
         ItemMeta meta = item.getItemMeta();
         meta.setDisplayName(FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("items.doublejump.name")));
         item.setItemMeta(meta);
         player.getInventory().setItem(this.plugin.getConfig().getInt("items.doublejump.slot", 0), item);
      }
   }

   protected void addLeaveItem(Player player) {
      Material leaveMaterial = Material.getMaterial(this.plugin.getConfig().getString("items.leave.material"));
      if (leaveMaterial == null) {
         leaveMaterial = Material.getMaterial("GREEN_BED");
         this.plugin.getConfig().set("items.leave.material", leaveMaterial.toString());
         this.plugin.saveConfig();
      }

      ItemStack item = new ItemStack(leaveMaterial);
      ItemMeta im = item.getItemMeta();
      im.setDisplayName(FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("items.leave.name")));
      item.setItemMeta(im);
      player.getInventory().setItem(this.plugin.getConfig().getInt("items.leave.slot", 8), item);
   }

   public int getVotesCast() {
      return this.votes.size();
   }

   public int getVotesRequired(Arena arena) {
      int minPlayers = arena.getStructureManager().getMinPlayers();
      double votePercent = arena.getStructureManager().getVotePercent();
      return (int)(Math.ceil((double)minPlayers * votePercent) - (double)this.getVotesCast());
   }

   public void clearPotionEffects(Player player) {
      Iterator var3 = player.getActivePotionEffects().iterator();

      while(var3.hasNext()) {
         PotionEffect effect = (PotionEffect)var3.next();
         player.removePotionEffect(effect.getType());
      }

   }

   public void allocateKits(Player player) {
      List<String> kitnames = new ArrayList();
      if (this.arena.getStructureManager().hasLinkedKits()) {
         if (!this.arena.getStructureManager().isRandomKit() && this.getLinkedKitName() != null) {
            ((List)kitnames).add(this.getLinkedKitName());
         } else {
            kitnames = this.arena.getStructureManager().getLinkedKits();
         }
      } else {
         ((List)kitnames).addAll(this.plugin.getKitManager().getKits());
      }

      if (Utils.debug()) {
         this.plugin.getLogger().info("kitnames = " + kitnames.toString());
      }

      String kit = ((List)kitnames).size() > 1 ? this.getRandomKitName((List)kitnames) : (String)((List)kitnames).get(0);
      if (this.plugin.getKitManager().kitExists(kit)) {
         this.giveKitToPlayer(kit, player);
      } else {
         Messages.sendMessage(player, Messages.kitnotexists.replace("{KIT}", kit));
      }

      if (this.arena.getStructureManager().hasLinkedKits() && !this.arena.getStructureManager().isRandomKit() && this.getLinkedKitName() == null) {
         this.setLinkedKitName(kit);
      }

   }

   private String getRandomKitName(List<String> kits) {
      String[] kitnames = (String[])kits.toArray(new String[kits.size()]);
      Random rnd = new Random();
      return kitnames[rnd.nextInt(kitnames.length)];
   }

   private String getLinkedKitName() {
      return this.linkedKitName;
   }

   private void setLinkedKitName(String kitname) {
      this.linkedKitName = kitname;
   }

   private void giveKitToPlayer(String kitname, Player player) {
      ItemStack purchasedHead = null;
      if (player.getInventory().getHelmet() != null) {
         purchasedHead = new ItemStack(player.getInventory().getHelmet());
      }

      this.plugin.getKitManager().giveKit(kitname, player);
      if (purchasedHead != null && purchasedHead.getType() == Material.PLAYER_HEAD) {
         player.getInventory().setHelmet(purchasedHead);
      }

   }

   private void cacheDoubleJumps(Player player) {
      int amount = 0;
      if (this.plugin.getConfig().getBoolean("freedoublejumps.enabled")) {
         amount = Utils.getAllowedDoubleJumps(player, this.plugin.getConfig().getInt("freedoublejumps.amount", 0));
      } else if (this.plugin.getPData().hasStoredDoubleJumps(player)) {
         amount = this.plugin.getPData().getDoubleJumpsFromFile((OfflinePlayer)player);
      }

      if (amount > 0) {
         this.doublejumps.put(player.getName(), amount);
      }

   }

   public boolean hasDoubleJumps(String playerName) {
      return this.getDoubleJumps(playerName) > 0;
   }

   public int getDoubleJumps(String playerName) {
      return this.doublejumps.get(playerName) != null ? (Integer)this.doublejumps.get(playerName) : 0;
   }

   public void decrementDoubleJumps(String playerName) {
      if (this.hasDoubleJumps(playerName)) {
         this.doublejumps.put(playerName, this.getDoubleJumps(playerName) - 1);
      }

   }

   public void incrementDoubleJumps(String playerName, int amount) {
      this.doublejumps.put(playerName, this.getDoubleJumps(playerName) + amount);
   }

   private void resetDoubleJumps(Player player) {
      if (!this.plugin.getConfig().getBoolean("freedoublejumps.enabled")) {
         this.plugin.getPData().saveDoubleJumpsToFile((OfflinePlayer)player, this.getDoubleJumps(player.getName()));
      }

      this.doublejumps.remove(player.getName());
   }

   private void updateWinStreak(Player player, boolean winner) {
      if (!this.arena.getPlayersManager().isSpectatorOnly(player.getName()) && this.arena.getGameHandler().isStatsActive()) {
         int amount = winner ? this.plugin.getPData().getWinStreak(player) + 1 : 0;
         this.plugin.getPData().setWinStreak(player, amount);
      }
   }

   private void allowFriendlyFire(Player player) {
      if (this.plugin.getVaultHandler().isPermissions()) {
         if (PartyAPI.inParty(player)) {
            if (!this.plugin.getVaultHandler().getPermissions().playerHas(player, "mcmmo.party.friendlyfire")) {
               this.plugin.getVaultHandler().getPermissions().playerAdd(player, "mcmmo.party.friendlyfire");
               if (!this.pparty.contains(player.getName())) {
                  this.pparty.add(player.getName());
               }
            }

         }
      }
   }

   private void removeFriendlyFire(Player player) {
      if (this.pparty.contains(player.getName())) {
         this.pparty.remove(player.getName());
         this.plugin.getVaultHandler().getPermissions().playerRemove(player, "mcmmo.party.friendlyfire");
      }

   }

   public void removePurchase(Player player) {
      if (this.plugin.isGlobalShop()) {
         this.plugin.getShop().getPlayersItems().remove(player.getName());
         this.plugin.getShop().getBuyers().remove(player.getName());
         this.plugin.getShop().getPurchasedCommands().remove(player.getName());
         if (this.plugin.getShop().getPotionEffects(player) != null) {
            this.plugin.getShop().removePotionEffects(player);
         }

      }
   }

   private void storePlayerData(Player player) {
      this.plugin.getPData().storePlayerGameMode(player);
      this.plugin.getPData().storePlayerFlight(player);
      player.setFlying(false);
      player.setAllowFlight(false);
      this.plugin.getPData().storePlayerLevel(player);
      this.plugin.getPData().storePlayerInventory(player);
      this.plugin.getPData().storePlayerArmor(player);
      this.plugin.getPData().storePlayerPotionEffects(player);
      this.plugin.getPData().storePlayerHunger(player);
      this.plugin.getPData().storePlayerHealth(player);
      player.updateInventory();
      if (this.plugin.getConfig().getBoolean("special.UseScoreboard")) {
         this.plugin.getScoreboardManager().storePrejoinScoreboard(player);
      }

   }

   private void restorePlayerData(Player player) {
      this.plugin.getPData().restorePlayerHealth(player);
      this.plugin.getPData().restorePlayerHunger(player);
      this.plugin.getPData().restorePlayerPotionEffects(player);
      this.plugin.getPData().restorePlayerArmor(player);
      this.plugin.getPData().restorePlayerInventory(player);
      this.plugin.getPData().restorePlayerLevel(player);
      player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 80, 80, true));
      if (this.plugin.getConfig().getBoolean("special.UseScoreboard")) {
         this.plugin.getScoreboardManager().restorePrejoinScoreboard(player);
      }

   }

   private String getFormattedMessage(Player player, String message) {
      message = message.replace("{PLAYER}", player.getName()).replace("{RANK}", Utils.getRank((OfflinePlayer)player)).replace("{COLOR}", Utils.getColourMeta((OfflinePlayer)player)).replace("{ARENA}", this.arena.getArenaName()).replace("{PS}", String.valueOf(this.arena.getPlayersManager().getPlayersCount())).replace("{MPS}", String.valueOf(this.arena.getStructureManager().getMaxPlayers()));
      return FormattingCodesParser.parseFormattingCodes(message);
   }

   private void sendInvitationMessage(Player player) {
      if (this.plugin.getConfig().getBoolean("invitationmessage.enabled")) {
         String welcomeJoinMessage = this.getFormattedMessage(player, Messages.playerjoininvite);
         List<String> excludedPlayers = this.getExcludedPlayers(player.getName());
         Iterator var5 = Bukkit.getOnlinePlayers().iterator();

         while(var5.hasNext()) {
            Player aplayer = (Player)var5.next();
            if (!excludedPlayers.contains(aplayer.getName())) {
               Utils.displayJoinMessage(aplayer, this.arena.getArenaName(), welcomeJoinMessage);
               this.plugin.getSound().INVITE_MESSAGE(aplayer);
            }
         }
      }

   }

   private List<String> getExcludedPlayers(String originator) {
      List<String> excludedPlayers = new ArrayList();
      if (this.plugin.getConfig().getBoolean("invitationmessage.excludeplayers")) {
         excludedPlayers = Utils.getTNTRunPlayers();
      }

      if (this.plugin.getConfig().getBoolean("invitationmessage.excludeoriginator")) {
         if (!((List)excludedPlayers).contains(originator)) {
            ((List)excludedPlayers).add(originator);
         }
      } else {
         ((List)excludedPlayers).remove(originator);
      }

      return (List)excludedPlayers;
   }

   private void joinPartyMembers(Player player) {
      if (this.plugin.getParties().isPartyLeader(player)) {
         this.plugin.getParties().getPartyMembers(player.getName()).forEach((member) -> {
            Player p = Bukkit.getPlayer(member);
            if (p != null && this.checkJoin(p)) {
               this.spawnPlayer(p, Messages.playerjoinedtoothers);
            }

         });
      }
   }

   private void joinAdpPartyMembers(Player player) {
      PartiesAPI api = Parties.getApi();
      UUID uuid = player.getUniqueId();
      PartyPlayer partyPlayer = api.getPartyPlayer(uuid);
      if (partyPlayer != null && partyPlayer.isInParty()) {
         String partyName = partyPlayer.getPartyName();
         if (!partyName.isEmpty()) {
            Party party = api.getParty(partyName);
            if (uuid.equals(party.getLeader())) {
               party.getOnlineMembers().forEach((member) -> {
                  if (!member.equals(partyPlayer)) {
                     Player p = Bukkit.getPlayer(member.getPlayerUUID());
                     if (p != null && this.checkJoin(p)) {
                        this.spawnPlayer(p, Messages.playerjoinedtoothers);
                     }

                  }
               });
            }
         }
      }
   }

   public void clearRewardedPlayers() {
      this.rewardedPlayers.clear();
   }
}
