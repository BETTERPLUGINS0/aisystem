/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.chat.Chat
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.WorldCreator
 *  org.bukkit.command.Command
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Monster
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.generator.ChunkGenerator
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 *  org.bukkit.plugin.RegisteredServiceProvider
 *  org.bukkit.plugin.ServicePriority
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.andrei1058.bedwars;

import com.andrei1058.bedwars.API;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.configuration.ConfigManager;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.levels.Level;
import com.andrei1058.bedwars.api.party.Party;
import com.andrei1058.bedwars.api.server.RestoreAdapter;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.api.server.VersionSupport;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.ArenaManager;
import com.andrei1058.bedwars.arena.VoidChunkGenerator;
import com.andrei1058.bedwars.arena.despawnables.TargetListener;
import com.andrei1058.bedwars.arena.feature.SpoilPlayerTNTFeature;
import com.andrei1058.bedwars.arena.spectator.SpectatorListeners;
import com.andrei1058.bedwars.arena.stats.DefaultStatsHandler;
import com.andrei1058.bedwars.arena.tasks.OneTick;
import com.andrei1058.bedwars.arena.tasks.Refresh;
import com.andrei1058.bedwars.arena.upgrades.BaseListener;
import com.andrei1058.bedwars.arena.upgrades.HealPoolListner;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import com.andrei1058.bedwars.commands.leave.LeaveCommand;
import com.andrei1058.bedwars.commands.party.PartyCommand;
import com.andrei1058.bedwars.commands.rejoin.RejoinCommand;
import com.andrei1058.bedwars.commands.shout.ShoutCommand;
import com.andrei1058.bedwars.configuration.GeneratorsConfig;
import com.andrei1058.bedwars.configuration.LevelsConfig;
import com.andrei1058.bedwars.configuration.MainConfig;
import com.andrei1058.bedwars.configuration.MoneyConfig;
import com.andrei1058.bedwars.configuration.SignsConfig;
import com.andrei1058.bedwars.configuration.Sounds;
import com.andrei1058.bedwars.database.Database;
import com.andrei1058.bedwars.database.MySQL;
import com.andrei1058.bedwars.database.SQLite;
import com.andrei1058.bedwars.halloween.HalloweenSpecial;
import com.andrei1058.bedwars.language.Bangla;
import com.andrei1058.bedwars.language.English;
import com.andrei1058.bedwars.language.Hindi;
import com.andrei1058.bedwars.language.Indonesia;
import com.andrei1058.bedwars.language.Italian;
import com.andrei1058.bedwars.language.LangListener;
import com.andrei1058.bedwars.language.Persian;
import com.andrei1058.bedwars.language.Polish;
import com.andrei1058.bedwars.language.Portuguese;
import com.andrei1058.bedwars.language.Romanian;
import com.andrei1058.bedwars.language.Russian;
import com.andrei1058.bedwars.language.SimplifiedChinese;
import com.andrei1058.bedwars.language.Spanish;
import com.andrei1058.bedwars.language.Turkish;
import com.andrei1058.bedwars.levels.internal.InternalLevel;
import com.andrei1058.bedwars.levels.internal.LevelListeners;
import com.andrei1058.bedwars.listeners.AutoscaleListener;
import com.andrei1058.bedwars.listeners.BreakPlace;
import com.andrei1058.bedwars.listeners.ChunkLoad;
import com.andrei1058.bedwars.listeners.CmdProcess;
import com.andrei1058.bedwars.listeners.DamageDeathMove;
import com.andrei1058.bedwars.listeners.EggBridge;
import com.andrei1058.bedwars.listeners.EnderPearlLanded;
import com.andrei1058.bedwars.listeners.FireballListener;
import com.andrei1058.bedwars.listeners.GameEndListener;
import com.andrei1058.bedwars.listeners.HungerWeatherSpawn;
import com.andrei1058.bedwars.listeners.Interact;
import com.andrei1058.bedwars.listeners.Inventory;
import com.andrei1058.bedwars.listeners.InvisibilityPotionListener;
import com.andrei1058.bedwars.listeners.QuitAndTeleportListener;
import com.andrei1058.bedwars.listeners.RefreshGUI;
import com.andrei1058.bedwars.listeners.ServerPingListener;
import com.andrei1058.bedwars.listeners.Warnings;
import com.andrei1058.bedwars.listeners.WorldLoadListener;
import com.andrei1058.bedwars.listeners.arenaselector.ArenaSelectorListener;
import com.andrei1058.bedwars.listeners.blockstatus.BlockStatusListener;
import com.andrei1058.bedwars.listeners.chat.ChatAFK;
import com.andrei1058.bedwars.listeners.chat.ChatFormatting;
import com.andrei1058.bedwars.listeners.joinhandler.JoinHandlerCommon;
import com.andrei1058.bedwars.listeners.joinhandler.JoinListenerBungee;
import com.andrei1058.bedwars.listeners.joinhandler.JoinListenerBungeeLegacy;
import com.andrei1058.bedwars.listeners.joinhandler.JoinListenerMultiArena;
import com.andrei1058.bedwars.listeners.joinhandler.JoinListenerShared;
import com.andrei1058.bedwars.lobbysocket.ArenaSocket;
import com.andrei1058.bedwars.lobbysocket.LoadedUsersCleaner;
import com.andrei1058.bedwars.lobbysocket.SendTask;
import com.andrei1058.bedwars.maprestore.internal.InternalAdapter;
import com.andrei1058.bedwars.metrics.MetricsManager;
import com.andrei1058.bedwars.money.internal.MoneyListeners;
import com.andrei1058.bedwars.shop.ShopManager;
import com.andrei1058.bedwars.sidebar.SidebarService;
import com.andrei1058.bedwars.stats.StatsManager;
import com.andrei1058.bedwars.support.citizens.CitizensListener;
import com.andrei1058.bedwars.support.citizens.JoinNPC;
import com.andrei1058.bedwars.support.papi.PAPISupport;
import com.andrei1058.bedwars.support.papi.SupportPAPI;
import com.andrei1058.bedwars.support.party.Internal;
import com.andrei1058.bedwars.support.party.NoParty;
import com.andrei1058.bedwars.support.party.PAF;
import com.andrei1058.bedwars.support.party.PAFBungeecordRedisApi;
import com.andrei1058.bedwars.support.party.PartiesAdapter;
import com.andrei1058.bedwars.support.preloadedparty.PrePartyListener;
import com.andrei1058.bedwars.support.vault.Chat;
import com.andrei1058.bedwars.support.vault.Economy;
import com.andrei1058.bedwars.support.vault.NoChat;
import com.andrei1058.bedwars.support.vault.NoEconomy;
import com.andrei1058.bedwars.support.vault.WithChat;
import com.andrei1058.bedwars.support.vault.WithEconomy;
import com.andrei1058.bedwars.support.vipfeatures.VipFeatures;
import com.andrei1058.bedwars.support.vipfeatures.VipListeners;
import com.andrei1058.bedwars.upgrades.UpgradesManager;
import com.andrei1058.vipfeatures.api.IVipFeatures;
import com.andrei1058.vipfeatures.api.MiniGameAlreadyRegistered;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class BedWars
extends JavaPlugin {
    private static ServerType serverType = ServerType.MULTIARENA;
    public static boolean debug = true;
    public static boolean autoscale = false;
    public static String mainCmd = "bw";
    public static String link = "https://www.spigotmc.org/resources/50942/";
    public static ConfigManager signs;
    public static ConfigManager generators;
    public static MainConfig config;
    public static ShopManager shop;
    public static StatsManager statsManager;
    public static BedWars plugin;
    public static VersionSupport nms;
    public static boolean isPaper;
    private static Party party;
    private static Chat chat;
    protected static Level level;
    private static Economy economy;
    private static final String version;
    private static String lobbyWorld;
    private static boolean shuttingDown;
    public static ArenaManager arenaManager;
    private static Database remoteDatabase;
    private boolean serverSoftwareSupport = true;
    private static com.andrei1058.bedwars.api.BedWars api;

    public void onLoad() {
        Class<?> supp2;
        try {
            Class.forName("org.spigotmc.SpigotConfig");
        } catch (Exception ignored) {
            this.getLogger().severe("I can't run on your server software. Please check:");
            this.getLogger().severe("https://gitlab.com/andrei1058/BedWars1058/wikis/compatibility");
            this.serverSoftwareSupport = false;
            return;
        }
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            isPaper = true;
        } catch (ClassNotFoundException e) {
            isPaper = false;
        }
        plugin = this;
        try {
            supp2 = Class.forName("com.andrei1058.bedwars.support.version." + version + "." + version);
        } catch (ClassNotFoundException e) {
            this.serverSoftwareSupport = false;
            this.getLogger().severe("I can't run on your version: " + version);
            return;
        }
        api = new API();
        Bukkit.getServicesManager().register(com.andrei1058.bedwars.api.BedWars.class, (Object)api, (Plugin)this, ServicePriority.Highest);
        try {
            nms = (VersionSupport)supp2.getConstructor(Class.forName("org.bukkit.plugin.Plugin"), String.class).newInstance(new Object[]{this, version});
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            this.serverSoftwareSupport = false;
            this.getLogger().severe("Could not load support for server version: " + version);
            return;
        }
        this.getLogger().info("Loading support for paper/spigot: " + version);
        new English();
        new Romanian();
        new Italian();
        new Polish();
        new Spanish();
        new Russian();
        new Bangla();
        new Persian();
        new Hindi();
        new Indonesia();
        new Portuguese();
        new SimplifiedChinese();
        new Turkish();
        config = new MainConfig((Plugin)this, "config");
        generators = new GeneratorsConfig((Plugin)this, "generators", this.getDataFolder().getPath());
        if (BedWars.getServerType() != ServerType.BUNGEE) {
            signs = new SignsConfig((Plugin)this, "signs", this.getDataFolder().getPath());
        }
    }

    public void onEnable() {
        if (!this.serverSoftwareSupport) {
            Bukkit.getPluginManager().disablePlugin((Plugin)this);
            return;
        }
        nms.registerVersionListeners();
        if (!this.handleWorldAdapter()) {
            api.setRestoreAdapter(new InternalAdapter((Plugin)this));
            this.getLogger().info("Using internal world restore system.");
        }
        nms.registerCommand(mainCmd, (Command)new MainCommand(mainCmd));
        if (nms.getVersion() >= 9) {
            this.registerDelayedCommands();
        } else {
            Bukkit.getScheduler().runTaskLater((Plugin)this, this::registerDelayedCommands, 20L);
        }
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "BungeeCord");
        Logger out = plugin.getLogger();
        if (config.getLobbyWorldName().isEmpty() && serverType != ServerType.BUNGEE) {
            out.log(java.util.logging.Level.WARNING, "Lobby location is not set!");
        }
        if (BedWars.getServerType() == ServerType.MULTIARENA) {
            Bukkit.getScheduler().runTaskLater((Plugin)this, () -> {
                if (!config.getLobbyWorldName().isEmpty()) {
                    World w;
                    Location l;
                    if (Bukkit.getWorld((String)config.getLobbyWorldName()) == null && new File(Bukkit.getWorldContainer(), config.getLobbyWorldName() + "/level.dat").exists() && !config.getLobbyWorldName().equalsIgnoreCase(((World)Bukkit.getServer().getWorlds().get(0)).getName())) {
                        Bukkit.getScheduler().runTaskLater((Plugin)this, () -> {
                            Bukkit.createWorld((WorldCreator)new WorldCreator(config.getLobbyWorldName()));
                            if (Bukkit.getWorld((String)config.getLobbyWorldName()) != null) {
                                Bukkit.getScheduler().runTaskLater((Plugin)plugin, () -> Objects.requireNonNull(Bukkit.getWorld((String)config.getLobbyWorldName())).getEntities().stream().filter(e -> e instanceof Monster).forEach(Entity::remove), 20L);
                            }
                        }, 100L);
                    }
                    if ((l = config.getConfigLoc("lobbyLoc")) != null && (w = Bukkit.getWorld((String)config.getLobbyWorldName())) != null) {
                        w.setSpawnLocation(l.getBlockX(), l.getBlockY(), l.getBlockZ());
                    }
                }
            }, 1L);
        }
        BedWars.registerEvents(new EnderPearlLanded(), new QuitAndTeleportListener(), new BreakPlace(), new DamageDeathMove(), new Inventory(), new Interact(), new RefreshGUI(), new HungerWeatherSpawn(), new CmdProcess(), new FireballListener(), new EggBridge(), new SpectatorListeners(), new BaseListener(), new TargetListener(), new LangListener(), new Warnings(this), new ChatAFK(), new GameEndListener(), new DefaultStatsHandler());
        if (config.getBoolean("performance-settings.heal-pool.enable")) {
            BedWars.registerEvents(new HealPoolListner());
        }
        if (BedWars.getServerType() == ServerType.BUNGEE) {
            if (autoscale) {
                ArenaSocket.lobbies.addAll(config.getList("bungee-settings.lobby-sockets"));
                new SendTask();
                BedWars.registerEvents(new AutoscaleListener(), new PrePartyListener(), new JoinListenerBungee());
                Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)this, (Runnable)new LoadedUsersCleaner(), 60L, 60L);
            } else {
                BedWars.registerEvents(new ServerPingListener(), new JoinListenerBungeeLegacy());
            }
        } else if (BedWars.getServerType() == ServerType.MULTIARENA || BedWars.getServerType() == ServerType.SHARED) {
            BedWars.registerEvents(new ArenaSelectorListener(), new BlockStatusListener());
            if (BedWars.getServerType() == ServerType.MULTIARENA) {
                BedWars.registerEvents(new JoinListenerMultiArena());
            } else {
                BedWars.registerEvents(new JoinListenerShared());
            }
        }
        BedWars.registerEvents(new WorldLoadListener());
        if (BedWars.getServerType() != ServerType.BUNGEE || !autoscale) {
            BedWars.registerEvents(new JoinHandlerCommon());
        }
        BedWars.registerEvents(new ChunkLoad());
        BedWars.registerEvents(new InvisibilityPotionListener());
        this.loadArenasAndSigns();
        statsManager = new StatsManager();
        Bukkit.getScheduler().runTaskLater((Plugin)this, () -> {
            if (config.getYml().getBoolean("party-settings.allow-parties")) {
                if (this.getServer().getPluginManager().isPluginEnabled("Parties")) {
                    out.info("Hook into Parties (by AlessioDP) support!");
                    party = new PartiesAdapter();
                } else if (Bukkit.getServer().getPluginManager().isPluginEnabled("PartyAndFriends")) {
                    out.info("Hook into Party and Friends for Spigot (by Simonsator) support!");
                    party = new PAF();
                } else if (Bukkit.getServer().getPluginManager().isPluginEnabled("Spigot-Party-API-PAF")) {
                    out.info("Hook into Spigot Party API for Party and Friends Extended (by Simonsator) support!");
                    party = new PAFBungeecordRedisApi();
                }
                if (party instanceof NoParty) {
                    party = new Internal();
                    out.info("Loading internal Party system. /party");
                }
            } else {
                party = new NoParty();
            }
        }, 10L);
        BedWars.setLevelAdapter(new InternalLevel());
        Bukkit.getScheduler().runTaskTimer((Plugin)this, (Runnable)new Refresh(), 20L, 20L);
        if (config.getBoolean("performance-settings.rotate-generators")) {
            Bukkit.getScheduler().runTaskTimer((Plugin)this, (Runnable)new OneTick(), 120L, 1L);
        }
        nms.registerEntities();
        if (config.getBoolean("database.enable")) {
            Iterator<Language> mySQL = new MySQL();
            long time = System.currentTimeMillis();
            if (!((MySQL)((Object)mySQL)).connect()) {
                this.getLogger().severe("Could not connect to database! Please verify your credentials and make sure that the server IP is whitelisted in MySQL.");
                remoteDatabase = new SQLite();
            } else {
                remoteDatabase = mySQL;
            }
            if (System.currentTimeMillis() - time >= 5000L) {
                this.getLogger().severe("It took " + (System.currentTimeMillis() - time) / 1000L + " ms to establish a database connection!\nUsing this remote connection is not recommended!");
            }
            remoteDatabase.init();
        } else {
            remoteDatabase = new SQLite();
            remoteDatabase.init();
        }
        Bukkit.getScheduler().runTaskLater((Plugin)plugin, () -> {
            if (this.getServer().getPluginManager().getPlugin("Citizens") != null) {
                JoinNPC.setCitizensSupport(true);
                out.info("Hook into Citizens support. /bw npc");
                BedWars.registerEvents(new CitizensListener());
            }
            try {
                JoinNPC.spawnNPCs();
            } catch (Exception e) {
                this.getLogger().severe("Could not spawn CmdJoin NPCs. Make sure you have right version of Citizens for your server!");
                JoinNPC.setCitizensSupport(false);
            }
        }, 5L);
        Language.setupCustomStatsMessages();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            out.info("Hooked into PlaceholderAPI support!");
            new PAPISupport().register();
            SupportPAPI.setSupportPAPI(new SupportPAPI.withPAPI());
        }
        Bukkit.getScheduler().runTask((Plugin)this, () -> {
            block9: {
                if (this.getServer().getPluginManager().getPlugin("Vault") != null) {
                    RegisteredServiceProvider rsp;
                    try {
                        rsp = this.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
                        if (rsp != null) {
                            WithChat.setChat((net.milkbowl.vault.chat.Chat)rsp.getProvider());
                            plugin.getLogger().info("Hooked into vault chat support!");
                            chat = new WithChat();
                        } else {
                            plugin.getLogger().info("Vault found, but no chat provider!");
                            chat = new NoChat();
                        }
                    } catch (Exception var2_2) {
                        chat = new NoChat();
                    }
                    try {
                        BedWars.registerEvents(new MoneyListeners());
                        rsp = this.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
                        if (rsp != null) {
                            WithEconomy.setEconomy((net.milkbowl.vault.economy.Economy)rsp.getProvider());
                            plugin.getLogger().info("Hooked into vault economy support!");
                            economy = new WithEconomy();
                            break block9;
                        }
                        plugin.getLogger().info("Vault found, but no economy provider!");
                        economy = new NoEconomy();
                    } catch (Exception var2_2) {
                        economy = new NoEconomy();
                    }
                } else {
                    chat = new NoChat();
                    economy = new NoEconomy();
                }
            }
        });
        if (config.getBoolean("chat-settings.format")) {
            BedWars.registerEvents(new ChatFormatting());
        }
        nms.registerTntWhitelist((float)config.getDouble("blast-protection.end-stone"), (float)config.getDouble("blast-protection.glass"));
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer("BedWars1058 was RELOADED! (do not reload plugins)");
        }
        Sounds.init();
        shop = new ShopManager();
        for (Language l : Language.getLanguages()) {
            l.setupUnSetCategories();
            Language.addDefaultMessagesCommandItems(l);
        }
        LevelsConfig.init();
        MoneyConfig.init();
        MetricsManager.initService(this);
        if (Bukkit.getPluginManager().getPlugin("VipFeatures") != null) {
            try {
                IVipFeatures vf = (IVipFeatures)Bukkit.getServicesManager().getRegistration(IVipFeatures.class).getProvider();
                vf.registerMiniGame(new VipFeatures((Plugin)this));
                BedWars.registerEvents(new VipListeners(vf));
                out.log(java.util.logging.Level.INFO, "Hook into VipFeatures support.");
            } catch (Exception e) {
                out.warning("Could not load support for VipFeatures.");
            } catch (MiniGameAlreadyRegistered miniGameAlreadyRegistered) {
                miniGameAlreadyRegistered.printStackTrace();
            }
        }
        Bukkit.getScheduler().runTaskLater((Plugin)this, () -> out.info("This server is running in " + String.valueOf((Object)BedWars.getServerType()) + " with auto-scale " + autoscale), 100L);
        UpgradesManager.init();
        if (!SidebarService.init(this)) {
            this.getLogger().severe("SidebarLib by andrei1058 does not support your server version");
            Bukkit.getPluginManager().disablePlugin((Plugin)this);
            return;
        }
        out.info("Initializing SidebarLib by andrei1058");
        HalloweenSpecial.init();
        SpoilPlayerTNTFeature.init();
        this.performDeprecationCheck();
    }

    private boolean handleWorldAdapter() {
        Plugin swmPlugin = Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        if (null == swmPlugin) {
            return false;
        }
        PluginDescriptionFile pluginDescription = swmPlugin.getDescription();
        if (null == pluginDescription) {
            return false;
        }
        String[] versionString = pluginDescription.getVersion().split("\\.");
        try {
            String adapterPath;
            int release;
            int major = Integer.parseInt(versionString[0]);
            int minor = Integer.parseInt(versionString[1]);
            int n = release = versionString.length > 2 ? Integer.parseInt(versionString[2]) : 0;
            if (major == 2 && minor == 2 && release == 1) {
                adapterPath = "com.andrei1058.bedwars.arena.mapreset.slime.SlimeAdapter";
            } else if (major == 2 && minor == 8 && release == 0) {
                adapterPath = "com.andrei1058.bedwars.arena.mapreset.slime.AdvancedSlimeAdapter";
            } else if (major > 2 || major == 2 && minor >= 10) {
                adapterPath = "com.andrei1058.bedwars.arena.mapreset.slime.SlimePaperAdapter";
            } else {
                return false;
            }
            Constructor<?> constructor = Class.forName(adapterPath).getConstructor(Plugin.class);
            this.getLogger().info("Loading restore adapter: " + adapterPath + " ...");
            RestoreAdapter candidate = (RestoreAdapter)constructor.newInstance(new Object[]{this});
            api.setRestoreAdapter(candidate);
            this.getLogger().info("Hook into " + candidate.getDisplayName() + " as restore adapter.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            this.getLogger().info("Something went wrong! Using internal reset adapter...");
            return false;
        }
    }

    private void registerDelayedCommands() {
        if (!nms.isBukkitCommandRegistered("shout")) {
            nms.registerCommand("shout", (Command)new ShoutCommand("shout"));
        }
        nms.registerCommand("rejoin", (Command)new RejoinCommand("rejoin"));
        if (!nms.isBukkitCommandRegistered("leave") || BedWars.getServerType() != ServerType.BUNGEE) {
            nms.registerCommand("leave", (Command)new LeaveCommand("leave"));
        }
        if (BedWars.getServerType() != ServerType.BUNGEE && config.getBoolean("party-settings.enable-party-cmd")) {
            Bukkit.getLogger().info("Registering /party command..");
            nms.registerCommand("party", (Command)new PartyCommand("party"));
        }
    }

    public void onDisable() {
        shuttingDown = true;
        if (!this.serverSoftwareSupport) {
            return;
        }
        if (BedWars.getServerType() == ServerType.BUNGEE) {
            ArenaSocket.disable();
        }
        for (IArena a : new LinkedList<IArena>(Arena.getArenas())) {
            try {
                a.disable();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadArenasAndSigns() {
        api.getRestoreAdapter().convertWorlds();
        File dir = new File(plugin.getDataFolder(), "/Arenas");
        if (dir.exists()) {
            ArrayList<File> files = new ArrayList<File>();
            File[] fls = dir.listFiles();
            for (File fl : Objects.requireNonNull(fls)) {
                if (!fl.isFile() || !fl.getName().endsWith(".yml")) continue;
                files.add(fl);
            }
            if (serverType == ServerType.BUNGEE && !autoscale) {
                if (files.isEmpty()) {
                    this.getLogger().log(java.util.logging.Level.WARNING, "Could not find any arena!");
                    return;
                }
                Random r = new Random();
                int x = r.nextInt(files.size());
                String name = ((File)files.get(x)).getName().replace(".yml", "");
                new Arena(name, null);
            } else {
                for (File file : files) {
                    new Arena(file.getName().replace(".yml", ""), null);
                }
            }
        }
    }

    public static void registerEvents(Listener ... listeners) {
        Arrays.stream(listeners).forEach(l -> plugin.getServer().getPluginManager().registerEvents(l, (Plugin)plugin));
    }

    public static void setDebug(boolean value) {
        debug = value;
    }

    public static void setServerType(ServerType serverType) {
        BedWars.serverType = serverType;
        if (serverType == ServerType.BUNGEE) {
            autoscale = true;
        }
    }

    public static void setAutoscale(boolean autoscale) {
        BedWars.autoscale = autoscale;
    }

    public static void debug(String message) {
        if (debug) {
            plugin.getLogger().info("DEBUG: " + message);
        }
    }

    public static String getForCurrentVersion(String v18, String v12, String v13) {
        switch (BedWars.getServerVersion()) {
            case "v1_8_R3": {
                return v18;
            }
            case "v1_12_R1": {
                return v12;
            }
        }
        return v13;
    }

    public static ServerType getServerType() {
        return serverType;
    }

    public static Party getParty() {
        return party;
    }

    public static Chat getChatSupport() {
        return chat;
    }

    public static Level getLevelSupport() {
        return level;
    }

    public static void setLevelAdapter(Level levelsManager) {
        if (levelsManager instanceof InternalLevel) {
            if (LevelListeners.instance == null) {
                Bukkit.getPluginManager().registerEvents((Listener)new LevelListeners(), (Plugin)plugin);
            }
        } else if (LevelListeners.instance != null) {
            PlayerJoinEvent.getHandlerList().unregister((Listener)LevelListeners.instance);
            PlayerQuitEvent.getHandlerList().unregister((Listener)LevelListeners.instance);
            LevelListeners.instance = null;
        }
        level = levelsManager;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static ConfigManager getGeneratorsCfg() {
        return generators;
    }

    public static void setLobbyWorld(String lobbyWorld) {
        BedWars.lobbyWorld = lobbyWorld;
    }

    public static String getServerVersion() {
        return version;
    }

    public static String getLobbyWorld() {
        return lobbyWorld;
    }

    public static Database getRemoteDatabase() {
        return remoteDatabase;
    }

    public static StatsManager getStatsManager() {
        return statsManager;
    }

    public static com.andrei1058.bedwars.api.BedWars getAPI() {
        return api;
    }

    public static boolean isShuttingDown() {
        return shuttingDown;
    }

    public static void setParty(Party party) {
        BedWars.party = party;
    }

    public void performDeprecationCheck() {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this, () -> {
            if (Arrays.stream(nms.getClass().getAnnotations()).anyMatch(annotation -> annotation instanceof Deprecated)) {
                this.getLogger().warning("Support for " + BedWars.getServerVersion() + " is scheduled for removal. Please consider upgrading your server software to a newer Minecraft version.");
            }
        });
    }

    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new VoidChunkGenerator();
    }

    static {
        isPaper = false;
        party = new NoParty();
        chat = new NoChat();
        version = Bukkit.getServer().getClass().getName().split("\\.")[3];
        lobbyWorld = "";
        shuttingDown = false;
        arenaManager = new ArenaManager();
    }
}

