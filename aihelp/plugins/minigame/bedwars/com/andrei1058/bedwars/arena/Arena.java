/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.Sign
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitScheduler
 *  org.bukkit.util.Vector
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.arena;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.NextEvent;
import com.andrei1058.bedwars.api.arena.generator.GeneratorType;
import com.andrei1058.bedwars.api.arena.generator.IGenerator;
import com.andrei1058.bedwars.api.arena.shop.ShopHolo;
import com.andrei1058.bedwars.api.arena.stats.DefaultStatistics;
import com.andrei1058.bedwars.api.arena.stats.GameStatistic;
import com.andrei1058.bedwars.api.arena.stats.GameStatsHolder;
import com.andrei1058.bedwars.api.arena.stats.Incrementable;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.arena.team.ITeamAssigner;
import com.andrei1058.bedwars.api.arena.team.TeamColor;
import com.andrei1058.bedwars.api.entity.Despawnable;
import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import com.andrei1058.bedwars.api.events.gameplay.GameStateChangeEvent;
import com.andrei1058.bedwars.api.events.gameplay.NextEventChangeEvent;
import com.andrei1058.bedwars.api.events.player.PlayerJoinArenaEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent;
import com.andrei1058.bedwars.api.events.player.PlayerReJoinEvent;
import com.andrei1058.bedwars.api.events.server.ArenaDisableEvent;
import com.andrei1058.bedwars.api.events.server.ArenaEnableEvent;
import com.andrei1058.bedwars.api.events.server.ArenaRestartEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.region.Region;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.api.sidebar.ISidebar;
import com.andrei1058.bedwars.api.tasks.PlayingTask;
import com.andrei1058.bedwars.api.tasks.RestartingTask;
import com.andrei1058.bedwars.api.tasks.StartingTask;
import com.andrei1058.bedwars.arena.LastHit;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.OreGenerator;
import com.andrei1058.bedwars.arena.PlayerGoods;
import com.andrei1058.bedwars.arena.ReJoin;
import com.andrei1058.bedwars.arena.stats.GameStatsManager;
import com.andrei1058.bedwars.arena.stats.StatisticsOrdered;
import com.andrei1058.bedwars.arena.tasks.GamePlayingTask;
import com.andrei1058.bedwars.arena.tasks.GameRestartingTask;
import com.andrei1058.bedwars.arena.tasks.GameStartingTask;
import com.andrei1058.bedwars.arena.tasks.ReJoinTask;
import com.andrei1058.bedwars.arena.team.BedWarsTeam;
import com.andrei1058.bedwars.arena.team.TeamAssigner;
import com.andrei1058.bedwars.arena.upgrades.BaseListener;
import com.andrei1058.bedwars.configuration.ArenaConfig;
import com.andrei1058.bedwars.configuration.Sounds;
import com.andrei1058.bedwars.levels.internal.InternalLevel;
import com.andrei1058.bedwars.levels.internal.PerMinuteTask;
import com.andrei1058.bedwars.listeners.blockstatus.BlockStatusListener;
import com.andrei1058.bedwars.listeners.dropshandler.PlayerDrops;
import com.andrei1058.bedwars.money.internal.MoneyPerMinuteTask;
import com.andrei1058.bedwars.shop.ShopCache;
import com.andrei1058.bedwars.sidebar.BwSidebar;
import com.andrei1058.bedwars.sidebar.SidebarService;
import com.andrei1058.bedwars.support.citizens.JoinNPC;
import com.andrei1058.bedwars.support.paper.TeleportManager;
import com.andrei1058.bedwars.support.papi.SupportPAPI;
import com.andrei1058.bedwars.support.vault.WithEconomy;
import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Arena
implements IArena {
    private static final HashMap<String, IArena> arenaByName = new HashMap();
    private static final HashMap<Player, IArena> arenaByPlayer = new HashMap();
    private static final HashMap<String, IArena> arenaByIdentifier = new HashMap();
    private static final LinkedList<IArena> arenas = new LinkedList();
    private static int gamesBeforeRestart = BedWars.config.getInt("bungee-settings.games-before-restart");
    public static HashMap<UUID, Integer> afkCheck = new HashMap();
    public static HashMap<UUID, Integer> magicMilk = new HashMap();
    private List<Player> players = new ArrayList<Player>();
    private List<Player> spectators = new ArrayList<Player>();
    private List<Block> signs = new ArrayList<Block>();
    private GameState status = GameState.restarting;
    private YamlConfiguration yml;
    private ArenaConfig cm;
    private int minPlayers = 2;
    private int maxPlayers = 10;
    private int maxInTeam = 1;
    private int islandRadius = 10;
    public int upgradeDiamondsCount = 0;
    public int upgradeEmeraldsCount = 0;
    public boolean allowSpectate = true;
    private World world;
    private String group = "Default";
    private String arenaName;
    private String worldName;
    private List<ITeam> teams = new ArrayList<ITeam>();
    private LinkedList<Vector> placed = new LinkedList();
    private List<String> nextEvents = new ArrayList<String>();
    private List<Region> regionsList = new ArrayList<Region>();
    private int renderDistance;
    private final List<Player> leaving = new ArrayList<Player>();
    private NextEvent nextEvent = NextEvent.DIAMOND_GENERATOR_TIER_II;
    private int diamondTier = 1;
    private int emeraldTier = 1;
    private ConcurrentHashMap<Player, Integer> respawnSessions = new ConcurrentHashMap();
    private ConcurrentHashMap<Player, Integer> showTime = new ConcurrentHashMap();
    private static final HashMap<Player, Location> playerLocation = new HashMap();
    private final GameStatsManager gameStats = new GameStatsManager(this);
    private StartingTask startingTask = null;
    private PlayingTask playingTask = null;
    private RestartingTask restartingTask = null;
    private List<IGenerator> oreGenerators = new ArrayList<IGenerator>();
    private PerMinuteTask perMinuteTask;
    private MoneyPerMinuteTask moneyperMinuteTask;
    private static final LinkedList<IArena> enableQueue = new LinkedList();
    private Location respawnLocation;
    private Location spectatorLocation;
    private Location waitingLocation;
    private int yKillHeight;
    private Instant startTime;
    private ITeamAssigner teamAssigner = new TeamAssigner();
    private boolean allowMapBreak = false;
    @Nullable
    private ITeam winner;
    private final Map<UUID, Long> fireballCooldowns = new HashMap<UUID, Long>();

    public Arena(String name, Player p) {
        if (!BedWars.autoscale) {
            for (IArena mm : enableQueue) {
                if (!mm.getArenaName().equalsIgnoreCase(name)) continue;
                BedWars.plugin.getLogger().severe("Tried to load arena " + name + " but it is already in the enable queue.");
                if (p != null) {
                    p.sendMessage(String.valueOf(ChatColor.RED) + "Tried to load arena " + name + " but it is already in the enable queue.");
                }
                return;
            }
            if (Arena.getArenaByName(name) != null) {
                BedWars.plugin.getLogger().severe("Tried to load arena " + name + " but it is already enabled.");
                if (p != null) {
                    p.sendMessage(String.valueOf(ChatColor.RED) + "Tried to load arena " + name + " but it is already enabled.");
                }
                return;
            }
        }
        this.arenaName = name;
        this.worldName = BedWars.autoscale ? BedWars.arenaManager.generateGameID() : this.arenaName;
        this.cm = new ArenaConfig((Plugin)BedWars.plugin, name, BedWars.plugin.getDataFolder().getPath() + "/Arenas");
        this.yml = this.cm.getYml();
        if (this.yml.get("Team") == null) {
            if (p != null) {
                p.sendMessage("You didn't set any team for arena: " + name);
            }
            BedWars.plugin.getLogger().severe("You didn't set any team for arena: " + name);
            return;
        }
        if (this.yml.getConfigurationSection("Team").getKeys(false).size() < 2) {
            if (p != null) {
                p.sendMessage("\u00a7cYou must set at least 2 teams on: " + name);
            }
            BedWars.plugin.getLogger().severe("You must set at least 2 teams on: " + name);
            return;
        }
        this.maxInTeam = this.yml.getInt("maxInTeam");
        this.maxPlayers = this.yml.getConfigurationSection("Team").getKeys(false).size() * this.maxInTeam;
        this.minPlayers = this.yml.getInt("minPlayers");
        this.allowSpectate = this.yml.getBoolean("allowSpectate");
        this.islandRadius = this.yml.getInt("island-radius");
        this.allowMapBreak = this.yml.getBoolean("allow-map-break");
        if (BedWars.config.getYml().get("arenaGroups") != null && BedWars.config.getYml().getStringList("arenaGroups").contains(this.yml.getString("group"))) {
            this.group = this.yml.getString("group");
        }
        if (!BedWars.getAPI().getRestoreAdapter().isWorld(name)) {
            if (p != null) {
                p.sendMessage(String.valueOf(ChatColor.RED) + "There isn't any map called " + name);
            }
            BedWars.plugin.getLogger().log(Level.WARNING, "There isn't any map called " + name);
            return;
        }
        boolean error = false;
        for (String team : this.yml.getConfigurationSection("Team").getKeys(false)) {
            String colorS = this.yml.getString("Team." + team + ".Color");
            if (colorS == null) continue;
            colorS = colorS.toUpperCase();
            try {
                TeamColor.valueOf(colorS);
            } catch (Exception e) {
                if (p != null) {
                    p.sendMessage("\u00a7cInvalid color at team: " + team + " in arena: " + name);
                }
                BedWars.plugin.getLogger().severe("Invalid color at team: " + team + " in arena: " + name);
                error = true;
            }
            for (String stuff : Arrays.asList("Color", "Spawn", "Bed", "Shop", "Upgrade", "Iron", "Gold")) {
                if (this.yml.get("Team." + team + "." + stuff) != null) continue;
                if (p != null) {
                    p.sendMessage("\u00a7c" + stuff + " not set for " + team + " team on: " + name);
                }
                BedWars.plugin.getLogger().severe(stuff + " not set for " + team + " team on: " + name);
                error = true;
            }
        }
        if (this.yml.get("generator.Diamond") == null) {
            if (p != null) {
                p.sendMessage("\u00a7cThere isn't set any Diamond generator on: " + name);
            }
            BedWars.plugin.getLogger().severe("There isn't set any Diamond generator on: " + name);
        }
        if (this.yml.get("generator.Emerald") == null) {
            if (p != null) {
                p.sendMessage("\u00a7cThere isn't set any Emerald generator on: " + name);
            }
            BedWars.plugin.getLogger().severe("There isn't set any Emerald generator on: " + name);
        }
        if (this.yml.get("waiting.Loc") == null) {
            if (p != null) {
                p.sendMessage("\u00a7cWaiting spawn not set on: " + name);
            }
            BedWars.plugin.getLogger().severe("Waiting spawn not set on: " + name);
            return;
        }
        if (error) {
            return;
        }
        this.yKillHeight = this.yml.getInt("y-kill-height");
        Arena.addToEnableQueue(this);
        Language.saveIfNotExists(Messages.ARENA_DISPLAY_GROUP_PATH + this.getGroup().toLowerCase(), String.valueOf(this.getGroup().charAt(0)).toUpperCase() + this.group.substring(1).toLowerCase());
    }

    @Override
    public void init(World world) {
        if (!BedWars.autoscale && Arena.getArenaByName(this.arenaName) != null) {
            return;
        }
        Arena.removeFromEnableQueue(this);
        BedWars.debug("Initialized arena " + this.getArenaName() + " with map " + world.getName());
        this.world = world;
        this.worldName = world.getName();
        this.getConfig().setName(this.worldName);
        world.getEntities().stream().filter(e -> e.getType() != EntityType.PLAYER).filter(e -> e.getType() != EntityType.PAINTING).filter(e -> e.getType() != EntityType.ITEM_FRAME).forEach(Entity::remove);
        for (String string : this.getConfig().getList("game-rules")) {
            String[] rule = string.split(":");
            if (rule.length != 2) continue;
            world.setGameRuleValue(rule[0], rule[1]);
        }
        world.setAutoSave(false);
        for (Entity entity : world.getEntities()) {
            if (entity.getType() != EntityType.ARMOR_STAND || ((ArmorStand)entity).isVisible()) continue;
            entity.remove();
        }
        for (String string : this.yml.getConfigurationSection("Team").getKeys(false)) {
            if (this.getTeam(string) != null) {
                BedWars.plugin.getLogger().severe("A team with name: " + string + " was already loaded for arena: " + this.getArenaName());
                continue;
            }
            BedWarsTeam bwt = new BedWarsTeam(string, TeamColor.valueOf(this.yml.getString("Team." + string + ".Color").toUpperCase()), this.cm.getArenaLoc("Team." + string + ".Spawn"), this.cm.getArenaLoc("Team." + string + ".Bed"), this.cm.getArenaLoc("Team." + string + ".Shop"), this.cm.getArenaLoc("Team." + string + ".Upgrade"), this);
            this.teams.add(bwt);
            bwt.spawnGenerators();
        }
        for (String type : Arrays.asList("Diamond", "Emerald")) {
            if (this.yml.get("generator." + type) == null) continue;
            for (String s : this.yml.getStringList("generator." + type)) {
                Location location = this.cm.convertStringToArenaLocation(s);
                if (location == null) {
                    BedWars.plugin.getLogger().severe("Invalid location for " + type + " generator: " + s);
                    continue;
                }
                this.oreGenerators.add(new OreGenerator(location, this, GeneratorType.valueOf(type.toUpperCase()), null));
            }
        }
        arenas.add(this);
        arenaByName.put(this.getArenaName(), this);
        arenaByIdentifier.put(this.worldName, this);
        world.getWorldBorder().setCenter(this.cm.getArenaLoc("waiting.Loc"));
        world.getWorldBorder().setSize((double)this.yml.getInt("worldBorder"));
        if (!this.getConfig().getYml().isSet("waiting.Pos1") && this.getConfig().getYml().isSet("waiting.Pos2")) {
            BedWars.plugin.getLogger().severe("Lobby Pos1 isn't set! The arena's lobby won't be removed!");
        }
        if (this.getConfig().getYml().isSet("waiting.Pos1") && !this.getConfig().getYml().isSet("waiting.Pos2")) {
            BedWars.plugin.getLogger().severe("Lobby Pos2 isn't set! The arena's lobby won't be removed!");
        }
        this.registerSigns();
        Bukkit.getPluginManager().callEvent((Event)new ArenaEnableEvent(this));
        this.respawnLocation = this.cm.getArenaLoc("spectator-loc");
        if (this.respawnLocation == null) {
            this.respawnLocation = this.cm.getArenaLoc("waiting.Loc");
        }
        if (this.respawnLocation == null) {
            this.respawnLocation = world.getSpawnLocation();
        }
        this.spectatorLocation = this.cm.getArenaLoc("spectator-loc");
        if (this.spectatorLocation == null) {
            this.spectatorLocation = this.cm.getArenaLoc("waiting.Loc");
        }
        if (this.spectatorLocation == null) {
            this.spectatorLocation = world.getSpawnLocation();
        }
        this.waitingLocation = this.cm.getArenaLoc("waiting.Loc");
        if (this.waitingLocation == null) {
            this.waitingLocation = world.getSpawnLocation();
        }
        this.changeStatus(GameState.waiting);
        for (NextEvent ne : NextEvent.values()) {
            this.nextEvents.add(ne.toString());
        }
        this.upgradeDiamondsCount = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.getGroup() + ".diamond.tierII.start") == null ? "Default.diamond.tierII.start" : this.getGroup() + ".diamond.tierII.start"));
        this.upgradeEmeraldsCount = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.getGroup() + ".emerald.tierII.start") == null ? "Default.emerald.tierII.start" : this.getGroup() + ".emerald.tierII.start"));
        BedWars.plugin.getLogger().info("Load done: " + this.getArenaName());
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)new File("spigot.yml"));
        this.renderDistance = yamlConfiguration.get("world-settings." + this.getWorldName() + ".entity-tracking-range.players") == null ? yamlConfiguration.getInt("world-settings.default.entity-tracking-range.players") : yamlConfiguration.getInt("world-settings." + this.getWorldName() + ".entity-tracking-range.players");
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean addPlayer(Player p, boolean skipOwnerCheck) {
        if (p == null) {
            return false;
        }
        BedWars.debug("Player added: " + p.getName() + " arena: " + this.getArenaName());
        BaseListener.isOnABase.remove(p);
        if (Arena.getArenaByPlayer(p) != null) {
            return false;
        }
        if (BedWars.getParty().hasParty(p) && !skipOwnerCheck) {
            if (!BedWars.getParty().isOwner(p)) {
                p.sendMessage(Language.getMsg(p, Messages.COMMAND_JOIN_DENIED_NOT_PARTY_LEADER));
                return false;
            }
            int partySize = (int)BedWars.getParty().getMembers(p).stream().filter(member -> {
                IArena arena = Arena.getArenaByPlayer(member);
                if (arena == null) {
                    return true;
                }
                return arena.isSpectator((Player)member);
            }).count();
            if (partySize > this.maxInTeam * this.getTeams().size() - this.getPlayers().size()) {
                p.sendMessage(Language.getMsg(p, Messages.COMMAND_JOIN_DENIED_PARTY_TOO_BIG));
                return false;
            }
            for (Player player : BedWars.getParty().getMembers(p)) {
                if (player == p) continue;
                IArena a = Arena.getArenaByPlayer(player);
                if (a != null && a.isSpectator(player)) {
                    a.removeSpectator(player, false);
                }
                this.addPlayer(player, true);
            }
        }
        this.leaving.remove(p);
        if (this.status == GameState.waiting || this.status == GameState.starting && this.startingTask != null && this.startingTask.getCountdown() > 1) {
            void var5_14;
            if (this.players.size() >= this.maxPlayers && !Arena.isVip(p)) {
                TextComponent text = new TextComponent(Language.getMsg(p, Messages.COMMAND_JOIN_DENIED_IS_FULL));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, BedWars.config.getYml().getString("storeLink")));
                p.spigot().sendMessage((BaseComponent)text);
                return false;
            }
            if (this.players.size() >= this.maxPlayers && Arena.isVip(p)) {
                boolean canJoin = false;
                for (Player player : new ArrayList<Player>(this.players)) {
                    if (Arena.isVip(player)) continue;
                    canJoin = true;
                    this.removePlayer(player, false);
                    TextComponent vipKick = new TextComponent(Language.getMsg(p, Messages.ARENA_JOIN_VIP_KICK));
                    vipKick.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, BedWars.config.getYml().getString("storeLink")));
                    p.spigot().sendMessage((BaseComponent)vipKick);
                    break;
                }
                if (!canJoin) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_JOIN_DENIED_IS_FULL_OF_VIPS));
                    return false;
                }
            }
            PlayerJoinArenaEvent ev = new PlayerJoinArenaEvent(this, p, false);
            Bukkit.getPluginManager().callEvent((Event)ev);
            if (ev.isCancelled()) {
                return false;
            }
            ReJoin rejoin = ReJoin.getPlayer(p);
            if (rejoin != null) {
                rejoin.destroy(true);
            }
            p.closeInventory();
            this.players.add(p);
            p.setFlying(false);
            p.setAllowFlight(false);
            p.setHealth(20.0);
            for (Player on : this.players) {
                on.sendMessage(Language.getMsg(on, Messages.COMMAND_JOIN_PLAYER_JOIN_MSG).replace("{vPrefix}", BedWars.getChatSupport().getPrefix(p)).replace("{vSuffix}", BedWars.getChatSupport().getSuffix(p)).replace("{playername}", p.getName()).replace("{player}", p.getDisplayName()).replace("{on}", String.valueOf(this.getPlayers().size())).replace("{max}", String.valueOf(this.getMaxPlayers())));
            }
            Arena.setArenaByPlayer(p, this);
            boolean bl = false;
            if (this.status == GameState.waiting) {
                int teams = 0;
                int teammates = 0;
                for (Player on : this.getPlayers()) {
                    if (BedWars.getParty().isOwner(on)) {
                        ++teams;
                    }
                    if (!BedWars.getParty().hasParty(on)) continue;
                    ++teammates;
                }
                if (this.minPlayers <= this.players.size() && teams > 0 && this.players.size() != teammates / teams) {
                    this.changeStatus(GameState.starting);
                    boolean bl2 = true;
                } else if (this.players.size() >= this.minPlayers && teams == 0) {
                    this.changeStatus(GameState.starting);
                    boolean bl3 = true;
                }
            }
            if (this.players.size() >= this.getMaxPlayers() / 2 && this.players.size() > this.minPlayers && this.startingTask != null && Bukkit.getScheduler().isCurrentlyRunning(this.startingTask.getTask()) && this.startingTask.getCountdown() > this.getConfig().getInt("countdowns.game-start-half-arena")) {
                this.startingTask.setCountdown(BedWars.config.getInt("countdowns.game-start-half-arena"));
            }
            if (BedWars.getServerType() != ServerType.BUNGEE) {
                new PlayerGoods(p, true);
                playerLocation.put(p, p.getLocation());
            }
            TeleportManager.teleportC((Entity)p, this.getWaitingLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            if (var5_14 == false) {
                SidebarService.getInstance().giveSidebar(p, this, false);
            }
            this.sendPreGameCommandItems(p);
            for (PotionEffect pf : p.getActivePotionEffects()) {
                p.removePotionEffect(pf.getType());
            }
        } else if (this.status == GameState.playing) {
            this.addSpectator(p, false, null);
            return false;
        }
        p.getInventory().setArmorContents(null);
        Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
            if (BedWars.getServerType() == ServerType.BUNGEE) {
                BedWars.nms.sendPlayerSpawnPackets(p, this);
            }
            for (Player on : Bukkit.getOnlinePlayers()) {
                if (on == null || on.equals((Object)p)) continue;
                if (this.isPlayer(on)) {
                    BedWars.nms.spigotShowPlayer(p, on);
                    BedWars.nms.spigotShowPlayer(on, p);
                    continue;
                }
                BedWars.nms.spigotHidePlayer(p, on);
                BedWars.nms.spigotHidePlayer(on, p);
            }
            if (BedWars.getServerType() == ServerType.BUNGEE) {
                BedWars.nms.sendPlayerSpawnPackets(p, this);
            }
        }, 17L);
        if (BedWars.getServerType() == ServerType.BUNGEE) {
            p.getEnderChest().clear();
        }
        if (this.getPlayers().size() >= this.getMaxPlayers() && this.startingTask != null && Bukkit.getScheduler().isCurrentlyRunning(this.startingTask.getTask()) && this.startingTask.getCountdown() > BedWars.config.getInt("countdowns.game-start-shortened")) {
            this.startingTask.setCountdown(BedWars.config.getInt("countdowns.game-start-shortened"));
        }
        this.refreshSigns();
        JoinNPC.updateNPCs(this.getGroup());
        return true;
    }

    @Override
    public boolean addSpectator(@NotNull Player p, boolean playerBefore, Location staffTeleport) {
        if (this.allowSpectate || playerBefore || staffTeleport != null) {
            ReJoin reJoin;
            BedWars.debug("Spectator added: " + p.getName() + " arena: " + this.getArenaName());
            if (!playerBefore) {
                PlayerJoinArenaEvent ev = new PlayerJoinArenaEvent(this, p, true);
                Bukkit.getPluginManager().callEvent((Event)ev);
                if (ev.isCancelled()) {
                    return false;
                }
            }
            if ((reJoin = ReJoin.getPlayer(p)) != null) {
                reJoin.destroy(true);
            }
            this.leaving.remove(p);
            p.closeInventory();
            this.spectators.add(p);
            this.players.remove(p);
            this.updateSpectatorCollideRule(p, false);
            if (!playerBefore) {
                if (BedWars.getServerType() != ServerType.BUNGEE) {
                    new PlayerGoods(p, true);
                    playerLocation.put(p, p.getLocation());
                }
                Arena.setArenaByPlayer(p, this);
            }
            SidebarService.getInstance().giveSidebar(p, this, false);
            BedWars.nms.setCollide(p, this, false);
            if (!playerBefore) {
                if (staffTeleport == null) {
                    TeleportManager.teleportC((Entity)p, this.getSpectatorLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                } else {
                    TeleportManager.teleportC((Entity)p, staffTeleport, PlayerTeleportEvent.TeleportCause.PLUGIN);
                }
            }
            p.setGameMode(GameMode.ADVENTURE);
            Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
                if (this.leaving.contains(p)) {
                    return;
                }
                p.setAllowFlight(true);
                p.setFlying(true);
            }, 5L);
            if (p.getPassenger() != null && p.getPassenger().getType() == EntityType.ARMOR_STAND) {
                p.getPassenger().remove();
            }
            Bukkit.getScheduler().runTask((Plugin)BedWars.plugin, () -> {
                if (this.leaving.contains(p)) {
                    return;
                }
                for (Player on : Bukkit.getOnlinePlayers()) {
                    if (on == p) continue;
                    if (this.getSpectators().contains(on)) {
                        BedWars.nms.spigotShowPlayer(p, on);
                        BedWars.nms.spigotShowPlayer(on, p);
                        continue;
                    }
                    if (this.getPlayers().contains(on)) {
                        BedWars.nms.spigotHidePlayer(p, on);
                        BedWars.nms.spigotShowPlayer(on, p);
                        continue;
                    }
                    BedWars.nms.spigotHidePlayer(p, on);
                    BedWars.nms.spigotHidePlayer(on, p);
                }
                if (!playerBefore) {
                    if (staffTeleport == null) {
                        TeleportManager.teleportC((Entity)p, this.getSpectatorLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                    } else {
                        TeleportManager.teleport((Entity)p, staffTeleport);
                    }
                } else {
                    TeleportManager.teleport((Entity)p, this.getSpectatorLocation());
                }
                p.setAllowFlight(true);
                p.setFlying(true);
                this.sendSpectatorCommandItems(p);
                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false));
                p.getInventory().setArmorContents(null);
            });
            this.leaving.remove(p);
            p.sendMessage(Language.getMsg(p, Messages.COMMAND_JOIN_SPECTATOR_MSG).replace("{arena}", this.getDisplayName()));
            String iso = Language.getPlayerLanguage(p).getIso();
            for (IGenerator o : this.getOreGenerators()) {
                o.updateHolograms(p, iso);
            }
            for (ITeam t : this.getTeams()) {
                for (IGenerator o : t.getGenerators()) {
                    o.updateHolograms(p, iso);
                }
            }
            for (ShopHolo sh : ShopHolo.getShopHolo()) {
                if (sh.getA() != this) continue;
                sh.updateForPlayer(p, iso);
            }
        } else {
            p.sendMessage(Language.getMsg(p, Messages.COMMAND_JOIN_SPECTATOR_DENIED_MSG));
            return false;
        }
        this.showTime.remove(p);
        this.refreshSigns();
        JoinNPC.updateNPCs(this.getGroup());
        return true;
    }

    @Override
    public void removePlayer(@NotNull Player p, boolean disconnect) {
        int n;
        Player lastDamager;
        LastHit lastHit;
        if (this.leaving.contains(p)) {
            return;
        }
        this.leaving.add(p);
        BedWars.debug("Player removed: " + p.getName() + " arena: " + this.getArenaName());
        this.respawnSessions.remove(p);
        ITeam team = null;
        afkCheck.remove(p.getUniqueId());
        BedWars.getAPI().getAFKUtil().setPlayerAFK(p, false);
        if (this.status == GameState.playing) {
            for (ITeam t : this.getTeams()) {
                if (!t.isMember(p)) continue;
                team = t;
                t.getMembers().remove(p);
                t.destroyBedHolo(p);
            }
        }
        ArrayList<ShopCache.CachedItem> cacheList = new ArrayList();
        if (ShopCache.getShopCache(p.getUniqueId()) != null) {
            cacheList = ShopCache.getShopCache(p.getUniqueId()).getCachedPermanents();
        }
        Player player2 = (lastHit = LastHit.getLastHit(p)) == null ? null : (lastDamager = lastHit.getDamager() instanceof Player ? (Player)lastHit.getDamager() : null);
        if (lastHit != null && lastHit.getTime() < System.currentTimeMillis() - 13000L) {
            lastDamager = null;
        }
        Bukkit.getPluginManager().callEvent((Event)new PlayerLeaveArenaEvent(p, this, lastDamager));
        this.players.remove(p);
        Arena.removeArenaByPlayer(p, this);
        for (Object pf : p.getActivePotionEffects()) {
            p.removePotionEffect(pf.getType());
        }
        if (p.getPassenger() != null && p.getPassenger().getType() == EntityType.ARMOR_STAND) {
            p.getPassenger().remove();
        }
        boolean teamuri = false;
        for (Player player3 : this.getPlayers()) {
            if (!BedWars.getParty().hasParty(player3)) continue;
            teamuri = true;
        }
        if (this.status == GameState.starting && (this.maxInTeam > this.players.size() && teamuri || this.players.size() < this.minPlayers && !teamuri)) {
            this.changeStatus(GameState.waiting);
            for (Player player4 : this.players) {
                player4.sendMessage(Language.getMsg(player4, Messages.ARENA_START_COUNTDOWN_STOPPED_INSUFF_PLAYERS_CHAT));
            }
        } else if (this.status == GameState.playing) {
            BedWars.debug("removePlayer debug1");
            int alive_teams = 0;
            for (ITeam t : this.getTeams()) {
                if (t == null || t.getMembers().isEmpty()) continue;
                ++alive_teams;
            }
            if (alive_teams == 1 && !BedWars.isShuttingDown()) {
                this.checkWinner();
                Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> this.changeStatus(GameState.restarting), 10L);
                if (team != null && !team.isBedDestroyed()) {
                    for (Player p2 : this.getPlayers()) {
                        p2.sendMessage(Language.getMsg(p2, Messages.TEAM_ELIMINATED_CHAT).replace("{TeamColor}", team.getColor().chat().toString()).replace("{TeamName}", team.getDisplayName(Language.getPlayerLanguage(p2))));
                    }
                    for (Player p2 : this.getSpectators()) {
                        p2.sendMessage(Language.getMsg(p2, Messages.TEAM_ELIMINATED_CHAT).replace("{TeamColor}", team.getColor().chat().toString()).replace("{TeamName}", team.getDisplayName(Language.getPlayerLanguage(p2))));
                    }
                }
            } else if (alive_teams == 0 && !BedWars.isShuttingDown()) {
                Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> this.changeStatus(GameState.restarting), 10L);
            } else if (!BedWars.isShuttingDown()) {
                new ReJoin(p, this, team, cacheList);
            }
            if (team != null) {
                ITeam iTeam = this.getTeam(lastDamager);
                if (lastDamager != null && this.isPlayer(lastDamager) && iTeam != null) {
                    Language lang;
                    String message;
                    PlayerKillEvent.PlayerKillCause cause;
                    if (team.isBedDestroyed()) {
                        cause = PlayerKillEvent.PlayerKillCause.PLAYER_DISCONNECT_FINAL;
                        message = Messages.PLAYER_DIE_PVP_LOG_OUT_FINAL;
                    } else {
                        message = Messages.PLAYER_DIE_PVP_LOG_OUT_REGULAR;
                        cause = PlayerKillEvent.PlayerKillCause.PLAYER_DISCONNECT;
                    }
                    PlayerKillEvent event = new PlayerKillEvent(this, p, team, lastDamager, iTeam, player -> Language.getMsg(player, message), cause);
                    Bukkit.getPluginManager().callEvent((Event)event);
                    if (null != event.getMessage()) {
                        for (Player inGame : this.getPlayers()) {
                            lang = Language.getPlayerLanguage(inGame);
                            inGame.sendMessage(event.getMessage().apply(inGame).replace("{PlayerTeamName}", team.getDisplayName(lang)).replace("{PlayerColor}", team.getColor().chat().toString()).replace("{PlayerName}", p.getDisplayName()).replace("{KillerColor}", iTeam.getColor().chat().toString()).replace("{KillerName}", lastDamager.getDisplayName()).replace("{KillerTeamName}", iTeam.getDisplayName(lang)));
                        }
                    }
                    if (null != event.getMessage()) {
                        for (Player inGame : this.getSpectators()) {
                            lang = Language.getPlayerLanguage(inGame);
                            inGame.sendMessage(event.getMessage().apply(inGame).replace("{PlayerTeamName}", team.getDisplayName(lang)).replace("{PlayerColor}", team.getColor().chat().toString()).replace("{PlayerName}", p.getDisplayName()).replace("{KillerColor}", iTeam.getColor().chat().toString()).replace("{KillerName}", lastDamager.getDisplayName()).replace("{KillerTeamName}", iTeam.getDisplayName(lang)));
                        }
                    }
                    PlayerDrops.handlePlayerDrops(this, p, lastDamager, team, iTeam, cause, new ArrayList<ItemStack>(Arrays.asList(p.getInventory().getContents())));
                }
            }
        }
        for (Player player5 : this.getPlayers()) {
            player5.sendMessage(Language.getMsg(player5, Messages.COMMAND_LEAVE_MSG).replace("{vPrefix}", BedWars.getChatSupport().getPrefix(p)).replace("{vSuffix}", BedWars.getChatSupport().getSuffix(p)).replace("{playername}", p.getName()).replace("{player}", p.getDisplayName()));
        }
        for (Player player6 : this.getSpectators()) {
            player6.sendMessage(Language.getMsg(player6, Messages.COMMAND_LEAVE_MSG).replace("{vPrefix}", BedWars.getChatSupport().getPrefix(p)).replace("{playername}", p.getName()).replace("{player}", p.getDisplayName()));
        }
        if (BedWars.getServerType() == ServerType.SHARED) {
            SidebarService.getInstance().remove(p);
            this.sendToMainLobby(p);
        } else {
            if (BedWars.getServerType() == ServerType.BUNGEE) {
                Misc.moveToLobbyOrKick(p, this, true);
                return;
            }
            this.sendToMainLobby(p);
        }
        PlayerGoods pg = PlayerGoods.getPlayerGoods(p);
        if (pg == null) {
            if (BedWars.getServerType() == ServerType.MULTIARENA) {
                Arena.sendLobbyCommandItems(p);
            }
        } else {
            pg.restore();
        }
        playerLocation.remove(p);
        for (PotionEffect pf : p.getActivePotionEffects()) {
            p.removePotionEffect(pf.getType());
        }
        if (!BedWars.isShuttingDown()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)BedWars.plugin, () -> {
                for (Player on : Bukkit.getOnlinePlayers()) {
                    if (on.equals((Object)p)) continue;
                    if (Arena.getArenaByPlayer(on) == null) {
                        BedWars.nms.spigotShowPlayer(p, on);
                        BedWars.nms.spigotShowPlayer(on, p);
                        continue;
                    }
                    BedWars.nms.spigotHidePlayer(p, on);
                    BedWars.nms.spigotHidePlayer(on, p);
                }
                if (!disconnect) {
                    SidebarService.getInstance().giveSidebar(p, null, false);
                }
            }, 5L);
        }
        if (BedWars.getParty().hasParty(p) && BedWars.getParty().isOwner(p) && this.status != GameState.restarting) {
            if (BedWars.getParty().isInternal()) {
                for (Player mem : new ArrayList<Player>(BedWars.getParty().getMembers(p))) {
                    mem.sendMessage(Language.getMsg(mem, Messages.ARENA_LEAVE_PARTY_DISBANDED));
                }
            }
            BedWars.getParty().disband(p);
            teamuri = false;
            for (Player on : this.getPlayers()) {
                if (!BedWars.getParty().hasParty(on)) continue;
                teamuri = true;
            }
            if (this.status == GameState.starting && (this.maxInTeam > this.players.size() && teamuri || this.players.size() < this.minPlayers && !teamuri)) {
                this.changeStatus(GameState.waiting);
                for (Player on : this.players) {
                    on.sendMessage(Language.getMsg(on, Messages.ARENA_START_COUNTDOWN_STOPPED_INSUFF_PLAYERS_CHAT));
                }
            }
        }
        p.setFlying(false);
        p.setAllowFlight(false);
        if (this.status == GameState.restarting && ReJoin.exists(p) && ReJoin.getPlayer(p).getArena() == this) {
            ReJoin.getPlayer(p).destroy(false);
        }
        if (magicMilk.containsKey(p.getUniqueId()) && (n = magicMilk.remove(p.getUniqueId()).intValue()) > 0) {
            Bukkit.getScheduler().cancelTask(n);
        }
        this.showTime.remove(p);
        this.refreshSigns();
        JoinNPC.updateNPCs(this.getGroup());
        if ((this.status == GameState.waiting || this.status == GameState.starting) && BedWars.getParty().hasParty(p) && !BedWars.getParty().isOwner(p)) {
            for (Player pl : BedWars.getParty().getMembers(p)) {
                if (!BedWars.getParty().isOwner(pl) || !pl.getWorld().getName().equalsIgnoreCase(this.getArenaName())) continue;
                BedWars.getParty().removeFromParty(p);
                break;
            }
        }
        if (lastHit != null) {
            lastHit.remove();
        }
    }

    @Override
    public void removeSpectator(@NotNull Player p, boolean disconnect) {
        int taskId;
        BedWars.debug("Spectator removed: " + p.getName() + " arena: " + this.getArenaName());
        if (this.leaving.contains(p)) {
            return;
        }
        this.leaving.add(p);
        Bukkit.getPluginManager().callEvent((Event)new PlayerLeaveArenaEvent(p, this, null));
        this.spectators.remove(p);
        Arena.removeArenaByPlayer(p, this);
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        BedWars.nms.setCollide(p, this, true);
        afkCheck.remove(p.getUniqueId());
        BedWars.getAPI().getAFKUtil().setPlayerAFK(p, false);
        if (BedWars.getServerType() == ServerType.SHARED) {
            SidebarService.getInstance().remove(p);
            this.sendToMainLobby(p);
        } else if (BedWars.getServerType() == ServerType.MULTIARENA) {
            this.sendToMainLobby(p);
        }
        for (Object pf : p.getActivePotionEffects()) {
            p.removePotionEffect(pf.getType());
        }
        PlayerGoods pg = PlayerGoods.getPlayerGoods(p);
        if (pg == null) {
            if (BedWars.getServerType() == ServerType.MULTIARENA) {
                Arena.sendLobbyCommandItems(p);
            }
        } else {
            pg.restore();
        }
        if (BedWars.getServerType() == ServerType.BUNGEE) {
            Misc.moveToLobbyOrKick(p, this, true);
            return;
        }
        playerLocation.remove(p);
        if (!BedWars.isShuttingDown()) {
            Bukkit.getScheduler().runTask((Plugin)BedWars.plugin, () -> {
                for (Player on : Bukkit.getOnlinePlayers()) {
                    if (on.equals((Object)p)) continue;
                    if (Arena.getArenaByPlayer(on) == null) {
                        BedWars.nms.spigotShowPlayer(p, on);
                        BedWars.nms.spigotShowPlayer(on, p);
                        continue;
                    }
                    BedWars.nms.spigotHidePlayer(p, on);
                    BedWars.nms.spigotHidePlayer(on, p);
                }
                if (!disconnect) {
                    SidebarService.getInstance().giveSidebar(p, null, false);
                }
            });
        }
        if (BedWars.getParty().hasParty(p) && BedWars.getParty().isOwner(p) && this.status != GameState.restarting) {
            if (BedWars.getParty().isInternal()) {
                for (Player mem : new ArrayList<Player>(BedWars.getParty().getMembers(p))) {
                    mem.sendMessage(Language.getMsg(mem, Messages.ARENA_LEAVE_PARTY_DISBANDED));
                }
            }
            BedWars.getParty().disband(p);
        }
        p.setFlying(false);
        p.setAllowFlight(false);
        if (ReJoin.exists(p) && ReJoin.getPlayer(p).getArena() == this) {
            ReJoin.getPlayer(p).destroy(false);
        }
        if (magicMilk.containsKey(p.getUniqueId()) && (taskId = magicMilk.get(p.getUniqueId()).intValue()) > 0) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
        this.refreshSigns();
        JoinNPC.updateNPCs(this.getGroup());
    }

    @Override
    public boolean reJoin(Player p) {
        ReJoin reJoin = ReJoin.getPlayer(p);
        if (reJoin == null) {
            return false;
        }
        if (reJoin.getArena() != this) {
            return false;
        }
        if (!reJoin.canReJoin()) {
            return false;
        }
        if (reJoin.getTask() != null) {
            reJoin.getTask().destroy();
        }
        PlayerReJoinEvent ev = new PlayerReJoinEvent(p, this, BedWars.config.getInt("countdowns.player-re-spawn"));
        Bukkit.getPluginManager().callEvent((Event)ev);
        if (ev.isCancelled()) {
            return false;
        }
        for (Player on : Bukkit.getOnlinePlayers()) {
            if (on.equals((Object)p) || Arena.isInArena(on)) continue;
            BedWars.nms.spigotHidePlayer(on, p);
            BedWars.nms.spigotHidePlayer(p, on);
        }
        p.closeInventory();
        this.players.add(p);
        for (Player on : this.players) {
            on.sendMessage(Language.getMsg(on, Messages.COMMAND_REJOIN_PLAYER_RECONNECTED).replace("{playername}", p.getName()).replace("{player}", p.getDisplayName()).replace("{on}", String.valueOf(this.getPlayers().size())).replace("{max}", String.valueOf(this.getMaxPlayers())));
        }
        for (Player on : this.spectators) {
            on.sendMessage(Language.getMsg(on, Messages.COMMAND_REJOIN_PLAYER_RECONNECTED).replace("{playername}", p.getName()).replace("{player}", p.getDisplayName()).replace("{on}", String.valueOf(this.getPlayers().size())).replace("{max}", String.valueOf(this.getMaxPlayers())));
        }
        Arena.setArenaByPlayer(p, this);
        if (BedWars.getServerType() != ServerType.BUNGEE) {
            playerLocation.put(p, p.getLocation());
        }
        TeleportManager.teleportC((Entity)p, this.getSpectatorLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        p.getInventory().clear();
        ShopCache sc = ShopCache.getShopCache(p.getUniqueId());
        if (sc != null) {
            sc.destroy();
        }
        sc = new ShopCache(p.getUniqueId());
        for (ShopCache.CachedItem ci : reJoin.getPermanentsAndNonDowngradables()) {
            sc.getCachedItems().add(ci);
        }
        reJoin.getBwt().reJoin(p, ev.getRespawnTime());
        reJoin.destroy(false);
        SidebarService.getInstance().giveSidebar(p, this, true);
        return true;
    }

    @Override
    public void disable() {
        for (Player p : new ArrayList<Player>(this.players)) {
            this.removePlayer(p, false);
        }
        for (Player p : new ArrayList<Player>(this.spectators)) {
            this.removeSpectator(p, false);
        }
        if (this.getRestartingTask() != null) {
            this.getRestartingTask().cancel();
        }
        if (this.getStartingTask() != null) {
            this.getStartingTask().cancel();
        }
        if (this.getPlayingTask() != null) {
            this.getPlayingTask().cancel();
        }
        BedWars.plugin.getLogger().log(Level.WARNING, "Disabling arena: " + this.getArenaName());
        for (Player inWorld : this.getWorld().getPlayers()) {
            inWorld.kickPlayer("You're not supposed to be here.");
        }
        BedWars.getAPI().getRestoreAdapter().onDisable(this);
        Bukkit.getPluginManager().callEvent((Event)new ArenaDisableEvent(this.getArenaName(), this.getWorldName()));
        this.destroyData();
    }

    @Override
    public void restart() {
        if (this.getRestartingTask() != null) {
            this.getRestartingTask().cancel();
        }
        if (this.getStartingTask() != null) {
            this.getStartingTask().cancel();
        }
        if (this.getPlayingTask() != null) {
            this.getPlayingTask().cancel();
        }
        if (null != this.moneyperMinuteTask) {
            this.moneyperMinuteTask.cancel();
        }
        if (null != this.perMinuteTask) {
            this.perMinuteTask.cancel();
        }
        BedWars.plugin.getLogger().log(Level.FINE, "Restarting arena: " + this.getArenaName());
        Bukkit.getPluginManager().callEvent((Event)new ArenaRestartEvent(this.getArenaName(), this.getWorldName()));
        for (Player inWorld : this.getWorld().getPlayers()) {
            inWorld.kickPlayer("You're not supposed to be here.");
        }
        BedWars.getAPI().getRestoreAdapter().onRestart(this);
        this.destroyData();
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public int getMaxInTeam() {
        return this.maxInTeam;
    }

    public static IArena getArenaByName(String arenaName) {
        return arenaByName.get(arenaName);
    }

    public static IArena getArenaByIdentifier(String worldName) {
        return arenaByIdentifier.get(worldName);
    }

    public static IArena getArenaByPlayer(Player p) {
        return arenaByPlayer.get(p);
    }

    public static LinkedList<IArena> getArenas() {
        return arenas;
    }

    @Override
    public String getDisplayStatus(Language lang) {
        String s = "";
        switch (this.status) {
            case waiting: {
                s = lang.m(Messages.ARENA_STATUS_WAITING_NAME);
                break;
            }
            case starting: {
                s = lang.m(Messages.ARENA_STATUS_STARTING_NAME);
                break;
            }
            case restarting: {
                s = lang.m(Messages.ARENA_STATUS_RESTARTING_NAME);
                break;
            }
            case playing: {
                s = lang.m(Messages.ARENA_STATUS_PLAYING_NAME);
            }
        }
        return s.replace("{full}", this.getPlayers().size() == this.getMaxPlayers() ? lang.m(Messages.MEANING_FULL) : "");
    }

    @Override
    public String getDisplayGroup(Player player) {
        return Language.getPlayerLanguage(player).m(Messages.ARENA_DISPLAY_GROUP_PATH + this.getGroup().toLowerCase());
    }

    @Override
    public String getDisplayGroup(@NotNull Language language) {
        return language.m(Messages.ARENA_DISPLAY_GROUP_PATH + this.getGroup().toLowerCase());
    }

    @Override
    public List<Player> getPlayers() {
        return this.players;
    }

    @Override
    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    @Override
    public String getDisplayName() {
        return this.getConfig().getYml().getString("display-name", (Character.toUpperCase(this.arenaName.charAt(0)) + this.arenaName.substring(1)).replace("_", " ").replace("-", " ")).trim().isEmpty() ? (Character.toUpperCase(this.arenaName.charAt(0)) + this.arenaName.substring(1)).replace("_", " ").replace("-", " ") : this.getConfig().getString("display-name");
    }

    @Override
    public void setWorldName(String name) {
        this.worldName = name;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public String getArenaName() {
        return this.arenaName;
    }

    @Override
    public List<ITeam> getTeams() {
        return this.teams;
    }

    @Override
    public ArenaConfig getConfig() {
        return this.cm;
    }

    @Override
    public void addPlacedBlock(Block block) {
        if (block == null) {
            return;
        }
        this.placed.add(new Vector(block.getX(), block.getY(), block.getZ()));
    }

    @Override
    public void removePlacedBlock(Block block) {
        if (block == null) {
            return;
        }
        if (!this.isBlockPlaced(block)) {
            return;
        }
        this.placed.remove(new Vector(block.getX(), block.getY(), block.getZ()));
    }

    @Override
    public boolean isBlockPlaced(Block block) {
        for (Vector v : this.getPlaced()) {
            if (v.getX() != (double)block.getX() || v.getY() != (double)block.getY() || v.getZ() != (double)block.getZ()) continue;
            return true;
        }
        return false;
    }

    @Override
    @Deprecated(forRemoval=true)
    public int getPlayerKills(Player player, boolean finalKills) {
        if (null == player || null == this.getStatsHolder()) {
            return 0;
        }
        Optional st = this.getStatsHolder().get(player).flatMap(stats -> stats.getStatistic(finalKills ? DefaultStatistics.KILLS_FINAL : DefaultStatistics.BEDS_DESTROYED));
        if (st.isEmpty()) {
            return 0;
        }
        GameStatistic gs = (GameStatistic)st.get();
        return gs instanceof Incrementable ? (Integer)gs.getValue() : 0;
    }

    @Override
    @Deprecated(forRemoval=true)
    public int getPlayerBedsDestroyed(Player player) {
        if (null == player || null == this.getStatsHolder()) {
            return 0;
        }
        Optional st = this.getStatsHolder().get(player).flatMap(stats -> stats.getStatistic(DefaultStatistics.BEDS_DESTROYED));
        if (st.isEmpty()) {
            return 0;
        }
        GameStatistic gs = (GameStatistic)st.get();
        return gs instanceof Incrementable ? (Integer)gs.getValue() : 0;
    }

    @Override
    public List<Block> getSigns() {
        return this.signs;
    }

    @Override
    public int getIslandRadius() {
        return this.islandRadius;
    }

    @Override
    public void setGroup(String group) {
        this.group = group;
    }

    public static void setArenaByPlayer(Player p, IArena arena) {
        arenaByPlayer.put(p, arena);
        arena.refreshSigns();
        JoinNPC.updateNPCs(arena.getGroup());
    }

    public static void setArenaByName(IArena arena) {
        arenaByName.put(arena.getArenaName(), arena);
    }

    public static void removeArenaByName(@NotNull String arena) {
        arenaByName.remove(arena.replace("_clone", ""));
    }

    public static void removeArenaByPlayer(Player p, @NotNull IArena arena) {
        arenaByPlayer.remove(p);
        arena.refreshSigns();
        JoinNPC.updateNPCs(arena.getGroup());
    }

    @Override
    public void setStatus(GameState status) {
        if (this.status != GameState.playing && status == GameState.playing) {
            this.startTime = Instant.now();
        }
        if (this.status == GameState.starting && status == GameState.waiting) {
            for (Player player : this.getPlayers()) {
                Language playerLang = Language.getPlayerLanguage(player);
                BedWars.nms.sendTitle(player, playerLang.m(Messages.ARENA_STATUS_START_COUNTDOWN_CANCELLED_TITLE), playerLang.m(Messages.ARENA_STATUS_START_COUNTDOWN_CANCELLED_SUB_TITLE), 0, 40, 10);
            }
        }
        this.status = status;
    }

    @Override
    public void changeStatus(GameState status) {
        if (status == this.status) {
            return;
        }
        if (this.status != GameState.playing && status == GameState.playing) {
            this.startTime = Instant.now();
        }
        this.status = status;
        Bukkit.getPluginManager().callEvent((Event)new GameStateChangeEvent(this, status, status));
        this.refreshSigns();
        if (status == GameState.playing) {
            for (Player p : this.players) {
                afkCheck.remove(p.getUniqueId());
                BedWars.getAPI().getAFKUtil().setPlayerAFK(p, false);
            }
            for (Player p : this.spectators) {
                afkCheck.remove(p.getUniqueId());
                BedWars.getAPI().getAFKUtil().setPlayerAFK(p, false);
            }
            this.getPlayers().forEach(this.gameStats::init);
        }
        BukkitScheduler bs = Bukkit.getScheduler();
        if (this.startingTask != null && (bs.isCurrentlyRunning(this.startingTask.getTask()) || bs.isQueued(this.startingTask.getTask()))) {
            this.startingTask.cancel();
        }
        this.startingTask = null;
        if (this.playingTask != null && (bs.isCurrentlyRunning(this.playingTask.getTask()) || bs.isQueued(this.playingTask.getTask()))) {
            this.playingTask.cancel();
        }
        this.playingTask = null;
        if (this.restartingTask != null && (bs.isCurrentlyRunning(this.restartingTask.getTask()) || bs.isQueued(this.restartingTask.getTask()))) {
            this.restartingTask.cancel();
        }
        this.restartingTask = null;
        if (null != this.moneyperMinuteTask) {
            this.moneyperMinuteTask.cancel();
        }
        if (null != this.perMinuteTask) {
            this.perMinuteTask.cancel();
        }
        this.players.forEach(c -> SidebarService.getInstance().giveSidebar((Player)c, this, false));
        this.spectators.forEach(c -> SidebarService.getInstance().giveSidebar((Player)c, this, false));
        if (status == GameState.starting) {
            this.startingTask = new GameStartingTask(this);
        } else if (status == GameState.playing) {
            if (BedWars.getLevelSupport() instanceof InternalLevel) {
                this.perMinuteTask = new PerMinuteTask(this);
            }
            if (BedWars.getEconomy() instanceof WithEconomy) {
                this.moneyperMinuteTask = new MoneyPerMinuteTask(this);
            }
            this.playingTask = new GamePlayingTask(this);
        } else if (status == GameState.restarting) {
            this.restartingTask = new GameRestartingTask(this);
        }
    }

    public static boolean isVip(Player p) {
        return p.hasPermission(BedWars.mainCmd + ".*") || p.hasPermission(BedWars.mainCmd + ".vip");
    }

    @Override
    public boolean isPlayer(Player p) {
        return this.players.contains(p);
    }

    @Override
    public boolean isSpectator(Player p) {
        return this.spectators.contains(p);
    }

    @Override
    public boolean isSpectator(UUID player) {
        for (Player p : this.getSpectators()) {
            if (!p.getUniqueId().equals(player)) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isReSpawning(UUID player) {
        if (player == null) {
            return false;
        }
        for (Player reSpawnSession : this.respawnSessions.keySet()) {
            if (!reSpawnSession.getUniqueId().equals(player)) continue;
            return true;
        }
        return false;
    }

    @Override
    public void addSign(Location loc) {
        if (loc == null) {
            return;
        }
        if (loc.getBlock().getType().toString().endsWith("_SIGN") || loc.getBlock().getType().toString().endsWith("_WALL_SIGN")) {
            this.signs.add(loc.getBlock());
            this.refreshSigns();
            BlockStatusListener.updateBlock(this);
        }
    }

    @Override
    public GameState getStatus() {
        return this.status;
    }

    @Override
    public synchronized void refreshSigns() {
        for (Block b : this.getSigns()) {
            if (b == null || !b.getType().toString().endsWith("_SIGN") && !b.getType().toString().endsWith("_WALL_SIGN") || !(b.getState() instanceof Sign)) continue;
            Sign s = (Sign)b.getState();
            if (s == null) {
                return;
            }
            int line = 0;
            for (String string : BedWars.signs.getList("format")) {
                if (string == null || this.getPlayers() == null) continue;
                s.setLine(line, string.replace("[on]", String.valueOf(this.getPlayers().size())).replace("[max]", String.valueOf(this.getMaxPlayers())).replace("[arena]", this.getDisplayName()).replace("[status]", this.getDisplayStatus(Language.getDefaultLanguage())).replace("[type]", String.valueOf(this.getMaxInTeam())));
                ++line;
            }
            try {
                s.update(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public List<Player> getSpectators() {
        return this.spectators;
    }

    @Override
    @Deprecated(forRemoval=true)
    public void addPlayerKill(Player player, boolean finalKill, Player victim) {
    }

    @Override
    @Deprecated(forRemoval=true)
    public void addPlayerBedDestroyed(Player player) {
    }

    public static void sendLobbyCommandItems(Player p) {
        if (BedWars.config.getYml().get("lobby-items") == null) {
            return;
        }
        if (!BedWars.config.getLobbyWorldName().equalsIgnoreCase(p.getWorld().getName())) {
            return;
        }
        p.getInventory().clear();
        Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin)BedWars.plugin, () -> {
            if (!BedWars.config.getLobbyWorldName().equalsIgnoreCase(p.getWorld().getName())) {
                return;
            }
            for (String item : BedWars.config.getYml().getConfigurationSection("lobby-items").getKeys(false)) {
                if (BedWars.config.getYml().get("lobby-items.%path%.material".replace("%path%", item)) == null) {
                    BedWars.plugin.getLogger().severe("lobby-items.%path%.material".replace("%path%", item) + " is not set!");
                    continue;
                }
                if (BedWars.config.getYml().get("lobby-items.%path%.data".replace("%path%", item)) == null) {
                    BedWars.plugin.getLogger().severe("lobby-items.%path%.data".replace("%path%", item) + " is not set!");
                    continue;
                }
                if (BedWars.config.getYml().get("lobby-items.%path%.slot".replace("%path%", item)) == null) {
                    BedWars.plugin.getLogger().severe("lobby-items.%path%.slot".replace("%path%", item) + " is not set!");
                    continue;
                }
                if (BedWars.config.getYml().get("lobby-items.%path%.enchanted".replace("%path%", item)) == null) {
                    BedWars.plugin.getLogger().severe("lobby-items.%path%.enchanted".replace("%path%", item) + " is not set!");
                    continue;
                }
                if (BedWars.config.getYml().get("lobby-items.%path%.command".replace("%path%", item)) == null) {
                    BedWars.plugin.getLogger().severe("lobby-items.%path%.command".replace("%path%", item) + " is not set!");
                    continue;
                }
                ItemStack i = Misc.createItem(Material.valueOf((String)BedWars.config.getYml().getString("lobby-items.%path%.material".replace("%path%", item))), (byte)BedWars.config.getInt("lobby-items.%path%.data".replace("%path%", item)), BedWars.config.getBoolean("lobby-items.%path%.enchanted".replace("%path%", item)), SupportPAPI.getSupportPAPI().replace(p, Language.getMsg(p, "lobby-items-%path%-name".replace("%path%", item))), SupportPAPI.getSupportPAPI().replace(p, Language.getList(p, "lobby-items-%path%-lore".replace("%path%", item))), p, "RUNCOMMAND", BedWars.config.getYml().getString("lobby-items.%path%.command".replace("%path%", item)));
                p.getInventory().setItem(BedWars.config.getInt("lobby-items.%path%.slot".replace("%path%", item)), i);
            }
        }, 15L);
    }

    @Override
    public void sendPreGameCommandItems(Player p) {
        if (BedWars.config.getYml().get("pre-game-items") == null) {
            return;
        }
        p.getInventory().clear();
        for (String item : BedWars.config.getYml().getConfigurationSection("pre-game-items").getKeys(false)) {
            if (BedWars.config.getYml().get("pre-game-items.%path%.material".replace("%path%", item)) == null) {
                BedWars.plugin.getLogger().severe("pre-game-items.%path%.material".replace("%path%", item) + " is not set!");
                continue;
            }
            if (BedWars.config.getYml().get("pre-game-items.%path%.data".replace("%path%", item)) == null) {
                BedWars.plugin.getLogger().severe("pre-game-items.%path%.data".replace("%path%", item) + " is not set!");
                continue;
            }
            if (BedWars.config.getYml().get("pre-game-items.%path%.slot".replace("%path%", item)) == null) {
                BedWars.plugin.getLogger().severe("pre-game-items.%path%.slot".replace("%path%", item) + " is not set!");
                continue;
            }
            if (BedWars.config.getYml().get("pre-game-items.%path%.enchanted".replace("%path%", item)) == null) {
                BedWars.plugin.getLogger().severe("pre-game-items.%path%.enchanted".replace("%path%", item) + " is not set!");
                continue;
            }
            if (BedWars.config.getYml().get("pre-game-items.%path%.command".replace("%path%", item)) == null) {
                BedWars.plugin.getLogger().severe("pre-game-items.%path%.command".replace("%path%", item) + " is not set!");
                continue;
            }
            ItemStack i = Misc.createItem(Material.valueOf((String)BedWars.config.getYml().getString("pre-game-items.%path%.material".replace("%path%", item))), (byte)BedWars.config.getInt("pre-game-items.%path%.data".replace("%path%", item)), BedWars.config.getBoolean("pre-game-items.%path%.enchanted".replace("%path%", item)), SupportPAPI.getSupportPAPI().replace(p, Language.getMsg(p, "pre-game-items-%path%-name".replace("%path%", item))), SupportPAPI.getSupportPAPI().replace(p, Language.getList(p, "pre-game-items-%path%-lore".replace("%path%", item))), p, "RUNCOMMAND", BedWars.config.getYml().getString("pre-game-items.%path%.command".replace("%path%", item)));
            p.getInventory().setItem(BedWars.config.getInt("pre-game-items.%path%.slot".replace("%path%", item)), i);
        }
    }

    @Override
    public void sendSpectatorCommandItems(Player p) {
        if (BedWars.config.getYml().get("spectator-items") == null) {
            return;
        }
        p.getInventory().clear();
        for (String item : BedWars.config.getYml().getConfigurationSection("spectator-items").getKeys(false)) {
            if (BedWars.config.getYml().get("spectator-items.%path%.material".replace("%path%", item)) == null) {
                BedWars.plugin.getLogger().severe("spectator-items.%path%.material".replace("%path%", item) + " is not set!");
                continue;
            }
            if (BedWars.config.getYml().get("spectator-items.%path%.data".replace("%path%", item)) == null) {
                BedWars.plugin.getLogger().severe("spectator-items.%path%.data".replace("%path%", item) + " is not set!");
                continue;
            }
            if (BedWars.config.getYml().get("spectator-items.%path%.slot".replace("%path%", item)) == null) {
                BedWars.plugin.getLogger().severe("spectator-items.%path%.slot".replace("%path%", item) + " is not set!");
                continue;
            }
            if (BedWars.config.getYml().get("spectator-items.%path%.enchanted".replace("%path%", item)) == null) {
                BedWars.plugin.getLogger().severe("spectator-items.%path%.enchanted".replace("%path%", item) + " is not set!");
                continue;
            }
            if (BedWars.config.getYml().get("spectator-items.%path%.command".replace("%path%", item)) == null) {
                BedWars.plugin.getLogger().severe("spectator-items.%path%.command".replace("%path%", item) + " is not set!");
                continue;
            }
            ItemStack i = Misc.createItem(Material.valueOf((String)BedWars.config.getYml().getString("spectator-items.%path%.material".replace("%path%", item))), (byte)BedWars.config.getInt("spectator-items.%path%.data".replace("%path%", item)), BedWars.config.getBoolean("spectator-items.%path%.enchanted".replace("%path%", item)), SupportPAPI.getSupportPAPI().replace(p, Language.getMsg(p, "spectator-items-%path%-name".replace("%path%", item))), SupportPAPI.getSupportPAPI().replace(p, Language.getList(p, "spectator-items-%path%-lore".replace("%path%", item))), p, "RUNCOMMAND", BedWars.config.getYml().getString("spectator-items.%path%.command".replace("%path%", item)));
            p.getInventory().setItem(BedWars.config.getInt("spectator-items.%path%.slot".replace("%path%", item)), i);
        }
    }

    public static boolean isInArena(Player p) {
        return arenaByPlayer.containsKey(p);
    }

    @Override
    public ITeam getTeam(Player p) {
        for (ITeam t : this.getTeams()) {
            if (!t.isMember(p)) continue;
            return t;
        }
        return null;
    }

    @Override
    public ITeam getExTeam(UUID p) {
        for (ITeam t : this.getTeams()) {
            if (!t.wasMember(p)) continue;
            return t;
        }
        return null;
    }

    @Override
    @Deprecated
    public ITeam getPlayerTeam(String playerCache) {
        for (ITeam t : this.getTeams()) {
            for (Player p : t.getMembersCache()) {
                if (!p.getName().equals(playerCache)) continue;
                return t;
            }
        }
        return null;
    }

    @Override
    public void checkWinner() {
        if (this.status != GameState.restarting) {
            int max = this.getTeams().size();
            int eliminated = 0;
            for (ITeam t : this.getTeams()) {
                if (t.getMembers().isEmpty()) {
                    ++eliminated;
                    continue;
                }
                this.winner = t;
            }
            if (max - eliminated == 1) {
                Serializable winners;
                if (this.winner != null) {
                    if (!this.winner.getMembers().isEmpty()) {
                        for (Player p : this.winner.getMembers()) {
                            if (!p.isOnline()) continue;
                            p.getInventory().clear();
                        }
                    }
                    winners = new StringBuilder();
                    for (Player p : this.winner.getMembersCache()) {
                        if (((StringBuilder)winners).toString().contains(p.getDisplayName())) continue;
                        ((StringBuilder)winners).append(p.getDisplayName()).append(" ");
                    }
                    if (((StringBuilder)winners).toString().endsWith(" ")) {
                        winners = new StringBuilder(((StringBuilder)winners).substring(0, ((StringBuilder)winners).length() - 1));
                    }
                    StatisticsOrdered topInChat = null;
                    if (null != this.getStatsHolder()) {
                        topInChat = new StatisticsOrdered(this, this.getConfig().getGameOverridableString("game-end.chat-top.order-by"));
                        if (this.getConfig().getGameOverridableBoolean("game-end.chat-top.hide-missing").booleanValue()) {
                            topInChat.setBoundsPolicy(StatisticsOrdered.BoundsPolicy.SKIP);
                        }
                    }
                    StatisticsOrdered topInSidebar = new StatisticsOrdered(this, this.getConfig().getGameOverridableString("game-end.sb-top.order-by"));
                    if (this.getConfig().getGameOverridableBoolean("game-end.sb-top.hide-missing").booleanValue()) {
                        topInSidebar.setBoundsPolicy(StatisticsOrdered.BoundsPolicy.SKIP);
                    }
                    ArrayList<Player> receivers = new ArrayList<Player>(this.getPlayers().size() + this.getSpectators().size());
                    receivers.addAll(this.getPlayers());
                    receivers.addAll(this.getSpectators());
                    if (null != topInChat) {
                        StatisticsOrdered.StringParser statParser = topInChat.newParser();
                        for (Player receiver : receivers) {
                            Language playerLang = Language.getPlayerLanguage(receiver);
                            String winnerTeamChat = playerLang.m(Messages.GAME_END_TEAM_WON_CHAT);
                            if (null != winnerTeamChat && !winnerTeamChat.isBlank()) {
                                receiver.sendMessage(winnerTeamChat.replace("{TeamColor}", this.winner.getColor().chat().toString()).replace("{TeamName}", this.winner.getDisplayName(playerLang)));
                            }
                            if (this.winner.getMembers().contains(receiver) || this.winner.wasMember(receiver.getUniqueId())) {
                                BedWars.nms.sendTitle(receiver, Language.getMsg(receiver, Messages.GAME_END_VICTORY_PLAYER_TITLE), null, 0, 70, 20);
                            } else {
                                BedWars.nms.sendTitle(receiver, playerLang.m(Messages.GAME_END_GAME_OVER_PLAYER_TITLE), null, 0, 70, 20);
                            }
                            statParser.resetIndex();
                            List<String> topChat = Language.getList(receiver, Messages.GAME_END_TOP_PLAYER_CHAT);
                            if (topChat.isEmpty() || topChat.size() == 1 && topChat.get(0).isEmpty()) continue;
                            for (String s : topChat) {
                                String msg = statParser.parseString(s, playerLang, playerLang.m(Messages.MEANING_NOBODY));
                                if (null == msg) continue;
                                msg = msg.replace("{winnerFormat}", this.getMaxInTeam() > 1 ? playerLang.m(Messages.FORMATTING_TEAM_WINNER_FORMAT).replace("{members}", ((StringBuilder)winners).toString()) : playerLang.m(Messages.FORMATTING_SOLO_WINNER_FORMAT).replace("{members}", ((StringBuilder)winners).toString())).replace("{TeamColor}", this.winner.getColor().chat().toString()).replace("{TeamName}", this.winner.getDisplayName(playerLang));
                                receiver.sendMessage(SupportPAPI.getSupportPAPI().replace(receiver, msg));
                            }
                            ISidebar sidebar = SidebarService.getInstance().getSidebar(receiver);
                            if (!(sidebar instanceof BwSidebar)) continue;
                            ((BwSidebar)sidebar).setTopStatistics(topInSidebar);
                        }
                    }
                }
                this.changeStatus(GameState.restarting);
                winners = new ArrayList();
                ArrayList<UUID> losers = new ArrayList<UUID>();
                ArrayList<UUID> aliveWinners = new ArrayList<UUID>();
                for (Player p : this.getPlayers()) {
                    aliveWinners.add(p.getUniqueId());
                }
                if (this.winner != null) {
                    for (Player p : this.winner.getMembersCache()) {
                        winners.add(p.getUniqueId());
                    }
                }
                for (ITeam bwt : this.getTeams()) {
                    if (this.winner != null && bwt == this.winner) continue;
                    for (Player p : bwt.getMembersCache()) {
                        losers.add(p.getUniqueId());
                    }
                }
                Bukkit.getPluginManager().callEvent((Event)new GameEndEvent(this, (List<UUID>)((Object)winners), losers, this.winner, aliveWinners));
            }
            if (this.players.isEmpty() && this.status != GameState.restarting) {
                this.changeStatus(GameState.restarting);
            }
        }
    }

    @Override
    @Deprecated(forRemoval=true)
    public void addPlayerDeath(Player player) {
    }

    @Override
    public void setNextEvent(NextEvent nextEvent) {
        if (this.nextEvent != null) {
            Sounds.playSound(this.nextEvent.getSoundPath(), this.getPlayers());
            Sounds.playSound(this.nextEvent.getSoundPath(), this.getSpectators());
        }
        Bukkit.getPluginManager().callEvent((Event)new NextEventChangeEvent(this, nextEvent, this.nextEvent));
        this.nextEvent = nextEvent;
    }

    @Override
    public void updateNextEvent() {
        BedWars.debug("---");
        BedWars.debug("updateNextEvent called");
        if (this.nextEvent == NextEvent.EMERALD_GENERATOR_TIER_II && this.upgradeEmeraldsCount == 0) {
            int next = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.getGroup() + ".emerald.tierIII.start") == null ? "Default.emerald.tierIII.start" : this.getGroup() + ".emerald.tierIII.start"));
            if (this.upgradeDiamondsCount < next && this.diamondTier == 1) {
                this.setNextEvent(NextEvent.DIAMOND_GENERATOR_TIER_II);
            } else if (this.upgradeDiamondsCount < next && this.diamondTier == 2) {
                this.setNextEvent(NextEvent.DIAMOND_GENERATOR_TIER_III);
            } else {
                this.setNextEvent(NextEvent.EMERALD_GENERATOR_TIER_III);
            }
            this.upgradeEmeraldsCount = next;
            this.emeraldTier = 2;
            this.sendEmeraldsUpgradeMessages();
            for (IGenerator o : this.getOreGenerators()) {
                if (o.getType() != GeneratorType.EMERALD || o.getBwt() != null) continue;
                o.upgrade();
            }
        } else if (this.nextEvent == NextEvent.DIAMOND_GENERATOR_TIER_II && this.upgradeDiamondsCount == 0) {
            int next = BedWars.getGeneratorsCfg().getInt((String)(BedWars.getGeneratorsCfg().getYml().get(this.getGroup() + ".diamond.tierIII.start") == null ? "Default.diamond.tierIII.start" : this.getGroup() + ".diamond.tierIII.start"));
            if (this.upgradeEmeraldsCount < next && this.emeraldTier == 1) {
                this.setNextEvent(NextEvent.EMERALD_GENERATOR_TIER_II);
            } else if (this.upgradeEmeraldsCount < next && this.emeraldTier == 2) {
                this.setNextEvent(NextEvent.EMERALD_GENERATOR_TIER_III);
            } else {
                this.setNextEvent(NextEvent.DIAMOND_GENERATOR_TIER_III);
            }
            this.upgradeDiamondsCount = next;
            this.diamondTier = 2;
            this.sendDiamondsUpgradeMessages();
            for (IGenerator o : this.getOreGenerators()) {
                if (o.getType() != GeneratorType.DIAMOND || o.getBwt() != null) continue;
                o.upgrade();
            }
        } else if (this.nextEvent == NextEvent.EMERALD_GENERATOR_TIER_III && this.upgradeEmeraldsCount == 0) {
            this.emeraldTier = 3;
            this.sendEmeraldsUpgradeMessages();
            if (this.diamondTier == 1 && this.upgradeDiamondsCount > 0) {
                this.setNextEvent(NextEvent.DIAMOND_GENERATOR_TIER_II);
            } else if (this.diamondTier == 2 && this.upgradeDiamondsCount > 0) {
                this.setNextEvent(NextEvent.DIAMOND_GENERATOR_TIER_III);
            } else {
                this.setNextEvent(NextEvent.BEDS_DESTROY);
            }
            for (IGenerator o : this.getOreGenerators()) {
                if (o.getType() != GeneratorType.EMERALD || o.getBwt() != null) continue;
                o.upgrade();
            }
        } else if (this.nextEvent == NextEvent.DIAMOND_GENERATOR_TIER_III && this.upgradeDiamondsCount == 0) {
            this.diamondTier = 3;
            this.sendDiamondsUpgradeMessages();
            if (this.emeraldTier == 1 && this.upgradeEmeraldsCount > 0) {
                this.setNextEvent(NextEvent.EMERALD_GENERATOR_TIER_II);
            } else if (this.emeraldTier == 2 && this.upgradeEmeraldsCount > 0) {
                this.setNextEvent(NextEvent.EMERALD_GENERATOR_TIER_III);
            } else {
                this.setNextEvent(NextEvent.BEDS_DESTROY);
            }
            for (IGenerator o : this.getOreGenerators()) {
                if (o.getType() != GeneratorType.DIAMOND || o.getBwt() != null) continue;
                o.upgrade();
            }
        } else if (this.nextEvent == NextEvent.BEDS_DESTROY && this.getPlayingTask().getBedsDestroyCountdown() == 0) {
            this.setNextEvent(NextEvent.ENDER_DRAGON);
        } else if (this.nextEvent == NextEvent.ENDER_DRAGON && this.getPlayingTask().getDragonSpawnCountdown() == 0) {
            this.setNextEvent(NextEvent.GAME_END);
        }
        BedWars.debug("---");
        BedWars.debug(this.nextEvent.toString());
    }

    public static HashMap<Player, IArena> getArenaByPlayer() {
        return arenaByPlayer;
    }

    @Override
    public NextEvent getNextEvent() {
        return this.nextEvent;
    }

    public static int getPlayers(@NotNull String group) {
        String[] groups;
        int i = 0;
        for (String g : groups = group.split("\\+")) {
            for (IArena a : Arena.getArenas()) {
                if (!a.getGroup().equalsIgnoreCase(g)) continue;
                i += a.getPlayers().size();
            }
        }
        return i;
    }

    private void registerSigns() {
        if (BedWars.getServerType() != ServerType.BUNGEE && BedWars.signs.getYml().get("locations") != null) {
            for (String st : BedWars.signs.getYml().getStringList("locations")) {
                Location l;
                String[] data = st.split(",");
                if (!data[0].equals(this.getArenaName())) continue;
                try {
                    l = new Location(Bukkit.getWorld((String)data[6]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]));
                } catch (Exception e) {
                    BedWars.plugin.getLogger().severe("Could not load sign at: " + data.toString());
                    continue;
                }
                this.addSign(l);
            }
        }
    }

    @Override
    public ITeam getTeam(String name) {
        for (ITeam bwt : this.getTeams()) {
            if (!bwt.getName().equals(name)) continue;
            return bwt;
        }
        return null;
    }

    @Override
    public ConcurrentHashMap<Player, Integer> getRespawnSessions() {
        return this.respawnSessions;
    }

    @Override
    public void updateSpectatorCollideRule(Player p, boolean collide) {
    }

    @Override
    public ConcurrentHashMap<Player, Integer> getShowTime() {
        return this.showTime;
    }

    @Override
    public StartingTask getStartingTask() {
        return this.startingTask;
    }

    @Override
    public PlayingTask getPlayingTask() {
        return this.playingTask;
    }

    @Override
    public RestartingTask getRestartingTask() {
        return this.restartingTask;
    }

    @Override
    public List<IGenerator> getOreGenerators() {
        return this.oreGenerators;
    }

    public static boolean joinRandomArena(Player p) {
        List<IArena> arenas = Arena.getSorted(Arena.getArenas());
        int amount = BedWars.getParty().hasParty(p) ? (int)BedWars.getParty().getMembers(p).stream().filter(member -> {
            IArena arena = Arena.getArenaByPlayer(member);
            if (arena == null) {
                return true;
            }
            return arena.isSpectator((Player)member);
        }).count() : 1;
        for (IArena a : arenas) {
            if (a.getPlayers().size() != a.getMaxPlayers() && a.getMaxPlayers() - a.getPlayers().size() >= amount && a.addPlayer(p, false)) break;
        }
        return true;
    }

    public static List<IArena> getSorted(List<IArena> arenas) {
        ArrayList<IArena> sorted = new ArrayList<IArena>(arenas);
        sorted.sort(new Comparator<IArena>(){

            @Override
            public int compare(IArena o1, IArena o2) {
                if (o1.getStatus() == GameState.starting && o2.getStatus() == GameState.starting) {
                    return Integer.compare(o2.getPlayers().size(), o1.getPlayers().size());
                }
                if (o1.getStatus() == GameState.starting && o2.getStatus() != GameState.starting) {
                    return -1;
                }
                if (o2.getStatus() == GameState.starting && o1.getStatus() != GameState.starting) {
                    return 1;
                }
                if (o1.getStatus() == GameState.waiting && o2.getStatus() == GameState.waiting) {
                    return Integer.compare(o2.getPlayers().size(), o1.getPlayers().size());
                }
                if (o1.getStatus() == GameState.waiting && o2.getStatus() != GameState.waiting) {
                    return -1;
                }
                if (o2.getStatus() == GameState.waiting && o1.getStatus() != GameState.waiting) {
                    return 1;
                }
                if (o1.getStatus() == GameState.playing && o2.getStatus() == GameState.playing) {
                    return 0;
                }
                if (o1.getStatus() == GameState.playing && o2.getStatus() != GameState.playing) {
                    return -1;
                }
                return 1;
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof IArena;
            }
        });
        return sorted;
    }

    public static boolean joinRandomFromGroup(Player p, @NotNull String group) {
        List<IArena> arenas = Arena.getSorted(Arena.getArenas());
        int amount = BedWars.getParty().hasParty(p) ? (int)BedWars.getParty().getMembers(p).stream().filter(member -> {
            IArena arena = Arena.getArenaByPlayer(member);
            if (arena == null) {
                return true;
            }
            return arena.isSpectator((Player)member);
        }).count() : 1;
        String[] groups = group.split("\\+");
        for (IArena a : arenas) {
            if (a.getPlayers().size() == a.getMaxPlayers()) continue;
            for (String g : groups) {
                if (!a.getGroup().equalsIgnoreCase(g) || a.getMaxPlayers() - a.getPlayers().size() < amount || !a.addPlayer(p, false)) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> getNextEvents() {
        return new ArrayList<String>(this.nextEvents);
    }

    @Override
    @Deprecated(forRemoval=true)
    public int getPlayerDeaths(Player player, boolean finalDeaths) {
        if (null == player || null == this.getStatsHolder()) {
            return 0;
        }
        Optional st = this.getStatsHolder().get(player).flatMap(stats -> stats.getStatistic(finalDeaths ? DefaultStatistics.DEATHS_FINAL : DefaultStatistics.DEATHS));
        if (st.isEmpty()) {
            return 0;
        }
        GameStatistic gs = (GameStatistic)st.get();
        return gs instanceof Incrementable ? (Integer)gs.getValue() : 0;
    }

    @Override
    public void sendDiamondsUpgradeMessages() {
        for (Player p : this.getPlayers()) {
            p.sendMessage(Language.getMsg(p, Messages.GENERATOR_UPGRADE_CHAT_ANNOUNCEMENT).replace("{generatorType}", Language.getMsg(p, Messages.GENERATOR_HOLOGRAM_TYPE_DIAMOND)).replace("{tier}", Language.getMsg(p, this.diamondTier == 2 ? Messages.FORMATTING_GENERATOR_TIER2 : Messages.FORMATTING_GENERATOR_TIER3)));
        }
        for (Player p : this.getSpectators()) {
            p.sendMessage(Language.getMsg(p, Messages.GENERATOR_UPGRADE_CHAT_ANNOUNCEMENT).replace("{generatorType}", Language.getMsg(p, Messages.GENERATOR_HOLOGRAM_TYPE_DIAMOND)).replace("{tier}", Language.getMsg(p, this.diamondTier == 2 ? Messages.FORMATTING_GENERATOR_TIER2 : Messages.FORMATTING_GENERATOR_TIER3)));
        }
    }

    @Override
    public void sendEmeraldsUpgradeMessages() {
        for (Player p : this.getPlayers()) {
            p.sendMessage(Language.getMsg(p, Messages.GENERATOR_UPGRADE_CHAT_ANNOUNCEMENT).replace("{generatorType}", Language.getMsg(p, Messages.GENERATOR_HOLOGRAM_TYPE_EMERALD)).replace("{tier}", Language.getMsg(p, this.emeraldTier == 2 ? Messages.FORMATTING_GENERATOR_TIER2 : Messages.FORMATTING_GENERATOR_TIER3)));
        }
        for (Player p : this.getSpectators()) {
            p.sendMessage(Language.getMsg(p, Messages.GENERATOR_UPGRADE_CHAT_ANNOUNCEMENT).replace("{generatorType}", Language.getMsg(p, Messages.GENERATOR_HOLOGRAM_TYPE_EMERALD)).replace("{tier}", Language.getMsg(p, this.emeraldTier == 2 ? Messages.FORMATTING_GENERATOR_TIER2 : Messages.FORMATTING_GENERATOR_TIER3)));
        }
    }

    public static int getGamesBeforeRestart() {
        return gamesBeforeRestart;
    }

    public static void setGamesBeforeRestart(int gamesBeforeRestart) {
        Arena.gamesBeforeRestart = gamesBeforeRestart;
    }

    @Override
    public List<Region> getRegionsList() {
        return this.regionsList;
    }

    @Override
    public LinkedList<Vector> getPlaced() {
        return this.placed;
    }

    public static LinkedList<IArena> getEnableQueue() {
        return enableQueue;
    }

    @Override
    public Map<UUID, Long> getFireballCooldowns() {
        return this.fireballCooldowns;
    }

    @Override
    public void destroyData() {
        this.destroyReJoins();
        if (this.worldName != null) {
            arenaByIdentifier.remove(this.worldName);
        }
        arenas.remove(this);
        for (ReJoinTask rjt : ReJoinTask.getReJoinTasks()) {
            if (rjt.getArena() != this) continue;
            rjt.destroy();
        }
        for (Despawnable despawnable : new ArrayList<Despawnable>(BedWars.nms.getDespawnablesList().values())) {
            if (despawnable.getTeam().getArena() != this) continue;
            despawnable.destroy();
        }
        arenaByName.remove(this.arenaName);
        arenaByPlayer.entrySet().removeIf(entry -> entry.getValue() == this);
        this.players = null;
        this.spectators = null;
        this.signs = null;
        this.yml = null;
        this.cm = null;
        this.world = null;
        for (IGenerator og : this.oreGenerators) {
            og.destroyData();
        }
        BaseListener.isOnABase.entrySet().removeIf(entry -> ((ITeam)entry.getValue()).getArena().equals(this));
        for (ITeam bwt : this.teams) {
            bwt.destroyData();
        }
        playerLocation.entrySet().removeIf(e -> Objects.requireNonNull(((Location)e.getValue()).getWorld()).getName().equalsIgnoreCase(this.worldName));
        this.teams = null;
        this.placed = null;
        this.nextEvents = null;
        this.regionsList = null;
        this.respawnSessions = null;
        this.showTime = null;
        this.startingTask = null;
        this.playingTask = null;
        this.restartingTask = null;
        this.oreGenerators = null;
        this.perMinuteTask = null;
        this.moneyperMinuteTask = null;
        this.leaving.clear();
        this.fireballCooldowns.clear();
    }

    public static void removeFromEnableQueue(IArena a) {
        enableQueue.remove(a);
        if (!enableQueue.isEmpty()) {
            BedWars.getAPI().getRestoreAdapter().onEnable(enableQueue.get(0));
            BedWars.plugin.getLogger().info("Loading arena: " + enableQueue.get(0).getWorldName());
        }
    }

    public static void addToEnableQueue(IArena a) {
        enableQueue.add(a);
        BedWars.plugin.getLogger().info("Arena " + a.getWorldName() + " was added to the enable queue.");
        if (enableQueue.size() == 1) {
            BedWars.getAPI().getRestoreAdapter().onEnable(a);
            BedWars.plugin.getLogger().info("Loading arena: " + a.getWorldName());
        }
    }

    @Override
    public int getUpgradeDiamondsCount() {
        return this.upgradeDiamondsCount;
    }

    @Override
    public int getUpgradeEmeraldsCount() {
        return this.upgradeEmeraldsCount;
    }

    @Override
    public void setAllowSpectate(boolean allowSpectate) {
        this.allowSpectate = allowSpectate;
    }

    @Override
    public boolean isAllowSpectate() {
        return this.allowSpectate;
    }

    @Override
    public String getWorldName() {
        return this.worldName;
    }

    @Override
    public int getRenderDistance() {
        return this.renderDistance;
    }

    @Override
    public Location getReSpawnLocation() {
        return this.respawnLocation;
    }

    @Override
    public Location getSpectatorLocation() {
        return this.spectatorLocation;
    }

    @Override
    public Location getWaitingLocation() {
        return this.waitingLocation;
    }

    @Override
    public boolean startReSpawnSession(Player player, int seconds) {
        if (this.respawnSessions.get(player) == null) {
            IArena arena = Arena.getArenaByPlayer(player);
            if (arena == null) {
                return false;
            }
            if (!arena.isPlayer(player)) {
                return false;
            }
            player.getInventory().clear();
            if (seconds > 1) {
                for (Player playing : arena.getPlayers()) {
                    if (playing.equals((Object)player)) continue;
                    BedWars.nms.spigotHidePlayer(player, playing);
                }
                TeleportManager.teleportC((Entity)player, this.getReSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                player.setAllowFlight(true);
                player.setFlying(true);
                this.respawnSessions.put(player, seconds);
                Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    BedWars.nms.setCollide(player, this, false);
                    for (Player invisible : this.getShowTime().keySet()) {
                        BedWars.nms.hideArmor(invisible, player);
                    }
                    this.updateSpectatorCollideRule(player, false);
                    TeleportManager.teleportC((Entity)player, this.getReSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                }, 10L);
            } else {
                ITeam team = this.getTeam(player);
                team.respawnMember(player);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isReSpawning(Player player) {
        return this.respawnSessions.containsKey(player);
    }

    public static boolean canAutoScale(String arenaName) {
        if (!BedWars.autoscale) {
            return true;
        }
        if (Arena.getArenas().isEmpty()) {
            return true;
        }
        for (IArena ar : Arena.getEnableQueue()) {
            if (!ar.getArenaName().equalsIgnoreCase(arenaName)) continue;
            return false;
        }
        if (Arena.getGamesBeforeRestart() != -1 && Arena.getArenas().size() >= Arena.getGamesBeforeRestart()) {
            return false;
        }
        int activeClones = 0;
        for (IArena ar : Arena.getArenas()) {
            GameState status;
            if (ar.getArenaName().equalsIgnoreCase(arenaName) && ((status = ar.getStatus()) == GameState.waiting || status == GameState.starting)) {
                return false;
            }
            if (!ar.getArenaName().equals(arenaName)) continue;
            ++activeClones;
        }
        return BedWars.config.getInt("bungee-settings.auto-scale-clone-limit") > activeClones;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof IArena) {
            return ((IArena)obj).getWorldName().equals(this.getWorldName());
        }
        return false;
    }

    private void destroyReJoins() {
        ArrayList<ReJoin> reJoins = new ArrayList<ReJoin>(ReJoin.getReJoinList());
        for (ReJoin reJoin : reJoins) {
            if (reJoin.getArena() != this) continue;
            reJoin.destroy(true);
        }
    }

    @Override
    public boolean isProtected(Location location) {
        return Misc.isBuildProtected(location, this);
    }

    @Override
    public void abandonGame(Player player) {
        if (player == null) {
            return;
        }
        ITeam team = this.getTeams().stream().filter(team1 -> team1.wasMember(player.getUniqueId())).findFirst().orElse(null);
        if (team != null) {
            team.getMembersCache().removeIf(cachedPlayer -> cachedPlayer.getUniqueId().equals(player.getUniqueId()));
            ReJoin rejoin = ReJoin.getPlayer(player);
            if (rejoin != null) {
                rejoin.destroy(team.getMembers().isEmpty());
            }
        }
    }

    @Override
    public int getYKillHeight() {
        return this.yKillHeight;
    }

    @Override
    public Instant getStartTime() {
        return this.startTime;
    }

    @Override
    public ITeamAssigner getTeamAssigner() {
        return this.teamAssigner;
    }

    @Override
    public void setTeamAssigner(ITeamAssigner teamAssigner) {
        if (teamAssigner == null) {
            this.teamAssigner = new TeamAssigner();
            BedWars.plugin.getLogger().info("Using Default team assigner on arena: " + this.getArenaName());
        } else {
            this.teamAssigner = teamAssigner;
            BedWars.plugin.getLogger().warning("Using " + teamAssigner.getClass().getSimpleName() + " team assigner on arena: " + this.getArenaName());
        }
    }

    @Override
    public List<Player> getLeavingPlayers() {
        return this.leaving;
    }

    private void sendToMainLobby(Player player) {
        if (BedWars.getServerType() == ServerType.SHARED) {
            Location loc = playerLocation.get(player);
            if (loc == null) {
                TeleportManager.teleportC((Entity)player, ((World)Bukkit.getWorlds().get(0)).getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                BedWars.plugin.getLogger().log(Level.SEVERE, player.getName() + " was teleported to the main world because lobby location is not set!");
            } else {
                TeleportManager.teleportC((Entity)player, loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        } else if (BedWars.getServerType() == ServerType.MULTIARENA) {
            if (BedWars.getLobbyWorld().isEmpty()) {
                TeleportManager.teleportC((Entity)player, ((World)Bukkit.getWorlds().get(0)).getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                BedWars.plugin.getLogger().log(Level.SEVERE, player.getName() + " was teleported to the main world because lobby location is not set!");
            } else {
                TeleportManager.teleportC((Entity)player, BedWars.config.getConfigLoc("lobbyLoc"), PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        }
    }

    @Override
    public boolean isAllowMapBreak() {
        return this.allowMapBreak;
    }

    @Override
    public void setAllowMapBreak(boolean allowMapBreak) {
        this.allowMapBreak = allowMapBreak;
    }

    @Override
    @Nullable
    public ITeam getBedsTeam(@NotNull Location location) {
        if (!location.getWorld().getName().equals(this.worldName)) {
            throw new RuntimeException("Given location is not on this game world.");
        }
        if (!BedWars.nms.isBed(location.getBlock().getType())) {
            return null;
        }
        for (ITeam team : this.teams) {
            if (!team.isBed(location)) continue;
            return team;
        }
        return null;
    }

    @Override
    @Nullable
    public ITeam getWinner() {
        return this.winner;
    }

    @Override
    public boolean isTeamBed(Location location) {
        return null != this.getBedsTeam(location);
    }

    @Override
    public GameStatsHolder getStatsHolder() {
        return this.gameStats;
    }
}

