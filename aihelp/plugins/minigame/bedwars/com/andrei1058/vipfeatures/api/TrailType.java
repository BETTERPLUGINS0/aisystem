/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.vipfeatures.api;

public enum TrailType {
    NONE(null, ""),
    FIRE("FLAME", "vipfeatures.tails.fire"),
    SLIME("SLIME", "vipfeatures.trails.slime"),
    WATER("WATER_SPLASH", "vipfeatures.trails.water"),
    NOTES("NOTE", "vipfeatures.trails.notes"),
    CRYSTAL("CRIT_MAGIC", "vipfeatures.trails.crystal");

    private final String particle;
    private final String permission;

    private TrailType(String particle, String permission) {
        this.particle = particle;
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }

    public String getParticle() {
        return this.particle;
    }
}

