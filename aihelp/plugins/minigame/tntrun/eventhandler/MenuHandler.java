package tntrun.eventhandler;

import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.menu.MenuHolder;
import tntrun.messages.Messages;
import tntrun.utils.Utils;

public class MenuHandler implements Listener {
   private TNTRun plugin;

   public MenuHandler(TNTRun plugin) {
      this.plugin = plugin;
   }

   @EventHandler
   public void onInventoryClick(InventoryClickEvent event) {
      if (event.getWhoClicked() instanceof Player) {
         Player player = (Player)event.getWhoClicked();
         InventoryHolder holder = event.getInventory().getHolder();
         if (holder instanceof MenuHolder) {
            event.setCancelled(true);
            MenuHolder menuHolder = (MenuHolder)holder;
            String menuType = menuHolder.getMenuType();
            switch(menuType.hashCode()) {
            case -1354792126:
               if (menuType.equals("config")) {
                  this.handleConfigSelect(event, player);
               }
               break;
            case -1067395272:
               if (menuType.equals("tracker")) {
                  this.handleTrackerSelect(event, player);
               }
               break;
            case 3267882:
               if (menuType.equals("join")) {
                  this.handleArenaSelect(event, player);
               }
            }

         }
      }
   }

   public void handleTrackerSelect(InventoryClickEvent e, Player player) {
      Inventory inv = e.getClickedInventory();
      if (inv != null) {
         if (this.isValidClick(e)) {
            ItemStack is = e.getCurrentItem();
            if (is != null && is.getType() == Material.PLAYER_HEAD) {
               String target = is.getItemMeta().getDisplayName();
               Player targetPlayer = Bukkit.getPlayer(target);
               Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
               if (targetPlayer != null && arena.getPlayersManager().getPlayers().contains(targetPlayer)) {
                  player.teleport(targetPlayer.getLocation());
                  player.closeInventory();
               } else {
                  Messages.sendMessage(player, Messages.playernotplaying);
               }
            }
         }
      }
   }

   public void handleArenaSelect(InventoryClickEvent e, Player player) {
      Inventory inv = e.getClickedInventory();
      if (inv != null) {
         if (this.isValidClick(e)) {
            ItemStack is = e.getCurrentItem();
            if (is != null) {
               if (is.getType() != this.plugin.getMenus().getPane()) {
                  String cmd = "tntrun join " + ChatColor.stripColor(is.getItemMeta().getDisplayName());
                  Bukkit.dispatchCommand(player, cmd);
                  player.closeInventory();
               }
            }
         }
      }
   }

   public void handleConfigSelect(InventoryClickEvent e, Player player) {
      Inventory inv = e.getClickedInventory();
      if (inv != null) {
         String title = e.getView().getTitle();
         if (title.startsWith("TNTRun setup")) {
            if (this.isValidClick(e)) {
               String arenaname = ChatColor.stripColor(title.substring(title.lastIndexOf(" ") + 1));
               Arena arena = this.plugin.amanager.getArenaByName(arenaname);
               if (arena != null) {
                  ItemStack is = e.getCurrentItem();
                  if (is != null) {
                     inv.setMaxStackSize(256);
                     boolean leftclick = e.getClick().isLeftClick();
                     int page = Character.getNumericValue(title.charAt(title.indexOf("/") - 1));
                     int slot = e.getRawSlot();
                     String cmd = "trsetup ";
                     int pos;
                     String damage;
                     switch(slot) {
                     case 4:
                        String status = arena.getStatusManager().isArenaEnabled() ? "disable " : "enable ";
                        Bukkit.dispatchCommand(player, "trsetup " + status + arenaname);
                        break;
                     case 5:
                     case 6:
                     case 7:
                     case 8:
                     case 9:
                     case 13:
                     case 17:
                     case 18:
                     case 22:
                     case 26:
                     case 28:
                     case 29:
                     case 30:
                     case 32:
                     case 33:
                     case 34:
                     default:
                        return;
                     case 10:
                        if (page == 1) {
                           cmd = cmd + "setlobby";
                        } else {
                           pos = leftclick ? arena.getStructureManager().getCountdown() + 5 : arena.getStructureManager().getCountdown() - 5;
                           cmd = cmd + "setcountdown " + arenaname + " " + pos;
                        }

                        Bukkit.dispatchCommand(player, cmd);
                        break;
                     case 11:
                        if (page == 1) {
                           cmd = cmd + "setarena " + arenaname;
                        } else {
                           pos = leftclick ? arena.getStructureManager().getTimeLimit() + 10 : arena.getStructureManager().getTimeLimit() - 10;
                           cmd = cmd + "settimelimit " + arenaname + " " + pos;
                        }

                        Bukkit.dispatchCommand(player, cmd);
                        break;
                     case 12:
                        if (page == 1) {
                           cmd = cmd + "setloselevel " + arenaname;
                           Bukkit.dispatchCommand(player, cmd);
                        } else {
                           if (arena.getStatusManager().isArenaEnabled()) {
                              Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", arenaname));
                              return;
                           }

                           pos = leftclick ? arena.getStructureManager().getStartVisibleCountdown() + 1 : arena.getStructureManager().getStartVisibleCountdown() - 1;
                           arena.getStructureManager().setStartVisibleCountdown(pos);
                        }
                        break;
                     case 14:
                        if (page == 1) {
                           Bukkit.dispatchCommand(player, "trsetup setspawn " + arenaname);
                        } else {
                           if (arena.getStatusManager().isArenaEnabled()) {
                              Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", arenaname));
                              return;
                           }

                           arena.getStructureManager().toggleTestMode();
                        }
                        break;
                     case 15:
                        if (page == 1) {
                           cmd = cmd + "setspectate " + arenaname;
                        } else {
                           pos = leftclick ? arena.getStructureManager().getGameLevelDestroyDelay() + 1 : arena.getStructureManager().getGameLevelDestroyDelay() - 1;
                           cmd = cmd + "setgameleveldestroydelay " + arenaname + " " + pos;
                        }

                        Bukkit.dispatchCommand(player, cmd);
                        break;
                     case 16:
                        if (page == 1) {
                           damage = arena.getStructureManager().getTeleportDestination().toString();
                           damage = damage.equalsIgnoreCase("LOBBY") ? " PREVIOUS" : " LOBBY";
                           cmd = cmd + "setteleport " + arenaname + damage;
                        } else {
                           pos = leftclick ? arena.getStructureManager().getRegenerationDelay() + 1 : arena.getStructureManager().getRegenerationDelay() - 1;
                           cmd = cmd + "setregenerationdelay " + arenaname + " " + pos;
                        }

                        Bukkit.dispatchCommand(player, cmd);
                        break;
                     case 19:
                        if (page == 1) {
                           pos = leftclick ? arena.getStructureManager().getMinPlayers() + 1 : arena.getStructureManager().getMinPlayers() - 1;
                           cmd = cmd + "setminplayers " + arenaname + " " + pos;
                        } else {
                           damage = arena.getStructureManager().getDamageEnabled().toString();
                           if (damage.equalsIgnoreCase("NO")) {
                              damage = " YES";
                           } else if (damage.equalsIgnoreCase("YES")) {
                              damage = " ZERO";
                           } else {
                              damage = " NO";
                           }

                           cmd = cmd + "setdamage " + arenaname + damage;
                        }

                        Bukkit.dispatchCommand(player, cmd);
                        break;
                     case 20:
                        if (page == 1) {
                           pos = leftclick ? arena.getStructureManager().getMaxPlayers() + 1 : arena.getStructureManager().getMaxPlayers() - 1;
                           cmd = cmd + "setmaxplayers " + arenaname + " " + pos;
                           Bukkit.dispatchCommand(player, cmd);
                        } else {
                           if (arena.getStatusManager().isArenaEnabled()) {
                              Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", arenaname));
                              return;
                           }

                           arena.getStructureManager().togglePunchDamage();
                        }
                        break;
                     case 21:
                        if (page == 1) {
                           double percent = leftclick ? arena.getStructureManager().getVotePercent() + 0.05D : arena.getStructureManager().getVotePercent() - 0.05D;
                           cmd = cmd + "setvotepercent " + arenaname + " " + Utils.getDecimalFormat(String.valueOf(percent));
                        } else {
                           damage = arena.getStructureManager().isKitsEnabled() ? "disablekits " : "enablekits ";
                           cmd = cmd + damage + arenaname;
                        }

                        Bukkit.dispatchCommand(player, cmd);
                        break;
                     case 23:
                        if (page == 1) {
                           Block block = player.getTargetBlock((Set)null, 5);
                           if (block.getState() instanceof Sign) {
                              this.plugin.getSignEditor().createJoinSign(block, arenaname);
                              Messages.sendMessage(player, Messages.signcreate);
                           } else {
                              Messages.sendMessage(player, Messages.signfail);
                           }

                           player.closeInventory();
                        } else {
                           if (arena.getStatusManager().isArenaEnabled()) {
                              Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", arenaname));
                              return;
                           }

                           arena.getStructureManager().toggleArenaStats();
                        }
                        break;
                     case 24:
                        if (page == 1) {
                           if (arena.getStatusManager().isArenaEnabled()) {
                              Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", arenaname));
                              return;
                           }

                           pos = leftclick ? arena.getStructureManager().getMaxFinalPositions() + 1 : arena.getStructureManager().getMaxFinalPositions() - 1;
                           arena.getStructureManager().setMaxFinalPositions(pos);
                        } else {
                           if (arena.getStatusManager().isArenaEnabled()) {
                              Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", arenaname));
                              return;
                           }

                           pos = leftclick ? arena.getStructureManager().getStatsMinPlayers() + 1 : arena.getStructureManager().getStatsMinPlayers() - 1;
                           arena.getStructureManager().setStatsMinPlayers(pos);
                        }
                        break;
                     case 25:
                        if (page == 1) {
                           Bukkit.dispatchCommand(player, "trsetup finish " + arenaname);
                        } else {
                           if (arena.getStatusManager().isArenaEnabled()) {
                              Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", arenaname));
                              return;
                           }

                           arena.getStructureManager().toggleShopEnabled();
                        }
                        break;
                     case 27:
                        if (page == 2) {
                           player.closeInventory();
                           this.plugin.getMenus().buildConfigMenu(player, arena, 1);
                        }

                        return;
                     case 31:
                        player.closeInventory();
                        return;
                     case 35:
                        if (page == 1) {
                           player.closeInventory();
                           this.plugin.getMenus().buildConfigMenu(player, arena, 2);
                        }

                        return;
                     }

                     this.plugin.getMenus().updateConfigItem(inv, slot, arena, page);
                     if (slot == 19 && page == 1) {
                        this.plugin.getMenus().updateConfigItem(inv, 21, arena, page);
                     }

                     player.updateInventory();
                  }
               }
            }
         }
      }
   }

   private boolean isValidClick(InventoryClickEvent e) {
      e.setCancelled(true);
      if (e.getRawSlot() >= e.getView().getTopInventory().getSize()) {
         return false;
      } else {
         return e.getAction() != InventoryAction.HOTBAR_MOVE_AND_READD && e.getAction() != InventoryAction.HOTBAR_SWAP && e.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY;
      }
   }
}
