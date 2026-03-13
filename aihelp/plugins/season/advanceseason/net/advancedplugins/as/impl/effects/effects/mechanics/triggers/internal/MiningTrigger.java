/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import java.util.ArrayList;
import java.util.List;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.CropUtils;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.plugins.ItemsAdderHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.McMMOHook;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class MiningTrigger
extends AdvancedTrigger {
    public MiningTrigger() {
        super("MINING");
        this.setDescription("Activates when block is broken");
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
        Player player = blockBreakEvent.getPlayer();
        if (player.isDead() || !player.isValid()) {
            return;
        }
        Block block = blockBreakEvent.getBlock();
        if (!blockBreakEvent.isDropItems()) {
            if (!HooksHandler.isEnabled(HookPlugin.ITEMSADDER)) {
                return;
            }
            if (!((ItemsAdderHook)HooksHandler.getHook(HookPlugin.ITEMSADDER)).isCustomBlock(block)) {
                return;
            }
        }
        if (block.hasMetadata("blockbreakevent-ignore") || HooksHandler.getHook(HookPlugin.MCMMO) != null && ((McMMOHook)HooksHandler.getHook(HookPlugin.MCMMO)).isFakeBlockBreak((Event)blockBreakEvent)) {
            return;
        }
        if (block.hasMetadata("cancelBreak")) {
            blockBreakEvent.setCancelled(true);
            return;
        }
        if (block.hasMetadata("ae-skip-trigger")) {
            block.removeMetadata("ae-skip-trigger", (Plugin)EffectsHandler.getInstance());
            blockBreakEvent.setCancelled(true);
            return;
        }
        Material material = block.getDrops(player.getItemInHand()).stream().findFirst().orElse(new ItemStack(Material.AIR)).getType();
        switch (material.name()) {
            case "WHEAT": 
            case "CROPS": {
                material = !MinecraftVersion.isNew() ? Material.valueOf((String)"SEEDS") : Material.WHEAT_SEEDS;
                break;
            }
            case "CARROT_ITEM": {
                material = Material.valueOf((String)"CARROT");
                break;
            }
            case "POTATO_ITEM": {
                material = Material.valueOf((String)"POTATO");
                break;
            }
            case "TORCHFLOWER": {
                material = Material.valueOf((String)"TORCHFLOWER_SEEDS");
                break;
            }
            case "PITCHER_CROP": {
                material = Material.valueOf((String)"PITCHER_POD");
            }
        }
        for (StackItem stackItem : GetAllRollItems.getMainItems((LivingEntity)blockBreakEvent.getPlayer())) {
            if (stackItem.getRollItemType() == RollItemType.OFFHAND) continue;
            ArrayList<ItemStack> arrayList = new ArrayList<ItemStack>(block.getDrops(stackItem.getItem()));
            if ((material == Material.POTATO || material == Material.CARROT) && arrayList.size() == 2) {
                arrayList.remove(1);
            }
            this.executionBuilder().setAttacker((LivingEntity)blockBreakEvent.getPlayer()).setAttackerMain(true).setBlock(blockBreakEvent.getBlock()).addDrops(blockBreakEvent.getBlock(), (List<ItemStack>)arrayList).setEvent((Event)blockBreakEvent).setStackItem(stackItem).processVariables("%block type%;" + ASManager.getBlockMaterial(blockBreakEvent.getBlock()), "%is crop%;" + CropUtils.isCrop(blockBreakEvent.getBlock().getType()), "%is fully grown%;" + CropUtils.isFullyGrown(blockBreakEvent.getBlock()), "%block drop type%;" + String.valueOf(material), "%block x%;" + block.getX(), "%exp%;" + blockBreakEvent.getExpToDrop(), "%block y%;" + block.getY(), "%block z%;" + block.getZ(), "%block location%;" + ((double)block.getX() + 0.5) + "|" + ((double)block.getY() + 0.5) + "|" + ((double)block.getZ() + 0.5)).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

