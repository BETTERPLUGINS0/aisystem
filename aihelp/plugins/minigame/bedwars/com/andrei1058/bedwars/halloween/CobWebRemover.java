/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.block.Block
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.halloween;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public class CobWebRemover {
    private static final LinkedHashMap<IArena, CobWebRemover> taskByArena = new LinkedHashMap();
    private int taskId;
    private IArena arena;
    private LinkedHashMap<Block, Long> cobWebs = new LinkedHashMap();

    protected CobWebRemover(IArena arena) {
        taskByArena.remove(arena);
        taskByArena.put(arena, this);
        this.arena = arena;
        this.taskId = Bukkit.getScheduler().runTaskTimer((Plugin)BedWars.plugin, (Runnable)new RemovalTask(), 20L, 20L).getTaskId();
    }

    public void addCobWeb(Block block) {
        this.cobWebs.put(block, System.currentTimeMillis() + 7500L);
    }

    public int getTaskId() {
        return this.taskId;
    }

    public IArena getArena() {
        return this.arena;
    }

    public static CobWebRemover getByArena(IArena arena) {
        return taskByArena.get(arena);
    }

    public static CobWebRemover getByArenaWorld(String world) {
        Optional<Map.Entry> entry = taskByArena.entrySet().stream().filter(arena -> ((IArena)arena.getKey()).getWorldName().equals(world)).findFirst();
        return entry.map(Map.Entry::getValue).orElse(null);
    }

    public void destroy() {
        Bukkit.getScheduler().cancelTask(this.getTaskId());
        taskByArena.remove(this.arena);
    }

    private class RemovalTask
    implements Runnable {
        private final LinkedList<Block> toBeRemoved = new LinkedList();

        private RemovalTask() {
        }

        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            CobWebRemover.this.cobWebs.forEach((key, value) -> {
                if (value <= currentTime) {
                    this.toBeRemoved.add((Block)key);
                    if (key.getType().toString().contains("WEB")) {
                        key.breakNaturally();
                    }
                }
            });
            this.toBeRemoved.forEach(block -> CobWebRemover.this.cobWebs.remove(block));
            this.toBeRemoved.clear();
        }
    }
}

