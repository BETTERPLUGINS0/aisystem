/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Item
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.Vector
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.api.server;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.server.ISetupSession;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class RestoreAdapter {
    private final Plugin plugin;

    public RestoreAdapter(Plugin owner) {
        this.plugin = owner;
    }

    public Plugin getOwner() {
        return this.plugin;
    }

    public abstract void onEnable(IArena var1);

    public abstract void onRestart(IArena var1);

    public abstract void onDisable(IArena var1);

    public abstract void onSetupSessionStart(ISetupSession var1);

    public abstract void onSetupSessionClose(ISetupSession var1);

    public void onLobbyRemoval(@NotNull IArena a) {
        this.foreachBlockInRegion(a.getConfig().getArenaLoc("waiting.Pos1"), a.getConfig().getArenaLoc("waiting.Pos2"), block -> block.setType(Material.AIR));
        Bukkit.getScheduler().runTaskLater(this.getOwner(), () -> this.clearItems(a.getWorld()), 15L);
    }

    public abstract boolean isWorld(String var1);

    public abstract void deleteWorld(String var1);

    public abstract void cloneArena(String var1, String var2);

    public abstract List<String> getWorldsList();

    public abstract void convertWorlds();

    public abstract String getDisplayName();

    public void foreachBlockInRegion(@Nullable Location corner1, @Nullable Location corner2, @NotNull Consumer<Block> consumer) {
        if (null == corner1 || null == corner2) {
            return;
        }
        Vector min = new Vector(Math.min(corner1.getBlockX(), corner2.getBlockX()), Math.min(corner1.getBlockY(), corner2.getBlockY()), Math.min(corner1.getBlockZ(), corner2.getBlockZ()));
        Vector max = new Vector(Math.max(corner1.getBlockX(), corner2.getBlockX()), Math.max(corner1.getBlockY(), corner2.getBlockY()), Math.max(corner1.getBlockZ(), corner2.getBlockZ()));
        for (int x = min.getBlockX(); x < max.getBlockX(); ++x) {
            for (int y = min.getBlockY(); y < max.getBlockY(); ++y) {
                for (int z = min.getBlockZ(); z < max.getBlockZ(); ++z) {
                    consumer.accept(corner1.getWorld().getBlockAt(x, y, z));
                }
            }
        }
    }

    public void clearItems(@NotNull World world) {
        world.getEntities().forEach(e -> {
            if (e instanceof Item) {
                e.remove();
            }
        });
    }
}

