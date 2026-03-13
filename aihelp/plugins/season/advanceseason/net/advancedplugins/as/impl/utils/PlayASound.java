/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayASound {
    private static final Cache<String, Boolean> warnedSounds = CacheBuilder.newBuilder().maximumSize(1000L).build();

    private static Sound getSound(String string) {
        Sound sound;
        if (string == null) {
            return null;
        }
        if (string.equalsIgnoreCase("none")) {
            return null;
        }
        if (string.isEmpty()) {
            return null;
        }
        try {
            sound = Sound.valueOf((String)string);
        } catch (IllegalArgumentException illegalArgumentException) {
            String[] stringArray;
            if (string.contains("_") && (stringArray = string.split("_")).length < 3) {
                String string2 = stringArray[1] + "_" + stringArray[0];
                try {
                    return Sound.valueOf((String)string2);
                } catch (IllegalArgumentException illegalArgumentException2) {
                    return null;
                }
            }
            return null;
        }
        return sound;
    }

    private static void warn(String string) {
        if (warnedSounds.getIfPresent(string) != null) {
            return;
        }
        Bukkit.getLogger().warning("Sound " + string + " couldn't be found: invalid sound for this minecraft version?");
        warnedSounds.put(string, true);
    }

    public static void playSound(String string, Player player) {
        PlayASound.playSound(string, player, 1.0f, 1.0f);
    }

    public static void playSound(String string, Player player, float f, float f2) {
        Sound sound = PlayASound.getSound(string);
        if (sound == null) {
            PlayASound.warn(string);
            return;
        }
        player.playSound(player.getLocation(), sound, f2, f);
    }

    public static void playSound(String string, Location location) {
        PlayASound.playSound(string, location, 1.0f, 1.0f);
    }

    public static void playSound(String string, Location location, float f, float f2) {
        Sound sound = PlayASound.getSound(string);
        if (sound == null) {
            PlayASound.warn(string);
            return;
        }
        location.getWorld().playSound(location, sound, f2, f);
    }
}

