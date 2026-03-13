/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  com.massivecraft.factions.iface.RelationParticipator
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.iface.RelationParticipator;
import java.lang.reflect.Method;
import net.advancedplugins.as.impl.utils.hooks.factions.FactionsPluginHook;
import org.bukkit.entity.Player;

public class FactionsUUIDHook
extends FactionsPluginHook {
    private final Method relationToLocationMethod = FPlayer.class.getMethod("getRelationToLocation", new Class[0]);
    private final Method relationMethod = FPlayer.class.getMethod("getRelationTo", RelationParticipator.class);

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "FactionsUUID";
    }

    @Override
    public String getRelation(Player player, Player player2) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        FPlayer fPlayer2 = FPlayers.getInstance().getByPlayer(player2);
        return this.relationMethod.invoke(fPlayer, fPlayer2).toString();
    }

    @Override
    public String getRelationOfLand(Player player) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        return this.relationToLocationMethod.invoke(fPlayer, new Object[0]).toString();
    }
}

