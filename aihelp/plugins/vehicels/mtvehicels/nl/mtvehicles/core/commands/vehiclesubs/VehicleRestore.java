/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.events.inventory.RestoreMenuOpenEvent;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.utils.MenuUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VehicleRestore
extends MTVSubCommand {
    public VehicleRestore() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        if (!this.checkPermission("mtvehicles.restore")) {
            return true;
        }
        this.sendMessage(Message.MENU_OPEN);
        if (this.arguments.length != 2) {
            MenuUtils.restoreCMD(this.player, 1, null);
            MenuUtils.restoreUUID.put(this.player, null);
            return true;
        }
        Player argPlayer = Bukkit.getPlayer((String)this.arguments[1]);
        if (argPlayer == null || !argPlayer.hasPlayedBefore()) {
            this.sendMessage(Message.OFFLINE_PLAYER_NOT_FOUND);
            return true;
        }
        RestoreMenuOpenEvent api = new RestoreMenuOpenEvent(this.player);
        api.call();
        if (api.isCancelled()) {
            return true;
        }
        MenuUtils.restoreCMD(this.player, 1, argPlayer.getUniqueId());
        MenuUtils.restoreUUID.put(this.player, argPlayer.getUniqueId());
        MenuUtils.restorePage.put(this.player, 1);
        return true;
    }
}

