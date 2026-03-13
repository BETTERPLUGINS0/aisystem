/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.arena.tasks;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.entity.Despawnable;

public class Refresh
implements Runnable {
    @Override
    public void run() {
        for (Despawnable d : BedWars.nms.getDespawnablesList().values()) {
            d.refresh();
        }
    }
}

