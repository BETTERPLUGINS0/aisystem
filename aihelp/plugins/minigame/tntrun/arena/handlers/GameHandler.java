package tntrun.arena.handlers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.events.ArenaStartEvent;
import tntrun.events.ArenaTimeoutEvent;
import tntrun.events.PlayerWinArenaEvent;
import tntrun.messages.Messages;
import tntrun.signs.editor.SignEditor;
import tntrun.utils.Bars;
import tntrun.utils.TitleMsg;
import tntrun.utils.Utils;

public class GameHandler {
   private TNTRun plugin;
   private Arena arena;
   public int lostPlayers = 0;
   private boolean activeStats = true;
   private Map<Integer, String> places = new HashMap();
   private SignEditor signEditor;
   private int leavetaskid;
   int runtaskid;
   public int count;
   private int timeremaining;
   private int arenahandler;
   private int startingPlayers;
   private boolean forceStartByCmd;
   private boolean hasTimeLimit;

   public GameHandler(TNTRun plugin, Arena arena) {
      this.plugin = plugin;
      this.arena = arena;
      this.count = arena.getStructureManager().getCountdown();
      this.signEditor = plugin.getSignEditor();
   }

   public void startArenaAntiLeaveHandler() {
      this.leavetaskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
         public void run() {
            Iterator var2 = GameHandler.this.arena.getPlayersManager().getPlayersCopy().iterator();

            Player player;
            while(var2.hasNext()) {
               player = (Player)var2.next();
               if (!GameHandler.this.arena.getStructureManager().isInArenaBounds(player.getLocation())) {
                  if (GameHandler.this.arena.getStatusManager().isArenaStarting()) {
                     GameHandler.this.arena.getPlayerHandler().leavePlayer(player, Messages.playerlefttoplayer, Messages.playerlefttoothers);
                  } else {
                     GameHandler.this.setPlaces(player.getName());
                     GameHandler.this.arena.getPlayerHandler().dispatchPlayer(player);
                  }
               }
            }

            var2 = GameHandler.this.arena.getPlayersManager().getSpectatorsCopy().iterator();

            while(var2.hasNext()) {
               player = (Player)var2.next();
               if (!GameHandler.this.arena.getStructureManager().isInArenaBounds(player.getLocation())) {
                  GameHandler.this.arena.getPlayerHandler().spectatePlayer(player, "", "");
               }
            }

         }
      }, 0L, 1L);
   }

   public void stopArenaAntiLeaveHandler() {
      Bukkit.getScheduler().cancelTask(this.leavetaskid);
   }

   public void runArenaCountdown() {
      this.count = this.arena.getStructureManager().getCountdown();
      this.arena.getStatusManager().setStarting(true);
      this.signEditor.modifySigns(this.arena.getArenaName());
      final int antiCamping = Math.max(this.plugin.getConfig().getInt("anticamping.teleporttime"), 5);
      final int startVisibleCountdown = this.arena.getStructureManager().getStartVisibleCountdown();
      this.runtaskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
         public void run() {
            double progressbar;
            if (GameHandler.this.arena.getPlayersManager().getPlayersCount() < GameHandler.this.arena.getStructureManager().getMinPlayers() && !GameHandler.this.arena.getPlayerHandler().forceStart()) {
               progressbar = (double)GameHandler.this.arena.getPlayersManager().getPlayersCount() / (double)GameHandler.this.arena.getStructureManager().getMinPlayers();
               Bars.setBar(GameHandler.this.arena, Bars.waiting, GameHandler.this.arena.getPlayersManager().getPlayersCount(), 0, progressbar, GameHandler.this.plugin);
               GameHandler.this.arena.getScoreboardHandler().createWaitingScoreBoard();
               GameHandler.this.stopArenaCountdown();
            } else {
               if (GameHandler.this.count == 0) {
                  GameHandler.this.stopArenaCountdown();
                  GameHandler.this.startArena();
                  return;
               }

               String message;
               Player playerx;
               Iterator var3;
               if (GameHandler.this.count == antiCamping) {
                  message = Messages.arenacountdown.replace("{COUNTDOWN}", String.valueOf(GameHandler.this.count));

                  for(var3 = GameHandler.this.arena.getPlayersManager().getPlayers().iterator(); var3.hasNext(); GameHandler.this.displayCountdown(playerx, GameHandler.this.count, message)) {
                     playerx = (Player)var3.next();
                     if (GameHandler.this.isAntiCamping() && !GameHandler.this.arena.getStructureManager().hasAdditionalSpawnPoints()) {
                        playerx.teleport(GameHandler.this.arena.getStructureManager().getSpawnPoint());
                     }
                  }
               } else if (GameHandler.this.count <= startVisibleCountdown || GameHandler.this.count % 10 == 0) {
                  message = Messages.arenacountdown.replace("{COUNTDOWN}", String.valueOf(GameHandler.this.count));
                  var3 = GameHandler.this.arena.getPlayersManager().getPlayers().iterator();

                  while(var3.hasNext()) {
                     playerx = (Player)var3.next();
                     GameHandler.this.displayCountdown(playerx, GameHandler.this.count, message);
                  }
               }
            }

            GameHandler.this.arena.getScoreboardHandler().createWaitingScoreBoard();
            progressbar = (double)GameHandler.this.count / (double)GameHandler.this.arena.getStructureManager().getCountdown();
            Bars.setBar(GameHandler.this.arena, Bars.starting, 0, GameHandler.this.count, progressbar, GameHandler.this.plugin);
            if (GameHandler.this.plugin.getConfig().getBoolean("usexpbar.countdown")) {
               Iterator var4 = GameHandler.this.arena.getPlayersManager().getPlayers().iterator();

               while(var4.hasNext()) {
                  Player player = (Player)var4.next();
                  player.setLevel(GameHandler.this.count);
               }
            }

            --GameHandler.this.count;
         }
      }, 0L, 20L);
   }

   public void stopArenaCountdown() {
      this.arena.getStatusManager().setStarting(false);
      this.signEditor.modifySigns(this.arena.getArenaName());
      Bukkit.getScheduler().cancelTask(this.runtaskid);
      this.count = this.arena.getStructureManager().getCountdown();
   }

   private void startArena() {
      this.arena.getStatusManager().setRunning(true);
      this.startingPlayers = this.arena.getPlayersManager().getPlayersCount();
      if (Utils.debug()) {
         this.plugin.getLogger().info("Arena " + this.arena.getArenaName() + " started");
         this.plugin.getLogger().info("Players in arena: " + this.startingPlayers);
      }

      this.plugin.getServer().getPluginManager().callEvent(new ArenaStartEvent(this.arena));
      Iterator var2 = this.arena.getPlayersManager().getSpectators().iterator();

      while(var2.hasNext()) {
         Player player = (Player)var2.next();
         this.arena.getScoreboardHandler().removeScoreboard(player);
      }

      this.arena.getStructureManager().getRewards().setStartingPlayers(this.startingPlayers);
      this.setActiveStats(this.startingPlayers);
      String message = Messages.trprefix;
      int limit = this.arena.getStructureManager().getTimeLimit();
      if (limit != 0) {
         this.hasTimeLimit = true;
         message = message + Messages.arenastarted;
         message = message.replace("{TIMELIMIT}", String.valueOf(this.arena.getStructureManager().getTimeLimit()));
      } else {
         this.hasTimeLimit = false;
         message = message + Messages.arenanolimit;
      }

      Iterator var4 = this.arena.getPlayersManager().getPlayers().iterator();

      while(var4.hasNext()) {
         Player player = (Player)var4.next();
         player.closeInventory();
         player.setLevel(0);
         if (this.plugin.useStats() && this.isStatsActive()) {
            this.plugin.getStats().addPlayedGames(player, 1);
         }

         if (this.arena.getPlayerHandler().hasDoubleJumps(player.getName())) {
            player.setAllowFlight(true);
         }

         Messages.sendMessage(player, message, false);
         this.plugin.getSound().ARENA_START(player);
         this.setGameInventory(player);
         TitleMsg.sendFullTitle(player, TitleMsg.start, TitleMsg.substart, 20, 20, 20, this.plugin);
         if (this.arena.getStructureManager().hasCommandsOnStart()) {
            this.executeCommandsOnStart(player);
         }
      }

      if (this.plugin.useStats() && this.isStatsActive()) {
         this.plugin.getStats().clearPlayedList();
      }

      this.signEditor.modifySigns(this.arena.getArenaName());
      this.timeremaining = limit * 20;
      this.arena.getScoreboardHandler().createPlayingScoreBoard();
      this.arenahandler = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
         public void run() {
            if (GameHandler.this.arena.getPlayersManager().getPlayersCount() == 0) {
               if (Utils.debug()) {
                  GameHandler.this.plugin.getLogger().info("GH calling stopArena...");
               }

               GameHandler.this.stopArena();
            } else if (!GameHandler.this.isTimedOut()) {
               double progress = 1.0D;
               int seconds = 0;
               if (GameHandler.this.hasTimeLimit) {
                  progress = (double)GameHandler.this.timeremaining / (double)(GameHandler.this.arena.getStructureManager().getTimeLimit() * 20);
                  seconds = (int)Math.ceil((double)GameHandler.this.timeremaining / 20.0D);
               }

               Bars.setBar(GameHandler.this.arena, Bars.playing, GameHandler.this.arena.getPlayersManager().getPlayersCount(), seconds, progress, GameHandler.this.plugin);

               Player playerx;
               for(Iterator var5 = GameHandler.this.arena.getPlayersManager().getPlayersCopy().iterator(); var5.hasNext(); GameHandler.this.handlePlayer(playerx)) {
                  playerx = (Player)var5.next();
                  if (GameHandler.this.plugin.getConfig().getBoolean("usexpbar.timelimit")) {
                     playerx.setLevel(seconds);
                  }
               }

               --GameHandler.this.timeremaining;
            } else {
               GameHandler.this.plugin.getServer().getPluginManager().callEvent(new ArenaTimeoutEvent(GameHandler.this.arena));
               GameHandler.this.places.clear();
               Iterator var2 = GameHandler.this.arena.getPlayersManager().getPlayersCopy().iterator();

               while(var2.hasNext()) {
                  Player player = (Player)var2.next();
                  GameHandler.this.arena.getPlayerHandler().leavePlayer(player, Messages.arenatimeout, "");
               }

            }
         }
      }, (long)(this.plugin.getConfig().getInt("onstart.delay", 0) * 20), 1L);
   }

   public void stopArena() {
      if (!this.arena.getStatusManager().isArenaRunning()) {
         if (Utils.debug()) {
            this.plugin.getLogger().info("stopArena: arena not running. Exiting...");
         }

      } else {
         this.arena.getStatusManager().setRunning(false);
         Iterator var2 = this.arena.getPlayersManager().getAllParticipantsCopy().iterator();

         while(var2.hasNext()) {
            Player player = (Player)var2.next();
            if (Utils.debug()) {
               this.plugin.getLogger().info("stopArena is removing player " + player.getName());
            }

            if (this.arena.getStructureManager().hasCommandsOnStop()) {
               this.executeCommandsOnStop(player);
            }

            this.arena.getScoreboardHandler().removeScoreboard(player);
            this.arena.getPlayerHandler().leavePlayer(player, "", "");
         }

         this.lostPlayers = 0;
         this.timeremaining = 0;
         this.forceStartByCmd = false;
         this.places.clear();
         this.arena.getPlayerHandler().clearRewardedPlayers();
         this.arena.getPlayersManager().setWinner((String)null);
         Bukkit.getScheduler().cancelTask(this.arenahandler);
         Bukkit.getScheduler().cancelTask(this.arena.getScoreboardHandler().getPlayingTask());
         this.signEditor.modifySigns(this.arena.getArenaName());
         if (this.arena.getStatusManager().isArenaEnabled()) {
            this.startArenaRegen();
         }

      }
   }

   private void handlePlayer(Player player) {
      Location plloc = player.getLocation();
      Location plufloc = plloc.clone().add(0.0D, -1.0D, 0.0D);
      this.arena.getStructureManager().getGameZone().destroyBlock(plufloc);
      if (this.arena.getPlayersManager().getPlayersCount() == 1 && !this.arena.getStructureManager().isTestMode()) {
         this.startEnding(player);
      } else {
         if (this.arena.getStructureManager().getLoseLevel().isLoseLocation(plloc)) {
            if (this.arena.getPlayersManager().getPlayersCount() == 1) {
               this.startEnding(player);
               return;
            }

            this.setPlaces(player.getName());
            this.arena.getPlayerHandler().dispatchPlayer(player);
         }

      }
   }

   public Map<Integer, String> getPlaces() {
      return this.places;
   }

   public void setPlaces(String playerName) {
      if (!this.places.containsValue(playerName) && !this.arena.getPlayersManager().isSpectator(playerName) && this.arena.getStatusManager().isArenaRunning()) {
         int remainingPlayers = this.arena.getPlayersManager().getPlayersCount();
         this.places.put(remainingPlayers, playerName);
      }
   }

   private void startArenaRegen() {
      if (!this.arena.getStatusManager().isArenaRegenerating()) {
         this.arena.getStatusManager().setRegenerating(true);
         if (Utils.debug()) {
            this.plugin.getLogger().info("Arena regen started");
         }

         this.signEditor.modifySigns(this.arena.getArenaName());
         int delay = this.arena.getStructureManager().getGameZone().regen();
         (new BukkitRunnable() {
            public void run() {
               GameHandler.this.arena.getStatusManager().setRegenerating(false);
               GameHandler.this.signEditor.modifySigns(GameHandler.this.arena.getArenaName());
               if (!GameHandler.this.plugin.isBungeecord()) {
                  this.cancel();
               } else {
                  if (GameHandler.this.plugin.getConfig().getBoolean("bungeecord.randomarena")) {
                     GameHandler.this.plugin.amanager.setBungeeArena();
                  }

                  if (GameHandler.this.plugin.getConfig().getBoolean("bungeecord.stopserver")) {
                     (new BukkitRunnable() {
                        public void run() {
                           Bukkit.shutdown();
                        }
                     }).runTaskLater(GameHandler.this.plugin, 20L);
                  }

               }
            }
         }).runTaskLater(this.plugin, (long)delay);
      }
   }

   private void startEnding(final Player player) {
      if (this.plugin.useStats() && this.isStatsActive()) {
         this.plugin.getStats().addWins(player, 1);
      }

      this.arena.getPlayersManager().setWinner(player.getName());
      TitleMsg.sendFullTitle(player, TitleMsg.win, TitleMsg.subwin, 20, 60, 20, this.plugin);
      this.arena.getPlayerHandler().clearPotionEffects(player);
      String message = this.getPodiumPlaces(player);
      Player p;
      Iterator var4;
      if (this.plugin.getConfig().getInt("broadcastwinlevel") == 1) {
         var4 = this.arena.getPlayersManager().getAllParticipantsCopy().iterator();

         while(var4.hasNext()) {
            p = (Player)var4.next();
            Messages.sendMessage(p, message, false);
         }
      } else if (this.plugin.getConfig().getInt("broadcastwinlevel") >= 2) {
         var4 = Bukkit.getOnlinePlayers().iterator();

         while(var4.hasNext()) {
            p = (Player)var4.next();
            Messages.sendMessage(p, message, false);
         }
      }

      Logger var10000 = this.plugin.getLogger();
      String var10001 = player.getName();
      var10000.info("1. " + var10001 + ", 2. " + (String)this.getPlaces().get(2) + ", 3. " + (String)this.getPlaces().get(3));
      player.setAllowFlight(true);
      player.setFlying(true);
      var4 = this.arena.getPlayersManager().getAllParticipantsCopy().iterator();

      while(var4.hasNext()) {
         p = (Player)var4.next();
         this.plugin.getSound().ARENA_START(p);
         p.teleport(this.arena.getStructureManager().getSpawnPoint());
         p.closeInventory();
         p.getInventory().clear();
      }

      Bukkit.getScheduler().cancelTask(this.arenahandler);
      Bukkit.getScheduler().cancelTask(this.arena.getScoreboardHandler().getPlayingTask());
      this.plugin.getServer().getPluginManager().callEvent(new PlayerWinArenaEvent(player, this.arena));
      if (this.plugin.getConfig().getBoolean("fireworksonwin.enabled")) {
         (new BukkitRunnable() {
            int i = 0;

            public void run() {
               if (this.i >= GameHandler.this.getFireworkDuration() * 2 - 1 || GameHandler.this.arena.getPlayersManager().getPlayersCount() == 0) {
                  this.cancel();
               }

               Firework f = (Firework)player.getWorld().spawn(GameHandler.this.arena.getStructureManager().getSpawnPoint(), Firework.class);
               FireworkMeta fm = f.getFireworkMeta();
               fm.addEffect(FireworkEffect.builder().withColor(Color.GREEN).withColor(Color.RED).withColor(Color.PURPLE).with(Type.BALL_LARGE).withFlicker().build());
               fm.setPower(1);
               f.setFireworkMeta(fm);
               ++this.i;
            }
         }).runTaskTimer(this.plugin, 0L, 10L);
      }

      (new BukkitRunnable() {
         public void run() {
            try {
               if (GameHandler.this.arena.getPlayersManager().getPlayersCount() == 1) {
                  String msg = GameHandler.this.arena.getStructureManager().isTestMode() ? Messages.playerfinishedtestmode : Messages.playerwontoplayer;
                  GameHandler.this.arena.getPlayerHandler().leaveWinner(player, msg);
                  if (GameHandler.this.arena.getStructureManager().hasCommandsOnStop()) {
                     GameHandler.this.executeCommandsOnStop(player);
                  }
               }

               if (Utils.debug()) {
                  GameHandler.this.plugin.getLogger().info("GH StartEnding calling stopArena...");
               }

               GameHandler.this.stopArena();
               ConsoleCommandSender console = Bukkit.getConsoleSender();
               if (!GameHandler.this.plugin.getConfig().getStringList("commandsonwin").isEmpty()) {
                  GameHandler.this.plugin.getConfig().getStringList("commandsonwin").forEach((command) -> {
                     Bukkit.dispatchCommand(console, command.replace("{PLAYER}", player.getName()).replace("{ARENA}", GameHandler.this.arena.getArenaName()));
                  });
               }

               if (GameHandler.this.plugin.getConfig().getStringList("commandsonlose").isEmpty()) {
                  return;
               }

               GameHandler.this.arena.getPlayersManager().getAllParticipantsCopy().stream().filter((p) -> {
                  return !p.getName().equalsIgnoreCase(player.getName());
               }).forEach((p) -> {
                  GameHandler.this.plugin.getConfig().getStringList("commandsonlose").forEach((command) -> {
                     Bukkit.dispatchCommand(console, command.replace("{PLAYER}", p.getName()).replace("{ARENA}", GameHandler.this.arena.getArenaName()));
                  });
               });
            } catch (NullPointerException var2) {
            }

         }
      }).runTaskLater(this.plugin, (long)(40 + this.getFireworkDuration() * 20));
   }

   private int getFireworkDuration() {
      return this.plugin.getConfig().getInt("fireworksonwin.duration", 4);
   }

   private boolean isAntiCamping() {
      return this.plugin.getConfig().getBoolean("anticamping.enabled", true);
   }

   private void displayCountdown(Player player, int count, String message) {
      this.plugin.getSound().NOTE_PLING(player, 1.0F, 999.0F);
      if (!this.plugin.getConfig().getBoolean("special.UseTitle")) {
         Messages.sendMessage(player, message);
      } else {
         TitleMsg.sendFullTitle(player, TitleMsg.starting.replace("{COUNT}", count.makeConcatWithConstants<invokedynamic>(count)), TitleMsg.substarting.replace("{COUNT}", count.makeConcatWithConstants<invokedynamic>(count)), 0, 40, 20, this.plugin);
      }
   }

   private void setGameInventory(Player player) {
      player.getInventory().remove(Material.getMaterial(this.plugin.getConfig().getString("items.shop.material")));
      player.getInventory().remove(Material.getMaterial(this.plugin.getConfig().getString("items.vote.material")));
      player.getInventory().remove(Material.getMaterial(this.plugin.getConfig().getString("items.info.material")));
      player.getInventory().remove(Material.getMaterial(this.plugin.getConfig().getString("items.stats.material")));
      player.getInventory().remove(Material.getMaterial(this.plugin.getConfig().getString("items.heads.material")));
      player.getInventory().setItemInOffHand((ItemStack)null);
      if (this.arena.getStructureManager().isKitsEnabled() && this.plugin.getKitManager().getKits().size() > 0) {
         this.arena.getPlayerHandler().allocateKits(player);
         if (this.plugin.getConfig().getBoolean("items.leave.use")) {
            this.arena.getPlayerHandler().addLeaveItem(player);
         }
      }

      if (this.plugin.getConfig().getBoolean("items.doublejump.use")) {
         this.arena.getPlayerHandler().addDoubleJumpItem(player);
      }

      this.givePlayerPurchasedItems(player);
   }

   private void givePlayerPurchasedItems(Player player) {
      if (this.plugin.isGlobalShop() && this.arena.getStructureManager().isShopEnabled()) {
         List cmds;
         if (this.plugin.getShop().getPlayersItems().containsKey(player.getName())) {
            cmds = (List)this.plugin.getShop().getPlayersItems().get(player.getName());
            if (cmds != null) {
               Iterator var4 = cmds.iterator();

               while(var4.hasNext()) {
                  ItemStack item = (ItemStack)var4.next();
                  if (this.isArmor(item)) {
                     this.setArmorItem(player, item);
                  } else {
                     player.getInventory().addItem(new ItemStack[]{item});
                  }
               }
            }

            player.updateInventory();
         }

         if (this.plugin.getShop().getPotionEffects(player) != null) {
            Iterator var6 = this.plugin.getShop().getPotionEffects(player).iterator();

            while(var6.hasNext()) {
               PotionEffect pe = (PotionEffect)var6.next();
               player.addPotionEffect(pe);
            }
         }

         cmds = (List)this.plugin.getShop().getPurchasedCommands().get(player.getName());
         if (cmds != null) {
            ConsoleCommandSender console = Bukkit.getConsoleSender();
            cmds.stream().forEach((cmd) -> {
               Bukkit.dispatchCommand(console, cmd.replace("{PLAYER}", player.getName()).replace("%PLAYER%", player.getName()));
            });
         }

         this.arena.getPlayerHandler().removePurchase(player);
      }
   }

   private boolean isArmor(ItemStack item) {
      List<String> armor = List.of("HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS");
      Stream var10000 = armor.stream();
      String var10001 = item.getType().toString();
      var10001.getClass();
      return var10000.anyMatch(var10001::contains);
   }

   private void setArmorItem(Player player, ItemStack item) {
      if (item.toString().contains("BOOTS")) {
         player.getInventory().setBoots(item);
      } else if (item.toString().contains("LEGGINGS")) {
         player.getInventory().setLeggings(item);
      } else if (item.toString().contains("CHESTPLATE")) {
         player.getInventory().setChestplate(item);
      } else if (item.toString().contains("HELMET")) {
         player.getInventory().setHelmet(item);
      }

   }

   public void forceStartByCommand() {
      this.forceStartByCmd = true;
      this.runArenaCountdown();
   }

   public boolean isForceStartByCommand() {
      return this.forceStartByCmd;
   }

   public int getTimeRemaining() {
      return this.hasTimeLimit ? this.timeremaining : 0;
   }

   public boolean isTimedOut() {
      return this.hasTimeLimit && this.timeremaining < 0;
   }

   private String getPodiumPlaces(Player winner) {
      StringBuilder sb = new StringBuilder(200);
      String header = Messages.resultshead.replace("{ARENA}", this.arena.getArenaName());
      sb.append("\n" + header);
      sb.append("\n ");
      sb.append("\n" + Messages.playerposition.replace("{POS}", "1").replace("{RANK}", Utils.getRank(winner.getName())).replace("{COLOR}", Utils.getColourMeta((OfflinePlayer)winner)).replace("{PLAYER}", winner.getName()));
      this.places.entrySet().stream().sorted(Entry.comparingByKey()).forEach((e) -> {
         if ((Integer)e.getKey() <= Math.min(this.arena.getStructureManager().getMaxFinalPositions(), this.startingPlayers)) {
            String playerName = (String)e.getValue();
            String var10001 = Messages.playerposition.replace("{POS}", String.valueOf(e.getKey())).replace("{RANK}", Utils.getRank(playerName)).replace("{COLOR}", Utils.getColourMeta(playerName));
            sb.append("\n" + var10001.replace("{PLAYER}", playerName));
         }

      });
      sb.append("\n ");
      sb.append("\n" + header);
      return sb.toString();
   }

   private void executeCommandsOnStart(Player player) {
      this.arena.getStructureManager().getCommandsOnStart().forEach((command) -> {
         Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command.replace("{PLAYER}", player.getName()).replace("{ARENA}", this.arena.getArenaName()));
      });
   }

   private void executeCommandsOnStop(Player player) {
      this.arena.getStructureManager().getCommandsOnStop().forEach((command) -> {
         Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command.replace("{PLAYER}", player.getName()).replace("{ARENA}", this.arena.getArenaName()));
      });
   }

   private void setActiveStats(int playercount) {
      if (!this.arena.getStructureManager().isArenaStatsEnabled()) {
         this.activeStats = false;
      } else {
         this.activeStats = playercount >= this.arena.getStructureManager().getStatsMinPlayers();
         if (Utils.debug()) {
            Logger var10000 = this.plugin.getLogger();
            boolean var10001 = this.activeStats;
            var10000.info("Stats active: " + var10001 + ", min players = " + this.arena.getStructureManager().getStatsMinPlayers());
         }

      }
   }

   protected boolean isStatsActive() {
      return this.activeStats;
   }
}
