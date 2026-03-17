/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.menu;

import java.util.EnumSet;
import me.zombie_striker.qav.ItemFact;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.gui.components.InteractionModifier;
import me.zombie_striker.qav.gui.guis.GuiItem;
import me.zombie_striker.qav.gui.guis.PaginatedGui;
import me.zombie_striker.qav.util.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Menu
extends PaginatedGui {
    protected final Player player;

    public Menu(int n, @NotNull String string, @NotNull Player player) {
        super(n, (n - 1) * 9, ChatColor.translateAlternateColorCodes((char)'&', (String)string), EnumSet.noneOf(InteractionModifier.class));
        this.player = player;
        this.setDefaultClickAction(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
    }

    public abstract void setupItems();

    public void open() {
        this.open((HumanEntity)this.player);
    }

    @Override
    public void open(@NotNull HumanEntity humanEntity, int n) {
        this.setupItems();
        super.open(humanEntity, n);
    }

    public void setPageButtons() {
        this.setItem(this.getRows(), 4, new GuiItem(ItemFact.a(XMaterial.STONE_BUTTON.parseMaterial(), MessagesConfig.PREV_PAGE, new String[0]), inventoryClickEvent -> this.previous()));
        this.setItem(this.getRows(), 6, new GuiItem(ItemFact.a(XMaterial.STONE_BUTTON.parseMaterial(), MessagesConfig.PREV_PAGE, new String[0]), inventoryClickEvent -> this.next()));
    }

    public Player getPlayer() {
        return this.player;
    }
}

