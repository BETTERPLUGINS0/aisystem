/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.customitemmanager.pack;

import me.zombie_striker.qav.customitemmanager.pack.ResourcepackProvider;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class StaticPackProvider
implements ResourcepackProvider {
    private final String url;

    public StaticPackProvider(String string) {
        this.url = string;
    }

    @Override
    public String getFor(@Nullable Player player) {
        return this.url;
    }

    @Override
    public Object serialize() {
        return this.url;
    }
}

