/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.configuration;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.stats.DefaultStatistics;
import com.andrei1058.bedwars.api.configuration.ConfigManager;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.arena.Misc;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class MainConfig
extends ConfigManager {
    public MainConfig(Plugin plugin, String name) {
        super(plugin, name, BedWars.plugin.getDataFolder().getPath());
        Language def;
        File[] new_path;
        YamlConfiguration yml = this.getYml();
        yml.options().header(plugin.getDescription().getName() + " by andrei1058. https://www.spigotmc.org/members/39904/\nDocumentation here: https://gitlab.com/andrei1058/BedWars1058/wikis/home\n");
        yml.addDefault("serverType", (Object)"MULTIARENA");
        yml.addDefault("language", (Object)"en");
        yml.addDefault("disabled-languages", Collections.singletonList("your language iso here"));
        yml.addDefault("storeLink", (Object)"https://www.spigotmc.org/resources/authors/39904/");
        yml.addDefault("lobbyServer", (Object)"hub");
        yml.addDefault("enable-halloween-feature", (Object)true);
        yml.addDefault("chat-settings.global", yml.get("globalChat", (Object)false));
        yml.addDefault("chat-settings.format", yml.get("formatChat", (Object)true));
        yml.addDefault("debug", (Object)false);
        yml.addDefault("mark-leave-as-abandon", (Object)false);
        yml.addDefault("party-settings.enable-party-cmd", (Object)true);
        yml.addDefault("party-settings.allow-parties", (Object)true);
        yml.addDefault("party-settings.alessioDP-choose-arena-rank", (Object)10);
        yml.addDefault("scoreboard-settings.sidebar.enable-lobby-sidebar", (Object)true);
        yml.addDefault("scoreboard-settings.sidebar.enable-game-sidebar", (Object)true);
        yml.addDefault("scoreboard-settings.sidebar.title-refresh-interval", (Object)4);
        yml.addDefault("scoreboard-settings.sidebar.placeholders-refresh-interval", (Object)20);
        yml.addDefault("scoreboard-settings.player-list.format-lobby-list", (Object)false);
        yml.addDefault("scoreboard-settings.player-list.format-waiting-list", (Object)false);
        yml.addDefault("scoreboard-settings.player-list.format-starting-list", (Object)false);
        yml.addDefault("scoreboard-settings.player-list.format-playing-list", (Object)true);
        yml.addDefault("scoreboard-settings.player-list.format-restarting-list", (Object)true);
        yml.addDefault("scoreboard-settings.player-list.names-refresh-interval", (Object)1200);
        yml.addDefault("scoreboard-settings.health.enable", (Object)true);
        yml.addDefault("scoreboard-settings.health.display-in-tab", (Object)true);
        yml.addDefault("scoreboard-settings.health.animation-refresh-interval", (Object)300);
        yml.addDefault("scoreboard-settings.tab-header-footer.enable", (Object)true);
        yml.addDefault("scoreboard-settings.tab-header-footer.refresh-interval", (Object)10);
        yml.addDefault("rejoin-time", (Object)300);
        yml.addDefault("re-spawn-invulnerability", (Object)4000);
        yml.addDefault("bungee-settings.games-before-restart", (Object)30);
        yml.addDefault("bungee-settings.restart-cmd", (Object)"restart");
        yml.addDefault("bungee-settings.auto-scale-clone-limit", (Object)5);
        yml.addDefault("bungee-settings.lobby-sockets", Collections.singletonList("0.0.0.0:2019"));
        yml.addDefault("countdowns.game-start-regular", (Object)40);
        yml.addDefault("countdowns.game-start-half-arena", (Object)25);
        yml.addDefault("countdowns.game-start-shortened", (Object)5);
        yml.addDefault("countdowns.game-restart", (Object)45);
        yml.addDefault("countdowns.player-re-spawn", (Object)5);
        yml.addDefault("countdowns.next-event-beds-destroy", (Object)360);
        yml.addDefault("countdowns.next-event-dragon-spawn", (Object)600);
        yml.addDefault("countdowns.next-event-game-end", (Object)120);
        yml.addDefault("shout-cmd-cooldown", (Object)30);
        yml.addDefault("server-ip", (Object)"yourServer.Com");
        yml.addDefault("powered-by", (Object)"BedWars1058");
        yml.addDefault("bungee-settings.server-id", (Object)"bw1");
        yml.addDefault("bungee-settings.bwp-time-out", (Object)5000);
        yml.addDefault("allow-hunger-depletion.waiting", (Object)false);
        yml.addDefault("allow-hunger-depletion.ingame", (Object)false);
        yml.addDefault("allow-fire-extinguish", (Object)true);
        yml.addDefault("performance-settings.heal-pool.enable", (Object)true);
        yml.addDefault("performance-settings.heal-pool.seen-by-team-only", (Object)true);
        yml.addDefault("tnt-jump-settings.barycenter-alteration-in-y", (Object)0.5);
        yml.addDefault("tnt-jump-settings.strength-reduction-constant", (Object)5);
        yml.addDefault("tnt-jump-settings.y-axis-reduction-constant", (Object)2);
        yml.addDefault("tnt-jump-settings.damage-self", (Object)1);
        yml.addDefault("tnt-jump-settings.damage-teammates", (Object)5);
        yml.addDefault("tnt-jump-settings.damage-others", (Object)10);
        yml.addDefault("blast-protection.end-stone", (Object)Float.valueOf(BedWars.nms.getVersion() == 0 ? 69.0f : 12.0f));
        yml.addDefault("blast-protection.glass", (Object)Float.valueOf(300.0f));
        yml.addDefault("blast-protection.ray-blocked-by-glass", (Object)true);
        yml.addDefault("tnt-prime-settings.auto-ignite", (Object)true);
        yml.addDefault("tnt-prime-settings.fuse-ticks", (Object)45);
        yml.addDefault("fireball.explosion-size", (Object)3);
        yml.addDefault("fireball.speed-multiplier", (Object)10);
        yml.addDefault("fireball.make-fire", (Object)false);
        yml.addDefault("fireball.knockback.horizontal", (Object)1.0);
        yml.addDefault("fireball.knockback.vertical", (Object)0.65);
        yml.addDefault("fireball.cooldown", (Object)0.5);
        yml.addDefault("fireball.damage.self", (Object)2.0);
        yml.addDefault("fireball.damage.enemy", (Object)2.0);
        yml.addDefault("fireball.damage.teammates", (Object)0.0);
        yml.addDefault("database.enable", (Object)false);
        yml.addDefault("database.host", (Object)"localhost");
        yml.addDefault("database.port", (Object)3306);
        yml.addDefault("database.database", (Object)"bedwars1058");
        yml.addDefault("database.user", (Object)"root");
        yml.addDefault("database.pass", (Object)"cheese");
        yml.addDefault("database.ssl", (Object)false);
        yml.addDefault("performance-settings.rotate-generators", (Object)true);
        yml.addDefault("performance-settings.spoil-tnt-players", (Object)true);
        yml.addDefault("performance-settings.paper-features", (Object)true);
        yml.addDefault("inventories.disable-crafting-table", (Object)true);
        yml.addDefault("inventories.disable-enchanting-table", (Object)true);
        yml.addDefault("inventories.disable-furnace", (Object)true);
        yml.addDefault("inventories.disable-brewing-stand", (Object)true);
        yml.addDefault("inventories.disable-anvil", (Object)true);
        this.saveLobbyCommandItem("stats", "bw stats", false, BedWars.getForCurrentVersion("SKULL_ITEM", "SKULL_ITEM", "PLAYER_HEAD"), 3, 0);
        this.saveLobbyCommandItem("arena-selector", "bw gui", true, "CHEST", 5, 4);
        this.saveLobbyCommandItem("leave", "bw leave", false, BedWars.getForCurrentVersion("BED", "BED", "RED_BED"), 0, 8);
        this.savePreGameCommandItem("stats", "bw stats", false, BedWars.getForCurrentVersion("SKULL_ITEM", "SKULL_ITEM", "PLAYER_HEAD"), 3, 0);
        this.savePreGameCommandItem("leave", "bw leave", false, BedWars.getForCurrentVersion("BED", "BED", "RED_BED"), 0, 8);
        this.saveSpectatorCommandItem("teleporter", "bw teleporter", false, BedWars.getForCurrentVersion("SKULL_ITEM", "SKULL_ITEM", "PLAYER_HEAD"), 3, 0);
        this.saveSpectatorCommandItem("leave", "bw leave", false, BedWars.getForCurrentVersion("BED", "BED", "RED_BED"), 0, 8);
        yml.addDefault("arena-gui.settings.inv-size", (Object)27);
        yml.addDefault("arena-gui.settings.show-playing", (Object)true);
        yml.addDefault("arena-gui.settings.use-slots", (Object)"10,11,12,13,14,15,16");
        yml.addDefault("arena-gui.%path%.material".replace("%path%", "waiting"), (Object)BedWars.getForCurrentVersion("STAINED_GLASS_PANE", "CONCRETE", "LIME_CONCRETE"));
        yml.addDefault("arena-gui.%path%.data".replace("%path%", "waiting"), (Object)5);
        yml.addDefault("arena-gui.%path%.enchanted".replace("%path%", "waiting"), (Object)false);
        yml.addDefault("arena-gui.%path%.material".replace("%path%", "starting"), (Object)BedWars.getForCurrentVersion("STAINED_GLASS_PANE", "CONCRETE", "YELLOW_CONCRETE"));
        yml.addDefault("arena-gui.%path%.data".replace("%path%", "starting"), (Object)4);
        yml.addDefault("arena-gui.%path%.enchanted".replace("%path%", "starting"), (Object)true);
        yml.addDefault("arena-gui.%path%.material".replace("%path%", "playing"), (Object)BedWars.getForCurrentVersion("STAINED_GLASS_PANE", "CONCRETE", "RED_CONCRETE"));
        yml.addDefault("arena-gui.%path%.data".replace("%path%", "playing"), (Object)14);
        yml.addDefault("arena-gui.%path%.enchanted".replace("%path%", "playing"), (Object)false);
        yml.addDefault("arena-gui.%path%.material".replace("%path%", "skipped-slot"), (Object)BedWars.getForCurrentVersion("STAINED_GLASS_PANE", "STAINED_GLASS_PANE", "BLACK_STAINED_GLASS_PANE"));
        yml.addDefault("arena-gui.%path%.data".replace("%path%", "skipped-slot"), (Object)15);
        yml.addDefault("arena-gui.%path%.enchanted".replace("%path%", "skipped-slot"), (Object)false);
        yml.addDefault("stats-gui.inv-size", (Object)27);
        if (this.isFirstTime()) {
            Misc.addDefaultStatsItem(yml, 10, Material.DIAMOND, 0, "wins");
            Misc.addDefaultStatsItem(yml, 11, Material.REDSTONE, 0, "losses");
            Misc.addDefaultStatsItem(yml, 12, Material.IRON_SWORD, 0, "kills");
            Misc.addDefaultStatsItem(yml, 13, Material.valueOf((String)BedWars.getForCurrentVersion("SKULL_ITEM", "SKULL_ITEM", "SKELETON_SKULL")), 0, "deaths");
            Misc.addDefaultStatsItem(yml, 14, Material.DIAMOND_SWORD, 0, "final-kills");
            Misc.addDefaultStatsItem(yml, 15, Material.valueOf((String)BedWars.getForCurrentVersion("SKULL_ITEM", "SKULL_ITEM", "SKELETON_SKULL")), 1, "final-deaths");
            Misc.addDefaultStatsItem(yml, 16, Material.valueOf((String)BedWars.getForCurrentVersion("BED", "BED", "RED_BED")), 0, "beds-destroyed");
            Misc.addDefaultStatsItem(yml, 21, Material.valueOf((String)BedWars.getForCurrentVersion("STAINED_GLASS_PANE", "STAINED_GLASS_PANE", "BLACK_STAINED_GLASS_PANE")), 0, "first-play");
            Misc.addDefaultStatsItem(yml, 22, Material.CHEST, 0, "games-played");
            Misc.addDefaultStatsItem(yml, 23, Material.valueOf((String)BedWars.getForCurrentVersion("STAINED_GLASS_PANE", "STAINED_GLASS_PANE", "BLACK_STAINED_GLASS_PANE")), 0, "last-play");
        }
        yml.addDefault("start-items-per-group.Default", Collections.singletonList(BedWars.getForCurrentVersion("WOOD_SWORD", "WOOD_SWORD", "WOODEN_SWORD")));
        yml.addDefault("allowed-commands", Arrays.asList("shout", "bw", "leave"));
        yml.addDefault("use-experimental-team-assigner", (Object)true);
        yml.addDefault("enable-gen-split", (Object)true);
        yml.addDefault("lobby-settings.void-tp", (Object)true);
        yml.addDefault("lobby-settings.void-height", (Object)0);
        yml.addDefault("game-end.show-eliminated", (Object)true);
        yml.addDefault("game-end.teleport-eliminated", (Object)true);
        yml.addDefault("game-end.chat-top.order-by", (Object)DefaultStatistics.KILLS.toString());
        yml.addDefault("game-end.chat-top.hide-missing", (Object)true);
        yml.addDefault("game-end.sb-top.order-by", (Object)DefaultStatistics.KILLS.toString());
        yml.addDefault("game-end.sb-top.hide-missing", (Object)true);
        yml.options().copyDefaults(true);
        this.save();
        yml.set("formatChat", null);
        yml.set("globalChat", null);
        if (yml.get("bungee-settings.lobby-servers") != null) {
            Iterator sockets = new ArrayList(yml.getStringList("bungee-settings.lobby-servers"));
            yml.set("bungee-settings.lobby-sockets", sockets);
            yml.set("bungee-settings.lobby-servers", null);
        }
        if (yml.get("arenaGui.settings.showPlaying") != null) {
            this.set("arena-gui.settings.show-playing", yml.getBoolean("arenaGui.settings.showPlaying"));
        }
        if (yml.get("arenaGui.settings.size") != null) {
            this.set("arena-gui.settings.inv-size", yml.getInt("arenaGui.settings.size"));
        }
        if (yml.get("arenaGui.settings.useSlots") != null) {
            this.set("arena-gui.settings.use-slots", yml.getString("arenaGui.settings.useSlots"));
        }
        if (this.getYml().get("arenaGui") != null) {
            for (File[] path : this.getYml().getConfigurationSection("arenaGui").getKeys(false)) {
                if (path.equalsIgnoreCase("settings")) continue;
                new_path = path;
                if ("skippedSlot".equals(path)) {
                    new_path = "skipped-slot";
                }
                if (this.getYml().get("arenaGui." + (String)path + ".itemStack") != null) {
                    this.set("arena-gui.%path%.material".replace("%path%", (CharSequence)new_path), this.getYml().getString("arenaGui." + (String)path + ".itemStack"));
                }
                if (this.getYml().get("arenaGui." + (String)path + ".data") != null) {
                    this.set("arena-gui.%path%.data".replace("%path%", (CharSequence)new_path), this.getYml().getInt("arenaGui." + (String)path + ".data"));
                }
                if (this.getYml().get("arenaGui." + (String)path + ".enchanted") == null) continue;
                this.set("arena-gui.%path%.enchanted".replace("%path%", (CharSequence)new_path), this.getYml().getBoolean("arenaGui." + (String)path + ".enchanted"));
            }
        }
        if (this.getYml().get("fireball.damage-multiplier") != null) {
            this.set("fireball.damage-multiplier", null);
        }
        this.set("arenaGui", null);
        if (this.getYml().get("npcLoc") != null) {
            this.set("join-npc-locations", this.getYml().getString("npcLoc"));
        }
        if (this.getYml().get("statsGUI.invSize") != null) {
            this.set("stats-gui.inv-size", this.getInt("statsGUI.invSize"));
        }
        if (this.getYml().get("disableCrafting") != null) {
            this.set("inventories.disable-crafting-table", this.getString("disableCrafting"));
        }
        if (this.getYml().get("statsGUI") != null) {
            for (String stats_path : this.getYml().getConfigurationSection("statsGUI").getKeys(false)) {
                new_path = stats_path;
                switch (stats_path) {
                    case "gamesPlayed": {
                        new_path = "games-played";
                        break;
                    }
                    case "lastPlay": {
                        new_path = "last-play";
                        break;
                    }
                    case "firstPlay": {
                        new_path = "first-play";
                        break;
                    }
                    case "bedsDestroyed": {
                        new_path = "beds-destroyed";
                        break;
                    }
                    case "finalDeaths": {
                        new_path = "final-deaths";
                        break;
                    }
                    case "finalKills": {
                        new_path = "final-kills";
                    }
                }
                if (this.getYml().get("statsGUI." + stats_path + ".itemStack") != null) {
                    this.set("stats-gui.%path%.material".replace("%path%", (CharSequence)new_path), this.getYml().getString("statsGUI." + stats_path + ".itemStack"));
                }
                if (this.getYml().get("statsGUI." + stats_path + ".data") != null) {
                    this.set("stats-gui.%path%.data".replace("%path%", (CharSequence)new_path), this.getYml().getInt("statsGUI." + stats_path + ".data"));
                }
                if (this.getYml().get("statsGUI." + stats_path + ".slot") == null) continue;
                this.set("stats-gui.%path%.slot".replace("%path%", (CharSequence)new_path), this.getYml().getInt("statsGUI." + stats_path + ".slot"));
            }
        }
        if (yml.get("server-name") != null) {
            this.set("bungee-settings.server-id", yml.get("server-name"));
        }
        if (yml.get("lobby-scoreboard") != null) {
            this.set("scoreboard-settings.sidebar.enable-lobby-sidebar", yml.getBoolean("lobby-scoreboard"));
            this.set("lobby-scoreboard", null);
        }
        if (yml.get("game-scoreboard") != null) {
            this.set("scoreboard-settings.sidebar.enable-game-sidebar", yml.getBoolean("game-scoreboard"));
            this.set("game-scoreboard", null);
        }
        if (yml.get("enable-party-cmd") != null) {
            this.set("party-settings.enable-party-cmd", yml.getBoolean("enable-party-cmd"));
            this.set("enable-party-cmd", null);
        }
        if (yml.get("allow-parties") != null) {
            this.set("party-settings.allow-parties", yml.getBoolean("allow-parties"));
            this.set("allow-parties", null);
        }
        this.set("server-name", null);
        this.set("statsGUI", null);
        this.set("startItems", null);
        this.set("generators", null);
        this.set("bedsDestroyCountdown", null);
        this.set("dragonSpawnCountdown", null);
        this.set("gameEndCountdown", null);
        this.set("npcLoc", null);
        this.set("blockedCmds", null);
        this.set("lobbyScoreboard", null);
        this.set("arenaGui.settings.startSlot", null);
        this.set("arenaGui.settings.endSlot", null);
        this.set("items", null);
        this.set("start-items-per-arena", null);
        this.set("safeMode", null);
        this.set("disableCrafting", null);
        this.set("performance-settings.disable-armor-packets", null);
        this.set("performance-settings.disable-respawn-packets", null);
        String whatLang = "en";
        File[] langs = new File(plugin.getDataFolder(), "/Languages").listFiles();
        if (langs != null) {
            for (File f : langs) {
                if (!f.isFile() || !f.getName().contains("messages_") || !f.getName().contains(".yml")) continue;
                String lang = f.getName().replace("messages_", "").replace(".yml", "");
                if (lang.equalsIgnoreCase(yml.getString("language"))) {
                    whatLang = f.getName().replace("messages_", "").replace(".yml", "");
                }
                if (Language.getLang(lang) != null) continue;
                new Language((Plugin)BedWars.plugin, lang);
            }
        }
        if ((def = Language.getLang(whatLang)) == null) {
            throw new IllegalStateException("Could not found default language: " + whatLang);
        }
        Language.setDefaultLanguage(def);
        for (String iso : yml.getStringList("disabled-languages")) {
            Language l = Language.getLang(iso);
            if (l == null || l == def) continue;
            Language.getLanguages().remove(l);
        }
        BedWars.setDebug(yml.getBoolean("debug"));
        new ConfigManager(plugin, "bukkit", Bukkit.getWorldContainer().getPath()).set("ticks-per.autosave", -1);
        Bukkit.spigot().getConfig().set("commands.send-namespaced", (Object)false);
        try {
            Bukkit.spigot().getConfig().save("spigot.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BedWars.setServerType(ServerType.valueOf(Objects.requireNonNull(yml.getString("serverType")).toUpperCase()));
        } catch (Exception e) {
            if (Objects.requireNonNull(yml.getString("serverType")).equalsIgnoreCase("BUNGEE_LEGACY")) {
                BedWars.setServerType(ServerType.BUNGEE);
                BedWars.setAutoscale(false);
            }
            this.set("serverType", "MULTIARENA");
        }
        BedWars.setLobbyWorld(this.getLobbyWorldName());
    }

    public String getLobbyWorldName() {
        if (this.getYml().get("lobbyLoc") == null) {
            return "";
        }
        String d = this.getYml().getString("lobbyLoc");
        String[] data = d.replace("[", "").replace("]", "").split(",");
        return data[data.length - 1];
    }

    public void saveLobbyCommandItem(String name, String cmd, boolean enchanted, String material, int data, int slot) {
        if (this.isFirstTime()) {
            this.getYml().addDefault("lobby-items.%path%.command".replace("%path%", name), (Object)cmd);
            this.getYml().addDefault("lobby-items.%path%.material".replace("%path%", name), (Object)material);
            this.getYml().addDefault("lobby-items.%path%.data".replace("%path%", name), (Object)data);
            this.getYml().addDefault("lobby-items.%path%.enchanted".replace("%path%", name), (Object)enchanted);
            this.getYml().addDefault("lobby-items.%path%.slot".replace("%path%", name), (Object)slot);
            this.getYml().options().copyDefaults(true);
            this.save();
        }
    }

    public void savePreGameCommandItem(String name, String cmd, boolean enchanted, String material, int data, int slot) {
        if (this.isFirstTime()) {
            this.getYml().addDefault("pre-game-items.%path%.command".replace("%path%", name), (Object)cmd);
            this.getYml().addDefault("pre-game-items.%path%.material".replace("%path%", name), (Object)material);
            this.getYml().addDefault("pre-game-items.%path%.data".replace("%path%", name), (Object)data);
            this.getYml().addDefault("pre-game-items.%path%.enchanted".replace("%path%", name), (Object)enchanted);
            this.getYml().addDefault("pre-game-items.%path%.slot".replace("%path%", name), (Object)slot);
            this.getYml().options().copyDefaults(true);
            this.save();
        }
    }

    public void saveSpectatorCommandItem(String name, String cmd, boolean enchanted, String material, int data, int slot) {
        if (this.isFirstTime()) {
            this.getYml().addDefault("spectator-items.%path%.command".replace("%path%", name), (Object)cmd);
            this.getYml().addDefault("spectator-items.%path%.material".replace("%path%", name), (Object)material);
            this.getYml().addDefault("spectator-items.%path%.data".replace("%path%", name), (Object)data);
            this.getYml().addDefault("spectator-items.%path%.enchanted".replace("%path%", name), (Object)enchanted);
            this.getYml().addDefault("spectator-items.%path%.slot".replace("%path%", name), (Object)slot);
            this.getYml().options().copyDefaults(true);
            this.save();
        }
    }
}

