/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.BrewerInventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.PotionMeta
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionData
 *  org.bukkit.potion.PotionType
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class BrewPotionTrigger
extends AdvancedTrigger {
    private final NamespacedKey antiAbuseKey = new NamespacedKey((Plugin)ASManager.getInstance(), "potion");

    public BrewPotionTrigger() {
        super("BREW_POTION");
        this.setDescription("Activates when potion is brewed");
    }

    @EventHandler
    public void onPotionBrew(InventoryClickEvent inventoryClickEvent) {
        if (inventoryClickEvent.getClickedInventory() == null || !(inventoryClickEvent.getClickedInventory() instanceof BrewerInventory)) {
            return;
        }
        Player player = (Player)inventoryClickEvent.getWhoClicked();
        this.handleItem(player, inventoryClickEvent.getCurrentItem(), inventoryClickEvent);
    }

    private void handleItem(Player player, ItemStack itemStack, InventoryClickEvent inventoryClickEvent) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        if (persistentDataContainer.has(this.antiAbuseKey, PersistentDataType.STRING)) {
            return;
        }
        PotionData potionData = this.getPotionData(itemStack);
        if (potionData == null) {
            return;
        }
        PotionType potionType = potionData.getType();
        if (potionType == PotionType.WATER) {
            return;
        }
        boolean bl = potionData.isExtended();
        boolean bl2 = potionData.isUpgraded();
        this.executionBuilder().setAttacker((LivingEntity)player).setAttackerMain(true).setEvent((Event)inventoryClickEvent).setItem(itemStack).setItemType(RollItemType.OTHER).processVariables("%potion type%;" + potionType.toString(), "%is extended%;" + bl, "%is upgraded%;" + bl2).buildAndExecute();
        persistentDataContainer.set(this.antiAbuseKey, PersistentDataType.STRING, (Object)"claimed");
        itemStack.setItemMeta(itemMeta);
    }

    private PotionData getPotionData(ItemStack itemStack) {
        if (!itemStack.getType().toString().contains("POTION")) {
            return null;
        }
        PotionMeta potionMeta = (PotionMeta)itemStack.getItemMeta();
        return potionMeta.getBasePotionData();
    }
}

