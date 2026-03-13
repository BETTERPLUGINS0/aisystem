/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.MathUtils;
import net.advancedplugins.as.impl.utils.Pair;
import net.advancedplugins.as.impl.utils.ReallyFastBlockHandler;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.factions.FactionsPluginHook;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class WaterWalkerEffect
extends AdvancedEffect {
    public static final HashMap<UUID, Integer> ACTIVATED_USERS = new HashMap();
    private static final Map<Integer, Pair<Block[], Material>> removalTasks = new HashMap<Integer, Pair<Block[], Material>>();

    public WaterWalkerEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "WATER_WALKER", "Walk on water", "%e:<RADIUS>");
        this.addArgument(0, Integer.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (!executionTask.getBuilder().isPermanent()) {
            this.warn(this.getName() + " can only be used as permanent effect.");
            return false;
        }
        if (executionTask.getBuilder().isRemoved()) {
            ACTIVATED_USERS.remove(livingEntity.getUniqueId());
            return true;
        }
        int n = stringArray.length > 0 ? ASManager.parseInt(stringArray[0], 2) : 2;
        n = MathUtils.clamp(n, 2, 10);
        ACTIVATED_USERS.put(livingEntity.getUniqueId(), n);
        return true;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent playerMoveEvent) {
        if (ACTIVATED_USERS.isEmpty()) {
            return;
        }
        if (playerMoveEvent.getPlayer().getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        if (playerMoveEvent.getPlayer().isFlying()) {
            return;
        }
        if (playerMoveEvent.getFrom().getBlock().equals((Object)playerMoveEvent.getTo().getBlock())) {
            return;
        }
        if (ASManager.sameBlock(playerMoveEvent.getFrom(), playerMoveEvent.getTo())) {
            return;
        }
        if (!ACTIVATED_USERS.containsKey(playerMoveEvent.getPlayer().getUniqueId())) {
            return;
        }
        int n = ACTIVATED_USERS.get(playerMoveEvent.getPlayer().getUniqueId());
        this.scanBlocks(playerMoveEvent.getPlayer(), playerMoveEvent.getTo(), n);
    }

    private void scanBlocks(Player player, Location location, int n) {
        Location location2 = location.clone().subtract(0.0, 1.0, 0.0);
        Block block = location2.getBlock();
        ArrayList<Block> arrayList = new ArrayList<Block>();
        if (n <= 2) {
            arrayList.add(block);
            arrayList.add(block.getRelative(BlockFace.NORTH));
            arrayList.add(block.getRelative(BlockFace.EAST));
            arrayList.add(block.getRelative(BlockFace.SOUTH));
            arrayList.add(block.getRelative(BlockFace.WEST));
        } else {
            int n2 = location2.getBlockX();
            int n3 = location2.getBlockY();
            int n4 = location2.getBlockZ();
            World world = location2.getWorld();
            int n5 = n * n;
            for (int i = n2 - n; i <= n2 + n; ++i) {
                for (int j = n4 - n; j <= n4 + n; ++j) {
                    if ((n2 - i) * (n2 - i) + (n4 - j) * (n4 - j) > n5) continue;
                    arrayList.add(world.getBlockAt(i, n3, j));
                }
            }
        }
        this.checkAndSet(player, arrayList, "WATER");
    }

    private void checkAndSet(Player player, List<Block> list, String string) {
        Block[] blockArray;
        if (HooksHandler.isEnabled(HookPlugin.FACTIONS) && !(blockArray = ((FactionsPluginHook)HooksHandler.getHook(HookPlugin.FACTIONS)).getRelationOfLand(player)).equalsIgnoreCase("Wilderness") && !blockArray.equalsIgnoreCase("null") && !blockArray.equalsIgnoreCase("neutral")) {
            return;
        }
        list.removeIf(block -> !block.isLiquid() || !block.getType().name().endsWith(string) || block.getData() != 0 || !EffectsHandler.getProtection().canBreak(block.getLocation(), player) || block.getRelative(BlockFace.UP).getType().name().equals("LILY_PAD"));
        if (list.isEmpty()) {
            return;
        }
        for (Block block2 : list) {
            block2.setMetadata("cancelBreak", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)true));
        }
        blockArray = list.toArray(new Block[0]);
        this.removeBlockLater(blockArray, list.get(0).getType());
        ReallyFastBlockHandler.getForWorld(player.getWorld()).setType(Material.OBSIDIAN, blockArray);
    }

    private void removeBlockLater(Block[] blockArray, Material material) {
        int n = Bukkit.getScheduler().runTaskLater((Plugin)EffectsHandler.getInstance(), () -> {
            ReallyFastBlockHandler.getForWorld(blockArray[0].getWorld()).setType(material, blockArray);
            for (Block block : blockArray) {
                block.getChunk().load(true);
                block.removeMetadata("cancelBreak", (Plugin)EffectsHandler.getInstance());
            }
        }, 100L).getTaskId();
        removalTasks.remove(n);
    }

    public static void clearQueue() {
        removalTasks.forEach((n, pair) -> {
            Bukkit.getScheduler().cancelTask(n.intValue());
            ReallyFastBlockHandler.getForWorld(((Block[])pair.getKey())[0].getWorld()).setType((Material)pair.getValue(), (Block[])pair.getKey());
        });
    }
}

