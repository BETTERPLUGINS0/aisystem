/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 */
package com.andrei1058.bedwars.lobbysocket;

import com.andrei1058.bedwars.lobbysocket.LoadedUser;
import com.andrei1058.bedwars.support.preloadedparty.PreLoadedParty;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class LoadedUsersCleaner
implements Runnable {
    private final List<LoadedUser> toRemove = new LinkedList<LoadedUser>();

    @Override
    public void run() {
        for (LoadedUser lu : LoadedUser.getLoaded().values()) {
            if (!lu.isTimedOut()) continue;
            this.toRemove.add(lu);
        }
        if (!this.toRemove.isEmpty()) {
            this.toRemove.forEach(c -> {
                PreLoadedParty plp;
                OfflinePlayer op = Bukkit.getOfflinePlayer((UUID)c.getUuid());
                if (op != null && op.getName() != null && (plp = PreLoadedParty.getPartyByOwner(op.getName())) != null) {
                    plp.clean();
                }
                c.destroy("Removed by cleaner task.");
            });
            this.toRemove.clear();
        }
    }
}

