/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package me.m56738.smoothcoasters.api.implementation;

import java.util.EnumSet;
import me.m56738.smoothcoasters.api.Feature;
import me.m56738.smoothcoasters.api.NetworkInterface;
import org.bukkit.entity.Player;

public interface Implementation {
    default public boolean isSupported(Feature feature) {
        return this.getFeatures().contains((Object)feature);
    }

    public EnumSet<Feature> getFeatures();

    public byte getVersion();

    default public void sendRotation(NetworkInterface network, Player player, float x, float y, float z, float w, byte ticks) {
        throw new UnsupportedOperationException();
    }

    default public void sendEntityRotation(NetworkInterface network, Player player, int entity, float x, float y, float z, float w, byte ticks) {
        throw new UnsupportedOperationException();
    }

    default public void sendEntityProperties(NetworkInterface network, Player player, int entity, byte ticks) {
        throw new UnsupportedOperationException();
    }

    default public void sendRotationLimit(NetworkInterface network, Player player, float minYaw, float maxYaw, float minPitch, float maxPitch) {
        throw new UnsupportedOperationException();
    }
}

