/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.api.language;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.configuration.ConfigManager;
import com.andrei1058.bedwars.api.events.player.PlayerLangChangeEvent;
import com.andrei1058.bedwars.api.language.Messages;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Language
extends ConfigManager {
    private final String iso;
    private String prefix = "";
    private static String prefixStatic = "";
    private static final HashMap<UUID, Language> langByPlayer = new HashMap();
    private static final List<Language> languages = new ArrayList<Language>();
    private static Language defaultLanguage;
    private String serverIp;

    /*
     * WARNING - void declaration
     */
    public Language(Plugin plugin, String iso) {
        super(plugin, "messages_" + iso, plugin.getDataFolder().getPath() + "/Languages");
        this.iso = iso;
        List oldMsg = this.getYml().getStringList(Messages.GAME_END_TOP_PLAYER_CHAT);
        if (!oldMsg.isEmpty()) {
            String[] oldTop1 = new String[]{"{firstName}", "{secondName}", "{thirdName}"};
            String[] oldTop2 = new String[]{"{firstKills}", "{secondKills}", "{thirdKills}"};
            ArrayList<void> newMsg = new ArrayList<void>();
            for (String string : oldMsg) {
                void var8_8;
                for (String oldPlaceholder : oldTop1) {
                    void var8_10;
                    String string2 = var8_10.replace(oldPlaceholder, "{topPlayerName}");
                }
                for (String oldPlaceholder : oldTop2) {
                    String string3 = var8_8.replace(oldPlaceholder, "{topValue}");
                }
                newMsg.add(var8_8);
            }
            this.getYml().set(Messages.GAME_END_TOP_PLAYER_CHAT, newMsg);
        }
        if (null != this.getYml().get("scoreboard")) {
            for (String group : this.getYml().getConfigurationSection("scoreboard").getKeys(false)) {
                if (group.equalsIgnoreCase("lobby")) {
                    this.relocate("scoreboard." + group, "sidebar." + group);
                    continue;
                }
                HashMap<String, String[]> stages = new HashMap<String, String[]>();
                stages.put("waiting", new String[]{Messages.SCOREBOARD_DEFAULT_WAITING, Messages.SCOREBOARD_DEFAULT_WAITING_SPEC});
                stages.put("starting", new String[]{Messages.SCOREBOARD_DEFAULT_STARTING, Messages.SCOREBOARD_DEFAULT_STARTING_SPEC});
                for (Map.Entry entry : stages.entrySet()) {
                    if (this.exists("scoreboard." + group + "." + (String)entry.getKey() + ".player")) {
                        this.relocate("scoreboard." + group + "." + (String)entry.getKey() + ".player", ((String[])entry.getValue())[0].replace("Default", group));
                    } else {
                        this.relocate("scoreboard." + group + "." + (String)entry.getKey(), ((String[])entry.getValue())[0].replace("Default", group));
                    }
                    if (!this.exists("scoreboard." + group + "." + (String)entry.getKey() + ".spectator")) continue;
                    this.relocate("scoreboard." + group + "." + (String)entry.getKey() + ".spectator", ((String[])entry.getValue())[1].replace("Default", group));
                }
                if (this.exists("scoreboard." + group + ".playing.alive")) {
                    this.relocate("scoreboard." + group + ".playing.alive", Messages.SCOREBOARD_DEFAULT_PLAYING.replace("Default", group));
                    this.relocate("scoreboard." + group + ".playing.spectator", Messages.SCOREBOARD_DEFAULT_PLAYING.replace("Default", group));
                    continue;
                }
                this.relocate("scoreboard." + group + ".playing", Messages.SCOREBOARD_DEFAULT_PLAYING.replace("Default", group));
            }
            this.getYml().set("scoreboard", null);
        }
        languages.add(this);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setPrefixStatic(String prefix) {
        prefixStatic = prefix;
    }

    public static List<String> getScoreboard(Player player, String path, String alternative) {
        Language language = Language.getPlayerLanguage(player);
        if (language.exists(path)) {
            return language.l(path);
        }
        if (path.split("\\.").length == 3) {
            String[] sp = path.split("\\.");
            Object path2 = sp[1];
            path2 = String.valueOf(((String)path2).charAt(0)).toUpperCase() + ((String)path2).substring(1).toLowerCase();
            if (language.exists((String)(path2 = sp[0] + "." + (String)path2 + "." + sp[2]))) {
                return language.l(path);
            }
            if (language.exists(sp[0] + "." + sp[1].toUpperCase() + "." + sp[2])) {
                return language.l(sp[0] + "." + sp[1].toUpperCase() + "." + sp[2]);
            }
        }
        return language.l(alternative);
    }

    public String getLangName() {
        return this.getYml().getString("name");
    }

    public static String getMsg(Player player, String path) {
        if (player == null) {
            return Language.getDefaultLanguage().m(path);
        }
        return langByPlayer.getOrDefault(player.getUniqueId(), Language.getDefaultLanguage()).m(path).replace("{prefix}", prefixStatic == null ? "" : prefixStatic);
    }

    public static Language getPlayerLanguage(@NotNull Player player) {
        return langByPlayer.getOrDefault(player.getUniqueId(), Language.getDefaultLanguage());
    }

    public static Language getPlayerLanguage(UUID p) {
        return langByPlayer.getOrDefault(p, Language.getDefaultLanguage());
    }

    public boolean exists(String path) {
        return this.getYml().get(path) != null;
    }

    public static List<String> getList(@NotNull Player player, String path) {
        return langByPlayer.getOrDefault(player.getUniqueId(), Language.getDefaultLanguage()).l(path);
    }

    public void relocate(String from, String to) {
        Object fromData = this.getYml().get(from);
        if (null != fromData) {
            this.getYml().set(to, fromData);
            this.getYml().set(from, null);
        }
    }

    public static void saveIfNotExists(String path, Object data) {
        for (Language l : languages) {
            if (l.getYml().get(path) != null) continue;
            l.set(path, data);
        }
    }

    public String m(String path) {
        BedWars api;
        String message = this.getYml().getString(path);
        if (message == null) {
            System.err.println("Missing message key " + path + " in language " + this.getIso());
            message = "MISSING_LANG";
        }
        if (null == this.serverIp && null != (api = (BedWars)Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider()).getConfigs().getMainConfig()) {
            this.serverIp = api.getConfigs().getMainConfig().getString("server-ip");
        }
        return ChatColor.translateAlternateColorCodes((char)'&', (String)message.replace("{prefix}", this.prefix == null ? "" : this.prefix).replace("{serverIp}", this.serverIp == null ? "" : this.serverIp));
    }

    public List<String> l(String path) {
        ArrayList<String> result = new ArrayList<String>();
        List<String> lines = this.getYml().getStringList(path);
        if (lines == null) {
            System.err.println("Missing message list key " + path + " in language " + this.getIso());
            lines = Collections.emptyList();
        }
        for (String line : lines) {
            result.add(ChatColor.translateAlternateColorCodes((char)'&', (String)line));
        }
        return result;
    }

    public static HashMap<UUID, Language> getLangByPlayer() {
        return langByPlayer;
    }

    public static boolean isLanguageExist(String iso) {
        for (Language l : languages) {
            if (!l.iso.equalsIgnoreCase(iso)) continue;
            return true;
        }
        return false;
    }

    public static Language getLang(String iso) {
        for (Language l : languages) {
            if (!l.iso.equalsIgnoreCase(iso)) continue;
            return l;
        }
        return Language.getDefaultLanguage();
    }

    public String getIso() {
        return this.iso;
    }

    public static List<Language> getLanguages() {
        return languages;
    }

    public static void setupCustomStatsMessages() {
        BedWars api = (BedWars)Bukkit.getServer().getServicesManager().getRegistration(BedWars.class).getProvider();
        for (Language l : Language.getLanguages()) {
            if (l == null || l.getYml() == null) continue;
            if (api.getConfigs().getMainConfig().getYml().get("stats-gui") == null) {
                return;
            }
            for (String item : api.getConfigs().getMainConfig().getYml().getConfigurationSection("stats-gui").getKeys(false)) {
                if ("stats-gui.inv-size".contains(item)) continue;
                if (l.getYml().getDefaults() == null || !l.getYml().getDefaults().contains(Messages.PLAYER_STATS_GUI_PATH + "-" + item + "-name")) {
                    l.getYml().addDefault(Messages.PLAYER_STATS_GUI_PATH + "-" + item + "-name", (Object)"Name not set");
                }
                if (l.getYml().getDefaults() != null && l.getYml().getDefaults().contains(Messages.PLAYER_STATS_GUI_PATH + "-" + item + "-lore")) continue;
                l.getYml().addDefault(Messages.PLAYER_STATS_GUI_PATH + "-" + item + "-lore", Collections.singletonList("lore not set"));
            }
            l.save();
        }
    }

    public void addDefaultStatsMsg(@NotNull YamlConfiguration yml, String path, String name, String ... lore) {
        if (yml.getDefaults() == null || !yml.getDefaults().contains(Messages.PLAYER_STATS_GUI_PATH + "-" + path + "-name")) {
            yml.addDefault(Messages.PLAYER_STATS_GUI_PATH + "-" + path + "-name", (Object)name);
        }
        if (yml.getDefaults() == null || !yml.getDefaults().contains(Messages.PLAYER_STATS_GUI_PATH + "-" + path + "-lore")) {
            yml.addDefault(Messages.PLAYER_STATS_GUI_PATH + "-" + path + "-lore", (Object)lore);
        }
    }

    public static void addDefaultMessagesCommandItems(Language language) {
        String p2;
        String p1;
        if (language == null) {
            return;
        }
        YamlConfiguration yml = language.getYml();
        if (yml == null) {
            return;
        }
        BedWars api = (BedWars)Bukkit.getServer().getServicesManager().getRegistration(BedWars.class).getProvider();
        if (api.getConfigs().getMainConfig().getYml().get("lobby-items") != null) {
            for (String item : api.getConfigs().getMainConfig().getYml().getConfigurationSection("lobby-items").getKeys(false)) {
                if (item.isEmpty()) continue;
                p1 = "lobby-items-%path%-name".replace("%path%", item);
                p2 = "lobby-items-%path%-lore".replace("%path%", item);
                if (yml.getDefaults() == null || !yml.getDefaults().contains(p1)) {
                    yml.addDefault(p1, (Object)("&cName not set at: &f" + p1));
                }
                if (yml.getDefaults() != null && yml.getDefaults().contains(p1)) continue;
                yml.addDefault(p2, Arrays.asList("&cLore not set at:", " &f" + p2));
            }
        }
        if (api.getConfigs().getMainConfig().getYml().get("spectator-items") != null) {
            for (String item : api.getConfigs().getMainConfig().getYml().getConfigurationSection("spectator-items").getKeys(false)) {
                if (item.isEmpty()) continue;
                p1 = "spectator-items-%path%-name".replace("%path%", item);
                p2 = "spectator-items-%path%-lore".replace("%path%", item);
                if (yml.getDefaults() == null || !yml.getDefaults().contains(p1)) {
                    yml.addDefault(p1, (Object)("&cName not set at: &f" + p1));
                }
                if (yml.getDefaults() != null && yml.getDefaults().contains(p1)) continue;
                yml.addDefault(p2, Arrays.asList("&cLore not set at:", " &f" + p2));
            }
        }
        if (api.getConfigs().getMainConfig().getYml().get("pre-game-items") != null) {
            for (String item : api.getConfigs().getMainConfig().getYml().getConfigurationSection("pre-game-items").getKeys(false)) {
                if (item.isEmpty()) continue;
                p1 = "pre-game-items-%path%-name".replace("%path%", item);
                p2 = "pre-game-items-%path%-lore".replace("%path%", item);
                if (yml.getDefaults() == null || !yml.getDefaults().contains(p1)) {
                    yml.addDefault(p1, (Object)("&cName not set at: &f" + p1));
                }
                if (yml.getDefaults() != null && yml.getDefaults().contains(p1)) continue;
                yml.addDefault(p2, Arrays.asList("&cLore not set at:", " &f" + p2));
            }
        }
        yml.options().copyDefaults(true);
        language.save();
    }

    public void setupUnSetCategories() {
        BedWars api = (BedWars)Bukkit.getServer().getServicesManager().getRegistration(BedWars.class).getProvider();
        for (String s : api.getConfigs().getShopConfig().getYml().getConfigurationSection("").getKeys(false)) {
            if (s.equalsIgnoreCase("shop-settings") || s.equalsIgnoreCase("shop-specials") || s.equals("quick-buy-defaults")) continue;
            if (!this.exists("shop-items-messages.%category%.inventory-name".replace("%category%", s))) {
                this.set("shop-items-messages.%category%.inventory-name".replace("%category%", s), "&8Name not set");
            }
            if (!this.exists("shop-items-messages.%category%.category-item-name".replace("%category%", s))) {
                this.set("shop-items-messages.%category%.category-item-name".replace("%category%", s), "&8Name not set");
            }
            if (!this.exists("shop-items-messages.%category%.category-item-lore".replace("%category%", s))) {
                this.set("shop-items-messages.%category%.category-item-lore".replace("%category%", s), Collections.singletonList("&8Lore not set"));
            }
            if (api.getConfigs().getShopConfig().getYml().get(s + ".category-content") == null) continue;
            for (String c : api.getConfigs().getShopConfig().getYml().getConfigurationSection(s + ".category-content").getKeys(false)) {
                if (!this.exists("shop-items-messages.%category%.content-item-%content%-name".replace("%category%", s).replace("%content%", c))) {
                    this.set("shop-items-messages.%category%.content-item-%content%-name".replace("%category%", s).replace("%content%", c), "&8Name not set");
                }
                if (this.exists("shop-items-messages.%category%.content-item-%content%-lore".replace("%category%", s).replace("%content%", c))) continue;
                this.set("shop-items-messages.%category%.content-item-%content%-lore".replace("%category%", s).replace("%content%", c), Collections.singletonList("&8Lore not set"));
            }
        }
    }

    public static void addCategoryMessages(@NotNull YamlConfiguration yml, String categoryName, String invName, String itemName, List<String> itemLore) {
        if (yml.getDefaults() == null || !yml.getDefaults().contains("shop-items-messages.%category%.inventory-name".replace("%category%", categoryName))) {
            yml.addDefault("shop-items-messages.%category%.inventory-name".replace("%category%", categoryName), (Object)invName);
        }
        if (yml.getDefaults() == null || !yml.getDefaults().contains("shop-items-messages.%category%.category-item-name".replace("%category%", categoryName))) {
            yml.addDefault("shop-items-messages.%category%.category-item-name".replace("%category%", categoryName), (Object)itemName);
        }
        if (yml.getDefaults() == null || !yml.getDefaults().contains("shop-items-messages.%category%.category-item-lore".replace("%category%", categoryName))) {
            yml.addDefault("shop-items-messages.%category%.category-item-lore".replace("%category%", categoryName), itemLore);
        }
    }

    public static void addContentMessages(@NotNull YamlConfiguration yml, String contentName, String categoryName, String itemName, List<String> itemLore) {
        String path1 = "shop-items-messages.%category%.content-item-%content%-name".replace("%category%", categoryName).replace("%content%", contentName);
        String path2 = "shop-items-messages.%category%.content-item-%content%-lore".replace("%category%", categoryName).replace("%content%", contentName);
        if (yml.getDefaults() == null || !yml.getDefaults().contains(path1)) {
            yml.addDefault(path1, (Object)itemName);
        }
        if (yml.getDefaults() == null || !yml.getDefaults().contains(path2)) {
            yml.addDefault(path2, itemLore);
        }
    }

    public static boolean setPlayerLanguage(UUID uuid, String iso) {
        if (iso == null) {
            Player player;
            if (langByPlayer.containsKey(uuid) && (player = Bukkit.getPlayer((UUID)uuid)) != null && player.isOnline()) {
                PlayerLangChangeEvent e = new PlayerLangChangeEvent(player, Language.langByPlayer.get((Object)uuid).iso, Language.getDefaultLanguage().iso);
                Bukkit.getPluginManager().callEvent((Event)e);
                if (e.isCancelled()) {
                    return false;
                }
            }
            langByPlayer.remove(uuid);
            return true;
        }
        Language newLang = Language.getLang(iso);
        if (newLang == null) {
            return false;
        }
        Language oldLang = Language.getPlayerLanguage(uuid);
        if (oldLang.getIso().equals(newLang.getIso())) {
            return false;
        }
        Player player = Bukkit.getPlayer((UUID)uuid);
        if (player != null && player.isOnline()) {
            PlayerLangChangeEvent e = new PlayerLangChangeEvent(player, oldLang.getIso(), newLang.getIso());
            Bukkit.getPluginManager().callEvent((Event)e);
            if (e.isCancelled()) {
                return false;
            }
        }
        if (Language.getDefaultLanguage().getIso().equals(newLang.getIso())) {
            langByPlayer.remove(uuid);
            return true;
        }
        if (langByPlayer.containsKey(uuid)) {
            langByPlayer.replace(uuid, newLang);
        } else {
            langByPlayer.put(uuid, newLang);
        }
        return true;
    }

    public static String @NotNull [] getCountDownTitle(@NotNull Language playerLang, int second) {
        String[] result = new String[2];
        result[0] = ChatColor.translateAlternateColorCodes((char)'&', (String)playerLang.getYml().get(Messages.ARENA_STATUS_START_COUNTDOWN_TITLE + "-" + second, (Object)playerLang.getString(Messages.ARENA_STATUS_START_COUNTDOWN_TITLE)).toString().replace("{second}", String.valueOf(second)));
        if (result[0].isEmpty()) {
            result[0] = " ";
        }
        result[1] = ChatColor.translateAlternateColorCodes((char)'&', (String)playerLang.getYml().get(Messages.ARENA_STATUS_START_COUNTDOWN_SUB_TITLE + "-" + second, (Object)playerLang.getString(Messages.ARENA_STATUS_START_COUNTDOWN_SUB_TITLE)).toString().replace("{second}", String.valueOf(second)));
        if (result[1].isEmpty()) {
            result[1] = " ";
        }
        return result;
    }

    public static void setDefaultLanguage(Language defaultLanguage) {
        Language.defaultLanguage = defaultLanguage;
    }

    public static Language getDefaultLanguage() {
        return defaultLanguage;
    }
}

