/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.support.version.v1_20_R1.despawnable;

import com.andrei1058.bedwars.support.version.v1_20_R1.despawnable.DespawnableType;

@Deprecated
public record DespawnableAttributes(DespawnableType type, double speed, double health, double damage, int despawnSeconds) {
}

