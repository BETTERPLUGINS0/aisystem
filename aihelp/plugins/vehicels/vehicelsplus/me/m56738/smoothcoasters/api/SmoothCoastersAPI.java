/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package me.m56738.smoothcoasters.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.m56738.smoothcoasters.api.DefaultNetworkInterface;
import me.m56738.smoothcoasters.api.Feature;
import me.m56738.smoothcoasters.api.NetworkInterface;
import me.m56738.smoothcoasters.api.PlayerEntry;
import me.m56738.smoothcoasters.api.PlayerListener;
import me.m56738.smoothcoasters.api.implementation.ImplV4;
import me.m56738.smoothcoasters.api.implementation.ImplV5;
import me.m56738.smoothcoasters.api.implementation.Implementation;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SmoothCoastersAPI {
    private final Plugin plugin;
    private final PlayerListener playerListener;
    private final Map<Byte, Implementation> implementations = new HashMap<Byte, Implementation>();
    private final Map<UUID, PlayerEntry> players = new ConcurrentHashMap<UUID, PlayerEntry>();
    private final NetworkInterface defaultNetwork;

    public SmoothCoastersAPI(Plugin plugin) {
        this.plugin = plugin;
        this.playerListener = new PlayerListener(this);
        this.defaultNetwork = new DefaultNetworkInterface(plugin);
        this.registerImplementation(new ImplV4(plugin));
        this.registerImplementation(new ImplV5(plugin));
    }

    public void registerImplementation(Implementation implementation) {
        this.implementations.put(implementation.getVersion(), implementation);
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    Map<Byte, Implementation> getImplementations() {
        return this.implementations;
    }

    PlayerEntry getEntry(Player player) {
        return this.players.get(player.getUniqueId());
    }

    PlayerEntry getOrCreateEntry(Player player) {
        return this.players.computeIfAbsent(player.getUniqueId(), uUID -> new PlayerEntry());
    }

    void removeEntry(Player player) {
        this.players.remove(player.getUniqueId());
    }

    private Implementation getImplementation(Player player) {
        PlayerEntry playerEntry = this.getEntry(player);
        if (playerEntry == null) {
            return null;
        }
        return playerEntry.getImplementation();
    }

    public boolean isEnabled(Player player) {
        return this.getImplementation(player) != null;
    }

    public byte getVersion(Player player) {
        Implementation implementation = this.getImplementation(player);
        if (implementation != null) {
            return implementation.getVersion();
        }
        return -1;
    }

    public String getModVersion(Player player) {
        PlayerEntry playerEntry = this.getEntry(player);
        if (playerEntry != null) {
            return playerEntry.getVersion();
        }
        return null;
    }

    public boolean isSupported(Player player, Feature feature) {
        Implementation implementation = this.getImplementation(player);
        if (implementation == null) {
            return false;
        }
        return implementation.isSupported(feature);
    }

    public boolean resetRotation(NetworkInterface networkInterface, Player player) {
        return this.setRotation(networkInterface, player, 0.0f, 0.0f, 0.0f, 1.0f, (byte)0);
    }

    public boolean setRotation(NetworkInterface networkInterface, Player player, float f, float f2, float f3, float f4, byte by) {
        Implementation implementation;
        if (networkInterface == null) {
            networkInterface = this.defaultNetwork;
        }
        if ((implementation = this.getImplementation(player)) == null || !implementation.isSupported(Feature.ROTATION)) {
            return false;
        }
        implementation.sendRotation(networkInterface, player, f, f2, f3, f4, by);
        return true;
    }

    public boolean setEntityRotation(NetworkInterface networkInterface, Player player, int n, float f, float f2, float f3, float f4, byte by) {
        Implementation implementation;
        if (networkInterface == null) {
            networkInterface = this.defaultNetwork;
        }
        if ((implementation = this.getImplementation(player)) == null || !implementation.isSupported(Feature.ENTITY_ROTATION)) {
            return false;
        }
        implementation.sendEntityRotation(networkInterface, player, n, f, f2, f3, f4, by);
        return true;
    }

    public boolean setEntityLerpTicks(NetworkInterface networkInterface, Player player, int n, byte by) {
        Implementation implementation;
        if (networkInterface == null) {
            networkInterface = this.defaultNetwork;
        }
        if ((implementation = this.getImplementation(player)) == null || !implementation.isSupported(Feature.ENTITY_PROPERTIES)) {
            return false;
        }
        implementation.sendEntityProperties(networkInterface, player, n, by);
        return true;
    }

    public boolean setRotationLimit(NetworkInterface networkInterface, Player player, float f, float f2, float f3, float f4) {
        Implementation implementation;
        if (networkInterface == null) {
            networkInterface = this.defaultNetwork;
        }
        if ((implementation = this.getImplementation(player)) == null || !implementation.isSupported(Feature.ROTATION_LIMIT)) {
            return false;
        }
        implementation.sendRotationLimit(networkInterface, player, f, f2, f3, f4);
        return true;
    }

    public boolean resetRotationLimit(NetworkInterface networkInterface, Player player) {
        return this.setRotationLimit(networkInterface, player, -180.0f, 180.0f, -90.0f, 90.0f);
    }

    public void unregister() {
        this.implementations.clear();
        this.playerListener.unregister();
        this.players.clear();
    }
}

