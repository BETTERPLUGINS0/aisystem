/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.hooks.factions;

import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.entity.Player;

public class FactionsPluginHook
extends PluginHookInstance {
    public String getRelation(Player player, Player player2) {
        return "NEUTRAL";
    }

    public String getRelationOfLand(Player player) {
        return "null";
    }
}

