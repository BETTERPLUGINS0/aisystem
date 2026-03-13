/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package io.papermc.lib.environments;

import io.papermc.lib.environments.CraftBukkitEnvironment;

public class SpigotEnvironment
extends CraftBukkitEnvironment {
    @Override
    public String getName() {
        return "Spigot";
    }

    @Override
    public boolean isSpigot() {
        return true;
    }
}

