/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.arena.tasks;

import com.andrei1058.bedwars.api.arena.generator.IGenerator;
import com.andrei1058.bedwars.arena.OreGenerator;

public class OneTick
implements Runnable {
    @Override
    public void run() {
        for (IGenerator iGenerator : OreGenerator.getRotation()) {
            iGenerator.rotate();
        }
    }
}

