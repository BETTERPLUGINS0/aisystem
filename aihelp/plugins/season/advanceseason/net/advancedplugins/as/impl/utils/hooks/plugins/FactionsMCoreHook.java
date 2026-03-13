/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  com.massivecraft.factions.iface.RelationParticipator
 *  com.massivecraft.factions.perms.Relation
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import net.advancedplugins.as.impl.utils.hooks.factions.FactionsPluginHook;
import org.bukkit.entity.Player;

public class FactionsMCoreHook
extends FactionsPluginHook {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "FactionsMCore";
    }

    @Override
    public String getRelation(Player player, Player player2) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        FPlayer fPlayer2 = FPlayers.getInstance().getByPlayer(player2);
        return fPlayer.getRelationTo((RelationParticipator)fPlayer2).toString();
    }

    @Override
    public String getRelationOfLand(Player player) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        Relation relation = fPlayer.getRelationToLocation();
        return relation.toString();
    }
}

