package tntrun.menu;

import com.google.common.base.Enums;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.messages.Messages;
import tntrun.utils.FormattingCodesParser;

public class Menus {
   private final TNTRun plugin;
   private int keyPos;

   public Menus(TNTRun plugin) {
      this.plugin = plugin;
   }

   public void buildJoinMenu(Player player) {
      TreeMap<String, Arena> arenas = this.getDisplayArenas();
      int size = this.getJoinMenuSize(arenas.size());
      Inventory inv = Bukkit.createInventory(new MenuHolder("join"), size, FormattingCodesParser.parseFormattingCodes(Messages.menutitle));
      this.keyPos = 9;
      arenas.entrySet().stream().limit(28L).forEach((e) -> {
         Arena arena = (Arena)e.getValue();
         boolean isPvp = arena.getStructureManager().isPvpEnabled();
         List<String> lores = new ArrayList();
         ItemStack is = new ItemStack(this.getMenuItem(arena, isPvp));
         ItemMeta im = is.getItemMeta();
         im.setDisplayName(FormattingCodesParser.parseFormattingCodes(Messages.menuarenaname).replace("{ARENA}", arena.getArenaName()));
         lores.add(FormattingCodesParser.parseFormattingCodes(Messages.menutext).replace("{PS}", String.valueOf(arena.getPlayersManager().getPlayersCount())).replace("{MPS}", String.valueOf(arena.getStructureManager().getMaxPlayers())));
         if (arena.getStructureManager().hasFee()) {
            lores.add(FormattingCodesParser.parseFormattingCodes(Messages.menufee.replace("{FEE}", arena.getStructureManager().getArenaCost())));
         }

         if (isPvp && Messages.menupvp.length() > 0) {
            lores.add(FormattingCodesParser.parseFormattingCodes(Messages.menupvp));
         }

         im.setLore(lores);
         im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
         is.setItemMeta(im);
         switch(this.keyPos) {
         case 16:
         case 25:
         case 34:
         case 43:
            this.keyPos += 3;
            break;
         default:
            ++this.keyPos;
         }

         inv.setItem(this.keyPos, is);
      });
      this.fillEmptySlots(inv, size);
      player.openInventory(inv);
   }

   public void buildTrackerMenu(Player player, Arena arena) {
      int size = this.getTrackerMenuSize(arena.getPlayersManager().getPlayersCount());
      Inventory inv = Bukkit.createInventory(new MenuHolder("tracker"), size, FormattingCodesParser.parseFormattingCodes(Messages.menutracker));
      Iterator var6 = arena.getPlayersManager().getPlayers().iterator();

      while(var6.hasNext()) {
         Player p = (Player)var6.next();
         ItemStack is = new ItemStack(Material.PLAYER_HEAD);
         ItemMeta meta = is.getItemMeta();
         meta.setDisplayName(p.getName());
         SkullMeta skullMeta = (SkullMeta)meta;
         skullMeta.setOwningPlayer(p);
         is.setItemMeta(skullMeta);
         inv.addItem(new ItemStack[]{is});
      }

      this.fillEmptySlots(inv, size);
      player.openInventory(inv);
   }

   public void buildConfigMenu(Player player, Arena arena, int page) {
      int size = true;
      String var10000 = "TNTRun setup {PAGE}/2 - ".replace("{PAGE}", String.valueOf(page));
      String title = var10000 + arena.getArenaName();
      Inventory inv = Bukkit.createInventory(new MenuHolder("config"), 36, title);
      if (page == 1) {
         Stream.of(ConfigMenu.values()).forEach((item) -> {
            int slot = item.getSlot();
            inv.setItem(slot, this.createConfigItem(Material.getMaterial(String.valueOf(item)), slot, arena, page));
         });
      } else {
         Stream.of(ConfigMenu2.values()).forEach((item) -> {
            int slot = item.getSlot();
            inv.setItem(slot, this.createConfigItem(Material.getMaterial(String.valueOf(item)), slot, arena, page));
         });
      }

      this.fillEmptySlots(inv, 36);
      player.openInventory(inv);
   }

   private ItemStack createConfigItem(Material material, int slot, Arena arena, int page) {
      String done = "Complete";
      String todo = String.valueOf(ChatColor.RED) + "Not set";
      String var10000 = String.valueOf(ChatColor.GOLD);
      String status = var10000 + "Status: " + String.valueOf(ChatColor.GREEN);
      List<String> lores = new ArrayList();
      boolean showhelp = this.plugin.getConfig().getBoolean("configmenu.lore", true);
      ItemStack is = new ItemStack(material);
      ItemMeta im = is.getItemMeta();
      switch(slot) {
      case 4:
         if (arena.getStatusManager().isArenaEnabled()) {
            is.setType(Material.LIME_WOOL);
         }

         im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set arena status");
         if (showhelp) {
            lores.add(String.valueOf(ChatColor.GRAY) + "Click to enable or disable the arena.");
         }

         lores.add(status + (arena.getStatusManager().isArenaEnabled() ? "Enabled" : "Disabled"));
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
         break;
      case 10:
         if (page == 1) {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set global lobby");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Set the TNTRun lobby at your current location.");
               lores.add(String.valueOf(ChatColor.GRAY) + "This is the lobby players will return to after the game.");
            }

            lores.add(status + (this.plugin.getGlobalLobby().isLobbyLocationSet() ? done : todo));
         } else {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set arena countdown");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Left click to increase, right click to decrease.");
            }

            lores.add(status + arena.getStructureManager().getCountdown());
         }
         break;
      case 11:
         if (page == 1) {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set arena bounds");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Set the corner points of a cuboid which");
               lores.add(String.valueOf(ChatColor.GRAY) + "completely encloses the arena.");
            }

            lores.add(status + (arena.getStructureManager().isArenaBoundsSet() ? done : todo));
         } else {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set arena time limit");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Left click to increase, right click to decrease.");
            }

            lores.add(status + arena.getStructureManager().getTimeLimit());
         }
         break;
      case 12:
         if (page == 1) {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set lose level");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Set the point at which players lose to your");
               lores.add(String.valueOf(ChatColor.GRAY) + "current Y location. You must be within");
               lores.add(String.valueOf(ChatColor.GRAY) + "the arena bounds to set the lose level.");
            }

            lores.add(status + (arena.getStructureManager().getLoseLevel().isConfigured() ? done : todo));
         } else {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set start time of visible countdown");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Set the time at which the countdown is");
               lores.add(String.valueOf(ChatColor.GRAY) + "displayed continuously on the screen.");
               lores.add(String.valueOf(ChatColor.GRAY) + "Left click to increase, right click to decrease.");
            }

            lores.add(status + arena.getStructureManager().getStartVisibleCountdown());
         }
         break;
      case 14:
         if (page == 1) {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set arena spawn point");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Set the point that players joining the arena");
               lores.add(String.valueOf(ChatColor.GRAY) + "will spawn to your current location.");
            }

            lores.add(status + (arena.getStructureManager().isSpawnpointSet() ? done : todo));
         } else {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set test mode status");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Click to enable or disable test mode.");
            }

            lores.add(status + (arena.getStructureManager().isTestMode() ? "Enabled" : "Disabled"));
         }
         break;
      case 15:
         if (page == 1) {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set spectator spawn point");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Set the point that spectators will spawn");
               lores.add(String.valueOf(ChatColor.GRAY) + "to your current location.");
            }

            lores.add(status + (arena.getStructureManager().isSpectatorSpawnSet() ? done : todo));
         } else {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set gameleveldestroydelay");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Set the time before a block is broken after being");
               lores.add(String.valueOf(ChatColor.GRAY) + "stepped on by a player (default 8 ticks).");
               lores.add(String.valueOf(ChatColor.GRAY) + "Left click to increase, right click to decrease.");
            }

            lores.add(status + arena.getStructureManager().getGameLevelDestroyDelay());
         }
         break;
      case 16:
         if (page == 1) {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set teleport location");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "When the game ends players will teleport to either");
               lores.add(String.valueOf(ChatColor.GRAY) + "their previous location or to the lobby.");
               lores.add(String.valueOf(ChatColor.GRAY) + "Click to toggle between LOBBY and PREVIOUS location.");
            }

            lores.add(status + String.valueOf(arena.getStructureManager().getTeleportDestination()));
         } else {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set regeneration delay");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Set the time allowed for regeneration before players");
               lores.add(String.valueOf(ChatColor.GRAY) + "are allowed to re-join the arena (default 60 ticks).");
               lores.add(String.valueOf(ChatColor.GRAY) + "Left click to increase, right click to decrease.");
            }

            lores.add(status + arena.getStructureManager().getRegenerationDelay());
         }
         break;
      case 19:
         if (page == 1) {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set the minimum number of players");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Left click to increase, right click to decrease.");
            }

            lores.add(status + arena.getStructureManager().getMinPlayers());
            is.setAmount(arena.getStructureManager().getMinPlayers());
         } else {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set damage (PVP)");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Enable or disable PVP in the arena by setting");
               lores.add(String.valueOf(ChatColor.GRAY) + "the damage indicator.");
               lores.add(String.valueOf(ChatColor.GRAY) + "Click to toggle between YES, NO and ZERO.");
            }

            lores.add(status + String.valueOf(arena.getStructureManager().getDamageEnabled()));
         }
         break;
      case 20:
         if (page == 1) {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set the maximum number of players");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Left click to increase, right click to decrease.");
            }

            lores.add(status + arena.getStructureManager().getMaxPlayers());
            is.setAmount(arena.getStructureManager().getMaxPlayers());
         } else {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set punch damage status");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "In a PVP arena, inflicting damage with an");
               lores.add(String.valueOf(ChatColor.GRAY) + "empty hand (punch) can be disallowed. ");
               lores.add(String.valueOf(ChatColor.GRAY) + "Click to enable or disable punch damage.");
            }

            lores.add(status + (arena.getStructureManager().isPunchDamage() ? "Enabled" : "Disabled"));
         }
         break;
      case 21:
         if (page == 1) {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set vote percentage");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Determine the votes needed to force-start");
               lores.add(String.valueOf(ChatColor.GRAY) + "the arena with < the minimum players.");
               lores.add(String.valueOf(ChatColor.GRAY) + "Left click to increase, right click to decrease.");
            }

            lores.add(status + arena.getStructureManager().getVotePercent() + String.valueOf(ChatColor.GOLD) + "  Votes Required: " + String.valueOf(ChatColor.GREEN) + arena.getPlayerHandler().getVotesRequired(arena));
         } else {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set kit status");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Click to enable or disable kits.");
            }

            lores.add(status + (arena.getStructureManager().isKitsEnabled() ? "Enabled" : "Disabled"));
         }
         break;
      case 23:
         if (page == 1) {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Create a join sign");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Target a sign and click to create a join sign.");
            }
         } else {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set arena stats status");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Click to enable or disable arena stats.");
            }

            lores.add(status + (arena.getStructureManager().isArenaStatsEnabled() ? "Enabled" : "Disabled"));
         }
         break;
      case 24:
         if (page == 1) {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set max final leaderboard size");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Set the maximum number of player positions");
               lores.add(String.valueOf(ChatColor.GRAY) + "to display at the end of a game.");
               lores.add(String.valueOf(ChatColor.GRAY) + "The default is 3, displaying 1st, 2nd and 3rd.");
               lores.add(String.valueOf(ChatColor.GRAY) + "Left click to increase, right click to decrease.");
            }

            lores.add(status + arena.getStructureManager().getMaxFinalPositions());
            is.setAmount(arena.getStructureManager().getMaxFinalPositions());
         } else {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set min players for stats");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Set the minimum number of players required");
               lores.add(String.valueOf(ChatColor.GRAY) + "for stats to be recorded.");
               lores.add(String.valueOf(ChatColor.GRAY) + "A value of zero means stats is always active.");
               lores.add(String.valueOf(ChatColor.GRAY) + "Left click to increase, right click to decrease.");
            }

            lores.add(status + arena.getStructureManager().getStatsMinPlayers());
         }
         break;
      case 25:
         if (page == 1) {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Finish configuring the arena");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Save the settings and enable the arena.");
            }

            lores.add(status + (arena.getStructureManager().isArenaFinished() ? done : todo));
         } else {
            im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Set shop status");
            if (showhelp) {
               lores.add(String.valueOf(ChatColor.GRAY) + "Click to enable or disable the shop.");
            }

            lores.add(status + (arena.getStructureManager().isShopEnabled() ? "Enabled" : "Disabled"));
         }
         break;
      case 27:
         im.setDisplayName(String.valueOf(ChatColor.GREEN) + "<- Back");
         break;
      case 31:
         im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Exit");
         break;
      case 35:
         im.setDisplayName(String.valueOf(ChatColor.GREEN) + "Next ->");
      }

      im.setLore(lores);
      im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
      is.setItemMeta(im);
      return is;
   }

   public void updateConfigItem(Inventory inv, int slot, Arena arena, int page) {
      String itemName = page == 1 ? String.valueOf(ConfigMenu.getName(slot)) : String.valueOf(ConfigMenu2.getName(slot));
      inv.setItem(slot, this.createConfigItem(Material.getMaterial(itemName), slot, arena, page));
   }

   private void fillEmptySlots(Inventory inv, Integer size) {
      ItemStack is = new ItemStack(this.getPane());
      if (is.getType() != Material.AIR) {
         for(int i = 0; i < size; ++i) {
            if (inv.getItem(i) == null) {
               ItemMeta im = is.getItemMeta();
               im.setDisplayName(String.valueOf(ChatColor.RED).makeConcatWithConstants<invokedynamic>(String.valueOf(ChatColor.RED)));
               is.setItemMeta(im);
               inv.setItem(i, is);
            }
         }

      }
   }

   public Material getPane() {
      String colour = this.plugin.getConfig().getString("menu.panecolor", "LIGHT_BLUE").toUpperCase();
      return colour != "NONE" && colour != "AIR" && Enums.getIfPresent(DyeColor.class, colour).orNull() != null ? Material.getMaterial(colour + "_STAINED_GLASS_PANE") : Material.AIR;
   }

   private Material getMenuItem(Arena arena, boolean pvpEnabled) {
      String path = pvpEnabled ? "menu.pvpitem" : "menu.item";
      String item = this.plugin.getConfig().getString(path, "TNT").toUpperCase();
      if (arena.getStructureManager().hasMenuItem(pvpEnabled)) {
         item = arena.getStructureManager().getMenuItem(pvpEnabled).toUpperCase();
      }

      return Material.getMaterial(item) != null ? Material.getMaterial(item) : Material.TNT;
   }

   public void autoJoin(Player player, String type) {
      if (this.plugin.amanager.getPlayerArena(player.getName()) != null) {
         Messages.sendMessage(player, Messages.arenajoined);
      } else if (!player.hasPermission("tntrun.autojoin")) {
         Messages.sendMessage(player, Messages.nopermission);
      } else {
         Arena autoArena = this.getAutoArena(player, type);
         if (autoArena == null) {
            Messages.sendMessage(player, Messages.noarenas);
         } else {
            if (autoArena.getPlayerHandler().processFee(player, false)) {
               autoArena.getPlayerHandler().spawnPlayer(player, Messages.playerjoinedtoothers);
            }

         }
      }
   }

   private Arena getAutoArena(Player player, String type) {
      Object var10000;
      Arena autoarena;
      int playercount;
      label30: {
         new HashSet();
         autoarena = null;
         playercount = -1;
         String var6;
         switch((var6 = type.toLowerCase()).hashCode()) {
         case 111402:
            if (var6.equals("pvp")) {
               var10000 = this.plugin.amanager.getPvpArenas();
               break label30;
            }
            break;
         case 105005513:
            if (var6.equals("nopvp")) {
               var10000 = this.plugin.amanager.getNonPvpArenas();
               break label30;
            }
         }

         var10000 = this.plugin.amanager.getArenas();
      }

      Collection<Arena> arenas = var10000;
      List<Arena> arenalist = new ArrayList((Collection)arenas);
      Collections.shuffle(arenalist);
      Iterator var9 = arenalist.iterator();

      while(var9.hasNext()) {
         Arena arena = (Arena)var9.next();
         if (arena.getPlayerHandler().checkJoin(player, true) && arena.getPlayersManager().getPlayersCount() > playercount) {
            autoarena = arena;
            playercount = arena.getPlayersManager().getPlayersCount();
         }
      }

      return autoarena;
   }

   private TreeMap<String, Arena> getDisplayArenas() {
      TreeMap<String, Arena> arenas = new TreeMap(String.CASE_INSENSITIVE_ORDER);
      Iterator var3 = this.plugin.amanager.getArenas().iterator();

      while(true) {
         Arena arena;
         do {
            if (!var3.hasNext()) {
               return arenas;
            }

            arena = (Arena)var3.next();
         } while(!arena.getStatusManager().isArenaEnabled() && !this.plugin.getConfig().getBoolean("menu.includedisabled"));

         arenas.put(arena.getArenaName(), arena);
      }
   }

   private int getJoinMenuSize(int size) {
      int invsize = 54;
      if (size < 8) {
         invsize = 27;
      } else if (size < 15) {
         invsize = 36;
      } else if (size < 22) {
         invsize = 45;
      }

      return invsize;
   }

   private int getTrackerMenuSize(int size) {
      int invsize = 54;
      if (size < 10) {
         invsize = 9;
      } else if (size < 19) {
         invsize = 18;
      } else if (size < 28) {
         invsize = 27;
      } else if (size < 37) {
         invsize = 36;
      } else if (size < 46) {
         invsize = 45;
      }

      return invsize;
   }
}
