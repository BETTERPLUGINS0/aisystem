/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.utils.protection;

import java.util.HashMap;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.LocalLocation;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.protection.ProtectionType;
import net.advancedplugins.as.impl.utils.protection.external.FactionsCheck;
import net.advancedplugins.as.impl.utils.protection.external.LandsCheck;
import net.advancedplugins.as.impl.utils.protection.external.ProtectionStonesCheck;
import net.advancedplugins.as.impl.utils.protection.external.SlimeFunCheck;
import net.advancedplugins.as.impl.utils.protection.external.WorldGuardCheck;
import net.advancedplugins.as.impl.utils.protection.internal.GlobalProtCheck;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ProtectionHandler {
    private final HashMap<String, ProtectionType> protectionMap = new HashMap();

    public ProtectionHandler(JavaPlugin javaPlugin) {
        if (HooksHandler.isEnabled(HookPlugin.WORLDGUARD)) {
            this.register(javaPlugin, new WorldGuardCheck());
        }
        if (HooksHandler.isEnabled(HookPlugin.SLIMEFUN)) {
            this.register(javaPlugin, new SlimeFunCheck());
        }
        if (HooksHandler.isEnabled(HookPlugin.FACTIONS)) {
            this.register(javaPlugin, new FactionsCheck());
        }
        if (HooksHandler.isEnabled(HookPlugin.LANDS)) {
            this.register(javaPlugin, new LandsCheck());
        }
        if (HooksHandler.isEnabled(HookPlugin.PROTECTIONSTONES)) {
            this.register(javaPlugin, new ProtectionStonesCheck());
        }
        this.register(javaPlugin, new GlobalProtCheck());
    }

    public void register(JavaPlugin javaPlugin, ProtectionType protectionType) {
        if (!javaPlugin.equals((Object)ASManager.getInstance())) {
            ASManager.getInstance().getLogger().info(javaPlugin.getName() + " register a new protection check: " + protectionType.getName());
        }
        this.protectionMap.put(protectionType.getName(), protectionType);
    }

    public boolean canBreak(Location location, Player player) {
        boolean bl = this.protectionMap.values().stream().allMatch(protectionType -> protectionType.canBreak(player, location));
        ASManager.debug("[LAND PROT] Can " + player.getName() + " break at " + new LocalLocation(location).getEncode() + "? " + bl);
        return bl;
    }

    public boolean canAttack(Player player, Player player2) {
        return this.protectionMap.values().stream().anyMatch(protectionType -> !protectionType.canAttack(player, player2));
    }

    public boolean isProtected(Location location) {
        return this.protectionMap.values().stream().anyMatch(protectionType -> protectionType.isProtected(location));
    }
}

