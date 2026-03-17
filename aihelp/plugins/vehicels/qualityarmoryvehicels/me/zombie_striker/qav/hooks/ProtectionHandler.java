/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package me.zombie_striker.qav.hooks;

import java.util.HashSet;
import java.util.Set;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.hooks.ProtectionHook;
import me.zombie_striker.qav.hooks.implementation.TownyHook;
import me.zombie_striker.qav.hooks.implementation.WorldGuardHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ProtectionHandler {
    private static final Set<ProtectionHook> compatibilities = new HashSet<ProtectionHook>();

    public static void init() {
        ProtectionHandler.hook("WorldGuard", WorldGuardHook::new);
        ProtectionHandler.hook("Towny", TownyHook::new);
    }

    public static boolean canMove(Player player, Location location) {
        return compatibilities.stream().allMatch(protectionHook -> protectionHook.canMove(player, location));
    }

    public static boolean canPlace(Player player, Location location) {
        return compatibilities.stream().allMatch(protectionHook -> protectionHook.canPlace(player, location));
    }

    public static boolean canRemove(Player player, Location location) {
        return compatibilities.stream().allMatch(protectionHook -> protectionHook.canRemove(player, location));
    }

    public static void hook(String string, CompatibilityConstructor compatibilityConstructor) {
        if (Bukkit.getPluginManager().getPlugin(string) != null && ((Boolean)Main.a("hooks." + string, true)).booleanValue()) {
            compatibilities.add(compatibilityConstructor.create());
        }
    }

    @FunctionalInterface
    private static interface CompatibilityConstructor {
        public ProtectionHook create();
    }
}

