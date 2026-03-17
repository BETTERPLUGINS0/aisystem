/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.libs.inventory.content;

import nl.sbdeveloper.vehiclesplus.libs.inventory.content.InventoryContents;
import org.bukkit.entity.Player;

public interface InventoryProvider {
    public void init(Player var1, InventoryContents var2);

    default public void update(Player player, InventoryContents contents) {
    }
}

