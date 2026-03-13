/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package com.andrei1058.bedwars.arena;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.team.TeamColor;
import com.andrei1058.bedwars.api.events.server.SetupSessionCloseEvent;
import com.andrei1058.bedwars.api.events.server.SetupSessionStartEvent;
import com.andrei1058.bedwars.api.server.ISetupSession;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.api.server.SetupType;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import com.andrei1058.bedwars.configuration.ArenaConfig;
import com.andrei1058.bedwars.support.paper.TeleportManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SetupSession
implements ISetupSession {
    private static List<SetupSession> setupSessions = new ArrayList<SetupSession>();
    private Player player;
    private String worldName;
    private SetupType setupType;
    private ArenaConfig cm;
    private boolean started = false;
    private boolean autoCreatedEmerald = false;
    private boolean autoCreatedDiamond = false;
    private List<Location> skipAutoCreateGen = new ArrayList<Location>();

    public SetupSession(Player player, String worldName) {
        this.player = player;
        this.worldName = worldName;
        SetupSession.getSetupSessions().add(this);
        SetupSession.openGUI(player);
    }

    public void setSetupType(SetupType setupType) {
        this.setupType = setupType;
    }

    public static List<SetupSession> getSetupSessions() {
        return setupSessions;
    }

    public static String getInvName() {
        return "\u00a78Choose a setup method";
    }

    public static int getAdvancedSlot() {
        return 5;
    }

    public static int getAssistedSlot() {
        return 3;
    }

    @Override
    public SetupType getSetupType() {
        return this.setupType;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public String getWorldName() {
        return this.worldName;
    }

    public boolean isStarted() {
        return this.started;
    }

    public boolean startSetup() {
        this.getPlayer().sendMessage("\u00a76 \u25aa \u00a77Loading " + this.getWorldName());
        this.cm = new ArenaConfig((Plugin)BedWars.plugin, this.getWorldName(), BedWars.plugin.getDataFolder().getPath() + "/Arenas");
        BedWars.getAPI().getRestoreAdapter().onSetupSessionStart(this);
        return true;
    }

    private static void openGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, (int)9, (String)SetupSession.getInvName());
        ItemStack assisted = new ItemStack(Material.GLOWSTONE_DUST);
        ItemMeta am = assisted.getItemMeta();
        am.setDisplayName("\u00a7e\u00a7lASSISTED SETUP");
        am.setLore(Arrays.asList("", "\u00a7aEasy and quick setup!", "\u00a77For beginners and lazy staff :D", "", "\u00a73Reduced options."));
        assisted.setItemMeta(am);
        inv.setItem(SetupSession.getAssistedSlot(), assisted);
        ItemStack advanced = new ItemStack(Material.REDSTONE);
        ItemMeta amm = advanced.getItemMeta();
        amm.setDisplayName("\u00a7c\u00a7lADVANCED SETUP");
        amm.setLore(Arrays.asList("", "\u00a7aDetailed setup!", "\u00a77For experienced staff :D", "", "\u00a73Advanced options."));
        advanced.setItemMeta(amm);
        inv.setItem(SetupSession.getAdvancedSlot(), advanced);
        player.openInventory(inv);
    }

    public void cancel() {
        SetupSession.getSetupSessions().remove(this);
        if (this.isStarted()) {
            this.player.sendMessage("\u00a76 \u25aa \u00a77" + this.getWorldName() + " setup cancelled!");
            this.done();
        }
    }

    public void done() {
        BedWars.getAPI().getRestoreAdapter().onSetupSessionClose(this);
        SetupSession.getSetupSessions().remove(this);
        if (BedWars.getServerType() != ServerType.BUNGEE) {
            try {
                TeleportManager.teleportC((Entity)this.getPlayer(), BedWars.config.getConfigLoc("lobbyLoc"), PlayerTeleportEvent.TeleportCause.PLUGIN);
            } catch (Exception ex) {
                TeleportManager.teleportC((Entity)this.getPlayer(), ((World)Bukkit.getWorlds().get(0)).getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        }
        this.getPlayer().removePotionEffect(PotionEffectType.SPEED);
        if (BedWars.getServerType() == ServerType.MULTIARENA) {
            Arena.sendLobbyCommandItems(this.getPlayer());
        }
        Bukkit.getPluginManager().callEvent((Event)new SetupSessionCloseEvent(this));
    }

    public static boolean isInSetupSession(UUID player) {
        for (SetupSession ss : SetupSession.getSetupSessions()) {
            if (!ss.getPlayer().getUniqueId().equals(player)) continue;
            return true;
        }
        return false;
    }

    public static SetupSession getSession(UUID p) {
        for (SetupSession ss : SetupSession.getSetupSessions()) {
            if (!ss.getPlayer().getUniqueId().equals(p)) continue;
            return ss;
        }
        return null;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    @Override
    public ArenaConfig getConfig() {
        return this.cm;
    }

    @Override
    public void teleportPlayer() {
        this.player.getInventory().clear();
        TeleportManager.teleport((Entity)this.player, Bukkit.getWorld((String)this.getWorldName()).getSpawnLocation());
        this.player.setGameMode(GameMode.CREATIVE);
        Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
            this.player.setAllowFlight(true);
            this.player.setFlying(true);
        }, 5L);
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        this.player.sendMessage("\n" + String.valueOf(ChatColor.WHITE) + "\n");
        for (int x = 0; x < 10; ++x) {
            this.getPlayer().sendMessage(" ");
        }
        this.player.sendMessage(String.valueOf(ChatColor.GREEN) + "You were teleported to the " + String.valueOf(ChatColor.GOLD) + this.getWorldName() + String.valueOf(ChatColor.GREEN) + "'s spawn.");
        if (this.getSetupType() == SetupType.ASSISTED && this.getConfig().getYml().get("waiting.Loc") == null) {
            this.player.sendMessage("");
            this.player.sendMessage(String.valueOf(ChatColor.GREEN) + "Hello " + this.player.getDisplayName() + "!");
            this.player.sendMessage(String.valueOf(ChatColor.WHITE) + "Please set the waiting spawn.");
            this.player.sendMessage(String.valueOf(ChatColor.WHITE) + "It is the place where players will wait the game to start.");
            this.player.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(String.valueOf(ChatColor.BLUE) + "     \u25aa     " + String.valueOf(ChatColor.GOLD) + "CLICK HERE TO SET THE WAITING LOBBY    " + String.valueOf(ChatColor.BLUE) + " \u25aa", String.valueOf(ChatColor.LIGHT_PURPLE) + "Click to set the waiting spawn.", "/" + BedWars.mainCmd + " setWaitingSpawn", ClickEvent.Action.RUN_COMMAND));
            this.player.spigot().sendMessage((BaseComponent)MainCommand.createTC(String.valueOf(ChatColor.YELLOW) + "Or type: " + String.valueOf(ChatColor.GRAY) + "/" + BedWars.mainCmd + " to see the command list.", "/" + BedWars.mainCmd, String.valueOf(ChatColor.WHITE) + "Show commands list."));
        } else {
            Bukkit.dispatchCommand((CommandSender)this.player, (String)(BedWars.mainCmd + " cmds"));
        }
        World w = Bukkit.getWorld((String)this.getWorldName());
        Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> w.getEntities().stream().filter(e -> e.getType() != EntityType.PLAYER).filter(e -> e.getType() != EntityType.PAINTING).filter(e -> e.getType() != EntityType.ITEM_FRAME).forEach(Entity::remove), 30L);
        w.setAutoSave(false);
        w.setGameRuleValue("doMobSpawning", "false");
        Bukkit.getPluginManager().callEvent((Event)new SetupSessionStartEvent(this));
        this.setStarted(true);
        Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
            for (String team : this.getTeams()) {
                for (String gen : new String[]{"Iron", "Gold", "Emerald"}) {
                    if (this.getConfig().getYml().get("Team." + team + "." + gen) != null) {
                        for (String loc : this.getConfig().getList("Team." + team + ".Iron")) {
                            com.andrei1058.bedwars.commands.Misc.createArmorStand(String.valueOf(ChatColor.GOLD) + gen + " generator added for team: " + String.valueOf(this.getTeamColor(team)) + team, this.getConfig().convertStringToArenaLocation(loc), loc);
                        }
                    }
                    if (this.getConfig().getYml().get("Team." + team + ".Spawn") != null) {
                        com.andrei1058.bedwars.commands.Misc.createArmorStand(String.valueOf(this.getTeamColor(team)) + team + " " + String.valueOf(ChatColor.GOLD) + "SPAWN SET", this.getConfig().getArenaLoc("Team." + team + ".Spawn"), this.getConfig().getString("Team." + team + ".Spawn"));
                    }
                    if (this.getConfig().getYml().get("Team." + team + ".Bed") == null) continue;
                    com.andrei1058.bedwars.commands.Misc.createArmorStand(String.valueOf(this.getTeamColor(team)) + team + " " + String.valueOf(ChatColor.GOLD) + "BED SET", this.getConfig().getArenaLoc("Team." + team + ".Bed"), this.getConfig().getString("Team." + team + ".Bed"));
                }
                if (this.getConfig().getYml().get("Team." + team + ".Shop") != null) {
                    com.andrei1058.bedwars.commands.Misc.createArmorStand(String.valueOf(this.getTeamColor(team)) + team + " " + String.valueOf(ChatColor.GOLD) + "SHOP SET", this.getConfig().getArenaLoc("Team." + team + ".Shop"), null);
                }
                if (this.getConfig().getYml().get("Team." + team + ".Upgrade") != null) {
                    com.andrei1058.bedwars.commands.Misc.createArmorStand(String.valueOf(this.getTeamColor(team)) + team + " " + String.valueOf(ChatColor.GOLD) + "UPGRADE SET", this.getConfig().getArenaLoc("Team." + team + ".Upgrade"), null);
                }
                if (this.getConfig().getYml().get("Team." + team + ".kill-drops-loc") == null) continue;
                com.andrei1058.bedwars.commands.Misc.createArmorStand(String.valueOf(ChatColor.GOLD) + "Kill drops " + team, this.getConfig().getArenaLoc("Team." + team + ".kill-drops-loc"), null);
            }
            for (String type : new String[]{"Emerald", "Diamond"}) {
                if (this.getConfig().getYml().get("generator." + type) == null) continue;
                for (String loc : this.getConfig().getList("generator." + type)) {
                    com.andrei1058.bedwars.commands.Misc.createArmorStand(String.valueOf(ChatColor.GOLD) + type + " SET", this.getConfig().convertStringToArenaLocation(loc), loc);
                }
            }
        }, 90L);
    }

    @Override
    public void close() {
        this.cancel();
    }

    public List<Location> getSkipAutoCreateGen() {
        return new ArrayList<Location>(this.skipAutoCreateGen);
    }

    public void addSkipAutoCreateGen(Location location) {
        this.skipAutoCreateGen.add(location);
    }

    public void setAutoCreatedEmerald(boolean autoCreatedEmerald) {
        this.autoCreatedEmerald = autoCreatedEmerald;
    }

    public boolean isAutoCreatedEmerald() {
        return this.autoCreatedEmerald;
    }

    public void setAutoCreatedDiamond(boolean autoCreatedDiamond) {
        this.autoCreatedDiamond = autoCreatedDiamond;
    }

    public boolean isAutoCreatedDiamond() {
        return this.autoCreatedDiamond;
    }

    public String getPrefix() {
        return String.valueOf(ChatColor.GREEN) + "[" + this.getWorldName() + String.valueOf(ChatColor.GREEN) + "] " + String.valueOf(ChatColor.GOLD);
    }

    public ChatColor getTeamColor(String team) {
        return TeamColor.getChatColor(this.getConfig().getString("Team." + team + ".Color"));
    }

    public void displayAvailableTeams() {
        if (this.getConfig().getYml().get("Team") != null) {
            this.getPlayer().sendMessage(this.getPrefix() + "Available teams: ");
            for (String team : Objects.requireNonNull(this.getConfig().getYml().getConfigurationSection("Team")).getKeys(false)) {
                this.getPlayer().sendMessage(this.getPrefix() + String.valueOf(TeamColor.getChatColor(Objects.requireNonNull(this.getConfig().getYml().getString("Team." + team + ".Color")))) + team);
            }
        }
    }

    public String getNearestTeam() {
        String foundTeam = "";
        ConfigurationSection cs = this.getConfig().getYml().getConfigurationSection("Team");
        if (cs == null) {
            return foundTeam;
        }
        double distance = 100.0;
        for (String team : cs.getKeys(false)) {
            double dis;
            if (this.getConfig().getYml().get("Team." + team + ".Spawn") == null || !((dis = this.getConfig().getArenaLoc("Team." + team + ".Spawn").distance(this.getPlayer().getLocation())) <= (double)this.getConfig().getInt("island-radius")) || !(dis < distance)) continue;
            distance = dis;
            foundTeam = team;
        }
        return foundTeam;
    }

    public String dot() {
        return String.valueOf(ChatColor.BLUE) + " \u25aa " + String.valueOf(ChatColor.GRAY) + "/" + BedWars.mainCmd + " ";
    }

    public List<String> getTeams() {
        if (this.getConfig().getYml().get("Team") == null) {
            return new ArrayList<String>();
        }
        return new ArrayList<String>(this.getConfig().getYml().getConfigurationSection("Team").getKeys(false));
    }
}

