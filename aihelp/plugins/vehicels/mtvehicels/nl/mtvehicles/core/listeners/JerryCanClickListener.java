/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemStack
 */
package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleFuel;
import nl.mtvehicles.core.events.JerryCanClickEvent;
import nl.mtvehicles.core.infrastructure.annotations.VersionSpecific;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class JerryCanClickListener
extends MTVListener {
    @EventHandler
    public void onJerryCanClick(PlayerInteractEvent event) {
        NBTItem nbt;
        this.event = event;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) {
            return;
        }
        try {
            nbt = new NBTItem(item);
            if (!nbt.hasKey("mtvehicles.benzinesize").booleanValue()) {
                return;
            }
        } catch (Exception e) {
            return;
        }
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || event.getHand() != EquipmentSlot.HAND) {
            event.getPlayer().sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.WRONG_HAND)));
            return;
        }
        this.player = event.getPlayer();
        int currentFuel = Integer.parseInt(nbt.getString("mtvehicles.benzineval"));
        int maxFuel = Integer.parseInt(nbt.getString("mtvehicles.benzinesize"));
        this.setAPI(new JerryCanClickEvent(currentFuel, maxFuel));
        this.callAPI();
        if (this.isCancelled()) {
            return;
        }
        event.setCancelled(true);
        if (!ConfigModule.defaultConfig.canFillJerryCans(this.player, clickedBlock.getLocation())) {
            return;
        }
        if (this.isFuelStation(clickedBlock)) {
            if (this.player.isSneaking()) {
                this.fillWholeJerryCan(currentFuel, maxFuel);
            } else {
                this.fillJerryCan(currentFuel, maxFuel);
            }
        }
    }

    private boolean isFuelStation(Block block) {
        String blockType = block.getType().toString();
        return blockType.contains("LEVER") && ConfigModule.defaultConfig.isFillJerryCansLeverEnabled() || blockType.contains("TRIPWIRE_HOOK") && ConfigModule.defaultConfig.isFillJerryCansTripwireHookEnabled();
    }

    private void fillJerryCan(int currentFuel, int maxFuel) {
        if (currentFuel >= maxFuel) {
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.JERRYCAN_FULL);
            return;
        }
        if (this.canAffordFuel(1)) {
            this.player.setItemInHand(VehicleFuel.jerrycanItem(maxFuel, currentFuel + 1));
            this.playJerryCanSound();
        }
    }

    private void fillWholeJerryCan(int currentFuel, int maxFuel) {
        if (currentFuel >= maxFuel) {
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.JERRYCAN_FULL);
            return;
        }
        int difference = maxFuel - currentFuel;
        if (this.canAffordFuel(difference)) {
            this.player.setItemInHand(VehicleFuel.jerrycanItem(maxFuel, maxFuel));
            this.playJerryCanSound();
        }
    }

    private boolean canAffordFuel(int litres) {
        if (!ConfigModule.defaultConfig.isFillJerryCanPriceEnabled()) {
            return true;
        }
        double price = (double)litres * ConfigModule.defaultConfig.getFillJerryCanPrice();
        return DependencyModule.vault.withdrawMoneyPlayer((OfflinePlayer)this.player, price);
    }

    @VersionSpecific
    private void playJerryCanSound() {
        if (!ConfigModule.defaultConfig.jerryCanPlaySound()) {
            return;
        }
        String soundName = VersionModule.getServerVersion().is1_12() ? "BLOCK_NOTE_PLING" : "BLOCK_NOTE_BLOCK_PLING";
        try {
            this.player.getWorld().playSound(this.player.getLocation(), Sound.valueOf((String)soundName), 3.0f, 0.5f);
        } catch (IllegalArgumentException e) {
            Main.logSevere("Could not play sound '" + soundName + "'.");
            e.printStackTrace();
        }
    }
}

