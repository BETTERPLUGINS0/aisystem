/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.player.PlayerEvent
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.effects.armorutils;

import net.advancedplugins.as.impl.effects.armorutils.ArmorType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ArmorEquipEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final EquipMethod equipType;
    private final ArmorType type;
    private boolean cancel = false;
    private ItemStack oldArmorPiece;
    private ItemStack newArmorPiece;
    private boolean sendMessage = true;
    private final long called;

    public ArmorEquipEvent(Player player, EquipMethod equipMethod, ArmorType armorType, ItemStack itemStack, ItemStack itemStack2) {
        super(player);
        this.equipType = equipMethod;
        this.type = armorType;
        this.oldArmorPiece = itemStack;
        this.newArmorPiece = itemStack2;
        this.called = System.currentTimeMillis();
    }

    public ArmorEquipEvent(Player player, EquipMethod equipMethod, ArmorType armorType, ItemStack itemStack, ItemStack itemStack2, boolean bl) {
        this(player, equipMethod, armorType, itemStack, itemStack2);
        this.sendMessage = bl;
    }

    public Long getCalled() {
        return this.called;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    public final HandlerList getHandlers() {
        return handlers;
    }

    public final boolean isCancelled() {
        return this.cancel;
    }

    public final void setCancelled(boolean bl) {
        this.cancel = bl;
    }

    public final ArmorType getType() {
        return this.type;
    }

    public final ItemStack getOldArmorPiece() {
        return this.oldArmorPiece;
    }

    public final void setOldArmorPiece(ItemStack itemStack) {
        this.oldArmorPiece = itemStack;
    }

    public final ItemStack getNewArmorPiece() {
        return this.newArmorPiece;
    }

    public final void setNewArmorPiece(ItemStack itemStack) {
        this.newArmorPiece = itemStack;
    }

    public EquipMethod getMethod() {
        return this.equipType;
    }

    public boolean isSendMessage() {
        return this.sendMessage;
    }

    public void setSendMessage(boolean bl) {
        this.sendMessage = bl;
    }

    public static enum EquipMethod {
        SHIFT_CLICK,
        DRAG,
        PICK_DROP,
        HOTBAR,
        HOTBAR_SWAP,
        OFFHAND_SWAP,
        DISPENSER,
        BROKE,
        DEATH,
        WORLD_CHANGE,
        COMMAND;

    }
}

