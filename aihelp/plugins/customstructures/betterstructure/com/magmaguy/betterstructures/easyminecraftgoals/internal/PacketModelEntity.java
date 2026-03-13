/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.util.EulerAngle
 */
package com.magmaguy.betterstructures.easyminecraftgoals.internal;

import com.magmaguy.betterstructures.easyminecraftgoals.internal.AbstractPacketBundle;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketEntityInterface;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.util.EulerAngle;

public interface PacketModelEntity
extends PacketEntityInterface {
    public void sendLocationAndRotationPacket(Location var1, EulerAngle var2);

    public void sendLocationAndRotationAndScalePacket(Location var1, EulerAngle var2, float var3);

    default public void sendLocationAndRotationAndScalePacket(Location location, EulerAngle eulerAngle, float scaleX, float scaleY, float scaleZ) {
        this.sendLocationAndRotationAndScalePacket(location, eulerAngle, scaleX);
    }

    public AbstractPacketBundle generateLocationAndRotationAndScalePackets(AbstractPacketBundle var1, Location var2, EulerAngle var3, float var4);

    default public AbstractPacketBundle generateLocationAndRotationAndScalePackets(AbstractPacketBundle packetBundle, Location location, EulerAngle eulerAngle, float scaleX, float scaleY, float scaleZ) {
        return this.generateLocationAndRotationAndScalePackets(packetBundle, location, eulerAngle, scaleX);
    }

    default public void initializeModel(Location location, int modelID) {
        throw new UnsupportedOperationException("Integer modelID not supported by this implementation.");
    }

    default public void initializeModel(Location location, String modelID) {
        throw new UnsupportedOperationException("String modelID not supported by this implementation.");
    }

    public void setScale(float var1);

    public void setHorseLeatherArmorColor(Color var1);

    @Override
    public boolean hasViewers();
}

