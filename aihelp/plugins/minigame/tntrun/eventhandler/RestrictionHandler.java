package tntrun.eventhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.messages.Messages;
import tntrun.utils.Heads;
import tntrun.utils.Utils;

public class RestrictionHandler implements Listener {
   private TNTRun plugin;
   private HashSet<String> allowedcommands = new HashSet(Arrays.asList("/tntrun leave", "/tntrun vote", "/tr leave", "/tr vote", "/tr help", "/tr info", "/tr stats", "/tntrun stats", "/tr", "/tntrun"));
   public ArrayList<String> u = new ArrayList();

   public RestrictionHandler(TNTRun plugin) {
      this.plugin = plugin;
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
      Player player = e.getPlayer();
      Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
      if (arena != null) {
         if (!player.hasPermission("tntrun.cmdblockbypass")) {
            if (!this.allowedcommands.contains(e.getMessage().toLowerCase()) && !this.plugin.getConfig().getStringList("commandwhitelist").contains(e.getMessage().toLowerCase())) {
               Messages.sendMessage(player, Messages.nopermission);
               e.setCancelled(true);
            }

         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onPlayerBlockBreak(BlockBreakEvent e) {
      Player player = e.getPlayer();
      Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
      if (arena != null) {
         e.setCancelled(true);
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onPlayerBlockPlace(BlockPlaceEvent e) {
      Player player = e.getPlayer();
      Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
      if (arena != null) {
         e.setCancelled(true);
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onPlayerItemDrop(PlayerDropItemEvent e) {
      Player player = e.getPlayer();
      Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
      if (arena != null) {
         e.setCancelled(true);
      }
   }

   @EventHandler
   public void onPlayerInteract(PlayerInteractEvent e) {
      Player player = e.getPlayer();
      Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
      if (arena != null) {
         if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getMaterial() == Material.getMaterial(this.plugin.getConfig().getString("items.leave.material"))) {
               e.setCancelled(true);
               this.plugin.getSound().ITEM_SELECT(player);
               if (arena.getStatusManager().isArenaRunning()) {
                  arena.getGameHandler().setPlaces(player.getName());
               }

               arena.getPlayerHandler().leavePlayer(player, Messages.playerlefttoplayer, Messages.playerlefttoothers);
            } else if (e.getMaterial() == Material.getMaterial(this.plugin.getConfig().getString("items.shop.material"))) {
               e.setCancelled(true);
               if (!this.plugin.isGlobalShop()) {
                  return;
               }

               this.plugin.getSound().ITEM_SELECT(player);
               this.plugin.getShop().buildShopMenu(player);
               player.openInventory(this.plugin.getShop().getInv(player.getName()));
            } else if (e.getMaterial() == Material.getMaterial(this.plugin.getConfig().getString("items.info.material"))) {
               e.setCancelled(true);
               if (this.u.contains(player.getName())) {
                  this.plugin.getSound().NOTE_PLING(player, 5.0F, 999.0F);
                  return;
               }

               this.u.add(player.getName());
               this.coolDown(player);
               this.plugin.getSound().ITEM_SELECT(player);
               Utils.displayInfo(player);
            } else if (e.getMaterial() == Material.getMaterial(this.plugin.getConfig().getString("items.vote.material"))) {
               e.setCancelled(true);
               if (this.u.contains(player.getName())) {
                  this.plugin.getSound().NOTE_PLING(player, 5.0F, 999.0F);
                  return;
               }

               this.plugin.getSound().ITEM_SELECT(player);
               this.u.add(player.getName());
               this.coolDown(player);
               if (arena.getStatusManager().isArenaStarting()) {
                  Messages.sendMessage(player, arena.getStatusManager().getFormattedMessage(Messages.arenastarting));
                  return;
               }

               if (arena.getPlayerHandler().vote(player)) {
                  Messages.sendMessage(player, Messages.playervotedforstart);
               } else {
                  Messages.sendMessage(player, Messages.playeralreadyvotedforstart);
               }
            } else if (e.getMaterial() == Material.getMaterial(this.plugin.getConfig().getString("items.stats.material"))) {
               e.setCancelled(true);
               if (this.u.contains(player.getName())) {
                  this.plugin.getSound().NOTE_PLING(player, 5.0F, 999.0F);
                  return;
               }

               this.u.add(player.getName());
               this.coolDown(player);
               this.plugin.getSound().ITEM_SELECT(player);
               player.chat("/tntrun stats");
            } else if (e.getMaterial() == Material.getMaterial(this.plugin.getConfig().getString("items.heads.material"))) {
               e.setCancelled(true);
               if (!player.hasPermission("tntrun.heads")) {
                  Messages.sendMessage(player, Messages.nopermission);
                  return;
               }

               this.plugin.getSound().ITEM_SELECT(player);
               Heads.openMenu(player);
            } else if (e.getMaterial() == Material.getMaterial(this.plugin.getConfig().getString("items.tracker.material"))) {
               e.setCancelled(true);
               this.plugin.getSound().ITEM_SELECT(player);
               this.plugin.getMenus().buildTrackerMenu(player, arena);
            } else if (e.getMaterial() == Material.getMaterial(this.plugin.getConfig().getString("items.doublejump.material"))) {
               e.setCancelled(true);
               this.plugin.getSound().ITEM_SELECT(player);
               this.handleFlight(player, arena);
            }

         }
      }
   }

   private void coolDown(final Player player) {
      (new BukkitRunnable() {
         public void run() {
            RestrictionHandler.this.u.remove(player.getName());
         }
      }).runTaskLater(this.plugin, 40L);
   }

   @EventHandler
   public void onFly(PlayerToggleFlightEvent e) {
      Player player = e.getPlayer();
      Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
      if (arena != null) {
         if (player.getGameMode() == GameMode.CREATIVE) {
            player.setAllowFlight(true);
         } else if (!arena.getPlayersManager().isSpectator(player.getName()) && !arena.getPlayersManager().isWinner(player.getName())) {
            e.setCancelled(true);
            this.handleFlight(player, arena);
         } else {
            e.setCancelled(false);
            player.setFlying(true);
         }
      }
   }

   private void handleFlight(final Player player, final Arena arena) {
      if (arena.getStatusManager().isArenaRunning()) {
         if (arena.getStructureManager().isAllowDoublejumps()) {
            if (!this.u.contains(player.getName())) {
               if (arena.getPlayerHandler().hasDoubleJumps(player.getName())) {
                  arena.getPlayerHandler().decrementDoubleJumps(player.getName());
                  player.setFlying(false);
                  player.setVelocity(player.getLocation().getDirection().multiply(this.plugin.getConfig().getDouble("doublejumps.multiplier", 1.5D)).setY(this.plugin.getConfig().getDouble("doublejumps.height", 0.7D)));
                  this.plugin.getSound().NOTE_PLING(player, 5.0F, 999.0F);
                  this.u.add(player.getName());
                  (new BukkitRunnable() {
                     public void run() {
                        RestrictionHandler.this.u.remove(player.getName());
                        if (!arena.getPlayerHandler().hasDoubleJumps(player.getName())) {
                           player.setAllowFlight(false);
                        }

                     }
                  }).runTaskLater(this.plugin, 20L);
               }
            }
         }
      }
   }

   @EventHandler
   public void onJoin(PlayerJoinEvent e) {
      final Player player = e.getPlayer();
      if (player.hasPermission("tntrun.version.check") && this.plugin.needUpdate()) {
         (new BukkitRunnable() {
            public void run() {
               Utils.displayUpdate(player);
            }
         }).runTaskLaterAsynchronously(this.plugin, 30L);
      }

      if (this.plugin.useStats() && !this.plugin.isFile()) {
         if (!this.plugin.getStats().hasDatabaseEntry(player)) {
            final String table = this.plugin.getConfig().getString("MySQL.table", "stats");
            (new BukkitRunnable() {
               public void run() {
                  String uuid = RestrictionHandler.this.plugin.useUuid() ? player.getUniqueId().toString() : player.getName();
                  RestrictionHandler.this.plugin.getMysql().query("INSERT IGNORE INTO `" + table + "` (`username`, `played`, `wins`, `streak`) VALUES ('" + uuid + "', '0', '0', '0');");
               }
            }).runTaskAsynchronously(this.plugin);
         }
      }
   }

   @EventHandler
   public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof Player) {
         Player player = (Player)event.getEntity();
         Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
         if (arena != null) {
            if (event.getDamager() instanceof Firework) {
               event.setCancelled(true);
            }

         }
      }
   }

   @EventHandler
   public void onInventoryClick(InventoryClickEvent event) {
      Player player = (Player)event.getWhoClicked();
      Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
      if (arena != null) {
         event.setCancelled(true);
      }
   }

   @EventHandler
   public void onProjectileImpact(ProjectileHitEvent e) {
      Projectile projectile = e.getEntity();
      if (projectile.getShooter() instanceof Player) {
         Player player = (Player)projectile.getShooter();
         Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
         if (arena != null) {
            if (this.plugin.getConfig().getBoolean("removearrows")) {
               if (e.getHitBlock() != null) {
                  if (projectile instanceof Arrow || projectile instanceof Trident) {
                     projectile.remove();
                  }

               }
            }
         }
      }
   }

   @EventHandler
   public void onLeaveLobby(PlayerTeleportEvent event) {
      if (this.plugin.getConfig().getBoolean("special.UseScoreboard") && this.plugin.getConfig().getBoolean("scoreboard.enablelobbyscoreboard")) {
         Player player = event.getPlayer();
         if (this.plugin.getScoreboardManager().hasLobbyScoreboard(player)) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            this.plugin.getScoreboardManager().restorePrejoinScoreboard(player);
            this.plugin.getScoreboardManager().removeLobbyScoreboard(player.getName());
         }

      }
   }
}
