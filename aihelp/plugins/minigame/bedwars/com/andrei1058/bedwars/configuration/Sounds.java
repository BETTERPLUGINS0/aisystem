/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.configuration;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.NextEvent;
import com.andrei1058.bedwars.api.configuration.ConfigManager;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Sounds {
    private static final ConfigManager sounds = new ConfigManager((Plugin)BedWars.plugin, "sounds", BedWars.plugin.getDataFolder().getPath());

    private Sounds() {
    }

    public static void init() {
        YamlConfiguration yml = sounds.getYml();
        Sounds.addDefSound("game-end", BedWars.getForCurrentVersion("AMBIENCE_THUNDER", "ENTITY_LIGHTNING_THUNDER", "ITEM_TRIDENT_THUNDER"));
        Sounds.addDefSound("rejoin-denied", BedWars.getForCurrentVersion("VILLAGER_NO", "ENTITY_VILLAGER_NO", "ENTITY_VILLAGER_NO"));
        Sounds.addDefSound("rejoin-allowed", BedWars.getForCurrentVersion("SLIME_WALK", "ENTITY_SLIME_JUMP", "ENTITY_SLIME_JUMP"));
        Sounds.addDefSound("spectate-denied", BedWars.getForCurrentVersion("VILLAGER_NO", "ENTITY_VILLAGER_NO", "ENTITY_VILLAGER_NO"));
        Sounds.addDefSound("spectate-allowed", BedWars.getForCurrentVersion("SLIME_WALK", "ENTITY_SLIME_JUMP", "ENTITY_SLIME_JUMP"));
        Sounds.addDefSound("join-denied", BedWars.getForCurrentVersion("VILLAGER_NO", "ENTITY_VILLAGER_NO", "ENTITY_VILLAGER_NO"));
        Sounds.addDefSound("join-allowed", BedWars.getForCurrentVersion("SLIME_WALK", "ENTITY_SLIME_JUMP", "ENTITY_SLIME_JUMP"));
        Sounds.addDefSound("spectator-gui-click", BedWars.getForCurrentVersion("SLIME_WALK", "ENTITY_SLIME_JUMP", "ENTITY_SLIME_JUMP"));
        Sounds.addDefSound("game-countdown-others", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        Sounds.addDefSound("game-countdown-s5", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        Sounds.addDefSound("game-countdown-s4", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        Sounds.addDefSound("game-countdown-s3", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        Sounds.addDefSound("game-countdown-s2", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        Sounds.addDefSound("game-countdown-s1", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        Sounds.addDefSound("game-countdown-start", BedWars.getForCurrentVersion("SLIME_ATTACK", "BLOCK_SLIME_FALL", "BLOCK_SLIME_BLOCK_FALL"));
        Sounds.addDefSound("kill", BedWars.getForCurrentVersion("ORB_PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP"));
        Sounds.addDefSound("bed-destroy", BedWars.getForCurrentVersion("ENDERDRAGON_GROWL", "ENTITY_ENDERDRAGON_GROWL", "ENTITY_ENDER_DRAGON_GROWL"));
        Sounds.addDefSound("bed-destroy-own", BedWars.getForCurrentVersion("WITHER_DEATH", "ENTITY_WITHER_DEATH", "ENTITY_WITHER_DEATH"));
        Sounds.addDefSound("shop-insufficient-money", BedWars.getForCurrentVersion("VILLAGER_NO", "ENTITY_VILLAGER_NO", "ENTITY_VILLAGER_NO"));
        Sounds.addDefSound("shop-bought", BedWars.getForCurrentVersion("VILLAGER_YES", "ENTITY_VILLAGER_YES", "ENTITY_VILLAGER_YES"));
        Sounds.addDefSound(NextEvent.BEDS_DESTROY.getSoundPath(), BedWars.getForCurrentVersion("ENDERDRAGON_GROWL", "ENTITY_ENDERDRAGON_GROWL", "ENTITY_ENDER_DRAGON_GROWL"));
        Sounds.addDefSound(NextEvent.DIAMOND_GENERATOR_TIER_II.getSoundPath(), BedWars.getForCurrentVersion("LEVEL_UP", "ENTITY_PLAYER_LEVELUP", "ENTITY_PLAYER_LEVELUP"));
        Sounds.addDefSound(NextEvent.DIAMOND_GENERATOR_TIER_III.getSoundPath(), BedWars.getForCurrentVersion("LEVEL_UP", "ENTITY_PLAYER_LEVELUP", "ENTITY_PLAYER_LEVELUP"));
        Sounds.addDefSound(NextEvent.EMERALD_GENERATOR_TIER_II.getSoundPath(), BedWars.getForCurrentVersion("GHAST_MOAN", "ENTITY_GHAST_WARN", "ENTITY_GHAST_WARN"));
        Sounds.addDefSound(NextEvent.EMERALD_GENERATOR_TIER_III.getSoundPath(), BedWars.getForCurrentVersion("GHAST_MOAN", "ENTITY_GHAST_WARN", "ENTITY_GHAST_WARN"));
        Sounds.addDefSound(NextEvent.ENDER_DRAGON.getSoundPath(), BedWars.getForCurrentVersion("ENDERDRAGON_WINGS", "ENTITY_ENDERDRAGON_FLAP", "ENTITY_ENDER_DRAGON_FLAP"));
        Sounds.addDefSound("player-re-spawn", BedWars.getForCurrentVersion("SLIME_ATTACK", "BLOCK_SLIME_FALL", "BLOCK_SLIME_BLOCK_FALL"));
        Sounds.addDefSound("arena-selector-open", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        Sounds.addDefSound("stats-gui-open", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        Sounds.addDefSound("trap-sound", BedWars.getForCurrentVersion("ENDERMAN_TELEPORT", "ENDERMAN_TELEPORT", "ENTITY_ENDERMAN_TELEPORT"));
        Sounds.addDefSound("shop-auto-equip", BedWars.getForCurrentVersion("HORSE_ARMOR", "ITEM_ARMOR_EQUIP_GENERIC", "ITEM_ARMOR_EQUIP_GENERIC"));
        Sounds.addDefSound("egg-bridge-block", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        Sounds.addDefSound("ender-pearl-landed", BedWars.getForCurrentVersion("ENDERMAN_TELEPORT", "ENTITY_ENDERMEN_TELEPORT", "ENTITY_ENDERMAN_TELEPORT"));
        Sounds.addDefSound("pop-up-tower-build", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        yml.options().copyDefaults(true);
        yml.set("bought", null);
        yml.set("insufficient-money", null);
        yml.set("player-kill", null);
        yml.set("countdown", null);
        sounds.save();
    }

    private static Sound getSound(String path) {
        try {
            return Sound.valueOf((String)sounds.getString(path + ".sound"));
        } catch (Exception ex) {
            return Sound.valueOf((String)BedWars.getForCurrentVersion("AMBIENCE_THUNDER", "ENTITY_LIGHTNING_THUNDER", "ITEM_TRIDENT_THUNDER"));
        }
    }

    public static void playSound(String path, List<Player> players) {
        if (path.equalsIgnoreCase("none")) {
            return;
        }
        Sound sound = Sounds.getSound(path);
        int volume = Sounds.getSounds().getInt(path + ".volume");
        int pitch = Sounds.getSounds().getInt(path + ".pitch");
        if (sound != null) {
            players.forEach(p -> p.playSound(p.getLocation(), sound, (float)volume, (float)pitch));
        }
    }

    public static boolean playSound(Sound sound, List<Player> players) {
        if (sound == null) {
            return false;
        }
        players.forEach(p -> p.playSound(p.getLocation(), sound, 1.0f, 1.0f));
        return true;
    }

    public static void playSound(String path, Player player) {
        Sound sound = Sounds.getSound(path);
        float volume = (float)Sounds.getSounds().getYml().getDouble(path + ".volume");
        float pitch = (float)Sounds.getSounds().getYml().getDouble(path + ".pitch");
        if (sound != null) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public static ConfigManager getSounds() {
        return sounds;
    }

    private static void addDefSound(String path, String value) {
        if (Sounds.getSounds().getYml().get(path) != null && Sounds.getSounds().getYml().get(path + ".volume") == null) {
            String temp = Sounds.getSounds().getYml().getString(path);
            Sounds.getSounds().getYml().set(path, null);
            Sounds.getSounds().getYml().set(path + ".sound", (Object)temp);
        }
        Sounds.getSounds().getYml().addDefault(path + ".sound", (Object)value);
        Sounds.getSounds().getYml().addDefault(path + ".volume", (Object)1);
        Sounds.getSounds().getYml().addDefault(path + ".pitch", (Object)1);
    }

    public static void playsoundArea(String path, Location location, float x, float y) {
        Sound sound = Sounds.getSound(path);
        if (sound != null) {
            location.getWorld().playSound(location, sound, x, y);
        }
    }
}

