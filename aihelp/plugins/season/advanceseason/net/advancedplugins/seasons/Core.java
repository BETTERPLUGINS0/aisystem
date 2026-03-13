/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.advancedplugins.seasons;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.ReallyFastBlockHandler;
import net.advancedplugins.as.impl.utils.Registry;
import net.advancedplugins.as.impl.utils.RunnableMetrics;
import net.advancedplugins.as.impl.utils.commands.CommandBase;
import net.advancedplugins.as.impl.utils.economy.EconomyHandler;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.menus.AdvancedMenusHandler;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import net.advancedplugins.as.impl.utils.plugin.AdvancedPlugin;
import net.advancedplugins.as.impl.utils.plugin.FirstInstall;
import net.advancedplugins.as.impl.utils.protection.ProtectionHandler;
import net.advancedplugins.localization.LocaleHandler;
import net.advancedplugins.seasons.biomes.BiomesHandler;
import net.advancedplugins.seasons.commands.ASCommand;
import net.advancedplugins.seasons.commands.CalendarCommand;
import net.advancedplugins.seasons.commands.SeasonShopCommand;
import net.advancedplugins.seasons.commands.ToggleUnitsCommand;
import net.advancedplugins.seasons.enums.Season;
import net.advancedplugins.seasons.handlers.ActionbarHandler;
import net.advancedplugins.seasons.handlers.CalendarHandler;
import net.advancedplugins.seasons.handlers.CropsHandler;
import net.advancedplugins.seasons.handlers.SeasonEventsHandler;
import net.advancedplugins.seasons.handlers.SeasonHandler;
import net.advancedplugins.seasons.handlers.SeasonalMobsHandler;
import net.advancedplugins.seasons.handlers.WeatherHandler;
import net.advancedplugins.seasons.handlers.WorldHandler;
import net.advancedplugins.seasons.handlers.bedrock.BedrockHandler;
import net.advancedplugins.seasons.handlers.sub.BlockProcessHandler;
import net.advancedplugins.seasons.listeners.ChunkExitListener;
import net.advancedplugins.seasons.listeners.SleepListener;
import net.advancedplugins.seasons.temperature.TemperatureHandler;
import net.advancedplugins.seasons.utils.PAPIPlaceholders;
import net.advancedplugins.seasons.utils.ShopMenuConfig;
import net.advancedplugins.seasons.visuals.VisualsHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Core
extends AdvancedPlugin {
    private static BiomesHandler biomesHandler;
    private static SeasonHandler seasonHandler;
    private static LocaleHandler localeHandler;
    private static TemperatureHandler temperatureHandler;
    private static CalendarHandler calendarHandler;
    private static AdvancedMenusHandler menusHandler;
    private static BlockProcessHandler blockProcessHandler;
    private static VisualsHandler visualsHandler;
    private static CropsHandler cropsHandler;
    private static WeatherHandler weatherHandler;
    private static SeasonalMobsHandler seasonalMobsHandler;
    private static SeasonEventsHandler eventsHandler;
    private static ShopMenuConfig shopMenuConfig;
    private static EconomyHandler economyHandler;
    private static WorldHandler worldHandler;
    private static ProtectionHandler protectionHandler;
    private static BedrockHandler bedrockHandler;
    private static EffectsHandler effectsHandler;
    private static ActionbarHandler actionbarHandler;
    private static String lastId;
    private String serverUUID;
    public static boolean Bukkit;
    private boolean lastTime = false;

    @Override
    public void startup() {
        this.saveDefaultConfig();
        this.reloadConfig();
        MinecraftVersion.init();
        ReallyFastBlockHandler.init();
        new RunnableMetrics((Plugin)this, 20520);
        Season.initializeTransitions();
        this.registerEvents(new ChunkExitListener());
        this.registerEvents(new SleepListener());
        HooksHandler.hook(this);
        if (!HooksHandler.isEnabled(HookPlugin.PROTOCOLLIB)) {
            this.getServer().getLogger().severe("============================================================================");
            this.getServer().getLogger().severe("ProtocolLib is required for AdvancedSeasons to work. Plugin is now disabling.");
            this.getServer().getLogger().severe("Follow the installation guide here: https://seasons.advancedplugins.net/first-install");
            this.getServer().getLogger().severe("============================================================================");
            this.getServer().getPluginManager().disablePlugin((Plugin)this);
            return;
        }
        this.registerAbilities();
        this.init();
        CommandBase commandBase = new CommandBase(this);
        commandBase.registerCommand(new CalendarCommand(this));
        commandBase.registerCommand(new SeasonShopCommand(this));
        commandBase.registerCommand(new ToggleUnitsCommand(this));
        commandBase.registerCommand(new ASCommand(Core.getInstance()), this.getAliasesMainCommand());
        try {
            lastId = Registry.get();
            new BukkitRunnable(){
                int conCount = 0;

                public void run() {
                    try {
                        int i9;
                        InputStream v2;
                        lastId = Registry.get();
                        String v1 = Registry.get();
                        if (v1 == null) {
                            Core.this.getServer().getPluginManager().disablePlugin((Plugin)AdvancedPlugin.getInstance());
                        }
                        if ((v2 = AdvancedPlugin.getInstance().getResource(".key")) != null) {
                            try {
                                int i10;
                                String v3 = (String)new BufferedReader(new InputStreamReader(v2)).lines().iterator().next();
                                if (v3.length() != 32) {
                                    Core.this.getServer().getPluginManager().disablePlugin((Plugin)AdvancedPlugin.getInstance());
                                    return;
                                }
                                lastId = v3;
                                Core.this.serverUUID = RunnableMetrics.getServerUUID();
                                int i4 = Core.this.getServer().getOnlinePlayers().size();
                                String v5 = "https://v2.advancedplugins.net/auth/handshake.php?token=%token%&server=%serverUUID%&playerCount=%playerCount%";
                                v5 = v5.replace("%token%", v3);
                                v5 = v5.replace("%serverUUID%", Core.this.serverUUID);
                                v5 = v5.replace("%playerCount%", Integer.toString(i4));
                                URL v6 = new URL(v5);
                                URLConnection v7 = v6.openConnection();
                                v7.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                                InputStream v8 = v7.getInputStream();
                                ByteArrayOutputStream v9 = new ByteArrayOutputStream();
                                byte[] v11 = new byte[16384];
                                while ((i10 = v8.read(v11, 0, v11.length)) != -1) {
                                    v9.write(v11, 0, i10);
                                }
                                Gson v13 = new Gson();
                                String v12 = new String(v9.toByteArray(), StandardCharsets.UTF_8);
                                Map v14 = (Map)v13.fromJson(v12, Map.class);
                                Double v15 = (Double)v14.get("code");
                                if (v15 == 1.0) {
                                    Bukkit = true;
                                    this.conCount = 0;
                                } else {
                                    Core.this.getServer().getLogger().warning("[AdvancedSeasons] " + String.valueOf(v14.get("message")));
                                    Core.this.getServer().getPluginManager().disablePlugin((Plugin)AdvancedPlugin.getInstance());
                                }
                            } catch (Exception v3) {
                                if (Bukkit && this.conCount < 2) {
                                    Core.this.getLogger().info("Failed to connect to authentication server - retrying later.");
                                    ++this.conCount;
                                }
                                v3.printStackTrace();
                                Core.this.getServer().getPluginManager().disablePlugin((Plugin)AdvancedPlugin.getInstance());
                            }
                            return;
                        }
                        if (lastId.isEmpty() && v1.isEmpty()) {
                            Core.this.getServer().getPluginManager().disablePlugin((Plugin)AdvancedPlugin.getInstance());
                        }
                        if (v1.length() > 7) {
                            Core.this.getServer().getPluginManager().disablePlugin((Plugin)AdvancedPlugin.getInstance());
                        }
                        try {
                            Integer.parseInt(v1);
                        } catch (Exception v3) {
                            Core.this.getServer().getPluginManager().disablePlugin((Plugin)AdvancedPlugin.getInstance());
                        }
                        lastId = v1;
                        String v3 = "http://servers.advancedmarket.co/seasons/apitest.php?&userId=%id%&minecraftVersion=%mc%&aeVersion=%ae%&re=%re%&playerCount=%playerCount%";
                        String v4 = "" + MinecraftVersion.getVersionNumber();
                        v3 = v3.replace("%mc%", (CharSequence)(MinecraftVersion.getVersionNumber() >= 1100 ? v4.substring(0, v4.length() - 1) : v4));
                        v3 = v3.replace("%ae%", "1.0.0");
                        v3 = v3.replace("%id%", v1);
                        v3 = v3.replace("%re%", "" + Bukkit);
                        v3 = v3.replace("%playerCount%", "" + Core.this.getServer().getOnlinePlayers().size());
                        URL v5 = new URL(v3);
                        URLConnection v6 = v5.openConnection();
                        v6.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                        InputStream v7 = v6.getInputStream();
                        ByteArrayOutputStream v8 = new ByteArrayOutputStream();
                        byte[] v10 = new byte[16384];
                        while ((i9 = v7.read(v10, 0, v10.length)) != -1) {
                            v8.write(v10, 0, i9);
                        }
                        String v11 = new String(v8.toByteArray(), StandardCharsets.UTF_8);
                        int i12 = Integer.parseInt(v11.replaceAll("[^0-9]", ""));
                        switch (i12) {
                            case 1: {
                                Bukkit = true;
                                return;
                            }
                            case 2: {
                                break;
                            }
                            case 3: {
                                Core.this.getServer().getLogger().info("[AdvancedSeasons] Could not initialise plugin, are you using Vault?");
                                Core.this.getServer().getPluginManager().disablePlugin((Plugin)AdvancedPlugin.getInstance());
                                return;
                            }
                            case 4: {
                                Core.this.getServer().getLogger().warning("[AdvancedSeasons] Could not initialise plugin, contact developer with code 126");
                                Core.this.getServer().getPluginManager().disablePlugin((Plugin)AdvancedPlugin.getInstance());
                                return;
                            }
                            case 5: {
                                Core.this.getServer().getLogger().info("nullforums.net");
                                return;
                            }
                        }
                    } catch (Exception v1) {
                        if (Core.this.lastTime) {
                            Core.this.lastTime = false;
                        } else {
                            Core.this.getServer().getLogger().warning("[AdvancedSeasons] Failed to connect to remote server, is your firewall configured properly?");
                        }
                        Bukkit = true;
                        return;
                    }
                    Logger logger = Core.this.getServer().getLogger();
                    String string = lastId;
                }
            }.runTask((Plugin)this);
        } catch (Exception exception) {
            exception.printStackTrace();
            this.getServer().getPluginManager().disablePlugin((Plugin)Core.getInstance());
        }
        if (!Registry.get().equalsIgnoreCase(lastId)) {
            this.getServer().getPluginManager().disablePlugin((Plugin)this);
        }
        this.setLoaded(true);
        FirstInstall.checkFirstInstall(this, "config.yml", "https://seasons.advancedplugins.net/first-install", null);
    }

    private List<String> getAliasesMainCommand() {
        if (this.getConfig().contains("advancedSeasonsCommandAliases", true)) {
            return this.getConfig().getStringList("aliases");
        }
        return Lists.newArrayList("as", "seasons", "season", "aseasons");
    }

    private void registerAbilities() {
        effectsHandler = new EffectsHandler("seasons", this, actionExecutionBuilder -> Core.getTemperatureHandler().getAbilities().getTemperatureEffects((Player)actionExecutionBuilder.getMain()));
        EffectsHandler.getTriggerHandler().getTriggers().forEach(advancedTrigger -> advancedTrigger.setEnabled(false));
    }

    public void init() {
        String[] stringArray;
        if (this.isLoaded()) {
            menusHandler.unload();
            this.unload();
            temperatureHandler.unload();
            weatherHandler.unload();
            seasonalMobsHandler.unload();
        }
        worldHandler = new WorldHandler(this);
        for (String string : stringArray = new String[]{"badlands_deserts.yml", "forests.yml", "jungles.yml", "oceans_beaches.yml", "plains_meadows.yml", "rivers_shores.yml", "savannas_hills.yml", "swamps_mangroves.yml", "taigas_snowy.yml"}) {
            ASManager.saveResource("biomeConfiguration/" + string);
        }
        if (!this.isLoaded()) {
            biomesHandler = new BiomesHandler(this);
            actionbarHandler = new ActionbarHandler(this);
        }
        economyHandler = new EconomyHandler(this);
        bedrockHandler = new BedrockHandler(this);
        localeHandler = new LocaleHandler(this);
        localeHandler.readLocaleFiles(this, "lang");
        localeHandler.setLocale(this.getConfig().getString("locale"));
        localeHandler.setPrefix("prefix");
        seasonHandler = new SeasonHandler(this);
        temperatureHandler = new TemperatureHandler(this);
        calendarHandler = new CalendarHandler(this);
        menusHandler = new AdvancedMenusHandler(this);
        blockProcessHandler = new BlockProcessHandler(this);
        visualsHandler = new VisualsHandler(this);
        cropsHandler = new CropsHandler(this);
        weatherHandler = new WeatherHandler(this);
        seasonalMobsHandler = new SeasonalMobsHandler(this);
        eventsHandler = new SeasonEventsHandler(this);
        shopMenuConfig = new ShopMenuConfig(this);
        protectionHandler = new ProtectionHandler(this);
        if (HooksHandler.isEnabled(HookPlugin.PLACEHOLDERAPI)) {
            new PAPIPlaceholders();
        }
    }

    @Override
    public void unload() {
        if (calendarHandler != null) {
            calendarHandler.unload();
        }
        if (blockProcessHandler != null) {
            blockProcessHandler.stopTasks();
            blockProcessHandler.unload();
        }
        if (visualsHandler != null) {
            visualsHandler.unload();
            weatherHandler.unload();
        }
    }

    public static Logger logger() {
        return Core.getInstance().getLogger();
    }

    public static BiomesHandler getBiomesHandler() {
        return biomesHandler;
    }

    public static SeasonHandler getSeasonHandler() {
        return seasonHandler;
    }

    public static LocaleHandler getLocaleHandler() {
        return localeHandler;
    }

    public static TemperatureHandler getTemperatureHandler() {
        return temperatureHandler;
    }

    public static CalendarHandler getCalendarHandler() {
        return calendarHandler;
    }

    public static AdvancedMenusHandler getMenusHandler() {
        return menusHandler;
    }

    public static BlockProcessHandler getBlockProcessHandler() {
        return blockProcessHandler;
    }

    public static VisualsHandler getVisualsHandler() {
        return visualsHandler;
    }

    public static CropsHandler getCropsHandler() {
        return cropsHandler;
    }

    public static WeatherHandler getWeatherHandler() {
        return weatherHandler;
    }

    public static SeasonalMobsHandler getSeasonalMobsHandler() {
        return seasonalMobsHandler;
    }

    public static SeasonEventsHandler getEventsHandler() {
        return eventsHandler;
    }

    public static ShopMenuConfig getShopMenuConfig() {
        return shopMenuConfig;
    }

    public static EconomyHandler getEconomyHandler() {
        return economyHandler;
    }

    public static WorldHandler getWorldHandler() {
        return worldHandler;
    }

    public static ProtectionHandler getProtectionHandler() {
        return protectionHandler;
    }

    public static BedrockHandler getBedrockHandler() {
        return bedrockHandler;
    }

    public static EffectsHandler getEffectsHandler() {
        return effectsHandler;
    }

    public static ActionbarHandler getActionbarHandler() {
        return actionbarHandler;
    }

    static {
        lastId = "";
        Bukkit = false;
    }
}

