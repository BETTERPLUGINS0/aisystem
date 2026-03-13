/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.advancedplugins.as.impl.effects.effects.effects.utils;

import java.util.List;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PumpkinDeathListener
implements Listener {
    private final ItemStack helmet;
    private final Material pumpkinMaterial;
    private final LivingEntity livingEntity;
    public boolean playerDied = false;

    public PumpkinDeathListener(ItemStack itemStack, Material material, int n, LivingEntity livingEntity) {
        this.helmet = itemStack;
        this.pumpkinMaterial = material;
        this.livingEntity = livingEntity;
        EffectsHandler.getInstance().getServer().getPluginManager().registerEvents((Listener)this, (Plugin)EffectsHandler.getInstance());
        new BukkitRunnable(){

            public void run() {
                HandlerList.unregisterAll((Listener)PumpkinDeathListener.this);
            }
        }.runTaskLater((Plugin)EffectsHandler.getInstance(), (long)n);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent playerDeathEvent) {
        if (playerDeathEvent.getEntity().getUniqueId() != this.livingEntity.getUniqueId()) {
            return;
        }
        if (playerDeathEvent.getKeepInventory()) {
            playerDeathEvent.getEntity().getInventory().setHelmet(this.helmet);
        } else {
            playerDeathEvent.getDrops().add(this.helmet);
            this.handlePumpkinDespawn(playerDeathEvent);
        }
        HandlerList.unregisterAll((Listener)this);
        this.playerDied = true;
    }

    @EventHandler
    public void onHelmetChange(InventoryClickEvent inventoryClickEvent) {
        if (inventoryClickEvent.getWhoClicked().getUniqueId() != this.livingEntity.getUniqueId()) {
            return;
        }
        if (inventoryClickEvent.getInventory().getType() != InventoryType.CRAFTING) {
            return;
        }
        if (inventoryClickEvent.getSlot() != 39) {
            return;
        }
        inventoryClickEvent.setCancelled(true);
    }

    private void handlePumpkinDespawn(PlayerDeathEvent playerDeathEvent) {
        List list = playerDeathEvent.getDrops();
        for (int i = 0; i <= list.size() - 1; ++i) {
            ItemStack itemStack = (ItemStack)playerDeathEvent.getDrops().get(i);
            if (itemStack.getType() != this.pumpkinMaterial) continue;
            itemStack.setAmount(itemStack.getAmount() - 1);
            playerDeathEvent.getDrops().remove(i);
            playerDeathEvent.getDrops().add(itemStack);
            break;
        }
    }
}

