/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  dev.lone.itemsadder.api.CustomBlock
 *  dev.lone.itemsadder.api.CustomStack
 *  dev.lone.itemsadder.api.Events.CustomBlockBreakEvent
 *  dev.lone.itemsadder.api.FontImages.FontImageWrapper
 *  dev.lone.itemsadder.api.ItemsAdder
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.permissions.Permissible
 *  org.bukkit.plugin.Plugin
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.ItemsAdder;
import java.util.Collections;
import java.util.List;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;

public class ItemsAdderHook
extends PluginHookInstance
implements Listener {
    private final Plugin plugin;

    public ItemsAdderHook(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.ITEMSADDER.getPluginName();
    }

    public boolean isCustomItem(ItemStack itemStack) {
        return CustomStack.byItemStack((ItemStack)itemStack) != null;
    }

    public boolean isCustomBlock(Block block) {
        if (block == null) {
            return false;
        }
        return CustomBlock.byAlreadyPlaced((Block)block) != null;
    }

    public boolean removeBlock(Block block) {
        if (!this.isCustomBlock(block)) {
            return false;
        }
        return CustomBlock.byAlreadyPlaced((Block)block).remove();
    }

    public List<ItemStack> getLootForCustomBlock(Block block) {
        if (!this.isCustomBlock(block)) {
            return Collections.emptyList();
        }
        return CustomBlock.byAlreadyPlaced((Block)block).getLoot();
    }

    public List<ItemStack> getLootForCustomBlock(ItemStack itemStack, Block block) {
        if (!this.isCustomBlock(block)) {
            return Collections.emptyList();
        }
        return CustomBlock.byAlreadyPlaced((Block)block).getLoot(itemStack, false);
    }

    public ItemStack setCustomItemDurability(ItemStack itemStack, int n) {
        CustomStack customStack = CustomStack.byItemStack((ItemStack)itemStack);
        customStack.setDurability(n);
        return customStack.getItemStack();
    }

    public boolean canBeBrokenWith(ItemStack itemStack, Block block) {
        return !CustomBlock.byAlreadyPlaced((Block)block).getLoot(itemStack, false).isEmpty();
    }

    public ItemStack getByName(String string) {
        CustomStack customStack = CustomStack.getInstance((String)string);
        if (customStack == null) {
            return null;
        }
        return customStack.getItemStack();
    }

    public int getCustomItemDurability(ItemStack itemStack) {
        return ItemsAdder.getCustomItemDurability((ItemStack)itemStack);
    }

    public int getCustomItemMaxDurability(ItemStack itemStack) {
        return ItemsAdder.getCustomItemMaxDurability((ItemStack)itemStack);
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
    public void onCustomBlockBreak(CustomBlockBreakEvent customBlockBreakEvent) {
        Player player = customBlockBreakEvent.getPlayer();
        Block block = customBlockBreakEvent.getBlock();
        if (block == null) {
            return;
        }
        if (block.hasMetadata("telepathy-broken-itemsadder")) {
            block.removeMetadata("telepathy-broken-itemsadder", this.plugin);
            customBlockBreakEvent.setCancelled(true);
            SchedulerUtils.runTaskLater(() -> {
                CustomBlock customBlock = CustomBlock.byAlreadyPlaced((Block)block);
                if (customBlock == null) {
                    return;
                }
                ItemStack[] itemStackArray = customBlock.getLoot(player.getEquipment().getItemInMainHand(), false).toArray(new ItemStack[0]);
                if (customBlock.remove()) {
                    ASManager.giveItem(player, itemStackArray);
                }
            });
        }
    }

    public String replaceFontImages(String string) {
        return FontImageWrapper.replaceFontImages((String)string);
    }

    public String replaceFontImages(Player player, String string) {
        return FontImageWrapper.replaceFontImages((Permissible)player, (String)string);
    }
}

