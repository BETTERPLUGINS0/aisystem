/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 */
package net.advancedplugins.as.impl.utils.menus.item;

import net.advancedplugins.as.impl.utils.menus.AdvancedMenu;
import net.advancedplugins.as.impl.utils.menus.item.AdvancedMenuItem;
import net.advancedplugins.as.impl.utils.menus.item.ClickAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface ClickActionArgs
extends ClickAction {
    public void onClick(Player var1, AdvancedMenu var2, AdvancedMenuItem var3, int var4, ClickType var5, String var6);
}

