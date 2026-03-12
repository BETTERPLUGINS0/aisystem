/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  java.net.http.HttpClient
 *  java.net.http.HttpRequest
 *  java.net.http.HttpResponse
 *  java.net.http.HttpResponse$BodyHandlers
 *  net.kyori.adventure.text.Component
 *  net.kyori.adventure.text.TextComponent
 *  net.kyori.adventure.text.event.ClickEvent
 *  net.kyori.adventure.text.event.HoverEvent
 *  net.kyori.adventure.text.event.HoverEventSource
 *  net.kyori.adventure.text.format.NamedTextColor
 *  net.kyori.adventure.text.format.TextColor
 *  org.bukkit.Bukkit
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package Blizzard1238562.simpleTPA;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SimpleTPA
extends JavaPlugin
implements Listener {
    private final Map<UUID, UUID> tpaRequests = new HashMap<UUID, UUID>();
    private final Map<UUID, Long> cooldowns = new HashMap<UUID, Long>();
    private final Set<UUID> tpaDisabledPlayers = new HashSet<UUID>();
    private static final long UPDATE_INTERVAL_TICKS = 864000L;
    private FileConfiguration config;
    private int tpaCooldown;
    private int tpaRequestTimeout;
    private boolean checkForUpdates;
    private String modrinthProjectSlug;
    private BukkitTask updateTask;
    private volatile boolean updateAvailable = false;
    private volatile String latestVersion = "";
    private volatile String latestVersionUrl = "https://modrinth.com/plugin/simpletpaplugin";

    public void onEnable() {
        File configFile = new File(this.getDataFolder(), "config.yml");
        if (configFile.exists()) {
            configFile.delete();
            this.getLogger().info("Existing config.yml overwritten with default values.");
        }
        this.saveDefaultConfig();
        this.config = this.getConfig();
        this.tpaCooldown = this.config.getInt("settings.tpa_cooldown", 30);
        this.tpaRequestTimeout = this.config.getInt("settings.tpa_request_timeout", 60);
        this.checkForUpdates = this.config.getBoolean("settings.check_for_updates", true);
        this.modrinthProjectSlug = this.config.getString("settings.modrinth_project_slug", "simpletpaplugin");
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.startUpdateChecker();
        this.getLogger().info("SimpleTPA Activated!");
    }

    public void onDisable() {
        if (this.updateTask != null) {
            this.updateTask.cancel();
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String commandName;
        switch (commandName = cmd.getName().toLowerCase()) {
            case "tpa": {
                if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
                    if (!sender.hasPermission("tpa.command.version")) {
                        sender.sendMessage(this.getMessage("no_permission"));
                        return true;
                    }
                    return this.handleTpaVersion(sender);
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage(this.getMessage("player_not_online"));
                    return false;
                }
                Player player = (Player)sender;
                if (!player.hasPermission("tpa.command.tpa")) {
                    player.sendMessage(this.getMessage("no_permission"));
                    return true;
                }
                return this.handleTpa(player, args);
            }
            case "tpaccept": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(this.getMessage("player_not_online"));
                    return false;
                }
                Player player = (Player)sender;
                if (!player.hasPermission("tpa.command.tpaccept")) {
                    player.sendMessage(this.getMessage("no_permission"));
                    return true;
                }
                return this.handleTpAccept(player);
            }
            case "tpdeny": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(this.getMessage("player_not_online"));
                    return false;
                }
                Player player = (Player)sender;
                if (!player.hasPermission("tpa.command.tpdeny")) {
                    player.sendMessage(this.getMessage("no_permission"));
                    return true;
                }
                return this.handleTpDeny(player);
            }
            case "tpacancel": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(this.getMessage("player_not_online"));
                    return false;
                }
                Player player = (Player)sender;
                if (!player.hasPermission("tpa.command.tpacancel")) {
                    player.sendMessage(this.getMessage("no_permission"));
                    return true;
                }
                return this.handleTpCancel(player);
            }
            case "tpreload": {
                return this.handleTpReload(sender);
            }
            case "tpatoggle": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(this.getMessage("player_not_online"));
                    return false;
                }
                Player player = (Player)sender;
                if (!player.hasPermission("tpa.command.tpatoggle")) {
                    player.sendMessage(this.getMessage("no_permission"));
                    return true;
                }
                return this.handleTpaToggle(player);
            }
        }
        return false;
    }

    private boolean handleTpaVersion(CommandSender sender) {
        String currentVersion = this.getDescription().getVersion();
        String latest = this.latestVersion.isEmpty() ? currentVersion : this.latestVersion;
        String url = this.latestVersionUrl;
        String message = this.getMessage("version_info").replace("%current%", currentVersion).replace("%latest%", latest).replace("%url%", url);
        sender.sendMessage(message);
        return true;
    }

    private boolean handleTpReload(CommandSender sender) {
        if (!sender.hasPermission("tpa.reload")) {
            sender.sendMessage(this.getMessage("no_permission"));
            return false;
        }
        this.reloadConfig();
        this.config = this.getConfig();
        this.tpaCooldown = this.config.getInt("settings.tpa_cooldown", 30);
        this.tpaRequestTimeout = this.config.getInt("settings.tpa_request_timeout", 60);
        this.checkForUpdates = this.config.getBoolean("settings.check_for_updates", true);
        this.modrinthProjectSlug = this.config.getString("settings.modrinth_project_slug", "simpletpaplugin");
        this.startUpdateChecker();
        sender.sendMessage(this.getMessage("config_reloaded"));
        return true;
    }

    private boolean handleTpCancel(Player player) {
        if (!this.tpaRequests.containsKey(player.getUniqueId())) {
            player.sendMessage(this.getMessage("tpa_no_request"));
            return false;
        }
        UUID targetUuid = this.tpaRequests.remove(player.getUniqueId());
        Player target = Bukkit.getPlayer((UUID)targetUuid);
        player.sendMessage(this.getMessage("tpa_cancel_success"));
        this.playSound(player, "tpa_deny");
        if (target != null && target.isOnline()) {
            target.sendMessage(this.getMessage("tpa_cancel_notify").replace("%player%", player.getName()));
        }
        return true;
    }

    private Component createClickableButton(String type) {
        String textKey = type + "_text";
        String hoverKey = type + "_hover";
        String commandKey = type + "_command";
        String colorKey = type + "_color";
        String text = this.config.getString("clickable_messages." + textKey, "[Button]");
        String hover = this.config.getString("clickable_messages." + hoverKey, "Klicken, um die Aktion auszuf\u00fchren.");
        String command = this.config.getString("clickable_messages." + commandKey, "/help");
        NamedTextColor color = this.getColor(this.config.getString("clickable_messages." + colorKey, "WHITE"));
        return ((TextComponent)((TextComponent)Component.text((String)text).color((TextColor)color)).clickEvent(ClickEvent.runCommand((String)command))).hoverEvent((HoverEventSource)HoverEvent.showText((Component)Component.text((String)hover)));
    }

    private NamedTextColor getColor(String colorName) {
        try {
            return (NamedTextColor)NamedTextColor.NAMES.value((Object)colorName.toLowerCase());
        } catch (Exception e) {
            return NamedTextColor.WHITE;
        }
    }

    private boolean handleTpa(final Player player, String[] args) {
        if (args.length != 1) {
            player.sendMessage(this.getMessage("wrong_usage").replace("%command%", "tpa"));
            return false;
        }
        long currentTime = System.currentTimeMillis();
        if (this.cooldowns.containsKey(player.getUniqueId()) && (currentTime - this.cooldowns.get(player.getUniqueId())) / 1000L < (long)this.tpaCooldown) {
            long remaining = (long)this.tpaCooldown - (currentTime - this.cooldowns.get(player.getUniqueId())) / 1000L;
            player.sendMessage(this.getMessage("tpa_cooldown").replace("%seconds%", String.valueOf(remaining)));
            return false;
        }
        final Player target = Bukkit.getPlayer((String)args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(this.getMessage("player_not_online"));
            return false;
        }
        if (this.tpaDisabledPlayers.contains(target.getUniqueId())) {
            player.sendMessage(this.getMessage("tpa_target_not_accepting").replace("%target%", target.getName()));
            return false;
        }
        if (this.tpaRequests.containsKey(player.getUniqueId())) {
            player.sendMessage(this.getMessage("tpa_request_exists"));
            return false;
        }
        this.tpaRequests.put(player.getUniqueId(), target.getUniqueId());
        this.cooldowns.put(player.getUniqueId(), currentTime);
        player.sendMessage(this.getMessage("tpa_request_sent").replace("%target%", target.getName()));
        Component acceptButton = this.createClickableButton("accept");
        Component denyButton = this.createClickableButton("deny");
        target.sendMessage(((TextComponent)((TextComponent)((TextComponent)Component.text((String)(this.getMessage("tpa_request_received").replace("%player%", player.getName()) + " ")).color((TextColor)NamedTextColor.YELLOW)).append(acceptButton)).append((Component)Component.text((String)" "))).append(denyButton));
        this.playSound(player, "tpa_request_sent");
        this.playSound(target, "tpa_request_received");
        new BukkitRunnable(){

            public void run() {
                if (SimpleTPA.this.tpaRequests.containsKey(player.getUniqueId()) && SimpleTPA.this.tpaRequests.get(player.getUniqueId()).equals(target.getUniqueId())) {
                    SimpleTPA.this.tpaRequests.remove(player.getUniqueId());
                    player.sendMessage(SimpleTPA.this.getMessage("tpa_request_expired_sender").replace("%target%", target.getName()));
                    target.sendMessage(SimpleTPA.this.getMessage("tpa_request_expired_receiver").replace("%player%", player.getName()));
                    SimpleTPA.this.playSound(player, "tpa_expired");
                    SimpleTPA.this.playSound(target, "tpa_expired");
                }
            }
        }.runTaskLater((Plugin)this, (long)this.tpaRequestTimeout * 20L);
        return true;
    }

    private boolean handleTpAccept(Player player) {
        UUID requesterUuid = this.tpaRequests.entrySet().stream().filter(entry -> ((UUID)entry.getValue()).equals(player.getUniqueId())).map(Map.Entry::getKey).findFirst().orElse(null);
        if (requesterUuid == null) {
            player.sendMessage(this.getMessage("tpa_no_request"));
            return false;
        }
        Player requester = Bukkit.getPlayer((UUID)requesterUuid);
        if (requester == null || !requester.isOnline()) {
            player.sendMessage(this.getMessage("player_not_online"));
            this.tpaRequests.remove(requesterUuid);
            return false;
        }
        requester.teleport((Entity)player);
        player.sendMessage(this.getMessage("tpa_accept_success").replace("%player%", requester.getName()));
        requester.sendMessage(this.getMessage("tpa_accept_teleport").replace("%player%", player.getName()));
        this.playSound(player, "tpa_accept");
        this.playSound(requester, "tpa_accept");
        this.tpaRequests.remove(requesterUuid);
        return true;
    }

    private boolean handleTpDeny(Player player) {
        UUID requesterUuid = this.tpaRequests.entrySet().stream().filter(entry -> ((UUID)entry.getValue()).equals(player.getUniqueId())).map(Map.Entry::getKey).findFirst().orElse(null);
        if (requesterUuid == null) {
            player.sendMessage(this.getMessage("tpa_no_request"));
            return false;
        }
        Player requester = Bukkit.getPlayer((UUID)requesterUuid);
        if (requester != null && requester.isOnline()) {
            requester.sendMessage(this.getMessage("tpa_deny_success").replace("%player%", player.getName()));
            this.playSound(requester, "tpa_deny");
        }
        player.sendMessage(this.getMessage("tpa_deny_success").replace("%player%", requester != null ? requester.getName() : "unbekannt"));
        this.playSound(player, "tpa_deny");
        this.tpaRequests.remove(requesterUuid);
        return true;
    }

    private boolean handleTpaToggle(Player player) {
        UUID uuid = player.getUniqueId();
        if (this.tpaDisabledPlayers.contains(uuid)) {
            this.tpaDisabledPlayers.remove(uuid);
            player.sendMessage(this.getMessage("tpa_toggle_disabled"));
            this.playSound(player, "tpa_toggle_disabled");
            return true;
        }
        this.tpaDisabledPlayers.add(uuid);
        player.sendMessage(this.getMessage("tpa_toggle_enabled"));
        this.playSound(player, "tpa_toggle_enabled");
        return true;
    }

    private void playSound(Player player, String soundKey) {
        String soundName = this.config.getString("sounds." + soundKey, "");
        if (!soundName.isEmpty()) {
            try {
                Sound sound = Sound.valueOf((String)soundName);
                player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
            } catch (IllegalArgumentException e) {
                this.getLogger().warning("Invalid Sound: " + soundName);
            }
        }
    }

    private String getMessage(String key) {
        return this.config.getString("messages." + key, "Missing Nachricht: " + key);
    }

    private void startUpdateChecker() {
        if (this.updateTask != null) {
            this.updateTask.cancel();
            this.updateTask = null;
        }
        if (!this.checkForUpdates) {
            this.updateAvailable = false;
            return;
        }
        this.latestVersionUrl = "https://modrinth.com/plugin/" + this.modrinthProjectSlug;
        this.updateTask = Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)this, this::checkForUpdates, 0L, 864000L);
    }

    private void checkForUpdates() {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.modrinth.com/v2/project/" + this.modrinthProjectSlug + "/version")).timeout(Duration.ofSeconds(10L)).GET().build();
            HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                this.getLogger().warning("Update check failed: HTTP " + response.statusCode());
                return;
            }
            JsonArray versions = JsonParser.parseString((String)response.body()).getAsJsonArray();
            if (versions.size() == 0) {
                return;
            }
            JsonObject latest = versions.get(0).getAsJsonObject();
            String remoteVersion = latest.get("version_number").getAsString();
            String projectUrl = "https://modrinth.com/plugin/" + this.modrinthProjectSlug;
            String currentVersion = this.getDescription().getVersion();
            if (!remoteVersion.equalsIgnoreCase(currentVersion)) {
                this.latestVersion = remoteVersion;
                this.latestVersionUrl = projectUrl;
                this.updateAvailable = true;
                this.notifyConsoleOfUpdate();
            } else {
                this.updateAvailable = false;
            }
        } catch (Exception e) {
            this.getLogger().warning("Failed to check for updates: " + e.getMessage());
        }
    }

    private void notifyConsoleOfUpdate() {
        Bukkit.getScheduler().runTask((Plugin)this, () -> {
            String message = this.getMessage("update_available_console").replace("%version%", this.latestVersion).replace("%url%", this.latestVersionUrl);
            this.getLogger().warning(message);
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!this.updateAvailable) {
            return;
        }
        Player player = event.getPlayer();
        if (player.isOp()) {
            String message = this.getMessage("update_available_player").replace("%version%", this.latestVersion).replace("%url%", this.latestVersionUrl);
            player.sendMessage(message);
        }
    }
}

