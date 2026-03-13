/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.seasons.visuals.type;

import net.advancedplugins.seasons.enums.SeasonType;
import org.bukkit.entity.Player;

public interface IVisualType {
    public void tick();

    public void tickSync();

    public SeasonType getType();

    public boolean isEnabled();

    public void setEnabled(boolean var1);

    public void activate(Player var1);
}

