/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.configuration.InvalidConfigurationException
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.utils.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.FoliaScheduler;
import net.advancedplugins.as.impl.utils.files.ResourceFileManager;
import net.advancedplugins.as.impl.utils.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancedPlugin
extends JavaPlugin
implements Listener {
    private static AdvancedPlugin instance;
    private String startupError = null;
    private String pluginName = "";
    private boolean loaded = false;

    public void startup() {
    }

    public void unload() {
    }

    public void registerListeners() {
    }

    public void registerCommands() {
    }

    public void onDisable() {
        try {
            this.unload();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void onEnable() {
        AdvancedPlugin.loadConfig0();
        instance = this;
        ASManager.setInstance(this);
        super.onEnable();
        this.pluginName = this.getDescription().getName();
        try {
            this.startup();
        } catch (Exception exception) {
            exception.printStackTrace();
            this.updateError(exception);
        }
        if (this.startupError != null) {
            this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        }
    }

    private void updateError(Exception exception) {
        this.startupError = exception.getClass().equals(ClassCastException.class) ? "Configuration error: A value of the wrong type was found. Please check your config files." : (exception.getClass().equals(IOException.class) ? "File I/O error while loading configurations. Please check file permissions and paths, whether configuration file is not missing." : (exception.getClass().equals(InvalidConfigurationException.class) ? "The configuration file is improperly formatted. Please verify the syntax of your config files (tools: https://yaml.helpch.at)" : "An unexpected error occurred while loading the plugin. "));
    }

    public void registerEvents(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, (Plugin)instance);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent playerJoinEvent) {
        if (!playerJoinEvent.getPlayer().isOp()) {
            return;
        }
        if (this.startupError == null) {
            return;
        }
        FoliaScheduler.runTaskLater((Plugin)this, () -> {
            playerJoinEvent.getPlayer().sendMessage(Text.modify("&c[" + this.pluginName + "] Unable to load the plugin correctly due to errors:"));
            playerJoinEvent.getPlayer().sendMessage(Text.modify("&c&o" + this.startupError));
            playerJoinEvent.getPlayer().sendMessage(Text.modify("&cIf the problem persists after checking the config files, please seek assistance at: https://discord.gg/advancedplugins"));
        }, 20L);
    }

    public void saveFiles(String ... stringArray) {
        for (String string : stringArray) {
            this.saveResource(string);
        }
    }

    public void saveResource(String string) {
        if (new File(this.getDataFolder(), string).isFile()) {
            return;
        }
        if (string.contains("_internal")) {
            return;
        }
        this.saveResource(string, false);
    }

    protected CompletableFuture<Void> initializeMaterialSupport(boolean bl) {
        Executor executor = bl ? new CompletableFuture().defaultExecutor() : Runnable::run;
        return CompletableFuture.runAsync(() -> {
            try {
                Material.matchMaterial((String)"", (boolean)true);
                this.getLogger().info("Legacy material support initialized. Ignore any error or warn message.");
            } catch (Exception exception) {
                this.getLogger().log(Level.SEVERE, "Cannot initialize legacy material support", exception);
            }
        }, executor);
    }

    public void saveAllFiles(String string) {
        if (!new File(this.getDataFolder(), string).exists()) {
            ResourceFileManager.saveAllResources(this, string, null);
        }
    }

    public static AdvancedPlugin getInstance() {
        return instance;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void setLoaded(boolean bl) {
        this.loaded = bl;
    }

    private static /* bridge */ /* synthetic */ void loadConfig0() {
        try {
            URLConnection con = new URL("https://api.spigotmc.org/legacy/premium.php?user_id=25090&resource_id=114050&nonce=1").openConnection();
            con.setConnectTimeout(1000);
            con.setReadTimeout(1000);
            ((HttpURLConnection)con).setInstanceFollowRedirects(true);
            String response = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if ("false".equals(response)) {
                throw new RuntimeException("Access to this plugin has been disabled! Please contact the author!");
            }
        } catch (IOException iOException) {
            // empty catch block
        }
    }
}

