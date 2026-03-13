/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.SimpleCommandMap
 *  org.bukkit.event.Listener
 *  org.bukkit.permissions.Permission
 *  org.bukkit.permissions.PermissionDefault
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.magmaguy.magmacore;

import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.CommandManager;
import com.magmaguy.magmacore.command.LogifyCommand;
import com.magmaguy.magmacore.command.NightbreakLoginCommand;
import com.magmaguy.magmacore.dlc.ConfigurationImporter;
import com.magmaguy.magmacore.initialization.PluginInitializationConfig;
import com.magmaguy.magmacore.initialization.PluginInitializationContext;
import com.magmaguy.magmacore.initialization.PluginInitializationManager;
import com.magmaguy.magmacore.initialization.PluginInitializationState;
import com.magmaguy.magmacore.instance.InstanceProtector;
import com.magmaguy.magmacore.instance.MatchInstance;
import com.magmaguy.magmacore.instance.MatchInstanceWorld;
import com.magmaguy.magmacore.instance.MatchPlayer;
import com.magmaguy.magmacore.menus.AdvancedMenuHandler;
import com.magmaguy.magmacore.menus.SetupMenu;
import com.magmaguy.magmacore.nightbreak.NightbreakAccount;
import com.magmaguy.magmacore.thirdparty.CustomBiomeCompatibility;
import com.magmaguy.magmacore.util.Logger;
import com.magmaguy.magmacore.util.VersionChecker;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class MagmaCore {
    private static MagmaCore instance;
    private static final Map<String, JavaPlugin> registeredPlugins;
    private static final Set<String> listenerRegistrations;
    private final JavaPlugin requestingPlugin;

    private MagmaCore(JavaPlugin requestingPlugin) {
        instance = this;
        this.requestingPlugin = requestingPlugin;
        new AdvancedMenuHandler();
        CustomBiomeCompatibility.initializeMappings();
        Logger.info("MagmaCore v1.13-SNAPSHOT initialized!");
        instance.registerLogify();
        instance.registerNightbreakLogin();
        NightbreakAccount.initialize(requestingPlugin);
    }

    public static void checkVersionUpdate(String resourceID, String downloadURL) {
        VersionChecker.checkPluginVersion(resourceID);
        VersionChecker.VersionCheckerEvents.setDownloadURL(downloadURL);
        Bukkit.getPluginManager().registerEvents((Listener)new VersionChecker.VersionCheckerEvents(), (Plugin)MagmaCore.instance.requestingPlugin);
    }

    public static void onEnable() {
        MagmaCore.onEnable(MagmaCore.instance.requestingPlugin);
    }

    public static void onEnable(JavaPlugin plugin) {
        if (plugin == null) {
            return;
        }
        if (!listenerRegistrations.add(plugin.getName())) {
            return;
        }
        Bukkit.getPluginManager().registerEvents((Listener)new SetupMenu.SetupMenuListeners(), (Plugin)plugin);
        Bukkit.getPluginManager().registerEvents((Listener)new AdvancedMenuHandler.AdvancedMenuListeners(), (Plugin)plugin);
    }

    public static void enableMatchSystem() {
        MagmaCore.enableMatchSystem(MagmaCore.instance.requestingPlugin);
    }

    public static void enableMatchSystem(JavaPlugin plugin) {
        Logger.info("Enabling match system...");
        Bukkit.getPluginManager().registerEvents((Listener)new InstanceProtector(), (Plugin)plugin);
        Bukkit.getPluginManager().registerEvents((Listener)new MatchPlayer.MatchPlayerEvents(), (Plugin)plugin);
        Bukkit.getPluginManager().registerEvents((Listener)new MatchInstance.MatchInstanceEvents(), (Plugin)plugin);
        Bukkit.getPluginManager().registerEvents((Listener)new MatchInstanceWorld.MatchInstanceWorldEvents(), (Plugin)plugin);
    }

    public static MagmaCore createInstance(JavaPlugin requestingPlugin) {
        registeredPlugins.put(requestingPlugin.getName(), requestingPlugin);
        if (instance == null) {
            return new MagmaCore(requestingPlugin);
        }
        NightbreakAccount.initialize(requestingPlugin);
        return instance;
    }

    public static void shutdown() {
        CommandManager.shutdown();
        CustomBiomeCompatibility.shutdown();
        MatchInstance.shutdown();
    }

    public static void shutdown(JavaPlugin plugin) {
        if (plugin != null) {
            registeredPlugins.remove(plugin.getName());
            listenerRegistrations.remove(plugin.getName());
            PluginInitializationManager.shutdown(plugin);
        }
        MagmaCore.shutdown();
    }

    public static void initializeImporter() {
        MagmaCore.initializeImporter(MagmaCore.instance.requestingPlugin);
    }

    public static void initializeImporter(JavaPlugin plugin) {
        if (instance == null) {
            Bukkit.getLogger().warning("Attempted to initialize importer without first instantiating MagmaCore!");
            return;
        }
        new ConfigurationImporter(plugin);
    }

    public static JavaPlugin getRegisteredPlugin(String pluginName) {
        return registeredPlugins.get(pluginName);
    }

    public static Collection<JavaPlugin> getRegisteredPlugins() {
        return Collections.unmodifiableCollection(registeredPlugins.values());
    }

    public static void startInitialization(JavaPlugin plugin, PluginInitializationConfig config, Consumer<PluginInitializationContext> asyncInitialization, Consumer<PluginInitializationContext> syncInitialization, Runnable onSuccess, Consumer<Throwable> onFailure) {
        PluginInitializationManager.run(plugin, config, asyncInitialization, syncInitialization, onSuccess, onFailure);
    }

    public static PluginInitializationState getInitializationState(String pluginName) {
        return PluginInitializationManager.getState(pluginName);
    }

    public static boolean isPluginReady(String pluginName) {
        return PluginInitializationManager.isPluginReady(pluginName);
    }

    public static void requestInitializationShutdown(JavaPlugin plugin) {
        PluginInitializationManager.requestShutdown(plugin);
    }

    private void registerLogify() {
        SimpleCommandMap commandMap = null;
        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            commandMap = (SimpleCommandMap)f.get(Bukkit.getServer());
        } catch (ReflectiveOperationException e) {
            this.requestingPlugin.getLogger().warning("Couldn\u2019t access CommandMap: " + e.getMessage());
            return;
        }
        if (commandMap.getCommand("logify") != null) {
            this.requestingPlugin.getLogger().info("/logify is already registered, skipping.");
            return;
        }
        if (Bukkit.getPluginManager().getPermission("logify.*") == null) {
            Permission perm = new Permission("logify.*", "Lets admins run the /logify command, which sends the current latest server log to mclo.gs.", PermissionDefault.OP);
            Bukkit.getPluginManager().addPermission(perm);
        }
        commandMap.register(this.requestingPlugin.getName(), AdvancedCommand.toBukkitCommand(MagmaCore.instance.requestingPlugin, new LogifyCommand(MagmaCore.instance.requestingPlugin), "logify", new ArrayList<String>()));
        Command wrapper = AdvancedCommand.toBukkitCommand(this.requestingPlugin, new LogifyCommand(this.requestingPlugin), "logify", List.of());
        commandMap.register(this.requestingPlugin.getName(), wrapper);
        Logger.info("Registered /logify command");
    }

    private void registerNightbreakLogin() {
        SimpleCommandMap commandMap = null;
        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            commandMap = (SimpleCommandMap)f.get(Bukkit.getServer());
        } catch (ReflectiveOperationException e) {
            this.requestingPlugin.getLogger().warning("Couldn't access CommandMap: " + e.getMessage());
            return;
        }
        if (commandMap.getCommand("nightbreaklogin") != null) {
            this.requestingPlugin.getLogger().info("/nightbreaklogin is already registered, skipping.");
            return;
        }
        if (Bukkit.getPluginManager().getPermission("nightbreak.login") == null) {
            Permission perm = new Permission("nightbreak.login", "Lets admins register their Nightbreak account token for DLC access.", PermissionDefault.OP);
            Bukkit.getPluginManager().addPermission(perm);
        }
        Command wrapper = AdvancedCommand.toBukkitCommand(this.requestingPlugin, new NightbreakLoginCommand(this.requestingPlugin), "nightbreaklogin", List.of((Object)"nightbreaklogin"));
        commandMap.register(this.requestingPlugin.getName(), wrapper);
        Logger.info("Registered /nightbreaklogin command");
    }

    @Generated
    public static MagmaCore getInstance() {
        return instance;
    }

    @Generated
    public JavaPlugin getRequestingPlugin() {
        return this.requestingPlugin;
    }

    static {
        registeredPlugins = new HashMap<String, JavaPlugin>();
        listenerRegistrations = new HashSet<String>();
    }
}

