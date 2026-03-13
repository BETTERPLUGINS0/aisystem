/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  dev.aurelium.auraskills.api.event.loot.LootDropEvent
 *  dev.aurelium.auraskills.api.event.loot.LootDropEvent$Cause
 *  dev.aurelium.auraskills.api.event.mana.TerraformBlockBreakEvent
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Vector
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import dev.aurelium.auraskills.api.event.loot.LootDropEvent;
import dev.aurelium.auraskills.api.event.mana.TerraformBlockBreakEvent;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.advancedplugins.as.impl.utils.abilities.DropsSettings;
import net.advancedplugins.as.impl.utils.abilities.SmeltMaterial;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class AuraSkillsHook
extends PluginHookInstance
implements Listener {
    private final ConcurrentHashMap<Vector, BrokenBlockInformation> brokenBlocksMap = new ConcurrentHashMap();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.AURASKILLS.getPluginName();
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void onLoot(LootDropEvent lootDropEvent) {
        if (lootDropEvent.getCause().equals((Object)LootDropEvent.Cause.EPIC_CATCH)) {
            return;
        }
        Player player = lootDropEvent.getPlayer();
        ItemStack itemStack = lootDropEvent.getItem();
        Location location = lootDropEvent.getLocation().clone();
        Vector vector = location.getBlock().getLocation().toVector();
        ItemStack itemStack2 = itemStack;
        BrokenBlockInformation brokenBlockInformation = this.brokenBlocksMap.get(vector);
        if (brokenBlockInformation == null) {
            return;
        }
        if (!player.equals((Object)brokenBlockInformation.player)) {
            return;
        }
        if (brokenBlockInformation.settings.isSmelt()) {
            itemStack2 = SmeltMaterial.material(itemStack);
        }
        lootDropEvent.setItem(itemStack2);
        lootDropEvent.setToInventory(brokenBlockInformation.settings.isAddToInventory());
    }

    public boolean isTerraformEvent(BlockBreakEvent blockBreakEvent) {
        return blockBreakEvent instanceof TerraformBlockBreakEvent;
    }

    public void addBrokenBlockToMap(Block block, Player player, DropsSettings dropsSettings) {
        Location location = block.getLocation();
        this.brokenBlocksMap.put(location.toVector(), new BrokenBlockInformation(player, dropsSettings));
        this.executorService.schedule(() -> this.brokenBlocksMap.remove(location.toVector()), 2000L, TimeUnit.MILLISECONDS);
    }

    static class BrokenBlockInformation {
        public final Player player;
        public final DropsSettings settings;

        public BrokenBlockInformation(Player player, DropsSettings dropsSettings) {
            this.player = player;
            this.settings = dropsSettings;
        }
    }
}

